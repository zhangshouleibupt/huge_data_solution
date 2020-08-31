package ipsearch;

public class IpwithFreq{
	String ip;
	long freq;
	public IpwithFreq(String ip,long freq) {
		this.ip = ip;
		this.freq = freq;
	}
	public String toString() {
		return String.format("(%s,%d)", ip,freq);
	}
}