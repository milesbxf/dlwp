package me.mb.dlwp;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.mb.dlwp.PythonRunner.PythonException;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public final class DataLogger {
	
	private static final Logger LOGGER = Logger.getLogger("DataLogger");
	
	private final Loggable loggable;
	private final List<List<Object>> data;
	
	private final int numberCols;
	
	
	public DataLogger(Loggable loggable) {
		numberCols = loggable.getHeaders().size();
		data = new LinkedList<>();
		this.loggable = loggable;
	}
	
	public List<List<Object>> getData() {
		return data;
	}
	
	public List<String> getHeaders() {
		return loggable.getHeaders();
	}
	

	public void appendData() {
		List<Object> dataToAdd = loggable.getData();
		if(dataToAdd.size() != numberCols)
			throw new IllegalArgumentException(String.format("Expected %d columns of data; got %d",numberCols,dataToAdd.size()));
		data.add(dataToAdd);
	}
	
	public void writeToCSVFile(File file) throws IOException {
		CSVWriter.CSVParamBuilder builder = new CSVWriter.CSVParamBuilder();
		builder.setHeaderNames(loggable.getHeaders());
		builder.setData(data);
		CSVWriter.writeToCSVFile(file, builder);
	}
	
	public void writeToCSVFileAndRunScript(File csvFile,String pythonScript,String... args) throws IOException, PythonException {
		writeToCSVFile(csvFile);
		PythonRunner pr = new PythonRunner();
		String[] newArgs = new String[args.length+1];
		newArgs[0] = csvFile.getCanonicalPath();
		for (int i = 0; i < args.length; i++) {
			newArgs[i+1] = args[i];
		}
		pr.runScript(pythonScript, newArgs);
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("|");
		
		for(String header : getHeaders()) {
			str.append(String.format("%10s",header)).append("\t|");
		}
		
		str.append("\n");
		
		for(List<Object> data: getData()) {
			str.append("|");
			for(Object datum: data) {
				str.append(datum).append("\t|");
			}
			str.append("\n");			
		}
		
		return str.toString();
	}
	
}
