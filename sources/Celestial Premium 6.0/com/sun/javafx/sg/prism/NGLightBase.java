/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.sg.prism.NGShape3D;
import com.sun.prism.Graphics;
import com.sun.prism.paint.Color;
import java.util.List;

public class NGLightBase
extends NGNode {
    private Color color = Color.WHITE;
    private boolean lightOn = true;
    private Affine3D worldTransform;
    List<NGNode> scopedNodes = List.of();
    List<NGNode> excludedNodes = List.of();

    protected NGLightBase() {
    }

    @Override
    public void setTransformMatrix(BaseTransform baseTransform) {
        super.setTransformMatrix(baseTransform);
    }

    @Override
    protected void doRender(Graphics graphics) {
    }

    @Override
    protected void renderContent(Graphics graphics) {
    }

    @Override
    protected boolean hasOverlappingContents() {
        return false;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Object object) {
        if (!this.color.equals(object)) {
            this.color = (Color)object;
            this.visualsChanged();
        }
    }

    public boolean isLightOn() {
        return this.lightOn;
    }

    public void setLightOn(boolean bl) {
        if (this.lightOn != bl) {
            this.visualsChanged();
            this.lightOn = bl;
        }
    }

    public Affine3D getWorldTransform() {
        return this.worldTransform;
    }

    public void setWorldTransform(Affine3D affine3D) {
        this.worldTransform = affine3D;
    }

    public void setScope(List<NGNode> list) {
        if (!this.scopedNodes.equals(list)) {
            this.scopedNodes = list;
            this.visualsChanged();
        }
    }

    public void setExclusionScope(List<NGNode> list) {
        if (!this.excludedNodes.equals(list)) {
            this.excludedNodes = list;
            this.visualsChanged();
        }
    }

    final boolean affects(NGShape3D nGShape3D) {
        if (!this.lightOn) {
            return false;
        }
        if (this.scopedNodes.isEmpty() && this.excludedNodes.isEmpty()) {
            return true;
        }
        if (this.scopedNodes.contains(nGShape3D)) {
            return true;
        }
        if (this.excludedNodes.contains(nGShape3D)) {
            return false;
        }
        for (NGNode nGNode = nGShape3D.getParent(); nGNode != null; nGNode = nGNode.getParent()) {
            if (this.scopedNodes.contains(nGNode)) {
                return true;
            }
            if (!this.excludedNodes.contains(nGNode)) continue;
            return false;
        }
        return this.scopedNodes.isEmpty();
    }

    @Override
    public void release() {
    }
}

