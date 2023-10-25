/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.apache.commons.io.IOUtils
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
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
    private final List<Integer> shaderUniformLocations;
    private final Map<String, Object> shaderSamplers = Maps.newHashMap();
    private static final ShaderDefault defaultShaderUniform;
    private final JsonBlendingMode field_148016_p;
    private final Map<String, ShaderUniform> mappedShaderUniforms;
    private final List<Integer> shaderSamplerLocations;
    private final List<String> samplerNames = Lists.newArrayList();
    private final String programFilename;
    private final List<Integer> attribLocations;
    private final List<String> attributes;
    private static ShaderManager staticShaderManager;
    private final boolean useFaceCulling;
    private final ShaderLoader fragmentShaderLoader;
    private final ShaderLoader vertexShaderLoader;
    private final int program;
    private final List<ShaderUniform> shaderUniforms;
    private boolean isDirty;
    private static final Logger logger;
    private static boolean field_148000_e;
    private static int currentProgram;

    public void deleteShader() {
        ShaderLinkHelper.getStaticShaderLinkHelper().deleteShader(this);
    }

    public ShaderUniform getShaderUniform(String string) {
        return this.mappedShaderUniforms.containsKey(string) ? this.mappedShaderUniforms.get(string) : null;
    }

    public ShaderUniform getShaderUniformOrDefault(String string) {
        return this.mappedShaderUniforms.containsKey(string) ? this.mappedShaderUniforms.get(string) : defaultShaderUniform;
    }

    private void parseUniform(JsonElement jsonElement) throws JsonException {
        JsonObject jsonObject = JsonUtils.getJsonObject(jsonElement, "uniform");
        String string = JsonUtils.getString(jsonObject, "name");
        int n = ShaderUniform.parseType(JsonUtils.getString(jsonObject, "type"));
        int n2 = JsonUtils.getInt(jsonObject, "count");
        float[] fArray = new float[Math.max(n2, 16)];
        JsonArray jsonArray = JsonUtils.getJsonArray(jsonObject, "values");
        if (jsonArray.size() != n2 && jsonArray.size() > 1) {
            throw new JsonException("Invalid amount of values specified (expected " + n2 + ", found " + jsonArray.size() + ")");
        }
        int n3 = 0;
        for (JsonElement jsonElement2 : jsonArray) {
            try {
                fArray[n3] = JsonUtils.getFloat(jsonElement2, "value");
            }
            catch (Exception exception) {
                JsonException jsonException = JsonException.func_151379_a(exception);
                jsonException.func_151380_a("values[" + n3 + "]");
                throw jsonException;
            }
            ++n3;
        }
        if (n2 > 1 && jsonArray.size() == 1) {
            while (n3 < n2) {
                fArray[n3] = fArray[0];
                ++n3;
            }
        }
        int n4 = n2 > 1 && n2 <= 4 && n < 8 ? n2 - 1 : 0;
        ShaderUniform shaderUniform = new ShaderUniform(string, n + n4, n2, this);
        if (n <= 3) {
            shaderUniform.set((int)fArray[0], (int)fArray[1], (int)fArray[2], (int)fArray[3]);
        } else if (n <= 7) {
            shaderUniform.func_148092_b(fArray[0], fArray[1], fArray[2], fArray[3]);
        } else {
            shaderUniform.set(fArray);
        }
        this.shaderUniforms.add(shaderUniform);
    }

    /*
     * WARNING - void declaration
     */
    public ShaderManager(IResourceManager iResourceManager, String string) throws IOException, JsonException {
        this.shaderSamplerLocations = Lists.newArrayList();
        this.shaderUniforms = Lists.newArrayList();
        this.shaderUniformLocations = Lists.newArrayList();
        this.mappedShaderUniforms = Maps.newHashMap();
        JsonParser jsonParser = new JsonParser();
        ResourceLocation resourceLocation = new ResourceLocation("shaders/program/" + string + ".json");
        this.programFilename = string;
        InputStream inputStream = null;
        try {
            JsonArray jsonArray;
            inputStream = iResourceManager.getResource(resourceLocation).getInputStream();
            JsonObject jsonObject = jsonParser.parse(IOUtils.toString((InputStream)inputStream, (Charset)Charsets.UTF_8)).getAsJsonObject();
            String string2 = JsonUtils.getString(jsonObject, "vertex");
            String string3 = JsonUtils.getString(jsonObject, "fragment");
            JsonArray jsonArray2 = JsonUtils.getJsonArray(jsonObject, "samplers", null);
            if (jsonArray2 != null) {
                int n = 0;
                for (JsonElement jsonElement : jsonArray2) {
                    try {
                        this.parseSampler(jsonElement);
                    }
                    catch (Exception exception) {
                        JsonException jsonException = JsonException.func_151379_a(exception);
                        jsonException.func_151380_a("samplers[" + n + "]");
                        throw jsonException;
                    }
                    ++n;
                }
            }
            if ((jsonArray = JsonUtils.getJsonArray(jsonObject, "attributes", null)) != null) {
                int n = 0;
                this.attribLocations = Lists.newArrayListWithCapacity((int)jsonArray.size());
                this.attributes = Lists.newArrayListWithCapacity((int)jsonArray.size());
                for (JsonElement jsonElement : jsonArray) {
                    try {
                        this.attributes.add(JsonUtils.getString(jsonElement, "attribute"));
                    }
                    catch (Exception exception) {
                        JsonException jsonException = JsonException.func_151379_a(exception);
                        jsonException.func_151380_a("attributes[" + n + "]");
                        throw jsonException;
                    }
                    ++n;
                }
            } else {
                this.attribLocations = null;
                this.attributes = null;
            }
            JsonArray jsonArray3 = JsonUtils.getJsonArray(jsonObject, "uniforms", null);
            if (jsonArray3 != null) {
                boolean bl = false;
                for (Iterator<String> iterator : jsonArray3) {
                    void var12_18;
                    try {
                        this.parseUniform((JsonElement)iterator);
                    }
                    catch (Exception exception) {
                        JsonException jsonException = JsonException.func_151379_a(exception);
                        jsonException.func_151380_a("uniforms[" + (int)var12_18 + "]");
                        throw jsonException;
                    }
                    ++var12_18;
                }
            }
            this.field_148016_p = JsonBlendingMode.func_148110_a(JsonUtils.getJsonObject(jsonObject, "blend", null));
            this.useFaceCulling = JsonUtils.getBoolean(jsonObject, "cull", true);
            this.vertexShaderLoader = ShaderLoader.loadShader(iResourceManager, ShaderLoader.ShaderType.VERTEX, string2);
            this.fragmentShaderLoader = ShaderLoader.loadShader(iResourceManager, ShaderLoader.ShaderType.FRAGMENT, string3);
            this.program = ShaderLinkHelper.getStaticShaderLinkHelper().createProgram();
            ShaderLinkHelper.getStaticShaderLinkHelper().linkProgram(this);
            this.setupUniforms();
            if (this.attributes != null) {
                for (String string4 : this.attributes) {
                    int n = OpenGlHelper.glGetAttribLocation(this.program, string4);
                    this.attribLocations.add(n);
                }
            }
        }
        catch (Exception exception) {
            JsonException jsonException = JsonException.func_151379_a(exception);
            jsonException.func_151381_b(resourceLocation.getResourcePath());
            throw jsonException;
        }
        IOUtils.closeQuietly((InputStream)inputStream);
        this.markDirty();
    }

    public void markDirty() {
        this.isDirty = true;
    }

    public void endShader() {
        OpenGlHelper.glUseProgram(0);
        currentProgram = -1;
        staticShaderManager = null;
        field_148000_e = true;
        int n = 0;
        while (n < this.shaderSamplerLocations.size()) {
            if (this.shaderSamplers.get(this.samplerNames.get(n)) != null) {
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + n);
                GlStateManager.bindTexture(0);
            }
            ++n;
        }
    }

    private void parseSampler(JsonElement jsonElement) throws JsonException {
        JsonObject jsonObject = JsonUtils.getJsonObject(jsonElement, "sampler");
        String string = JsonUtils.getString(jsonObject, "name");
        if (!JsonUtils.isString(jsonObject, "file")) {
            this.shaderSamplers.put(string, null);
            this.samplerNames.add(string);
        } else {
            this.samplerNames.add(string);
        }
    }

    public int getProgram() {
        return this.program;
    }

    public ShaderLoader getVertexShaderLoader() {
        return this.vertexShaderLoader;
    }

    public void addSamplerTexture(String string, Object object) {
        if (this.shaderSamplers.containsKey(string)) {
            this.shaderSamplers.remove(string);
        }
        this.shaderSamplers.put(string, object);
        this.markDirty();
    }

    private void setupUniforms() {
        int n = 0;
        int n2 = 0;
        while (n < this.samplerNames.size()) {
            String string = this.samplerNames.get(n);
            int n3 = OpenGlHelper.glGetUniformLocation(this.program, string);
            if (n3 == -1) {
                logger.warn("Shader " + this.programFilename + "could not find sampler named " + (String)string + " in the specified shader program.");
                this.shaderSamplers.remove(string);
                this.samplerNames.remove(n2);
                --n2;
            } else {
                this.shaderSamplerLocations.add(n3);
            }
            ++n;
            ++n2;
        }
        for (ShaderUniform shaderUniform : this.shaderUniforms) {
            String string = shaderUniform.getShaderName();
            int n4 = OpenGlHelper.glGetUniformLocation(this.program, string);
            if (n4 == -1) {
                logger.warn("Could not find uniform named " + string + " in the specified" + " shader program.");
                continue;
            }
            this.shaderUniformLocations.add(n4);
            shaderUniform.setUniformLocation(n4);
            this.mappedShaderUniforms.put(string, shaderUniform);
        }
    }

    public ShaderLoader getFragmentShaderLoader() {
        return this.fragmentShaderLoader;
    }

    static {
        logger = LogManager.getLogger();
        defaultShaderUniform = new ShaderDefault();
        staticShaderManager = null;
        currentProgram = -1;
        field_148000_e = true;
    }

    public void useShader() {
        this.isDirty = false;
        staticShaderManager = this;
        this.field_148016_p.func_148109_a();
        if (this.program != currentProgram) {
            OpenGlHelper.glUseProgram(this.program);
            currentProgram = this.program;
        }
        if (this.useFaceCulling) {
            GlStateManager.enableCull();
        } else {
            GlStateManager.disableCull();
        }
        int n = 0;
        while (n < this.shaderSamplerLocations.size()) {
            if (this.shaderSamplers.get(this.samplerNames.get(n)) != null) {
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit + n);
                GlStateManager.enableTexture2D();
                Object object = this.shaderSamplers.get(this.samplerNames.get(n));
                int n2 = -1;
                if (object instanceof Framebuffer) {
                    n2 = ((Framebuffer)object).framebufferTexture;
                } else if (object instanceof ITextureObject) {
                    n2 = ((ITextureObject)object).getGlTextureId();
                } else if (object instanceof Integer) {
                    n2 = (Integer)object;
                }
                if (n2 != -1) {
                    GlStateManager.bindTexture(n2);
                    OpenGlHelper.glUniform1i(OpenGlHelper.glGetUniformLocation(this.program, this.samplerNames.get(n)), n);
                }
            }
            ++n;
        }
        for (ShaderUniform shaderUniform : this.shaderUniforms) {
            shaderUniform.upload();
        }
    }
}

