/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class FillRoundRect_LinearGradient_PAD_AlphaTest_Loader {
    private FillRoundRect_LinearGradient_PAD_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory shaderFactory, InputStream inputStream) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("colors", 0);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("perspVec", 15);
        hashMap2.put("gradParams", 14);
        hashMap2.put("offset", 13);
        hashMap2.put("fractions", 1);
        hashMap2.put("oinvarcradii", 0);
        return shaderFactory.createShader(inputStream, hashMap, hashMap2, 1, true, true);
    }
}

