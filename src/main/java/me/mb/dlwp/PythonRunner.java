package me.mb.dlwp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.Iterator;

public final class PythonRunner {

	private String lastStdOut = "";
	private Process process;

	public void runScript(String file, String... args) throws IOException,
			PythonException {
		if (!new File(file).exists())
			throw new FileNotFoundException("Script " + file
					+ " was not found.");

		ProcessBuilder pb = new ProcessBuilder(processArgsToString(
				file, args));
		pb.inheritIO();
		process = pb.start();

		BufferedReader outReader = new BufferedReader(new InputStreamReader(
				process.getInputStream()));

		StringBuilder outBuilder = new StringBuilder();

		String line = null;
		while ((line = outReader.readLine()) != null) {
			outBuilder.append(line);
			outBuilder.append(System.getProperty("line.separator"));
		}

		lastStdOut = outBuilder.toString();
		String error = getErrOut();

		try {
			process.waitFor();
		} catch (InterruptedException e) {
		}

		if (process.exitValue() > 0) {
			throw new PythonException(
					"Python returned an error. Error detail: " + error, error);
		}

	}

	private String[] processArgsToString(String file, String... args) {
		String[] newArgs = new String[args.length + 2];
		newArgs[0] = "python";
		newArgs[1] = file;
		for (int i = 0; i < args.length; i++) {
			newArgs[i+2] = args[i];
		}
		return newArgs;
	}

	public String getStdOut() {
		if (process == null)
			throw new IllegalStateException(
					"Process needs to be run before getting stdout; call runScript()");
		return lastStdOut;
	}

	private String getErrOut() throws IOException {
		BufferedReader errReader = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));

		StringBuilder errBuilder = new StringBuilder();
		String line = null;
		while ((line = errReader.readLine()) != null) {
			errBuilder.append(line);
			errBuilder.append(System.getProperty("line.separator"));
		}
		return errBuilder.toString();
	}

	@SuppressWarnings("serial")
	public static class PythonException extends Exception {
		private final String exceptionText;

		public PythonException(String message, String exceptionText) {
			super(message);
			this.exceptionText = exceptionText;
		}

		public String getExceptionText() {
			return exceptionText;
		}
	}
}
