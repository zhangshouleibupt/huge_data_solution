package ipsearch;
import java.util.concurrent.*;
import java.util.*;
public class Test {
	public static void main(String[] args) {
		Thread  t1 = new Thread(new Task("task1"));
		t1.start();
		try {
			t1.join();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		Thread t3 = new Thread(new Task("task3"));
		t3.start();
	}
}
class Task implements Runnable {
	String name;
	public Task(String name) {
		this.name = name;
	}
	@Override
	public void run() {
		try {
			for(int i = 0; i < 10; i++) {
				Thread.sleep(200);
				System.out.printf("in task %s num : %d\n",name,i);
			}
			System.out.printf("thread %s have finished\n",name);
			/*Thread t = new Thread(new Task("task2"));
			t.start();
			*/
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
