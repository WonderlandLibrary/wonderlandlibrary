package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.prism.Graphics;

public abstract class NGCamera extends NGNode {
   public static final NGCamera INSTANCE = new NGDefaultCamera();
   protected Affine3D worldTransform = new Affine3D();
   protected double viewWidth = 1.0;
   protected double viewHeight = 1.0;
   protected double zNear = 0.1;
   protected double zFar = 100.0;
   private Vec3d worldPosition = new Vec3d();
   protected GeneralTransform3D projViewTx = new GeneralTransform3D();

   protected void doRender(Graphics var1) {
   }

   protected void renderContent(Graphics var1) {
   }

   protected boolean hasOverlappingContents() {
      return false;
   }

   public void setNearClip(float var1) {
      this.zNear = (double)var1;
   }

   public double getNearClip() {
      return this.zNear;
   }

   public void setFarClip(float var1) {
      this.zFar = (double)var1;
   }

   public double getFarClip() {
      return this.zFar;
   }

   public void setViewWidth(double var1) {
      this.viewWidth = var1;
   }

   public double getViewWidth() {
      return this.viewWidth;
   }

   public void setViewHeight(double var1) {
      this.viewHeight = var1;
   }

   public double getViewHeight() {
      return this.viewHeight;
   }

   public void setProjViewTransform(GeneralTransform3D var1) {
      this.projViewTx.set(var1);
   }

   public void setPosition(Vec3d var1) {
      this.worldPosition.set(var1);
   }

   public void setWorldTransform(Affine3D var1) {
      this.worldTransform.setTransform(var1);
   }

   public GeneralTransform3D getProjViewTx(GeneralTransform3D var1) {
      if (var1 == null) {
         var1 = new GeneralTransform3D();
      }

      return var1.set(this.projViewTx);
   }

   public Vec3d getPositionInWorld(Vec3d var1) {
      if (var1 == null) {
         var1 = new Vec3d();
      }

      var1.set(this.worldPosition);
      return var1;
   }

   public void release() {
   }

   public abstract PickRay computePickRay(float var1, float var2, PickRay var3);
}
