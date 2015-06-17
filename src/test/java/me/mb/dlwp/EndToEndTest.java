package me.mb.dlwp;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import me.mb.dlwp.DataLogger;
import me.mb.dlwp.Loggable;
import me.mb.dlwp.PythonRunner.PythonException;

import org.junit.Test;

import com.google.common.collect.Lists;

public class EndToEndTest {

	@Test
	public void test() throws IOException, PythonException {
		MockLoggable ml = new MockLoggable();
		DataLogger dl = new DataLogger(ml);
		for(int i = 0; i < 10; i++) {
			dl.appendData();
		}
		dl.writeToCSVFileAndRunScript(new File("test.csv"), "/home/miles/workspace/Summer Project/dlwp/src/main/pythontest.py");
	}

	public class MockLoggable implements Loggable {

		int i = 0;
		
		@Override
		public List<Object> getData() {
			List data = Lists.newArrayList(i,2*i,3*i,4*i);
			i++;
			return data;			
		}

		@Override
		public List<String> getHeaders() {
			// TODO Auto-generated method stub
			return Lists.newArrayList("h1","h2","h3","h4");
		}
		
	}
	
}
