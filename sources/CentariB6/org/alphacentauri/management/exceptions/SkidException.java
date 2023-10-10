package org.alphacentauri.management.exceptions;

public class SkidException extends Exception {
   public SkidException(String insult) {
      for(int i = 0; i < 1337; ++i) {
         System.out.println(insult);
      }

      System.exit(1337);
   }
}
