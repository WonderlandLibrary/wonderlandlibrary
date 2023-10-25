package com.mojang.realmsclient.client;

import com.google.gson.JsonParser;
import com.mojang.realmsclient.RealmsVersion;
import com.mojang.realmsclient.dto.UploadInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUpload {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private static final String UPLOAD_PATH = "/upload";
  
  private static final String PORT = "8080";
  
  private volatile boolean cancelled = false;
  
  private volatile boolean finished = false;
  
  private HttpPost request;
  
  private int statusCode = -1;
  
  private String errorMessage;
  
  private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();
  
  private Thread currentThread;
  
  public void upload(final File file, final long worldId, final int slotId, final UploadInfo uploadInfo, final String sessionId, final String username, final String clientVersion, final UploadStatus uploadStatus) {
    if (this.currentThread != null)
      return; 
    this.currentThread = new Thread() {
        public void run() {
          FileUpload.this.request = new HttpPost("http://" + uploadInfo.getUploadEndpoint() + ":" + "8080" + "/upload" + "/" + String.valueOf(worldId) + "/" + String.valueOf(slotId));
          CloseableHttpClient client = null;
          try {
            client = HttpClientBuilder.create().setDefaultRequestConfig(FileUpload.this.requestConfig).build();
            String realmsVersion = RealmsVersion.getVersion();
            if (realmsVersion != null) {
              FileUpload.this.request.setHeader("Cookie", "sid=" + sessionId + ";token=" + uploadInfo.getToken() + ";user=" + username + ";version=" + clientVersion + ";realms_version=" + realmsVersion);
            } else {
              FileUpload.this.request.setHeader("Cookie", "sid=" + sessionId + ";token=" + uploadInfo.getToken() + ";user=" + username + ";version=" + clientVersion);
            } 
            uploadStatus.totalBytes = Long.valueOf(file.length());
            FileUpload.CustomInputStreamEntity entity = new FileUpload.CustomInputStreamEntity(new FileInputStream(file), file.length(), uploadStatus);
            entity.setContentType("application/octet-stream");
            FileUpload.this.request.setEntity((HttpEntity)entity);
            CloseableHttpResponse closeableHttpResponse = client.execute((HttpUriRequest)FileUpload.this.request);
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            if (statusCode == 401)
              FileUpload.LOGGER.debug("Realms server returned 401: " + closeableHttpResponse.getFirstHeader("WWW-Authenticate")); 
            FileUpload.this.statusCode = statusCode;
            String json = EntityUtils.toString(closeableHttpResponse.getEntity(), "UTF-8");
            if (json != null)
              try {
                JsonParser parser = new JsonParser();
                FileUpload.this.errorMessage = parser.parse(json).getAsJsonObject().get("errorMsg").getAsString();
              } catch (Exception e) {} 
          } catch (Exception e) {
            FileUpload.LOGGER.error("Caught exception while uploading: " + e.getMessage());
          } finally {
            FileUpload.this.request.releaseConnection();
            FileUpload.this.finished = true;
            if (client != null)
              try {
                client.close();
              } catch (IOException e) {
                FileUpload.LOGGER.error("Failed to close Realms upload client");
              }  
          } 
        }
      };
    this.currentThread.start();
  }
  
  public void cancel() {
    this.cancelled = true;
    if (this.request != null)
      this.request.abort(); 
  }
  
  public boolean isFinished() {
    return this.finished;
  }
  
  public int getStatusCode() {
    return this.statusCode;
  }
  
  public String getErrorMessage() {
    return this.errorMessage;
  }
  
  private static class CustomInputStreamEntity extends InputStreamEntity {
    private final long length;
    
    private final InputStream content;
    
    private final UploadStatus uploadStatus;
    
    public CustomInputStreamEntity(InputStream instream, long length, UploadStatus uploadStatus) {
      super(instream);
      this.content = instream;
      this.length = length;
      this.uploadStatus = uploadStatus;
    }
    
    public void writeTo(OutputStream outstream) throws IOException {
      Args.notNull(outstream, "Output stream");
      InputStream instream = this.content;
      try {
        byte[] buffer = new byte[4096];
        if (this.length < 0L) {
          int l;
          while ((l = instream.read(buffer)) != -1) {
            outstream.write(buffer, 0, l);
            UploadStatus uploadStatus = this.uploadStatus;
            uploadStatus.bytesWritten = Long.valueOf(uploadStatus.bytesWritten.longValue() + l);
          } 
        } else {
          long remaining = this.length;
          while (remaining > 0L) {
            int l = instream.read(buffer, 0, (int)Math.min(4096L, remaining));
            if (l == -1)
              break; 
            outstream.write(buffer, 0, l);
            UploadStatus uploadStatus = this.uploadStatus;
            uploadStatus.bytesWritten = Long.valueOf(uploadStatus.bytesWritten.longValue() + l);
            remaining -= l;
            outstream.flush();
          } 
        } 
      } finally {
        instream.close();
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\client\FileUpload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */