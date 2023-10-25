/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.transform.Transform
 */
package com.sun.javafx.scene.transform;

import com.sun.javafx.scene.transform.TransformHelper;
import javafx.scene.transform.Transform;

public class TransformUtils {
    public static Transform immutableTransform(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        return TransformHelper.createImmutableTransform(d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
    }

    public static Transform immutableTransform(Transform transform) {
        return TransformHelper.createImmutableTransform(transform.getMxx(), transform.getMxy(), transform.getMxz(), transform.getTx(), transform.getMyx(), transform.getMyy(), transform.getMyz(), transform.getTy(), transform.getMzx(), transform.getMzy(), transform.getMzz(), transform.getTz());
    }

    public static Transform immutableTransform(Transform transform, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12) {
        return TransformHelper.createImmutableTransform(transform, d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12);
    }

    public static Transform immutableTransform(Transform transform, Transform transform2) {
        return TransformHelper.createImmutableTransform(transform, transform2.getMxx(), transform2.getMxy(), transform2.getMxz(), transform2.getTx(), transform2.getMyx(), transform2.getMyy(), transform2.getMyz(), transform2.getTy(), transform2.getMzx(), transform2.getMzy(), transform2.getMzz(), transform2.getTz());
    }

    public static Transform immutableTransform(Transform transform, Transform transform2, Transform transform3) {
        return TransformHelper.createImmutableTransform(transform, transform2, transform3);
    }
}

