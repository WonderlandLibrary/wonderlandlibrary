/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.shader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
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
import org.lwjgl.util.vector.Matrix4f;

public class ShaderGroup {
    public Framebuffer mainFramebuffer;
    private final IResourceManager resourceManager;
    private final String shaderGroupName;
    private final List<Shader> listShaders = Lists.newArrayList();
    private final Map<String, Framebuffer> mapFramebuffers = Maps.newHashMap();
    private final List<Framebuffer> listFramebuffers = Lists.newArrayList();
    private Matrix4f projectionMatrix;
    private int mainFramebufferWidth;
    private int mainFramebufferHeight;
    private float time;
    private float lastStamp;

    public ShaderGroup(TextureManager p_i1050_1_, IResourceManager resourceManagerIn, Framebuffer mainFramebufferIn, ResourceLocation p_i1050_4_) throws JsonException, IOException, JsonSyntaxException {
        this.resourceManager = resourceManagerIn;
        this.mainFramebuffer = mainFramebufferIn;
        this.time = 0.0f;
        this.lastStamp = 0.0f;
        this.mainFramebufferWidth = mainFramebufferIn.framebufferWidth;
        this.mainFramebufferHeight = mainFramebufferIn.framebufferHeight;
        this.shaderGroupName = p_i1050_4_.toString();
        this.resetProjectionMatrix();
        this.parseGroup(p_i1050_1_, p_i1050_4_);
    }

    public void parseGroup(TextureManager p_152765_1_, ResourceLocation p_152765_2_) throws JsonException, IOException, JsonSyntaxException {
        IResource iresource;
        block11: {
            JsonParser jsonparser = new JsonParser();
            iresource = null;
            try {
                iresource = this.resourceManager.getResource(p_152765_2_);
                JsonObject jsonobject = jsonparser.parse(IOUtils.toString(iresource.getInputStream(), StandardCharsets.UTF_8)).getAsJsonObject();
                if (JsonUtils.isJsonArray(jsonobject, "targets")) {
                    JsonArray jsonarray = jsonobject.getAsJsonArray("targets");
                    int i = 0;
                    for (JsonElement jsonelement : jsonarray) {
                        try {
                            this.initTarget(jsonelement);
                        }
                        catch (Exception exception1) {
                            JsonException jsonexception1 = JsonException.forException(exception1);
                            jsonexception1.prependJsonKey("targets[" + i + "]");
                            throw jsonexception1;
                        }
                        ++i;
                    }
                }
                if (!JsonUtils.isJsonArray(jsonobject, "passes")) break block11;
                JsonArray jsonarray1 = jsonobject.getAsJsonArray("passes");
                int j = 0;
                for (JsonElement jsonelement1 : jsonarray1) {
                    try {
                        this.parsePass(p_152765_1_, jsonelement1);
                    }
                    catch (Exception exception) {
                        JsonException jsonexception2 = JsonException.forException(exception);
                        jsonexception2.prependJsonKey("passes[" + j + "]");
                        throw jsonexception2;
                    }
                    ++j;
                }
            }
            catch (Exception exception2) {
                try {
                    JsonException jsonexception = JsonException.forException(exception2);
                    jsonexception.setFilenameAndFlush(p_152765_2_.getPath());
                    throw jsonexception;
                }
                catch (Throwable throwable) {
                    IOUtils.closeQuietly(iresource);
                    throw throwable;
                }
            }
        }
        IOUtils.closeQuietly((Closeable)iresource);
    }

    public List<Shader> getShaders() {
        return this.listShaders;
    }

    private void initTarget(JsonElement p_148027_1_) throws JsonException {
        if (JsonUtils.isString(p_148027_1_)) {
            this.addFramebuffer(p_148027_1_.getAsString(), this.mainFramebufferWidth, this.mainFramebufferHeight);
        } else {
            JsonObject jsonobject = JsonUtils.getJsonObject(p_148027_1_, "target");
            String s = JsonUtils.getString(jsonobject, "name");
            int i = JsonUtils.getInt(jsonobject, "width", this.mainFramebufferWidth);
            int j = JsonUtils.getInt(jsonobject, "height", this.mainFramebufferHeight);
            if (this.mapFramebuffers.containsKey(s)) {
                throw new JsonException(s + " is already defined");
            }
            this.addFramebuffer(s, i, j);
        }
    }

    private void parsePass(TextureManager p_152764_1_, JsonElement p_152764_2_) throws JsonException, IOException {
        JsonArray jsonarray1;
        JsonObject jsonobject;
        block16: {
            jsonobject = JsonUtils.getJsonObject(p_152764_2_, "pass");
            String s = JsonUtils.getString(jsonobject, "name");
            String s1 = JsonUtils.getString(jsonobject, "intarget");
            String s2 = JsonUtils.getString(jsonobject, "outtarget");
            Framebuffer framebuffer = this.getFramebuffer(s1);
            Framebuffer framebuffer1 = this.getFramebuffer(s2);
            if (framebuffer == null) {
                throw new JsonException("Input target '" + s1 + "' does not exist");
            }
            if (framebuffer1 == null) {
                throw new JsonException("Output target '" + s2 + "' does not exist");
            }
            Shader shader = this.addShader(s, framebuffer, framebuffer1);
            JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "auxtargets", null);
            if (jsonarray == null) break block16;
            int i = 0;
            for (JsonElement jsonelement : jsonarray) {
                block15: {
                    try {
                        Framebuffer framebuffer2;
                        String s4;
                        block17: {
                            JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonelement, "auxtarget");
                            s4 = JsonUtils.getString(jsonobject1, "name");
                            String s3 = JsonUtils.getString(jsonobject1, "id");
                            framebuffer2 = this.getFramebuffer(s3);
                            if (framebuffer2 != null) break block17;
                            ResourceLocation resourcelocation = new ResourceLocation("textures/effect/" + s3 + ".png");
                            IResource iresource = null;
                            try {
                                iresource = this.resourceManager.getResource(resourcelocation);
                            }
                            catch (FileNotFoundException var29) {
                                try {
                                    throw new JsonException("Render target or texture '" + s3 + "' does not exist");
                                }
                                catch (Throwable throwable) {
                                    IOUtils.closeQuietly(iresource);
                                    throw throwable;
                                }
                            }
                            IOUtils.closeQuietly((Closeable)iresource);
                            p_152764_1_.bindTexture(resourcelocation);
                            ITextureObject lvt_20_2_ = p_152764_1_.getTexture(resourcelocation);
                            int lvt_21_1_ = JsonUtils.getInt(jsonobject1, "width");
                            int lvt_22_1_ = JsonUtils.getInt(jsonobject1, "height");
                            boolean lvt_23_1_ = JsonUtils.getBoolean(jsonobject1, "bilinear");
                            if (lvt_23_1_) {
                                GlStateManager.glTexParameteri(3553, 10241, 9729);
                                GlStateManager.glTexParameteri(3553, 10240, 9729);
                            } else {
                                GlStateManager.glTexParameteri(3553, 10241, 9728);
                                GlStateManager.glTexParameteri(3553, 10240, 9728);
                            }
                            shader.addAuxFramebuffer(s4, lvt_20_2_.getGlTextureId(), lvt_21_1_, lvt_22_1_);
                            break block15;
                        }
                        shader.addAuxFramebuffer(s4, framebuffer2, framebuffer2.framebufferTextureWidth, framebuffer2.framebufferTextureHeight);
                    }
                    catch (Exception exception1) {
                        JsonException jsonexception = JsonException.forException(exception1);
                        jsonexception.prependJsonKey("auxtargets[" + i + "]");
                        throw jsonexception;
                    }
                }
                ++i;
            }
        }
        if ((jsonarray1 = JsonUtils.getJsonArray(jsonobject, "uniforms", null)) != null) {
            int l = 0;
            for (JsonElement jsonelement1 : jsonarray1) {
                try {
                    this.initUniform(jsonelement1);
                }
                catch (Exception exception) {
                    JsonException jsonexception1 = JsonException.forException(exception);
                    jsonexception1.prependJsonKey("uniforms[" + l + "]");
                    throw jsonexception1;
                }
                ++l;
            }
        }
    }

    private void initUniform(JsonElement p_148028_1_) throws JsonException {
        JsonObject jsonobject = JsonUtils.getJsonObject(p_148028_1_, "uniform");
        String s = JsonUtils.getString(jsonobject, "name");
        ShaderUniform shaderuniform = this.listShaders.get(this.listShaders.size() - 1).getShaderManager().getShaderUniform(s);
        if (shaderuniform == null) {
            throw new JsonException("Uniform '" + s + "' does not exist");
        }
        float[] afloat = new float[4];
        int i = 0;
        for (JsonElement jsonelement : JsonUtils.getJsonArray(jsonobject, "values")) {
            try {
                afloat[i] = JsonUtils.getFloat(jsonelement, "value");
            }
            catch (Exception exception) {
                JsonException jsonexception = JsonException.forException(exception);
                jsonexception.prependJsonKey("values[" + i + "]");
                throw jsonexception;
            }
            ++i;
        }
        switch (i) {
            default: {
                break;
            }
            case 1: {
                shaderuniform.set(afloat[0]);
                break;
            }
            case 2: {
                shaderuniform.set(afloat[0], afloat[1]);
                break;
            }
            case 3: {
                shaderuniform.set(afloat[0], afloat[1], afloat[2]);
                break;
            }
            case 4: {
                shaderuniform.set(afloat[0], afloat[1], afloat[2], afloat[3]);
            }
        }
    }

    public Framebuffer getFramebufferRaw(String p_177066_1_) {
        return this.mapFramebuffers.get(p_177066_1_);
    }

    public void addFramebuffer(String p_148020_1_, int p_148020_2_, int p_148020_3_) {
        Framebuffer framebuffer = new Framebuffer(p_148020_2_, p_148020_3_, true);
        framebuffer.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.mapFramebuffers.put(p_148020_1_, framebuffer);
        if (p_148020_2_ == this.mainFramebufferWidth && p_148020_3_ == this.mainFramebufferHeight) {
            this.listFramebuffers.add(framebuffer);
        }
    }

    public void deleteShaderGroup() {
        for (Framebuffer framebuffer : this.mapFramebuffers.values()) {
            framebuffer.deleteFramebuffer();
        }
        for (Shader shader : this.listShaders) {
            shader.deleteShader();
        }
        this.listShaders.clear();
    }

    public Shader addShader(String p_148023_1_, Framebuffer p_148023_2_, Framebuffer p_148023_3_) throws JsonException, IOException {
        Shader shader = new Shader(this.resourceManager, p_148023_1_, p_148023_2_, p_148023_3_);
        this.listShaders.add(this.listShaders.size(), shader);
        return shader;
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

    public void createBindFramebuffers(int width, int height) {
        this.mainFramebufferWidth = this.mainFramebuffer.framebufferTextureWidth;
        this.mainFramebufferHeight = this.mainFramebuffer.framebufferTextureHeight;
        this.resetProjectionMatrix();
        for (Shader shader : this.listShaders) {
            shader.setProjectionMatrix(this.projectionMatrix);
        }
        for (Framebuffer framebuffer : this.listFramebuffers) {
            framebuffer.createBindFramebuffer(width, height);
        }
    }

    public void loadShaderGroup(float partialTicks) {
        if (partialTicks < this.lastStamp) {
            this.time += 1.0f - this.lastStamp;
            this.time += partialTicks;
        } else {
            this.time += partialTicks - this.lastStamp;
        }
        this.lastStamp = partialTicks;
        while (this.time > 20.0f) {
            this.time -= 20.0f;
        }
        for (Shader shader : this.listShaders) {
            shader.loadShader(this.time / 20.0f);
        }
    }

    public final String getShaderGroupName() {
        return this.shaderGroupName;
    }

    private Framebuffer getFramebuffer(String p_148017_1_) {
        if (p_148017_1_ == null) {
            return null;
        }
        return p_148017_1_.equals("minecraft:main") ? this.mainFramebuffer : this.mapFramebuffers.get(p_148017_1_);
    }
}

