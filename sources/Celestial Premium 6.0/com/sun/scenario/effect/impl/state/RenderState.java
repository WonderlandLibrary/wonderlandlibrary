/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;

public interface RenderState {
    public static final RenderState UserSpaceRenderState = new RenderState(){

        @Override
        public EffectCoordinateSpace getEffectTransformSpace() {
            return EffectCoordinateSpace.UserSpace;
        }

        @Override
        public BaseTransform getInputTransform(BaseTransform baseTransform) {
            return BaseTransform.IDENTITY_TRANSFORM;
        }

        @Override
        public BaseTransform getResultTransform(BaseTransform baseTransform) {
            return baseTransform;
        }

        @Override
        public Rectangle getInputClip(int n, Rectangle rectangle) {
            return rectangle;
        }
    };
    public static final RenderState UnclippedUserSpaceRenderState = new RenderState(){

        @Override
        public EffectCoordinateSpace getEffectTransformSpace() {
            return EffectCoordinateSpace.UserSpace;
        }

        @Override
        public BaseTransform getInputTransform(BaseTransform baseTransform) {
            return BaseTransform.IDENTITY_TRANSFORM;
        }

        @Override
        public BaseTransform getResultTransform(BaseTransform baseTransform) {
            return baseTransform;
        }

        @Override
        public Rectangle getInputClip(int n, Rectangle rectangle) {
            return null;
        }
    };
    public static final RenderState RenderSpaceRenderState = new RenderState(){

        @Override
        public EffectCoordinateSpace getEffectTransformSpace() {
            return EffectCoordinateSpace.RenderSpace;
        }

        @Override
        public BaseTransform getInputTransform(BaseTransform baseTransform) {
            return baseTransform;
        }

        @Override
        public BaseTransform getResultTransform(BaseTransform baseTransform) {
            return BaseTransform.IDENTITY_TRANSFORM;
        }

        @Override
        public Rectangle getInputClip(int n, Rectangle rectangle) {
            return rectangle;
        }
    };

    public EffectCoordinateSpace getEffectTransformSpace();

    public BaseTransform getInputTransform(BaseTransform var1);

    public BaseTransform getResultTransform(BaseTransform var1);

    public Rectangle getInputClip(int var1, Rectangle var2);

    public static final class EffectCoordinateSpace
    extends Enum<EffectCoordinateSpace> {
        public static final /* enum */ EffectCoordinateSpace UserSpace = new EffectCoordinateSpace();
        public static final /* enum */ EffectCoordinateSpace CustomSpace = new EffectCoordinateSpace();
        public static final /* enum */ EffectCoordinateSpace RenderSpace = new EffectCoordinateSpace();
        private static final /* synthetic */ EffectCoordinateSpace[] $VALUES;

        public static EffectCoordinateSpace[] values() {
            return (EffectCoordinateSpace[])$VALUES.clone();
        }

        public static EffectCoordinateSpace valueOf(String string) {
            return Enum.valueOf(EffectCoordinateSpace.class, string);
        }

        private static /* synthetic */ EffectCoordinateSpace[] $values() {
            return new EffectCoordinateSpace[]{UserSpace, CustomSpace, RenderSpace};
        }

        static {
            $VALUES = EffectCoordinateSpace.$values();
        }
    }
}

