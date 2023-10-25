/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.glass.utils.NativeLibLoader;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.ResourceFactory;
import com.sun.prism.d3d.D3DDriverInformation;
import com.sun.prism.d3d.D3DResourceFactory;
import com.sun.prism.impl.PrismSettings;
import java.security.AccessController;
import java.util.List;

public final class D3DPipeline
extends GraphicsPipeline {
    private static final boolean d3dEnabled;
    private static final Thread creator;
    private static D3DPipeline theInstance;
    private static D3DResourceFactory[] factories;
    private static boolean d3dInitialized;
    D3DResourceFactory _default;
    private int maxSamples = -1;

    public static D3DPipeline getInstance() {
        return theInstance;
    }

    private static boolean isDriverWarning(String string) {
        return string.contains("driver version");
    }

    private static void printDriverWarning(D3DDriverInformation d3DDriverInformation) {
        if (d3DDriverInformation != null && d3DDriverInformation.warningMessage != null && (PrismSettings.verbose || D3DPipeline.isDriverWarning(d3DDriverInformation.warningMessage))) {
            System.out.println("Device \"" + d3DDriverInformation.deviceDescription + "\" (" + d3DDriverInformation.deviceName + ") initialization failed : ");
            System.out.println(d3DDriverInformation.warningMessage);
        }
    }

    private static void printDriverWarning(int n) {
        D3DPipeline.printDriverWarning(D3DPipeline.nGetDriverInformation(n, new D3DDriverInformation()));
    }

    private static void printDriverInformation(int n) {
        D3DDriverInformation d3DDriverInformation = D3DPipeline.nGetDriverInformation(n, new D3DDriverInformation());
        if (d3DDriverInformation != null) {
            System.out.println("OS Information:");
            System.out.println("\t" + d3DDriverInformation.getOsVersion() + " build " + d3DDriverInformation.osBuildNumber);
            System.out.println("D3D Driver Information:");
            System.out.println("\t" + d3DDriverInformation.deviceDescription);
            System.out.println("\t" + d3DDriverInformation.deviceName);
            System.out.println("\tDriver " + d3DDriverInformation.driverName + ", version " + d3DDriverInformation.getDriverVersion());
            System.out.println("\tPixel Shader version " + d3DDriverInformation.psVersionMajor + "." + d3DDriverInformation.psVersionMinor);
            System.out.println("\tDevice : " + d3DDriverInformation.getDeviceID());
            System.out.println("\tMax Multisamples supported: " + d3DDriverInformation.maxSamples);
            if (d3DDriverInformation.warningMessage != null) {
                System.out.println("\t *** " + d3DDriverInformation.warningMessage);
            }
        }
    }

    private static void printDriverWarnings() {
        D3DDriverInformation d3DDriverInformation;
        int n = 0;
        while ((d3DDriverInformation = D3DPipeline.nGetDriverInformation(n, new D3DDriverInformation())) != null) {
            D3DPipeline.printDriverWarning(d3DDriverInformation);
            ++n;
        }
    }

    private D3DPipeline() {
    }

    @Override
    public boolean init() {
        return d3dEnabled;
    }

    private static native boolean nInit(Class var0, boolean var1);

    private static native String nGetErrorMessage();

    private static native void nDispose(boolean var0);

    private static native int nGetAdapterOrdinal(long var0);

    private static native int nGetAdapterCount();

    private static native D3DDriverInformation nGetDriverInformation(int var0, D3DDriverInformation var1);

    private static native int nGetMaxSampleSupport(int var0);

    private void reset(boolean bl) {
        if (!d3dInitialized) {
            return;
        }
        if (creator != Thread.currentThread()) {
            throw new IllegalStateException("This operation is not permitted on the current thread [" + Thread.currentThread().getName() + "]");
        }
        for (int i = 0; i != factories.length; ++i) {
            if (factories[i] != null) {
                factories[i].dispose();
            }
            D3DPipeline.factories[i] = null;
        }
        factories = null;
        this._default = null;
        d3dInitialized = false;
        D3DPipeline.nDispose(bl);
    }

    void reinitialize() {
        if (PrismSettings.verbose) {
            System.err.println("D3DPipeline: reinitialize after device was removed");
        }
        this.reset(false);
        boolean bl = D3DPipeline.nInit(PrismSettings.class, false);
        if (!bl) {
            D3DPipeline.nDispose(false);
            return;
        }
        d3dInitialized = true;
        factories = new D3DResourceFactory[D3DPipeline.nGetAdapterCount()];
    }

    @Override
    public void dispose() {
        this.reset(true);
        theInstance = null;
        super.dispose();
    }

    private static D3DResourceFactory createResourceFactory(int n, Screen screen) {
        long l = D3DResourceFactory.nGetContext(n);
        return l != 0L ? new D3DResourceFactory(l, screen) : null;
    }

    private static D3DResourceFactory getD3DResourceFactory(int n, Screen screen) {
        D3DResourceFactory d3DResourceFactory = factories[n];
        if (d3DResourceFactory == null && screen != null) {
            D3DPipeline.factories[n] = d3DResourceFactory = D3DPipeline.createResourceFactory(n, screen);
        }
        return d3DResourceFactory;
    }

    private static Screen getScreenForAdapter(List<Screen> list, int n) {
        for (Screen screen : list) {
            if (screen.getAdapterOrdinal() != n) continue;
            return screen;
        }
        return Screen.getMainScreen();
    }

    @Override
    public int getAdapterOrdinal(Screen screen) {
        return D3DPipeline.nGetAdapterOrdinal(screen.getNativeScreen());
    }

    private static D3DResourceFactory findDefaultResourceFactory(List<Screen> list) {
        if (!d3dInitialized) {
            D3DPipeline.getInstance().reinitialize();
            if (!d3dInitialized) {
                return null;
            }
        }
        int n = D3DPipeline.nGetAdapterCount();
        for (int i = 0; i != n; ++i) {
            D3DResourceFactory d3DResourceFactory = D3DPipeline.getD3DResourceFactory(i, D3DPipeline.getScreenForAdapter(list, i));
            if (d3DResourceFactory != null) {
                if (PrismSettings.verbose) {
                    D3DPipeline.printDriverInformation(i);
                }
                return d3DResourceFactory;
            }
            if (PrismSettings.disableBadDriverWarning) continue;
            D3DPipeline.printDriverWarning(i);
        }
        return null;
    }

    @Override
    public ResourceFactory getDefaultResourceFactory(List<Screen> list) {
        if (this._default == null) {
            this._default = D3DPipeline.findDefaultResourceFactory(list);
        }
        return this._default;
    }

    @Override
    public ResourceFactory getResourceFactory(Screen screen) {
        return D3DPipeline.getD3DResourceFactory(screen.getAdapterOrdinal(), screen);
    }

    @Override
    public boolean is3DSupported() {
        return true;
    }

    int getMaxSamples() {
        if (this.maxSamples < 0) {
            this.isMSAASupported();
        }
        return this.maxSamples;
    }

    @Override
    public boolean isMSAASupported() {
        if (this.maxSamples < 0) {
            this.maxSamples = D3DPipeline.nGetMaxSampleSupport(0);
        }
        return this.maxSamples > 0;
    }

    @Override
    public boolean isVsyncSupported() {
        return true;
    }

    @Override
    public boolean supportsShaderType(GraphicsPipeline.ShaderType shaderType) {
        switch (shaderType) {
            case HLSL: {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean supportsShaderModel(GraphicsPipeline.ShaderModel shaderModel) {
        switch (shaderModel) {
            case SM3: {
                return true;
            }
        }
        return false;
    }

    static {
        boolean bl;
        boolean bl2;
        d3dEnabled = bl2 = AccessController.doPrivileged(() -> {
            if (PrismSettings.verbose) {
                System.out.println("Loading D3D native library ...");
            }
            NativeLibLoader.loadLibrary("prism_d3d");
            if (PrismSettings.verbose) {
                System.out.println("\tsucceeded.");
            }
            return D3DPipeline.nInit(PrismSettings.class, true);
        }).booleanValue();
        if (PrismSettings.verbose) {
            System.out.println("Direct3D initialization " + (d3dEnabled ? "succeeded" : "failed"));
        }
        boolean bl3 = bl = PrismSettings.verbose || !PrismSettings.disableBadDriverWarning;
        if (!d3dEnabled && bl) {
            if (PrismSettings.verbose) {
                System.out.println(D3DPipeline.nGetErrorMessage());
            }
            D3DPipeline.printDriverWarnings();
        }
        creator = Thread.currentThread();
        if (d3dEnabled) {
            d3dInitialized = true;
            theInstance = new D3DPipeline();
            factories = new D3DResourceFactory[D3DPipeline.nGetAdapterCount()];
        }
    }
}

