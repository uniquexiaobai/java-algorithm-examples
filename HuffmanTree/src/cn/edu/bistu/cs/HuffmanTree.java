package cn.edu.bistu.cs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * 哈夫曼树实现
 * 
 */
public class HuffmanTree {
	private Map<Character, String> code = new HashMap<Character, String>();
	private HTNode tree = null;
	private String s;
	private ArrayList<HTNode> nodeList;
	
	public HuffmanTree(String s) {
		this.s = s;
		Map<Character, Integer> chars = HuffmanTree.computeCharCount(s);
		
		nodeList = new ArrayList<>();
		for(Character c : chars.keySet()){
			HTNode node = new HTNode(c, chars.get(c));
			nodeList.add(node);
		}
		
		buildTree(nodeList);
	}
		
	/**
	 * 根据初始的结点列表，建立哈夫曼树，
	 * 并生成哈夫曼编码，保存在当前类的code对象中，
	 * 生成的树根结点，被保存在当前类的tree对象中。
	 * 可以反复生成哈夫曼树，每次重新构建树，将更新编码
	 * @param nodes
	 * @return
	 */
	public void buildTree(List<HTNode> nodes) {
		PriorityQueue<HTNode> pq = new PriorityQueue<HTNode>(nodes.size());
		HTNode left, right, top;
		
		for (int i = 0; i < nodes.size(); i++) {
			pq.add(nodes.get(i));
		}

		while (pq.size() > 1) {
			left = pq.remove();
			right = pq.remove();
			int newWeight = left.getWeight() + right.getWeight();
			top = new HTNode('$', newWeight, left, right);
			pq.add(top);
		}
		
		tree = pq.remove();
		createCode(tree, "");
	}
	
	private void createCode(HTNode node, String c) {
		if (node.isLeafNode()) {
			this.code.put(node.getData(), c);
			node.setCode(String.valueOf(c));
		} else {
			createCode(node.getLchild(), c + 0);
			createCode(node.getRchild(), c + 1);
		}
	}
		
	/**
	 * 获取已建立的哈夫曼树生成的字符编码，
	 * 字符编码应为0，1字符串
	 * @return
	 */
	public Map<Character, String> getCode() {
		return this.code;
	}
	
	/**
	 * 统计字符串中字符出现的频率
	 * @param text
	 * @return
	 */
	public static Map<Character,Integer> computeCharCount(String text) {
		Map<Character,Integer> map = new HashMap<Character,Integer>();
		
		List<String> original = text.chars().mapToObj(i -> (char) i).map(String::valueOf).collect(Collectors.toList());
		List<String> duplicateRemoved = text.chars().mapToObj(i -> (char) i).map(String::valueOf).distinct().collect(Collectors.toList());

		int count = 1;
		for (String s : duplicateRemoved) {
			count = (int) original.stream().filter(s1 -> s1.equals(s)).count();
			map.put(s.charAt(0), count);
		}
		
		return map;
	}
	
	/**
	 * 使用当前类训练好的huffman编码来对文本进行编码
	 * @return
	 */
	public String encode(String text) {
		String result = text.chars().mapToObj(i -> (char) i).map(this.code::get).collect(Collectors.joining());

		return result;
	}

	/**
	 * 使用当前类中训练好的huffman编码，
	 * 对编码后的文本进行解码
	 * @param text
	 * @return
	 */
	public String decode(String text) {
		StringBuilder result = new StringBuilder();
		HTNode base = tree;
		
		while (!text.isEmpty()) {
			base = text.charAt(0) == '1' ? base.getRchild() : base.getLchild();
			text = text.substring(1);
			
			if (base.isLeafNode()){
		  		result.append(base.getData());
		  		base = tree;
		  	}
		}
		
		return result.toString();
	}
	
	public void printCompressRatio() {
		int bits = 0;
		for(HTNode n : nodeList) {
			bits += n.getWeight() * n.getCode().length();
		}
		
		float p1 = bits * 1f / (s.length() * 16);
		float p = (1 - p1) * 100;
		
		System.out.println("The compression rate is " + p + "%");
	}

	public static void main(String[] args) {
		
		//首先对字符串中的字符出现次数进行统计
		String data = "In computer science and information theory, "
				+ "a Huffman code is a particular type of optimal prefix code that is commonly used for lossless data compression. "
				+ "The process of finding and/or using such a code proceeds by means of Huffman coding, "
				+ "an algorithm developed by David A. Huffman while he was a Ph.D. student at MIT, and published in the 1952 paper "
				+ "\"A Method for the Construction of Minimum-Redundancy Codes\".[1] "
				+ "The output from Huffman's algorithm can be viewed as a variable-length code table for encoding a source symbol "
				+ "(such as a character in a file). The algorithm derives this table from the estimated probability or frequency of occurrence"
				+ " (weight) for each possible value of the source symbol. As in other entropy encoding methods, more common symbols are generally "
				+ "represented using fewer bits than less common symbols. Huffman's method can be efficiently implemented, "
				+ "finding a code in linear time to the number of input weights if these weights are sorted.[2] However, "
				+ "although optimal among methods encoding symbols separately, Huffman coding is not always optimal among all compression methods.";
		
		HuffmanTree htree = new HuffmanTree(data);
		
		Map<Character, String> code = htree.getCode();
		for(Character c : code.keySet()) {
			System.out.println("字符'"+ c + "'的哈夫曼编码：" + code.get(c));
		}

		String text = "In computer science and information theory";
		String coded = htree.encode(text);
		System.out.println("字符串：In computer science and information theory");
		System.out.println("被编码为："+coded);
		
		String oriText = htree.decode(coded);
		System.out.println("编码："+coded);
		System.out.println("被解码为："+oriText);
		System.out.println(oriText.equals(text));
		
		htree.printCompressRatio();
	}
	
	
	
}
