package net.minecraft.util;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;

public class HttpUtil {
    public static final ListeningExecutorService SERVICE = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool((new ThreadFactoryBuilder()).setDaemon(true).setNameFormat("Downloader %d").build()));

    /**
     * The number of download threads that we have started so far.
     */
    private static final Logger logger = LogManager.getLogger();

    /**
     * Builds an encoded HTTP POST content string from a string map
     */
    public static String buildPostString(Map<String, Object> data) {
        StringBuilder stringbuilder = new StringBuilder();

        for (Entry<String, Object> entry : data.entrySet()) {
            if (stringbuilder.length() > 0)
                stringbuilder.append('&');

            try {
                stringbuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            } catch (UnsupportedEncodingException unsupportedencodingexception1) {
                unsupportedencodingexception1.printStackTrace();
            }

            if (entry.getValue() != null) {
                stringbuilder.append('=');

                try {
                    stringbuilder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                } catch (UnsupportedEncodingException unsupportedencodingexception) {
                    unsupportedencodingexception.printStackTrace();
                }
            }
        }

        return stringbuilder.toString();
    }

    /**
     * Sends a POST to the given URL using the map as the POST args
     */
    public static void postMap(URL url, Map<String, Object> data, boolean skipLoggingErrors) {
        post(url, buildPostString(data), skipLoggingErrors);
    }

    /**
     * Sends a POST to the given URL
     */
    private static void post(URL url, String content, boolean skipLoggingErrors) {
        try {
            Proxy proxy = MinecraftServer.getServer() == null ? Proxy.NO_PROXY : MinecraftServer.getServer().getServerProxy();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(content.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
            stream.writeBytes(content);
            stream.flush();
            stream.close();
        } catch (Exception exception) {
            if (!skipLoggingErrors)
                logger.error("Could not post to " + url, exception);
        }
    }

    public static ListenableFuture<?> downloadResourcePack(final File saveFile, final String packUrl, final Map<String, String> p_180192_2_, final int maxSize, final IProgressUpdate progressUpdate, final Proxy proxy) {
        return SERVICE.submit(() -> {
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            OutputStream outputStream = null;

            if (progressUpdate != null) {
                progressUpdate.resetProgressAndMessage("Downloading Resource Pack");
                progressUpdate.displayLoadingString("Making Request...");
            }

            try {
                try {
                    byte[] bytes = new byte[4096];
                    URL url = new URL(packUrl);
                    connection = (HttpURLConnection) url.openConnection(proxy);
                    float progressSize = 0.0F;
                    float f1 = (float) p_180192_2_.entrySet().size();

                    for (Entry<String, String> entry : p_180192_2_.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                        if (progressUpdate != null) progressUpdate.setLoadingProgress((int) (++progressSize / f1 * 100.0F));
                    }

                    inputStream = connection.getInputStream();
                    f1 = (float) connection.getContentLength();
                    int i = connection.getContentLength();

                    if (progressUpdate != null)
                        progressUpdate.displayLoadingString(String.format("Downloading file (%.2f MB)...", f1 / 1000.0F / 1000.0F));

                    if (saveFile.exists()) {
                        long j = saveFile.length();

                        if (j == (long) i) {
                            if (progressUpdate != null)
                                progressUpdate.setDoneWorking();

                            return;
                        }

                        HttpUtil.logger.warn("Deleting " + saveFile + " as it does not match what we currently have (" + i + " vs our " + j + ").");
                        FileUtils.deleteQuietly(saveFile);
                    } else if (saveFile.getParentFile() != null)
                        saveFile.getParentFile().mkdirs();

                    outputStream = new DataOutputStream(new FileOutputStream(saveFile));

                    if (maxSize > 0 && f1 > (float) maxSize) {
                        if (progressUpdate != null)
                            progressUpdate.setDoneWorking();

                        throw new IOException("Filesize is bigger than maximum allowed (file is " + progressSize + ", limit is " + maxSize + ")");
                    }

                    int size;
                    while ((size = inputStream.read(bytes)) >= 0) {
                        progressSize += (float) size;

                        if (progressUpdate != null)
                            progressUpdate.setLoadingProgress((int) (progressSize / f1 * 100.0F));

                        if (maxSize > 0 && progressSize > (float) maxSize) {
                            if (progressUpdate != null)
                                progressUpdate.setDoneWorking();

                            throw new IOException("Filesize was bigger than maximum allowed (got >= " + progressSize + ", limit was " + maxSize + ")");
                        }

                        if (Thread.interrupted()) {
                            HttpUtil.logger.error("INTERRUPTED");
                            if (progressUpdate != null) progressUpdate.setDoneWorking();
                            return;
                        }

                        outputStream.write(bytes, 0, size);
                    }

                    if (progressUpdate != null) progressUpdate.setDoneWorking();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();

                    if (connection != null) {
                        InputStream inputstream1 = connection.getErrorStream();

                        try {
                            HttpUtil.logger.error(IOUtils.toString(inputstream1));
                        } catch (IOException ioexception) {
                            ioexception.printStackTrace();
                        }
                    }

                    if (progressUpdate != null) progressUpdate.setDoneWorking();
                }
            } finally {
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(outputStream);
            }
        });
    }

    public static int getSuitableLanPort() throws IOException {
        ServerSocket serversocket = null;
        int i;

        try {
            serversocket = new ServerSocket(0);
            i = serversocket.getLocalPort();
        } finally {
            try {
                if (serversocket != null) serversocket.close();
            } catch (IOException ignored) {
            }
        }

        return i;
    }

    /**
     * Send a GET request to the given URL.
     */
    public static String get(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder stringbuilder = new StringBuilder();
        String s;

        while ((s = bufferedreader.readLine()) != null) {
            stringbuilder.append(s);
            stringbuilder.append('\r');
        }

        bufferedreader.close();
        return stringbuilder.toString();
    }
}