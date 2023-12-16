// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.shaders.uniform;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.optifine.expr.ExpressionType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.optifine.expr.IExpressionBool;

public enum ShaderParameterBool implements IExpressionBool
{
    IS_ALIVE("IS_ALIVE", 0, "is_alive"), 
    IS_BURNING("IS_BURNING", 1, "is_burning"), 
    IS_CHILD("IS_CHILD", 2, "is_child"), 
    IS_GLOWING("IS_GLOWING", 3, "is_glowing"), 
    IS_HURT("IS_HURT", 4, "is_hurt"), 
    IS_IN_LAVA("IS_IN_LAVA", 5, "is_in_lava"), 
    IS_IN_WATER("IS_IN_WATER", 6, "is_in_water"), 
    IS_INVISIBLE("IS_INVISIBLE", 7, "is_invisible"), 
    IS_ON_GROUND("IS_ON_GROUND", 8, "is_on_ground"), 
    IS_RIDDEN("IS_RIDDEN", 9, "is_ridden"), 
    IS_RIDING("IS_RIDING", 10, "is_riding"), 
    IS_SNEAKING("IS_SNEAKING", 11, "is_sneaking"), 
    IS_SPRINTING("IS_SPRINTING", 12, "is_sprinting"), 
    IS_WET("IS_WET", 13, "is_wet");
    
    private String name;
    private RenderManager renderManager;
    private static final ShaderParameterBool[] VALUES;
    
    static {
        VALUES = values();
    }
    
    private ShaderParameterBool(final String s, final int n, final String name) {
        this.name = name;
        this.renderManager = Minecraft.getMinecraft().getRenderManager();
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.BOOL;
    }
    
    @Override
    public boolean eval() {
        final Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
            switch (this) {
                case IS_ALIVE: {
                    return entitylivingbase.isEntityAlive();
                }
                case IS_BURNING: {
                    return entitylivingbase.isBurning();
                }
                case IS_CHILD: {
                    return entitylivingbase.isChild();
                }
                case IS_HURT: {
                    return entitylivingbase.hurtTime > 0;
                }
                case IS_IN_LAVA: {
                    return entitylivingbase.isInLava();
                }
                case IS_IN_WATER: {
                    return entitylivingbase.isInWater();
                }
                case IS_INVISIBLE: {
                    return entitylivingbase.isInvisible();
                }
                case IS_ON_GROUND: {
                    return entitylivingbase.onGround;
                }
                case IS_RIDDEN: {
                    return entitylivingbase.riddenByEntity != null;
                }
                case IS_RIDING: {
                    return entitylivingbase.isRiding();
                }
                case IS_SNEAKING: {
                    return entitylivingbase.isSneaking();
                }
                case IS_SPRINTING: {
                    return entitylivingbase.isSprinting();
                }
                case IS_WET: {
                    return entitylivingbase.isWet();
                }
            }
        }
        return false;
    }
    
    public static ShaderParameterBool parse(final String str) {
        if (str == null) {
            return null;
        }
        for (int i = 0; i < ShaderParameterBool.VALUES.length; ++i) {
            final ShaderParameterBool shaderparameterbool = ShaderParameterBool.VALUES[i];
            if (shaderparameterbool.getName().equals(str)) {
                return shaderparameterbool;
            }
        }
        return null;
    }
}
