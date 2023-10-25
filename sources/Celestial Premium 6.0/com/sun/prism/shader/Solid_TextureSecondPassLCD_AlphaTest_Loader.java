/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class Solid_TextureSecondPassLCD_AlphaTest_Loader {
    private Solid_TextureSecondPassLCD_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory shaderFactory, InputStream inputStream) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("dstColor", 0);
        hashMap.put("glyphColor", 1);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        hashMap2.put("gamma", 0);
        return shaderFactory.createShader(inputStream, hashMap, hashMap2, 1, false, true);
    }
}

