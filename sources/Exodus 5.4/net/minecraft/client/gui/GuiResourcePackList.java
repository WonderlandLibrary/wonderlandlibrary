/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.ResourcePackListEntry;
import net.minecraft.util.EnumChatFormatting;

public abstract class GuiResourcePackList
extends GuiListExtended {
    protected final Minecraft mc;
    protected final List<ResourcePackListEntry> field_148204_l;

    protected abstract String getListHeader();

    @Override
    protected int getScrollBarX() {
        return this.right - 6;
    }

    public GuiResourcePackList(Minecraft minecraft, int n, int n2, List<ResourcePackListEntry> list) {
        super(minecraft, n, n2, 32, n2 - 55 + 4, 36);
        this.mc = minecraft;
        this.field_148204_l = list;
        this.field_148163_i = false;
        this.setHasListHeader(true, (int)((float)Minecraft.fontRendererObj.FONT_HEIGHT * 1.5f));
    }

    @Override
    protected int getSize() {
        return this.getList().size();
    }

    public List<ResourcePackListEntry> getList() {
        return this.field_148204_l;
    }

    @Override
    public int getListWidth() {
        return this.width;
    }

    @Override
    protected void drawListHeader(int n, int n2, Tessellator tessellator) {
        String string = (Object)((Object)EnumChatFormatting.UNDERLINE) + (Object)((Object)EnumChatFormatting.BOLD) + this.getListHeader();
        Minecraft.fontRendererObj.drawString(string, n + this.width / 2 - Minecraft.fontRendererObj.getStringWidth(string) / 2, Math.min(this.top + 3, n2), 0xFFFFFF);
    }

    @Override
    public ResourcePackListEntry getListEntry(int n) {
        return this.getList().get(n);
    }
}

