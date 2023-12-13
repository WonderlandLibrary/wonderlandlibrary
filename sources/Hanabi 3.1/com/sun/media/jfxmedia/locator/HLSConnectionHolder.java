package com.sun.media.jfxmedia.locator;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmediaimpl.MediaUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

final class HLSConnectionHolder extends ConnectionHolder {
   private URLConnection urlConnection = null;
   private PlaylistThread playlistThread = new PlaylistThread();
   private VariantPlaylist variantPlaylist = null;
   private Playlist currentPlaylist = null;
   private int mediaFileIndex = -1;
   private CountDownLatch readySignal = new CountDownLatch(1);
   private Semaphore liveSemaphore = new Semaphore(0);
   private boolean isPlaylistClosed = false;
   private boolean isBitrateAdjustable = false;
   private long startTime = -1L;
   private static final long HLS_VALUE_FLOAT_MULTIPLIER = 1000L;
   private static final int HLS_PROP_GET_DURATION = 1;
   private static final int HLS_PROP_GET_HLS_MODE = 2;
   private static final int HLS_PROP_GET_MIMETYPE = 3;
   private static final int HLS_VALUE_MIMETYPE_MP2T = 1;
   private static final int HLS_VALUE_MIMETYPE_MP3 = 2;
   private static final String CHARSET_UTF_8 = "UTF-8";
   private static final String CHARSET_US_ASCII = "US-ASCII";

   HLSConnectionHolder(URI var1) throws IOException {
      this.playlistThread.setPlaylistURI(var1);
      this.init();
   }

   private void init() {
      this.playlistThread.putState(0);
      this.playlistThread.start();
   }

   public int readNextBlock() throws IOException {
      if (this.isBitrateAdjustable && this.startTime == -1L) {
         this.startTime = System.currentTimeMillis();
      }

      int var1 = super.readNextBlock();
      if (this.isBitrateAdjustable && var1 == -1) {
         long var2 = System.currentTimeMillis() - this.startTime;
         this.startTime = -1L;
         this.adjustBitrate(var2);
      }

      return var1;
   }

   int readBlock(long var1, int var3) throws IOException {
      throw new IOException();
   }

   boolean needBuffer() {
      return true;
   }

   boolean isSeekable() {
      return true;
   }

   boolean isRandomAccess() {
      return false;
   }

   public long seek(long var1) {
      try {
         this.readySignal.await();
      } catch (Exception var4) {
         return -1L;
      }

      return (long)(this.currentPlaylist.seek(var1) * 1000.0);
   }

   public void closeConnection() {
      this.currentPlaylist.close();
      super.closeConnection();
      this.resetConnection();
      this.playlistThread.putState(1);
   }

   int property(int var1, int var2) {
      try {
         this.readySignal.await();
      } catch (Exception var4) {
         return -1;
      }

      if (var1 == 1) {
         return (int)(this.currentPlaylist.getDuration() * 1000.0);
      } else if (var1 == 2) {
         return 1;
      } else {
         return var1 == 3 ? this.currentPlaylist.getMimeType() : -1;
      }
   }

   int getStreamSize() {
      try {
         this.readySignal.await();
      } catch (Exception var2) {
         return -1;
      }

      return this.loadNextSegment();
   }

   private void resetConnection() {
      super.closeConnection();
      Locator.closeConnection(this.urlConnection);
      this.urlConnection = null;
   }

   private int loadNextSegment() {
      this.resetConnection();
      String var1 = this.currentPlaylist.getNextMediaFile();
      if (var1 == null) {
         return -1;
      } else {
         try {
            URI var2 = new URI(var1);
            this.urlConnection = var2.toURL().openConnection();
            this.channel = this.openChannel();
         } catch (Exception var3) {
            return -1;
         }

         return this.currentPlaylist.isCurrentMediaFileDiscontinuity() ? -1 * this.urlConnection.getContentLength() : this.urlConnection.getContentLength();
      }
   }

   private ReadableByteChannel openChannel() throws IOException {
      return Channels.newChannel(this.urlConnection.getInputStream());
   }

   private void adjustBitrate(long var1) {
      int var3 = (int)((long)this.urlConnection.getContentLength() * 8L * 1000L / var1);
      Playlist var4 = this.variantPlaylist.getPlaylistBasedOnBitrate(var3);
      if (var4 != null && var4 != this.currentPlaylist) {
         if (this.currentPlaylist.isLive()) {
            var4.update(this.currentPlaylist.getNextMediaFile());
            this.playlistThread.setReloadPlaylist(var4);
         }

         var4.setForceDiscontinuity(true);
         this.currentPlaylist = var4;
      }

   }

   private static String stripParameters(String var0) {
      int var1 = var0.indexOf(63);
      if (var1 > 0) {
         var0 = var0.substring(0, var1);
      }

      return var0;
   }

   private class Playlist {
      private boolean isLive;
      private volatile boolean isLiveWaiting;
      private volatile boolean isLiveStop;
      private long targetDuration;
      private URI playlistURI;
      private final Object lock;
      private List mediaFiles;
      private List mediaFilesStartTimes;
      private List mediaFilesDiscontinuities;
      private boolean needBaseURI;
      private String baseURI;
      private double duration;
      private int sequenceNumber;
      private int sequenceNumberStart;
      private boolean sequenceNumberUpdated;
      private boolean forceDiscontinuity;

      private Playlist(boolean var2, int var3) {
         this.isLive = false;
         this.isLiveWaiting = false;
         this.isLiveStop = false;
         this.targetDuration = 0L;
         this.playlistURI = null;
         this.lock = new Object();
         this.mediaFiles = new ArrayList();
         this.mediaFilesStartTimes = new ArrayList();
         this.mediaFilesDiscontinuities = new ArrayList();
         this.needBaseURI = true;
         this.baseURI = null;
         this.duration = 0.0;
         this.sequenceNumber = -1;
         this.sequenceNumberStart = -1;
         this.sequenceNumberUpdated = false;
         this.forceDiscontinuity = false;
         this.isLive = var2;
         this.targetDuration = (long)(var3 * 1000);
         if (var2) {
            this.duration = -1.0;
         }

      }

      private Playlist(URI var2) {
         this.isLive = false;
         this.isLiveWaiting = false;
         this.isLiveStop = false;
         this.targetDuration = 0L;
         this.playlistURI = null;
         this.lock = new Object();
         this.mediaFiles = new ArrayList();
         this.mediaFilesStartTimes = new ArrayList();
         this.mediaFilesDiscontinuities = new ArrayList();
         this.needBaseURI = true;
         this.baseURI = null;
         this.duration = 0.0;
         this.sequenceNumber = -1;
         this.sequenceNumberStart = -1;
         this.sequenceNumberUpdated = false;
         this.forceDiscontinuity = false;
         this.playlistURI = var2;
      }

      private void update(String var1) {
         PlaylistParser var2 = new PlaylistParser();
         var2.load(this.playlistURI);
         this.isLive = var2.isLivePlaylist();
         this.targetDuration = (long)(var2.getTargetDuration() * 1000);
         if (this.isLive) {
            this.duration = -1.0;
         }

         if (this.setSequenceNumber(var2.getSequenceNumber())) {
            while(var2.hasNext()) {
               this.addMediaFile(var2.getString(), var2.getDouble(), var2.getBoolean());
            }
         }

         if (var1 != null) {
            synchronized(this.lock) {
               for(int var4 = 0; var4 < this.mediaFiles.size(); ++var4) {
                  String var5 = (String)this.mediaFiles.get(var4);
                  if (var1.endsWith(var5)) {
                     HLSConnectionHolder.this.mediaFileIndex = var4 - 1;
                     break;
                  }
               }
            }
         }

      }

      private boolean isLive() {
         return this.isLive;
      }

      private long getTargetDuration() {
         return this.targetDuration;
      }

      private void setPlaylistURI(URI var1) {
         this.playlistURI = var1;
      }

      private void addMediaFile(String var1, double var2, boolean var4) {
         synchronized(this.lock) {
            if (this.needBaseURI) {
               this.setBaseURI(this.playlistURI.toString(), var1);
            }

            if (this.isLive) {
               if (this.sequenceNumberUpdated) {
                  int var6 = this.mediaFiles.indexOf(var1);
                  if (var6 != -1) {
                     for(int var7 = 0; var7 < var6; ++var7) {
                        this.mediaFiles.remove(0);
                        this.mediaFilesDiscontinuities.remove(0);
                        if (HLSConnectionHolder.this.mediaFileIndex == -1) {
                           this.forceDiscontinuity = true;
                        }

                        if (HLSConnectionHolder.this.mediaFileIndex >= 0) {
                           HLSConnectionHolder.this.mediaFileIndex--;
                        }
                     }
                  }

                  this.sequenceNumberUpdated = false;
               }

               if (this.mediaFiles.contains(var1)) {
                  return;
               }
            }

            this.mediaFiles.add(var1);
            this.mediaFilesDiscontinuities.add(var4);
            if (this.isLive) {
               if (this.isLiveWaiting) {
                  HLSConnectionHolder.this.liveSemaphore.release();
               }
            } else {
               this.mediaFilesStartTimes.add(this.duration);
               this.duration += var2;
            }

         }
      }

      private String getNextMediaFile() {
         if (this.isLive) {
            synchronized(this.lock) {
               this.isLiveWaiting = HLSConnectionHolder.this.mediaFileIndex + 1 >= this.mediaFiles.size();
            }

            if (this.isLiveWaiting) {
               try {
                  HLSConnectionHolder.this.liveSemaphore.acquire();
                  this.isLiveWaiting = false;
                  if (this.isLiveStop) {
                     this.isLiveStop = false;
                     return null;
                  }
               } catch (InterruptedException var5) {
                  this.isLiveWaiting = false;
                  return null;
               }
            }

            if (HLSConnectionHolder.this.isPlaylistClosed) {
               return null;
            }
         }

         synchronized(this.lock) {
            HLSConnectionHolder.this.mediaFileIndex++;
            if (HLSConnectionHolder.this.mediaFileIndex < this.mediaFiles.size()) {
               return this.baseURI != null ? this.baseURI + (String)this.mediaFiles.get(HLSConnectionHolder.this.mediaFileIndex) : (String)this.mediaFiles.get(HLSConnectionHolder.this.mediaFileIndex);
            } else {
               return null;
            }
         }
      }

      private double getDuration() {
         return this.duration;
      }

      private void setForceDiscontinuity(boolean var1) {
         this.forceDiscontinuity = var1;
      }

      private boolean isCurrentMediaFileDiscontinuity() {
         if (this.forceDiscontinuity) {
            this.forceDiscontinuity = false;
            return true;
         } else {
            return (Boolean)this.mediaFilesDiscontinuities.get(HLSConnectionHolder.this.mediaFileIndex);
         }
      }

      private double seek(long var1) {
         synchronized(this.lock) {
            if (this.isLive) {
               if (var1 == 0L) {
                  HLSConnectionHolder.this.mediaFileIndex = -1;
                  if (this.isLiveWaiting) {
                     this.isLiveStop = true;
                     HLSConnectionHolder.this.liveSemaphore.release();
                  }

                  return 0.0;
               }
            } else {
               var1 += this.targetDuration / 2000L;
               int var4 = this.mediaFilesStartTimes.size();

               for(int var5 = 0; var5 < var4; ++var5) {
                  if ((double)var1 >= (Double)this.mediaFilesStartTimes.get(var5)) {
                     if (var5 + 1 < var4) {
                        if ((double)var1 < (Double)this.mediaFilesStartTimes.get(var5 + 1)) {
                           HLSConnectionHolder.this.mediaFileIndex = var5 - 1;
                           return (Double)this.mediaFilesStartTimes.get(var5);
                        }
                     } else {
                        if ((double)(var1 - this.targetDuration / 2000L) < this.duration) {
                           HLSConnectionHolder.this.mediaFileIndex = var5 - 1;
                           return (Double)this.mediaFilesStartTimes.get(var5);
                        }

                        if (Double.compare((double)(var1 - this.targetDuration / 2000L), this.duration) == 0) {
                           return this.duration;
                        }
                     }
                  }
               }
            }

            return -1.0;
         }
      }

      private int getMimeType() {
         synchronized(this.lock) {
            if (this.mediaFiles.size() > 0) {
               if (HLSConnectionHolder.stripParameters((String)this.mediaFiles.get(0)).endsWith(".ts")) {
                  return 1;
               }

               if (HLSConnectionHolder.stripParameters((String)this.mediaFiles.get(0)).endsWith(".mp3")) {
                  return 2;
               }
            }

            return -1;
         }
      }

      private String getMediaFileExtension() {
         synchronized(this.lock) {
            if (this.mediaFiles.size() > 0) {
               String var2 = HLSConnectionHolder.stripParameters((String)this.mediaFiles.get(0));
               int var3 = var2.lastIndexOf(".");
               if (var3 != -1) {
                  return var2.substring(var3);
               }
            }

            return null;
         }
      }

      private boolean setSequenceNumber(int var1) {
         if (this.sequenceNumberStart == -1) {
            this.sequenceNumberStart = var1;
         } else {
            if (this.sequenceNumber == var1) {
               return false;
            }

            this.sequenceNumberUpdated = true;
            this.sequenceNumber = var1;
         }

         return true;
      }

      private void close() {
         if (this.isLive) {
            HLSConnectionHolder.this.isPlaylistClosed = true;
            HLSConnectionHolder.this.liveSemaphore.release();
         }

      }

      private void setBaseURI(String var1, String var2) {
         if (!var2.startsWith("http://") && !var2.startsWith("https://")) {
            this.baseURI = var1.substring(0, var1.lastIndexOf("/") + 1);
         }

         this.needBaseURI = false;
      }

      // $FF: synthetic method
      Playlist(boolean var2, int var3, Object var4) {
         this(var2, var3);
      }

      // $FF: synthetic method
      Playlist(URI var2, Object var3) {
         this(var2);
      }
   }

   private static class VariantPlaylist {
      private URI playlistURI;
      private int infoIndex;
      private List playlistsLocations;
      private List playlistsBitrates;
      private List playlists;
      private String mediaFileExtension;

      private VariantPlaylist(URI var1) {
         this.playlistURI = null;
         this.infoIndex = -1;
         this.playlistsLocations = new ArrayList();
         this.playlistsBitrates = new ArrayList();
         this.playlists = new ArrayList();
         this.mediaFileExtension = null;
         this.playlistURI = var1;
      }

      private void addPlaylistInfo(String var1, int var2) {
         this.playlistsLocations.add(var1);
         this.playlistsBitrates.add(var2);
      }

      private void addPlaylist(Playlist var1) {
         if (this.mediaFileExtension == null) {
            this.mediaFileExtension = var1.getMediaFileExtension();
         } else if (!this.mediaFileExtension.equals(var1.getMediaFileExtension())) {
            this.playlistsLocations.remove(this.infoIndex);
            this.playlistsBitrates.remove(this.infoIndex);
            --this.infoIndex;
            return;
         }

         this.playlists.add(var1);
      }

      private Playlist getPlaylist(int var1) {
         return this.playlists.size() > var1 ? (Playlist)this.playlists.get(var1) : null;
      }

      private boolean hasNext() {
         ++this.infoIndex;
         return this.playlistsLocations.size() > this.infoIndex && this.playlistsBitrates.size() > this.infoIndex;
      }

      private URI getPlaylistURI() throws URISyntaxException, MalformedURLException {
         String var1 = (String)this.playlistsLocations.get(this.infoIndex);
         return !var1.startsWith("http://") && !var1.startsWith("https://") ? new URI(this.playlistURI.toURL().toString().substring(0, this.playlistURI.toURL().toString().lastIndexOf("/") + 1) + var1) : new URI(var1);
      }

      private Playlist getPlaylistBasedOnBitrate(int var1) {
         int var2 = -1;
         int var3 = 0;

         int var4;
         int var5;
         for(var4 = 0; var4 < this.playlistsBitrates.size(); ++var4) {
            var5 = (Integer)this.playlistsBitrates.get(var4);
            if (var5 < var1) {
               if (var2 != -1) {
                  if (var5 > var3) {
                     var3 = var5;
                     var2 = var4;
                  }
               } else {
                  var2 = var4;
               }
            }
         }

         if (var2 == -1) {
            for(var4 = 0; var4 < this.playlistsBitrates.size(); ++var4) {
               var5 = (Integer)this.playlistsBitrates.get(var4);
               if (var5 < var3 || var2 == -1) {
                  var3 = var5;
                  var2 = var4;
               }
            }
         }

         return var2 >= 0 && var2 < this.playlists.size() ? (Playlist)this.playlists.get(var2) : null;
      }

      // $FF: synthetic method
      VariantPlaylist(URI var1, Object var2) {
         this(var1);
      }
   }

   private static class PlaylistParser {
      private boolean isFirstLine;
      private boolean isLineMediaFileURI;
      private boolean isEndList;
      private boolean isLinePlaylistURI;
      private boolean isVariantPlaylist;
      private boolean isDiscontinuity;
      private int targetDuration;
      private int sequenceNumber;
      private int dataListIndex;
      private List dataListString;
      private List dataListInteger;
      private List dataListDouble;
      private List dataListBoolean;

      private PlaylistParser() {
         this.isFirstLine = true;
         this.isLineMediaFileURI = false;
         this.isEndList = false;
         this.isLinePlaylistURI = false;
         this.isVariantPlaylist = false;
         this.isDiscontinuity = false;
         this.targetDuration = 0;
         this.sequenceNumber = 0;
         this.dataListIndex = -1;
         this.dataListString = new ArrayList();
         this.dataListInteger = new ArrayList();
         this.dataListDouble = new ArrayList();
         this.dataListBoolean = new ArrayList();
      }

      private void load(URI var1) {
         HttpURLConnection var2 = null;
         BufferedReader var3 = null;

         try {
            var2 = (HttpURLConnection)var1.toURL().openConnection();
            var2.setRequestMethod("GET");
            if (var2.getResponseCode() != 200) {
               MediaUtils.error(this, MediaError.ERROR_LOCATOR_CONNECTION_LOST.code(), "HTTP responce code: " + var2.getResponseCode(), (Throwable)null);
            }

            Charset var4 = this.getCharset(var1.toURL().toExternalForm(), var2.getContentType());
            if (var4 != null) {
               var3 = new BufferedReader(new InputStreamReader(var2.getInputStream(), var4));
            }

            boolean var5;
            if (var3 != null) {
               do {
                  var5 = this.parseLine(var3.readLine());
               } while(var5);
            }
         } catch (MalformedURLException var16) {
         } catch (IOException var17) {
         } finally {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (IOException var15) {
               }

               Locator.closeConnection(var2);
            }

         }

      }

      private boolean isVariantPlaylist() {
         return this.isVariantPlaylist;
      }

      private boolean isLivePlaylist() {
         return !this.isEndList;
      }

      private int getTargetDuration() {
         return this.targetDuration;
      }

      private int getSequenceNumber() {
         return this.sequenceNumber;
      }

      private boolean hasNext() {
         ++this.dataListIndex;
         return this.dataListString.size() > this.dataListIndex || this.dataListInteger.size() > this.dataListIndex || this.dataListDouble.size() > this.dataListIndex || this.dataListBoolean.size() > this.dataListIndex;
      }

      private String getString() {
         return (String)this.dataListString.get(this.dataListIndex);
      }

      private Integer getInteger() {
         return (Integer)this.dataListInteger.get(this.dataListIndex);
      }

      private Double getDouble() {
         return (Double)this.dataListDouble.get(this.dataListIndex);
      }

      private Boolean getBoolean() {
         return (Boolean)this.dataListBoolean.get(this.dataListIndex);
      }

      private boolean parseLine(String var1) {
         if (var1 == null) {
            return false;
         } else if (this.isFirstLine) {
            if (var1.compareTo("#EXTM3U") != 0) {
               return false;
            } else {
               this.isFirstLine = false;
               return true;
            }
         } else if (var1.isEmpty() || var1.startsWith("#") && !var1.startsWith("#EXT")) {
            return true;
         } else {
            String[] var2;
            String[] var3;
            if (var1.startsWith("#EXTINF")) {
               var2 = var1.split(":");
               if (var2.length == 2 && var2[1].length() > 0) {
                  var3 = var2[1].split(",");
                  if (var3.length >= 1) {
                     this.dataListDouble.add(Double.parseDouble(var3[0]));
                  }
               }

               this.isLineMediaFileURI = true;
            } else if (var1.startsWith("#EXT-X-TARGETDURATION")) {
               var2 = var1.split(":");
               if (var2.length == 2 && var2[1].length() > 0) {
                  this.targetDuration = Integer.parseInt(var2[1]);
               }
            } else if (var1.startsWith("#EXT-X-MEDIA-SEQUENCE")) {
               var2 = var1.split(":");
               if (var2.length == 2 && var2[1].length() > 0) {
                  this.sequenceNumber = Integer.parseInt(var2[1]);
               }
            } else if (var1.startsWith("#EXT-X-STREAM-INF")) {
               this.isVariantPlaylist = true;
               int var7 = 0;
               var3 = var1.split(":");
               if (var3.length == 2 && var3[1].length() > 0) {
                  String[] var4 = var3[1].split(",");
                  if (var4.length > 0) {
                     for(int var5 = 0; var5 < var4.length; ++var5) {
                        var4[var5] = var4[var5].trim();
                        if (var4[var5].startsWith("BANDWIDTH")) {
                           String[] var6 = var4[var5].split("=");
                           if (var6.length == 2 && var6[1].length() > 0) {
                              var7 = Integer.parseInt(var6[1]);
                           }
                        }
                     }
                  }
               }

               if (var7 < 1) {
                  return false;
               }

               this.dataListInteger.add(var7);
               this.isLinePlaylistURI = true;
            } else if (var1.startsWith("#EXT-X-ENDLIST")) {
               this.isEndList = true;
            } else if (var1.startsWith("#EXT-X-DISCONTINUITY")) {
               this.isDiscontinuity = true;
            } else if (this.isLinePlaylistURI) {
               this.isLinePlaylistURI = false;
               this.dataListString.add(var1);
            } else if (this.isLineMediaFileURI) {
               this.isLineMediaFileURI = false;
               this.dataListString.add(var1);
               this.dataListBoolean.add(this.isDiscontinuity);
               this.isDiscontinuity = false;
            }

            return true;
         }
      }

      private Charset getCharset(String var1, String var2) {
         if ((var1 == null || !HLSConnectionHolder.stripParameters(var1).endsWith(".m3u8")) && (var2 == null || !var2.equals("application/vnd.apple.mpegurl"))) {
            if ((var1 != null && HLSConnectionHolder.stripParameters(var1).endsWith(".m3u") || var2 != null && var2.equals("audio/mpegurl")) && Charset.isSupported("US-ASCII")) {
               return Charset.forName("US-ASCII");
            }
         } else if (Charset.isSupported("UTF-8")) {
            return Charset.forName("UTF-8");
         }

         return null;
      }

      // $FF: synthetic method
      PlaylistParser(Object var1) {
         this();
      }
   }

   private class PlaylistThread extends Thread {
      public static final int STATE_INIT = 0;
      public static final int STATE_EXIT = 1;
      public static final int STATE_RELOAD_PLAYLIST = 2;
      private BlockingQueue stateQueue;
      private URI playlistURI;
      private Playlist reloadPlaylist;
      private final Object reloadLock;
      private volatile boolean stopped;

      private PlaylistThread() {
         this.stateQueue = new LinkedBlockingQueue();
         this.playlistURI = null;
         this.reloadPlaylist = null;
         this.reloadLock = new Object();
         this.stopped = false;
         this.setName("JFXMedia HLS Playlist Thread");
         this.setDaemon(true);
      }

      private void setPlaylistURI(URI var1) {
         this.playlistURI = var1;
      }

      private void setReloadPlaylist(Playlist var1) {
         synchronized(this.reloadLock) {
            this.reloadPlaylist = var1;
         }
      }

      public void run() {
         while(!this.stopped) {
            try {
               int var1 = (Integer)this.stateQueue.take();
               switch (var1) {
                  case 0:
                     this.stateInit();
                     break;
                  case 1:
                     this.stopped = true;
                     break;
                  case 2:
                     this.stateReloadPlaylist();
               }
            } catch (Exception var2) {
            }
         }

      }

      private void putState(int var1) {
         if (this.stateQueue != null) {
            try {
               this.stateQueue.put(var1);
            } catch (InterruptedException var3) {
            }
         }

      }

      private void stateInit() {
         if (this.playlistURI != null) {
            PlaylistParser var1 = new PlaylistParser();
            var1.load(this.playlistURI);
            if (var1.isVariantPlaylist()) {
               HLSConnectionHolder.this.variantPlaylist = new VariantPlaylist(this.playlistURI);

               while(var1.hasNext()) {
                  HLSConnectionHolder.this.variantPlaylist.addPlaylistInfo(var1.getString(), var1.getInteger());
               }
            } else {
               if (HLSConnectionHolder.this.currentPlaylist == null) {
                  HLSConnectionHolder.this.currentPlaylist = HLSConnectionHolder.this.new Playlist(var1.isLivePlaylist(), var1.getTargetDuration());
                  HLSConnectionHolder.this.currentPlaylist.setPlaylistURI(this.playlistURI);
               }

               if (HLSConnectionHolder.this.currentPlaylist.setSequenceNumber(var1.getSequenceNumber())) {
                  while(var1.hasNext()) {
                     HLSConnectionHolder.this.currentPlaylist.addMediaFile(var1.getString(), var1.getDouble(), var1.getBoolean());
                  }
               }

               if (HLSConnectionHolder.this.variantPlaylist != null) {
                  HLSConnectionHolder.this.variantPlaylist.addPlaylist(HLSConnectionHolder.this.currentPlaylist);
               }
            }

            if (HLSConnectionHolder.this.variantPlaylist != null) {
               while(HLSConnectionHolder.this.variantPlaylist.hasNext()) {
                  try {
                     HLSConnectionHolder.this.currentPlaylist = HLSConnectionHolder.this.new Playlist(HLSConnectionHolder.this.variantPlaylist.getPlaylistURI());
                     HLSConnectionHolder.this.currentPlaylist.update((String)null);
                     HLSConnectionHolder.this.variantPlaylist.addPlaylist(HLSConnectionHolder.this.currentPlaylist);
                  } catch (URISyntaxException var3) {
                  } catch (MalformedURLException var4) {
                  }
               }
            }

            if (HLSConnectionHolder.this.variantPlaylist != null) {
               HLSConnectionHolder.this.currentPlaylist = HLSConnectionHolder.this.variantPlaylist.getPlaylist(0);
               HLSConnectionHolder.this.isBitrateAdjustable = true;
            }

            if (HLSConnectionHolder.this.currentPlaylist.isLive()) {
               this.setReloadPlaylist(HLSConnectionHolder.this.currentPlaylist);
               this.putState(2);
            }

            HLSConnectionHolder.this.readySignal.countDown();
         }
      }

      private void stateReloadPlaylist() {
         try {
            long var1;
            synchronized(this.reloadLock) {
               var1 = this.reloadPlaylist.getTargetDuration() / 2L;
            }

            Thread.sleep(var1);
         } catch (InterruptedException var8) {
            return;
         }

         synchronized(this.reloadLock) {
            this.reloadPlaylist.update((String)null);
         }

         this.putState(2);
      }

      // $FF: synthetic method
      PlaylistThread(Object var2) {
         this();
      }
   }
}
