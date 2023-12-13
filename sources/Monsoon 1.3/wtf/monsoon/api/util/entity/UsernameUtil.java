package wtf.monsoon.api.util.entity;

import java.util.ArrayList;
import java.util.Random;

public class UsernameUtil {
	
	public static String getNewName() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789012345678901234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 6) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		String name = "Monsoon_" + saltStr;

		 return name;

	}

}
