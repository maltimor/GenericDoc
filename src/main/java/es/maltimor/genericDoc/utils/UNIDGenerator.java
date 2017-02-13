package es.maltimor.genericDoc.utils;

import java.security.MessageDigest;

public class UNIDGenerator {
	public static String getUNID(){
		String input = "-"+System.currentTimeMillis()+Math.random()+"-";
		try {
			byte[] res = MessageDigest.getInstance("SHA256").digest(input.getBytes());
			String sres = new String(HexString.encode(res));
			return sres;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
