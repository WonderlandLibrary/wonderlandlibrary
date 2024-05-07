package net.minecraft.client.gui;

import com.masterof13fps.Client;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourcePackListEntry;

import java.awt.*;
import java.util.List;

public abstract class GuiResourcePackList extends GuiListExtended {
    protected final Minecraft mc;
    protected final List<ResourcePackListEntry> field_148204_l;

    public GuiResourcePackList(Minecraft mcIn, int p_i45055_2_, int p_i45055_3_, List<ResourcePackListEntry> p_i45055_4_) {
        super(mcIn, p_i45055_2_, p_i45055_3_, 32, p_i45055_3_ - 55 + 4, 36);
        this.mc = mcIn;
        this.field_148204_l = p_i45055_4_;
        this.field_148163_i = false;
        this.setHasListHeader(true, (int) ((float) mcIn.fontRendererObj.FONT_HEIGHT * 1.5F));
    }

    /**
     * Handles drawing a list's header row.
     */
    protected void drawListHeader(int p_148129_1_, int p_148129_2_, Tessellator p_148129_3_) {
        String s = getListHeader();
        UnicodeFontRenderer comfortaa20 = Client.main().fontMgr().font("Comfortaa", 20, Font.PLAIN);
        comfortaa20.drawStringWithShadow(s, p_148129_1_ + width / 2 - comfortaa20.getStringWidth(s) / 2, Math.min(this.top + 3, p_148129_2_), 16777215);
    }

    protected abstract String getListHeader();

    public List<ResourcePackListEntry> getList() {
        return this.field_148204_l;
    }

    protected int getSize() {
        return this.getList().size();
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public ResourcePackListEntry getListEntry(int index) {
        return (ResourcePackListEntry) this.getList().get(index);
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth() {
        return this.width;
    }

    protected int getScrollBarX() {
        return this.right - 6;
    }
}
