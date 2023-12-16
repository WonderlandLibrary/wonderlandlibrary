// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.spotify;

import net.minecraft.client.gui.Gui;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import moonsense.event.impl.SCSongFavoritedEvent;
import moonsense.event.impl.SCSongUnfavoritedEvent;
import moonsense.MoonsenseClient;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import java.util.function.Consumer;
import moonsense.ui.screen.AbstractGuiScreen;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import net.minecraft.util.ResourceLocation;
import moonsense.ui.elements.Element;

public class ElementFavoriteSong extends Element
{
    private static final ResourceLocation favoriteIcon;
    private String id;
    private IPlaylistItem track;
    private AlbumSimplified album;
    
    static {
        favoriteIcon = new ResourceLocation("streamlined/icons/favorite.png");
    }
    
    public ElementFavoriteSong(final int x, final int y, final AbstractGuiScreen parent) {
        super(x, y, 12, 12, true, null, parent);
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.hovered) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            if (this.isSongFavorited()) {
                MoonsenseClient.INSTANCE.getSongManager().remove(this.id);
                new SCSongUnfavoritedEvent(this.id, this.track, this.album).call();
            }
            else {
                MoonsenseClient.INSTANCE.getSongManager().add(this.id);
                new SCSongFavoritedEvent(this.id, this.track, this.album).call();
            }
            new Thread("Save Favorite Songs") {
                @Override
                public void run() {
                    MoonsenseClient.INSTANCE.getSongManager().save();
                }
            }.start();
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        if (this.isSongFavorited()) {
            this.mc.getTextureManager().bindTexture(ElementFavoriteSong.favoriteIcon);
            GuiUtils.setGlColor(new Color(255, 255, 0, 50 + (this.hovered ? 50 : 0)).getRGB());
            Gui.drawModalRectWithCustomSizedTexture(this.getX(), this.getY(), 0.0f, 0.0f, this.width, this.height, (float)this.width, (float)this.height);
        }
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
        this.mc.getTextureManager().bindTexture(ElementFavoriteSong.favoriteIcon);
        GuiUtils.setGlColor(new Color(0, 0, 0, 50 + (this.hovered ? 50 : 0)).getRGB());
        Gui.drawModalRectWithCustomSizedTexture(this.getX(), this.getY(), 0.0f, 0.0f, this.width, this.height, (float)this.width, (float)this.height);
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
    
    public IPlaylistItem getTrack() {
        return this.track;
    }
    
    public void setTrack(final IPlaylistItem track) {
        this.track = track;
    }
    
    public AlbumSimplified getAlbum() {
        return this.album;
    }
    
    public void setAlbum(final AlbumSimplified album) {
        this.album = album;
    }
}
