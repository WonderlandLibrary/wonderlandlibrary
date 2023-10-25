package me.rhys.client.module.render;

import java.util.concurrent.ThreadLocalRandom;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.render.RenderNameTagEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.render.RenderUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Nametags extends Module {
  @Name("Health")
  public boolean health = false;
  
  public Nametags(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  void onRender(RenderNameTagEvent event) {
    EntityLivingBase entity = event.getEntity();
    if (!(entity instanceof net.minecraft.client.entity.EntityOtherPlayerMP))
      return; 
    EntityPlayer player = (EntityPlayer)entity;
    if (player.isInvisible())
      return; 
    String name = event.getName();
    FontRenderer fontrenderer = this.mc.fontRendererObj;
    float scale = 0.004F * player().getDistanceToEntity((Entity)player);
    GlStateManager.pushMatrix();
    GlStateManager.translate((float)event.getX() + 0.0F, (float)event.getY() + player.height + 0.5F, (float)event.getZ());
    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(-(this.mc.getRenderManager()).playerViewY, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate((this.mc.getRenderManager()).playerViewX, 1.0F, 0.0F, 0.0F);
    GlStateManager.scale(-scale, -scale, scale);
    GlStateManager.disableLighting();
    GlStateManager.depthMask(false);
    GlStateManager.disableDepth();
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    byte yOffset = 0;
    if (name.equals("deadmau5"))
      yOffset = -10; 
    int width = (int)(FontUtil.getStringWidth(name) / 2.0F);
    GlStateManager.disableTexture2D();
    float[] colors = RenderUtil.getColors(-2147483648);
    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
    worldrenderer.pos((-width - 2), (-1 + yOffset), 0.0D).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
    worldrenderer.pos((-width - 2), (9 + yOffset), 0.0D).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
    worldrenderer.pos((width + 1), (9 + yOffset), 0.0D).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
    worldrenderer.pos((width + 1), (-1 + yOffset), 0.0D).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
    tessellator.draw();
    GlStateManager.enableTexture2D();
    if (this.health) {
      try {
        GlStateManager.pushMatrix();
        this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
        int i = MathHelper.ceiling_float_int(player.getHealth());
        boolean flag = (player.hurtTime > 0 && player.hurtTime < 4 && ThreadLocalRandom.current().nextBoolean());
        IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.maxHealth);
        int i1 = -41;
        int k1 = -12;
        float f = (float)iattributeinstance.getAttributeValue();
        float f1 = player.getAbsorptionAmount();
        int l1 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10.0F);
        int i2 = Math.max(10 - l1 - 2, 3);
        float f2 = f1;
        int l2 = -1;
        for (int j5 = MathHelper.ceiling_float_int((f + f1) / 2.0F) - 1; j5 >= 0; j5--) {
          int k5 = 16;
          if (player.isPotionActive(Potion.poison)) {
            k5 += 36;
          } else if (player.isPotionActive(Potion.wither)) {
            k5 += 72;
          } 
          byte b0 = 0;
          if (flag)
            b0 = 1; 
          int k3 = MathHelper.ceiling_float_int((j5 + 1) / 10.0F) - 1;
          int l3 = i1 + j5 % 10 * 8;
          int i4 = k1 - k3 * i2;
          if (i <= 4)
            i4 += ThreadLocalRandom.current().nextInt(2); 
          byte b1 = 0;
          if (player.worldObj.getWorldInfo().isHardcoreModeEnabled())
            b1 = 5; 
          Gui.drawTexturedModalRect(l3, i4, 16 + b0 * 9, 9 * b1, 9, 9);
          if (flag) {
            if (j5 * 2 + 1 < 0)
              Gui.drawTexturedModalRect(l3, i4, k5 + 54, 9 * b1, 9, 9); 
            if (j5 * 2 + 1 == 0)
              Gui.drawTexturedModalRect(l3, i4, k5 + 63, 9 * b1, 9, 9); 
          } 
          if (f2 <= 0.0F) {
            if (j5 * 2 + 1 < i)
              Gui.drawTexturedModalRect(l3, i4, k5 + 36, 9 * b1, 9, 9); 
            if (j5 * 2 + 1 == i)
              Gui.drawTexturedModalRect(l3, i4, k5 + 45, 9 * b1, 9, 9); 
          } else {
            if (f2 == f1 && f1 % 2.0F == 1.0F) {
              Gui.drawTexturedModalRect(l3, i4, k5 + 153, 9 * b1, 9, 9);
            } else {
              Gui.drawTexturedModalRect(l3, i4, k5 + 144, 9 * b1, 9, 9);
            } 
            f2 -= 2.0F;
          } 
        } 
      } catch (Exception exception) {}
      GlStateManager.popMatrix();
    } 
    FontUtil.drawStringWithShadow(name, -FontUtil.getStringWidth(name) / 2.0F, yOffset, -1);
    GlStateManager.enableDepth();
    GlStateManager.depthMask(true);
    FontUtil.drawStringWithShadow(name, -FontUtil.getStringWidth(name) / 2.0F, yOffset, -1);
    GlStateManager.enableLighting();
    GlStateManager.disableBlend();
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.popMatrix();
    event.setCancelled(true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\render\Nametags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */