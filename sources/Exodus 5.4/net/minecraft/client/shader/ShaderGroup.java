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
 *  com.google.gson.JsonSyntaxException
 *  org.apache.commons.io.IOUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.vector.Matrix4f
 */
package net.minecraft.client.shader;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

public class ShaderGroup {
    private final List<Framebuffer> listFramebuffers;
    private float field_148036_j;
    private final Map<String, Framebuffer> mapFramebuffers;
    private int mainFramebufferWidth;
    private String shaderGroupName;
    private IResourceManager resourceManager;
    private Matrix4f projectionMatrix;
    private int mainFramebufferHeight;
    private Framebuffer mainFramebuffer;
    private float field_148037_k;
    private final List<Shader> listShaders = Lists.newArrayList();

    public void deleteShaderGroup() {
        for (Framebuffer object : this.mapFramebuffers.values()) {
            object.deleteFramebuffer();
        }
        for (Shader shader : this.listShaders) {
            shader.deleteShader();
        }
        this.listShaders.clear();
    }

    private void initTarget(JsonElement jsonElement) throws JsonException {
        if (JsonUtils.isString(jsonElement)) {
            this.addFramebuffer(jsonElement.getAsString(), this.mainFramebufferWidth, this.mainFramebufferHeight);
        } else {
            JsonObject jsonObject = JsonUtils.getJsonObject(jsonElement, "target");
            String string = JsonUtils.getString(jsonObject, "name");
            int n = JsonUtils.getInt(jsonObject, "width", this.mainFramebufferWidth);
            int n2 = JsonUtils.getInt(jsonObject, "height", this.mainFramebufferHeight);
            if (this.mapFramebuffers.containsKey(string)) {
                throw new JsonException(String.valueOf(string) + " is already defined");
            }
            this.addFramebuffer(string, n, n2);
        }
    }

    public void addFramebuffer(String string, int n, int n2) {
        Framebuffer framebuffer = new Framebuffer(n, n2, true);
        framebuffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.mapFramebuffers.put(string, framebuffer);
        if (n == this.mainFramebufferWidth && n2 == this.mainFramebufferHeight) {
            this.listFramebuffers.add(framebuffer);
        }
    }

    public final String getShaderGroupName() {
        return this.shaderGroupName;
    }

    public ShaderGroup(TextureManager textureManager, IResourceManager iResourceManager, Framebuffer framebuffer, ResourceLocation resourceLocation) throws JsonSyntaxException, IOException, JsonException {
        this.mapFramebuffers = Maps.newHashMap();
        this.listFramebuffers = Lists.newArrayList();
        this.resourceManager = iResourceManager;
        this.mainFramebuffer = framebuffer;
        this.field_148036_j = 0.0f;
        this.field_148037_k = 0.0f;
        this.mainFramebufferWidth = framebuffer.framebufferWidth;
        this.mainFramebufferHeight = framebuffer.framebufferHeight;
        this.shaderGroupName = resourceLocation.toString();
        this.resetProjectionMatrix();
        this.parseGroup(textureManager, resourceLocation);
    }

    private void parsePass(TextureManager textureManager, JsonElement jsonElement) throws JsonException, IOException {
        JsonArray jsonArray;
        Object object;
        JsonObject jsonObject = JsonUtils.getJsonObject(jsonElement, "pass");
        String string = JsonUtils.getString(jsonObject, "name");
        String string2 = JsonUtils.getString(jsonObject, "intarget");
        String string3 = JsonUtils.getString(jsonObject, "outtarget");
        Framebuffer framebuffer = this.getFramebuffer(string2);
        Framebuffer framebuffer2 = this.getFramebuffer(string3);
        if (framebuffer == null) {
            throw new JsonException("Input target '" + string2 + "' does not exist");
        }
        if (framebuffer2 == null) {
            throw new JsonException("Output target '" + string3 + "' does not exist");
        }
        Shader shader = this.addShader(string, framebuffer, framebuffer2);
        JsonArray jsonArray2 = JsonUtils.getJsonArray(jsonObject, "auxtargets", null);
        if (jsonArray2 != null) {
            int n = 0;
            for (JsonElement jsonElement2 : jsonArray2) {
                block15: {
                    Object object2;
                    try {
                        Iterator iterator = JsonUtils.getJsonObject(jsonElement2, "auxtarget");
                        object2 = JsonUtils.getString((JsonObject)iterator, "name");
                        object = JsonUtils.getString((JsonObject)iterator, "id");
                        Framebuffer framebuffer3 = this.getFramebuffer((String)object);
                        if (framebuffer3 == null) {
                            ResourceLocation resourceLocation = new ResourceLocation("textures/effect/" + (String)object + ".png");
                            try {
                                this.resourceManager.getResource(resourceLocation);
                            }
                            catch (FileNotFoundException fileNotFoundException) {
                                throw new JsonException("Render target or texture '" + (String)object + "' does not exist");
                            }
                            textureManager.bindTexture(resourceLocation);
                            ITextureObject iTextureObject = textureManager.getTexture(resourceLocation);
                            int n2 = JsonUtils.getInt((JsonObject)iterator, "width");
                            int n3 = JsonUtils.getInt((JsonObject)iterator, "height");
                            boolean bl = JsonUtils.getBoolean((JsonObject)iterator, "bilinear");
                            if (bl) {
                                GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
                                GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
                            } else {
                                GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
                                GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
                            }
                            shader.addAuxFramebuffer((String)object2, iTextureObject.getGlTextureId(), n2, n3);
                            break block15;
                        }
                        shader.addAuxFramebuffer((String)object2, framebuffer3, framebuffer3.framebufferTextureWidth, framebuffer3.framebufferTextureHeight);
                    }
                    catch (Exception exception) {
                        object2 = JsonException.func_151379_a(exception);
                        ((JsonException)object2).func_151380_a("auxtargets[" + n + "]");
                        throw object2;
                    }
                }
                ++n;
            }
        }
        if ((jsonArray = JsonUtils.getJsonArray(jsonObject, "uniforms", null)) != null) {
            int n = 0;
            for (Iterator iterator : jsonArray) {
                try {
                    this.initUniform((JsonElement)iterator);
                }
                catch (Exception exception) {
                    object = JsonException.func_151379_a(exception);
                    ((JsonException)object).func_151380_a("uniforms[" + n + "]");
                    throw object;
                }
                ++n;
            }
        }
    }

    public Framebuffer getFramebufferRaw(String string) {
        return this.mapFramebuffers.get(string);
    }

    private void initUniform(JsonElement jsonElement) throws JsonException {
        JsonObject jsonObject = JsonUtils.getJsonObject(jsonElement, "uniform");
        String string = JsonUtils.getString(jsonObject, "name");
        ShaderUniform shaderUniform = this.listShaders.get(this.listShaders.size() - 1).getShaderManager().getShaderUniform(string);
        if (shaderUniform == null) {
            throw new JsonException("Uniform '" + string + "' does not exist");
        }
        float[] fArray = new float[4];
        int n = 0;
        for (JsonElement jsonElement2 : JsonUtils.getJsonArray(jsonObject, "values")) {
            try {
                fArray[n] = JsonUtils.getFloat(jsonElement2, "value");
            }
            catch (Exception exception) {
                JsonException jsonException = JsonException.func_151379_a(exception);
                jsonException.func_151380_a("values[" + n + "]");
                throw jsonException;
            }
            ++n;
        }
        switch (n) {
            default: {
                break;
            }
            case 1: {
                shaderUniform.set(fArray[0]);
                break;
            }
            case 2: {
                shaderUniform.set(fArray[0], fArray[1]);
                break;
            }
            case 3: {
                shaderUniform.set(fArray[0], fArray[1], fArray[2]);
                break;
            }
            case 4: {
                shaderUniform.set(fArray[0], fArray[1], fArray[2], fArray[3]);
            }
        }
    }

    private void resetProjectionMatrix() {
        this.projectionMatrix = new Matrix4f();
        this.projectionMatrix.setIdentity();
        this.projectionMatrix.m00 = 2.0f / (float)this.mainFramebuffer.framebufferTextureWidth;
        this.projectionMatrix.m11 = 2.0f / (float)(-this.mainFramebuffer.framebufferTextureHeight);
        this.projectionMatrix.m22 = -0.0020001999f;
        this.projectionMatrix.m33 = 1.0f;
        this.projectionMatrix.m03 = -1.0f;
        this.projectionMatrix.m13 = 1.0f;
        this.projectionMatrix.m23 = -1.0001999f;
    }

    public void parseGroup(TextureManager textureManager, ResourceLocation resourceLocation) throws IOException, JsonException, JsonSyntaxException {
        InputStream inputStream;
        block9: {
            JsonParser jsonParser = new JsonParser();
            inputStream = null;
            try {
                int n;
                JsonArray jsonArray;
                IResource iResource = this.resourceManager.getResource(resourceLocation);
                inputStream = iResource.getInputStream();
                JsonObject jsonObject = jsonParser.parse(IOUtils.toString((InputStream)inputStream, (Charset)Charsets.UTF_8)).getAsJsonObject();
                if (JsonUtils.isJsonArray(jsonObject, "targets")) {
                    jsonArray = jsonObject.getAsJsonArray("targets");
                    n = 0;
                    for (JsonElement jsonElement : jsonArray) {
                        try {
                            this.initTarget(jsonElement);
                        }
                        catch (Exception exception) {
                            JsonException jsonException = JsonException.func_151379_a(exception);
                            jsonException.func_151380_a("targets[" + n + "]");
                            throw jsonException;
                        }
                        ++n;
                    }
                }
                if (!JsonUtils.isJsonArray(jsonObject, "passes")) break block9;
                jsonArray = jsonObject.getAsJsonArray("passes");
                n = 0;
                for (JsonElement jsonElement : jsonArray) {
                    try {
                        this.parsePass(textureManager, jsonElement);
                    }
                    catch (Exception exception) {
                        JsonException jsonException = JsonException.func_151379_a(exception);
                        jsonException.func_151380_a("passes[" + n + "]");
                        throw jsonException;
                    }
                    ++n;
                }
            }
            catch (Exception exception) {
                JsonException jsonException = JsonException.func_151379_a(exception);
                jsonException.func_151381_b(resourceLocation.getResourcePath());
                throw jsonException;
            }
        }
        IOUtils.closeQuietly((InputStream)inputStream);
    }

    private Framebuffer getFramebuffer(String string) {
        return string == null ? null : (string.equals("minecraft:main") ? this.mainFramebuffer : this.mapFramebuffers.get(string));
    }

    public void createBindFramebuffers(int n, int n2) {
        this.mainFramebufferWidth = this.mainFramebuffer.framebufferTextureWidth;
        this.mainFramebufferHeight = this.mainFramebuffer.framebufferTextureHeight;
        this.resetProjectionMatrix();
        for (Shader object : this.listShaders) {
            object.setProjectionMatrix(this.projectionMatrix);
        }
        for (Framebuffer framebuffer : this.listFramebuffers) {
            framebuffer.createBindFramebuffer(n, n2);
        }
    }

    public void loadShaderGroup(float f) {
        if (f < this.field_148037_k) {
            this.field_148036_j += 1.0f - this.field_148037_k;
            this.field_148036_j += f;
        } else {
            this.field_148036_j += f - this.field_148037_k;
        }
        this.field_148037_k = f;
        while (this.field_148036_j > 20.0f) {
            this.field_148036_j -= 20.0f;
        }
        for (Shader shader : this.listShaders) {
            shader.loadShader(this.field_148036_j / 20.0f);
        }
    }

    public Shader addShader(String string, Framebuffer framebuffer, Framebuffer framebuffer2) throws IOException, JsonException {
        Shader shader = new Shader(this.resourceManager, string, framebuffer, framebuffer2);
        this.listShaders.add(this.listShaders.size(), shader);
        return shader;
    }
}

