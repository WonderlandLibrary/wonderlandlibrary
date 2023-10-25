/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class RenderRabbit
extends RenderLiving<EntityRabbit> {
    private static final ResourceLocation BLACK;
    private static final ResourceLocation CAERBANNOG;
    private static final ResourceLocation BROWN;
    private static final ResourceLocation WHITE;
    private static final ResourceLocation WHITE_SPLOTCHED;
    private static final ResourceLocation TOAST;
    private static final ResourceLocation SALT;
    private static final ResourceLocation GOLD;

    @Override
    protected ResourceLocation getEntityTexture(EntityRabbit entityRabbit) {
        String string = EnumChatFormatting.getTextWithoutFormattingCodes(entityRabbit.getName());
        if (string != null && string.equals("Toast")) {
            return TOAST;
        }
        switch (entityRabbit.getRabbitType()) {
            default: {
                return BROWN;
            }
            case 1: {
                return WHITE;
            }
            case 2: {
                return BLACK;
            }
            case 3: {
                return WHITE_SPLOTCHED;
            }
            case 4: {
                return GOLD;
            }
            case 5: {
                return SALT;
            }
            case 99: 
        }
        return CAERBANNOG;
    }

    static {
        BROWN = new ResourceLocation("textures/entity/rabbit/brown.png");
        WHITE = new ResourceLocation("textures/entity/rabbit/white.png");
        BLACK = new ResourceLocation("textures/entity/rabbit/black.png");
        GOLD = new ResourceLocation("textures/entity/rabbit/gold.png");
        SALT = new ResourceLocation("textures/entity/rabbit/salt.png");
        WHITE_SPLOTCHED = new ResourceLocation("textures/entity/rabbit/white_splotched.png");
        TOAST = new ResourceLocation("textures/entity/rabbit/toast.png");
        CAERBANNOG = new ResourceLocation("textures/entity/rabbit/caerbannog.png");
    }

    public RenderRabbit(RenderManager renderManager, ModelBase modelBase, float f) {
        super(renderManager, modelBase, f);
    }
}

