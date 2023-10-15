/*     */ package org.neverhook.client.cmd.impl;
/*     */ 
/*     */ import java.util.Base64;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import org.neverhook.client.cmd.CommandAbstract;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AesCommand
/*     */   extends CommandAbstract
/*     */ {
/*     */   public AesCommand() {
/*  20 */     super("bind", "bind", ".aes encrypt | decrypt true | false (base64) key text", new String[] { "aes" });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(String... strings) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encryptAes(String value, String key, boolean base64) {
/*     */     try {
/*  57 */       byte[] raw = key.getBytes();
/*     */       
/*  59 */       SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
/*  60 */       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/*  61 */       cipher.init(1, keySpec);
/*     */       
/*  63 */       byte[] encrypted = cipher.doFinal(value.getBytes());
/*  64 */       return base64 ? Base64.getEncoder().encodeToString(encrypted) : new String(encrypted);
/*  65 */     } catch (Exception ex) {
/*  66 */       ex.printStackTrace();
/*     */       
/*  68 */       return null;
/*     */     } 
/*     */   }
/*     */   public static String encryptXor(String text, boolean base64, byte[] key) {
/*  72 */     byte[] textBytes = text.getBytes();
/*  73 */     byte[] textLength = new byte[text.length()];
/*     */     
/*  75 */     for (int i = 0; i < textBytes.length; i++) {
/*  76 */       textLength[i] = (byte)(textLength[i] ^ (byte)(textBytes[i] ^ key[i % key.length] ^ 0x10));
/*     */     }
/*     */     
/*  79 */     return base64 ? Base64.getEncoder().encodeToString(textBytes) : new String(textLength);
/*     */   }
/*     */   
/*     */   public static String decryptXor(byte[] text, boolean base64, String pKey) {
/*  83 */     byte[] textLength = new byte[text.length];
/*  84 */     byte[] key = pKey.getBytes();
/*     */     
/*  86 */     for (int i = 0; i < text.length; i++) {
/*  87 */       textLength[i] = (byte)(textLength[i] ^ (byte)(text[i] ^ key[i % key.length] ^ 0x10));
/*     */     }
/*     */     
/*  90 */     return base64 ? Base64.getEncoder().encodeToString(textLength) : new String(text);
/*     */   }
/*     */   
/*     */   public static String decryptAes(String encrypted, String key, boolean base64) {
/*     */     try {
/*  95 */       byte[] raw = key.getBytes();
/*  96 */       SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
/*  97 */       Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
/*  98 */       cipher.init(2, keySpec);
/*     */       
/* 100 */       byte[] original = cipher.doFinal(base64 ? Base64.getDecoder().decode(encrypted) : encrypted.getBytes());
/*     */       
/* 102 */       return new String(original);
/* 103 */     } catch (Exception ex) {
/* 104 */       ex.printStackTrace();
/*     */       
/* 106 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\neverhook\client\cmd\impl\AesCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */