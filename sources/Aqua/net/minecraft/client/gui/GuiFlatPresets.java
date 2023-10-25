package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateFlatWorld;
import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import org.lwjgl.input.Keyboard;

public class GuiFlatPresets
extends GuiScreen {
    private static final List<LayerItem> FLAT_WORLD_PRESETS = Lists.newArrayList();
    private final GuiCreateFlatWorld parentScreen;
    private String presetsTitle;
    private String presetsShare;
    private String field_146436_r;
    private ListSlot field_146435_s;
    private GuiButton field_146434_t;
    private GuiTextField field_146433_u;

    public GuiFlatPresets(GuiCreateFlatWorld p_i46318_1_) {
        this.parentScreen = p_i46318_1_;
    }

    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents((boolean)true);
        this.presetsTitle = I18n.format((String)"createWorld.customize.presets.title", (Object[])new Object[0]);
        this.presetsShare = I18n.format((String)"createWorld.customize.presets.share", (Object[])new Object[0]);
        this.field_146436_r = I18n.format((String)"createWorld.customize.presets.list", (Object[])new Object[0]);
        this.field_146433_u = new GuiTextField(2, this.fontRendererObj, 50, 40, width - 100, 20);
        this.field_146435_s = new ListSlot(this);
        this.field_146433_u.setMaxStringLength(1230);
        this.field_146433_u.setText(this.parentScreen.func_146384_e());
        this.field_146434_t = new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format((String)"createWorld.customize.presets.select", (Object[])new Object[0]));
        this.buttonList.add((Object)this.field_146434_t);
        this.buttonList.add((Object)new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format((String)"gui.cancel", (Object[])new Object[0])));
        this.func_146426_g();
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_146435_s.handleMouseInput();
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.field_146433_u.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!this.field_146433_u.textboxKeyTyped(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0 && this.func_146430_p()) {
            this.parentScreen.func_146383_a(this.field_146433_u.getText());
            this.mc.displayGuiScreen((GuiScreen)this.parentScreen);
        } else if (button.id == 1) {
            this.mc.displayGuiScreen((GuiScreen)this.parentScreen);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.field_146435_s.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.presetsTitle, width / 2, 8, 0xFFFFFF);
        this.drawString(this.fontRendererObj, this.presetsShare, 50, 30, 0xA0A0A0);
        this.drawString(this.fontRendererObj, this.field_146436_r, 50, 70, 0xA0A0A0);
        this.field_146433_u.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void updateScreen() {
        this.field_146433_u.updateCursorCounter();
        super.updateScreen();
    }

    public void func_146426_g() {
        boolean flag;
        this.field_146434_t.enabled = flag = this.func_146430_p();
    }

    private boolean func_146430_p() {
        return this.field_146435_s.field_148175_k > -1 && this.field_146435_s.field_148175_k < FLAT_WORLD_PRESETS.size() || this.field_146433_u.getText().length() > 1;
    }

    private static void func_146425_a(String p_146425_0_, Item p_146425_1_, BiomeGenBase p_146425_2_, FlatLayerInfo ... p_146425_3_) {
        GuiFlatPresets.func_175354_a(p_146425_0_, p_146425_1_, 0, p_146425_2_, (List<String>)((List)null), p_146425_3_);
    }

    private static void func_146421_a(String p_146421_0_, Item p_146421_1_, BiomeGenBase p_146421_2_, List<String> p_146421_3_, FlatLayerInfo ... p_146421_4_) {
        GuiFlatPresets.func_175354_a(p_146421_0_, p_146421_1_, 0, p_146421_2_, p_146421_3_, p_146421_4_);
    }

    private static void func_175354_a(String p_175354_0_, Item p_175354_1_, int p_175354_2_, BiomeGenBase p_175354_3_, List<String> p_175354_4_, FlatLayerInfo ... p_175354_5_) {
        FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
        for (int i = p_175354_5_.length - 1; i >= 0; --i) {
            flatgeneratorinfo.getFlatLayers().add((Object)p_175354_5_[i]);
        }
        flatgeneratorinfo.setBiome(p_175354_3_.biomeID);
        flatgeneratorinfo.func_82645_d();
        if (p_175354_4_ != null) {
            for (String s : p_175354_4_) {
                flatgeneratorinfo.getWorldFeatures().put((Object)s, (Object)Maps.newHashMap());
            }
        }
        FLAT_WORLD_PRESETS.add((Object)new LayerItem(p_175354_1_, p_175354_2_, p_175354_0_, flatgeneratorinfo.toString()));
    }

    static /* synthetic */ List access$000() {
        return FLAT_WORLD_PRESETS;
    }

    static /* synthetic */ ListSlot access$100(GuiFlatPresets x0) {
        return x0.field_146435_s;
    }

    static /* synthetic */ GuiTextField access$200(GuiFlatPresets x0) {
        return x0.field_146433_u;
    }

    static {
        GuiFlatPresets.func_146421_a("Classic Flat", Item.getItemFromBlock((Block)Blocks.grass), BiomeGenBase.plains, (List<String>)Arrays.asList((Object[])new String[]{"village"}), new FlatLayerInfo(1, (Block)Blocks.grass), new FlatLayerInfo(2, Blocks.dirt), new FlatLayerInfo(1, Blocks.bedrock));
        GuiFlatPresets.func_146421_a("Tunnelers' Dream", Item.getItemFromBlock((Block)Blocks.stone), BiomeGenBase.extremeHills, (List<String>)Arrays.asList((Object[])new String[]{"biome_1", "dungeon", "decoration", "stronghold", "mineshaft"}), new FlatLayerInfo(1, (Block)Blocks.grass), new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(230, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
        GuiFlatPresets.func_146421_a("Water World", Items.water_bucket, BiomeGenBase.deepOcean, (List<String>)Arrays.asList((Object[])new String[]{"biome_1", "oceanmonument"}), new FlatLayerInfo(90, (Block)Blocks.water), new FlatLayerInfo(5, (Block)Blocks.sand), new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(5, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
        GuiFlatPresets.func_175354_a("Overworld", Item.getItemFromBlock((Block)Blocks.tallgrass), BlockTallGrass.EnumType.GRASS.getMeta(), BiomeGenBase.plains, (List<String>)Arrays.asList((Object[])new String[]{"village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon", "lake", "lava_lake"}), new FlatLayerInfo(1, (Block)Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
        GuiFlatPresets.func_146421_a("Snowy Kingdom", Item.getItemFromBlock((Block)Blocks.snow_layer), BiomeGenBase.icePlains, (List<String>)Arrays.asList((Object[])new String[]{"village", "biome_1"}), new FlatLayerInfo(1, Blocks.snow_layer), new FlatLayerInfo(1, (Block)Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
        GuiFlatPresets.func_146421_a("Bottomless Pit", Items.feather, BiomeGenBase.plains, (List<String>)Arrays.asList((Object[])new String[]{"village", "biome_1"}), new FlatLayerInfo(1, (Block)Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(2, Blocks.cobblestone));
        GuiFlatPresets.func_146421_a("Desert", Item.getItemFromBlock((Block)Blocks.sand), BiomeGenBase.desert, (List<String>)Arrays.asList((Object[])new String[]{"village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon"}), new FlatLayerInfo(8, (Block)Blocks.sand), new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
        GuiFlatPresets.func_146425_a("Redstone Ready", Items.redstone, BiomeGenBase.desert, new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
    }
}
