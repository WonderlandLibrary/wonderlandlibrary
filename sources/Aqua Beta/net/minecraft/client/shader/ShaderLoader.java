package net.minecraft.client.shader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.ShaderLoader;
import net.minecraft.client.shader.ShaderManager;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;

public class ShaderLoader {
    private final ShaderType shaderType;
    private final String shaderFilename;
    private int shader;
    private int shaderAttachCount = 0;

    private ShaderLoader(ShaderType type, int shaderId, String filename) {
        this.shaderType = type;
        this.shader = shaderId;
        this.shaderFilename = filename;
    }

    public void attachShader(ShaderManager manager) {
        ++this.shaderAttachCount;
        OpenGlHelper.glAttachShader((int)manager.getProgram(), (int)this.shader);
    }

    public void deleteShader(ShaderManager manager) {
        --this.shaderAttachCount;
        if (this.shaderAttachCount <= 0) {
            OpenGlHelper.glDeleteShader((int)this.shader);
            this.shaderType.getLoadedShaders().remove((Object)this.shaderFilename);
        }
    }

    public String getShaderFilename() {
        return this.shaderFilename;
    }

    public static ShaderLoader loadShader(IResourceManager resourceManager, ShaderType type, String filename) throws IOException {
        ShaderLoader shaderloader = (ShaderLoader)type.getLoadedShaders().get((Object)filename);
        if (shaderloader == null) {
            ResourceLocation resourcelocation = new ResourceLocation("shaders/program/" + filename + type.getShaderExtension());
            BufferedInputStream bufferedinputstream = new BufferedInputStream(resourceManager.getResource(resourcelocation).getInputStream());
            byte[] abyte = ShaderLoader.toByteArray(bufferedinputstream);
            ByteBuffer bytebuffer = BufferUtils.createByteBuffer((int)abyte.length);
            bytebuffer.put(abyte);
            bytebuffer.position(0);
            int i = OpenGlHelper.glCreateShader((int)type.getShaderMode());
            OpenGlHelper.glShaderSource((int)i, (ByteBuffer)bytebuffer);
            OpenGlHelper.glCompileShader((int)i);
            if (OpenGlHelper.glGetShaderi((int)i, (int)OpenGlHelper.GL_COMPILE_STATUS) == 0) {
                String s = StringUtils.trim((String)OpenGlHelper.glGetShaderInfoLog((int)i, (int)32768));
                JsonException jsonexception = new JsonException("Couldn't compile " + type.getShaderName() + " program: " + s);
                jsonexception.func_151381_b(resourcelocation.getResourcePath());
                throw jsonexception;
            }
            shaderloader = new ShaderLoader(type, i, filename);
            type.getLoadedShaders().put((Object)filename, (Object)shaderloader);
        }
        return shaderloader;
    }

    protected static byte[] toByteArray(BufferedInputStream p_177064_0_) throws IOException {
        byte[] abyte;
        try {
            abyte = IOUtils.toByteArray((InputStream)p_177064_0_);
        }
        finally {
            p_177064_0_.close();
        }
        return abyte;
    }
}
