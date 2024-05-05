package space.lunaclient.luna.impl.elements.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.impl.events.EventRender3D;
import space.lunaclient.luna.impl.managers.FriendManager;
import space.lunaclient.luna.util.GLUtil;
import space.lunaclient.luna.util.RenderUtils;

@ElementInfo(name="NameTags", category=Category.RENDER, description="Makes nametags bigger.")
public class NameTags
  extends Element
{
  public boolean formatting = true;
  
  public NameTags() {}
  
  @EventRegister
  public void onUpdate(EventRender3D event)
  {
    for (Object o : Minecraft.theWorld.playerEntities)
    {
      EntityPlayer p = (EntityPlayer)o;
      if ((p != mc.func_175606_aa()) && (p.isEntityAlive()))
      {
        double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * mc.timer.renderPartialTicks - RenderManager.renderPosX;
        
        double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * mc.timer.renderPartialTicks - RenderManager.renderPosY;
        
        double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * mc.timer.renderPartialTicks - RenderManager.renderPosZ;
        
        renderNameTag(p, this.formatting ? p.getDisplayName().getFormattedText() : p.getName(), pX, pY, pZ);
      }
    }
  }
  
  @EventRegister
  private void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ)
  {
    FontRenderer var12 = Minecraft.fontRendererObj;
    pY += (entity.isSneaking() ? 0.5D : 0.7D);
    float var13 = Minecraft.thePlayer.getDistanceToEntity(entity) / 4.0F;
    if (var13 < 1.6F) {
      var13 = 1.6F;
    }
    int colour = 16777215;
    if (!this.formatting) {
      tag = EnumChatFormatting.getTextWithoutFormattingCodes(tag);
    }
    if (FriendManager.isFriend(entity.getName())) {
      colour = -16133175;
    } else if (entity.isInvisible()) {
      colour = 16756480;
    } else if (entity.isSneaking()) {
      colour = 11468900;
    }
    tag = entity.getName();
    
    double health = Math.ceil(entity.getHealth() + entity.getAbsorptionAmount()) / 2.0D;
    EnumChatFormatting healthCol;
    EnumChatFormatting healthCol;
    if (health < 5.0D)
    {
      healthCol = EnumChatFormatting.DARK_RED;
    }
    else
    {
      EnumChatFormatting healthCol;
      if ((health > 5.0D) && (health < 6.5D)) {
        healthCol = EnumChatFormatting.GOLD;
      } else {
        healthCol = EnumChatFormatting.DARK_GREEN;
      }
    }
    if (Math.floor(health) == health) {
      tag = tag + " " + healthCol + (int)Math.floor(health);
    } else {
      tag = tag + " " + healthCol + health;
    }
    float scale = var13 * 2.0F;
    scale /= 100.0F;
    GL11.glPushMatrix();
    GL11.glTranslatef((float)pX, (float)pY + 1.4F, (float)pZ);
    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
    GL11.glRotatef(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
    GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
    GL11.glScalef(-scale, -scale, scale);
    GLUtil.setGLCap(2896, false);
    GLUtil.setGLCap(2929, false);
    int width = Minecraft.fontRendererObj.getStringWidth(tag) / 2;
    GLUtil.setGLCap(3042, true);
    GL11.glBlendFunc(770, 771);
    
    RenderUtils.drawBorderedRectNameTag(-width - 2, -(Minecraft.fontRendererObj.FONT_HEIGHT + 1), width + 2, 2.0F, 1.0F, -16777216, 1593835520);
    
    var12.func_175065_a(tag, -width, -(Minecraft.fontRendererObj.FONT_HEIGHT - 1), colour, true);
    GL11.glPushMatrix();
    int xOffset = 0;
    ItemStack[] arrayOfItemStack1 = entity.inventory.armorInventory;int i = arrayOfItemStack1.length;
    for (ItemStack localItemStack1 = 0; localItemStack1 < i; localItemStack1++)
    {
      armourStack = arrayOfItemStack1[localItemStack1];
      if (armourStack != null) {
        xOffset -= 8;
      }
    }
    if (entity.getHeldItem() != null)
    {
      xOffset -= 8;
      ItemStack renderStack = entity.getHeldItem().copy();
      if ((renderStack.hasEffect()) && (
        ((renderStack.getItem() instanceof ItemTool)) || 
        ((renderStack.getItem() instanceof ItemArmor)))) {
        renderStack.stackSize = 1;
      }
      renderItemStack(renderStack, xOffset);
      xOffset += 16;
    }
    ItemStack[] arrayOfItemStack2 = entity.inventory.armorInventory;localItemStack1 = arrayOfItemStack2.length;
    for (ItemStack armourStack = 0; armourStack < localItemStack1; armourStack++)
    {
      ItemStack armourStack = arrayOfItemStack2[armourStack];
      if (armourStack != null)
      {
        ItemStack renderStack1 = armourStack.copy();
        if ((renderStack1.hasEffect()) && (((renderStack1.getItem() instanceof ItemTool)) || 
          ((renderStack1.getItem() instanceof ItemArmor)))) {
          renderStack1.stackSize = 1;
        }
        renderItemStack(renderStack1, xOffset);
        xOffset += 16;
      }
    }
    GL11.glPopMatrix();
    GLUtil.revertAllCaps();
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glPopMatrix();
  }
  
  @EventRegister
  private void renderItemStack(ItemStack stack, int x)
  {
    GL11.glPushMatrix();
    GL11.glDepthMask(true);
    GlStateManager.clear(256);
    RenderHelper.enableStandardItemLighting();
    mc.getRenderItem().zLevel = -150.0F;
    whatTheFuckOpenGLThisFixesItemGlint();
    mc.getRenderItem().func_180450_b(stack, x, -26);
    mc.getRenderItem().func_175030_a(Minecraft.fontRendererObj, stack, x, -26);
    mc.getRenderItem().zLevel = 0.0F;
    RenderHelper.disableStandardItemLighting();
    GlStateManager.disableCull();
    GlStateManager.enableAlpha();
    GlStateManager.disableBlend();
    GlStateManager.disableLighting();
    GlStateManager.scale(0.5D, 0.5D, 0.5D);
    GlStateManager.disableDepth();
    renderEnchantText(stack, x);
    GlStateManager.enableDepth();
    GlStateManager.scale(2.0F, 2.0F, 2.0F);
    GL11.glPopMatrix();
  }
  
  @EventRegister
  private void renderEnchantText(ItemStack stack, int x)
  {
    int encY = -50;
    if ((stack.getItem() instanceof ItemArmor))
    {
      int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack);
      int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
      int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
      if (pLevel > 0)
      {
        Minecraft.fontRendererObj.drawString("p" + pLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (tLevel > 0)
      {
        Minecraft.fontRendererObj.drawString("t" + tLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (uLevel > 0)
      {
        Minecraft.fontRendererObj.drawString("u" + uLevel, x * 2, encY, 16777215);
        encY += 7;
      }
    }
    if ((stack.getItem() instanceof ItemBow))
    {
      int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
      int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
      int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
      int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
      if (sLevel > 0)
      {
        Minecraft.fontRendererObj.drawString("d" + sLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (kLevel > 0)
      {
        Minecraft.fontRendererObj.drawString("k" + kLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (fLevel > 0)
      {
        Minecraft.fontRendererObj.drawString("f" + fLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (uLevel > 0)
      {
        Minecraft.fontRendererObj.drawString("u" + uLevel, x * 2, encY, 16777215);
        encY += 7;
      }
    }
    if ((stack.getItem() instanceof ItemSword))
    {
      int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, stack);
      int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId, stack);
      int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
      int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
      if (sLevel > 0)
      {
        Minecraft.fontRendererObj.drawString("s" + sLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (kLevel > 0)
      {
        Minecraft.fontRendererObj.drawString("k" + kLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (fLevel > 0)
      {
        Minecraft.fontRendererObj.drawString("f" + fLevel, x * 2, encY, 16777215);
        encY += 7;
      }
      if (uLevel > 0) {
        Minecraft.fontRendererObj.drawString("u" + uLevel, x * 2, encY, 16777215);
      }
    }
  }
  
  private void whatTheFuckOpenGLThisFixesItemGlint()
  {
    GlStateManager.disableLighting();
    GlStateManager.disableDepth();
    GlStateManager.disableBlend();
    GlStateManager.enableLighting();
    GlStateManager.enableDepth();
    GlStateManager.disableLighting();
    GlStateManager.disableDepth();
    GlStateManager.func_179090_x();
    GlStateManager.disableAlpha();
    GlStateManager.disableBlend();
    GlStateManager.enableBlend();
    GlStateManager.enableAlpha();
    GlStateManager.func_179098_w();
    GlStateManager.enableLighting();
    GlStateManager.enableDepth();
  }
  
  public void onEnable()
  {
    space.lunaclient.luna.impl.events.EventRenderNameTag.cancel = true;
    super.onEnable();
  }
  
  public void onDisable()
  {
    space.lunaclient.luna.impl.events.EventRenderNameTag.cancel = false;
    super.onDisable();
  }
}
