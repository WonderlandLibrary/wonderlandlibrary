package com.mojang.realmsclient.client;

import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsAnvilLevelStorageSource;
import net.minecraft.realms.RealmsLevelSummary;
import net.minecraft.realms.RealmsSharedConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileDownload {
  private static final Logger LOGGER = LogManager.getLogger();
  
  private volatile boolean cancelled = false;
  
  private volatile boolean finished = false;
  
  private volatile boolean error = false;
  
  private volatile boolean extracting = false;
  
  private volatile File tempFile;
  
  private volatile HttpGet request;
  
  private Thread currentThread;
  
  private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectTimeout(120000).build();
  
  public long contentLength(String downloadLink) {
    CloseableHttpClient client = null;
    HttpGet httpGet = null;
    try {
      httpGet = new HttpGet(downloadLink);
      client = HttpClientBuilder.create().setDefaultRequestConfig(this.requestConfig).build();
      CloseableHttpResponse response = client.execute((HttpUriRequest)httpGet);
      return Long.parseLong(response.getFirstHeader("Content-Length").getValue());
    } catch (Throwable t) {
      LOGGER.error("Unable to get content length for download");
      return 0L;
    } finally {
      if (httpGet != null)
        httpGet.releaseConnection(); 
      if (client != null)
        try {
          client.close();
        } catch (IOException e) {
          LOGGER.error("Could not close http client", e);
        }  
    } 
  }
  
  public void download(final String downloadLink, final String worldName, final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus, final RealmsAnvilLevelStorageSource levelStorageSource) {
    if (this.currentThread != null)
      return; 
    this.currentThread = new Thread() {
        public void run() {
          CloseableHttpClient client = null;
          try {
            FileDownload.this.tempFile = File.createTempFile("backup", ".tar.gz");
            FileDownload.this.request = new HttpGet(downloadLink);
            client = HttpClientBuilder.create().setDefaultRequestConfig(FileDownload.this.requestConfig).build();
            CloseableHttpResponse closeableHttpResponse = client.execute((HttpUriRequest)FileDownload.this.request);
            downloadStatus.totalBytes = Long.valueOf(Long.parseLong(closeableHttpResponse.getFirstHeader("Content-Length").getValue()));
            if (closeableHttpResponse.getStatusLine().getStatusCode() != 200) {
              FileDownload.this.error = true;
              FileDownload.this.request.abort();
              return;
            } 
            OutputStream os = new FileOutputStream(FileDownload.this.tempFile);
            FileDownload.ProgressListener progressListener = new FileDownload.ProgressListener(worldName.trim(), FileDownload.this.tempFile, levelStorageSource, downloadStatus);
            FileDownload.DownloadCountingOutputStream dcount = new FileDownload.DownloadCountingOutputStream(os);
            dcount.setListener(progressListener);
            IOUtils.copy(closeableHttpResponse.getEntity().getContent(), (OutputStream)dcount);
          } catch (Exception e) {
            FileDownload.LOGGER.error("Caught exception while downloading: " + e.getMessage());
            FileDownload.this.error = true;
          } finally {
            FileDownload.this.request.releaseConnection();
            if (FileDownload.this.tempFile != null)
              FileDownload.this.tempFile.delete(); 
            if (client != null)
              try {
                client.close();
              } catch (IOException e) {
                FileDownload.LOGGER.error("Failed to close Realms download client");
              }  
          } 
        }
      };
    this.currentThread.start();
  }
  
  public void cancel() {
    if (this.request != null)
      this.request.abort(); 
    if (this.tempFile != null)
      this.tempFile.delete(); 
    this.cancelled = true;
  }
  
  public boolean isFinished() {
    return this.finished;
  }
  
  public boolean isError() {
    return this.error;
  }
  
  public boolean isExtracting() {
    return this.extracting;
  }
  
  private static final String[] INVALID_FILE_NAMES = new String[] { 
      "CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", 
      "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", 
      "LPT6", "LPT7", "LPT8", "LPT9" };
  
  public static String findAvailableFolderName(String folder) {
    folder = folder.replaceAll("[\\./\"]", "_");
    for (String invalidName : INVALID_FILE_NAMES) {
      if (folder.equalsIgnoreCase(invalidName))
        folder = "_" + folder + "_"; 
    } 
    return folder;
  }
  
  private void untarGzipArchive(String name, File file, RealmsAnvilLevelStorageSource levelStorageSource) throws IOException {
    String finalName;
    Pattern namePattern = Pattern.compile(".*-([0-9]+)$");
    int number = 1;
    for (char replacer : RealmsSharedConstants.ILLEGAL_FILE_CHARACTERS)
      name = name.replace(replacer, '_'); 
    if (StringUtils.isEmpty(name))
      name = "Realm"; 
    name = findAvailableFolderName(name);
    try {
      for (RealmsLevelSummary summary : levelStorageSource.getLevelList()) {
        if (summary.getLevelId().toLowerCase().startsWith(name.toLowerCase())) {
          Matcher matcher = namePattern.matcher(summary.getLevelId());
          if (matcher.matches()) {
            if (Integer.valueOf(matcher.group(1)).intValue() > number)
              number = Integer.valueOf(matcher.group(1)).intValue(); 
            continue;
          } 
          number++;
        } 
      } 
    } catch (Exception e) {
      this.error = true;
      return;
    } 
    if (!levelStorageSource.isNewLevelIdAcceptable(name) || number > 1) {
      finalName = name + ((number == 1) ? "" : ("-" + number));
      if (!levelStorageSource.isNewLevelIdAcceptable(finalName)) {
        boolean foundName = false;
        while (!foundName) {
          number++;
          finalName = name + ((number == 1) ? "" : ("-" + number));
          if (levelStorageSource.isNewLevelIdAcceptable(finalName))
            foundName = true; 
        } 
      } 
    } else {
      finalName = name;
    } 
    TarArchiveInputStream tarIn = null;
    File saves = new File(Realms.getGameDirectoryPath(), "saves");
    try {
      saves.mkdir();
      tarIn = new TarArchiveInputStream((InputStream)new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(file))));
      TarArchiveEntry tarEntry = tarIn.getNextTarEntry();
      while (tarEntry != null) {
        File destPath = new File(saves, tarEntry.getName().replace("world", finalName));
        if (tarEntry.isDirectory()) {
          destPath.mkdirs();
        } else {
          destPath.createNewFile();
          byte[] btoRead = new byte[1024];
          BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(destPath));
          int len = 0;
          while ((len = tarIn.read(btoRead)) != -1)
            bout.write(btoRead, 0, len); 
          bout.close();
          btoRead = null;
        } 
        tarEntry = tarIn.getNextTarEntry();
      } 
    } catch (Exception e) {
      this.error = true;
    } finally {
      if (tarIn != null)
        tarIn.close(); 
      if (file != null)
        file.delete(); 
      RealmsAnvilLevelStorageSource levelSource = levelStorageSource;
      levelSource.renameLevel(finalName, finalName.trim());
      this.finished = true;
    } 
  }
  
  private class ProgressListener implements ActionListener {
    private volatile String worldName;
    
    private volatile File tempFile;
    
    private volatile RealmsAnvilLevelStorageSource levelStorageSource;
    
    private volatile RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus;
    
    private ProgressListener(String worldName, File tempFile, RealmsAnvilLevelStorageSource levelStorageSource, RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus) {
      this.worldName = worldName;
      this.tempFile = tempFile;
      this.levelStorageSource = levelStorageSource;
      this.downloadStatus = downloadStatus;
    }
    
    public void actionPerformed(ActionEvent e) {
      this.downloadStatus.bytesWritten = Long.valueOf(((FileDownload.DownloadCountingOutputStream)e.getSource()).getByteCount());
      if (this.downloadStatus.bytesWritten.longValue() >= this.downloadStatus.totalBytes.longValue() && !FileDownload.this.cancelled)
        try {
          FileDownload.this.extracting = true;
          FileDownload.this.untarGzipArchive(this.worldName, this.tempFile, this.levelStorageSource);
        } catch (IOException e1) {
          FileDownload.this.error = true;
        }  
    }
  }
  
  private class DownloadCountingOutputStream extends CountingOutputStream {
    private ActionListener listener = null;
    
    public DownloadCountingOutputStream(OutputStream out) {
      super(out);
    }
    
    public void setListener(ActionListener listener) {
      this.listener = listener;
    }
    
    protected void afterWrite(int n) throws IOException {
      super.afterWrite(n);
      if (this.listener != null)
        this.listener.actionPerformed(new ActionEvent(this, 0, null)); 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclient\client\FileDownload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */