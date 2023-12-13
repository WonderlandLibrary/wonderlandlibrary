package net.minecraft.util.security;

import net.minecraft.client.Minecraft;
import summer.Summer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;


public class Verify {
	public static Minecraft mc = Minecraft.getMinecraft();
	public static void checkInfo() throws IOException {
		String requestedUrl = "CNR8dy8EJXQCJvs6lspxD3wEfnhqjB3mrEMuh5r/BcSaigoZxIhaPA==";
		String dec;
		dec = decrypt(requestedUrl, "1");
		URL url = new URL(dec);
		String found = readStringFromURL(dec);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setConnectTimeout(10000);
		httpURLConnection.connect();
		if(found.contains(Summer.VERSION)) {

		}else {
			JOptionPane.showMessageDialog(null,"Outdated Version of Summer, Download the Latest Version");
			mc.shutdown();
		}
	}
	public static String readStringFromURL(String requestURL) throws IOException
	  {
	      try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
	              StandardCharsets.UTF_8.toString()))
	      {
	          scanner.useDelimiter("\\A");
	          return scanner.hasNext() ? scanner.next() : "";
	      }
	  }
	public static String decrypt(String obj, String key) {
	      try {
	          SecretKeySpec keySpec = new SecretKeySpec(MessageDigest.getInstance("MD5").digest(key.getBytes(StandardCharsets.UTF_8)), "Blowfish");

	          Cipher des = Cipher.getInstance("Blowfish");
	          des.init(Cipher.DECRYPT_MODE, keySpec);

	          String s = new String(des.doFinal(Base64.getDecoder().decode(obj.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
	          return s;

	      } catch (Exception e) {
	          e.printStackTrace();
	      }
	      return null;
	  }
	  public static String encrypt(String obj, String key) {
	      try {
	          SecretKeySpec keySpec = new SecretKeySpec(MessageDigest.getInstance("MD5").digest(key.getBytes(StandardCharsets.UTF_8)), "Blowfish");

	          Cipher des = Cipher.getInstance("Blowfish");
	          des.init(Cipher.ENCRYPT_MODE, keySpec);

	          return new String(Base64.getEncoder().encode(des.doFinal(obj.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);

	      } catch (Exception e) {
	          e.printStackTrace();
	      }
	      return null;
	  }
}
