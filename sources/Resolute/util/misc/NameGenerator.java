// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.misc;

import java.util.Random;
import java.util.Calendar;

public class NameGenerator
{
    private static final int diffBetweenAtoZ = 25;
    private static final int charValueOfa = 97;
    private static String lastGeneratedName;
    public static int length;
    static char[] vowels;
    
    public NameGenerator(int lengthOfName) {
        if (lengthOfName < 5 || lengthOfName > 10) {
            System.out.println("Setting default length to 7");
            lengthOfName = 7;
        }
        NameGenerator.length = lengthOfName;
    }
    
    public static String getName() {
        String currentGeneratedName;
        do {
            final Random randomNumberGenerator = new Random(Calendar.getInstance().getTimeInMillis());
            final char[] nameInCharArray = new char[NameGenerator.length];
            for (int i = 0; i < NameGenerator.length; ++i) {
                if (positionIsOdd(i)) {
                    nameInCharArray[i] = getVowel(randomNumberGenerator);
                }
                else {
                    nameInCharArray[i] = getConsonant(randomNumberGenerator);
                }
            }
            nameInCharArray[0] = Character.toUpperCase(nameInCharArray[0]);
            currentGeneratedName = new String(nameInCharArray);
        } while (currentGeneratedName.equals(NameGenerator.lastGeneratedName));
        return NameGenerator.lastGeneratedName = currentGeneratedName;
    }
    
    private static boolean positionIsOdd(final int i) {
        return i % 2 == 0;
    }
    
    private static char getConsonant(final Random randomNumberGenerator) {
        char currentCharacter;
        while (true) {
            currentCharacter = (char)(randomNumberGenerator.nextInt(25) + 97);
            if (currentCharacter != 'a' && currentCharacter != 'e' && currentCharacter != 'i' && currentCharacter != 'o') {
                if (currentCharacter == 'u') {
                    continue;
                }
                break;
            }
        }
        return currentCharacter;
    }
    
    private static char getVowel(final Random randomNumberGenerator) {
        return NameGenerator.vowels[randomNumberGenerator.nextInt(NameGenerator.vowels.length)];
    }
    
    static {
        NameGenerator.lastGeneratedName = "";
        NameGenerator.vowels = new char[] { 'a', 'e', 'i', 'o', 'u' };
    }
}
