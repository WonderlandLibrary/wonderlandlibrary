// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.screen;

import net.minecraft.client.Minecraft;
import se.michaelthelin.spotify.model_objects.specification.Image;
import java.io.IOException;
import moonsense.features.modules.SpotifyIntegrationModule;
import moonsense.utils.KeyBinding;
import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import moonsense.MoonsenseClient;
import moonsense.ui.utils.GuiUtils;
import moonsense.features.ThemeSettings;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import moonsense.ui.elements.spotify.ElementFavoriteSongs;
import moonsense.ui.elements.spotify.ElementFavoriteSong;
import moonsense.ui.elements.Element;
import java.util.function.BiConsumer;
import moonsense.integrations.spotify.SpotifyManager;
import moonsense.utils.MathUtil;
import moonsense.ui.elements.spotify.SpotifySeeker;
import moonsense.ui.elements.button.GuiCustomButton;
import moonsense.ui.elements.button.GuiButtonIcon;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;

public class GuiSpotifyOverlay extends AbstractGuiScreen
{
    private boolean drawingPlayButton;
    private boolean drawingPauseButton;
    private boolean first;
    private boolean playing;
    private long lastRequestMS;
    private CurrentlyPlaying currentlyPlaying;
    private AlbumSimplified currentlyPlayingAlbum;
    
    public GuiSpotifyOverlay() {
        this.drawingPlayButton = true;
        this.drawingPauseButton = false;
        this.playing = false;
        this.lastRequestMS = 0L;
        this.playing = false;
        this.first = true;
        this.currentlyPlaying = null;
        this.lastRequestMS = System.currentTimeMillis();
    }
    
    @Override
    public void initGui() {
        this.elements.clear();
        this.buttonList.clear();
        this.buttonList.add(new GuiButtonIcon(0, this.width / 2 - 27, this.height / 2 + 37, 15, 15, "spotify/back.png", "", true));
        this.buttonList.add(new GuiButtonIcon(1, this.width / 2 - 7, this.height / 2 + 37, 15, 15, "spotify/play.png", "", true));
        this.buttonList.add(new GuiButtonIcon(2, this.width / 2 + 13, this.height / 2 + 37, 15, 15, "spotify/forward.png", "", true));
        this.buttonList.add(new GuiCustomButton(4, this.width / 2 - 75, this.height / 2 + 72 + 5, 73, 15, "Refresh", true));
        this.buttonList.add(new GuiCustomButton(5, this.width / 2 + 2, this.height / 2 + 72 + 5, 73, 15, "Logout", true));
        this.buttonList.add(new GuiCustomButton(3, this.width / 2 - 75, this.height / 2 + 72 + 22, 150, 15, "Clear Image Cache", true));
        this.elements.add(new SpotifySeeker(this.width / 2 - 85, this.height / 2 + 55, 170, 5, 0.0f, 0.0f, 100.0f, 0.0f, null, (element, sliderValue) -> {
            SpotifyManager.seekTo((int)element.getRoundedValue(MathUtil.denormalizeValue(element.sliderValue, element.min, element.max, element.step)));
            this.currentlyPlaying = SpotifyManager.getCurrentlyPlaying();
            this.currentlyPlayingAlbum = SpotifyManager.getCurrentlyPlayingAlbum();
            return;
        }, this));
        this.elements.add(new ElementFavoriteSong(this.width / 2 - 95 + 2, this.height / 2 - 72 + 2, this));
        this.elements.add(new ElementFavoriteSongs(this.width / 2 + 95 - 14, this.height / 2 - 72 + 2, this));
        this.buttonList.add(new GuiButtonIcon(6, this.width / 2 + 95 + 2, this.height / 2 + 72 + 2, 15, 15, "spotify/back.png", "", true));
        this.buttonList.add(new GuiButtonIcon(7, this.width / 2 + 95 + 100 - 15 - 2, this.height / 2 + 72 + 2, 15, 15, "spotify/forward.png", "", true));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Gui.drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 50).getRGB());
        ++this.lastRequestMS;
        GuiUtils.drawRoundedRect((float)(this.width / 2 - 95), (float)(this.height / 2 - 72), (float)(this.width / 2 + 95), (float)(this.height / 2 + 72), 3.0f, ThemeSettings.INSTANCE.uiBackgroundMain.getColor());
        GuiUtils.drawRoundedOutline(this.width / 2 - 95, this.height / 2 - 72, this.width / 2 + 95, this.height / 2 + 72, 3.0f, 2.0f, ThemeSettings.INSTANCE.uiAccent.getColor());
        if (!MoonsenseClient.INSTANCE.isSpotifyLoggedIn()) {
            MoonsenseClient.titleRenderer2.drawCenteredString("You are not", (float)(this.width / 2), (float)(this.height / 2 - 60), -1);
            MoonsenseClient.titleRenderer2.drawCenteredString("logged in!", (float)(this.width / 2), (float)(this.height / 2 - 50), -1);
            MoonsenseClient.textRenderer.drawCenteredString("Click the button below", (float)(this.width / 2), (float)(this.height / 2 - 30), -1);
            MoonsenseClient.textRenderer.drawCenteredString("to authorize your", (float)(this.width / 2), (float)(this.height / 2 - 20), -1);
            MoonsenseClient.textRenderer.drawCenteredString("Spotify Account", (float)(this.width / 2), (float)(this.height / 2 - 10), -1);
            GuiUtils.drawRoundedRect((float)(this.width / 2 - 70), (float)(this.height / 2 + 30), (float)(this.width / 2 + 70), (float)(this.height / 2 + 60), 3.0f, new Color(150, 150, 150, 160).getRGB());
            GuiUtils.drawRoundedOutline(this.width / 2 - 70, this.height / 2 + 30, this.width / 2 + 70, this.height / 2 + 60, 3.0f, 2.0f, new Color(160, 160, 160, 200).getRGB());
            this.mc.getTextureManager().bindTexture(new ResourceLocation("streamlined/icons/spotify.png"));
            Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - 67, this.height / 2 + 30 + 3, 0.0f, 0.0f, 24, 24, 24.0f, 24.0f);
            MoonsenseClient.watermarkRenderer1.drawCenteredString("Login", (float)(this.width / 2 + 10), (float)(this.height / 2 + 33), -1);
        }
        else {
            if (this.first) {
                this.first = false;
                new Thread("Spotify Get Current Track") {
                    @Override
                    public void run() {
                        while (GuiSpotifyOverlay.this.mc.currentScreen == GuiSpotifyOverlay.this) {
                            GuiSpotifyOverlay.access$1(GuiSpotifyOverlay.this, SpotifyManager.getCurrentlyPlaying());
                            GuiSpotifyOverlay.access$2(GuiSpotifyOverlay.this, SpotifyManager.getCurrentlyPlayingAlbum());
                            GuiSpotifyOverlay.access$3(GuiSpotifyOverlay.this, System.currentTimeMillis());
                            try {
                                Thread.sleep(3000L);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
            if (this.currentlyPlaying == null || this.currentlyPlayingAlbum == null) {
                MoonsenseClient.titleRenderer2.drawCenteredString("Waiting for Spotify...", (float)(this.width / 2), (float)(this.height / 2 - 50), -1);
                MoonsenseClient.textRenderer.drawCenteredString("We are fetching your currently playing", (float)(this.width / 2), (float)(this.height / 2 - 25), -1);
                MoonsenseClient.textRenderer.drawCenteredString("track from Spotify...", (float)(this.width / 2), (float)(this.height / 2 - 15), -1);
                MoonsenseClient.textRenderer.drawCenteredString("You might want to check to see if you", (float)(this.width / 2), (float)(this.height / 2 - 5), -1);
                MoonsenseClient.textRenderer.drawCenteredString("have Spotify open with a track playing", (float)(this.width / 2), (float)(this.height / 2 + 5), -1);
                MoonsenseClient.textRenderer.drawCenteredString("so Moonsense Client knows you are active!", (float)(this.width / 2), (float)(this.height / 2 + 15), -1);
            }
            else {
                this.drawPlayer(mouseX, mouseY);
                for (final Object b : this.buttonList) {
                    if (((GuiButton)b).id == 6 || ((GuiButton)b).id == 7) {
                        if (!this.elements.get(2).isExtended()) {
                            continue;
                        }
                        ((GuiButton)b).drawButton(this.mc, mouseX, mouseY);
                    }
                    else {
                        ((GuiButton)b).drawButton(this.mc, mouseX, mouseY);
                    }
                }
                for (final Element e : this.elements) {
                    e.render(mouseX, mouseY, partialTicks);
                }
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (MoonsenseClient.INSTANCE.isSpotifyLoggedIn() && this.currentlyPlaying != null && this.currentlyPlayingAlbum != null) {
            if (keyCode == ((KeyBinding)SpotifyIntegrationModule.INSTANCE.startResumeKeybind.getObject()).getKeyCode() && !this.elements.get(2).getSearch().isFocused()) {
                try {
                    if (this.currentlyPlaying.getIs_playing()) {
                        SpotifyManager.pauseCurrentTrack();
                    }
                    else {
                        SpotifyManager.startResumeCurrentTrack();
                    }
                }
                catch (Exception ex) {}
            }
            if (keyCode == ((KeyBinding)SpotifyIntegrationModule.INSTANCE.favoriteSongKeybind.getObject()).getKeyCode()) {
                try {
                    this.elements.get(1).hovered = true;
                    this.elements.get(1).mouseClicked(this.elements.get(1).x + 1, this.elements.get(1).y + 1, 0);
                    this.elements.get(1).hovered = false;
                }
                catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
            if (keyCode == ((KeyBinding)SpotifyIntegrationModule.INSTANCE.favoritesMenuKeybind.getObject()).getKeyCode()) {
                try {
                    this.elements.get(2).toggleVisibility();
                }
                catch (ClassCastException ex2) {}
            }
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (this.mc.entityRenderer.theShaderGroup != null) {
            this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            this.mc.entityRenderer.theShaderGroup = null;
        }
    }
    
    private void drawPlayer(final int mouseX, final int mouseY) {
        this.elements.get(1).setId(this.currentlyPlaying.getItem().getId());
        this.elements.get(1).setAlbum(this.currentlyPlayingAlbum);
        this.elements.get(1).setTrack(this.currentlyPlaying.getItem());
        try {
            final String title = (this.currentlyPlaying.getItem().getName().length() > 36) ? (String.valueOf(this.currentlyPlaying.getItem().getName().substring(0, 36)) + "...") : this.currentlyPlaying.getItem().getName();
            MoonsenseClient.textRenderer.drawCenteredString(title, (float)(this.width / 2), (float)(this.height / 2 - 70), -1);
        }
        catch (Exception ex) {}
        try {
            String artist = (this.currentlyPlayingAlbum.getArtists()[0].getName().length() > 30) ? (String.valueOf(this.currentlyPlayingAlbum.getArtists()[0].getName().substring(0, 30)) + "...") : this.currentlyPlayingAlbum.getArtists()[0].getName();
            if (this.currentlyPlayingAlbum.getArtists().length > 1) {
                artist = String.valueOf(artist) + " and " + (this.currentlyPlayingAlbum.getArtists().length - 1) + " more";
            }
            MoonsenseClient.textRenderer.drawCenteredString((artist.length() > 42) ? (String.valueOf(artist.substring(0, 42)) + "...") : artist, (float)(this.width / 2), (float)(this.height / 2 - 60), new Color(200, 200, 200, 255).getRGB());
        }
        catch (Exception ex2) {}
        if (this.currentlyPlayingAlbum != null) {
            final Image[] imgArr = this.currentlyPlayingAlbum.getImages();
            if (imgArr != null && imgArr.length > 0) {
                final Image img = imgArr[0];
                if (img != null) {
                    this.mc.getTextureManager().bindTexture(SpotifyManager.getAlbumImageLocation(img.getUrl()));
                    Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - 40, this.height / 2 - 47, 0.0f, 0.0f, 80, 80, 80.0f, 80.0f);
                }
            }
        }
        if (this.currentlyPlaying.getIs_playing()) {
            this.buttonList.get(1).setImage("spotify/pause.png");
        }
        else {
            this.buttonList.get(1).setImage("spotify/play.png");
        }
        final SpotifySeeker e = this.elements.get(0);
        if (!e.isDragging()) {
            e.min = 0.0f;
            e.max = this.currentlyPlaying.getItem().getDurationMs();
            e.sliderValue = MathUtil.normalizeValue(this.currentlyPlaying.getProgress_ms(), e.min, e.max, e.step);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (this.loginAreaClicked(mouseX, mouseY) && !MoonsenseClient.INSTANCE.isSpotifyLoggedIn()) {
            SpotifyManager.connectSpotify();
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        if (this.elements.get(0).hovered) {
            this.elements.get(0).mouseDragged(mouseX, mouseY);
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        if (this.elements.get(0).isDragging()) {
            this.elements.get(0).mouseReleased(mouseX, mouseY, state);
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            try {
                if (this.currentlyPlaying.getProgress_ms() < 2000) {
                    SpotifyManager.skipToPreviousTrack();
                }
                else {
                    SpotifyManager.seekTo(0);
                }
            }
            catch (NullPointerException ex) {}
        }
        if (button.id == 1) {
            if (this.currentlyPlaying == null) {
                this.buttonList.get(1).setImage("spotify/pause.png");
                SpotifyManager.startResumeCurrentTrack();
            }
            else if (this.currentlyPlaying.getIs_playing()) {
                this.buttonList.get(1).setImage("spotify/play.png");
                SpotifyManager.pauseCurrentTrack();
            }
            else {
                this.buttonList.get(1).setImage("spotify/pause.png");
                SpotifyManager.startResumeCurrentTrack();
            }
        }
        if (button.id == 2) {
            SpotifyManager.skipToNextTrack();
        }
        if (button.id == 3) {
            SpotifyManager.albumImages.clear();
            SpotifyManager.userImages.clear();
        }
        if (button.id == 4) {
            this.buttonList.get(1).setImage("spotify/play.png");
            if (SpotifyManager.getCurrentlyPlaying() != null && SpotifyManager.getCurrentlyPlaying().getIs_playing()) {
                SpotifyManager.pauseCurrentTrack();
            }
            SpotifyManager.setLogged(false);
            MoonsenseClient.INSTANCE.setSpotifyLoggedIn(false);
            SpotifyManager.connectSpotify();
        }
        if (button.id == 5) {
            if (SpotifyManager.getCurrentlyPlaying() != null && SpotifyManager.getCurrentlyPlaying().getIs_playing()) {
                SpotifyManager.pauseCurrentTrack();
            }
            SpotifyManager.setLogged(false);
            MoonsenseClient.INSTANCE.setSpotifyLoggedIn(false);
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    private boolean loginAreaClicked(final int mouseX, final int mouseY) {
        return mouseX > this.width / 2 - 70 && mouseY > this.height / 2 + 30 && mouseX < this.width / 2 + 70 && mouseY < this.height / 2 + 60;
    }
    
    private boolean playButtonClicked(final int mouseX, final int mouseY) {
        return this.drawingPlayButton && (mouseX > this.width - 140 + 70 - 9 && mouseY > this.height - 40 && mouseX < this.width - 140 + 70 - 7 + 12 && mouseY < this.height - 40 + 15);
    }
    
    public CurrentlyPlaying getCurrentlyPlaying() {
        return this.currentlyPlaying;
    }
    
    public AlbumSimplified getCurrentlyPlayingAlbum() {
        return this.currentlyPlayingAlbum;
    }
    
    static /* synthetic */ void access$1(final GuiSpotifyOverlay guiSpotifyOverlay, final CurrentlyPlaying currentlyPlaying) {
        guiSpotifyOverlay.currentlyPlaying = currentlyPlaying;
    }
    
    static /* synthetic */ void access$2(final GuiSpotifyOverlay guiSpotifyOverlay, final AlbumSimplified currentlyPlayingAlbum) {
        guiSpotifyOverlay.currentlyPlayingAlbum = currentlyPlayingAlbum;
    }
    
    static /* synthetic */ void access$3(final GuiSpotifyOverlay guiSpotifyOverlay, final long lastRequestMS) {
        guiSpotifyOverlay.lastRequestMS = lastRequestMS;
    }
}
