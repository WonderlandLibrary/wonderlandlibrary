// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.shader;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import java.io.InputStream;
import net.minecraft.client.renderer.OpenGlHelper;
import com.google.gson.JsonObject;
import net.minecraft.client.util.JsonException;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import net.minecraft.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import com.google.common.base.Charsets;
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
    private static final Logger logger;
    private static final ShaderDefault defaultShaderUniform;
    private static ShaderManager staticShaderManager;
    private static int currentProgram;
    private static boolean field_148000_e;
    private final Map shaderSamplers;
    private final List samplerNames;
    private final List shaderSamplerLocations;
    private final List shaderUniforms;
    private final List shaderUniformLocations;
    private final Map mappedShaderUniforms;
    private final int program;
    private final String programFilename;
    private final boolean useFaceCulling;
    private boolean isDirty;
    private final JsonBlendingMode field_148016_p;
    private final List field_148015_q;
    private final List field_148014_r;
    private final ShaderLoader vertexShaderLoader;
    private final ShaderLoader fragmentShaderLoader;
    private static final String __OBFID = "CL_00001040";
    
    public ShaderManager(final IResourceManager resourceManager, final String programName) throws JsonException {
        this.shaderSamplers = Maps.newHashMap();
        this.samplerNames = Lists.newArrayList();
        this.shaderSamplerLocations = Lists.newArrayList();
        this.shaderUniforms = Lists.newArrayList();
        this.shaderUniformLocations = Lists.newArrayList();
        this.mappedShaderUniforms = Maps.newHashMap();
        final JsonParser var3 = new JsonParser();
        final ResourceLocation var4 = new ResourceLocation("shaders/program/" + programName + ".json");
        this.programFilename = programName;
        InputStream var5 = null;
        try {
            var5 = resourceManager.getResource(var4).getInputStream();
            final JsonObject var6 = var3.parse(IOUtils.toString(var5, Charsets.UTF_8)).getAsJsonObject();
            final String var7 = JsonUtils.getJsonObjectStringFieldValue(var6, "vertex");
            final String var8 = JsonUtils.getJsonObjectStringFieldValue(var6, "fragment");
            final JsonArray var9 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var6, "samplers", null);
            if (var9 != null) {
                int var10 = 0;
                for (final JsonElement var12 : var9) {
                    try {
                        this.parseSampler(var12);
                    }
                    catch (Exception var14) {
                        final JsonException var13 = JsonException.func_151379_a(var14);
                        var13.func_151380_a("samplers[" + var10 + "]");
                        throw var13;
                    }
                    ++var10;
                }
            }
            final JsonArray var15 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var6, "attributes", null);
            if (var15 != null) {
                int var16 = 0;
                this.field_148015_q = Lists.newArrayListWithCapacity(var15.size());
                this.field_148014_r = Lists.newArrayListWithCapacity(var15.size());
                for (final JsonElement var18 : var15) {
                    try {
                        this.field_148014_r.add(JsonUtils.getJsonElementStringValue(var18, "attribute"));
                    }
                    catch (Exception var20) {
                        final JsonException var19 = JsonException.func_151379_a(var20);
                        var19.func_151380_a("attributes[" + var16 + "]");
                        throw var19;
                    }
                    ++var16;
                }
            }
            else {
                this.field_148015_q = null;
                this.field_148014_r = null;
            }
            final JsonArray var21 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var6, "uniforms", null);
            if (var21 != null) {
                int var22 = 0;
                for (final JsonElement var24 : var21) {
                    try {
                        this.parseUniform(var24);
                    }
                    catch (Exception var26) {
                        final JsonException var25 = JsonException.func_151379_a(var26);
                        var25.func_151380_a("uniforms[" + var22 + "]");
                        throw var25;
                    }
                    ++var22;
                }
            }
            this.field_148016_p = JsonBlendingMode.func_148110_a(JsonUtils.getJsonObjectFieldOrDefault(var6, "blend", null));
            this.useFaceCulling = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var6, "cull", true);
            this.vertexShaderLoader = ShaderLoader.loadShader(resourceManager, ShaderLoader.ShaderType.VERTEX, var7);
            this.fragmentShaderLoader = ShaderLoader.loadShader(resourceManager, ShaderLoader.ShaderType.FRAGMENT, var8);
            this.program = ShaderLinkHelper.getStaticShaderLinkHelper().createProgram();
            ShaderLinkHelper.getStaticShaderLinkHelper().linkProgram(this);
            this.setupUniforms();
            if (this.field_148014_r != null) {
                for (final String var27 : this.field_148014_r) {
                    final int var28 = OpenGlHelper.glGetAttribLocation(this.program, var27);
                    this.field_148015_q.add(var28);
                }
            }
        }
        catch (Exception var30) {
            final JsonException var29 = JsonException.func_151379_a(var30);
            var29.func_151381_b(var4.getResourcePath());
            throw var29;
        }
        finally {
            IOUtils.closeQuietly(var5);
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
        ShaderManager.field_148000_e = true;
        for (int var1 = 0; var1 < this.shaderSamplerLocations.size(); ++var1) {
            if (this.shaderSamplers.get(this.samplerNames.get(var1)) != null) {
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + var1);
                GlStateManager.func_179144_i(0);
            }
        }
    }
    
    public void useShader() {
        this.isDirty = false;
        ShaderManager.staticShaderManager = this;
        this.field_148016_p.func_148109_a();
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
        for (int var1 = 0; var1 < this.shaderSamplerLocations.size(); ++var1) {
            if (this.shaderSamplers.get(this.samplerNames.get(var1)) != null) {
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + var1);
                GlStateManager.enableTextures();
                final Object var2 = this.shaderSamplers.get(this.samplerNames.get(var1));
                int var3 = -1;
                if (var2 instanceof Framebuffer) {
                    var3 = ((Framebuffer)var2).framebufferTexture;
                }
                else if (var2 instanceof ITextureObject) {
                    var3 = ((ITextureObject)var2).getGlTextureId();
                }
                else if (var2 instanceof Integer) {
                    var3 = (int)var2;
                }
                if (var3 != -1) {
                    GlStateManager.func_179144_i(var3);
                    OpenGlHelper.glUniform1i(OpenGlHelper.glGetUniformLocation(this.program, this.samplerNames.get(var1)), var1);
                }
            }
        }
        for (final ShaderUniform var5 : this.shaderUniforms) {
            var5.upload();
        }
    }
    
    public void markDirty() {
        this.isDirty = true;
    }
    
    public ShaderUniform getShaderUniform(final String p_147991_1_) {
        return this.mappedShaderUniforms.containsKey(p_147991_1_) ? this.mappedShaderUniforms.get(p_147991_1_) : null;
    }
    
    public ShaderUniform getShaderUniformOrDefault(final String p_147984_1_) {
        return this.mappedShaderUniforms.containsKey(p_147984_1_) ? this.mappedShaderUniforms.get(p_147984_1_) : ShaderManager.defaultShaderUniform;
    }
    
    private void setupUniforms() {
        for (int var1 = 0, var2 = 0; var1 < this.samplerNames.size(); ++var1, ++var2) {
            final String var3 = this.samplerNames.get(var1);
            final int var4 = OpenGlHelper.glGetUniformLocation(this.program, var3);
            if (var4 == -1) {
                ShaderManager.logger.warn("Shader " + this.programFilename + "could not find sampler named " + var3 + " in the specified shader program.");
                this.shaderSamplers.remove(var3);
                this.samplerNames.remove(var2);
                --var2;
            }
            else {
                this.shaderSamplerLocations.add(var4);
            }
        }
        for (final ShaderUniform var6 : this.shaderUniforms) {
            final String var3 = var6.getShaderName();
            final int var4 = OpenGlHelper.glGetUniformLocation(this.program, var3);
            if (var4 == -1) {
                ShaderManager.logger.warn("Could not find uniform named " + var3 + " in the specified shader program.");
            }
            else {
                this.shaderUniformLocations.add(var4);
                var6.setUniformLocation(var4);
                this.mappedShaderUniforms.put(var3, var6);
            }
        }
    }
    
    private void parseSampler(final JsonElement p_147996_1_) {
        final JsonObject var2 = JsonUtils.getElementAsJsonObject(p_147996_1_, "sampler");
        final String var3 = JsonUtils.getJsonObjectStringFieldValue(var2, "name");
        if (!JsonUtils.jsonObjectFieldTypeIsString(var2, "file")) {
            this.shaderSamplers.put(var3, null);
            this.samplerNames.add(var3);
        }
        else {
            this.samplerNames.add(var3);
        }
    }
    
    public void addSamplerTexture(final String p_147992_1_, final Object p_147992_2_) {
        if (this.shaderSamplers.containsKey(p_147992_1_)) {
            this.shaderSamplers.remove(p_147992_1_);
        }
        this.shaderSamplers.put(p_147992_1_, p_147992_2_);
        this.markDirty();
    }
    
    private void parseUniform(final JsonElement p_147987_1_) throws JsonException {
        final JsonObject var2 = JsonUtils.getElementAsJsonObject(p_147987_1_, "uniform");
        final String var3 = JsonUtils.getJsonObjectStringFieldValue(var2, "name");
        final int var4 = ShaderUniform.parseType(JsonUtils.getJsonObjectStringFieldValue(var2, "type"));
        final int var5 = JsonUtils.getJsonObjectIntegerFieldValue(var2, "count");
        final float[] var6 = new float[Math.max(var5, 16)];
        final JsonArray var7 = JsonUtils.getJsonObjectJsonArrayField(var2, "values");
        if (var7.size() != var5 && var7.size() > 1) {
            throw new JsonException("Invalid amount of values specified (expected " + var5 + ", found " + var7.size() + ")");
        }
        int var8 = 0;
        for (final JsonElement var10 : var7) {
            try {
                var6[var8] = JsonUtils.getJsonElementFloatValue(var10, "value");
            }
            catch (Exception var12) {
                final JsonException var11 = JsonException.func_151379_a(var12);
                var11.func_151380_a("values[" + var8 + "]");
                throw var11;
            }
            ++var8;
        }
        if (var5 > 1 && var7.size() == 1) {
            while (var8 < var5) {
                var6[var8] = var6[0];
                ++var8;
            }
        }
        final int var13 = (var5 > 1 && var5 <= 4 && var4 < 8) ? (var5 - 1) : 0;
        final ShaderUniform var14 = new ShaderUniform(var3, var4 + var13, var5, this);
        if (var4 <= 3) {
            var14.set((int)var6[0], (int)var6[1], (int)var6[2], (int)var6[3]);
        }
        else if (var4 <= 7) {
            var14.func_148092_b(var6[0], var6[1], var6[2], var6[3]);
        }
        else {
            var14.set(var6);
        }
        this.shaderUniforms.add(var14);
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
        logger = LogManager.getLogger();
        defaultShaderUniform = new ShaderDefault();
        ShaderManager.staticShaderManager = null;
        ShaderManager.currentProgram = -1;
        ShaderManager.field_148000_e = true;
    }
}
