/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.particle.EntityFX
 *  net.minecraft.client.renderer.culling.ICamera
 */
package net.dev.important.patcher.util.world;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.culling.ICamera;

public class ParticleCulling {
    public static ICamera camera;

    public static boolean shouldRender(EntityFX entityFX) {
        return entityFX != null && (camera == null || entityFX.field_70140_Q > -1.0f);
    }
}

