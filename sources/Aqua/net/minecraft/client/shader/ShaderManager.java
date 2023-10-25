package net.minecraft.client.shader;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderDefault;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.client.shader.ShaderLoader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.client.util.JsonBlendingMode;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShaderManager {
    private static final Logger logger = LogManager.getLogger();
    private static final ShaderDefault defaultShaderUniform = new ShaderDefault();
    private static ShaderManager staticShaderManager = null;
    private static int currentProgram = -1;
    private static boolean field_148000_e = true;
    private final Map<String, Object> shaderSamplers = Maps.newHashMap();
    private final List<String> samplerNames = Lists.newArrayList();
    private final List<Integer> shaderSamplerLocations = Lists.newArrayList();
    private final List<ShaderUniform> shaderUniforms = Lists.newArrayList();
    private final List<Integer> shaderUniformLocations = Lists.newArrayList();
    private final Map<String, ShaderUniform> mappedShaderUniforms = Maps.newHashMap();
    private final int program;
    private final String programFilename;
    private final boolean useFaceCulling;
    private boolean isDirty;
    private final JsonBlendingMode field_148016_p;
    private final List<Integer> attribLocations;
    private final List<String> attributes;
    private final ShaderLoader vertexShaderLoader;
    private final ShaderLoader fragmentShaderLoader;

    public ShaderManager(IResourceManager resourceManager, String programName) throws JsonException, IOException {
        JsonParser jsonparser = new JsonParser();
        ResourceLocation resourcelocation = new ResourceLocation("shaders/program/" + programName + ".json");
        this.programFilename = programName;
        InputStream inputstream = null;
        try {
            JsonArray jsonarray2;
            JsonArray jsonarray1;
            inputstream = resourceManager.getResource(resourcelocation).getInputStream();
            JsonObject jsonobject = jsonparser.parse(IOUtils.toString((InputStream)inputstream, (Charset)Charsets.UTF_8)).getAsJsonObject();
            String s = JsonUtils.getString((JsonObject)jsonobject, (String)"vertex");
            String s1 = JsonUtils.getString((JsonObject)jsonobject, (String)"fragment");
            JsonArray jsonarray = JsonUtils.getJsonArray((JsonObject)jsonobject, (String)"samplers", (JsonArray)null);
            if (jsonarray != null) {
                int i = 0;
                for (JsonElement jsonelement : jsonarray) {
                    try {
                        this.parseSampler(jsonelement);
                    }
                    catch (Exception exception2) {
                        JsonException jsonexception1 = JsonException.func_151379_a((Exception)exception2);
                        jsonexception1.func_151380_a("samplers[" + i + "]");
                        throw jsonexception1;
                    }
                    ++i;
                }
            }
            if ((jsonarray1 = JsonUtils.getJsonArray((JsonObject)jsonobject, (String)"attributes", (JsonArray)null)) != null) {
                int j = 0;
                this.attribLocations = Lists.newArrayListWithCapacity((int)jsonarray1.size());
                this.attributes = Lists.newArrayListWithCapacity((int)jsonarray1.size());
                for (Iterator jsonelement1 : jsonarray1) {
                    try {
                        this.attributes.add((Object)JsonUtils.getString((JsonElement)jsonelement1, (String)"attribute"));
                    }
                    catch (Exception exception1) {
                        JsonException jsonexception2 = JsonException.func_151379_a((Exception)exception1);
                        jsonexception2.func_151380_a("attributes[" + j + "]");
                        throw jsonexception2;
                    }
                    ++j;
                }
            } else {
                this.attribLocations = null;
                this.attributes = null;
            }
            if ((jsonarray2 = JsonUtils.getJsonArray((JsonObject)jsonobject, (String)"uniforms", (JsonArray)null)) != null) {
                int k = 0;
                for (JsonElement jsonelement2 : jsonarray2) {
                    try {
                        this.parseUniform(jsonelement2);
                    }
                    catch (Exception exception) {
                        JsonException jsonexception3 = JsonException.func_151379_a((Exception)exception);
                        jsonexception3.func_151380_a("uniforms[" + k + "]");
                        throw jsonexception3;
                    }
                    ++k;
                }
            }
            this.field_148016_p = JsonBlendingMode.func_148110_a((JsonObject)JsonUtils.getJsonObject((JsonObject)jsonobject, (String)"blend", (JsonObject)null));
            this.useFaceCulling = JsonUtils.getBoolean((JsonObject)jsonobject, (String)"cull", (boolean)true);
            this.vertexShaderLoader = ShaderLoader.loadShader((IResourceManager)resourceManager, (ShaderLoader.ShaderType)ShaderLoader.ShaderType.VERTEX, (String)s);
            this.fragmentShaderLoader = ShaderLoader.loadShader((IResourceManager)resourceManager, (ShaderLoader.ShaderType)ShaderLoader.ShaderType.FRAGMENT, (String)s1);
            this.program = ShaderLinkHelper.getStaticShaderLinkHelper().createProgram();
            ShaderLinkHelper.getStaticShaderLinkHelper().linkProgram(this);
            this.setupUniforms();
            if (this.attributes != null) {
                for (String s2 : this.attributes) {
                    int l = OpenGlHelper.glGetAttribLocation((int)this.program, (CharSequence)s2);
                    this.attribLocations.add((Object)l);
                }
            }
        }
        catch (Exception exception3) {
            try {
                JsonException jsonexception = JsonException.func_151379_a((Exception)exception3);
                jsonexception.func_151381_b(resourcelocation.getResourcePath());
                throw jsonexception;
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(inputstream);
                throw throwable;
            }
        }
        IOUtils.closeQuietly((InputStream)inputstream);
        this.markDirty();
    }

    public void deleteShader() {
        ShaderLinkHelper.getStaticShaderLinkHelper().deleteShader(this);
    }

    public void endShader() {
        OpenGlHelper.glUseProgram((int)0);
        currentProgram = -1;
        staticShaderManager = null;
        field_148000_e = true;
        for (int i = 0; i < this.shaderSamplerLocations.size(); ++i) {
            if (this.shaderSamplers.get(this.samplerNames.get(i)) == null) continue;
            GlStateManager.setActiveTexture((int)(OpenGlHelper.defaultTexUnit + i));
            GlStateManager.bindTexture((int)0);
        }
    }

    public void useShader() {
        this.isDirty = false;
        staticShaderManager = this;
        this.field_148016_p.func_148109_a();
        if (this.program != currentProgram) {
            OpenGlHelper.glUseProgram((int)this.program);
            currentProgram = this.program;
        }
        if (this.useFaceCulling) {
            GlStateManager.enableCull();
        } else {
            GlStateManager.disableCull();
        }
        for (int i = 0; i < this.shaderSamplerLocations.size(); ++i) {
            if (this.shaderSamplers.get(this.samplerNames.get(i)) == null) continue;
            GlStateManager.setActiveTexture((int)(OpenGlHelper.defaultTexUnit + i));
            GlStateManager.enableTexture2D();
            Object object = this.shaderSamplers.get(this.samplerNames.get(i));
            int j = -1;
            if (object instanceof Framebuffer) {
                j = ((Framebuffer)object).framebufferTexture;
            } else if (object instanceof ITextureObject) {
                j = ((ITextureObject)object).getGlTextureId();
            } else if (object instanceof Integer) {
                j = (Integer)object;
            }
            if (j == -1) continue;
            GlStateManager.bindTexture((int)j);
            OpenGlHelper.glUniform1i((int)OpenGlHelper.glGetUniformLocation((int)this.program, (CharSequence)((CharSequence)this.samplerNames.get(i))), (int)i);
        }
        for (ShaderUniform shaderuniform : this.shaderUniforms) {
            shaderuniform.upload();
        }
    }

    public void markDirty() {
        this.isDirty = true;
    }

    public ShaderUniform getShaderUniform(String p_147991_1_) {
        return this.mappedShaderUniforms.containsKey((Object)p_147991_1_) ? (ShaderUniform)this.mappedShaderUniforms.get((Object)p_147991_1_) : null;
    }

    public ShaderUniform getShaderUniformOrDefault(String p_147984_1_) {
        return this.mappedShaderUniforms.containsKey((Object)p_147984_1_) ? (ShaderUniform)this.mappedShaderUniforms.get((Object)p_147984_1_) : defaultShaderUniform;
    }

    private void setupUniforms() {
        int i = 0;
        int j = 0;
        while (i < this.samplerNames.size()) {
            String s = (String)this.samplerNames.get(i);
            int k = OpenGlHelper.glGetUniformLocation((int)this.program, (CharSequence)s);
            if (k == -1) {
                logger.warn("Shader " + this.programFilename + "could not find sampler named " + s + " in the specified shader program.");
                this.shaderSamplers.remove((Object)s);
                this.samplerNames.remove(j);
                --j;
            } else {
                this.shaderSamplerLocations.add((Object)k);
            }
            ++i;
            ++j;
        }
        for (ShaderUniform shaderuniform : this.shaderUniforms) {
            String s1 = shaderuniform.getShaderName();
            int l = OpenGlHelper.glGetUniformLocation((int)this.program, (CharSequence)s1);
            if (l == -1) {
                logger.warn("Could not find uniform named " + s1 + " in the specified shader program.");
                continue;
            }
            this.shaderUniformLocations.add((Object)l);
            shaderuniform.setUniformLocation(l);
            this.mappedShaderUniforms.put((Object)s1, (Object)shaderuniform);
        }
    }

    private void parseSampler(JsonElement p_147996_1_) throws JsonException {
        JsonObject jsonobject = JsonUtils.getJsonObject((JsonElement)p_147996_1_, (String)"sampler");
        String s = JsonUtils.getString((JsonObject)jsonobject, (String)"name");
        if (!JsonUtils.isString((JsonObject)jsonobject, (String)"file")) {
            this.shaderSamplers.put((Object)s, null);
            this.samplerNames.add((Object)s);
        } else {
            this.samplerNames.add((Object)s);
        }
    }

    public void addSamplerTexture(String p_147992_1_, Object p_147992_2_) {
        if (this.shaderSamplers.containsKey((Object)p_147992_1_)) {
            this.shaderSamplers.remove((Object)p_147992_1_);
        }
        this.shaderSamplers.put((Object)p_147992_1_, p_147992_2_);
        this.markDirty();
    }

    private void parseUniform(JsonElement p_147987_1_) throws JsonException {
        JsonObject jsonobject = JsonUtils.getJsonObject((JsonElement)p_147987_1_, (String)"uniform");
        String s = JsonUtils.getString((JsonObject)jsonobject, (String)"name");
        int i = ShaderUniform.parseType((String)JsonUtils.getString((JsonObject)jsonobject, (String)"type"));
        int j = JsonUtils.getInt((JsonObject)jsonobject, (String)"count");
        float[] afloat = new float[Math.max((int)j, (int)16)];
        JsonArray jsonarray = JsonUtils.getJsonArray((JsonObject)jsonobject, (String)"values");
        if (jsonarray.size() != j && jsonarray.size() > 1) {
            throw new JsonException("Invalid amount of values specified (expected " + j + ", found " + jsonarray.size() + ")");
        }
        int k = 0;
        for (JsonElement jsonelement : jsonarray) {
            try {
                afloat[k] = JsonUtils.getFloat((JsonElement)jsonelement, (String)"value");
            }
            catch (Exception exception) {
                JsonException jsonexception = JsonException.func_151379_a((Exception)exception);
                jsonexception.func_151380_a("values[" + k + "]");
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
        int l = j > 1 && j <= 4 && i < 8 ? j - 1 : 0;
        ShaderUniform shaderuniform = new ShaderUniform(s, i + l, j, this);
        if (i <= 3) {
            shaderuniform.set((int)afloat[0], (int)afloat[1], (int)afloat[2], (int)afloat[3]);
        } else if (i <= 7) {
            shaderuniform.func_148092_b(afloat[0], afloat[1], afloat[2], afloat[3]);
        } else {
            shaderuniform.set(afloat);
        }
        this.shaderUniforms.add((Object)shaderuniform);
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
}
