package ipsearch;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

public class FrequencyCounter implements Callable<List<IpwithFreq>>{
	private File file;
	private Scanner sc;
	private HashMap<String,Long> map;
	List<IpwithFreq> ans;
	public FrequencyCounter(File file) {
		this.file = file;
	}
	@Override
	public List<IpwithFreq> call(){
		try {
			sc = init();
			map = new HashMap<>();
			while(sc.hasNext()) {
				String line = sc.nextLine();
				long cnt = map.getOrDefault(line,0L);
				map.put(line, cnt+1);
			}
			List<Map.Entry<String,Long>> list = new ArrayList<>(map.entrySet());
			Comparator<Map.Entry<String, Long>> com = new Comparator<Map.Entry<String,Long>>() {
				
				@Override
				public int compare(Entry<String,Long> o1, Entry<String, Long> o2) {
					// TODO Auto-generated method stub
					if(o1.getValue() != o2.getValue()) return (int)(o2.getValue() - o1.getValue());
					return 0;
				}
			}; 
			list.sort(com);
			ans = new ArrayList<>(100);
			for(int i = 0; i < 100 && i < list.size(); i++) {
				Map.Entry<String, Long> entry = list.get(i);
				ans.add(new IpwithFreq(entry.getKey(),entry.getValue()));
			}
			return ans;
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			map.clear();
			sc.close();
			return ans;
		}
	}
	public Scanner init() throws FileNotFoundException{
		Scanner sc;
		//File file = new File(fileName);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		sc = new Scanner(bis);
		return sc;
	}
}