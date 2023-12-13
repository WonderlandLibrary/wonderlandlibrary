package cn.hanabi.gui.cloudmusic;

import cn.hanabi.events.EventTick;
import cn.hanabi.gui.cloudmusic.api.CloudMusicAPI;
import cn.hanabi.gui.cloudmusic.impl.Lyric;
import cn.hanabi.gui.cloudmusic.impl.Track;
import cn.hanabi.utils.PlayerUtil;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class MusicManager {
   public static MusicManager INSTANCE = new MusicManager();
   public Track currentTrack = null;
   public ArrayList playlist = new ArrayList();
   public Thread loadingThread = null;
   public Thread analyzeThread = null;
   public float downloadProgress = 0.0F;
   public boolean repeat = false;
   public float cacheProgress = 0.0F;
   public float[] magnitudes;
   public float[] smoothMagnitudes;
   public Thread lyricAnalyzeThread = null;
   public boolean lyric = false;
   public static boolean showMsg = false;
   public boolean noUpdate = false;
   public CopyOnWriteArrayList lrc = new CopyOnWriteArrayList();
   public CopyOnWriteArrayList tlrc = new CopyOnWriteArrayList();
   public HashMap circleLocations = new HashMap();
   public String lrcCur = "_EMPTY_";
   public String tlrcCur = "_EMPTY_";
   public int lrcIndex = 0;
   public int tlrcIndex = 0;
   public File circleImage;
   private final HashMap artsLocations = new HashMap();
   private MediaPlayer mediaPlayer;
   private final File musicFolder;
   private final File artPicFolder;

   public MusicManager() {
      Minecraft mc = Minecraft.getMinecraft();
      this.musicFolder = new File(mc.mcDataDir, ".cache/musicCache");
      this.artPicFolder = new File(mc.mcDataDir, ".cache/artCache");
      File cookie = new File(mc.mcDataDir, ".cache/cookies.txt");
      if (!this.artPicFolder.exists()) {
         this.artPicFolder.mkdirs();
      }

      if (!this.musicFolder.exists()) {
         this.musicFolder.mkdirs();
      }

      this.circleImage = new File(Minecraft.getMinecraft().mcDataDir.toString() + File.separator + "Hanabi" + File.separator + "circleImage");
      if (!this.circleImage.exists()) {
         this.circleImage.mkdirs();
      }

      SwingUtilities.invokeLater(JFXPanel::new);
      if (cookie.exists()) {
         try {
            String[] split = FileUtils.readFileToString(cookie).split(";");
            CloudMusicAPI.INSTANCE.cookies = new String[split.length][2];

            for(int i = 0; i < split.length; ++i) {
               CloudMusicAPI.INSTANCE.cookies[i][0] = split[i].split("=")[0];
               CloudMusicAPI.INSTANCE.cookies[i][1] = split[i].split("=")[1];
            }

            (new Thread(() -> {
               try {
                  CloudMusicAPI.INSTANCE.refreshState();
               } catch (Exception var1) {
                  var1.printStackTrace();
               }

            })).start();
         } catch (Exception var5) {
            var5.printStackTrace();
         }
      }

      EventManager.register(this);
   }

   public void loadFromCache(long id) {
      if (!this.artsLocations.containsKey(id)) {
         File path = new File(this.artPicFolder.getAbsolutePath() + File.separator + id);
         if (path.exists()) {
            (new Thread(() -> {
               this.artsLocations.put(id, (Object)null);
               final ResourceLocation rl = new ResourceLocation("cloudMusicCache/" + id);
               IImageBuffer iib = new IImageBuffer() {
                  final ImageBufferDownload ibd = new ImageBufferDownload();

                  public BufferedImage parseUserSkin(BufferedImage image) {
                     return image;
                  }

                  public void skinAvailable() {
                     MusicManager.this.artsLocations.put(id, rl);
                  }
               };
               ThreadDownloadImageData textureArt = new ThreadDownloadImageData(path, (String)null, (ResourceLocation)null, iib);
               Minecraft.getMinecraft().getTextureManager().loadTexture(rl, textureArt);
            })).start();
         }
      }
   }

   public ResourceLocation getArt(long id) {
      return (ResourceLocation)this.artsLocations.get(id);
   }

   public void play(Track track) {
      this.noUpdate = false;
      this.lrcIndex = 0;
      this.tlrcIndex = 0;
      if (this.currentTrack != null && this.currentTrack.id == track.id) {
         this.noUpdate = true;
      } else {
         this.lrc.clear();
         this.tlrc.clear();
         this.lrcCur = "等待歌词解析回应...";
         this.tlrcCur = "等待歌词解析回应...";
      }

      this.currentTrack = track;
      INSTANCE.loadFromCache(track.id);
      this.downloadProgress = 0.0F;
      if (!showMsg) {
         showMsg = true;
      }

      if (this.mediaPlayer != null) {
         this.mediaPlayer.stop();
      }

      File mp3File = new File(this.musicFolder, track.id + ".mp3");
      File flacFile = new File(this.musicFolder, track.id + ".flac");
      File artFile = new File(this.artPicFolder, "" + track.id);
      if (!mp3File.exists() && !flacFile.exists()) {
         if (this.loadingThread != null) {
            this.loadingThread.interrupt();
         }

         this.loadingThread = new Thread(() -> {
            try {
               String addr = (String)CloudMusicAPI.INSTANCE.getDownloadUrl(String.valueOf(track.id), 128000L)[1];
               CloudMusicAPI.INSTANCE.downloadFile(addr, addr.endsWith(".flac") ? flacFile.getAbsolutePath() : mp3File.getAbsolutePath());
               INSTANCE.downloadFile(track.picUrl, artFile.getAbsolutePath());
               this.play(track);
            } catch (Exception var6) {
               PlayerUtil.tellPlayer("缓存音乐时发生错误, 可能是因为该歌曲已被下架或需要VIP!");
               if (mp3File.exists()) {
                  mp3File.delete();
               }

               if (flacFile.exists()) {
                  flacFile.delete();
               }

               var6.printStackTrace();
            }

            this.loadingThread = null;
         });
         this.loadingThread.start();
      } else {
         Media hit = new Media(mp3File.exists() ? mp3File.toURI().toString() : flacFile.toURI().toString());
         this.mediaPlayer = new MediaPlayer(hit);
         this.mediaPlayer.setVolume(1.0);
         this.mediaPlayer.setAutoPlay(true);
         this.mediaPlayer.setAudioSpectrumNumBands(128);
         this.mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
            if (this.magnitudes == null || this.magnitudes.length < magnitudes.length || this.magnitudes.length > magnitudes.length) {
               this.magnitudes = new float[magnitudes.length];
               this.smoothMagnitudes = new float[magnitudes.length];
            }

            for(int i = 0; i < magnitudes.length; ++i) {
               this.magnitudes[i] = magnitudes[i] - (float)this.mediaPlayer.getAudioSpectrumThreshold();
            }

         });
         this.mediaPlayer.setOnEndOfMedia(() -> {
            if (this.repeat) {
               this.play(this.currentTrack);
            } else {
               this.next();
            }

         });
      }

      if (!this.noUpdate) {
         if (this.lyricAnalyzeThread != null) {
            this.lyricAnalyzeThread.interrupt();
         }

         this.lyricAnalyzeThread = new Thread(() -> {
            try {
               String[] lyrics = CloudMusicAPI.INSTANCE.requestLyric(CloudMusicAPI.INSTANCE.getLyricJson(String.valueOf(track.id)));
               this.lrc.clear();
               this.tlrc.clear();
               if (!lyrics[0].equals("")) {
                  if (lyrics[0].equals("_NOLYRIC_")) {
                     this.lrcCur = this.currentTrack.name;
                  } else {
                     CloudMusicAPI.INSTANCE.analyzeLyric(this.lrc, lyrics[0]);
                  }
               } else {
                  this.lrcCur = "(解析时发生错误或歌词不存在)";
                  this.lrc.clear();
               }

               if (!lyrics[1].equals("")) {
                  if (lyrics[1].equals("_NOLYRIC_")) {
                     this.tlrcCur = "纯音乐, 请欣赏";
                  } else if (lyrics[1].equals("_UNCOLLECT_")) {
                     this.tlrcCur = "该歌曲暂无歌词";
                  } else {
                     CloudMusicAPI.INSTANCE.analyzeLyric(this.tlrc, lyrics[1]);
                  }
               } else {
                  this.tlrcCur = "(解析时发生错误或翻译歌词不存在)";
                  this.tlrc.clear();
               }
            } catch (Exception var3) {
               this.lrc.clear();
               this.tlrc.clear();
               this.lrcCur = this.currentTrack.name;
               this.tlrcCur = "(获取歌词时出现错误)";
               var3.printStackTrace();
            }

         });
         this.lyricAnalyzeThread.start();
      }

   }

   @EventTarget
   public void onTick(EventTick evt) {
      if (this.getMediaPlayer() != null) {
         long mill = (long)this.getMediaPlayer().getCurrentTime().toMillis();
         if (!this.lrc.isEmpty() && ((Lyric)this.lrc.get(this.lrcIndex)).time < mill) {
            ++this.lrcIndex;
            this.lrcCur = ((Lyric)this.lrc.get(this.lrcIndex - 1)).text;
            if (this.tlrc.isEmpty()) {
               this.tlrcCur = this.lrcIndex > this.lrc.size() - 1 ? "" : ((Lyric)this.lrc.get(this.lrcIndex)).text;
            }
         }

         if (!this.tlrc.isEmpty() && ((Lyric)this.tlrc.get(this.tlrcIndex)).time < mill) {
            ++this.tlrcIndex;
            this.tlrcCur = this.tlrcIndex - 1 > this.tlrc.size() - 1 ? "" : ((Lyric)this.tlrc.get(this.tlrcIndex - 1)).text;
         }
      }

   }

   public void getCircle(final Track track) {
      if (!this.circleLocations.containsKey(track.id)) {
         try {
            if (!(new File(this.circleImage.getAbsolutePath() + File.separator + track.id)).exists()) {
               this.makeCirclePicture(track, 128, this.circleImage.getAbsolutePath() + File.separator + track.id);
            }

            final ResourceLocation rl2 = new ResourceLocation("circle/" + track.id);
            IImageBuffer iib2 = new IImageBuffer() {
               public BufferedImage parseUserSkin(BufferedImage a) {
                  return a;
               }

               public void skinAvailable() {
                  MusicManager.this.circleLocations.put(track.id, rl2);
               }
            };
            ThreadDownloadImageData textureArt2 = new ThreadDownloadImageData(new File(this.circleImage.getAbsolutePath() + File.separator + track.id), (String)null, (ResourceLocation)null, iib2);
            Minecraft.getMinecraft().getTextureManager().loadTexture(rl2, textureArt2);
         } catch (Exception var5) {
            var5.printStackTrace();
         }

      }
   }

   public void makeCirclePicture(Track track, int wid, String path) {
      try {
         BufferedImage avatarImage = ImageIO.read(new URL(track.picUrl));
         BufferedImage formatAvatarImage = new BufferedImage(wid, wid, 6);
         Graphics2D graphics = formatAvatarImage.createGraphics();
         graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
         int border = 0;
         Ellipse2D.Double shape = new Ellipse2D.Double((double)border, (double)border, (double)(wid - border * 2), (double)(wid - border * 2));
         graphics.setClip(shape);
         graphics.drawImage(avatarImage, border, border, wid - border * 2, wid - border * 2, (ImageObserver)null);
         graphics.dispose();

         try {
            OutputStream os = new FileOutputStream(path);
            Throwable var24 = null;

            try {
               ImageIO.write(formatAvatarImage, "png", os);
            } catch (Throwable var19) {
               var24 = var19;
               throw var19;
            } finally {
               if (os != null) {
                  if (var24 != null) {
                     try {
                        os.close();
                     } catch (Throwable var18) {
                        var24.addSuppressed(var18);
                     }
                  } else {
                     os.close();
                  }
               }

            }
         } catch (Exception var21) {
         }
      } catch (Exception var22) {
         var22.printStackTrace();
      }

   }

   public void downloadFile(String url, String filepath) {
      try {
         HttpClient client = HttpClients.createDefault();
         HttpGet httpget = new HttpGet(url);
         HttpResponse response = client.execute(httpget);
         HttpEntity entity = response.getEntity();
         InputStream is = entity.getContent();
         File file = new File(filepath);
         FileOutputStream fileout = new FileOutputStream(file);
         byte[] buffer = new byte[10240];
         int ch = false;

         int ch;
         while((ch = is.read(buffer)) != -1) {
            fileout.write(buffer, 0, ch);
         }

         is.close();
         fileout.flush();
         fileout.close();
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }

   public void next() {
      if (!this.playlist.isEmpty()) {
         if (this.currentTrack == null) {
            this.play((Track)this.playlist.get(0));
         } else {
            boolean playNext = false;
            Iterator var2 = this.playlist.iterator();

            while(var2.hasNext()) {
               Track t = (Track)var2.next();
               if (playNext) {
                  this.play(t);
                  break;
               }

               if (t.id == this.currentTrack.id) {
                  playNext = true;
               }
            }
         }
      }

   }

   public void prev() {
      if (!this.playlist.isEmpty()) {
         if (this.currentTrack == null) {
            this.play((Track)this.playlist.get(0));
         } else {
            boolean playPrev = false;

            for(int i = 0; i < this.playlist.size(); ++i) {
               Track t = (Track)this.playlist.get(i);
               if (playPrev) {
                  if (i - 2 < 0) {
                     this.play((Track)this.playlist.get(this.playlist.size() - 1));
                  } else {
                     this.play((Track)this.playlist.get(i - 2));
                  }
                  break;
               }

               if (t.id == this.currentTrack.id) {
                  playPrev = true;
               }
            }
         }
      }

   }

   public Track getCurrentTrack() {
      return this.currentTrack;
   }

   public MediaPlayer getMediaPlayer() {
      return this.mediaPlayer;
   }

   public Thread getLoadingThread() {
      return this.loadingThread;
   }
}
