package info.sigmaclient.sigma.music.youtubedl;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.sigma5.youtubedl.YoutubeDownloader;
import info.sigmaclient.sigma.config.ConfigManager;
import info.sigmaclient.sigma.modules.gui.hide.ClickGUI;
import info.sigmaclient.sigma.gui.clickgui.musicplayer.JelloMusicPlayer;
import info.sigmaclient.sigma.utils.ChatUtils;
import info.sigmaclient.sigma.utils.music.Music;
import it.sauronsoftware.jave.Encoder;

import java.io.*;
import java.net.*;
import java.util.*;


public class YoutubeVideoHelper {
    static String apikey = "NoneLOL-Needit By Yourself";
    static HttpTransport httpTransport = new NetHttpTransport();
    static JsonFactory jsonFactory = new JacksonFactory();
    static YouTube youtube = new YouTube.Builder(httpTransport, jsonFactory, request -> {}).setApplicationName("youtube").build();

    public static String agent1 = "User-Agent";
    public static String agent2 = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";

    public static String visitSite2(String urly) throws IOException {
        String url = urly;
        if(!ConfigManager.musicDir.exists())
            ConfigManager.musicDir.mkdir();
        // 创建HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "*/*");
        map.put("Accept-Language", "zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4");
        map.put("Connection", "keep-alive");
        map.put("Content-Type", "application/x-www-form-urlencoded");
        map.put("Cookie", "appver=2.7.1.198277; os=pc; " + "");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
        map.forEach(connection::setRequestProperty);
        // 检查响应状态
        // 设置不跟随重定向
        connection.setInstanceFollowRedirects(false);

        // 获取响应状态码
        int statusCode = connection.getResponseCode();

        // 检查响应状态码是否为 3XX
        if (statusCode >= 300 && statusCode < 400) {
            // 响应为重定向
            System.out.println("Redirected to: " + connection.getURL());
        } else {
            // 响应不是重定向
            System.out.println("Not redirected");
        }

        ArrayList<String> lines = new ArrayList<>();
        StringBuilder stuff = new StringBuilder();
        try {
            String line = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = in.readLine()) != null) {
                lines.add(line + "\n");
            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        for(String s : lines){
            stuff.append(s);
        }
        return stuff.toString();
    }
    public static String visitSite(String urly){
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "*/*");
        map.put("Accept-Language", "zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4");
        map.put("Connection", "keep-alive");
        map.put("Content-Type", "application/x-www-form-urlencoded");
        map.put("Cookie", "appver=2.7.1.198277; os=pc; ");
        map.put("User-Agent", "NEXTGEN_SIGMA");

        ArrayList<String> lines = new ArrayList<>();
        StringBuilder stuff = new StringBuilder();
        URL url;
        try {
            String line;
            url = new URL(urly);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            map.forEach(connection::setRequestProperty);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = in.readLine()) != null) {
                lines.add(line + "\n");
            }
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        for(String s : lines){
            stuff.append(s);
        }
        return stuff.toString();
    }
    public static String downLoadToFile(String s) throws IOException {
        String url = s;
        if(!ConfigManager.musicDir.exists())
            ConfigManager.musicDir.mkdir();
        // 创建HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
//        connection.setRequestMethod("GET");
//        connection.setRequestProperty("Accept", "application/octet-stream");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "*/*");
        map.put("Accept-Language", "zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4");
        map.put("Connection", "keep-alive");
        map.put("Content-Type", "application/x-www-form-urlencoded");
        map.put("Referer", "mdelta.123tokyo.xyz");
        map.put("Host", "mdelta.123tokyo.xyz");
        map.put("Cookie", "appver=2.7.1.198277; os=pc; " + "");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");
        map.forEach(connection::setRequestProperty);
        // 检查响应状态
        // 设置不跟随重定向
        connection.setInstanceFollowRedirects(false);

        // 获取响应状态码
        int statusCode = connection.getResponseCode();

        // 检查响应状态码是否为 3XX
        if (statusCode >= 300 && statusCode < 400) {
            // 响应为重定向
            System.out.println("Redirected to: " + connection.getURL());
        } else {
            // 响应不是重定向
            System.out.println("Not redirected");
        }

        // 创建文件
        File file = new File(ConfigManager.musicDir,s.hashCode() + ".mp3");

        // 创建输出流
        FileOutputStream outputStream = new FileOutputStream(file);
        InputStream inputStream = connection.getInputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len=inputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,len);//写出这个数据
        }
        inputStream.close();
        connection.disconnect();//断开连接
        outputStream.close();

        return file.toURI().toString();
    }
    static Thread thread = null;
    public static void playMusicLink(Music music) {
        if(thread != null && thread.isAlive()){
            thread.stop();
        }
        thread = (new Thread(() -> {
            try {
//                String urls = "";
//                String s2 = "";
//                s2 = visitSite(music.ytid);
//                urls = s2.split("\\{\"link\":\"")[1].split("\"")[0];
//                System.out.println(urls);
//                System.out.println(s2);
//                for (int i = 0; i < 2; i++) {
//                    if (urls.isEmpty()) {
//                        s2 = visitSite(music.ytid);
//                        urls = s2.split("\\{\"link\":\"")[1].split("\"")[0];
//                        System.out.println(urls);
//                        System.out.println(s2);
//                        Thread.sleep(5000);
//                    } else break;
//                }
//                if (urls.isEmpty()) {
//                    ChatUtils.sendMessageWithPrefix("Cant get, too large or not availed");
//                    return;
//                }
//                urls = urls.replaceAll("\\\\/", "/");
//                urls = new URL(urls).toURI().toString();
//                System.out.println("RETURN: " + urls);

                new YoutubeDownloader().download(music.ytid, (file)->{
                    ClickGUI.clickGui.musicPlayer.mp3.SetPlayAudioPathYT(file, music);
                    JelloMusicPlayer.currentSongLength = (int) getMp4Duration(file);
                    System.out.println("Successfully got virtual storage location " + JelloMusicPlayer.currentSongLength + "\n");
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        thread.start();
    }
    /**
     * 获取视频文件的播放长度(mp4、mov格式)
     * @param videoPath
     * @return 单位为毫秒
     */
    public static long getMp4Duration(String videoPath) {
        File source = new File(videoPath);
        Encoder encoder = new Encoder();
        long ls = 0;
        try {
            it.sauronsoftware.jave.MultimediaInfo m = encoder.getInfo(source);
            ls = m.getDuration();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ls;
    }
    static public List<Music> search(String channelId) throws IOException {
        SigmaNG.initProxy();
        if(channelId.equals("NCS")){
            channelId = "NCS - Copyright Free Music";
        }

        System.out.println("Start!");
        List<Music> music = new ArrayList<>();
        List<SearchResult> results = get(channelId);
        for(SearchResult searchResult : results){
            Music music1 = new Music();
            music1.copyRight = false;
            String t = searchResult.getSnippet().getTitle();
//            boolean c = t.contains(" - ");
//            if(c) {
//                music1.aliasName = t.split(" - ")[1];
//                music1.name = t.split(" - ")[0];
//            }else{
                music1.name = t.replace("&#39;", "'");
                if(music1.name.contains(" - ")){
                    music1.name = music1.name.split(" - ")[0];
                }
                music1.aliasName = searchResult.getSnippet().getChannelTitle();
//            }
            music1.ytid = searchResult.getId().getVideoId();
            music1.picUrl = searchResult.getSnippet().getThumbnails().getDefault().getUrl();
            music1.download();
            music.add(music1);
        }
        return music;
    }
    static public List<SearchResult> get(String channelId) throws IOException {
        YouTube.Search.List search = youtube.search().list("id,snippet");
        search.setKey("AIzaSyDRdF7fEq0COKFwetb4PXn3oGT6m65MWnM");
        search.setType("video");
        search.setFields("items(id/videoId,snippet/title,snippet/channelTitle,snippet/thumbnails/default/url),pageInfo");
        search.setMaxResults(20L);
//        search.setOrder("mostPopular");
        search.setQ(channelId);

        SearchListResponse searchResponse;
        System.out.println("reponse!");
        try {
            searchResponse = search.execute();
            System.out.println("done search reponse");
        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            ChatUtils.sendMessageWithPrefix("Api key is over, please try tomorrow");
            return null;
        }
//        Gson gson = new Gson();
//        SearchListResponse request = gson.fromJson(visitSite("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&q=" + channelId.replace(" ", "%20") + "&type=video&key="+apikey), SearchListResponse.class);

        List<SearchResult> searchResultList = searchResponse.getItems();

        searchResultList.forEach(System.out::println);
        System.out.println("OKay!");
//        httpTransport.shutdown();
        return searchResultList;
    }
}
