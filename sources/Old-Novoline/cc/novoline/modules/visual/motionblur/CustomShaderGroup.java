package cc.novoline.modules.visual.motionblur;

import cc.novoline.Novoline;
import cc.novoline.modules.visual.Camera;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class CustomShaderGroup extends ShaderGroup {
    public CustomShaderGroup(TextureManager p_i1050_1_, IResourceManager p_i1050_2_, Framebuffer p_i1050_3_, ResourceLocation p_i1050_4_) throws IOException, JsonSyntaxException {
        super(p_i1050_1_, p_i1050_2_, p_i1050_3_, p_i1050_4_);
    }

    @Override
    public void parseGroup(TextureManager p_152765_1_, ResourceLocation p_152765_2_) throws IOException, JsonSyntaxException {
        JsonObject jsonobject = Novoline.getInstance().getModuleManager().getModule(Camera.class).getJsonObject();

        if (JsonUtils.isJsonArray(jsonobject, "targets")) {
            JsonArray jsonarray = jsonobject.getAsJsonArray("targets");
            int i = 0;

            for (JsonElement jsonelement : jsonarray) {
                try {
                    super.initTarget(jsonelement);
                } catch (Exception exception1) {
                    JsonException jsonexception1 = JsonException.func_151379_a(exception1);
                    jsonexception1.func_151380_a("targets[" + i + "]");
                    throw jsonexception1;
                }

                ++i;
            }
        }

        if (JsonUtils.isJsonArray(jsonobject, "passes")) {
            JsonArray jsonarray1 = jsonobject.getAsJsonArray("passes");
            int j = 0;

            for (JsonElement jsonelement1 : jsonarray1) {
                try {
                    super.parsePass(p_152765_1_, jsonelement1);
                } catch (Exception exception) {
                    JsonException jsonexception2 = JsonException.func_151379_a(exception);
                    jsonexception2.func_151380_a("passes[" + j + "]");
                    throw jsonexception2;
                }

                ++j;
            }
        }
    }
}
