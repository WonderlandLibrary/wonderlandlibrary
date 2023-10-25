/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene;

public final class DirtyBits
extends Enum<DirtyBits> {
    public static final /* enum */ DirtyBits NODE_CACHE = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_CLIP = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_EFFECT = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_OPACITY = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_TRANSFORM = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_BOUNDS = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_TRANSFORMED_BOUNDS = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_VISIBLE = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_DEPTH_TEST = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_BLENDMODE = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_CSS = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_FORCE_SYNC = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_VIEW_ORDER = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_GEOMETRY = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_CULLFACE = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_DRAWMODE = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_SMOOTH = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_VIEWPORT = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_CONTENTS = new DirtyBits();
    public static final /* enum */ DirtyBits PARENT_CHILDREN = new DirtyBits();
    public static final /* enum */ DirtyBits PARENT_CHILDREN_VIEW_ORDER = new DirtyBits();
    public static final /* enum */ DirtyBits SHAPE_FILL = new DirtyBits();
    public static final /* enum */ DirtyBits SHAPE_FILLRULE = new DirtyBits();
    public static final /* enum */ DirtyBits SHAPE_MODE = new DirtyBits();
    public static final /* enum */ DirtyBits SHAPE_STROKE = new DirtyBits();
    public static final /* enum */ DirtyBits SHAPE_STROKEATTRS = new DirtyBits();
    public static final /* enum */ DirtyBits REGION_SHAPE = new DirtyBits();
    public static final /* enum */ DirtyBits TEXT_ATTRS = new DirtyBits();
    public static final /* enum */ DirtyBits TEXT_FONT = new DirtyBits();
    public static final /* enum */ DirtyBits TEXT_SELECTION = new DirtyBits();
    public static final /* enum */ DirtyBits TEXT_HELPER = new DirtyBits();
    public static final /* enum */ DirtyBits MEDIAVIEW_MEDIA = new DirtyBits();
    public static final /* enum */ DirtyBits WEBVIEW_VIEW = new DirtyBits();
    public static final /* enum */ DirtyBits EFFECT_EFFECT = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_CAMERA = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_CAMERA_TRANSFORM = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_LIGHT = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_LIGHT_SCOPE = new DirtyBits();
    public static final /* enum */ DirtyBits NODE_LIGHT_TRANSFORM = new DirtyBits();
    public static final /* enum */ DirtyBits MATERIAL = new DirtyBits();
    public static final /* enum */ DirtyBits MESH = new DirtyBits();
    public static final /* enum */ DirtyBits MESH_GEOM = new DirtyBits();
    public static final /* enum */ DirtyBits DEBUG = new DirtyBits();
    private static final /* synthetic */ DirtyBits[] $VALUES;

    public static DirtyBits[] values() {
        return (DirtyBits[])$VALUES.clone();
    }

    public static DirtyBits valueOf(String string) {
        return Enum.valueOf(DirtyBits.class, string);
    }

    private static /* synthetic */ DirtyBits[] $values() {
        return new DirtyBits[]{NODE_CACHE, NODE_CLIP, NODE_EFFECT, NODE_OPACITY, NODE_TRANSFORM, NODE_BOUNDS, NODE_TRANSFORMED_BOUNDS, NODE_VISIBLE, NODE_DEPTH_TEST, NODE_BLENDMODE, NODE_CSS, NODE_FORCE_SYNC, NODE_VIEW_ORDER, NODE_GEOMETRY, NODE_CULLFACE, NODE_DRAWMODE, NODE_SMOOTH, NODE_VIEWPORT, NODE_CONTENTS, PARENT_CHILDREN, PARENT_CHILDREN_VIEW_ORDER, SHAPE_FILL, SHAPE_FILLRULE, SHAPE_MODE, SHAPE_STROKE, SHAPE_STROKEATTRS, REGION_SHAPE, TEXT_ATTRS, TEXT_FONT, TEXT_SELECTION, TEXT_HELPER, MEDIAVIEW_MEDIA, WEBVIEW_VIEW, EFFECT_EFFECT, NODE_CAMERA, NODE_CAMERA_TRANSFORM, NODE_LIGHT, NODE_LIGHT_SCOPE, NODE_LIGHT_TRANSFORM, MATERIAL, MESH, MESH_GEOM, DEBUG};
    }

    static {
        $VALUES = DirtyBits.$values();
    }
}

