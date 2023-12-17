// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.shader;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.resources.IResource;
import java.io.Closeable;
import net.minecraft.client.renderer.OpenGlHelper;
import com.google.gson.JsonObject;
import net.minecraft.client.util.JsonException;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import net.minecraft.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import net.minecraft.util.ResourceLocation;
import com.google.gson.JsonParser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonBlendingMode;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class ShaderManager
{
    private static final Logger LOGGER;
    private static final ShaderDefault DEFAULT_SHADER_UNIFORM;
    private static ShaderManager staticShaderManager;
    private static int currentProgram;
    private static boolean lastCull;
    private final Map<String, Object> shaderSamplers;
    private final List<String> samplerNames;
    private final List<Integer> shaderSamplerLocations;
    private final List<ShaderUniform> shaderUniforms;
    private final List<Integer> shaderUniformLocations;
    private final Map<String, ShaderUniform> mappedShaderUniforms;
    private final int program;
    private final String programFilename;
    private final boolean useFaceCulling;
    private boolean isDirty;
    private final JsonBlendingMode blendingMode;
    private final List<Integer> attribLocations;
    private final List<String> attributes;
    private final ShaderLoader vertexShaderLoader;
    private final ShaderLoader fragmentShaderLoader;
    
    public ShaderManager(final IResourceManager resourceManager, final String programName) throws JsonException, IOException {
        this.shaderSamplers = (Map<String, Object>)Maps.newHashMap();
        this.samplerNames = (List<String>)Lists.newArrayList();
        this.shaderSamplerLocations = (List<Integer>)Lists.newArrayList();
        this.shaderUniforms = (List<ShaderUniform>)Lists.newArrayList();
        this.shaderUniformLocations = (List<Integer>)Lists.newArrayList();
        this.mappedShaderUniforms = (Map<String, ShaderUniform>)Maps.newHashMap();
        final JsonParser jsonparser = new JsonParser();
        final ResourceLocation resourcelocation = new ResourceLocation("shaders/program/" + programName + ".json");
        this.programFilename = programName;
        IResource iresource = null;
        try {
            iresource = resourceManager.getResource(resourcelocation);
            final JsonObject jsonobject = jsonparser.parse(IOUtils.toString(iresource.getInputStream(), StandardCharsets.UTF_8)).getAsJsonObject();
            final String s = JsonUtils.getString(jsonobject, "vertex");
            final String s2 = JsonUtils.getString(jsonobject, "fragment");
            final JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "samplers", null);
            if (jsonarray != null) {
                int i = 0;
                for (final JsonElement jsonelement : jsonarray) {
                    try {
                        this.parseSampler(jsonelement);
                    }
                    catch (Exception exception2) {
                        final JsonException jsonexception1 = JsonException.forException(exception2);
                        jsonexception1.prependJsonKey("samplers[" + i + "]");
                        throw jsonexception1;
                    }
                    ++i;
                }
            }
            final JsonArray jsonarray2 = JsonUtils.getJsonArray(jsonobject, "attributes", null);
            if (jsonarray2 != null) {
                int j = 0;
                this.attribLocations = (List<Integer>)Lists.newArrayListWithCapacity(jsonarray2.size());
                this.attributes = (List<String>)Lists.newArrayListWithCapacity(jsonarray2.size());
                for (final JsonElement jsonelement2 : jsonarray2) {
                    try {
                        this.attributes.add(JsonUtils.getString(jsonelement2, "attribute"));
                    }
                    catch (Exception exception3) {
                        final JsonException jsonexception2 = JsonException.forException(exception3);
                        jsonexception2.prependJsonKey("attributes[" + j + "]");
                        throw jsonexception2;
                    }
                    ++j;
                }
            }
            else {
                this.attribLocations = null;
                this.attributes = null;
            }
            final JsonArray jsonarray3 = JsonUtils.getJsonArray(jsonobject, "uniforms", null);
            if (jsonarray3 != null) {
                int k = 0;
                for (final JsonElement jsonelement3 : jsonarray3) {
                    try {
                        this.parseUniform(jsonelement3);
                    }
                    catch (Exception exception4) {
                        final JsonException jsonexception3 = JsonException.forException(exception4);
                        jsonexception3.prependJsonKey("uniforms[" + k + "]");
                        throw jsonexception3;
                    }
                    ++k;
                }
            }
            this.blendingMode = JsonBlendingMode.parseBlendNode(JsonUtils.getJsonObject(jsonobject, "blend", null));
            this.useFaceCulling = JsonUtils.getBoolean(jsonobject, "cull", true);
            this.vertexShaderLoader = ShaderLoader.loadShader(resourceManager, ShaderLoader.ShaderType.VERTEX, s);
            this.fragmentShaderLoader = ShaderLoader.loadShader(resourceManager, ShaderLoader.ShaderType.FRAGMENT, s2);
            this.program = ShaderLinkHelper.getStaticShaderLinkHelper().createProgram();
            ShaderLinkHelper.getStaticShaderLinkHelper().linkProgram(this);
            this.setupUniforms();
            if (this.attributes != null) {
                for (final String s3 : this.attributes) {
                    final int l = OpenGlHelper.glGetAttribLocation(this.program, s3);
                    this.attribLocations.add(l);
                }
            }
        }
        catch (Exception exception5) {
            final JsonException jsonexception4 = JsonException.forException(exception5);
            jsonexception4.setFilenameAndFlush(resourcelocation.getPath());
            throw jsonexception4;
        }
        finally {
            IOUtils.closeQuietly((Closeable)iresource);
        }
        this.markDirty();
    }
    
    public void deleteShader() {
        ShaderLinkHelper.getStaticShaderLinkHelper().deleteShader(this);
    }
    
    public void endShader() {
        OpenGlHelper.glUseProgram(0);
        ShaderManager.currentProgram = -1;
        ShaderManager.staticShaderManager = null;
        ShaderManager.lastCull = true;
        for (int i = 0; i < this.shaderSamplerLocations.size(); ++i) {
            if (this.shaderSamplers.get(this.samplerNames.get(i)) != null) {
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + i);
                GlStateManager.bindTexture(0);
            }
        }
    }
    
    public void useShader() {
        this.isDirty = false;
        ShaderManager.staticShaderManager = this;
        this.blendingMode.apply();
        if (this.program != ShaderManager.currentProgram) {
            OpenGlHelper.glUseProgram(this.program);
            ShaderManager.currentProgram = this.program;
        }
        if (this.useFaceCulling) {
            GlStateManager.enableCull();
        }
        else {
            GlStateManager.disableCull();
        }
        for (int i = 0; i < this.shaderSamplerLocations.size(); ++i) {
            if (this.shaderSamplers.get(this.samplerNames.get(i)) != null) {
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + i);
                GlStateManager.enableTexture2D();
                final Object object = this.shaderSamplers.get(this.samplerNames.get(i));
                int j = -1;
                if (object instanceof Framebuffer) {
                    j = ((Framebuffer)object).framebufferTexture;
                }
                else if (object instanceof ITextureObject) {
                    j = ((ITextureObject)object).getGlTextureId();
                }
                else if (object instanceof Integer) {
                    j = (int)object;
                }
                if (j != -1) {
                    GlStateManager.bindTexture(j);
                    OpenGlHelper.glUniform1i(OpenGlHelper.glGetUniformLocation(this.program, this.samplerNames.get(i)), i);
                }
            }
        }
        for (final ShaderUniform shaderuniform : this.shaderUniforms) {
            shaderuniform.upload();
        }
    }
    
    public void markDirty() {
        this.isDirty = true;
    }
    
    @Nullable
    public ShaderUniform getShaderUniform(final String name) {
        return this.mappedShaderUniforms.get(name);
    }
    
    public ShaderUniform getShaderUniformOrDefault(final String name) {
        final ShaderUniform shaderuniform = this.getShaderUniform(name);
        return (shaderuniform == null) ? ShaderManager.DEFAULT_SHADER_UNIFORM : shaderuniform;
    }
    
    private void setupUniforms() {
        for (int i = 0, j = 0; i < this.samplerNames.size(); ++i, ++j) {
            final String s = this.samplerNames.get(i);
            final int k = OpenGlHelper.glGetUniformLocation(this.program, s);
            if (k == -1) {
                ShaderManager.LOGGER.warn("Shader {}could not find sampler named {} in the specified shader program.", (Object)this.programFilename, (Object)s);
                this.shaderSamplers.remove(s);
                this.samplerNames.remove(j);
                --j;
            }
            else {
                this.shaderSamplerLocations.add(k);
            }
        }
        for (final ShaderUniform shaderuniform : this.shaderUniforms) {
            final String s2 = shaderuniform.getShaderName();
            final int l = OpenGlHelper.glGetUniformLocation(this.program, s2);
            if (l == -1) {
                ShaderManager.LOGGER.warn("Could not find uniform named {} in the specified shader program.", (Object)s2);
            }
            else {
                this.shaderUniformLocations.add(l);
                shaderuniform.setUniformLocation(l);
                this.mappedShaderUniforms.put(s2, shaderuniform);
            }
        }
    }
    
    private void parseSampler(final JsonElement element) throws JsonException {
        final JsonObject jsonobject = JsonUtils.getJsonObject(element, "sampler");
        final String s = JsonUtils.getString(jsonobject, "name");
        if (!JsonUtils.isString(jsonobject, "file")) {
            this.shaderSamplers.put(s, null);
            this.samplerNames.add(s);
        }
        else {
            this.samplerNames.add(s);
        }
    }
    
    public void addSamplerTexture(final String name, final Object samplerTexture) {
        if (this.shaderSamplers.containsKey(name)) {
            this.shaderSamplers.remove(name);
        }
        this.shaderSamplers.put(name, samplerTexture);
        this.markDirty();
    }
    
    private void parseUniform(final JsonElement element) throws JsonException {
        final JsonObject jsonobject = JsonUtils.getJsonObject(element, "uniform");
        final String s = JsonUtils.getString(jsonobject, "name");
        final int i = ShaderUniform.parseType(JsonUtils.getString(jsonobject, "type"));
        final int j = JsonUtils.getInt(jsonobject, "count");
        final float[] afloat = new float[Math.max(j, 16)];
        final JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "values");
        if (jsonarray.size() != j && jsonarray.size() > 1) {
            throw new JsonException("Invalid amount of values specified (expected " + j + ", found " + jsonarray.size() + ")");
        }
        int k = 0;
        for (final JsonElement jsonelement : jsonarray) {
            try {
                afloat[k] = JsonUtils.getFloat(jsonelement, "value");
            }
            catch (Exception exception) {
                final JsonException jsonexception = JsonException.forException(exception);
                jsonexception.prependJsonKey("values[" + k + "]");
                throw jsonexception;
            }
            ++k;
        }
        if (j > 1 && jsonarray.size() == 1) {
            while (k < j) {
                afloat[k] = afloat[0];
                ++k;
            }
        }
        final int l = (j > 1 && j <= 4 && i < 8) ? (j - 1) : 0;
        final ShaderUniform shaderuniform = new ShaderUniform(s, i + l, j, this);
        if (i <= 3) {
            shaderuniform.set((int)afloat[0], (int)afloat[1], (int)afloat[2], (int)afloat[3]);
        }
        else if (i <= 7) {
            shaderuniform.setSafe(afloat[0], afloat[1], afloat[2], afloat[3]);
        }
        else {
            shaderuniform.set(afloat);
        }
        this.shaderUniforms.add(shaderuniform);
    }
    
    public ShaderLoader getVertexShaderLoader() {
        return this.vertexShaderLoader;
    }
    
    public ShaderLoader getFragmentShaderLoader() {
        return this.fragmentShaderLoader;
    }
    
    public int getProgram() {
        return this.program;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DEFAULT_SHADER_UNIFORM = new ShaderDefault();
        ShaderManager.currentProgram = -1;
        ShaderManager.lastCull = true;
    }
}
