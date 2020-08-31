package ipsearch;
import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
public class IpSearch {
	public static void main(String[] args) {
		ConcurrentHashMap<Integer, List<String>> bucket = new ConcurrentHashMap<>();
		ExecutorService pool = Executors.newFixedThreadPool(3);
		String fileName = "./data/hugeIpFile.txt";
		Thread producer = new Thread(new Producer(fileName,bucket,pool));
		//split the huge ip data into small file by the bucket method
		producer.start();
		//wait the split producer thread finished
		try {
			producer.join();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		List<Future<List<IpwithFreq>>> ans = new ArrayList<>();
		File rootDir = new File("./data/smallipfile");
		for(File file : rootDir.listFiles()){
			//System.out.println(file.getName());
			ans.add(pool.submit(new FrequencyCounter(file)));
		}
		pool.shutdown();
		List<IpwithFreq> topFreqList = new ArrayList<>();
		for(Future<List<IpwithFreq>> res : ans) {
			try {
				topFreqList.addAll(res.get());
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}	
			catch(ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		Comparator<IpwithFreq> com = new Comparator<IpwithFreq>() {
			
			@Override
			public int compare(IpwithFreq o1, IpwithFreq o2) {
				// TODO Auto-generated method stub
				if(o1.freq != o2.freq) return (int) (o2.freq - o1.freq);
				return 0;
			}
		};
		topFreqList.sort(com);
		for(int i = 0; i < 100; i++) {
			
			System.out.println(topFreqList.get(i));
			
		}
		System.out.println("all count have finished");
		//when the producer finished, use the multithread way to 
		//count the frequency
	}
}