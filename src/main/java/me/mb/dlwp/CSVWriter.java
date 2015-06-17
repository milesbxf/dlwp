package me.mb.dlwp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class CSVWriter {
	
	public static void writeToCSVFile(File file,CSVParamBuilder builder) throws IOException {
		FileOutputStream fileOS = new FileOutputStream(file);
		writeToCSVOutputStream(fileOS, builder);
	}

	public static void writeToCSVOutputStream(OutputStream outputStream,
			CSVParamBuilder builder) throws IOException {

		StringBuilder output = new StringBuilder();
		Joiner joiner = Joiner.on(builder.columnSeparator);
		output.append(joiner.join(builder.headerNames)).append("\n");
		for (List<Object> row : builder.data) {
			output.append(joiner.join(row)).append("\n");
		}

		try (OutputStreamWriter osWriter = new OutputStreamWriter(outputStream);) {
			osWriter.write(output.toString());
		}
	}

	public static class CSVParamBuilder {
		private char columnSeparator = '\t';
		private List<String> headerNames = new LinkedList<>();
		private List<List<Object>> data = new LinkedList<>();

		public CSVParamBuilder setColumnSeparator(char columnSeparator) {
			this.columnSeparator = columnSeparator;
			return this;
		}

		public CSVParamBuilder setHeaderNames(List<String> headerNames) {
			this.headerNames = headerNames;
			return this;
		}

		public CSVParamBuilder setData(List<List<Object>> data) {
			this.data = data;
			return this;
		}

		public CSVParamBuilder addDataRow(List<Object> data) {
			this.data.add(data);
			return this;
		}		

		public CSVParamBuilder addSingleData(Object data) {
			this.data.add(Lists.newArrayList(data));
			return this;
		}

		public CSVParamBuilder addHeader(String header) {
			this.headerNames.add(header);
			return this;
		}
	}
}
