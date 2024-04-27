/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.apache.commons.codec.digest.DigestUtils
 *  org.apache.http.HttpEntity
 *  org.apache.http.HttpResponse
 *  org.apache.http.client.CookieStore
 *  org.apache.http.client.config.RequestConfig
 *  org.apache.http.client.methods.HttpGet
 *  org.apache.http.client.methods.HttpPost
 *  org.apache.http.client.methods.HttpUriRequest
 *  org.apache.http.client.protocol.HttpClientContext
 *  org.apache.http.cookie.Cookie
 *  org.apache.http.entity.StringEntity
 *  org.apache.http.impl.client.BasicCookieStore
 *  org.apache.http.impl.client.CloseableHttpClient
 *  org.apache.http.impl.client.HttpClients
 *  org.apache.http.impl.cookie.BasicClientCookie
 *  org.apache.http.protocol.HttpContext
 */
package cn.hanabi.musicplayer.api;

import cn.hanabi.musicplayer.MusicManager;
import cn.hanabi.musicplayer.api.PlayList;
import cn.hanabi.musicplayer.impl.Lyric;
import cn.hanabi.musicplayer.impl.Track;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
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
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;

public enum CloudMusicAPI {
    INSTANCE;

    private final String[][] headers;
    private final JsonParser parser;
    public String[][] cookies;

    /*
     * Exception decompiling
     */
    private CloudMusicAPI() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Underrun type stack
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getEntry(StackSim.java:35)
         *     at org.benf.cfr.reader.bytecode.opcode.OperationFactoryDefault.getStackTypes(OperationFactoryDefault.java:51)
         *     at org.benf.cfr.reader.bytecode.opcode.OperationFactoryDup.getStackDelta(OperationFactoryDup.java:18)
         *     at org.benf.cfr.reader.bytecode.opcode.JVMInstr.getStackDelta(JVMInstr.java:315)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:199)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public Object[] loginPhone(String phoneNum, String passwd) throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("phone", phoneNum);
        obj.addProperty("password", DigestUtils.md5Hex((byte[])passwd.getBytes()));
        obj.addProperty("rememberLogin", Boolean.valueOf(true));
        String data = this.encryptRequest(obj.toString());
        return this.httpRequest("https://music.163.com/weapi/login/cellphone", data, RequestType.POST);
    }

    public String QRKey() throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", (Number)1);
        String data = this.encryptRequest(obj.toString());
        return ((JsonObject)this.parser.parse((String)this.httpRequest("https://music.163.com/weapi/login/qrcode/unikey", data, RequestType.POST)[0])).get("unikey").getAsString();
    }

    public Object[] QRState(String key) throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", (Number)1);
        obj.addProperty("key", key);
        String data = this.encryptRequest(obj.toString());
        Object[] request = this.httpRequest("https://music.163.com/weapi/login/qrcode/client/login", data, RequestType.POST);
        JsonObject result = (JsonObject)this.parser.parse((String)request[0]);
        int code = result.get("code").getAsInt();
        return new Object[]{code, request[1]};
    }

    public Object[] refreshState() throws Exception {
        return this.httpRequest("https://music.163.com/weapi/login/token/refresh", null, RequestType.POST);
    }

    public Object[] getPlayList(String userId) throws Exception {
        String json = (String)this.httpRequest("http://music.163.com/api/user/playlist/?offset=0&limit=100&uid=" + userId, null, RequestType.GET)[0];
        JsonObject obj = (JsonObject)this.parser.parse(json);
        if (obj.get("code").getAsInt() != 200) {
            return new Object[]{"\u83b7\u53d6\u6b4c\u5355\u5217\u8868\u65f6\u53d1\u751f\u9519\u8bef, \u9519\u8bef\u7801 " + obj.get("code").getAsInt(), null};
        }
        ArrayList<PlayList> temp = new ArrayList<PlayList>();
        for (int i = 0; i < obj.get("playlist").getAsJsonArray().size(); ++i) {
            JsonObject shit = obj.get("playlist").getAsJsonArray().get(i).getAsJsonObject();
            temp.add(new PlayList(this.toDBC(this.getObjectAsString(shit, "name")), this.getObjectAsString(shit, "id")));
        }
        return new Object[]{"200", temp};
    }

    public String getLyricJson(String songId) throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", songId);
        obj.addProperty("lv", (Number)-1);
        obj.addProperty("tv", (Number)-1);
        return (String)this.httpRequest("https://music.163.com/weapi/song/lyric", this.encryptRequest(obj.toString()), RequestType.POST)[0];
    }

    public String[] requestLyric(String result) {
        String lyric = "";
        String transLyric = "";
        JsonObject phase = (JsonObject)this.parser.parse(result);
        if (!phase.get("code").getAsString().contains("200")) {
            System.out.println("\u89e3\u6790\u65f6\u51fa\u73b0\u95ee\u9898, \u9519\u8bef\u7801 " + phase.get("code").getAsString());
            return new String[]{"", ""};
        }
        if (phase.get("nolyric") != null && phase.get("nolyric").getAsBoolean()) {
            return new String[]{"_NOLYRIC_", "_NOLYRIC_"};
        }
        if (phase.get("uncollected") != null && phase.get("uncollected").getAsBoolean()) {
            return new String[]{"_NOLYRIC_", "_UNCOLLECT_"};
        }
        lyric = !phase.get("lrc").isJsonNull() ? phase.get("lrc").getAsJsonObject().get("lyric").getAsString() : "";
        try {
            transLyric = !phase.get("tlyric").isJsonNull() ? phase.get("tlyric").getAsJsonObject().get("lyric").getAsString() : "";
        }
        catch (Exception ex) {
            transLyric = "";
        }
        return new String[]{lyric, transLyric};
    }

    public void analyzeLyric(CopyOnWriteArrayList<Lyric> list, String lyric) {
        try {
            String regex = "\\[([0-9]{2}):([0-9]{2}).([0-9]{1,3})\\]";
            String regex2 = "\\[([0-9]{2}):([0-9]{2})\\]";
            Pattern pattern = Pattern.compile(regex);
            Pattern pattern2 = Pattern.compile(regex2);
            for (String s : lyric.split("\n")) {
                String sec;
                String min;
                Matcher matcher = pattern.matcher(s);
                Matcher matcher2 = pattern2.matcher(s);
                while (matcher.find()) {
                    min = matcher.group(1);
                    sec = matcher.group(2);
                    String mills = matcher.group(3);
                    String text = s.replaceAll(regex, "");
                    list.add(new Lyric(text, this.strToLong(min, sec, mills)));
                }
                while (matcher2.find()) {
                    min = matcher2.group(1);
                    sec = matcher2.group(2);
                    String text = s.replaceAll(regex2, "");
                    list.add(new Lyric(text, this.strToLong(min, sec, "000")));
                }
            }
            list.sort(new LyricSort());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("\u89e3\u6790\u6b4c\u8bcd\u65f6\u53d1\u751f\u9519\u8bef");
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
        request.addProperty("n", (Number)100000);
        request.addProperty("total", Boolean.valueOf(true));
        String json = (String)this.httpRequest("https://music.163.com/weapi/v6/playlist/detail?id=" + playListId, this.encryptRequest(request.toString()), RequestType.POST)[0];
        JsonObject obj = (JsonObject)this.parser.parse(json);
        if (obj.get("code").getAsInt() != 200) {
            return new Object[]{"\u83b7\u53d6\u6b4c\u5355\u8be6\u60c5\u65f6\u53d1\u751f\u9519\u8bef, \u9519\u8bef\u7801 " + obj.get("code").getAsInt(), null};
        }
        ArrayList<Track> temp = new ArrayList<Track>();
        JsonObject result = obj.getAsJsonObject("playlist");
        for (int i = 0; i < result.get("tracks").getAsJsonArray().size(); ++i) {
            StringBuilder artist = new StringBuilder();
            JsonObject shit = result.get("tracks").getAsJsonArray().get(i).getAsJsonObject();
            boolean isCloudDiskSong = shit.get("t").getAsInt() == 1;
            for (int a = 0; a < shit.get("ar").getAsJsonArray().size(); ++a) {
                JsonObject _shit = shit.get("ar").getAsJsonArray().get(a).getAsJsonObject();
                if (_shit.get("name").isJsonNull() || isCloudDiskSong) {
                    artist = new StringBuilder(isCloudDiskSong ? "\u4e91\u76d8\u6b4c\u66f2/" : "\u672a\u77e5\u4f5c\u66f2\u5bb6/");
                    continue;
                }
                artist.append(_shit.get("name").getAsString()).append("/");
            }
            artist = new StringBuilder(this.toDBC(artist.substring(0, artist.length() - 1)));
            String songName = this.toDBC(this.getObjectAsString(shit, "name"));
            temp.add(new Track(Long.parseLong(this.getObjectAsString(shit, "id")), songName.startsWith(" ") ? songName.substring(1) : songName, artist.toString(), this.getObjectAsString(shit.get("al").getAsJsonObject(), "picUrl")));
        }
        return new Object[]{"200", temp};
    }

    public Object[] getDownloadUrl(String songId, long bitRate) throws Exception {
        JsonObject obj = new JsonObject();
        obj.addProperty("ids", "[" + songId + "]");
        obj.addProperty("br", String.valueOf(bitRate));
        String json = (String)this.httpRequest("https://music.163.com/weapi/song/enhance/player/url", this.encryptRequest(obj.toString()), RequestType.POST)[0];
        JsonObject result = (JsonObject)this.parser.parse(json);
        if (result.get("code").getAsInt() != 200) {
            return new Object[]{"\u83b7\u53d6\u4e0b\u8f7d\u5730\u5740\u65f6\u53d1\u751f\u9519\u8bef, \u9519\u8bef\u7801 " + result.get("code").getAsInt(), null};
        }
        return new Object[]{"200", result.get("data").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString()};
    }

    public Object[] httpRequest(String url, String data, RequestType type) throws Exception {
        return this.httpRequest(url, data, null, type);
    }

    public Object[] httpRequest(String url, String data, CookieStore cookie, RequestType type) throws Exception {
        RequestConfig config = RequestConfig.custom().setCookieSpec("compatibility").build();
        CookieStore cookieStore = cookie == null ? new BasicCookieStore() : cookie;
        cookieStore.clear();
        if (cookie == null) {
            for (String[] a : this.cookies) {
                String[][] c = new BasicClientCookie(a[0], a[1]);
                c.setPath("/");
                c.setDomain("music.163.com");
                cookieStore.addCookie((Cookie)c);
            }
        }
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).setDefaultCookieStore(cookieStore).build();
        String resp = "";
        switch (type) {
            case POST: {
                HttpPost httpPost = new HttpPost(url);
                for (String[] header : this.headers) {
                    httpPost.addHeader(header[0], header[1]);
                }
                httpPost.setConfig(config);
                if (data != null) {
                    httpPost.setEntity((HttpEntity)new StringEntity(data));
                }
                resp = (String)httpClient.execute((HttpUriRequest)httpPost, httpResponse -> this.getStringFromInputStream(httpResponse.getEntity().getContent()), (HttpContext)context);
                CookieStore cs = context.getCookieStore();
                return new Object[]{resp, cs};
            }
            case GET: {
                HttpGet httpGet = new HttpGet(url);
                for (String[] header : this.headers) {
                    httpGet.addHeader(header[0], header[1]);
                }
                httpGet.setConfig(config);
                resp = (String)httpClient.execute((HttpUriRequest)httpGet, httpResponse -> this.getStringFromInputStream(httpResponse.getEntity().getContent()), (HttpContext)context);
                return new Object[]{resp, null};
            }
        }
        throw new NullPointerException("Invalid request type!");
    }

    public void downloadFile(String url, String filepath) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute((HttpUriRequest)httpget);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            long progress = 0L;
            long totalLen = entity.getContentLength();
            long unit = totalLen / 100L;
            File file = new File(filepath);
            FileOutputStream fileout = new FileOutputStream(file);
            byte[] buffer = new byte[10240];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer, 0, ch);
                MusicManager.INSTANCE.downloadProgress = (progress += (long)ch) / unit;
            }
            is.close();
            fileout.flush();
            fileout.close();
        }
        catch (Exception e) {
            e.printStackTrace();
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
        }
        catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public String aesEncrypt(String text, String key) {
        try {
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, (Key)skeySpec, iv);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        }
        catch (Exception ex) {
            return "";
        }
    }

    /*
     * Exception decompiling
     */
    public String rsaEncrypt(String text, String pubKey, String modulus) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl55 : ALOAD - null : trying to set 1 previously set to 0
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public String createSecretKey(int length) {
        String shits = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            sb.append(shits.charAt(new Random().nextInt(shits.length())));
        }
        return sb.toString();
    }

    /*
     * Exception decompiling
     */
    public String toDBC(String input) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * java.lang.IllegalStateException: Invisible function parameters on a non-constructor (or reads of uninitialised local variables).
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.assignSSAIdentifiers(Op02WithProcessedDataAndRefs.java:1631)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.discoverStorageLiveness(Op02WithProcessedDataAndRefs.java:1871)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:461)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getStringFromInputStream(InputStream is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((line = br.readLine()) != null) {
                if (sb.length() != 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
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

    public static class LyricSort
    implements Comparator<Lyric> {
        @Override
        public int compare(Lyric a, Lyric b) {
            return Long.compare(a.time, b.time);
        }
    }

    public static enum RequestType {
        GET,
        POST;

    }
}

