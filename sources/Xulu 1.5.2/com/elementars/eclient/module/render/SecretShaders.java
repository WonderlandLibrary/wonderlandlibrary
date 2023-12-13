// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class SecretShaders extends Module
{
    private final Value<String> shader;
    
    public SecretShaders() {
        super("SecretShaders", "Brings back super secret settings shaders", 0, Category.RENDER, true);
        this.shader = this.register(new Value<String>("Shader", this, "notch", new ArrayList<String>(Arrays.asList("antialias", "art", "bits", "blobs", "blobs2", "blur", "bumpy", "color_convolve", "creeper", "deconverge", "desaturate", "entity_outline", "flip", "fxaa", "green", "invert", "notch", "ntsc", "outline", "pencil", "phosphor", "scan_pincusion", "sobel", "spider", "wobble"))));
    }
    
    @Override
    public void onEnable() {
        if (OpenGlHelper.shadersSupported && SecretShaders.mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (SecretShaders.mc.entityRenderer.getShaderGroup() != null) {
                SecretShaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            try {
                SecretShaders.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/" + this.shader.getValue() + ".json"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (SecretShaders.mc.entityRenderer.getShaderGroup() != null && SecretShaders.mc.currentScreen == null) {
            SecretShaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
    
    @Override
    public void onDisable() {
        if (SecretShaders.mc.entityRenderer.getShaderGroup() != null) {
            SecretShaders.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }
}
