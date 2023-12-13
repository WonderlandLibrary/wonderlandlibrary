package io.netty.handler.ssl.util;

import io.netty.util.internal.ThreadLocalRandom;
import java.security.SecureRandom;
import java.util.Random;

final class ThreadLocalInsecureRandom extends SecureRandom {
   private static final long serialVersionUID = -8209473337192526191L;
   private static final SecureRandom INSTANCE = new ThreadLocalInsecureRandom();

   static SecureRandom current() {
      return INSTANCE;
   }

   private ThreadLocalInsecureRandom() {
   }

   public String getAlgorithm() {
      return "insecure";
   }

   public void setSeed(byte[] var1) {
   }

   public void setSeed(long var1) {
   }

   public void nextBytes(byte[] var1) {
      random().nextBytes(var1);
   }

   public byte[] generateSeed(int var1) {
      byte[] var2 = new byte[var1];
      random().nextBytes(var2);
      return var2;
   }

   public int nextInt() {
      return random().nextInt();
   }

   public int nextInt(int var1) {
      return random().nextInt(var1);
   }

   public boolean nextBoolean() {
      return random().nextBoolean();
   }

   public long nextLong() {
      return random().nextLong();
   }

   public float nextFloat() {
      return random().nextFloat();
   }

   public double nextDouble() {
      return random().nextDouble();
   }

   public double nextGaussian() {
      return random().nextGaussian();
   }

   private static Random random() {
      return ThreadLocalRandom.current();
   }
}
