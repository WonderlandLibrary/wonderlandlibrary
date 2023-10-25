/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class Texture_ImagePattern_AlphaTest_Loader {
    private Texture_ImagePattern_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory shaderFactory, InputStream inputStream) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("inputTex", 1);
        hashMap.put("maskInput", 0);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("perspVec", 2);
        hashMap2.put("xParams", 0);
        hashMap2.put("yParams", 1);
        hashMap2.put("content", 3);
        return shaderFactory.createShader(inputStream, hashMap, hashMap2, 0, true, true);
    }
}

