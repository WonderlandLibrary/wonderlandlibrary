package info.sigmaclient.sigma.gui.clickgui.musicplayer;

import java.util.concurrent.TimeUnit;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import java.util.List;
import java.util.Arrays;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import java.util.Iterator;
import java.io.IOException;
import java.util.Collection;
import info.sigmaclient.sigma.music.youtubedl.YoutubeVideoHelper;
import info.sigmaclient.sigma.utils.music.TextureManager;
import info.sigmaclient.sigma.utils.music.Draw;
import info.sigmaclient.sigma.sigma5.jellomusic.Player;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import java.awt.Color;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import net.minecraft.client.Minecraft;
import info.sigmaclient.sigma.utils.render.anims.AnimationUtils;
import info.sigmaclient.sigma.utils.music.Music163;
import java.util.concurrent.CopyOnWriteArrayList;
import info.sigmaclient.sigma.utils.music.Music;
import info.sigmaclient.sigma.gui.JelloTextField;
import info.sigmaclient.sigma.utils.TimerUtil;

public class JelloMusicPlayer {
        public TimerUtil antiRepeat;
        public JelloTextField searchBar;
        public String currentChannel;
        public String lastSearch;
        public Music currentFile;
        public MusicPlayer mp3;
        public static CopyOnWriteArrayList<Music> musics;
        public Music163 music163;
        public boolean repeat;
        public boolean hide;
        public int scroll;
        public static int currentSongLength;
        public long currentPosition;
        public int x;
        public int y;
        public boolean dragging;
        public double startX;
        public double startY;
        public float lastX;
        public float lastY;
        public static float volumeControl;
        int offsetY;
        double animForMouse;
        public boolean isMouseDown;
        AnimationUtils mouseAnim;
        Thread searchThread;

        public JelloMusicPlayer() {
            this.antiRepeat = new TimerUtil();
            this.currentChannel = "";
            this.lastSearch = "";
            this.currentFile = null;
            this.mp3 = new MusicPlayer();
            this.repeat = false;
            this.hide = false;
            this.offsetY = 0;
            this.isMouseDown = false;
            this.mouseAnim = new AnimationUtils();
            this.x = 500;
            this.y = 100;
            this.music163 = new Music163("");
            JelloMusicPlayer.musics = new CopyOnWriteArrayList<Music>();
        }

    public void init() {
            if (this.searchBar == null) {
                (this.searchBar = new JelloTextField(10, Minecraft.getInstance().fontRenderer, this.x + 121 + 15.5f, this.y - 9 + 20 - 1.5f, 243, 20, "Search...")).setMaxStringLength(32);
            }
        }

    public void handleMouse(final double L, final int mx, final int my) {
            if (ClickUtils.isClickable((float)this.x, (float)(this.y + 30), (float)(this.x + 500), (float)(this.y + 290 - 50), mx, my)) {
                this.animForMouse += L;
            }
        }

    public void doMouseAnim() {
            this.animForMouse = this.mouseAnim.animate(0.0, this.animForMouse, 0.30000001192092896);
            if (this.animForMouse > 1.0 || this.animForMouse < -1.0) {
                this.offsetY += (int)this.animForMouse;
            }
        }

    public void doVolumeControl(final int mouseX, final int mouseY, final float opacity) {
            final float xpos = 386.0f;
            final float ypos = 255.5f;
            final float volume = JelloMusicPlayer.volumeControl;
            RenderUtils.drawRect(this.x + xpos, this.y + ypos, this.x + xpos + 2.0f, this.y + ypos + 20.0f, new Color(0.0f, 0.0f, 0.0f, 0.2f * opacity).getRGB());
            RenderUtils.drawRect(this.x + xpos, this.y + ypos - (-1.0f + volume) * 20.0f, this.x + xpos + 2.0f, this.y + ypos + 20.0f, new Color(1.0f, 1.0f, 1.0f, 0.5f * opacity).getRGB());
            if (this.isMouseDown && ClickUtils.isClickable(this.x + xpos, this.y + ypos, this.x + xpos + 2.0f, this.y + ypos + 21.0f, mouseX, mouseY)) {
                JelloMusicPlayer.volumeControl = Math.min(20.0f, Math.max(0.0f, this.y + ypos + 20.0f - mouseY)) / 20.0f;
                Player.setVolume((float)(int)(JelloMusicPlayer.volumeControl * 100.0f));
            }
        }

    public void reloaded() {
            this.offsetY = 40;
            Draw.caches.clear();
            TextureManager.cache.clear();
            JelloMusicPlayer.musics.clear();
            if (this.searchThread != null && this.searchThread.isAlive()) {
                this.searchThread.stop();
            }
        (this.searchThread = new Thread(() -> {
            if (!this.currentChannel.isEmpty()) {
                try {
                    JelloMusicPlayer.musics.addAll(YoutubeVideoHelper.search(this.currentChannel));
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        })).start();
        }

    public void handleMouseScroll() {
        }

    public void ticks(final int mouseX, final int mouseY) {
            int count = 0;
            int xOffset = 90;
            int calcY = this.offsetY;
            for (final Music file : JelloMusicPlayer.musics) {
                if (this.y - 63 > (float)(this.y + calcY + 5)) {
                    if (count < 2) {
                        ++count;
                    }
                else {
                    calcY += 100;
                    count = 0;
                }
                }
            else if (this.y + 270.0f < this.y + calcY + 5) {
                if (count < 2) {
                    ++count;
                }
                else {
                    calcY += 100;
                    count = 0;
                }
            }
            else {
                if (ClickUtils.isClickable((float)this.x, (float)(this.y + 30), (float)(this.x + 500), (float)(this.y + 290 - 50), mouseX, mouseY) && ClickUtils.isClickable((float)(this.x + 40 + xOffset), (float)(this.y + calcY), this.x + 40 + xOffset + 77.0f, this.y + calcY + 77.0f, mouseX, mouseY)) {
                    file.hover.interpolate(10.0f, 5.0);
                }
                else {
                    file.hover.interpolate(0.0f, 5.0);
                }
                if (count < 2) {
                    ++count;
                    xOffset += (int)86.5f;
                }
                else {
                    xOffset = 90;
                    calcY += 100;
                    count = 0;
                }
            }
            }
        }

    public void drawMusics(final int mouseX, final int mouseY, final float opacity) {
            StencilUtil.initStencilToWrite();
            RenderUtils.drawRect((float)this.x, (float)(this.y + 30), (float)(this.x + 500), (float)(this.y + 290 - 50), -1);
            StencilUtil.readStencilBuffer(1);
            int count = 0;
            int xOffset = 90;
            int calcY = this.offsetY;
            for (final Music file : JelloMusicPlayer.musics) {
                if (this.y - 63 > (float)(this.y + calcY + 5)) {
                    if (count < 2) {
                        ++count;
                    }
                else {
                    calcY += 100;
                    count = 0;
                }
                }
            else if (this.y + 270.0f < this.y + calcY + 5) {
                if (count < 2) {
                    ++count;
                }
                else {
                    calcY += 100;
                    count = 0;
                }
            }
            else {
                if (file.textureImage != null) {
                    GlStateManager.color(1.0f, 1.0f, 1.0f, opacity);
                    RenderUtils.drawShadow((float)(this.x + 40 + xOffset), (float)(this.y + calcY), this.x + 40 + xOffset + 77.0f, this.y + calcY + 77.0f, opacity);
                    GlStateManager.color(1.0f, 1.0f, 1.0f, opacity);
                    final float scaled = 1.7821782f;
                    final float add = scaled - 1.0f;
                    file.textureImage.rectTextureMasked2((float)(this.x + 40 + xOffset), (float)(this.y + calcY), 77.0f, 77.0f, 115.5f, 102.41f, 19.25f, 12.705002f);
                    if (file.hover.getValue() / 10.0f > 0.0f) {
                        RenderUtils.drawTexture(this.x + 40 + xOffset + 20.0f + 7.0f, this.y + calcY + 20.0f + 5.0f, 25.0, 23.5, "musicplayer/bigplay", (float)(file.hover.getValue() / 10.0f * 0.45));
                        StencilUtil.uninitStencilBuffer();
                        StencilUtil.initStencilToWrite();
                        RenderUtils.drawRect((float)this.x, (float)(this.y + 30), (float)(this.x + 500), (float)(this.y + 290 - 50), -1);
                        StencilUtil.readStencilBuffer(1);
                    }
                }
                else {
                    RenderUtils.drawRect((float)(this.x + 40 + xOffset), (float)(this.y + calcY), this.x + 40 + xOffset + 60.0f, this.y + calcY + 60.0f, new Color(100, 100, 100).getRGB());
                }
                JelloFontUtil.jelloFont14.drawCenteredString((file.name.length() > 20) ? file.name.substring((int)(System.currentTimeMillis() / 100L % (file.name.length() - 20)), (int)(System.currentTimeMillis() / 100L % (file.name.length() - 20)) + 19) : file.name, (float)(this.x + 40 + 10 + xOffset + 29), (float)(this.y + calcY + 5 + 77), new Color(255, 255, 255, (int)(255.0f * opacity)).getRGB());
                JelloFontUtil.jelloFont14.drawCenteredString(file.aliasName, (float)(this.x + 40 + 10 + xOffset + 29), this.y + calcY + 5 + 77 + 8.0f, new Color(255, 255, 255, (int)(180.0f * opacity)).getRGB());
                if (count < 2) {
                    ++count;
                    xOffset += (int)86.5f;
                }
                else {
                    xOffset = 90;
                    calcY += 100;
                    count = 0;
                }
            }
            }
        StencilUtil.uninitStencilBuffer();
        }

    public void drawRepeat(final double x, final double y, final float opacity) {
            GL11.glPushMatrix();
            final boolean enableBlend = GL11.glIsEnabled(3042);
            final boolean disableAlpha = !GL11.glIsEnabled(3008);
            if (!enableBlend) {
                GL11.glEnable(3042);
            }
        if (!disableAlpha) {
            GL11.glDisable(3008);
        }
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(this.repeat ? "sigma/musicplayer/repeat.png" : "sigma/musicplayer/repeatdis.png"));
        GlStateManager.color(1.0f, 1.0f, 1.0f, opacity);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        AbstractGui.drawModalRectWithCustomSizedTexture((int)(x - 16.0 + 20.0 + 140.0) - 21, (int)(y - 16.0 + 280.5), 0.0, 0.0, 15.199999809265137, 12.0, 15.199999809265137, 12.0);
        if (!enableBlend) {
            GL11.glDisable(3042);
        }
        if (!disableAlpha) {
            GL11.glEnable(3008);
        }
        GL11.glPopMatrix();
        }

    public void releasePanel(final int mouseX, final int mouseY) {
            if (this.dragging) {
                final ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());
                if (this.x > sr.getScaledWidth() - 300) {
                    this.x = sr.getScaledWidth() - 20;
                    this.y = (int)(sr.getScaledHeight() / 2.0f - 150.0f);
                    this.searchBar.x = (int)(this.x + 121 + 15.5f);
                    this.searchBar.y = (int)(this.y - 9 + 20 - 1.5f);
                }
            }
        this.dragging = false;
        }

    public boolean clickPanel(final int mouseX, final int mouseY, final int b) {
            if (b == 0) {
                if (ClickUtils.isClickable(this.x + 120 + 137.5f - 100.0f, this.y - 4.0f + 5.0f + 15.0f - 8.0f, this.x + 120 + 137.5f + 100.0f, this.y - 4.0f + 5.0f + 15.0f + 8.0f, mouseX, mouseY) && this.hide) {
                    this.hide = false;
                    return true;
                }
                final List<String> channels = Arrays.asList("Trap Nation", "Child Nation", "VEVO", "NCS", "CloudKid", "Trap City", "MrSuicideSheep", "Rap Nation");
                int channelCount = 0;
                for (final String c : channels) {
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    if (ClickUtils.isClickableWithRect(this.x - 4.75f + 0.5f + 62.5f - 0.5f - JelloFontUtil.jelloFont13.getStringWidth(c) / 2.0, this.y - 3.5f + 46.5f + 0.5f + 20 * channelCount, JelloFontUtil.jelloFont13.getStringWidth(c) / 2.0, JelloFontUtil.jelloFont13.getHeight(), mouseX, mouseY)) {
                        this.currentChannel = c;
                        this.lastSearch = this.currentChannel;
                        this.hide = true;
                        this.reloaded();
                        return true;
                    }
                    ++channelCount;
                }
                int count = 0;
                int xOffset = 90;
                int calcY = this.offsetY;
                if (ClickUtils.isClickable((float)this.x, (float)(this.y + 30), (float)(this.x + 500), (float)(this.y + 290 - 50), mouseX, mouseY)) {
                    for (final Music file : JelloMusicPlayer.musics) {
                        if (this.y - 63 > (float)(this.y + calcY + 5)) {
                            if (count < 2) {
                                ++count;
                            }
                        else {
                            calcY += 100;
                            count = 0;
                        }
                        }
                    else if (this.y + 270.0f < this.y + calcY + 5) {
                        if (count < 2) {
                            ++count;
                        }
                        else {
                            calcY += 100;
                            count = 0;
                        }
                    }
                    else {
                        if (ClickUtils.isClickable((float)(this.x + 40 + xOffset), (float)(this.y + calcY), this.x + 40 + xOffset + 77.0f, this.y + calcY + 77.0f, mouseX, mouseY)) {
                            YoutubeVideoHelper.playMusicLink(this.currentFile = file);
                            return true;
                        }
                        if (count < 2) {
                            ++count;
                            xOffset += (int)86.5f;
                        }
                        else {
                            xOffset = 90;
                            calcY += 100;
                            count = 0;
                        }
                    }
                    }
                }
                if (ClickUtils.isClickableWithRect(this.x - 16 + 20 + 140, (int)(this.y - 16 + 280.5f), 15.199999809265137, 12.0, mouseX, mouseY)) {
                    this.repeat = !this.repeat;
                    return true;
                }
                if (this.mp3.isNotNull) {
                    if (this.mp3.isOnPlaying) {
                        if (ClickUtils.isClickable(this.x - 16 + 265, this.y + 262, this.x - 16 + 265 + 17.5f, this.y + 262 + 19.0f, mouseX, mouseY)) {
                            this.mp3.stop();
                            return true;
                        }
                    }
                else if (ClickUtils.isClickable(this.x - 16 + 265, this.y + 262, this.x - 16 + 265 + 17.5f, this.y + 262 + 19.0f, mouseX, mouseY)) {
                    this.mp3.play();
                    return true;
                }
                }
                if (ClickUtils.isClickable(this.x, this.y, this.x + 125, this.y + 35, mouseX, mouseY)) {
                    this.startX = mouseX;
                    this.startY = mouseY;
                    this.lastX = (float)this.x;
                    this.lastY = (float)this.y;
                    return this.dragging = true;
                }
            }
        if (this.searchBar != null) {
            this.searchBar.mouseClicked(mouseX, mouseY, b);
        }
        return false;
        }

    public void drawPlayer(final int mouseX, final int mouseY, float opacity) {
            final ScaledResolution sr = new ScaledResolution(Minecraft.getInstance());
            if (this.searchBar != null && !this.lastSearch.equals(this.searchBar.getText()) && !this.hide) {
                this.lastSearch = this.searchBar.getText();
                this.antiRepeat.reset();
            }
        else if (this.antiRepeat.hasTimeElapsed(2000L) && !this.currentChannel.equals(this.lastSearch)) {
            this.currentChannel = this.lastSearch;
            this.reloaded();
        }
        if (this.searchBar != null) {
            this.searchBar.setVisible(!this.hide);
        }
        this.doMouseAnim();
        if (this.dragging) {
            this.x = (int)(this.lastX + mouseX - this.startX);
            this.y = (int)(this.lastY + mouseY - this.startY);
            this.searchBar.x = (int)(this.x + 121 + 15.5f);
            this.searchBar.y = (int)(this.y - 9 + 20 - 1.5f);
        }
        if (this.x > sr.getScaledWidth() - 300) {
            opacity = 0.5f;
        }
        final float sidewarsY = this.y - 16 + 323.0f - 16.0f + 4.0f + 2.0f;
        if (!this.isMouseDown || ClickUtils.isClickable((float)(this.x + 110), sidewarsY - 4.5, this.x + 110 + (this.x + 423.0f - 16.0f - 10.0f - (this.x + 110)), sidewarsY - 1.5, mouseX, mouseY)) {}
        RenderUtils.drawTexture(this.x - 16, this.y - 16, 423.0, 323.0, "jellomusicbackground", opacity);
        RenderUtils.drawTexture(this.x - 16, this.y - 16, 423.0, 323.0, "musicshadow", opacity);
        RenderUtils.drawTexture(this.x - 16 + 9 + 125.0f, this.y - 16 + 280.5f + 28.5f - 2.5f, 277.5, 5.0, "durationshadow", opacity);
        if (this.currentFile != null && this.currentFile.textureImage != null && this.currentFile.textureImage.pixels != null) {
            GlStateManager.resetColor();
            StencilUtil.initStencilToWrite();
            RenderUtils.drawRect((float)(this.x - 10 + 5), sidewarsY - 50.0f, this.x - 10 + 5 + (this.x + 423.0f - 16.0f - 10.0f) - (this.x - 10 + 5), sidewarsY - 0.5f, -1);
            StencilUtil.readStencilBuffer(1);
            GlStateManager.resetColor();
            GlStateManager.color(1.0f, 1.0f, 1.0f, opacity * 1.0f);
            final float radius = 0.0f;
            this.currentFile.textureImage.rectTextureMasked(this.x - 10 + 5 - radius * 1.5f, sidewarsY - 50.0f - 60.0f - radius * 1.5f, this.x + 423.0f - 16.0f - 10.0f - (this.x - 10 + 5) + radius * 3.0f, sidewarsY - 0.5f - (sidewarsY - 50.0f - 40.0f) * 3.0f + radius * 3.0f, 0.0f, 0.0f);
            StencilUtil.uninitStencilBuffer();
            RenderUtils.drawRect((float)(this.x - 10 + 5), sidewarsY - 50.0f, this.x - 10 + 5 + (this.x + 423.0f - 16.0f - 10.0f) - (this.x - 10 + 5), sidewarsY - 0.5f, new Color(0, 0, 0, (int)(100.0f * opacity)).getRGB());
        }
        if (this.currentFile != null && this.currentFile.textureImage != null) {
            GlStateManager.resetColor();
            GlStateManager.color(1.0f, 1.0f, 1.0f, opacity);
            this.currentFile.textureImage.rectTextureMasked2(this.x + 45.5f - 11.5f - 4.5f, this.y + 226.5f - 11.5f - 4.5f - 1.0f, 58.0f, 58.0f, 87.0f, 77.14f, 14.5f, 9.57f);
        }
        RenderUtils.drawTexture((int)(this.x - 16 + 207.5f), (int)(this.y + 264.5f), 22.5, 15.0, "rewind", opacity);
        if (this.mp3.isOnPlaying && this.mp3.isNotNull) {
            RenderUtils.drawTexture(this.x - 16 + 265, this.y + 262, 17.5, 19.0, "pause", opacity);
        }
        else {
            RenderUtils.drawTexture(this.x - 16 + 265, (int)(this.y + 262.5f), 18.5, 18.0, "play", opacity);
        }
        RenderUtils.drawTexture((int)(this.x - 16 + 322.0f), (int)(this.y - 16 + 280.5f), 22.5, 15.0, "fastforward", opacity);
        RenderUtils.drawTexture(this.x - 16 + 20, (int)(this.y - 16 + 280.5f - 35.0f), 11.40000057220459, 12.0, "musicplayer/wave", opacity);
        this.drawRepeat(this.x, this.y, opacity);
        this.doVolumeControl(mouseX, mouseY, opacity);
        this.drawMusics(mouseX, mouseY, opacity);
        if (this.hide) {
            JelloFontUtil.jelloFontBold25.drawCenteredString(this.currentChannel, this.x + 120 + 137.5f, this.y - 4.0f + 5.0f + 15.0f, new Color(255, 255, 255, 255).getRGB());
        }
        RenderUtils.resetColor();
        final List<String> channels = Arrays.asList("Trap Nation", "Child Nation", "VEVO", "NCS", "CloudKid", "Trap City", "MrSuicideSheep", "Rap Nation");
        int channelCount = 0;
        for (final String c : channels) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            JelloFontUtil.jelloFont13.drawCenteredString(c, this.x - 4.75f + 0.5f + 62.5f - 0.5f, this.y - 3.5f + 46.5f + 0.5f + 20 * channelCount, ColorUtils.reAlpha(Color.white, opacity * 0.8f).getRGB());
            JelloFontUtil.jelloFont13.drawCenteredString(c, this.x - 4.75f + 0.5f + 62.5f - 0.5f, this.y - 3.5f + 46.5f + 0.5f + 20 * channelCount, ColorUtils.reAlpha(Color.white, opacity * 0.8f).getRGB());
            ++channelCount;
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, opacity);
        String time = "0:00";
        String maxTime = "0:00";
        if (JelloMusicPlayer.currentSongLength != 0 && this.currentFile != null && Player.player != null) {
            this.currentPosition = Player.player.getPosition();
            final int MINUTES_IN_AN_HOUR = 60;
            final int SECONDS_IN_A_MINUTE = 60;
            final int seconds = JelloMusicPlayer.currentSongLength % 60;
            final int totalMinutes = JelloMusicPlayer.currentSongLength / 60;
            final int minutes = totalMinutes % 60;
            final int hours = totalMinutes / 60;
            final int progress = 20;
            if (JelloMusicPlayer.currentSongLength >= 3600000) {
                final String format = "%2d:%02d:%02d";
                maxTime = String.format(format, TimeUnit.MILLISECONDS.toHours(JelloMusicPlayer.currentSongLength), TimeUnit.MILLISECONDS.toMinutes(JelloMusicPlayer.currentSongLength) % TimeUnit.HOURS.toMinutes(1L), TimeUnit.MILLISECONDS.toSeconds(JelloMusicPlayer.currentSongLength) % TimeUnit.MINUTES.toSeconds(1L));
            }
            else if (JelloMusicPlayer.currentSongLength >= 60000) {
                final String format = "%2d:%02d";
                maxTime = String.format(format, TimeUnit.MILLISECONDS.toMinutes(JelloMusicPlayer.currentSongLength) % TimeUnit.HOURS.toMinutes(1L), TimeUnit.MILLISECONDS.toSeconds(JelloMusicPlayer.currentSongLength) % TimeUnit.MINUTES.toSeconds(1L));
            }
            else if (JelloMusicPlayer.currentSongLength >= 1000) {
                final String format = "0:%02d";
                maxTime = String.format(format, TimeUnit.MILLISECONDS.toSeconds(this.currentPosition) % TimeUnit.MINUTES.toSeconds(1L));
            }
            if (this.currentPosition >= 3600000L) {
                final String format = "%2d:%02d:%02d";
                time = String.format(format, TimeUnit.MILLISECONDS.toHours(this.currentPosition), TimeUnit.MILLISECONDS.toMinutes(this.currentPosition) % TimeUnit.HOURS.toMinutes(1L), TimeUnit.MILLISECONDS.toSeconds(this.currentPosition) % TimeUnit.MINUTES.toSeconds(1L));
            }
            else if (this.currentPosition >= 60000L) {
                final String format = "%2d:%02d";
                time = String.format(format, TimeUnit.MILLISECONDS.toMinutes(this.currentPosition) % TimeUnit.HOURS.toMinutes(1L), TimeUnit.MILLISECONDS.toSeconds(this.currentPosition) % TimeUnit.MINUTES.toSeconds(1L));
            }
            else if (this.currentPosition >= 1000L) {
                final String format = "0:%02d";
                time = String.format(format, TimeUnit.MILLISECONDS.toSeconds(this.currentPosition) % TimeUnit.MINUTES.toSeconds(1L));
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, opacity);
            RenderUtils.drawRect((float)(this.x + 110), sidewarsY - 4.5, this.x + 110 + (this.x + 423.0f - 16.0f - 10.0f - (this.x + 110)) * (this.currentPosition / (float)JelloMusicPlayer.currentSongLength), sidewarsY - 1.5, new Color(255, 255, 255, (int)(100.0f * opacity)).getRGB());
            GlStateManager.resetColor();
            JelloFontUtil.jelloFont14.drawCenteredString(this.currentFile.name, (float)(this.x + 10 + 100 - 85 + 30 + 7), (float)(this.y + 323 - 10 - 16 - 16 - 5 - 8 - 8 + 15 + 10 - 10 + 3 - 6), new Color(255, 255, 255, (int)(255.0f * opacity)).getRGB());
            GlStateManager.resetColor();
            JelloFontUtil.jelloFont14.drawCenteredString(this.currentFile.aliasName, (float)(this.x + 10 + 100 - 85 + 30 + 7), (float)(this.y + 323 - 10 - 16 - 16 - 5 - 8 - 8 + 15 + 10 - 10 + 5), new Color(255, 255, 255, (int)(150.0f * opacity)).getRGB());
        }
        else {
            JelloFontUtil.jelloFont14.drawCenteredString("Jello Music", (float)(this.x + 10 + 100 - 85 + 30 + 7), (float)(this.y + 323 - 10 - 16 - 16 - 5 - 8 - 8 + 15 + 10 - 10 + 3), new Color(255, 255, 255, (int)(255.0f * opacity)).getRGB());
        }
        JelloFontUtil.jelloFont14.drawString(time, (float)(this.x + 10 + 100 + 13), (float)(this.y + 323 - 10 - 16 - 16 - 5 + 5), new Color(255, 255, 255, (int)(255.0f * opacity)).getRGB());
        JelloFontUtil.jelloFont14.drawString(maxTime, (float)(this.x + 313 + 60 - JelloFontUtil.jelloFont18.getStringWidth(maxTime) - 1.0 + 10.0), (float)(this.y + 323 - 10 - 16 - 16 - 5 + 5), new Color(255, 255, 255, (int)(255.0f * opacity)).getRGB());
        if (this.searchBar != null && opacity >= 0.9) {
            this.searchBar.drawTextBox();
        }
        }

    static {
            JelloMusicPlayer.volumeControl = 0.5f;
        }
    }
