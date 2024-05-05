package net.minecraft.util;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IProgressUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpUtil {
    public static final ListeningExecutorService field_180193_a = MoreExecutors.listeningDecorator((ExecutorService)Executors.newCachedThreadPool((ThreadFactory)new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Downloader %d").build()));
    private static final AtomicInteger downloadThreadsStarted = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();

    public static String buildPostString(Map<String, Object> data) {
        StringBuilder stringbuilder = new StringBuilder();
        for (Map.Entry entry : data.entrySet()) {
            if (stringbuilder.length() > 0) {
                stringbuilder.append('&');
            }
            try {
                stringbuilder.append(URLEncoder.encode((String)((String)entry.getKey()), (String)"UTF-8"));
            }
            catch (UnsupportedEncodingException unsupportedencodingexception1) {
                unsupportedencodingexception1.printStackTrace();
            }
            if (entry.getValue() == null) continue;
            stringbuilder.append('=');
            try {
                stringbuilder.append(URLEncoder.encode((String)entry.getValue().toString(), (String)"UTF-8"));
            }
            catch (UnsupportedEncodingException unsupportedencodingexception) {
                unsupportedencodingexception.printStackTrace();
            }
        }
        return stringbuilder.toString();
    }

    public static String postMap(URL url, Map<String, Object> data, boolean skipLoggingErrors) {
        return HttpUtil.post(url, HttpUtil.buildPostString(data), skipLoggingErrors);
    }

    private static String post(URL url, String content, boolean skipLoggingErrors) {
        try {
            String s;
            Proxy proxy;
            Proxy proxy2 = proxy = MinecraftServer.getServer() == null ? null : MinecraftServer.getServer().getServerProxy();
            if (proxy == null) {
                proxy = Proxy.NO_PROXY;
            }
            HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection(proxy);
            httpurlconnection.setRequestMethod("POST");
            httpurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpurlconnection.setRequestProperty("Content-Length", "" + content.getBytes().length);
            httpurlconnection.setRequestProperty("Content-Language", "en-US");
            httpurlconnection.setUseCaches(false);
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);
            DataOutputStream dataoutputstream = new DataOutputStream(httpurlconnection.getOutputStream());
            dataoutputstream.writeBytes(content);
            dataoutputstream.flush();
            dataoutputstream.close();
            BufferedReader bufferedreader = new BufferedReader((Reader)new InputStreamReader(httpurlconnection.getInputStream()));
            StringBuffer stringbuffer = new StringBuffer();
            while ((s = bufferedreader.readLine()) != null) {
                stringbuffer.append(s);
                stringbuffer.append('\r');
            }
            bufferedreader.close();
            return stringbuffer.toString();
        }
        catch (Exception exception) {
            if (!skipLoggingErrors) {
                logger.error("Could not post to " + url, (Throwable)exception);
            }
            return "";
        }
    }

    public static ListenableFuture<Object> downloadResourcePack(File saveFile, String packUrl, Map<String, String> p_180192_2_, int maxSize, IProgressUpdate p_180192_4_, Proxy p_180192_5_) {
        ListenableFuture listenablefuture = field_180193_a.submit((Runnable)new /* Unavailable Anonymous Inner Class!! */);
        return listenablefuture;
    }

    public static int getSuitableLanPort() throws IOException {
        ServerSocket serversocket = null;
        int i = -1;
        try {
            serversocket = new ServerSocket(0);
            i = serversocket.getLocalPort();
        }
        finally {
            try {
                if (serversocket != null) {
                    serversocket.close();
                }
            }
            catch (IOException iOException) {}
        }
        return i;
    }

    public static String get(URL url) throws IOException {
        String s;
        HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection();
        httpurlconnection.setRequestMethod("GET");
        BufferedReader bufferedreader = new BufferedReader((Reader)new InputStreamReader(httpurlconnection.getInputStream()));
        StringBuilder stringbuilder = new StringBuilder();
        while ((s = bufferedreader.readLine()) != null) {
            stringbuilder.append(s);
            stringbuilder.append('\r');
        }
        bufferedreader.close();
        return stringbuilder.toString();
    }

    static /* synthetic */ Logger access$000() {
        return logger;
    }
}
