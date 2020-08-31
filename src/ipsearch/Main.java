package ipsearch;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.File;
public class Main {
	public static String createIp() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 4; i++) {
			if(i != 0) sb.append(".");
			sb.append((int)(Math.random() * 255));
		}
		return sb.toString();
	}
	public static void main(String[] args) {
		String fileName = "./data/hugeIpFile.txt";
		OutputStreamWriter osw = null;
		try {
			File file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file,false);
			//System.out.println(fos);
			osw = new OutputStreamWriter(new BufferedOutputStream(fos));
			for(int i = 0; i < 100000; i++) {
				String line = createIp();
				osw.write(line);
				osw.write("\n");
			}
			System.out.println("have finished create file");
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				osw.flush();
				osw.close();
			}
			catch(IOException e) {
				
			}
		}
	}
}
