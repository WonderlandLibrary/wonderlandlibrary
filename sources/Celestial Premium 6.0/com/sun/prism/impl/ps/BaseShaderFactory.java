/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.ps;

import com.sun.prism.Image;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseResourceFactory;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.ps.CachingEllipseRep;
import com.sun.prism.impl.ps.CachingRoundRectRep;
import com.sun.prism.impl.ps.CachingShapeRep;
import com.sun.prism.impl.shape.BasicEllipseRep;
import com.sun.prism.impl.shape.BasicRoundRectRep;
import com.sun.prism.impl.shape.BasicShapeRep;
import com.sun.prism.ps.ShaderFactory;
import com.sun.prism.shape.ShapeRep;
import java.util.Map;

public abstract class BaseShaderFactory
extends BaseResourceFactory
implements ShaderFactory {
    public BaseShaderFactory() {
    }

    public BaseShaderFactory(Map<Image, Texture> map, Map<Image, Texture> map2, Map<Image, Texture> map3) {
        super(map, map2, map3);
    }

    @Override
    public ShapeRep createPathRep() {
        return PrismSettings.cacheComplexShapes ? new CachingShapeRep() : new BasicShapeRep();
    }

    @Override
    public ShapeRep createRoundRectRep() {
        return PrismSettings.cacheSimpleShapes ? new CachingRoundRectRep() : new BasicRoundRectRep();
    }

    @Override
    public ShapeRep createEllipseRep() {
        return PrismSettings.cacheSimpleShapes ? new CachingEllipseRep() : new BasicEllipseRep();
    }

    @Override
    public ShapeRep createArcRep() {
        return PrismSettings.cacheComplexShapes ? new CachingShapeRep() : new BasicShapeRep();
    }
}

