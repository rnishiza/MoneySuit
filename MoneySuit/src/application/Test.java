package application;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Test {

	public static void main(String[] args) {
		System.out.println("Test");
	}
	
	private String getMD5Pass(String pass) {
		try {
			byte[] pass_byte = pass.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] pass_md5 = md.digest(pass_byte);
			
			BigInteger bigInt = new BigInteger(1,pass_md5);
			String pass_coded = bigInt.toString(16);
			return pass_coded;
		} catch (Exception e) {
			return null;
		}
	}

}
