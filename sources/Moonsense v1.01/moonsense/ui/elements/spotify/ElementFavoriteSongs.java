// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.ui.elements.spotify;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import com.google.gson.JsonSyntaxException;
import org.apache.hc.core5.http.ParseException;
import com.google.gson.JsonParser;
import moonsense.integrations.spotify.SpotifyManager;
import se.michaelthelin.spotify.model_objects.specification.Track;
import org.jetbrains.annotations.NotNull;
import moonsense.event.impl.SCSongUnfavoritedEvent;
import moonsense.event.SubscribeEvent;
import moonsense.event.impl.SCSongFavoritedEvent;
import java.io.IOException;
import org.lwjgl.input.Mouse;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.Gui;
import moonsense.ui.utils.GuiUtils;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Set;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import java.util.ArrayList;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.LinkedHashSet;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import java.util.Iterator;
import moonsense.event.EventBus;
import moonsense.MoonsenseClient;
import com.google.common.collect.Lists;
import java.util.function.Consumer;
import moonsense.ui.screen.AbstractGuiScreen;
import java.util.List;
import moonsense.ui.elements.text.ElementTextfield;
import net.minecraft.util.ResourceLocation;
import moonsense.ui.elements.Element;

public class ElementFavoriteSongs extends Element
{
    private static final ResourceLocation menuIcon;
    private static final ResourceLocation closeIcon;
    private ElementTextfield search;
    private List<ElementFavoriteSongEntry> elements;
    private List<ElementFavoriteSongEntry> copy;
    private boolean extended;
    private int scroll;
    
    static {
        menuIcon = new ResourceLocation("streamlined/icons/spotify/menu.png");
        closeIcon = new ResourceLocation("streamlined/icons/spotify/close.png");
    }
    
    public ElementFavoriteSongs(final int x, final int y, final AbstractGuiScreen parent) {
        super(x, y, 12, 12, true, null, parent);
        this.extended = false;
        this.scroll = 0;
        this.search = new ElementTextfield(this.parent.width / 2 + 95 + 2, this.parent.height / 2 - 72 + 2, 96, 14, "Search...", this.parent);
        this.elements = (List<ElementFavoriteSongEntry>)Lists.newArrayList();
        this.copy = (List<ElementFavoriteSongEntry>)Lists.newArrayList();
        int count = 0;
        for (final String s : MoonsenseClient.INSTANCE.getSongManager()) {
            final ElementFavoriteSongEntry e = new ElementFavoriteSongEntry(this.parent.width / 2 + 95 + 2, this.parent.height / 2 - 72 + 2 + 14 + 2 + count * 18, s, this.parent);
            this.elements.add(e);
            this.copy.add(e);
            ++count;
        }
        MoonsenseClient.INSTANCE.getEventManager();
        EventBus.register(this);
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (!this.search.isFocused() && this.search.hovered) {
            this.search.setFocused(true);
        }
        if (this.search.isFocused() && !this.search.hovered) {
            this.search.setFocused(false);
        }
        if (this.hovered && !this.extended) {
            this.extended = true;
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            return super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (this.hovered && this.extended) {
            this.extended = false;
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0f));
            return super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (!this.hovered && this.extended) {
            this.search.mouseClicked(mouseX, mouseY, mouseButton);
            for (final ElementFavoriteSongEntry e : this.elements) {
                final boolean inScissorBounds = mouseY >= this.parent.height / 2 - 72 + 2 + 14 + 2 && mouseY <= this.parent.height / 2 - 72 + 2 + 14 + 2 + 126;
                if (e.hovered && inScissorBounds) {
                    e.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
            return super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if (this.search.isFocused()) {
            this.search.keyTyped(typedChar, keyCode);
            final LinkedHashSet<ElementFavoriteSongEntry> ids = new LinkedHashSet<ElementFavoriteSongEntry>(this.copy);
            final Set<ElementFavoriteSongEntry> toRemove = (Set<ElementFavoriteSongEntry>)Sets.newLinkedHashSet();
            this.elements.clear();
            int counter = 0;
            for (final ElementFavoriteSongEntry e : ids) {
                final ArrayList<ArtistSimplified> artists = new ArrayList<ArtistSimplified>();
                ArtistSimplified[] artists2;
                for (int length = (artists2 = e.getTrack().getArtists()).length, i = 0; i < length; ++i) {
                    final ArtistSimplified a = artists2[i];
                    artists.add(a);
                }
                if (MoonsenseClient.getSearchPattern(this.search.getText()).matcher(e.getTrack().getName()).find() || artists.stream().anyMatch(artist -> MoonsenseClient.getSearchPattern(this.search.getText()).matcher(artist.getName()).find())) {
                    this.addElement(e, counter);
                    ++counter;
                }
                else {
                    toRemove.add(e);
                }
            }
            this.scroll = 0;
        }
    }
    
    private void addElement(final ElementFavoriteSongEntry entry, final int counter) {
        final int x = this.parent.width / 2 + 95 + 2;
        final int y = this.parent.height / 2 - 72 + 2 + 14 + 2 + counter * 18;
        this.elements.add(new ElementFavoriteSongEntry(x, y, entry.getId(), this.parent));
    }
    
    @Override
    public void renderElement(final float partialTicks) {
        GlStateManager.pushMatrix();
        if (this.extended) {
            this.mc.getTextureManager().bindTexture(ElementFavoriteSongs.closeIcon);
            GuiUtils.setGlColor(new Color(0, 0, 0, 255).getRGB());
            Gui.drawModalRectWithCustomSizedTexture(this.getX(), this.getY(), 0.0f, 0.0f, this.width, this.height, (float)this.width, (float)this.height);
        }
        else {
            this.mc.getTextureManager().bindTexture(ElementFavoriteSongs.menuIcon);
            GuiUtils.setGlColor(new Color(0, 0, 0, 255).getRGB());
            Gui.drawModalRectWithCustomSizedTexture(this.getX(), this.getY(), 0.0f, 0.0f, this.width, this.height, (float)this.width, (float)this.height);
        }
        GlStateManager.popMatrix();
        if (this.extended) {
            this.search.render(this.mouseX, this.mouseY, partialTicks);
            GL11.glEnable(3089);
            GuiUtils.scissorHelper(this.parent.width / 2 + 95 + 2, this.parent.height / 2 - 72 + 2 + 14 + 2, this.parent.width / 2 + 95 + 2 + 96, this.parent.height / 2 - 72 + 2 + 14 + 2 + 126);
            for (final ElementFavoriteSongEntry e : this.elements) {
                e.setYOffset(this.scroll);
                e.render(this.mouseX, this.mouseY, partialTicks);
            }
            GL11.glDisable(3089);
            this.drawScroll(this.parent.width / 2 + 95 + 2 + 96 + 1, this.parent.width / 2 + 95 + 2 + 96 + 4);
        }
    }
    
    private void drawScroll(final int leftBound, final int rightBound) {
        if (this.elementsSize() < 8) {
            return;
        }
        final int top = this.parent.height / 2 - 72 + 2 + 14 + 2;
        final int bottom = this.parent.height / 2 - 72 + 2 + 14 + 2 + 180;
        final int contentHeight = this.elementsSize() * 18 + (bottom - top);
        this.scroll *= -1;
        final int j2 = Math.max(0, contentHeight - (bottom - top - 4));
        if (j2 > 0) {
            int height = (bottom - top) * (bottom - top) / contentHeight;
            height = MathHelper.clamp_int(height, 32, bottom - top - 8);
            height -= (int)Math.min((this.scroll < 0.0) ? ((double)(-this.scroll)) : ((double)((this.scroll > contentHeight) ? (this.scroll - contentHeight) : 0)), height * 0.75);
            final int minY = Math.min(Math.max(this.scroll * (bottom - top - height) / contentHeight + top, top), bottom - height);
            final Color c = new Color(255, 255, 255, 255);
            GuiUtils.drawRoundedRect((float)(rightBound - 5), (float)minY, rightBound - 5 + 2.75f, minY + height - 1.25f, 1.5f, MoonsenseClient.getMainColor(255));
        }
        this.scroll *= -1;
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (!Mouse.isButtonDown(0)) {
            int wheel = Mouse.getEventDWheel();
            if (wheel != 0) {
                if (wheel > 0) {
                    wheel = 36;
                }
                else if (wheel < 0) {
                    wheel = -36;
                }
                this.scroll += wheel;
                this.scroll = MathHelper.clamp_int(this.scroll, -1 * (18 * (this.elementsSize() - 7)), 0);
            }
        }
    }
    
    @Override
    public void renderBackground(final float partialTicks) {
        if (this.extended) {
            GuiUtils.drawRoundedRect((float)(this.parent.width / 2 + 95), (float)(this.parent.height / 2 - 72), (float)(this.parent.width / 2 + 95 + 100), (float)(this.parent.height / 2 + 72), 3.0f, new Color(0, 0, 0, 140).getRGB());
            GuiUtils.drawRoundedOutline(this.parent.width / 2 + 95, this.parent.height / 2 - 72, this.parent.width / 2 + 95 + 100, this.parent.height / 2 + 72, 3.0f, 2.0f, new Color(125, 136, 142, 150).getRGB());
        }
    }
    
    @SubscribeEvent
    public void songFavorited(final SCSongFavoritedEvent event) {
        for (final ElementFavoriteSongEntry e : this.elements) {
            if (e.getId().equals(event.id)) {
                return;
            }
            if (e.getTrack() != null && e.getTrack().equals(event.track)) {
                return;
            }
        }
        ElementFavoriteSongEntry e = new ElementFavoriteSongEntry(this.parent.width / 2 + 95 + 2, this.parent.height / 2 - 72 + 2 + 14 + 2 + this.elements.size() * 18, event.id, this.parent);
        this.elements.add(e);
    }
    
    @SubscribeEvent
    public void songUnfavorited(final SCSongUnfavoritedEvent event) {
        ElementFavoriteSongEntry toRemove = null;
        for (final ElementFavoriteSongEntry e : this.elements) {
            if (e.getId().equals(event.id)) {
                toRemove = e;
            }
            if (e.getTrack() != null && e.getTrack().equals(event.track)) {
                toRemove = e;
            }
        }
        this.elements.remove(toRemove);
    }
    
    public int indexEntriesByIdOrTrack(@NotNull final String id, final Track track) {
        for (int i = 0; i < this.elements.size(); ++i) {
            final ElementFavoriteSongEntry e = this.elements.get(i);
            if (e.getId().equals(id)) {
                return i;
            }
            if (e.getTrack() != null && track != null && e.getTrack().equals(track)) {
                return i;
            }
        }
        return -1;
    }
    
    public void getEntryAndPlay(final int index) {
        try {
            SpotifyManager.getSpotifyAPI().startResumeUsersPlayback().uris(JsonParser.parseString("[\"spotify:track:" + this.elements.get(index).getId() + "\"]").getAsJsonArray()).build().execute();
        }
        catch (ParseException | JsonSyntaxException | SpotifyWebApiException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    public int elementsSize() {
        return this.elements.size();
    }
    
    public void toggleVisibility() {
        this.extended = !this.extended;
    }
    
    public boolean isExtended() {
        return this.extended;
    }
    
    public void setExtended(final boolean extended) {
        this.extended = extended;
    }
    
    public ElementTextfield getSearch() {
        return this.search;
    }
}
