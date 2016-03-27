/**
 * 
 */
package cn.edu.bistu.cs;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigInteger implements BigInt {
	
	private int sign1 = 0;                       // 当为负数时，sign1值为 1
	private LinkedList<Byte> num1 = new LinkedList<Byte>();
	
	// num1和sign1的getter方法
	public int getSign() {
		return this.sign1;
	}
	
	public LinkedList<Byte> getValue() {
		return this.num1;
	}
	
	
	/**
	 * 比较num1和num2确定其大小，当num1大于等于num2时， 返回true
	 * @param num1 
	 * @param num2 
	 * @param maxListLength
	 * @return boolean
	 */
	private boolean isGreater (LinkedList<Byte> num1, LinkedList<Byte> num2, int maxListLength) {
			
		boolean b = false;
		
		if(num1.size() > num2.size()) {
			b = true;
		} 
		
		if(num1.size() == num2.size()) {
			
			for(int i=0; (!b && i<num1.size()); i++) {
				
				if(num1.get(i) > num2.get(i)) {
					b = true;
				}
				
			}
			
		}
		
		return b;
		
	}
	
	/**
	 * 绝对值加法处理逻辑
	 * @param num1
	 * @param num2
	 * @param result
	 * @param maxListLength
	 */
	private void absAdd(LinkedList<Byte> num1, LinkedList<Byte> num2, StringBuffer result, int maxListLength) {
		
		Byte temp = 0;
		
		for(int i=0; i<(maxListLength+1); i++) {
			
			Byte a = 0;
			Byte b = 0;			
			
			if(num1.peek() != null) {
				a = num1.pop();
			} 
			
			if(num2.peek() != null) {
				b = num2.pop();
			} 
			
			byte data = 0;
			
			if((a + b) >= 10) {
				data = (byte) ((a+b+temp) % 10);
				temp = 1;
			}
			
			else {
				data = (byte) ((a+b+temp) % 10);
				temp = 0;
			}
			
			result.append(data);
			
		}
		
	}
	
	/**
	 * 绝对值减法处理逻辑
	 * @param num1
	 * @param num2
	 * @param result
	 * @param maxListLength
	 */
	private void absSub(LinkedList<Byte> num1, LinkedList<Byte> num2, StringBuffer result, int maxListLength) {
		
		Byte temp = 0;
	
		for(int i=0; i<(maxListLength+1); i++) {
			
			Byte a = 0;         
			Byte b = 0;			
			
			if(num1.peek() != null) {
				a = num1.pop();
			} 
			
			if(num2.peek() != null) {
				b = num2.pop();
			} 
			
			byte data = 0;
			
			if(a - b - temp < 0) {
				data = (byte) (a - b - temp + 10);
				temp = 1;
			} 
			
			else {
				data = (byte) (a - b - temp);
				temp = 0;
			}
			
			result.append(data);
			
		}
		
	}
	
	
	/**
	 * 参数为BigInt类型的加法
	 */
	public BigInt add(BigInt bInt) {
		
		LinkedList<Byte> num2 = ((BigInteger) bInt).getValue();
		int sign2 = ((BigInteger) bInt).getSign();
		
		LinkedList<Byte> num1 = this.num1;
		int sign1 = this.sign1;
		
		StringBuffer result = new StringBuffer();
		int maxListLength = (num1.size() >= num2.size()) ? num1.size() : num2.size();
		
		// a>0, b>0, a+b型，结果为正
		if(sign1 == 0 && sign2 == 0) {
			absAdd(num1, num2, result, maxListLength);
		}	
		
		// a<0, b<0, -a-b型，结果为负
		if(sign1 == 1 && sign2 == 1) {
			result.append('-');
			absAdd(num1, num2, result, maxListLength);
		}	
		
		// a>0, b<0, a+-b型，转换成减法
		if(sign1 == 0 && sign2 == 1) {
			
			// |a|>|b|，结果为正
			if(isGreater(num1, num2, maxListLength)) {
				absSub(num1, num2, result, maxListLength);
			}
						
			// |a|<=|b|，结果为负
			else {
				result.append('-');
				absSub(num2, num1, result, maxListLength);
			}
			
		}
		
		// a<0, b>0, -a+b型，转换成减法
		if(sign1 == 1 && sign2 == 0 ) {
				
			// |a|>|b|，结果为负
			if(isGreater(num1, num2, maxListLength)) {
				result.append('-');
				absSub(num1, num2, result, maxListLength);
			}
			
			// |a|<=|b|，结果为正
			else {
				absSub(num2, num1, result, maxListLength);	
			}
			
		}

		return new BigInteger (result.toString());
		
	}
	
	/**
	 * 参数为BigInt类型的减法
	 */
	public BigInt sub(BigInt bInt) {
		
		LinkedList<Byte> num2 = ((BigInteger) bInt).getValue();
		int sign2 = ((BigInteger) bInt).getSign();
		
		LinkedList<Byte> num1 = this.num1;
		int sign1 = this.sign1;
		
		StringBuffer result = new StringBuffer();
		int maxListLength = (num1.size() >= num2.size()) ? num1.size() : num2.size();
		
		// a>0, b>0, a-b型
		if(sign1 == 0 && sign2 == 0) {
			
			// |a|>|b|，结果为正
			if(isGreater(num1, num2, maxListLength)) {
				absSub(num1, num2, result, maxListLength);	
			}
			
			// |a|<=|b|，结果为负
			else {	
				result.append('-');
				absSub(num2, num1, result, maxListLength);
			}
			
		}
			
		// a<0, b<0, -a--b型
		if(sign1 == 1 && sign2 == 1) {
			
			// |a|>|b|，结果为负
			if(isGreater(num1, num2, maxListLength)) {	
				result.append('-');		
				absSub(num1, num2, result, maxListLength);
			}
			
			// |a|<=|b|，结果为正
			else {
				absSub(num2, num1, result, maxListLength);
			}
			
		}
		
		// a>0, b<0, a--b型，结果为正
		if(sign1 == 0 && sign2 == 1) {
			absAdd(num1, num2, result, maxListLength);
		}
		
		// a<0, b>0, -a-b型，结果为负
		if(sign1 == 1 && sign2 == 0) {
			result.append('-');
			absAdd(num1, num2, result, maxListLength);
		}
			
		return new BigInteger (result.toString());
		
	}

	
	/**
	 * 参数为long类型的加法
	 */
	public BigInt add(long bLong) {
		
		BigInt bInt = new BigInteger(bLong);
		
		return add(bInt);
		
	}
	
	/**
	 * 参数为long类型的减法
	 */
	public BigInt sub(long bLong) {
		
		BigInt bInt = new BigInteger(bLong);
		
		return sub(bInt);
		
	}

	
	/**
	 * 参数为int类型的加法
	 */
	public BigInt add(int bInt) {
		
		BigInt bbInt = new BigInteger(bInt);
		
		return add(bbInt);
		
	}

	/**
	 * 参数为int类型的减法
	 */
	public BigInt sub(int bInt) {
		
		BigInt bbInt = new BigInteger(bInt);
		
		return sub(bbInt);
		
	}

	/**
	 * 构造函数
	 * @param bInt
	 */
	public BigInteger(int bInt) {
		
		this(Integer.toString(bInt));
		
	}

	/**
	 * 构造函数
	 * @param bLong
	 */
	public BigInteger(long bLong){
		
		this(Long.toString(bLong));
		
	}
	
	/**
	 * 构造函数
	 * @param bString
	 */
	public BigInteger(String bString){
		
		// 限制输入的必须为整数
		Pattern pattern = Pattern.compile("^[-0-9][0-9]*$");
		Matcher m = pattern.matcher(bString);
		
		if(!m.find()) {
			System.out.println("要计算的两个数必须为整数，请你修改数值后重新运行！！！");
			System.exit(0);
		}
		
		byte[] num1 = bString.getBytes();  
		
		for(int i=0; i<num1.length; i++) {
			
			if(i == 0 && num1[i] == '-') {
				sign1 = 1;
			} 
			
			else {
				this.num1.push((byte) (num1[i] - '0'));
			}
	
		}
		
	}
	
	/**
	 * 格式化输出结果
	 */
	public String toString() {
		
		StringBuffer str = new StringBuffer();
		int listLength = this.num1.size();	
		
		// 消除结果前的 0
		boolean bool = false;
		
		for(int i=0; !bool && i<listLength-1; i++) {
			
			if(this.num1.peek() == 0) {
				this.num1.pop();
			} 
			
			else {
				bool = true;
			}
			
		}
		
		// 将结果转存到str中
		listLength = this.num1.size();

		for(int i=0; i<listLength; i++) {
			str.append(this.num1.get(i));
		}
		
		
		// 3位整数一组输出
		int n = 0;
		int index = 2;
		int strLength = str.length();
		
		if(strLength % 3 == 0) {
			n = (int) (strLength / 3) - 1;
			index = 3;
		}
		
		else {
			
			if(strLength % 3 == 2) {
				index = 1;
			}

			n = (int) Math.floor(strLength / 3);
			
		}
		
		for(int i=0; i<n; i++) {
			str.insert(index, ',');
			index += 4;
		}
		
		// 当计算结果为负数且不为0时，添加负号	
		if(!str.toString().equals("0") && this.sign1 == 1) {
			str.insert(0, '-');
		}
		
		return str.toString();
		
	}
}
