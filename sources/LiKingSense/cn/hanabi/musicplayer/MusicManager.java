/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javafx.scene.media.Media
 *  javafx.scene.media.MediaPlayer
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.IImageBuffer
 *  net.minecraft.client.renderer.ImageBufferDownload
 *  net.minecraft.client.renderer.ThreadDownloadImageData
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.util.ResourceLocation
 *  org.apache.commons.io.FileUtils
 *  org.apache.http.HttpEntity
 *  org.apache.http.HttpResponse
 *  org.apache.http.client.methods.HttpGet
 *  org.apache.http.client.methods.HttpUriRequest
 *  org.apache.http.impl.client.CloseableHttpClient
 *  org.apache.http.impl.client.HttpClients
 */
package cn.hanabi.musicplayer;

import cn.hanabi.musicplayer.api.CloudMusicAPI;
import cn.hanabi.musicplayer.impl.Lyric;
import cn.hanabi.musicplayer.impl.Track;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.imageio.ImageIO;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class MusicManager {
    public static MusicManager INSTANCE;
    public static boolean showMsg;
    private final HashMap<Long, ResourceLocation> artsLocations = new HashMap();
    private final File musicFolder;
    private final File artPicFolder;
    public Track currentTrack = null;
    public ArrayList<Track> playlist = new ArrayList();
    public Thread loadingThread = null;
    public Thread analyzeThread = null;
    public float downloadProgress = 0.0f;
    public boolean repeat = false;
    public float cacheProgress = 0.0f;
    public float[] magnitudes;
    public float[] smoothMagnitudes;
    public Thread lyricAnalyzeThread = null;
    public boolean lyric = false;
    public boolean noUpdate = false;
    public CopyOnWriteArrayList<Lyric> lrc = new CopyOnWriteArrayList();
    public CopyOnWriteArrayList<Lyric> tlrc = new CopyOnWriteArrayList();
    public HashMap<Long, ResourceLocation> circleLocations = new HashMap();
    public String lrcCur = "_EMPTY_";
    public String tlrcCur = "_EMPTY_";
    public int lrcIndex = 0;
    public int tlrcIndex = 0;
    public File circleImage;
    private MediaPlayer mediaPlayer;

    public MusicManager() {
        Minecraft mc = Minecraft.func_71410_x();
        this.musicFolder = new File(mc.field_71412_D, ".cache/musicCache");
        this.artPicFolder = new File(mc.field_71412_D, ".cache/artCache");
        File cookie = new File(mc.field_71412_D, ".cache/cookies.txt");
        if (!this.artPicFolder.exists()) {
            this.artPicFolder.mkdirs();
        }
        if (!this.musicFolder.exists()) {
            this.musicFolder.mkdirs();
        }
        this.circleImage = new File(Minecraft.func_71410_x().field_71412_D.toString() + File.separator + "LiKingSense" + File.separator + "circleImage");
        if (!this.circleImage.exists()) {
            this.circleImage.mkdirs();
        }
        if (cookie.exists()) {
            try {
                String[] split = FileUtils.readFileToString((File)cookie).split(";");
                CloudMusicAPI.INSTANCE.cookies = new String[split.length][2];
                for (int i = 0; i < split.length; ++i) {
                    CloudMusicAPI.INSTANCE.cookies[i][0] = split[i].split("=")[0];
                    CloudMusicAPI.INSTANCE.cookies[i][1] = split[i].split("=")[1];
                }
                new Thread(() -> {
                    try {
                        CloudMusicAPI.INSTANCE.refreshState();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void loadFromCache(final long id) {
        if (this.artsLocations.containsKey(id)) {
            return;
        }
        File path = new File(this.artPicFolder.getAbsolutePath() + File.separator + id);
        if (!path.exists()) {
            return;
        }
        new Thread(() -> {
            this.artsLocations.put(id, null);
            final ResourceLocation rl = new ResourceLocation("cloudMusicCache/" + id);
            IImageBuffer iib = new IImageBuffer(){
                final ImageBufferDownload ibd = new ImageBufferDownload();

                public BufferedImage func_78432_a(BufferedImage image2) {
                    return image2;
                }

                public void func_152634_a() {
                    MusicManager.this.artsLocations.put(id, rl);
                }
            };
            ThreadDownloadImageData textureArt = new ThreadDownloadImageData(path, null, null, iib);
            Minecraft.func_71410_x().func_110434_K().func_110579_a(rl, (ITextureObject)textureArt);
        }).start();
    }

    public ResourceLocation getArt(long id) {
        return this.artsLocations.get(id);
    }

    public void play(Track track) throws Exception {
        this.noUpdate = false;
        this.lrcIndex = 0;
        this.tlrcIndex = 0;
        if (this.currentTrack != null && this.currentTrack.id == track.id) {
            this.noUpdate = true;
        } else {
            this.lrc.clear();
            this.tlrc.clear();
            this.lrcCur = "\u7b49\u5f85\u6b4c\u8bcd\u89e3\u6790\u56de\u5e94...";
            this.tlrcCur = "\u7b49\u5f85\u6b4c\u8bcd\u89e3\u6790\u56de\u5e94...";
        }
        this.currentTrack = track;
        INSTANCE.loadFromCache(track.id);
        this.downloadProgress = 0.0f;
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
                }
                catch (Exception ex) {
                    ClientUtils.displayChatMessage("\u7f13\u5b58\u97f3\u4e50\u65f6\u53d1\u751f\u9519\u8bef, \u53ef\u80fd\u662f\u56e0\u4e3a\u8be5\u6b4c\u66f2\u5df2\u88ab\u4e0b\u67b6\u6216\u9700\u8981VIP!");
                    if (mp3File.exists()) {
                        mp3File.delete();
                    }
                    if (flacFile.exists()) {
                        flacFile.delete();
                    }
                    ex.printStackTrace();
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
                for (int i = 0; i < magnitudes.length; ++i) {
                    this.magnitudes[i] = magnitudes[i] - (float)this.mediaPlayer.getAudioSpectrumThreshold();
                }
            });
            this.mediaPlayer.setOnEndOfMedia(() -> {
                if (this.repeat) {
                    try {
                        this.play(this.currentTrack);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
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
                        this.lrcCur = "(\u89e3\u6790\u65f6\u53d1\u751f\u9519\u8bef\u6216\u6b4c\u8bcd\u4e0d\u5b58\u5728)";
                        this.lrc.clear();
                    }
                    if (!lyrics[1].equals("")) {
                        if (lyrics[1].equals("_NOLYRIC_")) {
                            this.tlrcCur = "\u7eaf\u97f3\u4e50, \u8bf7\u6b23\u8d4f";
                        } else if (lyrics[1].equals("_UNCOLLECT_")) {
                            this.tlrcCur = "\u8be5\u6b4c\u66f2\u6682\u65e0\u6b4c\u8bcd";
                        } else {
                            CloudMusicAPI.INSTANCE.analyzeLyric(this.tlrc, lyrics[1]);
                        }
                    } else {
                        this.tlrcCur = "(\u89e3\u6790\u65f6\u53d1\u751f\u9519\u8bef\u6216\u7ffb\u8bd1\u6b4c\u8bcd\u4e0d\u5b58\u5728)";
                        this.tlrc.clear();
                    }
                }
                catch (Exception ex) {
                    this.lrc.clear();
                    this.tlrc.clear();
                    this.lrcCur = this.currentTrack.name;
                    this.tlrcCur = "(\u83b7\u53d6\u6b4c\u8bcd\u65f6\u51fa\u73b0\u9519\u8bef)";
                    ex.printStackTrace();
                }
            });
            this.lyricAnalyzeThread.start();
        }
    }

    @EventTarget
    public void onTick(TickEvent evt) {
        if (this.getMediaPlayer() != null) {
            long mill = (long)this.getMediaPlayer().getCurrentTime().toMillis();
            if (!this.lrc.isEmpty() && this.lrc.get((int)this.lrcIndex).time < mill) {
                ++this.lrcIndex;
                this.lrcCur = this.lrc.get((int)(this.lrcIndex - 1)).text;
                if (this.tlrc.isEmpty()) {
                    String string = this.tlrcCur = this.lrcIndex > this.lrc.size() - 1 ? "" : this.lrc.get((int)this.lrcIndex).text;
                }
            }
            if (!this.tlrc.isEmpty() && this.tlrc.get((int)this.tlrcIndex).time < mill) {
                ++this.tlrcIndex;
                this.tlrcCur = this.tlrcIndex - 1 > this.tlrc.size() - 1 ? "" : this.tlrc.get((int)(this.tlrcIndex - 1)).text;
            }
        }
    }

    public void getCircle(final Track track) {
        if (this.circleLocations.containsKey(track.id)) {
            return;
        }
        try {
            if (!new File(this.circleImage.getAbsolutePath() + File.separator + track.id).exists()) {
                this.makeCirclePicture(track, 128, this.circleImage.getAbsolutePath() + File.separator + track.id);
            }
            final ResourceLocation rl2 = new ResourceLocation("circle/" + track.id);
            IImageBuffer iib2 = new IImageBuffer(){

                public BufferedImage func_78432_a(BufferedImage a) {
                    return a;
                }

                public void func_152634_a() {
                    MusicManager.this.circleLocations.put(track.id, rl2);
                }
            };
            ThreadDownloadImageData textureArt2 = new ThreadDownloadImageData(new File(this.circleImage.getAbsolutePath() + File.separator + track.id), null, null, iib2);
            Minecraft.func_71410_x().func_110434_K().func_110579_a(rl2, (ITextureObject)textureArt2);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void makeCirclePicture(Track track, int wid, String path) {
        try {
            BufferedImage avatarImage = ImageIO.read(new URL(track.picUrl));
            BufferedImage formatAvatarImage = new BufferedImage(wid, wid, 6);
            Graphics2D graphics = formatAvatarImage.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int border = 0;
            Ellipse2D.Double shape = new Ellipse2D.Double(border, border, wid - border * 2, wid - border * 2);
            graphics.setClip(shape);
            graphics.drawImage(avatarImage, border, border, wid - border * 2, wid - border * 2, null);
            graphics.dispose();
            try (FileOutputStream os = new FileOutputStream(path);){
                ImageIO.write((RenderedImage)formatAvatarImage, "png", os);
            }
            catch (Exception exception) {}
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void downloadFile(String url, String filepath) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute((HttpUriRequest)httpget);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            File file = new File(filepath);
            FileOutputStream fileout = new FileOutputStream(file);
            byte[] buffer = new byte[10240];
            int ch = 0;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer, 0, ch);
            }
            is.close();
            fileout.flush();
            fileout.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void next() {
        block5: {
            try {
                if (this.playlist.isEmpty()) break block5;
                if (this.currentTrack == null) {
                    this.play(this.playlist.get(0));
                    break block5;
                }
                boolean playNext = false;
                for (Track t2 : this.playlist) {
                    if (playNext) {
                        this.play(t2);
                        break;
                    }
                    if (t2.id != this.currentTrack.id) continue;
                    playNext = true;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void prev() {
        block7: {
            try {
                if (this.playlist.isEmpty()) break block7;
                if (this.currentTrack == null) {
                    this.play(this.playlist.get(0));
                    break block7;
                }
                boolean playPrev = false;
                for (int i = 0; i < this.playlist.size(); ++i) {
                    Track t2 = this.playlist.get(i);
                    if (playPrev) {
                        if (i - 2 < 0) {
                            this.play(this.playlist.get(this.playlist.size() - 1));
                        } else {
                            this.play(this.playlist.get(i - 2));
                        }
                        break;
                    }
                    if (t2.id != this.currentTrack.id) continue;
                    playPrev = true;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
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

    static {
        showMsg = false;
        INSTANCE = new MusicManager();
    }
}

