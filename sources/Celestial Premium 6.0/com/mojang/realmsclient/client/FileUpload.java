/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.RealmsVersion;
import com.mojang.realmsclient.client.UploadStatus;
import com.mojang.realmsclient.dto.UploadInfo;
import com.mojang.realmsclient.gui.screens.UploadResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUpload {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int MAX_RETRIES = 5;
    private static final String UPLOAD_PATH = "/upload";
    private final File file;
    private final long worldId;
    private final int slotId;
    private final UploadInfo uploadInfo;
    private final String sessionId;
    private final String username;
    private final String clientVersion;
    private final UploadStatus uploadStatus;
    private AtomicBoolean cancelled = new AtomicBoolean(false);
    private CompletableFuture<UploadResult> uploadTask;
    private final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout((int)TimeUnit.MINUTES.toMillis(10L)).setConnectTimeout((int)TimeUnit.SECONDS.toMillis(15L)).build();

    public FileUpload(File file, long worldId, int slotId, UploadInfo uploadInfo, String sessionId, String username, String clientVersion, UploadStatus uploadStatus) {
        this.file = file;
        this.worldId = worldId;
        this.slotId = slotId;
        this.uploadInfo = uploadInfo;
        this.sessionId = sessionId;
        this.username = username;
        this.clientVersion = clientVersion;
        this.uploadStatus = uploadStatus;
    }

    public void upload(Consumer<UploadResult> callback) {
        if (this.uploadTask != null) {
            return;
        }
        this.uploadTask = CompletableFuture.supplyAsync(() -> this.requestUpload(0));
        this.uploadTask.thenAcceptAsync((Consumer)callback);
    }

    public void cancel() {
        this.cancelled.set(true);
        if (this.uploadTask != null) {
            this.uploadTask.cancel(false);
            this.uploadTask = null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private UploadResult requestUpload(int currentAttempt) {
        UploadResult.Builder uploadResultBuilder = new UploadResult.Builder();
        if (this.cancelled.get()) {
            return uploadResultBuilder.build();
        }
        this.uploadStatus.totalBytes = this.file.length();
        HttpPost request = new HttpPost("http://" + this.uploadInfo.getUploadEndpoint() + ":" + this.uploadInfo.getPort() + UPLOAD_PATH + "/" + this.worldId + "/" + this.slotId);
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(this.requestConfig).build();
        try {
            this.setupRequest(request);
            CloseableHttpResponse response = client.execute(request);
            long retryDelaySeconds = this.getRetryDelaySeconds(response);
            if (this.shouldRetry(retryDelaySeconds, currentAttempt)) {
                UploadResult uploadResult = this.retryUploadAfter(retryDelaySeconds, currentAttempt);
                return uploadResult;
            }
            this.handleResponse(response, uploadResultBuilder);
        }
        catch (Exception e) {
            if (!this.cancelled.get()) {
                LOGGER.error("Caught exception while uploading: ", (Throwable)e);
            }
        }
        finally {
            this.cleanup(request, client);
        }
        return uploadResultBuilder.build();
    }

    private void cleanup(HttpPost request, CloseableHttpClient client) {
        request.releaseConnection();
        if (client != null) {
            try {
                client.close();
            }
            catch (IOException ignored) {
                LOGGER.error("Failed to close Realms upload client");
            }
        }
    }

    private void setupRequest(HttpPost request) throws FileNotFoundException {
        String realmsVersion = RealmsVersion.getVersion();
        if (realmsVersion != null) {
            request.setHeader("Cookie", "sid=" + this.sessionId + ";token=" + this.uploadInfo.getToken() + ";user=" + this.username + ";version=" + this.clientVersion + ";realms_version=" + realmsVersion);
        } else {
            request.setHeader("Cookie", "sid=" + this.sessionId + ";token=" + this.uploadInfo.getToken() + ";user=" + this.username + ";version=" + this.clientVersion);
        }
        CustomInputStreamEntity entity = new CustomInputStreamEntity((InputStream)new FileInputStream(this.file), this.file.length(), this.uploadStatus);
        entity.setContentType("application/octet-stream");
        request.setEntity(entity);
    }

    private void handleResponse(HttpResponse response, UploadResult.Builder uploadResultBuilder) throws IOException {
        String json;
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 401) {
            LOGGER.debug("Realms server returned 401: " + response.getFirstHeader("WWW-Authenticate"));
        }
        uploadResultBuilder.withStatusCode(statusCode);
        if (response.getEntity() != null && (json = EntityUtils.toString(response.getEntity(), "UTF-8")) != null) {
            try {
                JsonParser parser = new JsonParser();
                JsonElement errorMsgElement = parser.parse(json).getAsJsonObject().get("errorMsg");
                Optional<String> errorMessage = Optional.ofNullable(errorMsgElement).map(JsonElement::getAsString);
                uploadResultBuilder.withErrorMessage(errorMessage.orElse(null));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    private boolean shouldRetry(long retryDelaySeconds, int currentAttempt) {
        return retryDelaySeconds > 0L && currentAttempt + 1 < 5;
    }

    private UploadResult retryUploadAfter(long retryDelaySeconds, int currentAttempt) throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(retryDelaySeconds).toMillis());
        return this.requestUpload(currentAttempt + 1);
    }

    private long getRetryDelaySeconds(HttpResponse response) {
        return Optional.ofNullable(response.getFirstHeader("Retry-After")).map(Header::getValue).map(Long::valueOf).orElse(0L);
    }

    public boolean isFinished() {
        return this.uploadTask.isDone() || this.uploadTask.isCancelled();
    }

    private static class CustomInputStreamEntity
    extends InputStreamEntity {
        private final long length;
        private final InputStream content;
        private final UploadStatus uploadStatus;

        public CustomInputStreamEntity(InputStream content, long length, UploadStatus uploadStatus) {
            super(content);
            this.content = content;
            this.length = length;
            this.uploadStatus = uploadStatus;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void writeTo(OutputStream outstream) throws IOException {
            block7: {
                Args.notNull(outstream, "Output stream");
                try (InputStream instream = this.content;){
                    int l;
                    byte[] buffer = new byte[4096];
                    if (this.length < 0L) {
                        int l2;
                        while ((l2 = instream.read(buffer)) != -1) {
                            outstream.write(buffer, 0, l2);
                            UploadStatus uploadStatus = this.uploadStatus;
                            Long.valueOf(uploadStatus.bytesWritten + (long)l2);
                            uploadStatus.bytesWritten = uploadStatus.bytesWritten;
                        }
                        break block7;
                    }
                    for (long remaining = this.length; remaining > 0L; remaining -= (long)l) {
                        l = instream.read(buffer, 0, (int)Math.min(4096L, remaining));
                        if (l == -1) {
                            break;
                        }
                        outstream.write(buffer, 0, l);
                        UploadStatus uploadStatus = this.uploadStatus;
                        Long.valueOf(uploadStatus.bytesWritten + (long)l);
                        uploadStatus.bytesWritten = uploadStatus.bytesWritten;
                        outstream.flush();
                    }
                }
            }
        }
    }
}

