package net.minecraft.client.resources;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

public abstract class ResourcePackListEntry implements GuiListExtended.IGuiListEntry {
   private static final IChatComponent field_183022_f = new ChatComponentTranslation("resourcePack.incompatible.new", new Object[0]);
   private static final ResourceLocation RESOURCE_PACKS_TEXTURE = new ResourceLocation("textures/gui/resource_packs.png");
   private static final IChatComponent field_183020_d = new ChatComponentTranslation("resourcePack.incompatible", new Object[0]);
   private static final IChatComponent field_183021_e = new ChatComponentTranslation("resourcePack.incompatible.old", new Object[0]);
   protected final GuiScreenResourcePacks resourcePacksGUI;
   protected final Minecraft mc;

   public boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6) {
      if (this.func_148310_d() && var5 <= 32) {
         if (this != false) {
            this.resourcePacksGUI.markChanged();
            int var10 = this.func_183019_a();
            if (var10 != 1) {
               String var11 = I18n.format("resourcePack.incompatible.confirm.title");
               String var9 = I18n.format("resourcePack.incompatible.confirm." + (var10 > 1 ? "new" : "old"));
               this.mc.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback(this) {
                  final ResourcePackListEntry this$0;

                  {
                     this.this$0 = var1;
                  }

                  public void confirmClicked(boolean var1, int var2) {
                     List var3 = this.this$0.resourcePacksGUI.getListContaining(this.this$0);
                     this.this$0.mc.displayGuiScreen(this.this$0.resourcePacksGUI);
                     if (var1) {
                        var3.remove(this.this$0);
                        this.this$0.resourcePacksGUI.getSelectedResourcePacks().add(0, this.this$0);
                     }

                  }
               }, var11, var9, 0));
            } else {
               this.resourcePacksGUI.getListContaining(this).remove(this);
               this.resourcePacksGUI.getSelectedResourcePacks().add(0, this);
            }

            return true;
         }

         if (var5 < 16 && this.func_148308_f()) {
            this.resourcePacksGUI.getListContaining(this).remove(this);
            this.resourcePacksGUI.getAvailableResourcePacks().add(0, this);
            this.resourcePacksGUI.markChanged();
            return true;
         }

         List var7;
         int var8;
         if (var5 > 16 && var6 < 16 && this > 0) {
            var7 = this.resourcePacksGUI.getListContaining(this);
            var8 = var7.indexOf(this);
            var7.remove(this);
            var7.add(var8 - 1, this);
            this.resourcePacksGUI.markChanged();
            return true;
         }

         if (var5 > 16 && var6 > 16 && this != false) {
            var7 = this.resourcePacksGUI.getListContaining(this);
            var8 = var7.indexOf(this);
            var7.remove(this);
            var7.add(var8 + 1, this);
            this.resourcePacksGUI.markChanged();
            return true;
         }
      }

      return false;
   }

   public void setSelected(int var1, int var2, int var3) {
   }

   protected abstract String func_148312_b();

   protected abstract int func_183019_a();

   protected boolean func_148308_f() {
      return this.resourcePacksGUI.hasResourcePackEntry(this);
   }

   protected abstract void func_148313_c();

   public ResourcePackListEntry(GuiScreenResourcePacks var1) {
      this.resourcePacksGUI = var1;
      this.mc = Minecraft.getMinecraft();
   }

   public void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6) {
   }

   public void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8) {
      int var9 = this.func_183019_a();
      if (var9 != 1) {
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         Gui.drawRect((double)(var2 - 1), (double)(var3 - 1), (double)(var2 + var4 - 9), (double)(var3 + var5 + 1), -8978432);
      }

      this.func_148313_c();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 0.0F, 0.0F, 32.0D, 32.0D, 32.0D, 32.0D);
      String var10 = this.func_148312_b();
      String var11 = this.func_148311_a();
      int var12;
      if ((this.mc.gameSettings.touchscreen || var8) && this.func_148310_d()) {
         this.mc.getTextureManager().bindTexture(RESOURCE_PACKS_TEXTURE);
         Gui.drawRect((double)var2, (double)var3, (double)(var2 + 32), (double)(var3 + 32), -1601138544);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         var12 = var6 - var2;
         int var13 = var7 - var3;
         if (var9 < 1) {
            var10 = field_183020_d.getFormattedText();
            var11 = field_183021_e.getFormattedText();
         } else if (var9 > 1) {
            var10 = field_183020_d.getFormattedText();
            var11 = field_183022_f.getFormattedText();
         }

         if (this != false) {
            if (var12 < 32) {
               Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 0.0F, 32.0F, 32.0D, 32.0D, 256.0D, 256.0D);
            } else {
               Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 0.0F, 0.0F, 32.0D, 32.0D, 256.0D, 256.0D);
            }
         } else {
            if (this.func_148308_f()) {
               if (var12 < 16) {
                  Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 32.0F, 32.0F, 32.0D, 32.0D, 256.0D, 256.0D);
               } else {
                  Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 32.0F, 0.0F, 32.0D, 32.0D, 256.0D, 256.0D);
               }
            }

            if (this > 0) {
               if (var12 < 32 && var12 > 16 && var13 < 16) {
                  Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 96.0F, 32.0F, 32.0D, 32.0D, 256.0D, 256.0D);
               } else {
                  Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 96.0F, 0.0F, 32.0D, 32.0D, 256.0D, 256.0D);
               }
            }

            if (this >= 0) {
               if (var12 < 32 && var12 > 16 && var13 > 16) {
                  Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 64.0F, 32.0F, 32.0D, 32.0D, 256.0D, 256.0D);
               } else {
                  Gui.drawModalRectWithCustomSizedTexture((double)var2, (double)var3, 64.0F, 0.0F, 32.0D, 32.0D, 256.0D, 256.0D);
               }
            }
         }
      }

      var12 = Minecraft.fontRendererObj.getStringWidth(var10);
      if (var12 > 157) {
         var10 = Minecraft.fontRendererObj.trimStringToWidth(var10, 157 - Minecraft.fontRendererObj.getStringWidth("...")) + "...";
      }

      Minecraft.fontRendererObj.drawStringWithShadow(var10, (float)(var2 + 32 + 2), (float)(var3 + 1), 16777215);
      List var15 = Minecraft.fontRendererObj.listFormattedStringToWidth(var11, 157);

      for(int var14 = 0; var14 < 2 && var14 < var15.size(); ++var14) {
         Minecraft.fontRendererObj.drawStringWithShadow((String)var15.get(var14), (float)(var2 + 32 + 2), (float)(var3 + 12 + 10 * var14), 8421504);
      }

   }

   protected abstract String func_148311_a();

   protected boolean func_148310_d() {
      return true;
   }
}
