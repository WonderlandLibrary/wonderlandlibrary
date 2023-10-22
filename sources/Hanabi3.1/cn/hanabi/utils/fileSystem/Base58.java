package cn.hanabi.utils.fileSystem;

import java.util.Arrays;

public class Base58 {
   private char[] ALPHABET;
   private int BASE_58;
   private int BASE_256;
   private int[] INDEXES;

   public Base58(int i) {
      this.init(i);
   }

   public void init(int key) {
      if (key == 14514) {
         this.ALPHABET = "tBMoi7Whp9aUrcJxKjkqVnbFHgL3C1N6e2AfmSwE5vTPQzYuG8dZR4ysXD".toCharArray();
         this.BASE_58 = this.ALPHABET.length;
         this.BASE_256 = 256;
         this.INDEXES = new int[128];
      } else {
         this.ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
         this.BASE_58 = this.ALPHABET.length;
         this.BASE_256 = 256;
         this.INDEXES = new int[128];
      }

      Arrays.fill(this.INDEXES, -1);

      for(int i = 0; i < this.ALPHABET.length; this.INDEXES[this.ALPHABET[i]] = i++) {
      }

   }

   public String encode(byte[] input) {
      if (input.length == 0) {
         return "";
      } else {
         input = this.copyOfRange(input, 0, input.length);

         int zeroCount;
         for(zeroCount = 0; zeroCount < input.length && input[zeroCount] == 0; ++zeroCount) {
         }

         byte[] temp = new byte[input.length * 2];
         int j = temp.length;

         byte mod;
         for(int startAt = zeroCount; startAt < input.length; temp[j] = (byte)this.ALPHABET[mod]) {
            mod = this.divmod58(input, startAt);
            if (input[startAt] == 0) {
               ++startAt;
            }

            --j;
         }

         while(j < temp.length && temp[j] == this.ALPHABET[0]) {
            ++j;
         }

         while(true) {
            --zeroCount;
            if (zeroCount < 0) {
               byte[] output = this.copyOfRange(temp, j, temp.length);
               return new String(output);
            }

            --j;
            temp[j] = (byte)this.ALPHABET[0];
         }
      }
   }

   public byte[] decode(String input) {
      if (input.length() == 0) {
         return new byte[0];
      } else {
         byte[] input58 = new byte[input.length()];

         int zeroCount;
         int j;
         for(zeroCount = 0; zeroCount < input.length(); ++zeroCount) {
            char c = input.charAt(zeroCount);
            j = -1;
            if (c >= 0 && c < 128) {
               j = this.INDEXES[c];
            }

            if (j < 0) {
               throw new RuntimeException("Not a Base58 input: " + input);
            }

            input58[zeroCount] = (byte)j;
         }

         for(zeroCount = 0; zeroCount < input58.length && input58[zeroCount] == 0; ++zeroCount) {
         }

         byte[] temp = new byte[input.length()];
         j = temp.length;

         byte mod;
         for(int startAt = zeroCount; startAt < input58.length; temp[j] = mod) {
            mod = this.divmod256(input58, startAt);
            if (input58[startAt] == 0) {
               ++startAt;
            }

            --j;
         }

         while(j < temp.length && temp[j] == 0) {
            ++j;
         }

         return this.copyOfRange(temp, j - zeroCount, temp.length);
      }
   }

   private byte divmod58(byte[] number, int startAt) {
      int remainder = 0;

      for(int i = startAt; i < number.length; ++i) {
         int digit256 = number[i] & 255;
         int temp = remainder * this.BASE_256 + digit256;
         number[i] = (byte)(temp / this.BASE_58);
         remainder = temp % this.BASE_58;
      }

      return (byte)remainder;
   }

   private byte divmod256(byte[] number58, int startAt) {
      int remainder = 0;

      for(int i = startAt; i < number58.length; ++i) {
         int digit58 = number58[i] & 255;
         int temp = remainder * this.BASE_58 + digit58;
         number58[i] = (byte)(temp / this.BASE_256);
         remainder = temp % this.BASE_256;
      }

      return (byte)remainder;
   }

   private byte[] copyOfRange(byte[] source, int from, int to) {
      byte[] range = new byte[to - from];
      System.arraycopy(source, from, range, 0, range.length);
      return range;
   }
}
