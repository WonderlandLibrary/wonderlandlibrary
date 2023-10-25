/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontFactory;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.ResourceFactory;
import com.sun.prism.j2d.J2DFontFactory;
import com.sun.prism.j2d.J2DResourceFactory;
import java.util.HashMap;
import java.util.List;

public class J2DPipeline
extends GraphicsPipeline {
    private static J2DPipeline theInstance;
    private final HashMap<Integer, J2DResourceFactory> factories = new HashMap(1);
    private FontFactory j2DFontFactory;

    @Override
    public boolean init() {
        return true;
    }

    private J2DPipeline() {
    }

    public static J2DPipeline getInstance() {
        if (theInstance == null) {
            theInstance = new J2DPipeline();
        }
        return theInstance;
    }

    @Override
    public int getAdapterOrdinal(Screen screen) {
        return Screen.getScreens().indexOf(screen);
    }

    @Override
    public ResourceFactory getResourceFactory(Screen screen) {
        Integer n = screen.getAdapterOrdinal();
        J2DResourceFactory j2DResourceFactory = this.factories.get(n);
        if (j2DResourceFactory == null) {
            j2DResourceFactory = new J2DResourceFactory(screen);
            this.factories.put(n, j2DResourceFactory);
        }
        return j2DResourceFactory;
    }

    @Override
    public ResourceFactory getDefaultResourceFactory(List<Screen> list) {
        return this.getResourceFactory(Screen.getMainScreen());
    }

    @Override
    public boolean is3DSupported() {
        return false;
    }

    @Override
    public boolean isVsyncSupported() {
        return false;
    }

    @Override
    public boolean supportsShaderType(GraphicsPipeline.ShaderType shaderType) {
        return false;
    }

    @Override
    public boolean supportsShaderModel(GraphicsPipeline.ShaderModel shaderModel) {
        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public FontFactory getFontFactory() {
        if (this.j2DFontFactory == null) {
            FontFactory fontFactory = super.getFontFactory();
            this.j2DFontFactory = new J2DFontFactory(fontFactory);
        }
        return this.j2DFontFactory;
    }

    @Override
    public boolean isUploading() {
        return true;
    }
}

