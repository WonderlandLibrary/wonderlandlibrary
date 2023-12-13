/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;

public class RenderHorse
extends RenderLiving<EntityHorse> {
    private static final Map<String, ResourceLocation> field_110852_a = Maps.newHashMap();
    private static final ResourceLocation whiteHorseTextures = new ResourceLocation("textures/entity/horse/horse_white.png");
    private static final ResourceLocation muleTextures = new ResourceLocation("textures/entity/horse/mule.png");
    private static final ResourceLocation donkeyTextures = new ResourceLocation("textures/entity/horse/donkey.png");
    private static final ResourceLocation zombieHorseTextures = new ResourceLocation("textures/entity/horse/horse_zombie.png");
    private static final ResourceLocation skeletonHorseTextures = new ResourceLocation("textures/entity/horse/horse_skeleton.png");

    public RenderHorse(RenderManager rendermanagerIn, ModelHorse model, float shadowSizeIn) {
        super(rendermanagerIn, model, shadowSizeIn);
    }

    @Override
    protected void preRenderCallback(EntityHorse entitylivingbaseIn, float partialTickTime) {
        float f2 = 1.0f;
        int i2 = entitylivingbaseIn.getHorseType();
        if (i2 == 1) {
            f2 *= 0.87f;
        } else if (i2 == 2) {
            f2 *= 0.92f;
        }
        GlStateManager.scale(f2, f2, f2);
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityHorse entity) {
        if (!entity.func_110239_cn()) {
            switch (entity.getHorseType()) {
                default: {
                    return whiteHorseTextures;
                }
                case 1: {
                    return donkeyTextures;
                }
                case 2: {
                    return muleTextures;
                }
                case 3: {
                    return zombieHorseTextures;
                }
                case 4: 
            }
            return skeletonHorseTextures;
        }
        return this.func_110848_b(entity);
    }

    private ResourceLocation func_110848_b(EntityHorse horse) {
        String s2 = horse.getHorseTexture();
        if (!horse.func_175507_cI()) {
            return null;
        }
        ResourceLocation resourcelocation = field_110852_a.get(s2);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s2);
            Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, new LayeredTexture(horse.getVariantTexturePaths()));
            field_110852_a.put(s2, resourcelocation);
        }
        return resourcelocation;
    }
}

