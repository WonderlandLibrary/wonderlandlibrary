package my.NewSnake.Tank.cosmetics.impl;

import my.NewSnake.Tank.cosmetics.Cosmetic;
import my.NewSnake.Tank.module.modules.PLAYER.Cosmetics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CosmeticGolem extends Cosmetic {
   public static final ResourceLocation TEXTURETOP = new ResourceLocation("client/Creep.png");
   CreepModel Creepermodel = new CreepModel();
   public static final ResourceLocation TEXTURE = new ResourceLocation("client/Golem.png");
   ModelIronGolem golemmodel = new ModelIronGolem();

   public void render(AbstractClientPlayer var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if ((new Cosmetics()).getInstance().isEnabled() && Cosmetics.Golem) {
         Minecraft.getMinecraft();
         if (var1 == Minecraft.thePlayer) {
            GlStateManager.pushMatrix();
            this.playerRenderer.bindTexture(TEXTURE);
            GlStateManager.translate(0.7D, -0.2D, 0.0D);
         } else {
            GlStateManager.translate(-0.7D, -0.2D, 0.0D);
         }

         if (var1.isSneaking()) {
            GlStateManager.translate(0.0D, 0.045D, 0.0D);
         }

         this.golemmodel.render(var1, var2, var3, var5, var7, var7, var8 / 4.0F);
         GL11.glColor3f(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }

      if (Cosmetics.Creep) {
         Minecraft.getMinecraft();
         if (var1 == Minecraft.thePlayer) {
            GlStateManager.pushMatrix();
            this.playerRenderer.bindTexture(TEXTURETOP);
            GlStateManager.translate(0.7D, -0.2D, 0.0D);
         } else {
            GlStateManager.translate(-0.7D, -0.2D, 0.0D);
         }

         if (var1.isSneaking()) {
            GlStateManager.translate(0.0D, 0.045D, 0.0D);
         }

         this.Creepermodel.render(var1, var2, var3, var5, var7, var7, var8 / 3.0F);
         GL11.glColor3f(1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }

   }

   public CosmeticGolem(RenderPlayer var1) {
      super(var1);
   }
}
