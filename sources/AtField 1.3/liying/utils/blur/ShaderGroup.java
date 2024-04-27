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
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.resources.IResource
 *  net.minecraft.client.resources.IResourceManager
 *  net.minecraft.client.shader.Framebuffer
 *  net.minecraft.client.shader.Shader
 *  net.minecraft.client.shader.ShaderUniform
 *  net.minecraft.client.util.JsonException
 *  net.minecraft.util.JsonUtils
 *  net.minecraft.util.ResourceLocation
 *  org.apache.commons.io.IOUtils
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.util.vector.Matrix4f
 */
package liying.utils.blur;

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
    private int mainFramebufferWidth;
    private final Framebuffer mainFramebuffer;
    private final List listShaders = Lists.newArrayList();
    private final Map mapFramebuffers = Maps.newHashMap();
    private Matrix4f projectionMatrix;
    private final List listFramebuffers = Lists.newArrayList();
    private final String shaderGroupName;
    private final IResourceManager resourceManager;
    private int mainFramebufferHeight;
    private float field_148037_k;
    private float field_148036_j;

    public Shader addShader(String string, Framebuffer framebuffer, Framebuffer framebuffer2) throws IOException {
        Shader shader = new Shader(this.resourceManager, string, framebuffer, framebuffer2);
        this.listShaders.add(this.listShaders.size(), shader);
        return shader;
    }

    private void initTarget(JsonElement jsonElement) throws JsonException {
        if (JsonUtils.func_151211_a((JsonElement)jsonElement)) {
            this.addFramebuffer(jsonElement.getAsString(), this.mainFramebufferWidth, this.mainFramebufferHeight);
        } else {
            JsonObject jsonObject = JsonUtils.func_151210_l((JsonElement)jsonElement, (String)"target");
            String string = JsonUtils.func_151200_h((JsonObject)jsonObject, (String)"name");
            int n = JsonUtils.func_151208_a((JsonObject)jsonObject, (String)"width", (int)this.mainFramebufferWidth);
            int n2 = JsonUtils.func_151208_a((JsonObject)jsonObject, (String)"height", (int)this.mainFramebufferHeight);
            if (this.mapFramebuffers.containsKey(string)) {
                throw new JsonException(string + " is already defined");
            }
            this.addFramebuffer(string, n, n2);
        }
    }

    private void initUniform(JsonElement jsonElement) throws JsonException {
        JsonObject jsonObject = JsonUtils.func_151210_l((JsonElement)jsonElement, (String)"uniform");
        String string = JsonUtils.func_151200_h((JsonObject)jsonObject, (String)"name");
        ShaderUniform shaderUniform = ((Shader)this.listShaders.get(this.listShaders.size() - 1)).func_148043_c().func_147991_a(string);
        if (shaderUniform == null) {
            throw new JsonException("Uniform '" + string + "' does not exist");
        }
        float[] fArray = new float[4];
        int n = 0;
        for (JsonElement jsonElement2 : JsonUtils.func_151214_t((JsonObject)jsonObject, (String)"values")) {
            try {
                fArray[n] = JsonUtils.func_151220_d((JsonElement)jsonElement2, (String)"value");
            }
            catch (Exception exception) {
                JsonException jsonException = JsonException.func_151379_a((Exception)exception);
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
                shaderUniform.func_148090_a(fArray[0]);
                break;
            }
            case 2: {
                shaderUniform.func_148087_a(fArray[0], fArray[1]);
                break;
            }
            case 3: {
                shaderUniform.func_148095_a(fArray[0], fArray[1], fArray[2]);
                break;
            }
            case 4: {
                shaderUniform.func_148081_a(fArray[0], fArray[1], fArray[2], fArray[3]);
            }
        }
    }

    private Framebuffer getFramebuffer(String string) {
        return string == null ? null : (string.equals("minecraft:main") ? this.mainFramebuffer : (Framebuffer)this.mapFramebuffers.get(string));
    }

    public void addFramebuffer(String string, int n, int n2) {
        Framebuffer framebuffer = new Framebuffer(n, n2, true);
        framebuffer.func_147604_a(0.0f, 0.0f, 0.0f, 0.0f);
        this.mapFramebuffers.put(string, framebuffer);
        if (n == this.mainFramebufferWidth && n2 == this.mainFramebufferHeight) {
            this.listFramebuffers.add(framebuffer);
        }
    }

    public ShaderGroup(TextureManager textureManager, IResourceManager iResourceManager, Framebuffer framebuffer, ResourceLocation resourceLocation) throws IOException, JsonSyntaxException {
        this.resourceManager = iResourceManager;
        this.mainFramebuffer = framebuffer;
        this.field_148036_j = 0.0f;
        this.field_148037_k = 0.0f;
        this.mainFramebufferWidth = framebuffer.field_147621_c;
        this.mainFramebufferHeight = framebuffer.field_147618_d;
        this.shaderGroupName = resourceLocation.toString();
        this.resetProjectionMatrix();
        this.parseGroup(textureManager, resourceLocation);
    }

    public void createBindFramebuffers(int n, int n2) {
        this.mainFramebufferWidth = this.mainFramebuffer.field_147622_a;
        this.mainFramebufferHeight = this.mainFramebuffer.field_147620_b;
        this.resetProjectionMatrix();
        for (Shader shader : this.listShaders) {
            shader.func_148045_a(this.projectionMatrix);
        }
        for (Shader shader : this.listFramebuffers) {
            shader.func_147613_a(n, n2);
        }
    }

    public List getShaders() {
        return this.listShaders;
    }

    public final String getShaderGroupName() {
        return this.shaderGroupName;
    }

    public void deleteShaderGroup() {
        for (Framebuffer framebuffer : this.mapFramebuffers.values()) {
            framebuffer.func_147608_a();
        }
        for (Framebuffer framebuffer : this.listShaders) {
            framebuffer.func_148044_b();
        }
        this.listShaders.clear();
    }

    private void resetProjectionMatrix() {
        this.projectionMatrix = new Matrix4f();
        this.projectionMatrix.setIdentity();
        this.projectionMatrix.m00 = 2.0f / (float)this.mainFramebuffer.field_147622_a;
        this.projectionMatrix.m11 = 2.0f / (float)(-this.mainFramebuffer.field_147620_b);
        this.projectionMatrix.m22 = -0.0020001999f;
        this.projectionMatrix.m33 = 1.0f;
        this.projectionMatrix.m03 = -1.0f;
        this.projectionMatrix.m13 = 1.0f;
        this.projectionMatrix.m23 = -1.0001999f;
    }

    public void parseGroup(TextureManager textureManager, ResourceLocation resourceLocation) throws IOException, JsonSyntaxException {
        InputStream inputStream;
        block9: {
            JsonParser jsonParser = new JsonParser();
            inputStream = null;
            try {
                int n;
                JsonArray jsonArray;
                IResource iResource = this.resourceManager.func_110536_a(resourceLocation);
                inputStream = iResource.func_110527_b();
                JsonObject jsonObject = jsonParser.parse(IOUtils.toString((InputStream)inputStream, (Charset)Charsets.UTF_8)).getAsJsonObject();
                if (JsonUtils.func_151202_d((JsonObject)jsonObject, (String)"targets")) {
                    jsonArray = jsonObject.getAsJsonArray("targets");
                    n = 0;
                    for (JsonElement jsonElement : jsonArray) {
                        try {
                            this.initTarget(jsonElement);
                        }
                        catch (Exception exception) {
                            JsonException jsonException = JsonException.func_151379_a((Exception)exception);
                            jsonException.func_151380_a("targets[" + n + "]");
                            throw jsonException;
                        }
                        ++n;
                    }
                }
                if (!JsonUtils.func_151202_d((JsonObject)jsonObject, (String)"passes")) break block9;
                jsonArray = jsonObject.getAsJsonArray("passes");
                n = 0;
                for (JsonElement jsonElement : jsonArray) {
                    try {
                        this.parsePass(textureManager, jsonElement);
                    }
                    catch (Exception exception) {
                        JsonException jsonException = JsonException.func_151379_a((Exception)exception);
                        jsonException.func_151380_a("passes[" + n + "]");
                        throw jsonException;
                    }
                    ++n;
                }
            }
            catch (Exception exception) {
                JsonException jsonException = JsonException.func_151379_a((Exception)exception);
                jsonException.func_151381_b(resourceLocation.func_110623_a());
                throw jsonException;
            }
        }
        IOUtils.closeQuietly((InputStream)inputStream);
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
            shader.func_148042_a(this.field_148036_j / 20.0f);
        }
    }

    private void parsePass(TextureManager textureManager, JsonElement jsonElement) throws IOException {
        JsonArray jsonArray;
        String string;
        JsonObject jsonObject = JsonUtils.func_151210_l((JsonElement)jsonElement, (String)"pass");
        String string2 = JsonUtils.func_151200_h((JsonObject)jsonObject, (String)"name");
        String string3 = JsonUtils.func_151200_h((JsonObject)jsonObject, (String)"intarget");
        String string4 = JsonUtils.func_151200_h((JsonObject)jsonObject, (String)"outtarget");
        Framebuffer framebuffer = this.getFramebuffer(string3);
        Framebuffer framebuffer2 = this.getFramebuffer(string4);
        if (framebuffer == null) {
            throw new JsonException("Input target '" + string3 + "' does not exist");
        }
        if (framebuffer2 == null) {
            throw new JsonException("Output target '" + string4 + "' does not exist");
        }
        Shader shader = this.addShader(string2, framebuffer, framebuffer2);
        JsonArray jsonArray2 = JsonUtils.func_151213_a((JsonObject)jsonObject, (String)"auxtargets", null);
        if (jsonArray2 != null) {
            int n = 0;
            for (Object object : jsonArray2) {
                block15: {
                    Object object2;
                    try {
                        JsonObject jsonObject2 = JsonUtils.func_151210_l((JsonElement)object, (String)"auxtarget");
                        object2 = JsonUtils.func_151200_h((JsonObject)jsonObject2, (String)"name");
                        string = JsonUtils.func_151200_h((JsonObject)jsonObject2, (String)"id");
                        Framebuffer framebuffer3 = this.getFramebuffer(string);
                        if (framebuffer3 == null) {
                            ResourceLocation resourceLocation = new ResourceLocation("textures/effect/" + string + "Scare.png");
                            try {
                                this.resourceManager.func_110536_a(resourceLocation);
                            }
                            catch (FileNotFoundException fileNotFoundException) {
                                throw new JsonException("Render target or texture '" + string + "' does not exist");
                            }
                            textureManager.func_110577_a(resourceLocation);
                            ITextureObject iTextureObject = textureManager.func_110581_b(resourceLocation);
                            int n2 = JsonUtils.func_151203_m((JsonObject)jsonObject2, (String)"width");
                            int n3 = JsonUtils.func_151203_m((JsonObject)jsonObject2, (String)"height");
                            boolean bl = JsonUtils.func_151212_i((JsonObject)jsonObject2, (String)"bilinear");
                            if (bl) {
                                GL11.glTexParameteri((int)3553, (int)10241, (int)9729);
                                GL11.glTexParameteri((int)3553, (int)10240, (int)9729);
                            } else {
                                GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
                                GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
                            }
                            shader.func_148041_a((String)object2, (Object)iTextureObject.func_110552_b(), n2, n3);
                            break block15;
                        }
                        shader.func_148041_a((String)object2, (Object)framebuffer3, framebuffer3.field_147622_a, framebuffer3.field_147620_b);
                    }
                    catch (Exception exception) {
                        object2 = JsonException.func_151379_a((Exception)exception);
                        object2.func_151381_b("auxtargets[" + n + "]");
                        throw object2;
                    }
                }
                ++n;
            }
        }
        if ((jsonArray = JsonUtils.func_151213_a((JsonObject)jsonObject, (String)"uniforms", null)) != null) {
            int n = 0;
            for (JsonObject jsonObject2 : jsonArray) {
                try {
                    this.initUniform((JsonElement)jsonObject2);
                }
                catch (Exception exception) {
                    string = JsonException.func_151379_a((Exception)exception);
                    string.func_151380_a("uniforms[" + n + "]");
                    throw string;
                }
                ++n;
            }
        }
    }

    public Framebuffer getFramebufferRaw(String string) {
        return (Framebuffer)this.mapFramebuffers.get(string);
    }
}

