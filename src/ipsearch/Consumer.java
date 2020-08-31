package ipsearch;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.*;
public class Consumer implements Runnable{
	private CountDownLatch latch;
	private OutputStreamWriter osw;
	private Map.Entry<Integer, List<String>> entry;
	public Consumer(Map.Entry<Integer, List<String>> entry,CountDownLatch latch) {
		this.entry = entry;
		this.latch = latch;
	}
	@Override
	public void run() {
		try {
			saveIntoFile();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				//clear the buffered memory and write into disk
				osw.flush();
				osw.close();
			}
			catch(IOException e) {
				
			}
			//when the all line save into disk,clear the list
			entry.getValue().clear();
			latch.countDown();
		}
	}
	public void saveIntoFile() throws IOException{
		int num = entry.getKey();
		String fileName = String.format("./data/smallipfile/ipmod%d.txt",num);
		File file = new File(fileName);
		FileOutputStream fos = new FileOutputStream(file,true);
		osw = new OutputStreamWriter(new BufferedOutputStream(fos));
		for (String line : entry.getValue()) {
			osw.write(line,0,line.length());
			osw.write("\n",0,1);
		}
	}
}
