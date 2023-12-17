// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourcePackListEntry;
import java.util.List;
import net.minecraft.client.Minecraft;

public abstract class GuiResourcePackList extends GuiListExtended
{
    protected final Minecraft mc;
    protected final List<ResourcePackListEntry> resourcePackEntries;
    
    public GuiResourcePackList(final Minecraft mcIn, final int p_i45055_2_, final int p_i45055_3_, final List<ResourcePackListEntry> p_i45055_4_) {
        super(mcIn, p_i45055_2_, p_i45055_3_, 32, p_i45055_3_ - 55 + 4, 36);
        this.mc = mcIn;
        this.resourcePackEntries = p_i45055_4_;
        this.centerListVertically = false;
        this.setHasListHeader(true, (int)(mcIn.fontRenderer.FONT_HEIGHT * 1.5f));
    }
    
    @Override
    protected void drawListHeader(final int insideLeft, final int insideTop, final Tessellator tessellatorIn) {
        final String s = TextFormatting.UNDERLINE + "" + TextFormatting.BOLD + this.getListHeader();
        this.mc.fontRenderer.drawString(s, insideLeft + this.width / 2 - this.mc.fontRenderer.getStringWidth(s) / 2, Math.min(this.top + 3, insideTop), 16777215);
    }
    
    protected abstract String getListHeader();
    
    public List<ResourcePackListEntry> getList() {
        return this.resourcePackEntries;
    }
    
    @Override
    protected int getSize() {
        return this.getList().size();
    }
    
    @Override
    public ResourcePackListEntry getListEntry(final int index) {
        return this.getList().get(index);
    }
    
    @Override
    public int getListWidth() {
        return this.width;
    }
    
    @Override
    protected int getScrollBarX() {
        return this.right - 6;
    }
}
