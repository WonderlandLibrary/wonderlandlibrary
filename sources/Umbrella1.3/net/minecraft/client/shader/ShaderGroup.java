/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  javax.vecmath.Matrix4f
 *  org.apache.commons.io.IOUtils
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.shader;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import javax.vecmath.Matrix4f;
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

public class ShaderGroup {
    private Framebuffer mainFramebuffer;
    private IResourceManager resourceManager;
    private String shaderGroupName;
    private final List listShaders = Lists.newArrayList();
    private final Map mapFramebuffers = Maps.newHashMap();
    private final List listFramebuffers = Lists.newArrayList();
    private Matrix4f projectionMatrix;
    private int mainFramebufferWidth;
    private int mainFramebufferHeight;
    private float field_148036_j;
    private float field_148037_k;
    private static final String __OBFID = "CL_00001041";

    public ShaderGroup(TextureManager p_i1050_1_, IResourceManager p_i1050_2_, Framebuffer p_i1050_3_, ResourceLocation p_i1050_4_) throws JsonException {
        this.resourceManager = p_i1050_2_;
        this.mainFramebuffer = p_i1050_3_;
        this.field_148036_j = 0.0f;
        this.field_148037_k = 0.0f;
        this.mainFramebufferWidth = p_i1050_3_.framebufferWidth;
        this.mainFramebufferHeight = p_i1050_3_.framebufferHeight;
        this.shaderGroupName = p_i1050_4_.toString();
        this.resetProjectionMatrix();
        this.parseGroup(p_i1050_1_, p_i1050_4_);
    }

    public void parseGroup(TextureManager p_152765_1_, ResourceLocation p_152765_2_) throws JsonException {
        InputStream var4;
        block11: {
            JsonParser var3 = new JsonParser();
            var4 = null;
            try {
                try {
                    int var8;
                    JsonArray var7;
                    IResource var5 = this.resourceManager.getResource(p_152765_2_);
                    var4 = var5.getInputStream();
                    JsonObject var22 = var3.parse(IOUtils.toString((InputStream)var4, (Charset)Charsets.UTF_8)).getAsJsonObject();
                    if (JsonUtils.jsonObjectFieldTypeIsArray(var22, "targets")) {
                        var7 = var22.getAsJsonArray("targets");
                        var8 = 0;
                        for (JsonElement var10 : var7) {
                            try {
                                this.initTarget(var10);
                            }
                            catch (Exception var19) {
                                JsonException var12 = JsonException.func_151379_a(var19);
                                var12.func_151380_a("targets[" + var8 + "]");
                                throw var12;
                            }
                            ++var8;
                        }
                    }
                    if (!JsonUtils.jsonObjectFieldTypeIsArray(var22, "passes")) break block11;
                    var7 = var22.getAsJsonArray("passes");
                    var8 = 0;
                    for (JsonElement var10 : var7) {
                        try {
                            this.parsePass(p_152765_1_, var10);
                        }
                        catch (Exception var18) {
                            JsonException var12 = JsonException.func_151379_a(var18);
                            var12.func_151380_a("passes[" + var8 + "]");
                            throw var12;
                        }
                        ++var8;
                    }
                }
                catch (Exception var20) {
                    JsonException var6 = JsonException.func_151379_a(var20);
                    var6.func_151381_b(p_152765_2_.getResourcePath());
                    throw var6;
                }
            }
            catch (Throwable throwable) {
                IOUtils.closeQuietly(var4);
                throw throwable;
            }
        }
        IOUtils.closeQuietly((InputStream)var4);
    }

    private void initTarget(JsonElement p_148027_1_) throws JsonException {
        if (JsonUtils.jsonElementTypeIsString(p_148027_1_)) {
            this.addFramebuffer(p_148027_1_.getAsString(), this.mainFramebufferWidth, this.mainFramebufferHeight);
        } else {
            JsonObject var2 = JsonUtils.getElementAsJsonObject(p_148027_1_, "target");
            String var3 = JsonUtils.getJsonObjectStringFieldValue(var2, "name");
            int var4 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var2, "width", this.mainFramebufferWidth);
            int var5 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var2, "height", this.mainFramebufferHeight);
            if (this.mapFramebuffers.containsKey(var3)) {
                throw new JsonException(String.valueOf(var3) + " is already defined");
            }
            this.addFramebuffer(var3, var4, var5);
        }
    }

    private void parsePass(TextureManager p_152764_1_, JsonElement p_152764_2_) throws JsonException {
        JsonArray var26;
        JsonObject var3 = JsonUtils.getElementAsJsonObject(p_152764_2_, "pass");
        String var4 = JsonUtils.getJsonObjectStringFieldValue(var3, "name");
        String var5 = JsonUtils.getJsonObjectStringFieldValue(var3, "intarget");
        String var6 = JsonUtils.getJsonObjectStringFieldValue(var3, "outtarget");
        Framebuffer var7 = this.getFramebuffer(var5);
        Framebuffer var8 = this.getFramebuffer(var6);
        if (var7 == null) {
            throw new JsonException("Input target '" + var5 + "' does not exist");
        }
        if (var8 == null) {
            throw new JsonException("Output target '" + var6 + "' does not exist");
        }
        Shader var9 = this.addShader(var4, var7, var8);
        JsonArray var10 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var3, "auxtargets", null);
        if (var10 != null) {
            int var11 = 0;
            for (JsonElement var13 : var10) {
                block15: {
                    try {
                        JsonObject var14 = JsonUtils.getElementAsJsonObject(var13, "auxtarget");
                        String var30 = JsonUtils.getJsonObjectStringFieldValue(var14, "name");
                        String var16 = JsonUtils.getJsonObjectStringFieldValue(var14, "id");
                        Framebuffer var17 = this.getFramebuffer(var16);
                        if (var17 == null) {
                            ResourceLocation var18 = new ResourceLocation("textures/effect/" + var16 + ".png");
                            try {
                                this.resourceManager.getResource(var18);
                            }
                            catch (FileNotFoundException var24) {
                                throw new JsonException("Render target or texture '" + var16 + "' does not exist");
                            }
                            p_152764_1_.bindTexture(var18);
                            ITextureObject var19 = p_152764_1_.getTexture(var18);
                            int var20 = JsonUtils.getJsonObjectIntegerFieldValue(var14, "width");
                            int var21 = JsonUtils.getJsonObjectIntegerFieldValue(var14, "height");
                            boolean var22 = JsonUtils.getJsonObjectBooleanFieldValue(var14, "bilinear");
                            if (var22) {
                                GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
                                GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
                            } else {
                                GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
                                GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
                            }
                            var9.addAuxFramebuffer(var30, var19.getGlTextureId(), var20, var21);
                            break block15;
                        }
                        var9.addAuxFramebuffer(var30, var17, var17.framebufferTextureWidth, var17.framebufferTextureHeight);
                    }
                    catch (Exception var25) {
                        JsonException var15 = JsonException.func_151379_a(var25);
                        var15.func_151380_a("auxtargets[" + var11 + "]");
                        throw var15;
                    }
                }
                ++var11;
            }
        }
        if ((var26 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var3, "uniforms", null)) != null) {
            int var27 = 0;
            for (JsonElement var29 : var26) {
                try {
                    this.initUniform(var29);
                }
                catch (Exception var23) {
                    JsonException var31 = JsonException.func_151379_a(var23);
                    var31.func_151380_a("uniforms[" + var27 + "]");
                    throw var31;
                }
                ++var27;
            }
        }
    }

    private void initUniform(JsonElement p_148028_1_) throws JsonException {
        JsonObject var2 = JsonUtils.getElementAsJsonObject(p_148028_1_, "uniform");
        String var3 = JsonUtils.getJsonObjectStringFieldValue(var2, "name");
        ShaderUniform var4 = ((Shader)this.listShaders.get(this.listShaders.size() - 1)).getShaderManager().getShaderUniform(var3);
        if (var4 == null) {
            throw new JsonException("Uniform '" + var3 + "' does not exist");
        }
        float[] var5 = new float[4];
        int var6 = 0;
        JsonArray var7 = JsonUtils.getJsonObjectJsonArrayField(var2, "values");
        for (JsonElement var9 : var7) {
            try {
                var5[var6] = JsonUtils.getJsonElementFloatValue(var9, "value");
            }
            catch (Exception var12) {
                JsonException var11 = JsonException.func_151379_a(var12);
                var11.func_151380_a("values[" + var6 + "]");
                throw var11;
            }
            ++var6;
        }
        switch (var6) {
            default: {
                break;
            }
            case 1: {
                var4.set(var5[0]);
                break;
            }
            case 2: {
                var4.set(var5[0], var5[1]);
                break;
            }
            case 3: {
                var4.set(var5[0], var5[1], var5[2]);
                break;
            }
            case 4: {
                var4.set(var5[0], var5[1], var5[2], var5[3]);
            }
        }
    }

    public Framebuffer func_177066_a(String p_177066_1_) {
        return (Framebuffer)this.mapFramebuffers.get(p_177066_1_);
    }

    public void addFramebuffer(String p_148020_1_, int p_148020_2_, int p_148020_3_) {
        Framebuffer var4 = new Framebuffer(p_148020_2_, p_148020_3_, true);
        var4.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.mapFramebuffers.put(p_148020_1_, var4);
        if (p_148020_2_ == this.mainFramebufferWidth && p_148020_3_ == this.mainFramebufferHeight) {
            this.listFramebuffers.add(var4);
        }
    }

    public void deleteShaderGroup() {
        for (Framebuffer var2 : this.mapFramebuffers.values()) {
            var2.deleteFramebuffer();
        }
        for (Shader var3 : this.listShaders) {
            var3.deleteShader();
        }
        this.listShaders.clear();
    }

    public Shader addShader(String p_148023_1_, Framebuffer p_148023_2_, Framebuffer p_148023_3_) throws JsonException {
        Shader var4 = new Shader(this.resourceManager, p_148023_1_, p_148023_2_, p_148023_3_);
        this.listShaders.add(this.listShaders.size(), var4);
        return var4;
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

    public void createBindFramebuffers(int p_148026_1_, int p_148026_2_) {
        this.mainFramebufferWidth = this.mainFramebuffer.framebufferTextureWidth;
        this.mainFramebufferHeight = this.mainFramebuffer.framebufferTextureHeight;
        this.resetProjectionMatrix();
        for (Shader var4 : this.listShaders) {
            var4.setProjectionMatrix(this.projectionMatrix);
        }
        for (Framebuffer var5 : this.listFramebuffers) {
            var5.createBindFramebuffer(p_148026_1_, p_148026_2_);
        }
    }

    public void loadShaderGroup(float p_148018_1_) {
        if (p_148018_1_ < this.field_148037_k) {
            this.field_148036_j += 1.0f - this.field_148037_k;
            this.field_148036_j += p_148018_1_;
        } else {
            this.field_148036_j += p_148018_1_ - this.field_148037_k;
        }
        this.field_148037_k = p_148018_1_;
        while (this.field_148036_j > 20.0f) {
            this.field_148036_j -= 20.0f;
        }
        for (Shader var3 : this.listShaders) {
            var3.loadShader(this.field_148036_j / 20.0f);
        }
    }

    public final String getShaderGroupName() {
        return this.shaderGroupName;
    }

    private Framebuffer getFramebuffer(String p_148017_1_) {
        return p_148017_1_ == null ? null : (p_148017_1_.equals("minecraft:main") ? this.mainFramebuffer : (Framebuffer)this.mapFramebuffers.get(p_148017_1_));
    }
}

