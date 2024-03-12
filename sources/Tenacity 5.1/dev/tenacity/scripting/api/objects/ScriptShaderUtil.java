package dev.tenacity.scripting.api.objects;

import dev.tenacity.utils.render.ShaderUtil;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude(Strategy.NAME_REMAPPING)
public class ScriptShaderUtil {
    private final ShaderUtil shaderUtil;

    public ScriptShaderUtil(String fragSource) {
        this.shaderUtil = new ShaderUtil(fragSource, false);
    }

    public void init() {
        shaderUtil.init();
    }

    public void unload() {
        shaderUtil.unload();
    }

    public void setUniformf(String name, float... args) {
        shaderUtil.setUniformf(name, args);
    }

    public void setUniformi(String name, int... args) {
        shaderUtil.setUniformi(name, args);
    }

    public void drawQuads() {
        ShaderUtil.drawQuads();
    }

    public void drawQuads(float x, float y, float width, float height) {
        ShaderUtil.drawQuads(x, y, width, height);
    }
}
