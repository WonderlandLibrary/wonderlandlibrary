package net.minecraft.util.security.utils;

import java.util.ArrayList;

public class StringUtil {
  public static int convertToString(String lette) {
    Alphabet.LETTERS letter = null;
    for (int i = 0; i < (Alphabet.LETTERS.values()).length; i++) {
      if (Alphabet.get(i).equalsIgnoreCase(lette))
        letter = Alphabet.LETTERS.values()[i]; 
    } 
    int string = -1;
    if (letter == Alphabet.LETTERS.A) {
      string = 1;
    } else if (letter == Alphabet.LETTERS.B) {
      string = 2;
    } else if (letter == Alphabet.LETTERS.C) {
      string = 3;
    } else if (letter == Alphabet.LETTERS.D) {
      string = 4;
    } else if (letter == Alphabet.LETTERS.E) {
      string = 5;
    } else if (letter == Alphabet.LETTERS.F) {
      string = 6;
    } else if (letter == Alphabet.LETTERS.G) {
      string = 7;
    } else if (letter == Alphabet.LETTERS.H) {
      string = 8;
    } else if (letter == Alphabet.LETTERS.I) {
      string = 9;
    } else if (letter == Alphabet.LETTERS.J) {
      string = 10;
    } else if (letter == Alphabet.LETTERS.K) {
      string = 11;
    } else if (letter == Alphabet.LETTERS.L) {
      string = 12;
    } else if (letter == Alphabet.LETTERS.M) {
      string = 13;
    } else if (letter == Alphabet.LETTERS.N) {
      string = 14;
    } else if (letter == Alphabet.LETTERS.O) {
      string = 15;
    } else if (letter == Alphabet.LETTERS.P) {
      string = 16;
    } else if (letter == Alphabet.LETTERS.Q) {
      string = 17;
    } else if (letter == Alphabet.LETTERS.R) {
      string = 18;
    } else if (letter == Alphabet.LETTERS.S) {
      string = 19;
    } else if (letter == Alphabet.LETTERS.T) {
      string = 20;
    } else if (letter == Alphabet.LETTERS.U) {
      string = 21;
    } else if (letter == Alphabet.LETTERS.V) {
      string = 22;
    } else if (letter == Alphabet.LETTERS.W) {
      string = 23;
    } else if (letter == Alphabet.LETTERS.X) {
      string = 24;
    } else if (letter == Alphabet.LETTERS.Y) {
      string = 25;
    } else if (letter == Alphabet.LETTERS.Z) {
      string = 26;
    } 
    return string;
  }
  
  public static ArrayList<String> getSubstrings(String s) {
    ArrayList<String> substrings = new ArrayList<>();
    for (int i = 0; i < s.length(); i++) {
      String substring = s.substring(i, i + 1);
      substrings.add(substring);
    } 
    return substrings;
  }
}
