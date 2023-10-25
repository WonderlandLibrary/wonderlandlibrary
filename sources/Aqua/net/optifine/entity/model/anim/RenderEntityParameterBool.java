package net.optifine.entity.model.anim;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.optifine.entity.model.anim.RenderEntityParameterBool;
import net.optifine.expr.ExpressionType;
import net.optifine.expr.IExpressionBool;

public enum RenderEntityParameterBool implements IExpressionBool
{
    IS_ALIVE("is_alive"),
    IS_BURNING("is_burning"),
    IS_CHILD("is_child"),
    IS_GLOWING("is_glowing"),
    IS_HURT("is_hurt"),
    IS_IN_LAVA("is_in_lava"),
    IS_IN_WATER("is_in_water"),
    IS_INVISIBLE("is_invisible"),
    IS_ON_GROUND("is_on_ground"),
    IS_RIDDEN("is_ridden"),
    IS_RIDING("is_riding"),
    IS_SNEAKING("is_sneaking"),
    IS_SPRINTING("is_sprinting"),
    IS_WET("is_wet");

    private String name;
    private RenderManager renderManager;
    private static final RenderEntityParameterBool[] VALUES;

    private RenderEntityParameterBool(String name) {
        this.name = name;
        this.renderManager = Minecraft.getMinecraft().getRenderManager();
    }

    public String getName() {
        return this.name;
    }

    public ExpressionType getExpressionType() {
        return ExpressionType.BOOL;
    }

    public boolean eval() {
        Render render = this.renderManager.renderRender;
        if (render == null) {
            return false;
        }
        if (render instanceof RendererLivingEntity) {
            RendererLivingEntity rendererlivingentity = (RendererLivingEntity)render;
            EntityLivingBase entitylivingbase = rendererlivingentity.renderEntity;
            if (entitylivingbase == null) {
                return false;
            }
            switch (1.$SwitchMap$net$optifine$entity$model$anim$RenderEntityParameterBool[this.ordinal()]) {
                case 1: {
                    return entitylivingbase.isEntityAlive();
                }
                case 2: {
                    return entitylivingbase.isBurning();
                }
                case 3: {
                    return entitylivingbase.isChild();
                }
                case 4: {
                    return entitylivingbase.hurtTime > 0;
                }
                case 5: {
                    return entitylivingbase.isInLava();
                }
                case 6: {
                    return entitylivingbase.isInWater();
                }
                case 7: {
                    return entitylivingbase.isInvisible();
                }
                case 8: {
                    return entitylivingbase.onGround;
                }
                case 9: {
                    return entitylivingbase.riddenByEntity != null;
                }
                case 10: {
                    return entitylivingbase.isRiding();
                }
                case 11: {
                    return entitylivingbase.isSneaking();
                }
                case 12: {
                    return entitylivingbase.isSprinting();
                }
                case 13: {
                    return entitylivingbase.isWet();
                }
            }
        }
        return false;
    }

    public static RenderEntityParameterBool parse(String str) {
        if (str == null) {
            return null;
        }
        for (int i = 0; i < VALUES.length; ++i) {
            RenderEntityParameterBool renderentityparameterbool = VALUES[i];
            if (!renderentityparameterbool.getName().equals((Object)str)) continue;
            return renderentityparameterbool;
        }
        return null;
    }

    static {
        VALUES = RenderEntityParameterBool.values();
    }
}
