package net.minecraft.client.resources;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public abstract class ResourcePackListEntry
  implements GuiListExtended.IGuiListEntry
{
  private static final ResourceLocation field_148316_c = new ResourceLocation("textures/gui/resource_packs.png");
  protected final Minecraft field_148317_a;
  protected final GuiScreenResourcePacks field_148315_b;
  private static final String __OBFID = "CL_00000821";
  
  public ResourcePackListEntry(GuiScreenResourcePacks p_i45051_1_)
  {
    this.field_148315_b = p_i45051_1_;
    this.field_148317_a = Minecraft.getMinecraft();
  }
  
  public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
  {
    func_148313_c();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
    if (((this.field_148317_a.gameSettings.touchscreen) || (isSelected)) && (func_148310_d()))
    {
      this.field_148317_a.getTextureManager().bindTexture(field_148316_c);
      Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      int var9 = mouseX - x;
      int var10 = mouseY - y;
      if (func_148309_e())
      {
        if (var9 < 32) {
          Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 32.0F, 32, 32, 256.0F, 256.0F);
        } else {
          Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, 32, 32, 256.0F, 256.0F);
        }
      }
      else
      {
        if (func_148308_f()) {
          if (var9 < 16) {
            Gui.drawModalRectWithCustomSizedTexture(x, y, 32.0F, 32.0F, 32, 32, 256.0F, 256.0F);
          } else {
            Gui.drawModalRectWithCustomSizedTexture(x, y, 32.0F, 0.0F, 32, 32, 256.0F, 256.0F);
          }
        }
        if (func_148314_g()) {
          if ((var9 < 32) && (var9 > 16) && (var10 < 16)) {
            Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 32.0F, 32, 32, 256.0F, 256.0F);
          } else {
            Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0F, 0.0F, 32, 32, 256.0F, 256.0F);
          }
        }
        if (func_148307_h()) {
          if ((var9 < 32) && (var9 > 16) && (var10 > 16)) {
            Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 32.0F, 32, 32, 256.0F, 256.0F);
          } else {
            Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0F, 0.0F, 32, 32, 256.0F, 256.0F);
          }
        }
      }
    }
    String var13 = func_148312_b();
    int var10 = Minecraft.fontRendererObj.getStringWidth(var13);
    if (var10 > 157) {
      var13 = Minecraft.fontRendererObj.trimStringToWidth(var13, 157 - Minecraft.fontRendererObj.getStringWidth("...")) + "...";
    }
    Minecraft.fontRendererObj.func_175063_a(var13, x + 32 + 2, y + 1, 16777215);
    List var11 = Minecraft.fontRendererObj.listFormattedStringToWidth(func_148311_a(), 157);
    for (int var12 = 0; (var12 < 2) && (var12 < var11.size()); var12++) {
      Minecraft.fontRendererObj.func_175063_a((String)var11.get(var12), x + 32 + 2, y + 12 + 10 * var12, 8421504);
    }
  }
  
  protected abstract String func_148311_a();
  
  protected abstract String func_148312_b();
  
  protected abstract void func_148313_c();
  
  protected boolean func_148310_d()
  {
    return true;
  }
  
  protected boolean func_148309_e()
  {
    return !this.field_148315_b.hasResourcePackEntry(this);
  }
  
  protected boolean func_148308_f()
  {
    return this.field_148315_b.hasResourcePackEntry(this);
  }
  
  protected boolean func_148314_g()
  {
    List var1 = this.field_148315_b.func_146962_b(this);
    int var2 = var1.indexOf(this);
    return (var2 > 0) && (((ResourcePackListEntry)var1.get(var2 - 1)).func_148310_d());
  }
  
  protected boolean func_148307_h()
  {
    List var1 = this.field_148315_b.func_146962_b(this);
    int var2 = var1.indexOf(this);
    return (var2 >= 0) && (var2 < var1.size() - 1) && (((ResourcePackListEntry)var1.get(var2 + 1)).func_148310_d());
  }
  
  public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
  {
    if ((func_148310_d()) && (p_148278_5_ <= 32))
    {
      if (func_148309_e())
      {
        this.field_148315_b.func_146962_b(this).remove(this);
        this.field_148315_b.func_146963_h().add(0, this);
        this.field_148315_b.func_175288_g();
        return true;
      }
      if ((p_148278_5_ < 16) && (func_148308_f()))
      {
        this.field_148315_b.func_146962_b(this).remove(this);
        this.field_148315_b.func_146964_g().add(0, this);
        this.field_148315_b.func_175288_g();
        return true;
      }
      if ((p_148278_5_ > 16) && (p_148278_6_ < 16) && (func_148314_g()))
      {
        List var7 = this.field_148315_b.func_146962_b(this);
        int var8 = var7.indexOf(this);
        var7.remove(this);
        var7.add(var8 - 1, this);
        this.field_148315_b.func_175288_g();
        return true;
      }
      if ((p_148278_5_ > 16) && (p_148278_6_ > 16) && (func_148307_h()))
      {
        List var7 = this.field_148315_b.func_146962_b(this);
        int var8 = var7.indexOf(this);
        var7.remove(this);
        var7.add(var8 + 1, this);
        this.field_148315_b.func_175288_g();
        return true;
      }
    }
    return false;
  }
  
  public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}
  
  public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}
}
