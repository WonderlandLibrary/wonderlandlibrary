// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import com.elementars.eclient.module.Category;
import com.elementars.eclient.module.Module;

public class Australia extends Module
{
    public Australia() {
        super("Australia", "Best Module", 0, Category.MISC, true);
    }
    
    @Override
    public void onUpdate() {
        if (OpenGlHelper.shadersSupported && Australia.mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (Australia.mc.entityRenderer.getShaderGroup() != null) {
                Australia.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            try {
                Australia.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/flip.json"));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (Australia.mc.entityRenderer.getShaderGroup() != null && Australia.mc.currentScreen == null) {
            Australia.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
        Australia.mc.player.setFire(1);
    }
}
