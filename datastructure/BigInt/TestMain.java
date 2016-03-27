
package cn.edu.bistu.cs;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		BigInt bInt = new BigInteger("222");
		System.out.println(bInt.sub(1234560L));
		
		BigInt bInt2 = new BigInteger(11);
		System.out.println(bInt2.sub(new BigInteger(1000)));
		
		BigInt bInt3 = new BigInteger("abcabc");
		System.out.println(bInt3.add(1234566789012312300L));
		
		BigInt bInt4 = new BigInteger(11);
		System.out.println(bInt4.add(new BigInteger("-333")));
		
		
		
	}
}
