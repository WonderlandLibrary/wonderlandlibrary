package space.lunaclient.luna.impl.elements.render.hud.structure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import org.lwjgl.opengl.GL11;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.elements.render.hud.HUD;
import space.lunaclient.luna.impl.events.EventRender2D;
import space.lunaclient.luna.util.FontUtils;

public class Armor
{
  public Minecraft mc = Minecraft.getMinecraft();
  
  public Armor() {}
  
  @EventRegister
  public void onRender(EventRender2D e)
  {
    if (!HUD.armor.getValBoolean()) {
      return;
    }
    int itemX = e.getWidth() / 2 + 9;
    for (int i = 0; i < 5; i++)
    {
      ItemStack ia = Minecraft.thePlayer.getEquipmentInSlot(i);
      if (ia != null)
      {
        float oldZ = this.mc.getRenderItem().zLevel;
        GL11.glPushMatrix();
        GlStateManager.clear(256);
        GlStateManager.disableAlpha();
        RenderHelper.enableStandardItemLighting();
        this.mc.getRenderItem().zLevel = -100.0F;
        this.mc.getRenderItem().func_175042_a(ia, itemX, e.getHeight() - 55);
        this.mc.getRenderItem().func_175030_a(Minecraft.fontRendererObj, ia, itemX, e.getHeight() - 55);
        this.mc.getRenderItem().zLevel = oldZ;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GL11.glPopMatrix();
        if (((ia.getItem() instanceof ItemSword)) || ((ia.getItem() instanceof ItemTool)) || ((ia.getItem() instanceof ItemArmor)) || ((ia.getItem() instanceof ItemBow)))
        {
          GlStateManager.pushMatrix();
          int durability = ia.getMaxDamage() - ia.getItemDamage();
          int y = e.getHeight() - 60;
          GlStateManager.scale(0.5D, 0.5D, 0.5D);
          FontUtils.drawStringWithShadow(durability + "", (itemX + 4) / 0.5F, y / 0.5F, 16777215);
          GlStateManager.popMatrix();
        }
        itemX += 16;
      }
    }
  }
}
