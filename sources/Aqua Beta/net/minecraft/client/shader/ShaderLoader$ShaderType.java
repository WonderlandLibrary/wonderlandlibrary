package net.minecraft.client.shader;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderLoader;

public static enum ShaderLoader.ShaderType {
    VERTEX("vertex", ".vsh", OpenGlHelper.GL_VERTEX_SHADER),
    FRAGMENT("fragment", ".fsh", OpenGlHelper.GL_FRAGMENT_SHADER);

    private final String shaderName;
    private final String shaderExtension;
    private final int shaderMode;
    private final Map<String, ShaderLoader> loadedShaders = Maps.newHashMap();

    private ShaderLoader.ShaderType(String p_i45090_3_, String p_i45090_4_, int p_i45090_5_) {
        this.shaderName = p_i45090_3_;
        this.shaderExtension = p_i45090_4_;
        this.shaderMode = p_i45090_5_;
    }

    public String getShaderName() {
        return this.shaderName;
    }

    protected String getShaderExtension() {
        return this.shaderExtension;
    }

    protected int getShaderMode() {
        return this.shaderMode;
    }

    protected Map<String, ShaderLoader> getLoadedShaders() {
        return this.loadedShaders;
    }
}
