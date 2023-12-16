// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.spotify;

import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import moonsense.ui.screen.GuiSpotifyOverlay;
import se.michaelthelin.spotify.model_objects.specification.Image;
import net.minecraft.client.gui.Gui;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonParser;
import moonsense.MoonsenseClient;
import moonsense.features.modules.SpotifyIntegrationModule;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import java.io.IOException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import org.apache.hc.core5.http.ParseException;
import moonsense.integrations.spotify.SpotifyManager;
import java.util.function.Consumer;
import moonsense.ui.screen.AbstractGuiScreen;
import org.jetbrains.annotations.NotNull;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;
import moonsense.ui.elements.Element;

public class ElementFavoriteSongEntry extends Element
{
    private String id;
    private Track track;
    private AlbumSimplified album;
    
    public ElementFavoriteSongEntry(final int x, final int y, @NotNull final String id, final AbstractGuiScreen parent) {
        super(x, y, 96, 16, true, null, parent);
        this.id = id;
        new Thread("Setup Song Entry Metadata: " + this.id) {
            @Override
            public void run() {
                boolean accomplished1 = false;
                boolean accomplished2 = false;
                while (!accomplished1 && !accomplished2) {
                    Label_0055: {
                        try {
                            if (!accomplished1) {
                                ElementFavoriteSongEntry.access$1(ElementFavoriteSongEntry.this, SpotifyManager.getSpotifyAPI().getTrack(ElementFavoriteSongEntry.this.id).build().execute());
                            }
                        }
                        catch (ParseException ex) {}
                        catch (SpotifyWebApiException ex2) {}
                        catch (IOException ex3) {
                            break Label_0055;
                        }
                        finally {
                            accomplished1 = true;
                        }
                        accomplished1 = true;
                        try {
                            if (!accomplished2) {
                                ElementFavoriteSongEntry.access$3(ElementFavoriteSongEntry.this, ElementFavoriteSongEntry.this.track.getAlbum());
                            }
                        }
                        catch (NullPointerException ex4) {
                            continue;
                        }
                        finally {
                            accomplished2 = true;
                        }
                    }
                    accomplished2 = true;
                }
            }
        }.start();
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.hovered) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            if (SpotifyIntegrationModule.INSTANCE.proceedToNextFavorite.getBoolean()) {
                String builder = "";
                builder = String.valueOf(builder) + "[";
                for (int i = 0; i < MoonsenseClient.INSTANCE.getSongManager().size(); ++i) {
                    if (i >= MoonsenseClient.INSTANCE.getSongManager().indexOf(this.id)) {
                        builder = String.valueOf(builder) + "\"spotify:track:" + MoonsenseClient.INSTANCE.getSongManager().get(i) + "\",";
                    }
                }
                builder = builder.substring(0, builder.length() - 1);
                builder = String.valueOf(builder) + "]";
                try {
                    SpotifyManager.getSpotifyAPI().startResumeUsersPlayback().uris(JsonParser.parseString(builder).getAsJsonArray()).build().execute();
                }
                catch (ParseException | JsonSyntaxException | SpotifyWebApiException | IOException ex3) {
                    final Exception ex;
                    final Exception e = ex;
                    e.printStackTrace();
                }
            }
            else {
                try {
                    SpotifyManager.getSpotifyAPI().startResumeUsersPlayback().uris(JsonParser.parseString("[\"spotify:track:" + this.id + "\"]").getAsJsonArray()).build().execute();
                }
                catch (ParseException | JsonSyntaxException | SpotifyWebApiException | IOException ex4) {
                    final Exception ex2;
                    final Exception e2 = ex2;
                    e2.printStackTrace();
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        if (this.album == null) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("streamlined/icons/spotify.png"));
            Gui.drawModalRectWithCustomSizedTexture(this.getX() + 1, this.getY() + 1, 0.0f, 0.0f, 14, 14, 14.0f, 14.0f);
        }
        else {
            final Image[] imgArr = this.album.getImages();
            if (imgArr != null && imgArr.length > 0) {
                final Image img = imgArr[0];
                if (img != null) {
                    this.mc.getTextureManager().bindTexture(SpotifyManager.getAlbumImageLocation(img.getUrl()));
                    Gui.drawModalRectWithCustomSizedTexture(this.getX() + 1, this.getY() + 1, 0.0f, 0.0f, 14, 14, 14.0f, 14.0f);
                }
            }
        }
        if (this.track == null) {
            MoonsenseClient.textRenderer.drawString("Waiting...", this.getX() + 16, this.getY() + 2, -1);
        }
        else {
            try {
                final String title = (this.track.getName().length() > 28) ? (String.valueOf(this.track.getName().substring(0, 28)) + "...") : this.track.getName();
                MoonsenseClient.tinyTextRenderer.drawString(title, this.getX() + 16, this.getY() + 2, -1);
            }
            catch (Exception ex) {}
            try {
                String artist = (this.album.getArtists()[0].getName().length() > 22) ? (String.valueOf(this.album.getArtists()[0].getName().substring(0, 22)) + "...") : this.album.getArtists()[0].getName();
                if (this.album.getArtists().length > 1) {
                    artist = String.valueOf(artist) + " +" + (this.album.getArtists().length - 1) + " more";
                }
                MoonsenseClient.tinyTextRenderer.drawString((artist.length() > 30) ? (String.valueOf(artist.substring(0, 30)) + "...") : artist, this.getX() + 16, this.getY() + 8, -1);
            }
            catch (Exception ex2) {}
        }
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
        int greenAdd;
        try {
            greenAdd = ((this.id == ((GuiSpotifyOverlay)this.parent).getCurrentlyPlaying().getItem().getId()) ? 100 : 0);
        }
        catch (Exception e) {
            greenAdd = 100;
        }
        GuiUtils.drawRoundedRect((float)this.getX(), (float)this.getY(), (float)(this.getX() + this.width), (float)(this.getY() + this.height), 0.0f, new Color(42, 50 + greenAdd, 55, 100 + (this.hovered ? 50 : 0)).getRGB());
        GuiUtils.drawRoundedOutline(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0.0f, 2.0f, new Color(105, 116 + greenAdd, 122, 100 + (this.hovered ? 50 : 0)).getRGB());
    }
    
    private boolean isSongFavorited() {
        return MoonsenseClient.INSTANCE.getSongManager().contains(this.id);
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public AlbumSimplified getAlbum() {
        return this.album;
    }
    
    public void setAlbum(final AlbumSimplified album) {
        this.album = album;
    }
    
    public Track getTrack() {
        return this.track;
    }
    
    public void setTrack(final Track track) {
        this.track = track;
    }
    
    static /* synthetic */ void access$1(final ElementFavoriteSongEntry elementFavoriteSongEntry, final Track track) {
        elementFavoriteSongEntry.track = track;
    }
    
    static /* synthetic */ void access$3(final ElementFavoriteSongEntry elementFavoriteSongEntry, final AlbumSimplified album) {
        elementFavoriteSongEntry.album = album;
    }
}
