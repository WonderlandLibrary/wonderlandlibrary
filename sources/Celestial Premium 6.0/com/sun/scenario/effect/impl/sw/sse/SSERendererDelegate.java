/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.sse;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.sw.RendererDelegate;
import java.security.AccessController;

public class SSERendererDelegate
implements RendererDelegate {
    public static native boolean isSupported();

    public SSERendererDelegate() {
        if (!SSERendererDelegate.isSupported()) {
            throw new UnsupportedOperationException("required instruction set (SSE2) not supported on this processor");
        }
    }

    @Override
    public Effect.AccelType getAccelType() {
        return Effect.AccelType.SIMD;
    }

    @Override
    public String getPlatformPeerName(String string, int n) {
        return "com.sun.scenario.effect.impl.sw.sse.SSE" + string + "Peer";
    }

    static {
        Object object = AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("decora_sse");
            return null;
        });
    }
}

