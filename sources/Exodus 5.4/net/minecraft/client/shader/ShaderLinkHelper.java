/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.shader;

import java.io.IOException;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderManager;
import net.minecraft.client.util.JsonException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShaderLinkHelper {
    private static ShaderLinkHelper staticShaderLinkHelper;
    private static final Logger logger;

    public void deleteShader(ShaderManager shaderManager) {
        shaderManager.getFragmentShaderLoader().deleteShader(shaderManager);
        shaderManager.getVertexShaderLoader().deleteShader(shaderManager);
        OpenGlHelper.glDeleteProgram(shaderManager.getProgram());
    }

    public static void setNewStaticShaderLinkHelper() {
        staticShaderLinkHelper = new ShaderLinkHelper();
    }

    public static ShaderLinkHelper getStaticShaderLinkHelper() {
        return staticShaderLinkHelper;
    }

    public int createProgram() throws JsonException {
        int n = OpenGlHelper.glCreateProgram();
        if (n <= 0) {
            throw new JsonException("Could not create shader program (returned program ID " + n + ")");
        }
        return n;
    }

    public void linkProgram(ShaderManager shaderManager) throws IOException {
        shaderManager.getFragmentShaderLoader().attachShader(shaderManager);
        shaderManager.getVertexShaderLoader().attachShader(shaderManager);
        OpenGlHelper.glLinkProgram(shaderManager.getProgram());
        int n = OpenGlHelper.glGetProgrami(shaderManager.getProgram(), OpenGlHelper.GL_LINK_STATUS);
        if (n == 0) {
            logger.warn("Error encountered when linking program containing VS " + shaderManager.getVertexShaderLoader().getShaderFilename() + " and FS " + shaderManager.getFragmentShaderLoader().getShaderFilename() + ". Log output:");
            logger.warn(OpenGlHelper.glGetProgramInfoLog(shaderManager.getProgram(), 32768));
        }
    }

    static {
        logger = LogManager.getLogger();
    }
}

