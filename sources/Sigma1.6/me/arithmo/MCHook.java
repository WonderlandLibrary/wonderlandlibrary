/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo;

import me.arithmo.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.renderer.texture.TextureManager;

public class MCHook
extends Minecraft {
    public MCHook(GameConfiguration gc) {
        super(gc);
    }

    @Override
    protected void func_180510_a(TextureManager texMan) {
        try {
            new Client().setup();
        }
        catch (Exception e) {
            throw new RuntimeException();
        }
        super.func_180510_a(texMan);
    }
}

