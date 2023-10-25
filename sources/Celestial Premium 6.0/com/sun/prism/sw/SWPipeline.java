/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.glass.utils.NativeLibLoader;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.ResourceFactory;
import com.sun.prism.sw.SWResourceFactory;
import java.security.AccessController;
import java.util.HashMap;
import java.util.List;

public final class SWPipeline
extends GraphicsPipeline {
    private static SWPipeline theInstance;
    private final HashMap<Integer, SWResourceFactory> factories = new HashMap(1);

    @Override
    public boolean init() {
        return true;
    }

    private SWPipeline() {
    }

    public static SWPipeline getInstance() {
        if (theInstance == null) {
            theInstance = new SWPipeline();
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
        SWResourceFactory sWResourceFactory = this.factories.get(n);
        if (sWResourceFactory == null) {
            sWResourceFactory = new SWResourceFactory(screen);
            this.factories.put(n, sWResourceFactory);
        }
        return sWResourceFactory;
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
    public boolean isUploading() {
        return true;
    }

    static {
        Object object = AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("prism_sw");
            return null;
        });
    }
}

