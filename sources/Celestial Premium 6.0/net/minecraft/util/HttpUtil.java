/*
 * Decompiled with CFR 0.150.
 */
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import net.minecraft.util.IProgressUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpUtil {
    public static final ListeningExecutorService DOWNLOADER_EXECUTOR = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Downloader %d").build()));
    private static final AtomicInteger DOWNLOAD_THREADS_STARTED = new AtomicInteger(0);
    private static final Logger LOGGER = LogManager.getLogger();

    public static String buildPostString(Map<String, Object> data) {
        StringBuilder stringbuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (stringbuilder.length() > 0) {
                stringbuilder.append('&');
            }
            try {
                stringbuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            }
            catch (UnsupportedEncodingException unsupportedencodingexception1) {
                unsupportedencodingexception1.printStackTrace();
            }
            if (entry.getValue() == null) continue;
            stringbuilder.append('=');
            try {
                stringbuilder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
            }
            catch (UnsupportedEncodingException unsupportedencodingexception) {
                unsupportedencodingexception.printStackTrace();
            }
        }
        return stringbuilder.toString();
    }

    public static String postMap(URL url, Map<String, Object> data, boolean skipLoggingErrors, @Nullable Proxy p_151226_3_) {
        return HttpUtil.post(url, HttpUtil.buildPostString(data), skipLoggingErrors, p_151226_3_);
    }

    private static String post(URL url, String content, boolean skipLoggingErrors, @Nullable Proxy p_151225_3_) {
        try {
            String s;
            if (p_151225_3_ == null) {
                p_151225_3_ = Proxy.NO_PROXY;
            }
            HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection(p_151225_3_);
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
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
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
                LOGGER.error("Could not post to {}", url, exception);
            }
            return "";
        }
    }

    public static ListenableFuture<Object> downloadResourcePack(final File saveFile, final String packUrl, final Map<String, String> p_180192_2_, final int maxSize, final @Nullable IProgressUpdate p_180192_4_, final Proxy p_180192_5_) {
        Future listenablefuture = DOWNLOADER_EXECUTOR.submit(new Runnable(){

            /*
             * Exception decompiling
             */
            @Override
            public void run() {
                /*
                 * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
                 * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK]], but top level block is 10[WHILELOOP]
                 * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:429)
                 * org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:478)
                 * org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:728)
                 * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:806)
                 * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:258)
                 * org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:192)
                 * org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
                 * org.benf.cfr.reader.entities.Method.analyse(Method.java:521)
                 * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1035)
                 * org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:903)
                 * org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1015)
                 * org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:922)
                 * org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:253)
                 * org.benf.cfr.reader.Driver.doJar(Driver.java:135)
                 * org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:65)
                 * org.benf.cfr.reader.Main.main(Main.java:49)
                 */
                throw new IllegalStateException(Decompilation failed);
            }
        });
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

    static /* synthetic */ Logger access$000() {
        return LOGGER;
    }
}

