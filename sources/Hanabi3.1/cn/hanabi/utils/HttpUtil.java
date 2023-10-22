package cn.hanabi.utils;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

@ObfuscationClass
public class HttpUtil {
   private static HttpURLConnection createUrlConnection(URL url) throws IOException {
      Validate.notNull(url);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setConnectTimeout(15000);
      connection.setReadTimeout(15000);
      connection.setUseCaches(false);
      return connection;
   }

   public static String performGetRequest(URL url) throws IOException {
      Validate.notNull(url);
      HttpURLConnection connection = createUrlConnection(url);
      InputStream inputStream = null;
      connection.setRequestProperty("User-agent", "Mozilla/5.0 AppIeWebKit");

      String var6;
      try {
         inputStream = connection.getInputStream();
         String var5 = IOUtils.toString(inputStream, Charsets.UTF_8);
         return var5;
      } catch (IOException var9) {
         IOUtils.closeQuietly(inputStream);
         inputStream = connection.getErrorStream();
         if (inputStream == null) {
            throw var9;
         }

         String result = IOUtils.toString(inputStream, Charsets.UTF_8);
         var6 = result;
      } finally {
         IOUtils.closeQuietly(inputStream);
      }

      return var6;
   }

   public static String performPostRequest(String Surl, String data) throws IOException {
      URL url = new URL(Surl);
      URLConnection uc = url.openConnection();
      uc.setDoOutput(true);
      OutputStream os = uc.getOutputStream();
      PrintStream ps = new PrintStream(os);
      ps.print(data);
      ps.close();
      InputStream is = uc.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      StringBuilder result = new StringBuilder();

      String s;
      while((s = reader.readLine()) != null) {
         result.append(s);
      }

      reader.close();
      return result.toString();
   }

   public static String doGet(String url) throws IOException {
      HttpURLConnection httpurlconnection = (HttpURLConnection)(new URL(url)).openConnection();
      httpurlconnection.setRequestMethod("GET");
      BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
      StringBuilder stringbuilder = new StringBuilder();

      String s;
      while((s = bufferedreader.readLine()) != null) {
         stringbuilder.append(s);
         stringbuilder.append('\r');
      }

      bufferedreader.close();
      return stringbuilder.toString();
   }

   public static Set getHeaders(String url) throws Exception {
      URL urlConnection = new URL("url");
      URLConnection conn = urlConnection.openConnection();
      Map headers = conn.getHeaderFields();
      return headers.keySet();
   }
}
