package net.minecraft.client.model;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import optifine.ModelSprite;
import org.lwjgl.opengl.GL11;

public class ModelRenderer {
   private int textureOffsetY;
   public float textureHeight;
   public float rotationPointY;
   private int displayList;
   public float offsetX;
   public boolean showModel;
   public float offsetZ;
   public float offsetY;
   private static final String __OBFID = "CL_00000874";
   public boolean mirror;
   public float rotateAngleX;
   public float rotationPointZ;
   public float rotateAngleY;
   public float rotateAngleZ;
   private boolean compiled;
   private ModelBase baseModel;
   public List cubeList;
   public List spriteList;
   private int textureOffsetX;
   public float rotationPointX;
   public final String boxName;
   public List childModels;
   float savedScale;
   public boolean isHidden;
   public float textureWidth;
   public boolean mirrorV;

   public boolean getCompiled() {
      return this.compiled;
   }

   public ModelRenderer(ModelBase var1, String var2) {
      this.spriteList = new ArrayList();
      this.mirrorV = false;
      this.textureWidth = 64.0F;
      this.textureHeight = 32.0F;
      this.showModel = true;
      this.cubeList = Lists.newArrayList();
      this.baseModel = var1;
      var1.boxList.add(this);
      this.boxName = var2;
      this.setTextureSize(var1.textureWidth, var1.textureHeight);
   }

   public ModelRenderer(ModelBase var1) {
      this(var1, (String)null);
   }

   public void addSprite(float var1, float var2, float var3, int var4, int var5, int var6, float var7) {
      this.spriteList.add(new ModelSprite(this, this.textureOffsetX, this.textureOffsetY, var1, var2, var3, var4, var5, var6, var7));
   }

   private void compileDisplayList(float var1) {
      if (this.displayList == 0) {
         this.savedScale = var1;
         this.displayList = GLAllocation.generateDisplayLists(1);
      }

      GL11.glNewList(this.displayList, 4864);
      WorldRenderer var2 = Tessellator.getInstance().getWorldRenderer();

      int var3;
      for(var3 = 0; var3 < this.cubeList.size(); ++var3) {
         ((ModelBox)this.cubeList.get(var3)).render(var2, var1);
      }

      for(var3 = 0; var3 < this.spriteList.size(); ++var3) {
         ModelSprite var4 = (ModelSprite)this.spriteList.get(var3);
         var4.render(Tessellator.getInstance(), var1);
      }

      GL11.glEndList();
      this.compiled = true;
   }

   public ModelRenderer setTextureOffset(int var1, int var2) {
      this.textureOffsetX = var1;
      this.textureOffsetY = var2;
      return this;
   }

   public ModelRenderer addBox(String var1, float var2, float var3, float var4, int var5, int var6, int var7) {
      var1 = this.boxName + "." + var1;
      TextureOffset var8 = this.baseModel.getTextureOffset(var1);
      this.setTextureOffset(var8.textureOffsetX, var8.textureOffsetY);
      this.cubeList.add((new ModelBox(this, this.textureOffsetX, this.textureOffsetY, var2, var3, var4, var5, var6, var7, 0.0F)).setBoxName(var1));
      return this;
   }

   public void renderWithRotation(float var1) {
      if (!this.isHidden && this.showModel) {
         if (!this.compiled) {
            this.compileDisplayList(var1);
         }

         GlStateManager.pushMatrix();
         GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
         if (this.rotateAngleY != 0.0F) {
            GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
         }

         if (this.rotateAngleX != 0.0F) {
            GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
         }

         if (this.rotateAngleZ != 0.0F) {
            GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
         }

         GlStateManager.callList(this.displayList);
         GlStateManager.popMatrix();
      }

   }

   public ModelRenderer(ModelBase var1, int var2, int var3) {
      this(var1);
      this.setTextureOffset(var2, var3);
   }

   public void setRotationPoint(float var1, float var2, float var3) {
      this.rotationPointX = var1;
      this.rotationPointY = var2;
      this.rotationPointZ = var3;
   }

   public int getDisplayList() {
      return this.displayList;
   }

   public ModelRenderer addBox(float var1, float var2, float var3, int var4, int var5, int var6) {
      this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, var1, var2, var3, var4, var5, var6, 0.0F));
      return this;
   }

   public void resetDisplayList() {
      if (this.compiled) {
         this.compiled = false;
         this.compileDisplayList(this.savedScale);
      }

   }

   public ModelRenderer setTextureSize(int var1, int var2) {
      this.textureWidth = (float)var1;
      this.textureHeight = (float)var2;
      return this;
   }

   public void render(float var1) {
      if (!this.isHidden && this.showModel) {
         if (!this.compiled) {
            this.compileDisplayList(var1);
         }

         GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
         int var2;
         if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
               GlStateManager.callList(this.displayList);
               if (this.childModels != null) {
                  for(var2 = 0; var2 < this.childModels.size(); ++var2) {
                     ((ModelRenderer)this.childModels.get(var2)).render(var1);
                  }
               }
            } else {
               GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
               GlStateManager.callList(this.displayList);
               if (this.childModels != null) {
                  for(var2 = 0; var2 < this.childModels.size(); ++var2) {
                     ((ModelRenderer)this.childModels.get(var2)).render(var1);
                  }
               }

               GlStateManager.translate(-this.rotationPointX * var1, -this.rotationPointY * var1, -this.rotationPointZ * var1);
            }
         } else {
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
            if (this.rotateAngleZ != 0.0F) {
               GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if (this.rotateAngleY != 0.0F) {
               GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (this.rotateAngleX != 0.0F) {
               GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            GlStateManager.callList(this.displayList);
            if (this.childModels != null) {
               for(var2 = 0; var2 < this.childModels.size(); ++var2) {
                  ((ModelRenderer)this.childModels.get(var2)).render(var1);
               }
            }

            GlStateManager.popMatrix();
         }

         GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
      }

   }

   public void addBox(float var1, float var2, float var3, int var4, int var5, int var6, float var7) {
      this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, var1, var2, var3, var4, var5, var6, var7));
   }

   public void addChild(ModelRenderer var1) {
      if (this.childModels == null) {
         this.childModels = Lists.newArrayList();
      }

      this.childModels.add(var1);
   }

   public void postRender(float var1) {
      if (!this.isHidden && this.showModel) {
         if (!this.compiled) {
            this.compileDisplayList(var1);
         }

         if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F) {
               GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
            }
         } else {
            GlStateManager.translate(this.rotationPointX * var1, this.rotationPointY * var1, this.rotationPointZ * var1);
            if (this.rotateAngleZ != 0.0F) {
               GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if (this.rotateAngleY != 0.0F) {
               GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (this.rotateAngleX != 0.0F) {
               GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
            }
         }
      }

   }

   public ModelRenderer addBox(float var1, float var2, float var3, int var4, int var5, int var6, boolean var7) {
      this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, var1, var2, var3, var4, var5, var6, 0.0F, var7));
      return this;
   }
}
