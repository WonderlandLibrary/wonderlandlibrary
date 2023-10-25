/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.Node
 *  javafx.scene.transform.Transform
 */
package com.sun.javafx.scene.transform;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.util.Utils;
import javafx.scene.Node;
import javafx.scene.transform.Transform;

public class TransformHelper {
    private static TransformAccessor transformAccessor;

    private TransformHelper() {
    }

    public static void add(Transform transform, Node node) {
        transformAccessor.add(transform, node);
    }

    public static void remove(Transform transform, Node node) {
        transformAccessor.remove(transform, node);
    }

    public static void apply(Transform transform, Affine3D affine3D) {
        transformAccessor.apply(transform, affine3D);
    }

    public static BaseTransform derive(Transform transform, BaseTransform baseTransform) {
        return transformAccessor.derive(transform, baseTransform);
    }

    public static Transform createImmutableTransform() {
        return transformAccessor.createImmutableTransform();
    }

    public static Transform createImmutableTransform(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        return transformAccessor.createImmutableTransform(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
    }

    public static Transform createImmutableTransform(Transform transform, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        return transformAccessor.createImmutableTransform(transform, d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
    }

    public static Transform createImmutableTransform(Transform transform, Transform transform2, Transform transform3) {
        return transformAccessor.createImmutableTransform(transform, transform2, transform3);
    }

    public static void setTransformAccessor(TransformAccessor transformAccessor) {
        if (TransformHelper.transformAccessor != null) {
            throw new IllegalStateException();
        }
        TransformHelper.transformAccessor = transformAccessor;
    }

    static {
        Utils.forceInit(Transform.class);
    }

    public static interface TransformAccessor {
        public void add(Transform var1, Node var2);

        public void remove(Transform var1, Node var2);

        public void apply(Transform var1, Affine3D var2);

        public BaseTransform derive(Transform var1, BaseTransform var2);

        public Transform createImmutableTransform();

        public Transform createImmutableTransform(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23);

        public Transform createImmutableTransform(Transform var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22, double var24);

        public Transform createImmutableTransform(Transform var1, Transform var2, Transform var3);
    }
}

