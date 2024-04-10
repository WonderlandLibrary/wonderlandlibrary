package info.sigmaclient.sigma.utils.music;

import com.alibaba.fastjson.JSONObject;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import top.fl0wowp4rty.phantomshield.annotations.Native;
@Native

public class HttpClient {
    public static class ActionException extends RuntimeException {

        public ActionException(String msg){
            super(msg);
        }

        public ActionException(Text text){
            super(text.toString());
        }
    }
    private final String MainPath;
    private Map<String, String> Header;

    public HttpClient(String path){
        this.MainPath = path;
        this.Header = new HashMap<String, String>();
    }

    public HttpClient(String path, Map<String, String> header){
        this.MainPath = path;
        this.Header = header;
    }

    public void setCookies(String cookies){
        this.Header.put("Cookie", cookies);
    }

    public Map<String, String> getHeader(){
        return this.Header;
    }

    public String getCookies(){
        if(!this.Header.containsKey("Cookie")){
            return "";
        }
        return this.Header.get("Cookie");
    }

    public String getUrl(String paht){
        return MainPath + paht;
    }

    public HttpResult GET(String paht, @Nullable Map<String, Object> data){
        return this.connection(this.getUrl(paht), data, (HttpURLConnection connection) -> {
            try {
                connection.setRequestMethod("GET");
            } catch (ProtocolException exception) {
                exception.printStackTrace();
            }
            return connection;
        }, 0);
    }

    public HttpResult POST(String paht, @Nullable Map<String, Object> data){
        return this.connection(this.getUrl(paht), data, (HttpURLConnection connection) -> {
            try {
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
            } catch (ProtocolException exception) {
                exception.printStackTrace();
            }
            return connection;
        }, 0);
    }

    public static class ApiException extends RuntimeException{

        public ApiException(int code, String msg){
            super("请求错误 " + code + ": " + msg);
        }
    }

    public JSONObject POST_API(String paht, @Nullable Map<String, Object> data){
        HttpResult result = this.POST(paht, data);
        JSONObject json = result.getJson();

        int code = json.getInteger("code");
        if(code == 301){
            throw new ActionException("Cookie使用出错");
        }

        if(code != 200){
            if(json.getString("msg") != null){
                throw new ApiException(json.getInteger("code"), json.getString("msg"));
            }

            throw new ApiException(json.getInteger("code"), json.getString("message"));
        }

        return json;
    }

    public String POST_LOGIN(String paht, Map<String, Object> data){
        HttpResult result = this.POST(paht, data);
        JSONObject json = result.getJson();

        int code = (int) json.get("code");
        if(code == 400){
            throw new ActionException("400");
        }

        if(code == 501){
            throw new ActionException(Text.translatable("cloudmusic.exception.login.501"));
        }

        if(code == 502){
            throw new ActionException(Text.translatable("cloudmusic.exception.login.502"));
        }

        if(code == 503){
            throw new ActionException(Text.translatable("cloudmusic.exception.login.503"));
        }

        if(code != 200){
            throw new ActionException(Text.translatable("cloudmusic.exception.login.err.code", json.toString()));
        }

        return result.getSetCookie();
    }

    private HttpURLConnection setRequestHeader(HttpURLConnection httpConnection){
        this.Header.forEach(httpConnection::setRequestProperty);
        return httpConnection;
    }

    private byte[] setData(Map<String, Object> data){
        String[] toDataString = {""};
        data.forEach((key, value) -> {
            toDataString[0] += key + "=" + value.toString() + "&";
        });
        return toDataString[0].getBytes();
    }
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }
    private HttpResult connection(String httpUrl, @Nullable Map<String, Object> data, Connection connection, int retry){
        HttpURLConnection httpConnection = null;
        InputStream inputStream = null;
        try {
            //创建连接
            httpConnection = this.setRequestHeader(connection.set((HttpURLConnection) new URL(httpUrl).openConnection()));
            if(data != null){
                httpConnection.getOutputStream().write(this.setData(data));
            }
            //设置连接超时时间
            httpConnection.setReadTimeout(1000);
            httpConnection.connect();
            //获取响应数据
            int code = httpConnection.getResponseCode();
            if (code == 200) {
                inputStream = httpConnection.getInputStream();
                return new HttpResult(code, true, readStream(inputStream), httpConnection.getHeaderFields().get("Set-Cookie"));
            }else{
                inputStream = httpConnection.getErrorStream();
                return new HttpResult(code, false, readStream(inputStream), httpConnection.getHeaderFields().get("Set-Cookie"));
            }
        } catch (Exception err) {
            if(retry <= 1){
                throw new ActionException(Text.translatable("cloudmusic.exception.http", 3, err.getMessage()));
            }
            return this.connection(httpUrl, data, connection, ++retry);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            //关闭远程连接
            assert httpConnection != null;
            httpConnection.disconnect();
        }
    }

    public static File download(String path, File targetFile) {
        return  HttpClient.download(path, targetFile, 0);
    }

    private static File download(String path, File targetFile, int retry) {
        try {
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            } else if (targetFile.exists()){
                return targetFile;
            }

            // 统一资源
            URL url = new URL(path);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设定请求的方法
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setRequestMethod("GET");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setReadTimeout(10 * 1000);
            // 打开到此 URL 引用的资源的通信链接
            httpURLConnection.connect();

            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());
            OutputStream out = new FileOutputStream(targetFile);

            int size = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                out.write(buf, 0, size);
            }

            bin.close();
            out.close();
        } catch (Exception err) {
            if(retry <= 3){
                throw new ActionException(Text.translatable("cloudmusic.exception.http.download", 3, err.getMessage()));
            }
            return HttpClient.download(path, targetFile, ++retry);
        }
        return targetFile;
    }

    public static InputStream downloadStream(String path) {
        return HttpClient.downloadStream(path, 0);
    }

    private static InputStream downloadStream(String path, int retry) {
        InputStream bin = null;
        try {
            // 统一资源
            URL url = new URL(path);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设定请求的方法
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setRequestMethod("GET");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setReadTimeout(10 * 1000);
            // 打开到此 URL 引用的资源的通信链接
            httpURLConnection.connect();

            bin = httpURLConnection.getInputStream();
        } catch (Exception err) {
            if(retry <= 3){
                throw new ActionException(Text.translatable("cloudmusic.exception.http.download", 3, err.getMessage()));
            }
            return HttpClient.downloadStream(path, ++retry);
        }
        return bin;
    }

    private interface Connection{
        HttpURLConnection set(HttpURLConnection connection);
    }

    public class HttpResult{
        public final int code;
        public final boolean status;
        private final byte[] data;
        private final List<String> cookies;

        public HttpResult(int code, boolean status, byte[] data, List<String> cookies){
            this.code = code;
            this.status = status;
            this.cookies = cookies;

            if(data == null){
                this.data = new byte[]{};
                return;
            }
            this.data = data;
        }

        public String getString(){
            return new String(this.data, StandardCharsets.UTF_8);
        }

        public JSONObject getJson(){
            return JSONObject.parseObject(getString());
        }

        public String getSetCookie(){
            Map<String, String> cookiesMap = new HashMap<>();
            for (String cookie : this.cookies) {
                String[] cookiekeys = cookie.split("; ")[0].split("=");
                if(cookiekeys.length == 1){
                    cookiesMap.put(cookiekeys[0], "");
                    continue;
                }
                cookiesMap.put(cookiekeys[0], cookiekeys[1]);
            }

            String cookieData = "";
            for (Entry<String, String> cookiekeys: cookiesMap.entrySet()) {
                cookieData += cookiekeys.getKey() + "=" + cookiekeys.getValue() + "; ";
            }
            return cookieData;
        }
    }
}
