package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.prism.Graphics;
import com.sun.prism.Material;
import com.sun.prism.MeshView;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.ResourceFactory;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;

public abstract class NGShape3D extends NGNode {
   private NGPhongMaterial material;
   private DrawMode drawMode;
   private CullFace cullFace;
   private boolean materialDirty = false;
   private boolean drawModeDirty = false;
   NGTriangleMesh mesh;
   private MeshView meshView;

   public void setMaterial(NGPhongMaterial var1) {
      this.material = var1;
      this.materialDirty = true;
      this.visualsChanged();
   }

   public void setDrawMode(Object var1) {
      this.drawMode = (DrawMode)var1;
      this.drawModeDirty = true;
      this.visualsChanged();
   }

   public void setCullFace(Object var1) {
      this.cullFace = (CullFace)var1;
      this.visualsChanged();
   }

   void invalidate() {
      this.meshView = null;
      this.visualsChanged();
   }

   private void renderMeshView(Graphics var1) {
      var1.setup3DRendering();
      ResourceFactory var2 = var1.getResourceFactory();
      if (this.meshView == null && this.mesh != null) {
         this.meshView = var2.createMeshView(this.mesh.createMesh(var2));
         this.materialDirty = this.drawModeDirty = true;
      }

      if (this.meshView != null && this.mesh.validate()) {
         Material var3 = this.material.createMaterial(var2);
         if (this.materialDirty) {
            this.meshView.setMaterial(var3);
            this.materialDirty = false;
         }

         int var4 = this.cullFace.ordinal();
         if (this.cullFace.ordinal() != MeshView.CULL_NONE && var1.getTransformNoClone().getDeterminant() < 0.0) {
            var4 = var4 == MeshView.CULL_BACK ? MeshView.CULL_FRONT : MeshView.CULL_BACK;
         }

         this.meshView.setCullingMode(var4);
         if (this.drawModeDirty) {
            this.meshView.setWireframe(this.drawMode == DrawMode.LINE);
            this.drawModeDirty = false;
         }

         int var5 = 0;
         if (var1.getLights() != null && var1.getLights()[0] != null) {
            float var16 = 0.0F;
            float var7 = 0.0F;
            float var8 = 0.0F;

            for(int var9 = 0; var9 < var1.getLights().length; ++var9) {
               NGLightBase var10 = var1.getLights()[var9];
               if (var10 == null) {
                  break;
               }

               if (var10.affects(this)) {
                  float var11 = var10.getColor().getRed();
                  float var12 = var10.getColor().getGreen();
                  float var13 = var10.getColor().getBlue();
                  if (var10 instanceof NGPointLight) {
                     NGPointLight var14 = (NGPointLight)var10;
                     if (var11 != 0.0F || var12 != 0.0F || var13 != 0.0F) {
                        Affine3D var15 = var14.getWorldTransform();
                        this.meshView.setPointLight(var5++, (float)var15.getMxt(), (float)var15.getMyt(), (float)var15.getMzt(), var11, var12, var13, 1.0F);
                     }
                  } else if (var10 instanceof NGAmbientLight) {
                     var16 += var11;
                     var8 += var12;
                     var7 += var13;
                  }
               }
            }

            var16 = saturate(var16);
            var8 = saturate(var8);
            var7 = saturate(var7);
            this.meshView.setAmbientLight(var16, var8, var7);
         } else {
            this.meshView.setAmbientLight(0.0F, 0.0F, 0.0F);
            Vec3d var6 = var1.getCameraNoClone().getPositionInWorld((Vec3d)null);
            this.meshView.setPointLight(var5++, (float)var6.x, (float)var6.y, (float)var6.z, 1.0F, 1.0F, 1.0F, 1.0F);
         }

         while(var5 < 3) {
            this.meshView.setPointLight(var5++, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
         }

         this.meshView.render(var1);
      }
   }

   private static float saturate(float var0) {
      return var0 < 1.0F ? (var0 < 0.0F ? 0.0F : var0) : 1.0F;
   }

   public void setMesh(NGTriangleMesh var1) {
      this.mesh = var1;
      this.meshView = null;
      this.visualsChanged();
   }

   protected void renderContent(Graphics var1) {
      if (Platform.isSupported(ConditionalFeature.SCENE3D) && this.material != null && !(var1 instanceof PrinterGraphics)) {
         this.renderMeshView(var1);
      }
   }

   boolean isShape3D() {
      return true;
   }

   protected boolean hasOverlappingContents() {
      return false;
   }

   public void release() {
   }
}
