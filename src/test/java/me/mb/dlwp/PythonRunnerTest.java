package me.mb.dlwp;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import me.mb.dlwp.PythonRunner;
import me.mb.dlwp.PythonRunner.PythonException;
import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Test;

public class PythonRunnerTest {

	@Mocked
	ProcessBuilder pb;
	@Mocked
	Process p;
	@Mocked
	BufferedReader br;
	@Mocked
	File file;
	@Mocked
	InputStream stream;

	@Test
	public void testRunPythonScriptReturnsOutput() throws IOException,
			PythonException {

		final String filename = "/home/miles/workspace/Summer Project/dlwp/src/main/pythontest.py";
				
		new Expectations() {
			{
				file = new File(anyString);
				file.exists();
				result = true;
				pb = new ProcessBuilder("python",filename);
				pb.inheritIO();
				pb.start();
				result = p;
				br.readLine();
				result = "2";
				result = null;
				p.getInputStream(); result = stream;
			}
		};

		PythonRunner pr = new PythonRunner();
		pr.runScript(filename);
		String result = pr.getStdOut();
		assertEquals("2\n", result);
	}

	@Test(expected = PythonException.class)
	public void testRunPythonScriptThrowsOnError() throws IOException,
			PythonException {
		new Expectations() {
			{
				file.exists();
				result = true;
				p.exitValue();
				result = 1;
			}
		};
		PythonRunner pr = new PythonRunner();
		String file = "/home/miles/workspace/Summer Project/dlwp/src/main/pythontest.py";
		pr.runScript(file);
	}

	@Test(expected = IOException.class)
	public void testPythonNotFoundThrowsError() throws IOException,
			PythonException {
		new Expectations() {
			{
				file.exists();
				result = true;
				new ProcessBuilder((String) any, (String) any);
				result = new IOException();
			}
		};
		PythonRunner pr = new PythonRunner();
		pr.runScript("");
	}

	@Test(expected = FileNotFoundException.class)
	public void ifPythonFileDoesntExistFNFIsThrown() throws IOException,
			PythonException {
		new Expectations() {
			{
				file.exists();
				result = false;
			}
		};
		PythonRunner pr = new PythonRunner();
		pr.runScript("");
	}

	@Test(expected = IllegalStateException.class)
	public void getStdOutFailsIfProcessNotRun() throws IOException {
		PythonRunner pr = new PythonRunner();
		pr.getStdOut();
	}

	@Test
	public void callingScriptWithArgsIsParsedCorrectly() throws IOException, PythonException {
		new Expectations() {
			{
				file.exists();
				result = true;
				new ProcessBuilder("python", "file firstArg secondArg");
			}
		};
		PythonRunner pr = new PythonRunner();
		pr.runScript("file", "firstArg", "secondArg");
	}
}
