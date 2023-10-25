/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;

public class GuiCreateFlatWorld
extends GuiScreen {
    private FlatGeneratorInfo theFlatGeneratorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
    private GuiButton field_146389_t;
    private final GuiCreateWorld createWorldGui;
    private String field_146391_r;
    private GuiButton field_146388_u;
    private GuiButton field_146386_v;
    private Details createFlatWorldListSlotGui;
    private String field_146394_i;
    private String flatWorldTitle;

    private boolean func_146382_i() {
        return this.createFlatWorldListSlotGui.field_148228_k > -1 && this.createFlatWorldListSlotGui.field_148228_k < this.theFlatGeneratorInfo.getFlatLayers().size();
    }

    public void func_146375_g() {
        boolean bl;
        this.field_146386_v.enabled = bl = this.func_146382_i();
        this.field_146388_u.enabled = bl;
        this.field_146388_u.enabled = false;
        this.field_146389_t.enabled = false;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.flatWorldTitle = I18n.format("createWorld.customize.flat.title", new Object[0]);
        this.field_146394_i = I18n.format("createWorld.customize.flat.tile", new Object[0]);
        this.field_146391_r = I18n.format("createWorld.customize.flat.height", new Object[0]);
        this.createFlatWorldListSlotGui = new Details();
        this.field_146389_t = new GuiButton(2, width / 2 - 154, height - 52, 100, 20, String.valueOf(I18n.format("createWorld.customize.flat.addLayer", new Object[0])) + " (NYI)");
        this.buttonList.add(this.field_146389_t);
        this.field_146388_u = new GuiButton(3, width / 2 - 50, height - 52, 100, 20, String.valueOf(I18n.format("createWorld.customize.flat.editLayer", new Object[0])) + " (NYI)");
        this.buttonList.add(this.field_146388_u);
        this.field_146386_v = new GuiButton(4, width / 2 - 155, height - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer", new Object[0]));
        this.buttonList.add(this.field_146386_v);
        this.buttonList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(5, width / 2 + 5, height - 52, 150, 20, I18n.format("createWorld.customize.presets", new Object[0])));
        this.buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.field_146388_u.visible = false;
        this.field_146389_t.visible = false;
        this.theFlatGeneratorInfo.func_82645_d();
        this.func_146375_g();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.createFlatWorldListSlotGui.handleMouseInput();
    }

    public GuiCreateFlatWorld(GuiCreateWorld guiCreateWorld, String string) {
        this.createWorldGui = guiCreateWorld;
        this.func_146383_a(string);
    }

    public String func_146384_e() {
        return this.theFlatGeneratorInfo.toString();
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) throws IOException {
        int n = this.theFlatGeneratorInfo.getFlatLayers().size() - this.createFlatWorldListSlotGui.field_148228_k - 1;
        if (guiButton.id == 1) {
            this.mc.displayGuiScreen(this.createWorldGui);
        } else if (guiButton.id == 0) {
            this.createWorldGui.chunkProviderSettingsJson = this.func_146384_e();
            this.mc.displayGuiScreen(this.createWorldGui);
        } else if (guiButton.id == 5) {
            this.mc.displayGuiScreen(new GuiFlatPresets(this));
        } else if (guiButton.id == 4 && this.func_146382_i()) {
            this.theFlatGeneratorInfo.getFlatLayers().remove(n);
            this.createFlatWorldListSlotGui.field_148228_k = Math.min(this.createFlatWorldListSlotGui.field_148228_k, this.theFlatGeneratorInfo.getFlatLayers().size() - 1);
        }
        this.theFlatGeneratorInfo.func_82645_d();
        this.func_146375_g();
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.drawDefaultBackground();
        this.createFlatWorldListSlotGui.drawScreen(n, n2, f);
        this.drawCenteredString(this.fontRendererObj, this.flatWorldTitle, width / 2, 8, 0xFFFFFF);
        int n3 = width / 2 - 92 - 16;
        this.drawString(this.fontRendererObj, this.field_146394_i, n3, 32, 0xFFFFFF);
        this.drawString(this.fontRendererObj, this.field_146391_r, n3 + 2 + 213 - this.fontRendererObj.getStringWidth(this.field_146391_r), 32, 0xFFFFFF);
        super.drawScreen(n, n2, f);
    }

    public void func_146383_a(String string) {
        this.theFlatGeneratorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(string);
    }

    class Details
    extends GuiSlot {
        public int field_148228_k;

        @Override
        protected void drawSlot(int n, int n2, int n3, int n4, int n5, int n6) {
            String string;
            FlatLayerInfo flatLayerInfo = GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().get(GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - n - 1);
            IBlockState iBlockState = flatLayerInfo.func_175900_c();
            Block block = iBlockState.getBlock();
            Item item = Item.getItemFromBlock(block);
            ItemStack itemStack = block != Blocks.air && item != null ? new ItemStack(item, 1, block.getMetaFromState(iBlockState)) : null;
            String string2 = string = itemStack == null ? "Air" : item.getItemStackDisplayName(itemStack);
            if (item == null) {
                if (block != Blocks.water && block != Blocks.flowing_water) {
                    if (block == Blocks.lava || block == Blocks.flowing_lava) {
                        item = Items.lava_bucket;
                    }
                } else {
                    item = Items.water_bucket;
                }
                if (item != null) {
                    itemStack = new ItemStack(item, 1, block.getMetaFromState(iBlockState));
                    string = block.getLocalizedName();
                }
            }
            this.func_148225_a(n2, n3, itemStack);
            GuiCreateFlatWorld.this.fontRendererObj.drawString(string, n2 + 18 + 5, n3 + 3, 0xFFFFFF);
            String string3 = n == 0 ? I18n.format("createWorld.customize.flat.layer.top", flatLayerInfo.getLayerCount()) : (n == GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - 1 ? I18n.format("createWorld.customize.flat.layer.bottom", flatLayerInfo.getLayerCount()) : I18n.format("createWorld.customize.flat.layer", flatLayerInfo.getLayerCount()));
            GuiCreateFlatWorld.this.fontRendererObj.drawString(string3, n2 + 2 + 213 - GuiCreateFlatWorld.this.fontRendererObj.getStringWidth(string3), n3 + 3, 0xFFFFFF);
        }

        @Override
        protected void elementClicked(int n, boolean bl, int n2, int n3) {
            this.field_148228_k = n;
            GuiCreateFlatWorld.this.func_146375_g();
        }

        private void func_148225_a(int n, int n2, ItemStack itemStack) {
            this.func_148226_e(n + 1, n2 + 1);
            GlStateManager.enableRescaleNormal();
            if (itemStack != null && itemStack.getItem() != null) {
                RenderHelper.enableGUIStandardItemLighting();
                GuiCreateFlatWorld.this.itemRender.renderItemIntoGUI(itemStack, n + 2, n2 + 2);
                RenderHelper.disableStandardItemLighting();
            }
            GlStateManager.disableRescaleNormal();
        }

        private void func_148226_e(int n, int n2) {
            this.func_148224_c(n, n2, 0, 0);
        }

        private void func_148224_c(int n, int n2, int n3, int n4) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(Gui.statIcons);
            float f = 0.0078125f;
            float f2 = 0.0078125f;
            int n5 = 18;
            int n6 = 18;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(n + 0, n2 + 18, zLevel).tex((float)(n3 + 0) * 0.0078125f, (float)(n4 + 18) * 0.0078125f).endVertex();
            worldRenderer.pos(n + 18, n2 + 18, zLevel).tex((float)(n3 + 18) * 0.0078125f, (float)(n4 + 18) * 0.0078125f).endVertex();
            worldRenderer.pos(n + 18, n2 + 0, zLevel).tex((float)(n3 + 18) * 0.0078125f, (float)(n4 + 0) * 0.0078125f).endVertex();
            worldRenderer.pos(n + 0, n2 + 0, zLevel).tex((float)(n3 + 0) * 0.0078125f, (float)(n4 + 0) * 0.0078125f).endVertex();
            tessellator.draw();
        }

        @Override
        protected int getSize() {
            return GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size();
        }

        @Override
        protected void drawBackground() {
        }

        @Override
        protected int getScrollBarX() {
            return this.width - 70;
        }

        public Details() {
            super(GuiCreateFlatWorld.this.mc, width, height, 43, height - 60, 24);
            this.field_148228_k = -1;
        }

        @Override
        protected boolean isSelected(int n) {
            return n == this.field_148228_k;
        }
    }
}

