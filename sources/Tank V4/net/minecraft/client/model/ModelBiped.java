package net.minecraft.client.model;

import java.util.HashMap;
import my.NewSnake.Tank.friend.FriendManager;
import my.NewSnake.Tank.module.modules.PLAYER.Cosmetics;
import my.NewSnake.Tank.module.modules.PLAYER.Wings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ModelBiped extends ModelBase {
   private static final ResourceLocation enderDragonTextures = new ResourceLocation("textures/entity/enderdragon/dragon.png");
   public ModelRenderer bipedRightLeg;
   public int heldItemLeft;
   public ModelRenderer bipedHead;
   public ModelRenderer bipedLeftLeg;
   public ModelRenderer bipedHeadwear;
   public boolean aimedBow;
   public ModelRenderer bipedLeftArm;
   private ModelRenderer wing;
   private ModelRenderer wingTip;
   public ModelRenderer bipedRightArm;
   public int heldItemRight;
   public ModelRenderer bipedBody;
   HashMap wingState;
   public boolean isSneak;

   public ModelBiped() {
      this(0.0F);
   }

   public ModelBiped(float var1) {
      this(var1, 0.0F, 64, 32);
   }

   public ModelBiped(float var1, float var2, int var3, int var4) {
      this.wingState = new HashMap();
      this.setTextureOffset("wingtip.bone", 112, 136);
      this.setTextureOffset("wing.skin", -56, 88);
      this.setTextureOffset("wing.bone", 112, 88);
      this.setTextureOffset("wingtip.skin", -56, 144);
      this.textureWidth = 256;
      this.textureHeight = 256;
      this.wing = new ModelRenderer(this, "wing");
      this.wing.setRotationPoint(-12.0F, 5.0F, 2.0F);
      this.wing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
      this.wing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 1, 56);
      this.wingTip = new ModelRenderer(this, "wingtip");
      this.wingTip.setRotationPoint(-56.0F, 0.0F, 0.0F);
      this.wingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
      this.wingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 1, 56);
      this.wing.addChild(this.wingTip);
      this.textureWidth = var3;
      this.textureHeight = var4;
      this.bipedHead = new ModelRenderer(this, 0, 0);
      this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, var1);
      this.bipedHead.setRotationPoint(0.0F, 0.0F + var2, 0.0F);
      this.bipedHeadwear = new ModelRenderer(this, 32, 0);
      this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, var1 + 0.5F);
      this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + var2, 0.0F);
      this.bipedBody = new ModelRenderer(this, 16, 16);
      this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, var1);
      this.bipedBody.setRotationPoint(0.0F, 0.0F + var2, 0.0F);
      this.bipedRightArm = new ModelRenderer(this, 40, 16);
      this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, var1);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + var2, 0.0F);
      this.bipedLeftArm = new ModelRenderer(this, 40, 16);
      this.bipedLeftArm.mirror = true;
      this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, var1);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + var2, 0.0F);
      this.bipedRightLeg = new ModelRenderer(this, 0, 16);
      this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, var1);
      this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + var2, 0.0F);
      this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
      this.bipedLeftLeg.mirror = true;
      this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, var1);
      this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + var2, 0.0F);
   }

   public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7) {
      this.bipedHead.rotateAngleY = var4 / 57.295776F;
      this.bipedHead.rotateAngleX = var5 / 57.295776F;
      this.bipedRightArm.rotateAngleX = MathHelper.cos(var1 * 0.6662F + 3.1415927F) * 2.0F * var2 * 0.5F;
      this.bipedLeftArm.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 2.0F * var2 * 0.5F;
      this.bipedRightArm.rotateAngleZ = 0.0F;
      this.bipedLeftArm.rotateAngleZ = 0.0F;
      this.bipedRightLeg.rotateAngleX = MathHelper.cos(var1 * 0.6662F) * 1.4F * var2;
      this.bipedLeftLeg.rotateAngleX = MathHelper.cos(var1 * 0.6662F + 3.1415927F) * 1.4F * var2;
      this.bipedRightLeg.rotateAngleY = 0.0F;
      this.bipedLeftLeg.rotateAngleY = 0.0F;
      ModelRenderer var10000;
      if (this.isRiding) {
         var10000 = this.bipedRightArm;
         var10000.rotateAngleX += -0.62831855F;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleX += -0.62831855F;
         this.bipedRightLeg.rotateAngleX = -1.2566371F;
         this.bipedLeftLeg.rotateAngleX = -1.2566371F;
         this.bipedRightLeg.rotateAngleY = 0.31415927F;
         this.bipedLeftLeg.rotateAngleY = -0.31415927F;
      }

      if (this.heldItemLeft != 0) {
         this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.31415927F * (float)this.heldItemLeft;
      }

      this.bipedRightArm.rotateAngleY = 0.0F;
      this.bipedRightArm.rotateAngleZ = 0.0F;
      switch(this.heldItemRight) {
      case 0:
      case 2:
      default:
         break;
      case 1:
         this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.31415927F * (float)this.heldItemRight;
         break;
      case 3:
         this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.31415927F * (float)this.heldItemRight;
         this.bipedRightArm.rotateAngleY = -0.5235988F;
      }

      if ((new Cosmetics()).getInstance().isEnabled() && Cosmetics.ThugLife) {
         Minecraft.getMinecraft();
         if (var7.equals(Minecraft.thePlayer) && Minecraft.getMinecraft().gameSettings.thirdPersonView > 0) {
            this.bipedHead.rotateAngleX = 0.5F;
            this.bipedHead.rotateAngleY = 1.0F;
            this.bipedRightArm.rotateAngleX = 4.5F;
            this.bipedRightArm.rotateAngleY = -1.25F;
            this.bipedLeftArm.rotateAngleX = 4.5F;
            this.bipedLeftArm.rotateAngleY = -1.25F;
         }
      }

      if ((new Cosmetics()).getInstance().isEnabled()) {
         if (Cosmetics.Correndo) {
            Minecraft.getMinecraft();
            if (var7.equals(Minecraft.thePlayer) && Minecraft.getMinecraft().gameSettings.thirdPersonView > 0) {
               this.bipedLeftArm.rotateAngleX = 1.0F;
               this.bipedRightArm.rotateAngleX = 1.0F;
            }
         }

         if (Cosmetics.DERP) {
            Minecraft.getMinecraft();
            if (var7.equals(Minecraft.thePlayer) && Minecraft.getMinecraft().gameSettings.thirdPersonView > 0) {
               this.bipedHead.rotateAngleX = 16.0F;
            }
         }

         if (Cosmetics.Saci) {
            Minecraft.getMinecraft();
            if (var7.equals(Minecraft.thePlayer) && Minecraft.getMinecraft().gameSettings.thirdPersonView > 0) {
               this.bipedRightLeg.rotateAngleX = 22.0F;
            }
         }

         if (Cosmetics.Zumbi) {
            Minecraft.getMinecraft();
            if (var7.equals(Minecraft.thePlayer) && Minecraft.getMinecraft().gameSettings.thirdPersonView > 0) {
               this.bipedHead.rotateAngleX = 0.0F;
               this.bipedHead.rotateAngleY = 0.0F;
               this.bipedRightArm.rotateAngleX = 10.75F;
               this.bipedRightArm.rotateAngleY = -0.0F;
               this.bipedLeftArm.rotateAngleX = 10.75F;
               this.bipedLeftArm.rotateAngleY = -0.0F;
            }
         }

         if (Cosmetics.EnShock) {
            Minecraft.getMinecraft();
            if (var7.equals(Minecraft.thePlayer) && Minecraft.getMinecraft().gameSettings.thirdPersonView > 0) {
               this.bipedHead.rotateAngleX = 0.0F;
               this.bipedHead.rotateAngleY = 0.0F;
               this.bipedRightArm.rotateAngleX = -40.75F;
               this.bipedRightArm.rotateAngleY = -0.0F;
               this.bipedLeftArm.rotateAngleX = -40.75F;
               this.bipedLeftArm.rotateAngleY = -0.0F;
            }
         }
      }

      if ((new Cosmetics()).getInstance().isEnabled() && Cosmetics.Fantasminha) {
         Minecraft.getMinecraft();
         if (var7.equals(Minecraft.thePlayer) && Minecraft.getMinecraft().gameSettings.thirdPersonView > 0) {
            this.bipedLeftArm.rotateAngleX = -1.0F;
            this.bipedRightArm.rotateAngleX = -1.0F;
            this.bipedLeftLeg.rotateAngleX = 0.5F;
            this.bipedRightLeg.rotateAngleX = 0.5F;
         }
      }

      this.bipedLeftArm.rotateAngleY = 0.0F;
      float var8;
      float var9;
      if (this.swingProgress > -9990.0F) {
         var8 = this.swingProgress;
         this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var8) * 3.1415927F * 2.0F) * 0.2F;
         this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
         var10000 = this.bipedRightArm;
         var10000.rotateAngleY += this.bipedBody.rotateAngleY;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleY += this.bipedBody.rotateAngleY;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleX += this.bipedBody.rotateAngleY;
         var8 = 1.0F - this.swingProgress;
         var8 *= var8;
         var8 *= var8;
         var8 = 1.0F - var8;
         var9 = MathHelper.sin(var8 * 3.1415927F);
         float var10 = MathHelper.sin(this.swingProgress * 3.1415927F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
         this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)var9 * 1.2D + (double)var10));
         var10000 = this.bipedRightArm;
         var10000.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
         var10000 = this.bipedRightArm;
         var10000.rotateAngleZ += MathHelper.sin(this.swingProgress * 3.1415927F) * -0.4F;
      }

      if (this.isSneak) {
         this.bipedBody.rotateAngleX = 0.5F;
         var10000 = this.bipedRightArm;
         var10000.rotateAngleX += 0.4F;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleX += 0.4F;
         this.bipedRightLeg.rotationPointZ = 4.0F;
         this.bipedLeftLeg.rotationPointZ = 4.0F;
         this.bipedRightLeg.rotationPointY = 9.0F;
         this.bipedLeftLeg.rotationPointY = 9.0F;
         this.bipedHead.rotationPointY = 1.0F;
      } else {
         this.bipedBody.rotateAngleX = 0.0F;
         this.bipedRightLeg.rotationPointZ = 0.1F;
         this.bipedLeftLeg.rotationPointZ = 0.1F;
         this.bipedRightLeg.rotationPointY = 12.0F;
         this.bipedLeftLeg.rotationPointY = 12.0F;
         this.bipedHead.rotationPointY = 0.0F;
      }

      var10000 = this.bipedRightArm;
      var10000.rotateAngleZ += MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
      var10000 = this.bipedLeftArm;
      var10000.rotateAngleZ -= MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
      var10000 = this.bipedRightArm;
      var10000.rotateAngleX += MathHelper.sin(var3 * 0.067F) * 0.05F;
      var10000 = this.bipedLeftArm;
      var10000.rotateAngleX -= MathHelper.sin(var3 * 0.067F) * 0.05F;
      if (this.aimedBow) {
         var8 = 0.0F;
         var9 = 0.0F;
         this.bipedRightArm.rotateAngleZ = 0.0F;
         this.bipedLeftArm.rotateAngleZ = 0.0F;
         this.bipedRightArm.rotateAngleY = -(0.1F - var8 * 0.6F) + this.bipedHead.rotateAngleY;
         this.bipedLeftArm.rotateAngleY = 0.1F - var8 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
         this.bipedRightArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
         this.bipedLeftArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
         var10000 = this.bipedRightArm;
         var10000.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
         var10000 = this.bipedRightArm;
         var10000.rotateAngleZ += MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleZ -= MathHelper.cos(var3 * 0.09F) * 0.05F + 0.05F;
         var10000 = this.bipedRightArm;
         var10000.rotateAngleX += MathHelper.sin(var3 * 0.067F) * 0.05F;
         var10000 = this.bipedLeftArm;
         var10000.rotateAngleX -= MathHelper.sin(var3 * 0.067F) * 0.05F;
      }

      copyModelAngles(this.bipedHead, this.bipedHeadwear);
   }

   public void postRenderArm(float var1) {
      this.bipedRightArm.postRender(var1);
   }

   public void setModelAttributes(ModelBase var1) {
      super.setModelAttributes(var1);
      if (var1 instanceof ModelBiped) {
         ModelBiped var2 = (ModelBiped)var1;
         this.heldItemLeft = var2.heldItemLeft;
         this.heldItemRight = var2.heldItemRight;
         this.isSneak = var2.isSneak;
         this.aimedBow = var2.aimedBow;
      }

   }

   public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);
      GlStateManager.pushMatrix();
      float var8;
      if ((new Cosmetics()).getInstance().isEnabled() && Cosmetics.Gigante) {
         var8 = 2.0F;
         GlStateManager.scale(5.0F / var8, 5.0F / var8, 5.0F / var8);
         GlStateManager.translate(0.0F, -15.0F * var7, 0.0F);
         this.bipedHead.rotationPointZ = 0.0F;
         this.bipedHead.rotateAngleZ = 0.0F;
         this.bipedBody.rotateAngleX = 0.0F;
         this.bipedRightLeg.rotationPointZ = 0.1F;
         this.bipedLeftLeg.rotationPointZ = 0.1F;
         this.bipedRightLeg.rotationPointY = 12.0F;
         this.bipedLeftLeg.rotationPointY = 12.0F;
         this.bipedHead.rotationPointY = 0.0F;
         this.bipedRightLeg.rotateAngleZ = 0.0F;
         this.bipedRightArm.offsetY = 0.0F;
         this.bipedRightLeg.offsetY = 0.0F;
         this.bipedRightLeg.offsetZ = 0.0F;
         this.bipedLeftLeg.offsetY = 0.0F;
         this.bipedLeftLeg.offsetZ = 0.0F;
      }

      if (this.isChild) {
         var8 = 2.0F;
         GlStateManager.scale(1.5F / var8, 1.5F / var8, 1.5F / var8);
         GlStateManager.translate(0.0F, 16.0F * var7, 0.0F);
         this.bipedHead.render(var7);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         GlStateManager.scale(1.0F / var8, 1.0F / var8, 1.0F / var8);
         GlStateManager.translate(0.0F, 24.0F * var7, 0.0F);
         this.bipedBody.render(var7);
         this.bipedRightArm.render(var7);
         this.bipedLeftArm.render(var7);
         this.bipedRightLeg.render(var7);
         this.bipedLeftLeg.render(var7);
         this.bipedHeadwear.render(var7);
      } else {
         if (var1.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
         }

         this.bipedHead.render(var7);
         this.bipedBody.render(var7);
         this.bipedRightArm.render(var7);
         this.bipedLeftArm.render(var7);
         this.bipedRightLeg.render(var7);
         this.bipedLeftLeg.render(var7);
         this.bipedHeadwear.render(var7);
      }

      GlStateManager.popMatrix();
      Minecraft.getMinecraft();
      if ((var1 == Minecraft.thePlayer || FriendManager.isFriend(var1.getName())) && Wings.Wing) {
         GlStateManager.pushMatrix();
         var8 = 100.0F;
         boolean var9 = this.wingState.containsKey(var1.getUniqueID());
         boolean var10 = var1.onGround;
         if (!var10 || var9) {
            var8 = 10.0F;
         }

         if (!var9 && !var10) {
            this.wingState.put(var1.getUniqueID(), 0L);
         }

         float var11 = var3 + var4 / var8;
         float var12 = var3 + var4 / 100.0F;
         float var13 = var11 * 3.1415927F * 2.0F;
         float var14 = 0.125F - (float)Math.cos((double)var13) * 0.2F;
         float var15 = var12 * 3.1415927F * 2.0F;
         float var16 = 0.125F - (float)Math.cos((double)var15) * 0.2F;
         if (this.wingState.containsKey(var1.getUniqueID()) && (int)(var14 * 100.0F) == (int)(var16 * 100.0F) && var10) {
            this.wingState.remove(var1.getUniqueID());
            var8 = 100.0F;
         }

         Minecraft.getMinecraft().getTextureManager().bindTexture(enderDragonTextures);
         GlStateManager.scale(0.0012D * (Wings.wingsSize + 75.0D), 0.0012D * (Wings.wingsSize + 75.0D), 0.0012D * (Wings.wingsSize + 75.0D));
         GlStateManager.translate(0.0D, var1.isSneaking() ? 0.7D : -0.1D, 0.6D);
         GlStateManager.rotate(45.0F, -30.0F, 0.0F, 0.0F);
         boolean var17 = false;
         boolean var18 = false;
         int var19;
         float var20;
         if (Wings.rainbowWings) {
            var19 = Wings.getRainbow(6000, -15);
            var20 = (float)(var19 >> 16 & 255) / 255.0F;
            float var21 = (float)(var19 >> 8 & 255) / 255.0F;
            float var22 = (float)(var19 & 255) / 255.0F;
            GlStateManager.color(var20, var21, var22);
         } else {
            GlStateManager.color((float)Wings.colorRed / 255.0F, (float)Wings.colorGreen / 255.0F, (float)Wings.colorBlue / 255.0F);
         }

         for(var19 = 0; var19 < 2; ++var19) {
            GlStateManager.enableCull();
            var20 = var11 * 3.1415927F * 2.0F;
            this.wing.rotateAngleX = 0.125F - (float)Math.cos((double)var20) * 0.2F;
            this.wing.rotateAngleY = 0.25F;
            this.wing.rotateAngleZ = (float)(Math.sin((double)var20) + 1.225D) * 0.3F;
            this.wingTip.rotateAngleZ = -((float)(Math.sin((double)(var20 + 2.0F)) + 0.5D)) * 0.75F;
            this.wing.isHidden = false;
            this.wingTip.isHidden = false;
            this.wing.render(var7);
            this.wing.isHidden = true;
            this.wingTip.isHidden = true;
            GlStateManager.scale(-1.0F, 1.0F, 1.0F);
            if (var19 == 0) {
               GlStateManager.cullFace(1028);
               if (var17) {
                  if (var18) {
                     GL11.glColor3d(0.0D, 0.0D, 18.0D);
                  } else {
                     GL11.glColor3d(18.0D, 0.0D, 0.0D);
                  }
               }
            }
         }

         GlStateManager.enableLight(0);
         GlStateManager.enableLight(1);
         GlStateManager.cullFace(1029);
         GlStateManager.disableCull();
         GlStateManager.enableDepth();
         GlStateManager.popMatrix();
         GlStateManager.color(1.0F, 1.0F, 1.0F);
         AbstractClientPlayer var23 = (AbstractClientPlayer)var1;
         GlStateManager.color(1.0F, 1.0F, 1.0F);
         ResourceLocation var24 = var23.getLocationSkin();
         Minecraft.getMinecraft().getTextureManager().bindTexture(var24);
      }

   }

   public void setInvisible(boolean var1) {
      this.bipedHead.showModel = var1;
      this.bipedHeadwear.showModel = var1;
      this.bipedBody.showModel = var1;
      this.bipedRightArm.showModel = var1;
      this.bipedLeftArm.showModel = var1;
      this.bipedRightLeg.showModel = var1;
      this.bipedLeftLeg.showModel = var1;
   }
}
