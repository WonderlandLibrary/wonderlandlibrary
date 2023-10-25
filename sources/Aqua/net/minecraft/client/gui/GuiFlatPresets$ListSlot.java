package net.minecraft.client.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/*
 * Exception performing whole class analysis ignored.
 */
class GuiFlatPresets.ListSlot
extends GuiSlot {
    public int field_148175_k;

    public GuiFlatPresets.ListSlot() {
        super(GuiFlatPresets.this.mc, GuiFlatPresets.width, GuiFlatPresets.height, 80, GuiFlatPresets.height - 37, 24);
        this.field_148175_k = -1;
    }

    private void func_178054_a(int p_178054_1_, int p_178054_2_, Item p_178054_3_, int p_178054_4_) {
        this.func_148173_e(p_178054_1_ + 1, p_178054_2_ + 1);
        GlStateManager.enableRescaleNormal();
        RenderHelper.enableGUIStandardItemLighting();
        GuiFlatPresets.this.itemRender.renderItemIntoGUI(new ItemStack(p_178054_3_, 1, p_178054_4_), p_178054_1_ + 2, p_178054_2_ + 2);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }

    private void func_148173_e(int p_148173_1_, int p_148173_2_) {
        this.func_148171_c(p_148173_1_, p_148173_2_, 0, 0);
    }

    private void func_148171_c(int p_148171_1_, int p_148171_2_, int p_148171_3_, int p_148171_4_) {
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.mc.getTextureManager().bindTexture(Gui.statIcons);
        float f = 0.0078125f;
        float f1 = 0.0078125f;
        int i = 18;
        int j = 18;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(p_148171_1_ + 0), (double)(p_148171_2_ + 18), (double)GuiFlatPresets.zLevel).tex((double)((float)(p_148171_3_ + 0) * 0.0078125f), (double)((float)(p_148171_4_ + 18) * 0.0078125f)).endVertex();
        worldrenderer.pos((double)(p_148171_1_ + 18), (double)(p_148171_2_ + 18), (double)GuiFlatPresets.zLevel).tex((double)((float)(p_148171_3_ + 18) * 0.0078125f), (double)((float)(p_148171_4_ + 18) * 0.0078125f)).endVertex();
        worldrenderer.pos((double)(p_148171_1_ + 18), (double)(p_148171_2_ + 0), (double)GuiFlatPresets.zLevel).tex((double)((float)(p_148171_3_ + 18) * 0.0078125f), (double)((float)(p_148171_4_ + 0) * 0.0078125f)).endVertex();
        worldrenderer.pos((double)(p_148171_1_ + 0), (double)(p_148171_2_ + 0), (double)GuiFlatPresets.zLevel).tex((double)((float)(p_148171_3_ + 0) * 0.0078125f), (double)((float)(p_148171_4_ + 0) * 0.0078125f)).endVertex();
        tessellator.draw();
    }

    protected int getSize() {
        return GuiFlatPresets.access$000().size();
    }

    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        this.field_148175_k = slotIndex;
        GuiFlatPresets.this.func_146426_g();
        GuiFlatPresets.access$200((GuiFlatPresets)GuiFlatPresets.this).setText(((GuiFlatPresets.LayerItem)GuiFlatPresets.access$000().get((int)GuiFlatPresets.access$100((GuiFlatPresets)GuiFlatPresets.this).field_148175_k)).field_148233_c);
    }

    protected boolean isSelected(int slotIndex) {
        return slotIndex == this.field_148175_k;
    }

    protected void drawBackground() {
    }

    protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
        GuiFlatPresets.LayerItem guiflatpresets$layeritem = (GuiFlatPresets.LayerItem)GuiFlatPresets.access$000().get(entryID);
        this.func_178054_a(p_180791_2_, p_180791_3_, guiflatpresets$layeritem.field_148234_a, guiflatpresets$layeritem.field_179037_b);
        GuiFlatPresets.this.fontRendererObj.drawString(guiflatpresets$layeritem.field_148232_b, p_180791_2_ + 18 + 5, p_180791_3_ + 6, 0xFFFFFF);
    }
}
