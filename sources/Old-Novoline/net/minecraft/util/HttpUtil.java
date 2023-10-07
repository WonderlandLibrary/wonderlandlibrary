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
import java.util.concurrent.atomic.AtomicInteger;

public final class HttpUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final ListeningExecutorService field_180193_a = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Downloader %d").build()));

    /**
     * The number of download threads that we have started so far.
     */
    private static final AtomicInteger downloadThreadsStarted = new AtomicInteger(0);

    /**
     * Builds an encoded HTTP POST content string from a string map
     */
    public static String buildPostString(Map<String, Object> data) {
        final StringBuilder stringbuilder = new StringBuilder();

        for (Entry<String, Object> entry : data.entrySet()) {
            if (stringbuilder.length() > 0) stringbuilder.append('&');

            try {
                stringbuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
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
    public static String postMap(URL url, Map<String, Object> data, boolean skipLoggingErrors) {
        return post(url, buildPostString(data), skipLoggingErrors);
    }

    /**
     * Sends a POST to the given URL
     */
    private static String post(URL url, String content, boolean skipLoggingErrors) {
        try {
            Proxy proxy = MinecraftServer.getServer() == null ? null : MinecraftServer.getServer().getServerProxy();

            if (proxy == null) {
                proxy = Proxy.NO_PROXY;
            }

            final HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection(proxy);
            httpurlconnection.setRequestMethod("POST");
            httpurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpurlconnection.setRequestProperty("Content-Length", "" + content.getBytes().length);
            httpurlconnection.setRequestProperty("Content-Language", "en-US");
            httpurlconnection.setUseCaches(false);
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);
            final DataOutputStream dataoutputstream = new DataOutputStream(httpurlconnection.getOutputStream());
            dataoutputstream.writeBytes(content);
            dataoutputstream.flush();
            dataoutputstream.close();
            final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
            final StringBuilder stringbuffer = new StringBuilder();
            String s;

            while ((s = bufferedreader.readLine()) != null) {
                stringbuffer.append(s);
                stringbuffer.append('\r');
            }

            bufferedreader.close();
            return stringbuffer.toString();
        } catch (Exception exception) {
            if (!skipLoggingErrors) {
                LOGGER.error("Could not post to " + url, exception);
            }

            return "";
        }
    }

    public static ListenableFuture<Object> downloadResourcePack(final File saveFile,
                                                                final String packUrl,
                                                                final Map<String, String> p_180192_2_,
                                                                final int maxSize,
                                                                final IProgressUpdate p_180192_4_,
                                                                final Proxy p_180192_5_) {
        final ListenableFuture<?> listenableFuture = field_180193_a.submit(() -> {
            HttpURLConnection httpurlconnection = null;
            InputStream inputstream = null;
            OutputStream outputstream = null;

            if (p_180192_4_ != null) {
                p_180192_4_.resetProgressAndMessage("Downloading Resource Pack");
                p_180192_4_.displayLoadingString("Making Request...");
            }

            try {
                try {
                    final byte[] bytes = new byte[4096];
                    final URL url = new URL(packUrl);
                    httpurlconnection = (HttpURLConnection) url.openConnection(p_180192_5_);
                    float f = 0.0F;
                    float f1 = (float) p_180192_2_.entrySet().size();

                    for (Entry<String, String> entry : p_180192_2_.entrySet()) {
                        httpurlconnection.setRequestProperty(entry.getKey(), entry.getValue());

                        if (p_180192_4_ != null) {
                            p_180192_4_.setLoadingProgress((int) (++f / f1 * 100.0F));
                        }
                    }

                    inputstream = httpurlconnection.getInputStream();
                    f1 = (float) httpurlconnection.getContentLength();
                    final int i = httpurlconnection.getContentLength();

                    if (p_180192_4_ != null) {
                        p_180192_4_.displayLoadingString(String.format("Downloading file (%.2f MB)...", f1 / 1000.0F / 1000.0F));
                    }

                    if (saveFile.exists()) {
                        final long j = saveFile.length();

                        if (j == (long) i) {
                            if (p_180192_4_ != null) {
                                p_180192_4_.setDoneWorking();
                            }

                            return;
                        }

                        HttpUtil.LOGGER.warn("Deleting " + saveFile + " as it does not match what we currently have (" + i + " vs our " + j + ").");
                        FileUtils.deleteQuietly(saveFile);
                    } else if (saveFile.getParentFile() != null) {
                        saveFile.getParentFile().mkdirs();
                    }

                    outputstream = new DataOutputStream(new FileOutputStream(saveFile));

                    if (maxSize > 0 && f1 > (float) maxSize) {
                        if (p_180192_4_ != null) {
                            p_180192_4_.setDoneWorking();
                        }

                        throw new IOException("Filesize is bigger than maximum allowed (file is " + f + ", limit is " + maxSize + ")");
                    }

                    int k = 0;

                    while ((k = inputstream.read(bytes)) >= 0) {
                        f += (float) k;

                        if (p_180192_4_ != null) {
                            p_180192_4_.setLoadingProgress((int) (f / f1 * 100.0F));
                        }

                        if (maxSize > 0 && f > (float) maxSize) {
                            if (p_180192_4_ != null) {
                                p_180192_4_.setDoneWorking();
                            }

                            throw new IOException("Filesize was bigger than maximum allowed (got >= " + f + ", limit was " + maxSize + ")");
                        }

                        if (Thread.interrupted()) {
                            HttpUtil.LOGGER.error("INTERRUPTED");

                            if (p_180192_4_ != null) {
                                p_180192_4_.setDoneWorking();
                            }

                            return;
                        }

                        outputstream.write(bytes, 0, k);
                    }

                    if (p_180192_4_ != null) {
                        p_180192_4_.setDoneWorking();
                        return;
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();

                    if (httpurlconnection != null) {
                        final InputStream inputstream1 = httpurlconnection.getErrorStream();

                        try {
                            HttpUtil.LOGGER.error(IOUtils.toString(inputstream1));
                        } catch (IOException ioexception) {
                            ioexception.printStackTrace();
                        }
                    }

                    if (p_180192_4_ != null) {
                        p_180192_4_.setDoneWorking();
                        return;
                    }
                }
            } finally {
                IOUtils.closeQuietly(inputstream);
                IOUtils.closeQuietly(outputstream);
            }
        });
        return (ListenableFuture<Object>) listenableFuture;
    }

    public static int getSuitableLanPort() throws IOException {
        ServerSocket serversocket = null;
        int i;

        try {
            serversocket = new ServerSocket(0);
            i = serversocket.getLocalPort();
        } finally {
            try {
                if (serversocket != null) {
                    serversocket.close();
                }
            } catch (IOException ignored) {
            }
        }

        return i;
    }

    /**
     * Send a GET request to the given URL.
     */
    public static String get(URL url) throws IOException {
        final HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
        httpurlconnection.setRequestMethod("GET");
        final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
        final StringBuilder stringbuilder = new StringBuilder();
        String s;

        while ((s = bufferedreader.readLine()) != null) {
            stringbuilder.append(s);
            stringbuilder.append('\r');
        }

        bufferedreader.close();
        return stringbuilder.toString();
    }

}
