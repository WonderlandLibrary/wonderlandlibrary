// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Collections;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Items;
import java.util.Arrays;
import net.minecraft.init.Biomes;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.Map;
import com.google.common.collect.Maps;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraft.world.biome.Biome;
import net.minecraft.item.Item;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import java.util.List;

public class GuiFlatPresets extends GuiScreen
{
    private static final List<LayerItem> FLAT_WORLD_PRESETS;
    private final GuiCreateFlatWorld parentScreen;
    private String presetsTitle;
    private String presetsShare;
    private String listText;
    private ListSlot list;
    private GuiButton btnSelect;
    private GuiTextField export;
    
    public GuiFlatPresets(final GuiCreateFlatWorld p_i46318_1_) {
        this.parentScreen = p_i46318_1_;
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.presetsTitle = I18n.format("createWorld.customize.presets.title", new Object[0]);
        this.presetsShare = I18n.format("createWorld.customize.presets.share", new Object[0]);
        this.listText = I18n.format("createWorld.customize.presets.list", new Object[0]);
        this.export = new GuiTextField(2, this.fontRenderer, 50, 40, this.width - 100, 20);
        this.list = new ListSlot();
        this.export.setMaxStringLength(1230);
        this.export.setText(this.parentScreen.getPreset());
        this.btnSelect = this.addButton(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("createWorld.customize.presets.select", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.updateButtonValidity();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.export.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (!this.export.textboxKeyTyped(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0 && this.hasValidSelection()) {
            this.parentScreen.setPreset(this.export.getText());
            GuiFlatPresets.mc.displayGuiScreen(this.parentScreen);
        }
        else if (button.id == 1) {
            GuiFlatPresets.mc.displayGuiScreen(this.parentScreen);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, this.presetsTitle, this.width / 2, 8, 16777215);
        this.drawString(this.fontRenderer, this.presetsShare, 50, 30, 10526880);
        this.drawString(this.fontRenderer, this.listText, 50, 70, 10526880);
        this.export.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void updateScreen() {
        this.export.updateCursorCounter();
        super.updateScreen();
    }
    
    public void updateButtonValidity() {
        this.btnSelect.enabled = this.hasValidSelection();
    }
    
    private boolean hasValidSelection() {
        return (this.list.selected > -1 && this.list.selected < GuiFlatPresets.FLAT_WORLD_PRESETS.size()) || this.export.getText().length() > 1;
    }
    
    private static void registerPreset(final String name, final Item icon, final Biome biome, final List<String> features, final FlatLayerInfo... layers) {
        registerPreset(name, icon, 0, biome, features, layers);
    }
    
    private static void registerPreset(final String name, final Item icon, final int iconMetadata, final Biome biome, final List<String> features, final FlatLayerInfo... layers) {
        final FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
        for (int i = layers.length - 1; i >= 0; --i) {
            flatgeneratorinfo.getFlatLayers().add(layers[i]);
        }
        flatgeneratorinfo.setBiome(Biome.getIdForBiome(biome));
        flatgeneratorinfo.updateLayers();
        for (final String s : features) {
            flatgeneratorinfo.getWorldFeatures().put(s, Maps.newHashMap());
        }
        GuiFlatPresets.FLAT_WORLD_PRESETS.add(new LayerItem(icon, iconMetadata, name, flatgeneratorinfo.toString()));
    }
    
    static {
        FLAT_WORLD_PRESETS = Lists.newArrayList();
        registerPreset(I18n.format("createWorld.customize.preset.classic_flat", new Object[0]), Item.getItemFromBlock(Blocks.GRASS), Biomes.PLAINS, Arrays.asList("village"), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(2, Blocks.DIRT), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.tunnelers_dream", new Object[0]), Item.getItemFromBlock(Blocks.STONE), Biomes.EXTREME_HILLS, Arrays.asList("biome_1", "dungeon", "decoration", "stronghold", "mineshaft"), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(230, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.water_world", new Object[0]), Items.WATER_BUCKET, Biomes.DEEP_OCEAN, Arrays.asList("biome_1", "oceanmonument"), new FlatLayerInfo(90, Blocks.WATER), new FlatLayerInfo(5, Blocks.SAND), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(5, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.overworld", new Object[0]), Item.getItemFromBlock(Blocks.TALLGRASS), BlockTallGrass.EnumType.GRASS.getMeta(), Biomes.PLAINS, Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon", "lake", "lava_lake"), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.snowy_kingdom", new Object[0]), Item.getItemFromBlock(Blocks.SNOW_LAYER), Biomes.ICE_PLAINS, Arrays.asList("village", "biome_1"), new FlatLayerInfo(1, Blocks.SNOW_LAYER), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.bottomless_pit", new Object[0]), Items.FEATHER, Biomes.PLAINS, Arrays.asList("village", "biome_1"), new FlatLayerInfo(1, Blocks.GRASS), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(2, Blocks.COBBLESTONE));
        registerPreset(I18n.format("createWorld.customize.preset.desert", new Object[0]), Item.getItemFromBlock(Blocks.SAND), Biomes.DESERT, Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon"), new FlatLayerInfo(8, Blocks.SAND), new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.redstone_ready", new Object[0]), Items.REDSTONE, Biomes.DESERT, Collections.emptyList(), new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        registerPreset(I18n.format("createWorld.customize.preset.the_void", new Object[0]), Item.getItemFromBlock(Blocks.BARRIER), Biomes.VOID, Arrays.asList("decoration"), new FlatLayerInfo(1, Blocks.AIR));
    }
    
    static class LayerItem
    {
        public Item icon;
        public int iconMetadata;
        public String name;
        public String generatorInfo;
        
        public LayerItem(final Item iconIn, final int iconMetadataIn, final String nameIn, final String generatorInfoIn) {
            this.icon = iconIn;
            this.iconMetadata = iconMetadataIn;
            this.name = nameIn;
            this.generatorInfo = generatorInfoIn;
        }
    }
    
    class ListSlot extends GuiSlot
    {
        public int selected;
        
        public ListSlot() {
            super(GuiFlatPresets.mc, GuiFlatPresets.this.width, GuiFlatPresets.this.height, 80, GuiFlatPresets.this.height - 37, 24);
            this.selected = -1;
        }
        
        private void renderIcon(final int p_178054_1_, final int p_178054_2_, final Item icon, final int iconMetadata) {
            this.blitSlotBg(p_178054_1_ + 1, p_178054_2_ + 1);
            GlStateManager.enableRescaleNormal();
            RenderHelper.enableGUIStandardItemLighting();
            GuiFlatPresets.this.itemRender.renderItemIntoGUI(new ItemStack(icon, 1, icon.getHasSubtypes() ? iconMetadata : 0), p_178054_1_ + 2, p_178054_2_ + 2);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
        }
        
        private void blitSlotBg(final int p_148173_1_, final int p_148173_2_) {
            this.blitSlotIcon(p_148173_1_, p_148173_2_, 0, 0);
        }
        
        private void blitSlotIcon(final int p_148171_1_, final int p_148171_2_, final int p_148171_3_, final int p_148171_4_) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(Gui.STAT_ICONS);
            final float f = 0.0078125f;
            final float f2 = 0.0078125f;
            final int i = 18;
            final int j = 18;
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(p_148171_1_ + 0, p_148171_2_ + 18, GuiFlatPresets.this.zLevel).tex((p_148171_3_ + 0) * 0.0078125f, (p_148171_4_ + 18) * 0.0078125f).endVertex();
            bufferbuilder.pos(p_148171_1_ + 18, p_148171_2_ + 18, GuiFlatPresets.this.zLevel).tex((p_148171_3_ + 18) * 0.0078125f, (p_148171_4_ + 18) * 0.0078125f).endVertex();
            bufferbuilder.pos(p_148171_1_ + 18, p_148171_2_ + 0, GuiFlatPresets.this.zLevel).tex((p_148171_3_ + 18) * 0.0078125f, (p_148171_4_ + 0) * 0.0078125f).endVertex();
            bufferbuilder.pos(p_148171_1_ + 0, p_148171_2_ + 0, GuiFlatPresets.this.zLevel).tex((p_148171_3_ + 0) * 0.0078125f, (p_148171_4_ + 0) * 0.0078125f).endVertex();
            tessellator.draw();
        }
        
        @Override
        protected int getSize() {
            return GuiFlatPresets.FLAT_WORLD_PRESETS.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            this.selected = slotIndex;
            GuiFlatPresets.this.updateButtonValidity();
            GuiFlatPresets.this.export.setText(GuiFlatPresets.FLAT_WORLD_PRESETS.get(GuiFlatPresets.this.list.selected).generatorInfo);
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return slotIndex == this.selected;
        }
        
        @Override
        protected void drawBackground() {
        }
        
        @Override
        protected void drawSlot(final int slotIndex, final int xPos, final int yPos, final int heightIn, final int mouseXIn, final int mouseYIn, final float partialTicks) {
            final LayerItem guiflatpresets$layeritem = GuiFlatPresets.FLAT_WORLD_PRESETS.get(slotIndex);
            this.renderIcon(xPos, yPos, guiflatpresets$layeritem.icon, guiflatpresets$layeritem.iconMetadata);
            GuiFlatPresets.this.fontRenderer.drawString(guiflatpresets$layeritem.name, xPos + 18 + 5, yPos + 6, 16777215);
        }
    }
}
