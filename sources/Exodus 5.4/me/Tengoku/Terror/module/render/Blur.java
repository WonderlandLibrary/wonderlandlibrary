/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.render;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import me.Tengoku.Terror.API.impl.Hwid;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class Blur
extends Module {
    public Blur() {
        super("Blur", 0, Category.MISC, "");
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (OpenGlHelper.shadersSupported) {
            if (Blur.mc.entityRenderer.theShaderGroup != null) {
                Blur.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            }
            if (this.isToggled()) {
                System.out.println(Hwid.getHWID2());
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Blur.mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/none.json"));
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}

