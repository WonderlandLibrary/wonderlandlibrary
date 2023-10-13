/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.ListenableFuture
 *  com.google.common.util.concurrent.ListeningExecutorService
 *  com.google.common.util.concurrent.MoreExecutors
 *  com.google.common.util.concurrent.ThreadFactoryBuilder
 *  org.apache.commons.io.FileUtils
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.util;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IProgressUpdate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpUtil {
    public static final ListeningExecutorService field_180193_a = MoreExecutors.listeningDecorator((ExecutorService)Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Downloader %d").build()));
    private static final AtomicInteger downloadThreadsStarted = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private static final String __OBFID = "CL_00001485";

    public static String buildPostString(Map data) {
        StringBuilder var1 = new StringBuilder();
        for (Map.Entry var3 : data.entrySet()) {
            if (var1.length() > 0) {
                var1.append('&');
            }
            try {
                var1.append(URLEncoder.encode((String)var3.getKey(), "UTF-8"));
            }
            catch (UnsupportedEncodingException var6) {
                var6.printStackTrace();
            }
            if (var3.getValue() == null) continue;
            var1.append('=');
            try {
                var1.append(URLEncoder.encode(var3.getValue().toString(), "UTF-8"));
            }
            catch (UnsupportedEncodingException var5) {
                var5.printStackTrace();
            }
        }
        return var1.toString();
    }

    public static String postMap(URL url, Map data, boolean skipLoggingErrors) {
        return HttpUtil.post(url, HttpUtil.buildPostString(data), skipLoggingErrors);
    }

    private static String post(URL url, String content, boolean skipLoggingErrors) {
        try {
            String var7;
            Proxy var3;
            Proxy proxy = var3 = MinecraftServer.getServer() == null ? null : MinecraftServer.getServer().getServerProxy();
            if (var3 == null) {
                var3 = Proxy.NO_PROXY;
            }
            HttpURLConnection var4 = (HttpURLConnection)url.openConnection(var3);
            var4.setRequestMethod("POST");
            var4.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            var4.setRequestProperty("Content-Length", "" + content.getBytes().length);
            var4.setRequestProperty("Content-Language", "en-US");
            var4.setUseCaches(false);
            var4.setDoInput(true);
            var4.setDoOutput(true);
            DataOutputStream var5 = new DataOutputStream(var4.getOutputStream());
            var5.writeBytes(content);
            var5.flush();
            var5.close();
            BufferedReader var6 = new BufferedReader(new InputStreamReader(var4.getInputStream()));
            StringBuffer var8 = new StringBuffer();
            while ((var7 = var6.readLine()) != null) {
                var8.append(var7);
                var8.append('\r');
            }
            var6.close();
            return var8.toString();
        }
        catch (Exception var9) {
            if (!skipLoggingErrors) {
                logger.error("Could not post to " + url, (Throwable)var9);
            }
            return "";
        }
    }

    public static ListenableFuture func_180192_a(final File p_180192_0_, final String p_180192_1_, final Map p_180192_2_, final int p_180192_3_, final IProgressUpdate p_180192_4_, final Proxy p_180192_5_) {
        ListenableFuture var6 = field_180193_a.submit(new Runnable(){
            private static final String __OBFID = "CL_00001486";

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public void run() {
                block15: {
                    block16: {
                        var2 = null;
                        var3 = null;
                        if (p_180192_4_ != null) {
                            p_180192_4_.resetProgressAndMessage("Downloading Resource Pack");
                            p_180192_4_.displayLoadingString("Making Request...");
                        }
                        var4 = new byte[4096];
                        var5 = new URL(p_180192_1_);
                        var1 = var5.openConnection(p_180192_5_);
                        var6 = 0.0f;
                        var7 = p_180192_2_.entrySet().size();
                        for (Map.Entry<K, V> var9 : p_180192_2_.entrySet()) {
                            var1.setRequestProperty((String)var9.getKey(), (String)var9.getValue());
                            if (p_180192_4_ == null) continue;
                            p_180192_4_.setLoadingProgress((int)((var6 += 1.0f) / var7 * 100.0f));
                        }
                        var2 = var1.getInputStream();
                        var7 = var1.getContentLength();
                        var16 = var1.getContentLength();
                        if (p_180192_4_ != null) {
                            p_180192_4_.displayLoadingString(String.format("Downloading file (%.2f MB)...", new Object[]{Float.valueOf(var7 / 1000.0f / 1000.0f)}));
                        }
                        if (!p_180192_0_.exists()) ** GOTO lbl37
                        var17 = p_180192_0_.length();
                        if (var17 != (long)var16) break block15;
                        if (p_180192_4_ == null) break block16;
                        p_180192_4_.setDoneWorking();
                    }
                    IOUtils.closeQuietly((InputStream)var2);
                    IOUtils.closeQuietly((OutputStream)var3);
                    return;
                }
                try {
                    block17: {
                        HttpUtil.access$0().warn("Deleting " + p_180192_0_ + " as it does not match what we currently have (" + var16 + " vs our " + var17 + ").");
                        FileUtils.deleteQuietly((File)p_180192_0_);
                        break block17;
lbl37:
                        // 1 sources

                        if (p_180192_0_.getParentFile() != null) {
                            p_180192_0_.getParentFile().mkdirs();
                        }
                    }
                    var3 = new DataOutputStream(new FileOutputStream(p_180192_0_));
                    if (p_180192_3_ > 0 && var7 > (float)p_180192_3_) {
                        if (p_180192_4_ == null) throw new IOException("Filesize is bigger than maximum allowed (file is " + var6 + ", limit is " + p_180192_3_ + ")");
                        p_180192_4_.setDoneWorking();
                        throw new IOException("Filesize is bigger than maximum allowed (file is " + var6 + ", limit is " + p_180192_3_ + ")");
                    }
                    var18 = false;
                    while (true) {
                        if ((var19 = var2.read(var4)) < 0) {
                            if (p_180192_4_ == null) return;
                            p_180192_4_.setDoneWorking();
                            break;
                        }
                        var6 += (float)var19;
                        if (p_180192_4_ != null) {
                            p_180192_4_.setLoadingProgress((int)(var6 / var7 * 100.0f));
                        }
                        if (p_180192_3_ > 0 && var6 > (float)p_180192_3_) {
                            if (p_180192_4_ == null) throw new IOException("Filesize was bigger than maximum allowed (got >= " + var6 + ", limit was " + p_180192_3_ + ")");
                            p_180192_4_.setDoneWorking();
                            throw new IOException("Filesize was bigger than maximum allowed (got >= " + var6 + ", limit was " + p_180192_3_ + ")");
                        }
                        var3.write(var4, 0, var19);
                    }
                }
                catch (Throwable var14) {
                    try {
                        var14.printStackTrace();
                        return;
                    }
                    catch (Throwable var12_15) {
                        throw var12_15;
                    }
                    finally {
                        IOUtils.closeQuietly(var2);
                        IOUtils.closeQuietly(var3);
                    }
                }
                IOUtils.closeQuietly((InputStream)var2);
                IOUtils.closeQuietly((OutputStream)var3);
                return;
            }
        });
        return var6;
    }

    public static int getSuitableLanPort() throws IOException {
        int var10;
        ServerSocket var0 = null;
        boolean var1 = true;
        try {
            var0 = new ServerSocket(0);
            var10 = var0.getLocalPort();
        }
        finally {
            try {
                if (var0 != null) {
                    var0.close();
                }
            }
            catch (IOException iOException) {}
        }
        return var10;
    }

    public static String get(URL url) throws IOException {
        String var3;
        HttpURLConnection var1 = (HttpURLConnection)url.openConnection();
        var1.setRequestMethod("GET");
        BufferedReader var2 = new BufferedReader(new InputStreamReader(var1.getInputStream()));
        StringBuilder var4 = new StringBuilder();
        while ((var3 = var2.readLine()) != null) {
            var4.append(var3);
            var4.append('\r');
        }
        var2.close();
        return var4.toString();
    }

    static /* synthetic */ Logger access$0() {
        return logger;
    }
}

