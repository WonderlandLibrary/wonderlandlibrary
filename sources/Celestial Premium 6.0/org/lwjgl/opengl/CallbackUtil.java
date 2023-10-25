/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GLContext;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
final class CallbackUtil {
    private static final Map<ContextCapabilities, Long> contextUserParamsARB = new HashMap<ContextCapabilities, Long>();
    private static final Map<ContextCapabilities, Long> contextUserParamsAMD = new HashMap<ContextCapabilities, Long>();
    private static final Map<ContextCapabilities, Long> contextUserParamsKHR = new HashMap<ContextCapabilities, Long>();

    private CallbackUtil() {
    }

    static long createGlobalRef(Object obj) {
        return obj == null ? 0L : CallbackUtil.ncreateGlobalRef(obj);
    }

    private static native long ncreateGlobalRef(Object var0);

    private static native void deleteGlobalRef(long var0);

    private static void registerContextCallback(long userParam, Map<ContextCapabilities, Long> contextUserData) {
        ContextCapabilities caps = GLContext.getCapabilities();
        if (caps == null) {
            CallbackUtil.deleteGlobalRef(userParam);
            throw new IllegalStateException("No context is current.");
        }
        Long userParam_old = contextUserData.remove(caps);
        if (userParam_old != null) {
            CallbackUtil.deleteGlobalRef(userParam_old);
        }
        if (userParam != 0L) {
            contextUserData.put(caps, userParam);
        }
    }

    static void unregisterCallbacks(Object context) {
        ContextCapabilities caps = GLContext.getCapabilities(context);
        Long userParam = contextUserParamsARB.remove(caps);
        if (userParam != null) {
            CallbackUtil.deleteGlobalRef(userParam);
        }
        if ((userParam = contextUserParamsAMD.remove(caps)) != null) {
            CallbackUtil.deleteGlobalRef(userParam);
        }
        if ((userParam = contextUserParamsKHR.remove(caps)) != null) {
            CallbackUtil.deleteGlobalRef(userParam);
        }
    }

    static native long getDebugOutputCallbackARB();

    static void registerContextCallbackARB(long userParam) {
        CallbackUtil.registerContextCallback(userParam, contextUserParamsARB);
    }

    static native long getDebugOutputCallbackAMD();

    static void registerContextCallbackAMD(long userParam) {
        CallbackUtil.registerContextCallback(userParam, contextUserParamsAMD);
    }

    static native long getDebugCallbackKHR();

    static void registerContextCallbackKHR(long userParam) {
        CallbackUtil.registerContextCallback(userParam, contextUserParamsKHR);
    }
}

