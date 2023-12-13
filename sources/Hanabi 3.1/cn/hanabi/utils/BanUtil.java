package cn.hanabi.utils;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

@ObfuscationClass
public class BanUtil {
   public static String getHypixelBanInformation(String banId) {
      CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
      HttpGet httpGet = new HttpGet("https://hypixel.net/api/players/" + Minecraft.getMinecraft().getSession().getProfile().getId().toString().replace("-", "") + "/ban/" + banId);
      httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.94 Safari/537.36");
      httpGet.setHeader("xf-api-key", "LnM-qSeQqtJlJmJnVt76GhU-SoiolWs9");
      String result = null;

      try {
         CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(httpGet);
         result = IOUtils.toString(closeableHttpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      return result;
   }
}
