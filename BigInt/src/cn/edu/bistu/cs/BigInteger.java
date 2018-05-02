package cn.edu.bistu.cs;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class BigInteger implements BigInt {
	private Sign sign = Sign.POSITIVE;
	private LinkedList<Byte> list = new LinkedList<Byte>();

	public BigInteger(String integer) {
		if(!isIntegerLegal(integer)) {
			System.out.println(integer + " is illegal");
			System.exit(0);
		}
		
		byte[] nums = integer.getBytes();
		
		for (int i = 0; i < nums.length; i++) {
			if (i == 0) {
				if (nums[i] == '-') {
					this.setSign(Sign.NEGATIVE);
				} else if (nums[i] == '+') {
					continue;
				} else {
					this.list.push((byte) (nums[i] - '0'));
				}
			} else {
				this.list.push((byte) (nums[i] - '0'));
			}
		}
	}
	
	public BigInteger(int integer) {
		this(Integer.toString(integer));
	}
	
	public BigInteger(Long integer) {
		this(Long.toString(integer));
	}
	
	@Override
	public BigInt add(BigInt bInt) {
		LinkedList<Byte> num1 = this.list;
		boolean isSign1Positive = this.isPositive();
		
		LinkedList<Byte> num2 = ((BigInteger) bInt).getList();
		boolean isSign2Positive = bInt.isPositive();
		
		StringBuffer result = new StringBuffer();
		
		// a > 0, b > 0, |a| + |b| => +
		if (isSign1Positive && isSign2Positive) {
			listMerge(num1, num2, result, true);
		}
		
		// a < 0, b < 0, -(|a| + |b|) => -
		if (!isSign1Positive && !isSign2Positive) {
			result.append('-');
			listMerge(num1, num2, result, true);
		}
		
		// a > 0, b < 0
		if (isSign1Positive && !isSign2Positive) {
			if (largerThan(num1, num2) == 1) {            // |a| > |b|, |a| - |b| => +
				listMerge(num1, num2, result, false);
			} else if (largerThan(num1, num2) == -1) {    // |a| < |b|, -(|b| - |a|) => -
				result.append('-');
				listMerge(num2, num1, result, false);
			} else {                                      // |a| == |b|, |a| - |b| => 0
				result.append('0');
			}
		}
		
		// a < 0, b > 0
		if (!isSign1Positive && isSign2Positive) {
			if (largerThan(num1, num2) == 1) {            // |a| > |b|, -(|a| - |b|) => -
				result.append('-');
				listMerge(num1, num2, result, false);
			} else if (largerThan(num1, num2) == -1) {    // |a| < |b|, |b| - |a| => +
				listMerge(num2, num1, result, false);
			} else {                                      // |a| == |b|, |a| - |b| => 0
				result.append('0');
			}
		}
		
		return new BigInteger(result.toString());
	}

	@Override
	public BigInt sub(BigInt bInt) {
		LinkedList<Byte> num1 = this.list;
		boolean isSign1Positive = this.isPositive();
		
		LinkedList<Byte> num2 = ((BigInteger) bInt).getList();
		boolean isSign2Positive = bInt.isPositive();
		
		StringBuffer result = new StringBuffer();
		
		// a > 0, b > 0
		if (isSign1Positive && isSign2Positive) {
			if (largerThan(num1, num2) == 1) {            // |a| > |b|, |a| - |b| => +
				listMerge(num1, num2, result, false);
			} else if (largerThan(num1, num2) == -1) {    // |a| < |b|, -(|b| - |a|) => -
				result.append('-');
				listMerge(num2, num1, result, false);
			} else {                                      // |a| == |b|, |a| - |b| => 0
				result.append('0');
			}
		}
		
		// a < 0, b < 0, -(|a| + |b|) => -
		if (!isSign1Positive && !isSign2Positive) {
			if (largerThan(num1, num2) == 1) {           // |a| > |b|, -(|a| - |b|) => -
				result.append('-');
				listMerge(num1, num2, result, false);
			} else if (largerThan(num1, num2) == -1) {   // |a| < |b|, |b| - |a| => +
				listMerge(num2, num1, result, false);
			} else {                                     // |a| == |b|, |a| - |b| => 0
				result.append('0');
			}
		}
		
		// a > 0, b < 0, |a| + |b| => +
		if (isSign1Positive && !isSign2Positive) {
			listMerge(num1, num2, result, true);
		}
		
		// a < 0, b > 0, -(|a| + |b|) => -
		if (!isSign1Positive && isSign2Positive) {
			result.append('-');
			listMerge(num1, num2, result, true);
		}
		
		return new BigInteger(result.toString());
	}

	@Override
	public BigInt add(long bLong) {
		BigInt bigInt = new BigInteger(bLong);
		
		return add(bigInt);
	}

	@Override
	public BigInt sub(long bLong) {
		BigInt bigInt = new BigInteger(bLong);
		
		return sub(bigInt);
	}

	@Override
	public BigInt add(int bInt) {
		BigInt bigInt = new BigInteger(bInt);
		
		return add(bigInt);
	}

	@Override
	public BigInt sub(int bInt) {
		BigInt bigInt = new BigInteger(bInt);
		
		return sub(bigInt);
	}
	
	private void listMerge(LinkedList<Byte> num1, LinkedList<Byte> num2, StringBuffer result, boolean isPlus) {
		byte temp = 0;
		
		while (num1.peek() != null || num2.peek() != null || temp != 0) {
			byte a = 0, b = 0;
			
			if (num1.peek() != null) {
				a = num1.pop();
			}
			if (num2.peek() != null) {
				b = num2.pop();
			}
			
			byte sum;
			if (isPlus) {
				if (a + b + temp >= 10) {
					sum = (byte) ((a + b + temp) % 10);
					temp = 1;
				} else {
					sum = (byte) ((a + b + temp) % 10);
					temp = 0;
				}
			} else {
				if (a - b - temp < 0) {
					sum = (byte) (a - b - temp + 10);
					temp = 1;
				} else {
					sum = (byte) (a - b - temp);
					temp = 0;
				}
			}
			
			result.append(sum);
		}
	}
	
	/*
	 * list1 > list2 => 1
	 * list1 < list2 => -1
	 * list1 == list2 => 0
	 */
	private int largerThan(LinkedList<Byte> list1, LinkedList<Byte> list2) {
		if (list1.size() > list2.size()) {
			return 1;
		} 
		if (list1.size() < list2.size()) {
			return -1;
		}
		
		int size = list1.size();
		
		for (int i = 0; i < size; i++) {
			if (list1.get(i) > list2.get(i)) {
				return 1;
			} else if (list1.get(i) < list2.get(i)) {
				return -1;
			} 
		}
		return 0;
	}
	
	private boolean isIntegerLegal(String integer) {
		Pattern pattern = Pattern.compile("^[+-]?[\\d]+$");
		boolean isMatch = pattern.matcher(integer).find();
		
		return isMatch;
	}

	@Override
	public boolean isPositive() {
		return sign == Sign.POSITIVE ? true : false;
	}

	@Override
	public void setSign(Sign sign) {
		this.sign = sign;
	}
	
	public LinkedList<Byte> getList() {
		return this.list;
	}
	
	@Override
	public Sign getSign() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Byte[] getValue() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		while (this.list.size() > 1 && this.list.peek() == 0) {
			this.list.pop();
		}
		
		int size = this.list.size();
		for (int i = 0; i < size; i++) {
			buffer.append(this.list.get(i));
		}
		
		int start = -1;
		if (buffer.length() > 3) {
			start = buffer.length() % 3;
		}	
		while (start > -1 && start < buffer.length()) {
			if (start != 0) {
				buffer.insert(start, ',');
				start += 4;
			} else {
				start += 3;
			}
		}
		
		if (!buffer.toString().equals("0") && !this.isPositive()) {
			buffer.insert(0, '-');
		}
		
		return buffer.toString();
	}	
}
