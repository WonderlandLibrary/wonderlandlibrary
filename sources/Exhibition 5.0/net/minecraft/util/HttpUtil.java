// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.logging.log4j.LogManager;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.net.ServerSocket;
import java.net.URLConnection;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import org.apache.commons.io.FileUtils;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import net.minecraft.server.MinecraftServer;
import java.net.URL;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.common.util.concurrent.ListeningExecutorService;

public class HttpUtil
{
    public static final ListeningExecutorService field_180193_a;
    private static final AtomicInteger downloadThreadsStarted;
    private static final Logger logger;
    private static final String __OBFID = "CL_00001485";
    
    public static String buildPostString(final Map data) {
        final StringBuilder var1 = new StringBuilder();
        for (final Map.Entry var3 : data.entrySet()) {
            if (var1.length() > 0) {
                var1.append('&');
            }
            try {
                var1.append(URLEncoder.encode(var3.getKey(), "UTF-8"));
            }
            catch (UnsupportedEncodingException var4) {
                var4.printStackTrace();
            }
            if (var3.getValue() != null) {
                var1.append('=');
                try {
                    var1.append(URLEncoder.encode(var3.getValue().toString(), "UTF-8"));
                }
                catch (UnsupportedEncodingException var5) {
                    var5.printStackTrace();
                }
            }
        }
        return var1.toString();
    }
    
    public static String postMap(final URL url, final Map data, final boolean skipLoggingErrors) {
        return post(url, buildPostString(data), skipLoggingErrors);
    }
    
    private static String post(final URL url, final String content, final boolean skipLoggingErrors) {
        try {
            Proxy var3 = (MinecraftServer.getServer() == null) ? null : MinecraftServer.getServer().getServerProxy();
            if (var3 == null) {
                var3 = Proxy.NO_PROXY;
            }
            final HttpURLConnection var4 = (HttpURLConnection)url.openConnection(var3);
            var4.setRequestMethod("POST");
            var4.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            var4.setRequestProperty("Content-Length", "" + content.getBytes().length);
            var4.setRequestProperty("Content-Language", "en-US");
            var4.setUseCaches(false);
            var4.setDoInput(true);
            var4.setDoOutput(true);
            final DataOutputStream var5 = new DataOutputStream(var4.getOutputStream());
            var5.writeBytes(content);
            var5.flush();
            var5.close();
            final BufferedReader var6 = new BufferedReader(new InputStreamReader(var4.getInputStream()));
            final StringBuffer var7 = new StringBuffer();
            String var8;
            while ((var8 = var6.readLine()) != null) {
                var7.append(var8);
                var7.append('\r');
            }
            var6.close();
            return var7.toString();
        }
        catch (Exception var9) {
            if (!skipLoggingErrors) {
                HttpUtil.logger.error("Could not post to " + url, (Throwable)var9);
            }
            return "";
        }
    }
    
    public static ListenableFuture func_180192_a(final File p_180192_0_, final String p_180192_1_, final Map p_180192_2_, final int p_180192_3_, final IProgressUpdate p_180192_4_, final Proxy p_180192_5_) {
        final ListenableFuture var6 = HttpUtil.field_180193_a.submit((Runnable)new Runnable() {
            private static final String __OBFID = "CL_00001486";
            
            @Override
            public void run() {
                InputStream var2 = null;
                DataOutputStream var3 = null;
                while (true) {
                    if (p_180192_4_ != null) {
                        p_180192_4_.resetProgressAndMessage("Downloading Resource Pack");
                        p_180192_4_.displayLoadingString("Making Request...");
                        try {
                            final byte[] var4 = new byte[4096];
                            final URL var5 = new URL(p_180192_1_);
                            final URLConnection var6 = var5.openConnection(p_180192_5_);
                            float var7 = 0.0f;
                            float var8 = p_180192_2_.entrySet().size();
                            for (final Map.Entry var10 : p_180192_2_.entrySet()) {
                                var6.setRequestProperty(var10.getKey(), var10.getValue());
                                if (p_180192_4_ != null) {
                                    p_180192_4_.setLoadingProgress((int)(++var7 / var8 * 100.0f));
                                }
                            }
                            var2 = var6.getInputStream();
                            var8 = var6.getContentLength();
                            final int var11 = var6.getContentLength();
                            if (p_180192_4_ != null) {
                                p_180192_4_.displayLoadingString(String.format("Downloading file (%.2f MB)...", var8 / 1000.0f / 1000.0f));
                            }
                            if (p_180192_0_.exists()) {
                                final long var12 = p_180192_0_.length();
                                if (var12 == var11) {
                                    if (p_180192_4_ != null) {
                                        p_180192_4_.setDoneWorking();
                                    }
                                    return;
                                }
                                HttpUtil.logger.warn("Deleting " + p_180192_0_ + " as it does not match what we currently have (" + var11 + " vs our " + var12 + ").");
                                FileUtils.deleteQuietly(p_180192_0_);
                            }
                            else if (p_180192_0_.getParentFile() != null) {
                                p_180192_0_.getParentFile().mkdirs();
                            }
                            var3 = new DataOutputStream(new FileOutputStream(p_180192_0_));
                            if (p_180192_3_ > 0 && var8 > p_180192_3_) {
                                if (p_180192_4_ != null) {
                                    p_180192_4_.setDoneWorking();
                                }
                                throw new IOException("Filesize is bigger than maximum allowed (file is " + var7 + ", limit is " + p_180192_3_ + ")");
                            }
                            final boolean var13 = false;
                            int var14;
                            while ((var14 = var2.read(var4)) >= 0) {
                                var7 += var14;
                                if (p_180192_4_ != null) {
                                    p_180192_4_.setLoadingProgress((int)(var7 / var8 * 100.0f));
                                }
                                if (p_180192_3_ > 0 && var7 > p_180192_3_) {
                                    if (p_180192_4_ != null) {
                                        p_180192_4_.setDoneWorking();
                                    }
                                    throw new IOException("Filesize was bigger than maximum allowed (got >= " + var7 + ", limit was " + p_180192_3_ + ")");
                                }
                                var3.write(var4, 0, var14);
                            }
                            if (p_180192_4_ != null) {
                                p_180192_4_.setDoneWorking();
                            }
                        }
                        catch (Throwable var15) {
                            var15.printStackTrace();
                        }
                        finally {
                            IOUtils.closeQuietly(var2);
                            IOUtils.closeQuietly((OutputStream)var3);
                        }
                        return;
                    }
                    continue;
                }
            }
        });
        return var6;
    }
    
    public static int getSuitableLanPort() throws IOException {
        ServerSocket var0 = null;
        final boolean var2 = true;
        int var3;
        try {
            var0 = new ServerSocket(0);
            var3 = var0.getLocalPort();
        }
        finally {
            try {
                if (var0 != null) {
                    var0.close();
                }
            }
            catch (IOException ex) {}
        }
        return var3;
    }
    
    public static String get(final URL url) throws IOException {
        final HttpURLConnection var1 = (HttpURLConnection)url.openConnection();
        var1.setRequestMethod("GET");
        final BufferedReader var2 = new BufferedReader(new InputStreamReader(var1.getInputStream()));
        final StringBuilder var3 = new StringBuilder();
        String var4;
        while ((var4 = var2.readLine()) != null) {
            var3.append(var4);
            var3.append('\r');
        }
        var2.close();
        return var3.toString();
    }
    
    static {
        field_180193_a = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Downloader %d").build()));
        downloadThreadsStarted = new AtomicInteger(0);
        logger = LogManager.getLogger();
    }
}
