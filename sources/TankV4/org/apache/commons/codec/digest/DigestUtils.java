package org.apache.commons.codec.digest;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

public class DigestUtils {
   private static final int STREAM_BUFFER_LENGTH = 1024;

   private static byte[] digest(MessageDigest var0, InputStream var1) throws IOException {
      return updateDigest(var0, var1).digest();
   }

   public static MessageDigest getDigest(String var0) {
      try {
         return MessageDigest.getInstance(var0);
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalArgumentException(var2);
      }
   }

   public static MessageDigest getMd2Digest() {
      return getDigest("MD2");
   }

   public static MessageDigest getMd5Digest() {
      return getDigest("MD5");
   }

   public static MessageDigest getSha1Digest() {
      return getDigest("SHA-1");
   }

   public static MessageDigest getSha256Digest() {
      return getDigest("SHA-256");
   }

   public static MessageDigest getSha384Digest() {
      return getDigest("SHA-384");
   }

   public static MessageDigest getSha512Digest() {
      return getDigest("SHA-512");
   }

   /** @deprecated */
   @Deprecated
   public static MessageDigest getShaDigest() {
      return getSha1Digest();
   }

   public static byte[] md2(byte[] var0) {
      return getMd2Digest().digest(var0);
   }

   public static byte[] md2(InputStream var0) throws IOException {
      return digest(getMd2Digest(), var0);
   }

   public static byte[] md2(String var0) {
      return md2(StringUtils.getBytesUtf8(var0));
   }

   public static String md2Hex(byte[] var0) {
      return Hex.encodeHexString(md2(var0));
   }

   public static String md2Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(md2(var0));
   }

   public static String md2Hex(String var0) {
      return Hex.encodeHexString(md2(var0));
   }

   public static byte[] md5(byte[] var0) {
      return getMd5Digest().digest(var0);
   }

   public static byte[] md5(InputStream var0) throws IOException {
      return digest(getMd5Digest(), var0);
   }

   public static byte[] md5(String var0) {
      return md5(StringUtils.getBytesUtf8(var0));
   }

   public static String md5Hex(byte[] var0) {
      return Hex.encodeHexString(md5(var0));
   }

   public static String md5Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(md5(var0));
   }

   public static String md5Hex(String var0) {
      return Hex.encodeHexString(md5(var0));
   }

   /** @deprecated */
   @Deprecated
   public static byte[] sha(byte[] var0) {
      return sha1(var0);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] sha(InputStream var0) throws IOException {
      return sha1(var0);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] sha(String var0) {
      return sha1(var0);
   }

   public static byte[] sha1(byte[] var0) {
      return getSha1Digest().digest(var0);
   }

   public static byte[] sha1(InputStream var0) throws IOException {
      return digest(getSha1Digest(), var0);
   }

   public static byte[] sha1(String var0) {
      return sha1(StringUtils.getBytesUtf8(var0));
   }

   public static String sha1Hex(byte[] var0) {
      return Hex.encodeHexString(sha1(var0));
   }

   public static String sha1Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha1(var0));
   }

   public static String sha1Hex(String var0) {
      return Hex.encodeHexString(sha1(var0));
   }

   public static byte[] sha256(byte[] var0) {
      return getSha256Digest().digest(var0);
   }

   public static byte[] sha256(InputStream var0) throws IOException {
      return digest(getSha256Digest(), var0);
   }

   public static byte[] sha256(String var0) {
      return sha256(StringUtils.getBytesUtf8(var0));
   }

   public static String sha256Hex(byte[] var0) {
      return Hex.encodeHexString(sha256(var0));
   }

   public static String sha256Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha256(var0));
   }

   public static String sha256Hex(String var0) {
      return Hex.encodeHexString(sha256(var0));
   }

   public static byte[] sha384(byte[] var0) {
      return getSha384Digest().digest(var0);
   }

   public static byte[] sha384(InputStream var0) throws IOException {
      return digest(getSha384Digest(), var0);
   }

   public static byte[] sha384(String var0) {
      return sha384(StringUtils.getBytesUtf8(var0));
   }

   public static String sha384Hex(byte[] var0) {
      return Hex.encodeHexString(sha384(var0));
   }

   public static String sha384Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha384(var0));
   }

   public static String sha384Hex(String var0) {
      return Hex.encodeHexString(sha384(var0));
   }

   public static byte[] sha512(byte[] var0) {
      return getSha512Digest().digest(var0);
   }

   public static byte[] sha512(InputStream var0) throws IOException {
      return digest(getSha512Digest(), var0);
   }

   public static byte[] sha512(String var0) {
      return sha512(StringUtils.getBytesUtf8(var0));
   }

   public static String sha512Hex(byte[] var0) {
      return Hex.encodeHexString(sha512(var0));
   }

   public static String sha512Hex(InputStream var0) throws IOException {
      return Hex.encodeHexString(sha512(var0));
   }

   public static String sha512Hex(String var0) {
      return Hex.encodeHexString(sha512(var0));
   }

   /** @deprecated */
   @Deprecated
   public static String shaHex(byte[] var0) {
      return sha1Hex(var0);
   }

   /** @deprecated */
   @Deprecated
   public static String shaHex(InputStream var0) throws IOException {
      return sha1Hex(var0);
   }

   /** @deprecated */
   @Deprecated
   public static String shaHex(String var0) {
      return sha1Hex(var0);
   }

   public static MessageDigest updateDigest(MessageDigest var0, byte[] var1) {
      var0.update(var1);
      return var0;
   }

   public static MessageDigest updateDigest(MessageDigest var0, InputStream var1) throws IOException {
      byte[] var2 = new byte[1024];

      for(int var3 = var1.read(var2, 0, 1024); var3 > -1; var3 = var1.read(var2, 0, 1024)) {
         var0.update(var2, 0, var3);
      }

      return var0;
   }

   public static MessageDigest updateDigest(MessageDigest var0, String var1) {
      var0.update(StringUtils.getBytesUtf8(var1));
      return var0;
   }
}
