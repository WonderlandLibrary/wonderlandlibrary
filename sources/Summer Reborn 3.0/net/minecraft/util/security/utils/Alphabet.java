package net.minecraft.util.security.utils;

public class Alphabet {
	  public enum LETTERS {
	    A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;
	  }
	  
	  public static String get(int letter) {
	    return LETTERS.values()[letter].name();
	  }
	}
