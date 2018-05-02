package cn.edu.bistu.cs;

public class TestMain {

	public static void main(String[] args) {
//		BigInt bInt = new BigInteger(+999999999999999999L);
//		BigInt result = bInt.sub(new BigInteger("1000000000000000000"));
		
		BigInt result = new BigInteger(999).add(new BigInteger(1));
		System.out.println(result);
	}
}
