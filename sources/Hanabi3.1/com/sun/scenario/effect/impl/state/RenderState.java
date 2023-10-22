package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;

public interface RenderState {
   RenderState UserSpaceRenderState = new RenderState() {
      public EffectCoordinateSpace getEffectTransformSpace() {
         return RenderState.EffectCoordinateSpace.UserSpace;
      }

      public BaseTransform getInputTransform(BaseTransform var1) {
         return BaseTransform.IDENTITY_TRANSFORM;
      }

      public BaseTransform getResultTransform(BaseTransform var1) {
         return var1;
      }

      public Rectangle getInputClip(int var1, Rectangle var2) {
         return var2;
      }
   };
   RenderState UnclippedUserSpaceRenderState = new RenderState() {
      public EffectCoordinateSpace getEffectTransformSpace() {
         return RenderState.EffectCoordinateSpace.UserSpace;
      }

      public BaseTransform getInputTransform(BaseTransform var1) {
         return BaseTransform.IDENTITY_TRANSFORM;
      }

      public BaseTransform getResultTransform(BaseTransform var1) {
         return var1;
      }

      public Rectangle getInputClip(int var1, Rectangle var2) {
         return null;
      }
   };
   RenderState RenderSpaceRenderState = new RenderState() {
      public EffectCoordinateSpace getEffectTransformSpace() {
         return RenderState.EffectCoordinateSpace.RenderSpace;
      }

      public BaseTransform getInputTransform(BaseTransform var1) {
         return var1;
      }

      public BaseTransform getResultTransform(BaseTransform var1) {
         return BaseTransform.IDENTITY_TRANSFORM;
      }

      public Rectangle getInputClip(int var1, Rectangle var2) {
         return var2;
      }
   };

   EffectCoordinateSpace getEffectTransformSpace();

   BaseTransform getInputTransform(BaseTransform var1);

   BaseTransform getResultTransform(BaseTransform var1);

   Rectangle getInputClip(int var1, Rectangle var2);

   public static enum EffectCoordinateSpace {
      UserSpace,
      CustomSpace,
      RenderSpace;
   }
}
