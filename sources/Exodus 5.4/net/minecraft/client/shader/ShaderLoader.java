/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  org.apache.commons.io.IOUtils
 *  org.apache.commons.lang3.StringUtils
 *  org.lwjgl.BufferUtils
 */
package net.minecraft.client.shader;

import com.google.common.collect.Maps;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.ShaderManager;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;

public class ShaderLoader {
    private final ShaderType shaderType;
    private final String shaderFilename;
    private int shaderAttachCount = 0;
    private int shader;

    public String getShaderFilename() {
        return this.shaderFilename;
    }

    public static ShaderLoader loadShader(IResourceManager iResourceManager, ShaderType shaderType, String string) throws IOException {
        ShaderLoader shaderLoader = shaderType.getLoadedShaders().get(string);
        if (shaderLoader == null) {
            ResourceLocation resourceLocation = new ResourceLocation("shaders/program/" + string + shaderType.getShaderExtension());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(iResourceManager.getResource(resourceLocation).getInputStream());
            byte[] byArray = ShaderLoader.toByteArray(bufferedInputStream);
            ByteBuffer byteBuffer = BufferUtils.createByteBuffer((int)byArray.length);
            byteBuffer.put(byArray);
            byteBuffer.position(0);
            int n = OpenGlHelper.glCreateShader(shaderType.getShaderMode());
            OpenGlHelper.glShaderSource(n, byteBuffer);
            OpenGlHelper.glCompileShader(n);
            if (OpenGlHelper.glGetShaderi(n, OpenGlHelper.GL_COMPILE_STATUS) == 0) {
                String string2 = StringUtils.trim((String)OpenGlHelper.glGetShaderInfoLog(n, 32768));
                JsonException jsonException = new JsonException("Couldn't compile " + shaderType.getShaderName() + " program: " + string2);
                jsonException.func_151381_b(resourceLocation.getResourcePath());
                throw jsonException;
            }
            shaderLoader = new ShaderLoader(shaderType, n, string);
            shaderType.getLoadedShaders().put(string, shaderLoader);
        }
        return shaderLoader;
    }

    public void deleteShader(ShaderManager shaderManager) {
        --this.shaderAttachCount;
        if (this.shaderAttachCount <= 0) {
            OpenGlHelper.glDeleteShader(this.shader);
            this.shaderType.getLoadedShaders().remove(this.shaderFilename);
        }
    }

    public void attachShader(ShaderManager shaderManager) {
        ++this.shaderAttachCount;
        OpenGlHelper.glAttachShader(shaderManager.getProgram(), this.shader);
    }

    private ShaderLoader(ShaderType shaderType, int n, String string) {
        this.shaderType = shaderType;
        this.shader = n;
        this.shaderFilename = string;
    }

    protected static byte[] toByteArray(BufferedInputStream bufferedInputStream) throws IOException {
        byte[] byArray = IOUtils.toByteArray((InputStream)bufferedInputStream);
        bufferedInputStream.close();
        return byArray;
    }

    public static enum ShaderType {
        VERTEX("vertex", ".vsh", OpenGlHelper.GL_VERTEX_SHADER),
        FRAGMENT("fragment", ".fsh", OpenGlHelper.GL_FRAGMENT_SHADER);

        private final String shaderName;
        private final int shaderMode;
        private final String shaderExtension;
        private final Map<String, ShaderLoader> loadedShaders = Maps.newHashMap();

        protected Map<String, ShaderLoader> getLoadedShaders() {
            return this.loadedShaders;
        }

        protected int getShaderMode() {
            return this.shaderMode;
        }

        protected String getShaderExtension() {
            return this.shaderExtension;
        }

        private ShaderType(String string2, String string3, int n2) {
            this.shaderName = string2;
            this.shaderExtension = string3;
            this.shaderMode = n2;
        }

        public String getShaderName() {
            return this.shaderName;
        }
    }
}

