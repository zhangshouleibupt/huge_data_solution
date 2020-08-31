package ipsearch;
import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class Producer implements Runnable{
	private Scanner sc;
	private String fileName;
	private ExecutorService pool;
	private ConcurrentHashMap<Integer, List<String>> bucket;
	private CountDownLatch latch;
	private int cnt = 0;
	private int batch = 0;
	public Producer(String fileName,
			ConcurrentHashMap<Integer,List<String>> bucket,
			ExecutorService pool) {
		this.fileName = fileName;
		this.bucket = bucket;
		this.pool = pool;		
	}
	@Override
	public void run() {
		try {
			Scanner sc = init();
			while(true) {
				if(!sc.hasNext()) {
					break;
				}
				String line = sc.nextLine();
				if(!isValidIp(line))continue;
				//System.out.println(line + " " + cnt);
				cnt++;
				if(cnt == 10000) {
					batch++;
					//wait util the pool task is all done;
					//it is blocking
					System.out.printf("batch %d have finished\n",batch);
					latch = new CountDownLatch(bucket.size());
					//System.out.println(bucket.size());
					submitTask(bucket);
					latch.await();
					cnt = 0;
				}
				int bucketId = calculateBucketId(line);
				List<String> list = bucket.getOrDefault(bucketId , new ArrayList<String>());
				list.add(line);
				bucket.put(bucketId,list);
			}
			submitTask(bucket);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		/*
		finally {
			sc.close();
		}
		*/
	}
	public int calculateBucketId(String ip) {
		int ans = 0;
		for(int i = 0; i < ip.length(); i++) {
			int c = ip.charAt(i) - '0';
			if(c >= 0 && c <= 9) ans = ans * 10 + c;
		}
		return Math.abs(ans) % 100;
	}
	public void submitTask(ConcurrentHashMap<Integer,List<String>> map) {
		for(Map.Entry<Integer,List<String>> entry : map.entrySet()) {
			pool.execute(new Consumer(entry,latch));
		}
	}
	public Scanner init() throws IOException{
		File file = new File(fileName);
		File toSaveDir = new File(file.getParent() + "/smallipfile");
		toSaveDir.mkdir();
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		Scanner sc = new Scanner(bis);
		return sc;
	}
	private boolean isValidIp(String line) {
		int n = line.length() - 1;
		if(line.charAt(n) == '\n') line = line.substring(0,n);
		String[]  parts = line.split("\\.");
		if(parts.length != 4) return false;
		for(String part : parts) {
			int num = Integer.parseInt(part);
			if(num < 0 || num > 255) return false;
		}
		return true;
	}
}
