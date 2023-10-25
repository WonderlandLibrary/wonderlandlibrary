/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class AlphaTextureDifference_RadialGradient_Loader {
    private AlphaTextureDifference_RadialGradient_Loader() {
    }

    public static Shader loadShader(ShaderFactory shaderFactory, InputStream inputStream) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("maskInput", 0);
        hashMap.put("colors", 1);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("innerOffset", 0);
        hashMap2.put("precalc", 2);
        hashMap2.put("content", 1);
        return shaderFactory.createShader(inputStream, hashMap, hashMap2, 1, false, true);
    }
}

