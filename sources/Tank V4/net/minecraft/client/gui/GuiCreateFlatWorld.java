package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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

public class GuiCreateFlatWorld extends GuiScreen {
   private String field_146391_r;
   private FlatGeneratorInfo theFlatGeneratorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
   private final GuiCreateWorld createWorldGui;
   private String field_146394_i;
   private GuiButton field_146386_v;
   private String flatWorldTitle;
   private GuiCreateFlatWorld.Details createFlatWorldListSlotGui;
   private GuiButton field_146388_u;
   private GuiButton field_146389_t;

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.createFlatWorldListSlotGui.drawScreen(var1, var2, var3);
      this.drawCenteredString(this.fontRendererObj, this.flatWorldTitle, width / 2, 8, 16777215);
      int var4 = width / 2 - 92 - 16;
      this.drawString(this.fontRendererObj, this.field_146394_i, var4, 32, 16777215);
      this.drawString(this.fontRendererObj, this.field_146391_r, var4 + 2 + 213 - this.fontRendererObj.getStringWidth(this.field_146391_r), 32, 16777215);
      super.drawScreen(var1, var2, var3);
   }

   public GuiCreateFlatWorld(GuiCreateWorld var1, String var2) {
      this.createWorldGui = var1;
      this.func_146383_a(var2);
   }

   static FlatGeneratorInfo access$0(GuiCreateFlatWorld var0) {
      return var0.theFlatGeneratorInfo;
   }

   public void func_146383_a(String var1) {
      this.theFlatGeneratorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(var1);
   }

   public void initGui() {
      this.buttonList.clear();
      this.flatWorldTitle = I18n.format("createWorld.customize.flat.title");
      this.field_146394_i = I18n.format("createWorld.customize.flat.tile");
      this.field_146391_r = I18n.format("createWorld.customize.flat.height");
      this.createFlatWorldListSlotGui = new GuiCreateFlatWorld.Details(this);
      this.buttonList.add(this.field_146389_t = new GuiButton(2, width / 2 - 154, height - 52, 100, 20, I18n.format("createWorld.customize.flat.addLayer") + " (NYI)"));
      this.buttonList.add(this.field_146388_u = new GuiButton(3, width / 2 - 50, height - 52, 100, 20, I18n.format("createWorld.customize.flat.editLayer") + " (NYI)"));
      this.buttonList.add(this.field_146386_v = new GuiButton(4, width / 2 - 155, height - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer")));
      this.buttonList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.format("gui.done")));
      this.buttonList.add(new GuiButton(5, width / 2 + 5, height - 52, 150, 20, I18n.format("createWorld.customize.presets")));
      this.buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.format("gui.cancel")));
      this.field_146389_t.visible = this.field_146388_u.visible = false;
      this.theFlatGeneratorInfo.func_82645_d();
      this.func_146375_g();
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.createFlatWorldListSlotGui.handleMouseInput();
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      int var2 = this.theFlatGeneratorInfo.getFlatLayers().size() - this.createFlatWorldListSlotGui.field_148228_k - 1;
      if (var1.id == 1) {
         this.mc.displayGuiScreen(this.createWorldGui);
      } else if (var1.id == 0) {
         this.createWorldGui.chunkProviderSettingsJson = this.func_146384_e();
         this.mc.displayGuiScreen(this.createWorldGui);
      } else if (var1.id == 5) {
         this.mc.displayGuiScreen(new GuiFlatPresets(this));
      } else if (var1.id == 4 && this != false) {
         this.theFlatGeneratorInfo.getFlatLayers().remove(var2);
         this.createFlatWorldListSlotGui.field_148228_k = Math.min(this.createFlatWorldListSlotGui.field_148228_k, this.theFlatGeneratorInfo.getFlatLayers().size() - 1);
      }

      this.theFlatGeneratorInfo.func_82645_d();
      this.func_146375_g();
   }

   public String func_146384_e() {
      return this.theFlatGeneratorInfo.toString();
   }

   public void func_146375_g() {
      boolean var1 = this.func_146382_i();
      this.field_146386_v.enabled = var1;
      this.field_146388_u.enabled = var1;
      this.field_146388_u.enabled = false;
      this.field_146389_t.enabled = false;
   }

   class Details extends GuiSlot {
      public int field_148228_k;
      final GuiCreateFlatWorld this$0;

      private void func_148226_e(int var1, int var2) {
         this.func_148224_c(var1, var2, 0, 0);
      }

      private void func_148224_c(int var1, int var2, int var3, int var4) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         this.mc.getTextureManager().bindTexture(Gui.statIcons);
         float var5 = 0.0078125F;
         float var6 = 0.0078125F;
         boolean var7 = true;
         boolean var8 = true;
         Tessellator var9 = Tessellator.getInstance();
         WorldRenderer var10 = var9.getWorldRenderer();
         var10.begin(7, DefaultVertexFormats.POSITION_TEX);
         var10.pos((double)(var1 + 0), (double)(var2 + 18), (double)GuiCreateFlatWorld.zLevel).tex((double)((float)(var3 + 0) * 0.0078125F), (double)((float)(var4 + 18) * 0.0078125F)).endVertex();
         var10.pos((double)(var1 + 18), (double)(var2 + 18), (double)GuiCreateFlatWorld.zLevel).tex((double)((float)(var3 + 18) * 0.0078125F), (double)((float)(var4 + 18) * 0.0078125F)).endVertex();
         var10.pos((double)(var1 + 18), (double)(var2 + 0), (double)GuiCreateFlatWorld.zLevel).tex((double)((float)(var3 + 18) * 0.0078125F), (double)((float)(var4 + 0) * 0.0078125F)).endVertex();
         var10.pos((double)(var1 + 0), (double)(var2 + 0), (double)GuiCreateFlatWorld.zLevel).tex((double)((float)(var3 + 0) * 0.0078125F), (double)((float)(var4 + 0) * 0.0078125F)).endVertex();
         var9.draw();
      }

      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
         this.field_148228_k = var1;
         this.this$0.func_146375_g();
      }

      public Details(GuiCreateFlatWorld var1) {
         super(var1.mc, GuiCreateFlatWorld.width, GuiCreateFlatWorld.height, 43, GuiCreateFlatWorld.height - 60, 24);
         this.this$0 = var1;
         this.field_148228_k = -1;
      }

      protected boolean isSelected(int var1) {
         return var1 == this.field_148228_k;
      }

      private void func_148225_a(int var1, int var2, ItemStack var3) {
         this.func_148226_e(var1 + 1, var2 + 1);
         GlStateManager.enableRescaleNormal();
         if (var3 != null && var3.getItem() != null) {
            RenderHelper.enableGUIStandardItemLighting();
            this.this$0.itemRender.renderItemIntoGUI(var3, var1 + 2, var2 + 2);
            RenderHelper.disableStandardItemLighting();
         }

         GlStateManager.disableRescaleNormal();
      }

      protected void drawBackground() {
      }

      protected int getSize() {
         return GuiCreateFlatWorld.access$0(this.this$0).getFlatLayers().size();
      }

      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6) {
         FlatLayerInfo var7 = (FlatLayerInfo)GuiCreateFlatWorld.access$0(this.this$0).getFlatLayers().get(GuiCreateFlatWorld.access$0(this.this$0).getFlatLayers().size() - var1 - 1);
         IBlockState var8 = var7.func_175900_c();
         Block var9 = var8.getBlock();
         Item var10 = Item.getItemFromBlock(var9);
         ItemStack var11 = var9 != Blocks.air && var10 != null ? new ItemStack(var10, 1, var9.getMetaFromState(var8)) : null;
         String var12 = var11 == null ? "Air" : var10.getItemStackDisplayName(var11);
         if (var10 == null) {
            if (var9 != Blocks.water && var9 != Blocks.flowing_water) {
               if (var9 == Blocks.lava || var9 == Blocks.flowing_lava) {
                  var10 = Items.lava_bucket;
               }
            } else {
               var10 = Items.water_bucket;
            }

            if (var10 != null) {
               var11 = new ItemStack(var10, 1, var9.getMetaFromState(var8));
               var12 = var9.getLocalizedName();
            }
         }

         this.func_148225_a(var2, var3, var11);
         this.this$0.fontRendererObj.drawString(var12, (double)(var2 + 18 + 5), (double)(var3 + 3), 16777215);
         String var13;
         if (var1 == 0) {
            var13 = I18n.format("createWorld.customize.flat.layer.top", var7.getLayerCount());
         } else if (var1 == GuiCreateFlatWorld.access$0(this.this$0).getFlatLayers().size() - 1) {
            var13 = I18n.format("createWorld.customize.flat.layer.bottom", var7.getLayerCount());
         } else {
            var13 = I18n.format("createWorld.customize.flat.layer", var7.getLayerCount());
         }

         this.this$0.fontRendererObj.drawString(var13, (double)(var2 + 2 + 213 - this.this$0.fontRendererObj.getStringWidth(var13)), (double)(var3 + 3), 16777215);
      }

      protected int getScrollBarX() {
         return this.width - 70;
      }
   }
}
