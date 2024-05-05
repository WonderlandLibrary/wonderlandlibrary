package xyz.cucumber.base.utils.math;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
	public static String SHA256(String input) 
    {
		String result = "";
		byte[] byteData = Sha256.hash(input.getBytes(Charset.forName("UTF-8")));

		StringBuffer hexString = new StringBuffer();
		for (int i=0;i<byteData.length;i++) 
		{
			String hex=Integer.toHexString(0xff & byteData[i]);
			if(hex.length()==1) 
			{
				hexString.append('0');
			}
			hexString.append(hex);
		}
		result = hexString.toString();

		return result;
    }
	public static String MD5(String string) {
		try {
			byte[] msgBytes = string.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(msgBytes);
			byte[] resultByte = md.digest();
			BigInteger bigInt = new BigInteger(1, resultByte);
			String res = bigInt.toString(16);
			
			while (res.length() < 32) {
				res = "0" + res;
			}
			
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}