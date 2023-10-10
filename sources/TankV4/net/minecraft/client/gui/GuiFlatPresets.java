package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockTallGrass;
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
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import org.lwjgl.input.Keyboard;

public class GuiFlatPresets extends GuiScreen {
   private final GuiCreateFlatWorld parentScreen;
   private GuiButton field_146434_t;
   private String field_146436_r;
   private String presetsTitle;
   private static final List FLAT_WORLD_PRESETS = Lists.newArrayList();
   private String presetsShare;
   private GuiFlatPresets.ListSlot field_146435_s;
   private GuiTextField field_146433_u;

   private static void func_146421_a(String var0, Item var1, BiomeGenBase var2, List var3, FlatLayerInfo... var4) {
      func_175354_a(var0, var1, 0, var2, var3, var4);
   }

   static GuiTextField access$1(GuiFlatPresets var0) {
      return var0.field_146433_u;
   }

   protected void keyTyped(char var1, int var2) throws IOException {
      if (!this.field_146433_u.textboxKeyTyped(var1, var2)) {
         super.keyTyped(var1, var2);
      }

   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.field_146435_s.handleMouseInput();
   }

   static List access$0() {
      return FLAT_WORLD_PRESETS;
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.field_146435_s.drawScreen(var1, var2, var3);
      this.drawCenteredString(this.fontRendererObj, this.presetsTitle, width / 2, 8, 16777215);
      this.drawString(this.fontRendererObj, this.presetsShare, 50, 30, 10526880);
      this.drawString(this.fontRendererObj, this.field_146436_r, 50, 70, 10526880);
      this.field_146433_u.drawTextBox();
      super.drawScreen(var1, var2, var3);
   }

   private static void func_175354_a(String var0, Item var1, int var2, BiomeGenBase var3, List var4, FlatLayerInfo... var5) {
      FlatGeneratorInfo var6 = new FlatGeneratorInfo();

      for(int var7 = var5.length - 1; var7 >= 0; --var7) {
         var6.getFlatLayers().add(var5[var7]);
      }

      var6.setBiome(var3.biomeID);
      var6.func_82645_d();
      if (var4 != null) {
         Iterator var8 = var4.iterator();

         while(var8.hasNext()) {
            String var9 = (String)var8.next();
            var6.getWorldFeatures().put(var9, Maps.newHashMap());
         }
      }

      FLAT_WORLD_PRESETS.add(new GuiFlatPresets.LayerItem(var1, var2, var0, var6.toString()));
   }

   static {
      func_146421_a("Classic Flat", Item.getItemFromBlock(Blocks.grass), BiomeGenBase.plains, Arrays.asList("village"), new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(2, Blocks.dirt), new FlatLayerInfo(1, Blocks.bedrock));
      func_146421_a("Tunnelers' Dream", Item.getItemFromBlock(Blocks.stone), BiomeGenBase.extremeHills, Arrays.asList("biome_1", "dungeon", "decoration", "stronghold", "mineshaft"), new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(230, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
      func_146421_a("Water World", Items.water_bucket, BiomeGenBase.deepOcean, Arrays.asList("biome_1", "oceanmonument"), new FlatLayerInfo(90, Blocks.water), new FlatLayerInfo(5, Blocks.sand), new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(5, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
      func_175354_a("Overworld", Item.getItemFromBlock(Blocks.tallgrass), BlockTallGrass.EnumType.GRASS.getMeta(), BiomeGenBase.plains, Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon", "lake", "lava_lake"), new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
      func_146421_a("Snowy Kingdom", Item.getItemFromBlock(Blocks.snow_layer), BiomeGenBase.icePlains, Arrays.asList("village", "biome_1"), new FlatLayerInfo(1, Blocks.snow_layer), new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
      func_146421_a("Bottomless Pit", Items.feather, BiomeGenBase.plains, Arrays.asList("village", "biome_1"), new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(2, Blocks.cobblestone));
      func_146421_a("Desert", Item.getItemFromBlock(Blocks.sand), BiomeGenBase.desert, Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon"), new FlatLayerInfo(8, Blocks.sand), new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
      func_146425_a("Redstone Ready", Items.redstone, BiomeGenBase.desert, new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
   }

   protected void mouseClicked(int var1, int var2, int var3) throws IOException {
      this.field_146433_u.mouseClicked(var1, var2, var3);
      super.mouseClicked(var1, var2, var3);
   }

   static GuiFlatPresets.ListSlot access$2(GuiFlatPresets var0) {
      return var0.field_146435_s;
   }

   private static void func_146425_a(String var0, Item var1, BiomeGenBase var2, FlatLayerInfo... var3) {
      func_175354_a(var0, var1, 0, var2, (List)null, var3);
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   protected void actionPerformed(GuiButton param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public GuiFlatPresets(GuiCreateFlatWorld var1) {
      this.parentScreen = var1;
   }

   public void initGui() {
      this.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      this.presetsTitle = I18n.format("createWorld.customize.presets.title");
      this.presetsShare = I18n.format("createWorld.customize.presets.share");
      this.field_146436_r = I18n.format("createWorld.customize.presets.list");
      this.field_146433_u = new GuiTextField(2, this.fontRendererObj, 50, 40, width - 100, 20);
      this.field_146435_s = new GuiFlatPresets.ListSlot(this);
      this.field_146433_u.setMaxStringLength(1230);
      this.field_146433_u.setText(this.parentScreen.func_146384_e());
      this.buttonList.add(this.field_146434_t = new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format("createWorld.customize.presets.select")));
      this.buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
      this.func_146426_g();
   }

   public void updateScreen() {
      this.field_146433_u.updateCursorCounter();
      super.updateScreen();
   }

   public void func_146426_g() {
      boolean var1 = this.func_146430_p();
      this.field_146434_t.enabled = var1;
   }

   static class LayerItem {
      public String field_148232_b;
      public Item field_148234_a;
      public String field_148233_c;
      public int field_179037_b;

      public LayerItem(Item var1, int var2, String var3, String var4) {
         this.field_148234_a = var1;
         this.field_179037_b = var2;
         this.field_148232_b = var3;
         this.field_148233_c = var4;
      }
   }

   class ListSlot extends GuiSlot {
      public int field_148175_k;
      final GuiFlatPresets this$0;

      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
         this.field_148175_k = var1;
         this.this$0.func_146426_g();
         GuiFlatPresets.access$1(this.this$0).setText(((GuiFlatPresets.LayerItem)GuiFlatPresets.access$0().get(GuiFlatPresets.access$2(this.this$0).field_148175_k)).field_148233_c);
      }

      private void func_148173_e(int var1, int var2) {
         this.func_148171_c(var1, var2, 0, 0);
      }

      private void func_148171_c(int var1, int var2, int var3, int var4) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(Gui.statIcons);
         float var5 = 0.0078125F;
         float var6 = 0.0078125F;
         boolean var7 = true;
         boolean var8 = true;
         Tessellator var9 = Tessellator.getInstance();
         WorldRenderer var10 = var9.getWorldRenderer();
         var10.begin(7, DefaultVertexFormats.POSITION_TEX);
         var10.pos((double)(var1 + 0), (double)(var2 + 18), (double)GuiFlatPresets.zLevel).tex((double)((float)(var3 + 0) * 0.0078125F), (double)((float)(var4 + 18) * 0.0078125F)).endVertex();
         var10.pos((double)(var1 + 18), (double)(var2 + 18), (double)GuiFlatPresets.zLevel).tex((double)((float)(var3 + 18) * 0.0078125F), (double)((float)(var4 + 18) * 0.0078125F)).endVertex();
         var10.pos((double)(var1 + 18), (double)(var2 + 0), (double)GuiFlatPresets.zLevel).tex((double)((float)(var3 + 18) * 0.0078125F), (double)((float)(var4 + 0) * 0.0078125F)).endVertex();
         var10.pos((double)(var1 + 0), (double)(var2 + 0), (double)GuiFlatPresets.zLevel).tex((double)((float)(var3 + 0) * 0.0078125F), (double)((float)(var4 + 0) * 0.0078125F)).endVertex();
         var9.draw();
      }

      protected void drawBackground() {
      }

      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6) {
         GuiFlatPresets.LayerItem var7 = (GuiFlatPresets.LayerItem)GuiFlatPresets.access$0().get(var1);
         this.func_178054_a(var2, var3, var7.field_148234_a, var7.field_179037_b);
         this.this$0.fontRendererObj.drawString(var7.field_148232_b, (double)(var2 + 18 + 5), (double)(var3 + 6), 16777215);
      }

      protected int getSize() {
         return GuiFlatPresets.access$0().size();
      }

      private void func_178054_a(int var1, int var2, Item var3, int var4) {
         this.func_148173_e(var1 + 1, var2 + 1);
         GlStateManager.enableRescaleNormal();
         RenderHelper.enableGUIStandardItemLighting();
         this.this$0.itemRender.renderItemIntoGUI(new ItemStack(var3, 1, var4), var1 + 2, var2 + 2);
         RenderHelper.disableStandardItemLighting();
         GlStateManager.disableRescaleNormal();
      }

      public ListSlot(GuiFlatPresets var1) {
         super(var1.mc, GuiFlatPresets.width, GuiFlatPresets.height, 80, GuiFlatPresets.height - 37, 24);
         this.this$0 = var1;
         this.field_148175_k = -1;
      }

      protected boolean isSelected(int var1) {
         return var1 == this.field_148175_k;
      }
   }
}
