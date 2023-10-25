/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.ps;

import com.sun.prism.ResourceFactory;
import com.sun.prism.ps.Shader;
import java.io.InputStream;
import java.util.Map;

public interface ShaderFactory
extends ResourceFactory {
    public Shader createShader(InputStream var1, Map<String, Integer> var2, Map<String, Integer> var3, int var4, boolean var5, boolean var6);

    public Shader createStockShader(String var1);
}

