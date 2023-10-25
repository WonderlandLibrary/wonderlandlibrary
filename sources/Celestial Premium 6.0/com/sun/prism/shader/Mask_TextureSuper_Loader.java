/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class Mask_TextureSuper_Loader {
    private Mask_TextureSuper_Loader() {
    }

    public static Shader loadShader(ShaderFactory shaderFactory, InputStream inputStream) {
        HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put("inputTex1", 1);
        hashMap.put("inputTex0", 0);
        HashMap<String, Integer> hashMap2 = new HashMap<String, Integer>();
        return shaderFactory.createShader(inputStream, hashMap, hashMap2, 1, false, true);
    }
}

