package info.sigmaclient.sigma.utils;

import info.sigmaclient.sigma.SigmaNG;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateLogs {
    public static HashMap<String, UpdateLog> logs = new HashMap<>();
    public static String visitSite(String urly){
        System.out.println("Successfully got virtual storage location " + urly);
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "*/*");
        map.put("Accept-Language", "zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4");
        map.put("Connection", "keep-alive");
        map.put("Content-Type", "application/x-www-form-urlencoded");
        map.put("Cookie", "appver=2.7.1.198277; os=pc; " + "");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36");

        SigmaNG.initProxy();
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
    public static class UpdateLog {
        public String title;
        public int id;
        public ArrayList<String> strings;
        public UpdateLog(String s, ArrayList<String> s2, int id){
            title = s;
            strings = s2;
            this.id = id;
        }
    }
    public static void loadLogs(){
        String url = "https://raw.githubusercontent.com/lzyqwq/repo/main/updatelog";
        String logs = visitSite(url);
        HashMap<String, UpdateLog> logMap = new HashMap<>();
        String[] split = logs.split("\n");
        for(String s : split){
            String pa = "<(.*) id=\"(.*)\">(.*)";
            Pattern p = Pattern.compile(pa);
            Matcher m = p.matcher(s);
            boolean b = m.matches();
            if(b){
                String box_name = m.group(1);
                String id = m.group(2);
                String text = m.group(3);
                if(box_name.equals("title")) {
                    logMap.put(id, new UpdateLog(text, new ArrayList<>(), Integer.parseInt(id)));
                }else if(box_name.equals("desc")){
                    UpdateLog log = logMap.get(id);
                    log.strings.add(text);
                }
            }
        }
        UpdateLog l = new UpdateLog("Last Version", new ArrayList<>(), 114514);
        l.strings.add("[ERROR] Cannot render changelog: More than 1000 changes are trying to be displayed at the sametime. Are the developers insane ?");
        UpdateLogs.logs = logMap;
        UpdateLogs.logs.put("114514", l);
    }
    public static String[] updateLogs = new String[]{
    };
}
