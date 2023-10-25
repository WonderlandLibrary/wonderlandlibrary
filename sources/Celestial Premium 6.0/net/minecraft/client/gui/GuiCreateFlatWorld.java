/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
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
    private final GuiCreateWorld createWorldGui;
    private FlatGeneratorInfo theFlatGeneratorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
    private String flatWorldTitle;
    private String materialText;
    private String heightText;
    private Details createFlatWorldListSlotGui;
    private GuiButton addLayerButton;
    private GuiButton editLayerButton;
    private GuiButton removeLayerButton;

    public GuiCreateFlatWorld(GuiCreateWorld createWorldGuiIn, String preset) {
        this.createWorldGui = createWorldGuiIn;
        this.setPreset(preset);
    }

    public String getPreset() {
        return this.theFlatGeneratorInfo.toString();
    }

    public void setPreset(String preset) {
        this.theFlatGeneratorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(preset);
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.flatWorldTitle = I18n.format("createWorld.customize.flat.title", new Object[0]);
        this.materialText = I18n.format("createWorld.customize.flat.tile", new Object[0]);
        this.heightText = I18n.format("createWorld.customize.flat.height", new Object[0]);
        this.createFlatWorldListSlotGui = new Details();
        this.addLayerButton = this.addButton(new GuiButton(2, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("createWorld.customize.flat.addLayer", new Object[0]) + " (NYI)"));
        this.editLayerButton = this.addButton(new GuiButton(3, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("createWorld.customize.flat.editLayer", new Object[0]) + " (NYI)"));
        this.removeLayerButton = this.addButton(new GuiButton(4, this.width / 2 - 155, this.height - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(5, this.width / 2 + 5, this.height - 52, 150, 20, I18n.format("createWorld.customize.presets", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.addLayerButton.visible = false;
        this.editLayerButton.visible = false;
        this.theFlatGeneratorInfo.updateLayers();
        this.onLayersChanged();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.createFlatWorldListSlotGui.handleMouseInput();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        int i = this.theFlatGeneratorInfo.getFlatLayers().size() - this.createFlatWorldListSlotGui.selectedLayer - 1;
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.createWorldGui);
        } else if (button.id == 0) {
            this.createWorldGui.chunkProviderSettingsJson = this.getPreset();
            this.mc.displayGuiScreen(this.createWorldGui);
        } else if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiFlatPresets(this));
        } else if (button.id == 4 && this.hasSelectedLayer()) {
            this.theFlatGeneratorInfo.getFlatLayers().remove(i);
            this.createFlatWorldListSlotGui.selectedLayer = Math.min(this.createFlatWorldListSlotGui.selectedLayer, this.theFlatGeneratorInfo.getFlatLayers().size() - 1);
        }
        this.theFlatGeneratorInfo.updateLayers();
        this.onLayersChanged();
    }

    public void onLayersChanged() {
        boolean flag;
        this.removeLayerButton.enabled = flag = this.hasSelectedLayer();
        this.editLayerButton.enabled = flag;
        this.editLayerButton.enabled = false;
        this.addLayerButton.enabled = false;
    }

    private boolean hasSelectedLayer() {
        return this.createFlatWorldListSlotGui.selectedLayer > -1 && this.createFlatWorldListSlotGui.selectedLayer < this.theFlatGeneratorInfo.getFlatLayers().size();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.createFlatWorldListSlotGui.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.flatWorldTitle, this.width / 2, 8, 0xFFFFFF);
        int i = this.width / 2 - 92 - 16;
        this.drawString(this.fontRendererObj, this.materialText, i, 32, 0xFFFFFF);
        this.drawString(this.fontRendererObj, this.heightText, i + 2 + 213 - this.fontRendererObj.getStringWidth(this.heightText), 32, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    class Details
    extends GuiSlot {
        public int selectedLayer;

        public Details() {
            super(GuiCreateFlatWorld.this.mc, GuiCreateFlatWorld.this.width, GuiCreateFlatWorld.this.height, 43, GuiCreateFlatWorld.this.height - 60, 24);
            this.selectedLayer = -1;
        }

        private void drawItem(int x, int z, ItemStack itemToDraw) {
            this.drawItemBackground(x + 1, z + 1);
            GlStateManager.enableRescaleNormal();
            if (!itemToDraw.isEmpty()) {
                RenderHelper.enableGUIStandardItemLighting();
                GuiCreateFlatWorld.this.itemRender.renderItemIntoGUI(itemToDraw, x + 2, z + 2);
                RenderHelper.disableStandardItemLighting();
            }
            GlStateManager.disableRescaleNormal();
        }

        private void drawItemBackground(int x, int y) {
            this.drawItemBackground(x, y, 0, 0);
        }

        private void drawItemBackground(int x, int z, int textureX, int textureY) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(Gui.STAT_ICONS);
            float f = 0.0078125f;
            float f1 = 0.0078125f;
            int i = 18;
            int j = 18;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(x + 0, z + 18, GuiCreateFlatWorld.this.zLevel).tex((float)(textureX + 0) * 0.0078125f, (float)(textureY + 18) * 0.0078125f).endVertex();
            bufferbuilder.pos(x + 18, z + 18, GuiCreateFlatWorld.this.zLevel).tex((float)(textureX + 18) * 0.0078125f, (float)(textureY + 18) * 0.0078125f).endVertex();
            bufferbuilder.pos(x + 18, z + 0, GuiCreateFlatWorld.this.zLevel).tex((float)(textureX + 18) * 0.0078125f, (float)(textureY + 0) * 0.0078125f).endVertex();
            bufferbuilder.pos(x + 0, z + 0, GuiCreateFlatWorld.this.zLevel).tex((float)(textureX + 0) * 0.0078125f, (float)(textureY + 0) * 0.0078125f).endVertex();
            tessellator.draw();
        }

        @Override
        protected int getSize() {
            return GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size();
        }

        @Override
        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
            this.selectedLayer = slotIndex;
            GuiCreateFlatWorld.this.onLayersChanged();
        }

        @Override
        protected boolean isSelected(int slotIndex) {
            return slotIndex == this.selectedLayer;
        }

        @Override
        protected void drawBackground() {
        }

        @Override
        protected void func_192637_a(int p_192637_1_, int p_192637_2_, int p_192637_3_, int p_192637_4_, int p_192637_5_, int p_192637_6_, float p_192637_7_) {
            FlatLayerInfo flatlayerinfo = GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().get(GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - p_192637_1_ - 1);
            IBlockState iblockstate = flatlayerinfo.getLayerMaterial();
            Block block = iblockstate.getBlock();
            Item item = Item.getItemFromBlock(block);
            if (item == Items.field_190931_a) {
                if (block != Blocks.WATER && block != Blocks.FLOWING_WATER) {
                    if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) {
                        item = Items.LAVA_BUCKET;
                    }
                } else {
                    item = Items.WATER_BUCKET;
                }
            }
            ItemStack itemstack = new ItemStack(item, 1, item.getHasSubtypes() ? block.getMetaFromState(iblockstate) : 0);
            String s = item.getItemStackDisplayName(itemstack);
            this.drawItem(p_192637_2_, p_192637_3_, itemstack);
            GuiCreateFlatWorld.this.fontRendererObj.drawString(s, p_192637_2_ + 18 + 5, p_192637_3_ + 3, 0xFFFFFF);
            String s1 = p_192637_1_ == 0 ? I18n.format("createWorld.customize.flat.layer.top", flatlayerinfo.getLayerCount()) : (p_192637_1_ == GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - 1 ? I18n.format("createWorld.customize.flat.layer.bottom", flatlayerinfo.getLayerCount()) : I18n.format("createWorld.customize.flat.layer", flatlayerinfo.getLayerCount()));
            GuiCreateFlatWorld.this.fontRendererObj.drawString(s1, p_192637_2_ + 2 + 213 - GuiCreateFlatWorld.this.fontRendererObj.getStringWidth(s1), p_192637_3_ + 3, 0xFFFFFF);
        }

        @Override
        protected int getScrollBarX() {
            return this.width - 70;
        }
    }
}

