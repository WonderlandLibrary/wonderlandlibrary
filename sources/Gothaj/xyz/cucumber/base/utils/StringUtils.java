package xyz.cucumber.base.utils;

import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

public class StringUtils {
	
	public static String alphabet = "qwertzuiopsdghjklyxcvbnmQWERTZUIOPAFGHJKLYCVBNM1234567890";
	public static String numbers = "1234567890";
	
	public static String generateRandomCharacterFromAlphaBet() {
		Random random = new Random();
	    int index = random.nextInt(alphabet.length());
	    return Character.toString(alphabet.charAt(index));
	}
	public static String generateRandomNumber() {
		Random random = new Random();
	    int index = random.nextInt(numbers.length());
	    return Character.toString(numbers.charAt(index));
	}
	
	public static String generateNamesForMinecraft(String[] preffix, String[] firstNames, String[] separator, String[] lastNames, String[] suffix) {
		Random random = new Random();
		
		String preffixString = preffix[random.nextInt(preffix.length)];
		String firstNamesString = firstNames[random.nextInt(firstNames.length)];
		String separatorString = separator[random.nextInt(separator.length)];
		String lastNamesString = lastNames[random.nextInt(lastNames.length)];
		String suffixString = suffix[random.nextInt(suffix.length)];
		
		if((preffixString + firstNamesString + separatorString + lastNamesString + suffixString).length() > 13) {
			preffixString = "";
			
			if((preffixString + firstNamesString + separatorString + lastNamesString + suffixString).length() > 13) {
				separatorString = "";
			}
			
			if((preffixString + firstNamesString + separatorString + lastNamesString + suffixString).length() > 13) {
				suffixString = "";
			}
		}
		
		String name = preffixString + firstNamesString + separatorString + lastNamesString + suffixString;
		
		for(int i = 0; i < 16; i++) {
			name += generateRandomNumber();
		}
		
		name = name.substring(0, 16);
		
		return name;
	}
}
