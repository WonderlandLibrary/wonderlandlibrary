/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class Texture_RadialGradient_PAD_AlphaTest_Loader {
    private Texture_RadialGradient_PAD_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory shaderFactory, InputStream inputStream) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("maskInput", 0);
        hashMap.put("colors", 1);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("perspVec", 16);
        hashMap2.put("m0", 13);
        hashMap2.put("offset", 12);
        hashMap2.put("m1", 14);
        hashMap2.put("precalc", 15);
        hashMap2.put("fractions", 0);
        return shaderFactory.createShader(inputStream, hashMap, hashMap2, 0, true, true);
    }
}

