package cn.edu.bistu.cs;

public class HTNode implements Comparable<HTNode> {
	private char data;
	private int weight;
	private String code;

	private HTNode lchild;
	private HTNode rchild;
	
	public HTNode(char data, int weight) {
		this.data = data;
		this.weight = weight;
	}
	
	public HTNode(char data, int weight, HTNode lchild, HTNode rchild) {
		this.data = data;
		this.weight = weight;
		this.lchild = lchild;
		this.rchild = rchild;
	}
	
	public char getData() {
		return data;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public HTNode getLchild() {
		return lchild;
	}
	
	public HTNode getRchild() {
		return rchild;
	}
	
	public boolean isLeafNode() {
		return this.lchild == null && this.rchild == null;
	}
	
	@Override
	public int compareTo(HTNode o) {
		if (this.weight < o.weight) {
			return -1;
		} else {
			return 1;
		}
	}
}