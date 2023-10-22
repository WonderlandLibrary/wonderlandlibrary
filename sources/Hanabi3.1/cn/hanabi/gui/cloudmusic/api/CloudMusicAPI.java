package cn.hanabi.gui.cloudmusic.api;

import cn.hanabi.Hanabi;
import cn.hanabi.gui.cloudmusic.MusicManager;
import cn.hanabi.gui.cloudmusic.impl.Lyric;
import cn.hanabi.gui.cloudmusic.impl.Track;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;

public enum CloudMusicAPI {
   INSTANCE;

   private final String[][] headers = new String[][]{{"Accept", "*/*"}, {"Accept-Language", "zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4"}, {"Connection", "keep-alive"}, {"Content-Type", "application/x-www-form-urlencoded"}, {"Host", "music.163.com"}, {"User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.4389.114 Safari/537.36"}, {"Referer", "https://music.163.com/"}, {"X-Real-IP", "117.181.172.1"}};
   public String[][] cookies = new String[][]{{"os", "pc"}, {"Referer", "https://music.163.com/"}, {"__remember_me", "true"}};
   private final JsonParser parser = new JsonParser();

   public Object[] loginPhone(String phoneNum, String passwd) throws Exception {
      JsonObject obj = new JsonObject();
      obj.addProperty("phone", phoneNum);
      obj.addProperty("password", DigestUtils.md5Hex(passwd.getBytes()));
      obj.addProperty("rememberLogin", true);
      String data = this.encryptRequest(obj.toString());
      return this.httpRequest("https://music.163.com/weapi/login/cellphone", data, CloudMusicAPI.RequestType.POST);
   }

   public String QRKey() throws Exception {
      JsonObject obj = new JsonObject();
      obj.addProperty("type", 1);
      String data = this.encryptRequest(obj.toString());
      return ((JsonObject)this.parser.parse((String)this.httpRequest("https://music.163.com/weapi/login/qrcode/unikey", data, CloudMusicAPI.RequestType.POST)[0])).get("unikey").getAsString();
   }

   public Object[] QRState(String key) throws Exception {
      JsonObject obj = new JsonObject();
      obj.addProperty("type", 1);
      obj.addProperty("key", key);
      String data = this.encryptRequest(obj.toString());
      Object[] request = this.httpRequest("https://music.163.com/weapi/login/qrcode/client/login", data, CloudMusicAPI.RequestType.POST);
      JsonObject result = (JsonObject)this.parser.parse((String)request[0]);
      int code = result.get("code").getAsInt();
      return new Object[]{code, request[1]};
   }

   public Object[] refreshState() throws Exception {
      return this.httpRequest("https://music.163.com/weapi/login/token/refresh", (String)null, CloudMusicAPI.RequestType.POST);
   }

   public Object[] getPlayList(String userId) throws Exception {
      String json = (String)this.httpRequest("http://music.163.com/api/user/playlist/?offset=0&limit=100&uid=" + userId, (String)null, CloudMusicAPI.RequestType.GET)[0];
      JsonObject obj = (JsonObject)this.parser.parse(json);
      if (obj.get("code").getAsInt() != 200) {
         return new Object[]{"获取歌单列表时发生错误, 错误码 " + obj.get("code").getAsInt(), null};
      } else {
         ArrayList temp = new ArrayList();

         for(int i = 0; i < obj.get("playlist").getAsJsonArray().size(); ++i) {
            JsonObject shit = obj.get("playlist").getAsJsonArray().get(i).getAsJsonObject();
            temp.add(new PlayList(this.toDBC(this.getObjectAsString(shit, "name")), this.getObjectAsString(shit, "id")));
         }

         return new Object[]{"200", temp};
      }
   }

   public String getLyricJson(String songId) throws Exception {
      JsonObject obj = new JsonObject();
      obj.addProperty("id", songId);
      obj.addProperty("lv", -1);
      obj.addProperty("tv", -1);
      return (String)this.httpRequest("https://music.163.com/weapi/song/lyric", this.encryptRequest(obj.toString()), CloudMusicAPI.RequestType.POST)[0];
   }

   public String[] requestLyric(String result) {
      String lyric = "";
      String transLyric = "";
      JsonObject phase = (JsonObject)this.parser.parse(result);
      if (!phase.get("code").getAsString().contains("200")) {
         Hanabi.INSTANCE.println("解析时出现问题, 错误码 " + phase.get("code").getAsString());
         return new String[]{"", ""};
      } else if (phase.get("nolyric") != null && phase.get("nolyric").getAsBoolean()) {
         return new String[]{"_NOLYRIC_", "_NOLYRIC_"};
      } else if (phase.get("uncollected") != null && phase.get("uncollected").getAsBoolean()) {
         return new String[]{"_NOLYRIC_", "_UNCOLLECT_"};
      } else {
         if (!phase.get("lrc").isJsonNull()) {
            lyric = phase.get("lrc").getAsJsonObject().get("lyric").getAsString();
         } else {
            lyric = "";
         }

         try {
            if (!phase.get("tlyric").isJsonNull()) {
               transLyric = phase.get("tlyric").getAsJsonObject().get("lyric").getAsString();
            } else {
               transLyric = "";
            }
         } catch (Exception var6) {
            transLyric = "";
         }

         return new String[]{lyric, transLyric};
      }
   }

   public void analyzeLyric(CopyOnWriteArrayList list, String lyric) {
      try {
         String regex = "\\[([0-9]{2}):([0-9]{2}).([0-9]{1,3})\\]";
         String regex2 = "\\[([0-9]{2}):([0-9]{2})\\]";
         Pattern pattern = Pattern.compile(regex);
         Pattern pattern2 = Pattern.compile(regex2);
         String[] var7 = lyric.split("\n");
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String s = var7[var9];
            Matcher matcher = pattern.matcher(s);
            Matcher matcher2 = pattern2.matcher(s);

            String min;
            String sec;
            String text;
            while(matcher.find()) {
               min = matcher.group(1);
               sec = matcher.group(2);
               text = matcher.group(3);
               String text = s.replaceAll(regex, "");
               list.add(new Lyric(text, this.strToLong(min, sec, text)));
            }

            while(matcher2.find()) {
               min = matcher2.group(1);
               sec = matcher2.group(2);
               text = s.replaceAll(regex2, "");
               list.add(new Lyric(text, this.strToLong(min, sec, "000")));
            }
         }

         list.sort(new LyricSort());
      } catch (Exception var17) {
         var17.printStackTrace();
         Hanabi.INSTANCE.println("解析歌词时发生错误");
      }

   }

   public long strToLong(String min, String sec, String mill) {
      int minInt = Integer.parseInt(min);
      int secInt = Integer.parseInt(sec);
      int millsInt = Integer.parseInt(mill);
      return (long)minInt * 60L * 1000L + (long)secInt * 1000L + (long)millsInt * (long)(mill.length() == 2 ? 10 : 1);
   }

   public Object[] getPlaylistDetail(String playListId) throws Exception {
      JsonObject request = new JsonObject();
      request.addProperty("id", playListId);
      request.addProperty("n", 100000);
      request.addProperty("total", true);
      String json = (String)this.httpRequest("https://music.163.com/weapi/v6/playlist/detail?id=" + playListId, this.encryptRequest(request.toString()), CloudMusicAPI.RequestType.POST)[0];
      JsonObject obj = (JsonObject)this.parser.parse(json);
      if (obj.get("code").getAsInt() != 200) {
         return new Object[]{"获取歌单详情时发生错误, 错误码 " + obj.get("code").getAsInt(), null};
      } else {
         ArrayList temp = new ArrayList();
         JsonObject result = obj.getAsJsonObject("playlist");

         for(int i = 0; i < result.get("tracks").getAsJsonArray().size(); ++i) {
            StringBuilder artist = new StringBuilder();
            JsonObject shit = result.get("tracks").getAsJsonArray().get(i).getAsJsonObject();
            boolean isCloudDiskSong = shit.get("t").getAsInt() == 1;

            for(int a = 0; a < shit.get("ar").getAsJsonArray().size(); ++a) {
               JsonObject _shit = shit.get("ar").getAsJsonArray().get(a).getAsJsonObject();
               if (!_shit.get("name").isJsonNull() && !isCloudDiskSong) {
                  artist.append(_shit.get("name").getAsString()).append("/");
               } else {
                  artist = new StringBuilder(isCloudDiskSong ? "云盘歌曲/" : "未知作曲家/");
               }
            }

            artist = new StringBuilder(this.toDBC(artist.substring(0, artist.length() - 1)));
            String songName = this.toDBC(this.getObjectAsString(shit, "name"));
            temp.add(new Track(Long.parseLong(this.getObjectAsString(shit, "id")), songName.startsWith(" ") ? songName.substring(1) : songName, artist.toString(), this.getObjectAsString(shit.get("al").getAsJsonObject(), "picUrl")));
         }

         return new Object[]{"200", temp};
      }
   }

   public Object[] getDownloadUrl(String songId, long bitRate) throws Exception {
      JsonObject obj = new JsonObject();
      obj.addProperty("ids", "[" + songId + "]");
      obj.addProperty("br", String.valueOf(bitRate));
      String json = (String)this.httpRequest("https://music.163.com/weapi/song/enhance/player/url", this.encryptRequest(obj.toString()), CloudMusicAPI.RequestType.POST)[0];
      JsonObject result = (JsonObject)this.parser.parse(json);
      return result.get("code").getAsInt() != 200 ? new Object[]{"获取下载地址时发生错误, 错误码 " + result.get("code").getAsInt(), null} : new Object[]{"200", result.get("data").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString()};
   }

   public Object[] httpRequest(String url, String data, RequestType type) throws Exception {
      return this.httpRequest(url, data, (CookieStore)null, type);
   }

   public Object[] httpRequest(String url, String data, CookieStore cookie, RequestType type) throws Exception {
      RequestConfig config = RequestConfig.custom().setCookieSpec("compatibility").build();
      CookieStore cookieStore = cookie == null ? new BasicCookieStore() : cookie;
      ((CookieStore)cookieStore).clear();
      if (cookie == null) {
         String[][] var7 = this.cookies;
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String[] a = var7[var9];
            BasicClientCookie c = new BasicClientCookie(a[0], a[1]);
            c.setPath("/");
            c.setDomain("music.163.com");
            ((CookieStore)cookieStore).addCookie(c);
         }
      }

      HttpClientContext context = HttpClientContext.create();
      context.setCookieStore((CookieStore)cookieStore);
      HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).setDefaultCookieStore((CookieStore)cookieStore).build();
      String resp = "";
      switch (type) {
         case POST:
            HttpPost httpPost = new HttpPost(url);
            String[][] var21 = this.headers;
            int var23 = var21.length;

            for(int var24 = 0; var24 < var23; ++var24) {
               String[] header = var21[var24];
               httpPost.addHeader(header[0], header[1]);
            }

            httpPost.setConfig(config);
            if (data != null) {
               httpPost.setEntity(new StringEntity(data));
            }

            resp = (String)httpClient.execute(httpPost, (httpResponse) -> {
               return this.getStringFromInputStream(httpResponse.getEntity().getContent());
            }, context);
            CookieStore cs = context.getCookieStore();
            return new Object[]{resp, cs};
         case GET:
            HttpGet httpGet = new HttpGet(url);
            String[][] var13 = this.headers;
            int var14 = var13.length;

            for(int var15 = 0; var15 < var14; ++var15) {
               String[] header = var13[var15];
               httpGet.addHeader(header[0], header[1]);
            }

            httpGet.setConfig(config);
            resp = (String)httpClient.execute(httpGet, (httpResponse) -> {
               return this.getStringFromInputStream(httpResponse.getEntity().getContent());
            }, context);
            return new Object[]{resp, null};
         default:
            throw new NullPointerException("Invalid request type!");
      }
   }

   public void downloadFile(String url, String filepath) {
      try {
         HttpClient client = HttpClients.createDefault();
         HttpGet httpget = new HttpGet(url);
         HttpResponse response = client.execute(httpget);
         HttpEntity entity = response.getEntity();
         InputStream is = entity.getContent();
         long progress = 0L;
         long totalLen = entity.getContentLength();
         long unit = totalLen / 100L;
         File file = new File(filepath);
         FileOutputStream fileout = new FileOutputStream(file);
         byte[] buffer = new byte[10240];

         int ch;
         for(int ch = false; (ch = is.read(buffer)) != -1; MusicManager.INSTANCE.downloadProgress = (float)(progress / unit)) {
            fileout.write(buffer, 0, ch);
            progress += (long)ch;
         }

         is.close();
         fileout.flush();
         fileout.close();
      } catch (Exception var18) {
         var18.printStackTrace();
      }

   }

   public String encryptRequest(String text) {
      String secKey = this.createSecretKey(16);
      String nonce = "0CoJUm6Qyw8W8jud";
      String encText = this.aesEncrypt(this.aesEncrypt(text, nonce), secKey);
      String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";
      String pubKey = "010001";
      String encSecKey = this.rsaEncrypt(secKey, pubKey, modulus);

      try {
         return "params=" + URLEncoder.encode(encText, "UTF-8") + "&encSecKey=" + URLEncoder.encode(encSecKey, "UTF-8");
      } catch (UnsupportedEncodingException var9) {
         return null;
      }
   }

   public String aesEncrypt(String text, String key) {
      try {
         IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes(StandardCharsets.UTF_8));
         SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
         cipher.init(1, skeySpec, iv);
         byte[] encrypted = cipher.doFinal(text.getBytes());
         return Base64.getEncoder().encodeToString(encrypted);
      } catch (Exception var7) {
         return "";
      }
   }

   public String rsaEncrypt(String text, String pubKey, String modulus) {
      text = (new StringBuilder(text)).reverse().toString();
      BigInteger rs = (new BigInteger(String.format("%x", new BigInteger(1, text.getBytes())), 16)).modPow(new BigInteger(pubKey, 16), new BigInteger(modulus, 16));
      StringBuilder r = new StringBuilder(rs.toString(16));
      if (r.length() >= 256) {
         return r.substring(r.length() - 256);
      } else {
         while(r.length() < 256) {
            r.insert(0, 0);
         }

         return r.toString();
      }
   }

   public String createSecretKey(int length) {
      String shits = "0123456789abcdefghijklmnopqrstuvwxyz";
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i < length; ++i) {
         sb.append(shits.charAt((new Random()).nextInt(shits.length())));
      }

      return sb.toString();
   }

   public String toDBC(String input) {
      char[] c = input.toCharArray();

      for(int i = 0; i < c.length; ++i) {
         if (c[i] == 12288) {
            c[i] = ' ';
         } else if (c[i] > '\uff00' && c[i] < '｟') {
            c[i] -= 'ﻠ';
         }
      }

      return new String(c);
   }

   public String getStringFromInputStream(InputStream is) {
      BufferedReader br = null;
      StringBuilder sb = new StringBuilder();

      try {
         String line;
         try {
            for(br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)); (line = br.readLine()) != null; sb.append(line)) {
               if (sb.length() != 0) {
                  sb.append("\n");
               }
            }
         } catch (IOException var14) {
            var14.printStackTrace();
         }
      } finally {
         if (br != null) {
            try {
               br.close();
            } catch (IOException var13) {
               var13.printStackTrace();
            }
         }

      }

      return sb.toString();
   }

   public String getObjectAsString(JsonObject obj, String member) {
      return obj.get(member).getAsString();
   }

   public int getObjectAsInt(JsonObject obj, String member) {
      return obj.get(member).getAsInt();
   }

   public static enum RequestType {
      GET,
      POST;
   }

   public static class LyricSort implements Comparator {
      public int compare(Lyric a, Lyric b) {
         return Long.compare(a.time, b.time);
      }
   }
}
