// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.shader;

import net.minecraft.client.renderer.texture.ITextureObject;
import org.lwjgl.opengl.GL11;
import java.io.FileNotFoundException;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.IResource;
import java.io.InputStream;
import com.google.gson.JsonElement;
import net.minecraft.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import com.google.common.base.Charsets;
import com.google.gson.JsonParser;
import net.minecraft.client.util.JsonException;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureManager;
import javax.vecmath.Matrix4f;
import java.util.Map;
import java.util.List;
import net.minecraft.client.resources.IResourceManager;

public class ShaderGroup
{
    private Framebuffer mainFramebuffer;
    private IResourceManager resourceManager;
    private String shaderGroupName;
    private final List listShaders;
    private final Map mapFramebuffers;
    private final List listFramebuffers;
    private Matrix4f projectionMatrix;
    private int mainFramebufferWidth;
    private int mainFramebufferHeight;
    private float field_148036_j;
    private float field_148037_k;
    private static final String __OBFID = "CL_00001041";
    
    public ShaderGroup(final TextureManager p_i1050_1_, final IResourceManager p_i1050_2_, final Framebuffer p_i1050_3_, final ResourceLocation p_i1050_4_) throws JsonException {
        this.listShaders = Lists.newArrayList();
        this.mapFramebuffers = Maps.newHashMap();
        this.listFramebuffers = Lists.newArrayList();
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
    
    public void parseGroup(final TextureManager p_152765_1_, final ResourceLocation p_152765_2_) throws JsonException {
        final JsonParser var3 = new JsonParser();
        InputStream var4 = null;
        try {
            final IResource var5 = this.resourceManager.getResource(p_152765_2_);
            var4 = var5.getInputStream();
            final JsonObject var6 = var3.parse(IOUtils.toString(var4, Charsets.UTF_8)).getAsJsonObject();
            if (JsonUtils.jsonObjectFieldTypeIsArray(var6, "targets")) {
                final JsonArray var7 = var6.getAsJsonArray("targets");
                int var8 = 0;
                for (final JsonElement var10 : var7) {
                    try {
                        this.initTarget(var10);
                    }
                    catch (Exception var12) {
                        final JsonException var11 = JsonException.func_151379_a(var12);
                        var11.func_151380_a("targets[" + var8 + "]");
                        throw var11;
                    }
                    ++var8;
                }
            }
            if (JsonUtils.jsonObjectFieldTypeIsArray(var6, "passes")) {
                final JsonArray var7 = var6.getAsJsonArray("passes");
                int var8 = 0;
                for (final JsonElement var10 : var7) {
                    try {
                        this.parsePass(p_152765_1_, var10);
                    }
                    catch (Exception var13) {
                        final JsonException var11 = JsonException.func_151379_a(var13);
                        var11.func_151380_a("passes[" + var8 + "]");
                        throw var11;
                    }
                    ++var8;
                }
            }
        }
        catch (Exception var15) {
            final JsonException var14 = JsonException.func_151379_a(var15);
            var14.func_151381_b(p_152765_2_.getResourcePath());
            throw var14;
        }
        finally {
            IOUtils.closeQuietly(var4);
        }
    }
    
    private void initTarget(final JsonElement p_148027_1_) throws JsonException {
        if (JsonUtils.jsonElementTypeIsString(p_148027_1_)) {
            this.addFramebuffer(p_148027_1_.getAsString(), this.mainFramebufferWidth, this.mainFramebufferHeight);
        }
        else {
            final JsonObject var2 = JsonUtils.getElementAsJsonObject(p_148027_1_, "target");
            final String var3 = JsonUtils.getJsonObjectStringFieldValue(var2, "name");
            final int var4 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var2, "width", this.mainFramebufferWidth);
            final int var5 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(var2, "height", this.mainFramebufferHeight);
            if (this.mapFramebuffers.containsKey(var3)) {
                throw new JsonException(var3 + " is already defined");
            }
            this.addFramebuffer(var3, var4, var5);
        }
    }
    
    private void parsePass(final TextureManager p_152764_1_, final JsonElement p_152764_2_) throws JsonException {
        final JsonObject var3 = JsonUtils.getElementAsJsonObject(p_152764_2_, "pass");
        final String var4 = JsonUtils.getJsonObjectStringFieldValue(var3, "name");
        final String var5 = JsonUtils.getJsonObjectStringFieldValue(var3, "intarget");
        final String var6 = JsonUtils.getJsonObjectStringFieldValue(var3, "outtarget");
        final Framebuffer var7 = this.getFramebuffer(var5);
        final Framebuffer var8 = this.getFramebuffer(var6);
        if (var7 == null) {
            throw new JsonException("Input target '" + var5 + "' does not exist");
        }
        if (var8 == null) {
            throw new JsonException("Output target '" + var6 + "' does not exist");
        }
        final Shader var9 = this.addShader(var4, var7, var8);
        final JsonArray var10 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var3, "auxtargets", null);
        if (var10 != null) {
            int var11 = 0;
            for (final JsonElement var13 : var10) {
                try {
                    final JsonObject var14 = JsonUtils.getElementAsJsonObject(var13, "auxtarget");
                    final String var15 = JsonUtils.getJsonObjectStringFieldValue(var14, "name");
                    final String var16 = JsonUtils.getJsonObjectStringFieldValue(var14, "id");
                    final Framebuffer var17 = this.getFramebuffer(var16);
                    if (var17 == null) {
                        final ResourceLocation var18 = new ResourceLocation("textures/effect/" + var16 + ".png");
                        try {
                            this.resourceManager.getResource(var18);
                        }
                        catch (FileNotFoundException var31) {
                            throw new JsonException("Render target or texture '" + var16 + "' does not exist");
                        }
                        p_152764_1_.bindTexture(var18);
                        final ITextureObject var19 = p_152764_1_.getTexture(var18);
                        final int var20 = JsonUtils.getJsonObjectIntegerFieldValue(var14, "width");
                        final int var21 = JsonUtils.getJsonObjectIntegerFieldValue(var14, "height");
                        final boolean var22 = JsonUtils.getJsonObjectBooleanFieldValue(var14, "bilinear");
                        if (var22) {
                            GL11.glTexParameteri(3553, 10241, 9729);
                            GL11.glTexParameteri(3553, 10240, 9729);
                        }
                        else {
                            GL11.glTexParameteri(3553, 10241, 9728);
                            GL11.glTexParameteri(3553, 10240, 9728);
                        }
                        var9.addAuxFramebuffer(var15, var19.getGlTextureId(), var20, var21);
                    }
                    else {
                        var9.addAuxFramebuffer(var15, var17, var17.framebufferTextureWidth, var17.framebufferTextureHeight);
                    }
                }
                catch (Exception var24) {
                    final JsonException var23 = JsonException.func_151379_a(var24);
                    var23.func_151380_a("auxtargets[" + var11 + "]");
                    throw var23;
                }
                ++var11;
            }
        }
        final JsonArray var25 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var3, "uniforms", null);
        if (var25 != null) {
            int var26 = 0;
            for (final JsonElement var28 : var25) {
                try {
                    this.initUniform(var28);
                }
                catch (Exception var30) {
                    final JsonException var29 = JsonException.func_151379_a(var30);
                    var29.func_151380_a("uniforms[" + var26 + "]");
                    throw var29;
                }
                ++var26;
            }
        }
    }
    
    private void initUniform(final JsonElement p_148028_1_) throws JsonException {
        final JsonObject var2 = JsonUtils.getElementAsJsonObject(p_148028_1_, "uniform");
        final String var3 = JsonUtils.getJsonObjectStringFieldValue(var2, "name");
        final ShaderUniform var4 = this.listShaders.get(this.listShaders.size() - 1).getShaderManager().getShaderUniform(var3);
        if (var4 == null) {
            throw new JsonException("Uniform '" + var3 + "' does not exist");
        }
        final float[] var5 = new float[4];
        int var6 = 0;
        final JsonArray var7 = JsonUtils.getJsonObjectJsonArrayField(var2, "values");
        for (final JsonElement var9 : var7) {
            try {
                var5[var6] = JsonUtils.getJsonElementFloatValue(var9, "value");
            }
            catch (Exception var11) {
                final JsonException var10 = JsonException.func_151379_a(var11);
                var10.func_151380_a("values[" + var6 + "]");
                throw var10;
            }
            ++var6;
        }
        switch (var6) {
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
                break;
            }
        }
    }
    
    public Framebuffer func_177066_a(final String p_177066_1_) {
        return this.mapFramebuffers.get(p_177066_1_);
    }
    
    public void addFramebuffer(final String p_148020_1_, final int p_148020_2_, final int p_148020_3_) {
        final Framebuffer var4 = new Framebuffer(p_148020_2_, p_148020_3_, true);
        var4.setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.mapFramebuffers.put(p_148020_1_, var4);
        if (p_148020_2_ == this.mainFramebufferWidth && p_148020_3_ == this.mainFramebufferHeight) {
            this.listFramebuffers.add(var4);
        }
    }
    
    public void deleteShaderGroup() {
        for (final Framebuffer var2 : this.mapFramebuffers.values()) {
            var2.deleteFramebuffer();
        }
        for (final Shader var3 : this.listShaders) {
            var3.deleteShader();
        }
        this.listShaders.clear();
    }
    
    public Shader addShader(final String p_148023_1_, final Framebuffer p_148023_2_, final Framebuffer p_148023_3_) throws JsonException {
        final Shader var4 = new Shader(this.resourceManager, p_148023_1_, p_148023_2_, p_148023_3_);
        this.listShaders.add(this.listShaders.size(), var4);
        return var4;
    }
    
    private void resetProjectionMatrix() {
        (this.projectionMatrix = new Matrix4f()).setIdentity();
        this.projectionMatrix.m00 = 2.0f / this.mainFramebuffer.framebufferTextureWidth;
        this.projectionMatrix.m11 = 2.0f / -this.mainFramebuffer.framebufferTextureHeight;
        this.projectionMatrix.m22 = -0.0020001999f;
        this.projectionMatrix.m33 = 1.0f;
        this.projectionMatrix.m03 = -1.0f;
        this.projectionMatrix.m13 = 1.0f;
        this.projectionMatrix.m23 = -1.0001999f;
    }
    
    public void createBindFramebuffers(final int p_148026_1_, final int p_148026_2_) {
        this.mainFramebufferWidth = this.mainFramebuffer.framebufferTextureWidth;
        this.mainFramebufferHeight = this.mainFramebuffer.framebufferTextureHeight;
        this.resetProjectionMatrix();
        for (final Shader var4 : this.listShaders) {
            var4.setProjectionMatrix(this.projectionMatrix);
        }
        for (final Framebuffer var5 : this.listFramebuffers) {
            var5.createBindFramebuffer(p_148026_1_, p_148026_2_);
        }
    }
    
    public void loadShaderGroup(final float p_148018_1_) {
        if (p_148018_1_ < this.field_148037_k) {
            this.field_148036_j += 1.0f - this.field_148037_k;
            this.field_148036_j += p_148018_1_;
        }
        else {
            this.field_148036_j += p_148018_1_ - this.field_148037_k;
        }
        this.field_148037_k = p_148018_1_;
        while (this.field_148036_j > 20.0f) {
            this.field_148036_j -= 20.0f;
        }
        for (final Shader var3 : this.listShaders) {
            var3.loadShader(this.field_148036_j / 20.0f);
        }
    }
    
    public final String getShaderGroupName() {
        return this.shaderGroupName;
    }
    
    private Framebuffer getFramebuffer(final String p_148017_1_) {
        return (p_148017_1_ == null) ? null : (p_148017_1_.equals("minecraft:main") ? this.mainFramebuffer : this.mapFramebuffers.get(p_148017_1_));
    }
}
