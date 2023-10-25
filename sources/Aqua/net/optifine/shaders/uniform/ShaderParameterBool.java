package net.optifine.shaders.uniform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.optifine.expr.IExpressionBool;
import net.optifine.shaders.uniform.ShaderParameterBool;

public enum ShaderParameterBool implements IExpressionBool
{
    IS_ALIVE("is_alive"),
    IS_BURNING("is_burning"),
    IS_CHILD("is_child"),
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
    private static final ShaderParameterBool[] VALUES;

    private ShaderParameterBool(String name) {
        this.name = name;
        this.renderManager = Minecraft.getMinecraft().getRenderManager();
    }

    public String getName() {
        return this.name;
    }

    public boolean eval() {
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
            switch (1.$SwitchMap$net$optifine$shaders$uniform$ShaderParameterBool[this.ordinal()]) {
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

    public static ShaderParameterBool parse(String str) {
        if (str == null) {
            return null;
        }
        for (int i = 0; i < VALUES.length; ++i) {
            ShaderParameterBool shaderparameterbool = VALUES[i];
            if (!shaderparameterbool.getName().equals((Object)str)) continue;
            return shaderparameterbool;
        }
        return null;
    }

    static {
        VALUES = ShaderParameterBool.values();
    }
}
