/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class Solid_TextureYV12_AlphaTest_Loader {
    private Solid_TextureYV12_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory shaderFactory, InputStream inputStream) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("crTex", 1);
        hashMap.put("alphaTex", 3);
        hashMap.put("cbTex", 2);
        hashMap.put("lumaTex", 0);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("cbCrScale", 1);
        hashMap2.put("lumaAlphaScale", 0);
        return shaderFactory.createShader(inputStream, hashMap, hashMap2, 0, false, true);
    }
}

