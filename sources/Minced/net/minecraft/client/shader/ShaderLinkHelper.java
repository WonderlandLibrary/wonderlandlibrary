// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.shader;

import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import net.minecraft.client.util.JsonException;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.logging.log4j.Logger;

public class ShaderLinkHelper
{
    private static final Logger LOGGER;
    private static ShaderLinkHelper staticShaderLinkHelper;
    
    public static void setNewStaticShaderLinkHelper() {
        ShaderLinkHelper.staticShaderLinkHelper = new ShaderLinkHelper();
    }
    
    public static ShaderLinkHelper getStaticShaderLinkHelper() {
        return ShaderLinkHelper.staticShaderLinkHelper;
    }
    
    public void deleteShader(final ShaderManager manager) {
        manager.getFragmentShaderLoader().deleteShader(manager);
        manager.getVertexShaderLoader().deleteShader(manager);
        OpenGlHelper.glDeleteProgram(manager.getProgram());
    }
    
    public int createProgram() throws JsonException {
        final int i = OpenGlHelper.glCreateProgram();
        if (i <= 0) {
            throw new JsonException("Could not create shader program (returned program ID " + i + ")");
        }
        return i;
    }
    
    public void linkProgram(final ShaderManager manager) throws IOException {
        manager.getFragmentShaderLoader().attachShader(manager);
        manager.getVertexShaderLoader().attachShader(manager);
        OpenGlHelper.glLinkProgram(manager.getProgram());
        final int i = OpenGlHelper.glGetProgrami(manager.getProgram(), OpenGlHelper.GL_LINK_STATUS);
        if (i == 0) {
            ShaderLinkHelper.LOGGER.warn("Error encountered when linking program containing VS {} and FS {}. Log output:", (Object)manager.getVertexShaderLoader().getShaderFilename(), (Object)manager.getFragmentShaderLoader().getShaderFilename());
            ShaderLinkHelper.LOGGER.warn(OpenGlHelper.glGetProgramInfoLog(manager.getProgram(), 32768));
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
