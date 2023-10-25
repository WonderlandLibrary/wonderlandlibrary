/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.openal;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCcontext;
import org.lwjgl.openal.ALCdevice;
import org.lwjgl.openal.EFX10;

public final class AL {
    static ALCdevice device;
    static ALCcontext context;
    private static boolean created;

    private AL() {
    }

    private static native void nCreate(String var0) throws LWJGLException;

    private static native void nCreateDefault() throws LWJGLException;

    private static native void nDestroy();

    public static boolean isCreated() {
        return created;
    }

    public static void create(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized) throws LWJGLException {
        AL.create(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, true);
    }

    public static void create(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized, boolean openDevice) throws LWJGLException {
        String[] library_names;
        String libname;
        if (created) {
            throw new IllegalStateException("Only one OpenAL context may be instantiated at any one time.");
        }
        switch (LWJGLUtil.getPlatform()) {
            case 3: {
                if (Sys.is64Bit()) {
                    libname = "OpenAL64";
                    library_names = new String[]{"OpenAL64.dll"};
                    break;
                }
                libname = "OpenAL32";
                library_names = new String[]{"OpenAL32.dll"};
                break;
            }
            case 1: {
                libname = "openal";
                library_names = new String[]{"libopenal64.so", "libopenal.so", "libopenal.so.0"};
                break;
            }
            case 2: {
                libname = "openal";
                library_names = new String[]{"openal.dylib"};
                break;
            }
            default: {
                throw new LWJGLException("Unknown platform: " + LWJGLUtil.getPlatform());
            }
        }
        String[] oalPaths = LWJGLUtil.getLibraryPaths(libname, library_names, AL.class.getClassLoader());
        LWJGLUtil.log("Found " + oalPaths.length + " OpenAL paths");
        for (String oalPath : oalPaths) {
            try {
                AL.nCreate(oalPath);
                created = true;
                AL.init(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, openDevice);
                break;
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Failed to load " + oalPath + ": " + e.getMessage());
            }
        }
        if (!created && LWJGLUtil.getPlatform() == 2) {
            AL.nCreateDefault();
            created = true;
            AL.init(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, openDevice);
        }
        if (!created) {
            throw new LWJGLException("Could not locate OpenAL library.");
        }
    }

    private static void init(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized, boolean openDevice) throws LWJGLException {
        try {
            AL10.initNativeStubs();
            ALC10.initNativeStubs();
            if (openDevice) {
                device = ALC10.alcOpenDevice(deviceArguments);
                if (device == null) {
                    throw new LWJGLException("Could not open ALC device");
                }
                context = contextFrequency == -1 ? ALC10.alcCreateContext(device, null) : ALC10.alcCreateContext(device, ALCcontext.createAttributeList(contextFrequency, contextRefresh, contextSynchronized ? 1 : 0));
                ALC10.alcMakeContextCurrent(context);
            }
        }
        catch (LWJGLException e) {
            AL.destroy();
            throw e;
        }
        ALC11.initialize();
        if (ALC10.alcIsExtensionPresent(device, "ALC_EXT_EFX")) {
            EFX10.initNativeStubs();
        }
    }

    public static void create() throws LWJGLException {
        AL.create(null, 44100, 60, false);
    }

    public static void destroy() {
        if (context != null) {
            ALC10.alcMakeContextCurrent(null);
            ALC10.alcDestroyContext(context);
            context = null;
        }
        if (device != null) {
            boolean result = ALC10.alcCloseDevice(device);
            device = null;
        }
        AL.resetNativeStubs(AL10.class);
        AL.resetNativeStubs(AL11.class);
        AL.resetNativeStubs(ALC10.class);
        AL.resetNativeStubs(ALC11.class);
        AL.resetNativeStubs(EFX10.class);
        if (created) {
            AL.nDestroy();
        }
        created = false;
    }

    private static native void resetNativeStubs(Class var0);

    public static ALCcontext getContext() {
        return context;
    }

    public static ALCdevice getDevice() {
        return device;
    }

    static {
        Sys.initialize();
    }
}

