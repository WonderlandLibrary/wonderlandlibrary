// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.render;

import dev.tenacity.utils.misc.IOUtils;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import java.io.FileNotFoundException;
import java.io.Reader;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.JsonElement;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import com.google.gson.JsonObject;
import dev.tenacity.utils.font.CustomFont;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import net.minecraft.client.renderer.texture.ITextureObject;
import java.io.File;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.IImageBuffer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import java.util.concurrent.TimeUnit;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.utils.render.RenderUtil;
import dev.tenacity.utils.render.RoundedUtil;
import dev.tenacity.utils.render.ColorUtil;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.utils.animations.impl.DecelerateAnimation;
import dev.tenacity.utils.animations.Direction;
import dev.tenacity.Tenacity;
import dev.tenacity.module.Category;
import java.awt.Color;
import net.minecraft.util.ResourceLocation;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import dev.tenacity.utils.spotify.SpotifyAPI;
import java.util.HashMap;
import dev.tenacity.utils.animations.Animation;
import dev.tenacity.utils.objects.Dragging;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.settings.impl.StringSetting;
import dev.tenacity.module.Module;

public class SpotifyMod extends Module
{
    private final StringSetting clientID;
    private final ModeSetting backgroundColor;
    private final ModeSetting progressBarColor;
    private final Dragging drag;
    public final float height = 50.0f;
    public final float albumCoverSize = 50.0f;
    private final float playerWidth = 135.0f;
    private final float width = 185.0f;
    private final Animation scrollTrack;
    private final Animation scrollArtist;
    public Animation playAnimation;
    public String[] buttons;
    public HashMap<String, Animation> buttonAnimations;
    public SpotifyAPI api;
    private CurrentlyPlayingContext currentPlayingContext;
    private Track currentTrack;
    public boolean playingMusic;
    public boolean hoveringPause;
    private boolean downloadedCover;
    private ResourceLocation currentAlbumCover;
    private Color imageColor;
    private final Color greyColor;
    private final Color progressBackground;
    private final Color shuffleColor;
    private final Color hoveredColor;
    private final Color circleColor;
    
    public SpotifyMod() {
        super("Spotify", "Spotify", Category.RENDER, "UI for spotify");
        this.clientID = new StringSetting("Client ID");
        this.backgroundColor = new ModeSetting("Background", "Average", new String[] { "Average", "Spotify Grey", "Sync" });
        this.progressBarColor = new ModeSetting("Progress Bar", "Green", new String[] { "Average", "Green", "White" });
        this.drag = Tenacity.INSTANCE.createDrag(this, "spotify", 5.0f, 150.0f);
        this.scrollTrack = new DecelerateAnimation(10000, 1.0, Direction.BACKWARDS);
        this.scrollArtist = new DecelerateAnimation(10000, 1.0, Direction.BACKWARDS);
        this.playAnimation = new DecelerateAnimation(250, 1.0);
        this.buttons = new String[] { "h", "k", "l" };
        this.imageColor = Color.WHITE;
        this.greyColor = new Color(30, 30, 30);
        this.progressBackground = new Color(45, 45, 45);
        this.shuffleColor = new Color(50, 255, 100);
        this.hoveredColor = new Color(195, 195, 195);
        this.circleColor = new Color(50, 50, 50);
        this.addSettings(this.clientID, this.backgroundColor, this.progressBarColor);
    }
    
    @Override
    public void onShaderEvent(final ShaderEvent event) {
        if (this.api.currentTrack == null || this.api.currentPlayingContext == null) {
            return;
        }
        final float x = this.drag.getX();
        final float y = this.drag.getY();
        if (event.isBloom()) {
            if (event.getBloomOptions().getSetting("Spotify").isEnabled()) {
                Color color2 = ColorUtil.darker(this.imageColor, 0.65f);
                final String mode = this.backgroundColor.getMode();
                switch (mode) {
                    case "Average": {
                        final float[] hsb = Color.RGBtoHSB(this.imageColor.getRed(), this.imageColor.getGreen(), this.imageColor.getBlue(), null);
                        if (hsb[2] < 0.5f) {
                            color2 = ColorUtil.brighter(this.imageColor, 0.65f);
                        }
                        RoundedUtil.drawGradientVertical(x + 35.0f, y, 150.0f, 50.0f, 6.0f, color2, this.imageColor);
                        break;
                    }
                    case "Spotify Grey": {
                        RoundedUtil.drawRound(x + 35.0f, y, 150.0f, 50.0f, 6.0f, this.greyColor);
                        break;
                    }
                    case "Sync": {
                        final Pair<Color, Color> colors = HUDMod.getClientColors();
                        RoundedUtil.drawGradientCornerLR(x + 35.0f, y, 150.0f, 50.0f, 6.0f, colors.getFirst(), colors.getSecond());
                        break;
                    }
                }
                if (this.currentAlbumCover != null && this.downloadedCover) {
                    RenderUtil.resetColor();
                    SpotifyMod.mc.getTextureManager().bindTexture(this.currentAlbumCover);
                    RoundedUtil.drawRoundTextured(x, y, 50.0f, 50.0f, 7.5f, 1.0f);
                }
            }
            else {
                RoundedUtil.drawRound(x, y, 185.0f, 50.0f, 6.0f, Color.BLACK);
            }
        }
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        if (this.api.currentTrack == null || this.api.currentPlayingContext == null) {
            return;
        }
        if (this.currentTrack != this.api.currentTrack || this.currentPlayingContext != this.api.currentPlayingContext) {
            this.currentTrack = this.api.currentTrack;
            this.currentPlayingContext = this.api.currentPlayingContext;
        }
        this.playingMusic = this.currentPlayingContext.getIs_playing();
        final float x = this.drag.getX();
        final float y = this.drag.getY();
        this.drag.setWidth(185.0f);
        this.drag.setHeight(50.0f);
        Color color2 = ColorUtil.darker(this.imageColor, 0.65f);
        final String mode = this.backgroundColor.getMode();
        switch (mode) {
            case "Average": {
                final float[] hsb = Color.RGBtoHSB(this.imageColor.getRed(), this.imageColor.getGreen(), this.imageColor.getBlue(), null);
                if (hsb[2] < 0.5f) {
                    color2 = ColorUtil.brighter(this.imageColor, 0.65f);
                }
                RoundedUtil.drawGradientVertical(x + 35.0f, y, 150.0f, 50.0f, 6.0f, color2, this.imageColor);
                break;
            }
            case "Spotify Grey": {
                RoundedUtil.drawRound(x + 35.0f, y, 150.0f, 50.0f, 6.0f, this.greyColor);
                break;
            }
            case "Sync": {
                final Pair<Color, Color> colors = HUDMod.getClientColors();
                RoundedUtil.drawGradientCornerLR(x + 35.0f, y, 150.0f, 50.0f, 6.0f, colors.getFirst(), colors.getSecond());
                break;
            }
        }
        final int diff = this.currentTrack.getDurationMs() - this.currentPlayingContext.getProgress_ms();
        final long diffSeconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60L;
        final long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60L;
        final String trackRemaining = String.format("-%s:%s", (diffMinutes < 10L) ? ("0" + diffMinutes) : Long.valueOf(diffMinutes), (diffSeconds < 10L) ? ("0" + diffSeconds) : Long.valueOf(diffSeconds));
        final StringBuilder artistsDisplay;
        int artistIndex;
        ArtistSimplified artist;
        final boolean needsToScrollTrack;
        final boolean needsToScrollArtist;
        final float n2;
        final float trackX;
        final float n3;
        final float artistX;
        RenderUtil.scissor(x + 50.0f, y, 135.0, 50.0, () -> {
            artistsDisplay = new StringBuilder();
            for (artistIndex = 0; artistIndex < this.currentTrack.getArtists().length; ++artistIndex) {
                artist = this.currentTrack.getArtists()[artistIndex];
                artistsDisplay.append(artist.getName()).append((artistIndex + 1 == this.currentTrack.getArtists().length) ? Character.valueOf('.') : ", ");
            }
            if (this.scrollTrack.finished(Direction.BACKWARDS)) {
                this.scrollTrack.reset();
            }
            if (this.scrollArtist.finished(Direction.BACKWARDS)) {
                this.scrollArtist.reset();
            }
            needsToScrollTrack = (SpotifyMod.tenacityBoldFont26.getStringWidth(this.currentTrack.getName()) > 135.0f);
            needsToScrollArtist = (SpotifyMod.tenacityFont22.getStringWidth(artistsDisplay.toString()) > 135.0f);
            trackX = (float)(n2 + 50.0f - SpotifyMod.tenacityBoldFont22.getStringWidth(this.currentTrack.getName()) + (SpotifyMod.tenacityBoldFont22.getStringWidth(this.currentTrack.getName()) + 135.0f) * this.scrollTrack.getLinearOutput());
            SpotifyMod.tenacityBoldFont22.drawString(this.currentTrack.getName(), needsToScrollTrack ? trackX : (n2 + 50.0f + 3.0f), n3 + 3.0f, -1);
            artistX = (float)(n2 + 50.0f - SpotifyMod.tenacityFont18.getStringWidth(artistsDisplay.toString()) + (SpotifyMod.tenacityFont18.getStringWidth(artistsDisplay.toString()) + 135.0f) * this.scrollArtist.getLinearOutput());
            SpotifyMod.tenacityFont18.drawString(artistsDisplay.toString(), needsToScrollArtist ? artistX : (n2 + 50.0f + 4.0f), n3 + 17.0f, -1);
            return;
        });
        SpotifyMod.tenacityFont16.drawString(trackRemaining, x + 185.0f - (SpotifyMod.tenacityFont16.getStringWidth(trackRemaining) + 3.0f), y + 50.0f - (SpotifyMod.tenacityFont16.getHeight() + 3), -1);
        final float progressBarWidth = 100.0f;
        final float progressBarHeight = 3.0f;
        final float progress = progressBarWidth * (this.currentPlayingContext.getProgress_ms() / (float)this.currentTrack.getDurationMs());
        final String mode2 = this.progressBarColor.getMode();
        Color progressColor = null;
        switch (mode2) {
            case "Average": {
                progressColor = (this.backgroundColor.is("Average") ? color2 : this.imageColor);
                break;
            }
            case "Green": {
                progressColor = new Color(50, 255, 100);
                break;
            }
            default: {
                progressColor = Color.WHITE;
                break;
            }
        }
        RoundedUtil.drawRound(x + 50.0f + 5.0f, y + 50.0f - (progressBarHeight + 4.5f), progressBarWidth, progressBarHeight, 1.5f, this.progressBackground);
        RoundedUtil.drawRound(x + 50.0f + 5.0f, y + 50.0f - (progressBarHeight + 4.5f), progress, progressBarHeight, 1.5f, progressColor);
        float spacing = 0.0f;
        RenderUtil.resetColor();
        for (final String button : this.buttons) {
            final Color normalColor = (button.equals("l") && this.currentPlayingContext.getShuffle_state()) ? this.shuffleColor : Color.WHITE;
            RenderUtil.resetColor();
            SpotifyMod.iconFont.size(20).drawString(button, x + 50.0f + 6.0f + spacing, y + 50.0f - 19.0f, ColorUtil.interpolateColor(normalColor, this.hoveredColor, this.buttonAnimations.get(button).getOutput().floatValue()));
            spacing += 15.0f;
        }
        if (this.currentAlbumCover != null && this.downloadedCover) {
            SpotifyMod.mc.getTextureManager().bindTexture(this.currentAlbumCover);
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            GL11.glEnable(3042);
            RoundedUtil.drawRoundTextured(x, y, 50.0f, 50.0f, 6.0f, 1.0f);
        }
        if (this.currentAlbumCover == null || !this.currentAlbumCover.getResourcePath().contains(this.currentTrack.getAlbum().getId())) {
            this.downloadedCover = false;
            final ThreadDownloadImageData albumCover = new ThreadDownloadImageData(null, this.currentTrack.getAlbum().getImages()[1].getUrl(), null, new IImageBuffer() {
                @Override
                public BufferedImage parseUserSkin(final BufferedImage image) {
                    SpotifyMod.this.imageColor = ColorUtil.averageColor(image, image.getWidth(), image.getHeight(), 1);
                    SpotifyMod.this.downloadedCover = true;
                    return image;
                }
                
                @Override
                public void skinAvailable() {
                }
            });
            SpotifyMod.mc.getTextureManager().loadTexture(this.currentAlbumCover = new ResourceLocation("spotifyAlbums/" + this.currentTrack.getAlbum().getId()), albumCover);
        }
        this.playAnimation.setDirection((!this.playingMusic || this.hoveringPause) ? Direction.FORWARDS : Direction.BACKWARDS);
        RoundedUtil.drawRound(x + 25.0f - 22.5f, y + 25.0f - 22.5f, 45.0f, 45.0f, 23.0f, ColorUtil.applyOpacity(this.circleColor, (float)(0.47 * this.playAnimation.getOutput().floatValue())));
        final CustomFont iconFont40 = SpotifyMod.iconFont.size(40);
        final String playIcon = this.currentPlayingContext.getIs_playing() ? "j" : "i";
        iconFont40.drawCenteredString(playIcon, x + 25.0f + (playIcon.equals("j") ? 2 : 0), y + 25.0f - iconFont40.getHeight() / 2.0f + 2.0f, ColorUtil.applyOpacity(-1, this.playAnimation.getOutput().floatValue()));
    }
    
    @Override
    public void onEnable() {
        if (SpotifyMod.mc.thePlayer == null) {
            this.toggle();
            return;
        }
        if (this.buttonAnimations == null) {
            this.buttonAnimations = new HashMap<String, Animation>();
            for (final String button : this.buttons) {
                this.buttonAnimations.put(button, new DecelerateAnimation(250, 1.0, Direction.BACKWARDS));
            }
        }
        String clientID = this.clientID.getString();
        if (this.api == null) {
            this.api = new SpotifyAPI();
        }
        if (clientID.equals("")) {
            clientID = this.getClientIDFromJson();
            if (clientID.equals("")) {
                this.toggleSilent();
                return;
            }
        }
        this.api.build(clientID);
        this.setClientID(clientID);
        this.api.startConnection();
        super.onEnable();
    }
    
    public void setClientID(final String clientID) {
        final JsonObject keyObject = new JsonObject();
        keyObject.addProperty("clientID", clientID);
        try {
            final Writer writer = new BufferedWriter(new FileWriter(SpotifyAPI.CLIENT_ID_DIR));
            SpotifyAPI.GSON.toJson((JsonElement)keyObject, (Appendable)writer);
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getClientIDFromJson() {
        try {
            final JsonObject fileContent = JsonParser.parseReader((Reader)new FileReader(SpotifyAPI.CLIENT_ID_DIR)).getAsJsonObject();
            if (fileContent.has("clientID")) {
                return fileContent.get("clientID").getAsString();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.openYoutubeTutorial();
        NotificationManager.post(NotificationType.WARNING, "Error", "No Client ID found");
        return "";
    }
    
    public void openYoutubeTutorial() {
        IOUtils.openLink("https://www.youtube.com/watch?v=3jOR29h1i40");
    }
}
