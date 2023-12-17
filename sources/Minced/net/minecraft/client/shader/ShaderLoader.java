// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.shader;

import com.google.common.collect.Maps;
import java.util.Map;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraft.client.resources.IResource;
import java.io.Closeable;
import net.minecraft.client.util.JsonException;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.io.BufferedInputStream;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.OpenGlHelper;

public class ShaderLoader
{
    private final ShaderType shaderType;
    private final String shaderFilename;
    private final int shader;
    private int shaderAttachCount;
    
    private ShaderLoader(final ShaderType type, final int shaderId, final String filename) {
        this.shaderType = type;
        this.shader = shaderId;
        this.shaderFilename = filename;
    }
    
    public void attachShader(final ShaderManager manager) {
        ++this.shaderAttachCount;
        OpenGlHelper.glAttachShader(manager.getProgram(), this.shader);
    }
    
    public void deleteShader(final ShaderManager manager) {
        --this.shaderAttachCount;
        if (this.shaderAttachCount <= 0) {
            OpenGlHelper.glDeleteShader(this.shader);
            this.shaderType.getLoadedShaders().remove(this.shaderFilename);
        }
    }
    
    public String getShaderFilename() {
        return this.shaderFilename;
    }
    
    public static ShaderLoader loadShader(final IResourceManager resourceManager, final ShaderType type, final String filename) throws IOException {
        ShaderLoader shaderloader = type.getLoadedShaders().get(filename);
        if (shaderloader == null) {
            final ResourceLocation resourcelocation = new ResourceLocation("shaders/program/" + filename + type.getShaderExtension());
            final IResource iresource = resourceManager.getResource(resourcelocation);
            try {
                final byte[] abyte = IOUtils.toByteArray((InputStream)new BufferedInputStream(iresource.getInputStream()));
                final ByteBuffer bytebuffer = BufferUtils.createByteBuffer(abyte.length);
                bytebuffer.put(abyte);
                bytebuffer.position(0);
                final int i = OpenGlHelper.glCreateShader(type.getShaderMode());
                OpenGlHelper.glShaderSource(i, bytebuffer);
                OpenGlHelper.glCompileShader(i);
                if (OpenGlHelper.glGetShaderi(i, OpenGlHelper.GL_COMPILE_STATUS) == 0) {
                    final String s = StringUtils.trim(OpenGlHelper.glGetShaderInfoLog(i, 32768));
                    final JsonException jsonexception = new JsonException("Couldn't compile " + type.getShaderName() + " program: " + s);
                    jsonexception.setFilenameAndFlush(resourcelocation.getPath());
                    throw jsonexception;
                }
                shaderloader = new ShaderLoader(type, i, filename);
                type.getLoadedShaders().put(filename, shaderloader);
            }
            finally {
                IOUtils.closeQuietly((Closeable)iresource);
            }
        }
        return shaderloader;
    }
    
    public enum ShaderType
    {
        VERTEX("vertex", ".vsh", OpenGlHelper.GL_VERTEX_SHADER), 
        FRAGMENT("fragment", ".fsh", OpenGlHelper.GL_FRAGMENT_SHADER);
        
        private final String shaderName;
        private final String shaderExtension;
        private final int shaderMode;
        private final Map<String, ShaderLoader> loadedShaders;
        
        private ShaderType(final String shaderNameIn, final String shaderExtensionIn, final int shaderModeIn) {
            this.loadedShaders = (Map<String, ShaderLoader>)Maps.newHashMap();
            this.shaderName = shaderNameIn;
            this.shaderExtension = shaderExtensionIn;
            this.shaderMode = shaderModeIn;
        }
        
        public String getShaderName() {
            return this.shaderName;
        }
        
        private String getShaderExtension() {
            return this.shaderExtension;
        }
        
        private int getShaderMode() {
            return this.shaderMode;
        }
        
        private Map<String, ShaderLoader> getLoadedShaders() {
            return this.loadedShaders;
        }
    }
}
