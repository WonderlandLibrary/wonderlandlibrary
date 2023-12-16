// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.entity.model.anim;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.optifine.expr.ExpressionType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.optifine.expr.IExpressionFloat;

public enum RenderEntityParameterFloat implements IExpressionFloat
{
    LIMB_SWING("LIMB_SWING", 0, "limb_swing"), 
    LIMB_SWING_SPEED("LIMB_SWING_SPEED", 1, "limb_speed"), 
    AGE("AGE", 2, "age"), 
    HEAD_YAW("HEAD_YAW", 3, "head_yaw"), 
    HEAD_PITCH("HEAD_PITCH", 4, "head_pitch"), 
    SCALE("SCALE", 5, "scale"), 
    HEALTH("HEALTH", 6, "health"), 
    HURT_TIME("HURT_TIME", 7, "hurt_time"), 
    IDLE_TIME("IDLE_TIME", 8, "idle_time"), 
    MAX_HEALTH("MAX_HEALTH", 9, "max_health"), 
    MOVE_FORWARD("MOVE_FORWARD", 10, "move_forward"), 
    MOVE_STRAFING("MOVE_STRAFING", 11, "move_strafing"), 
    PARTIAL_TICKS("PARTIAL_TICKS", 12, "partial_ticks"), 
    POS_X("POS_X", 13, "pos_x"), 
    POS_Y("POS_Y", 14, "pos_y"), 
    POS_Z("POS_Z", 15, "pos_z"), 
    REVENGE_TIME("REVENGE_TIME", 16, "revenge_time"), 
    SWING_PROGRESS("SWING_PROGRESS", 17, "swing_progress");
    
    private String name;
    private RenderManager renderManager;
    private static final RenderEntityParameterFloat[] VALUES;
    
    static {
        VALUES = values();
    }
    
    private RenderEntityParameterFloat(final String s, final int n, final String name) {
        this.name = name;
        this.renderManager = Minecraft.getMinecraft().getRenderManager();
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public ExpressionType getExpressionType() {
        return ExpressionType.FLOAT;
    }
    
    @Override
    public float eval() {
        final Render render = this.renderManager.renderRender;
        if (render == null) {
            return 0.0f;
        }
        Label_0249: {
            if (render instanceof RendererLivingEntity) {
                final RendererLivingEntity rendererlivingentity = (RendererLivingEntity)render;
                switch (this) {
                    case LIMB_SWING: {
                        return rendererlivingentity.renderLimbSwing;
                    }
                    case LIMB_SWING_SPEED: {
                        return rendererlivingentity.renderLimbSwingAmount;
                    }
                    case AGE: {
                        return rendererlivingentity.renderAgeInTicks;
                    }
                    case HEAD_YAW: {
                        return rendererlivingentity.renderHeadYaw;
                    }
                    case HEAD_PITCH: {
                        return rendererlivingentity.renderHeadPitch;
                    }
                    case SCALE: {
                        return rendererlivingentity.renderScaleFactor;
                    }
                    default: {
                        final EntityLivingBase entitylivingbase = rendererlivingentity.renderEntity;
                        if (entitylivingbase == null) {
                            return 0.0f;
                        }
                        switch (this) {
                            case HEALTH: {
                                return entitylivingbase.getHealth();
                            }
                            case HURT_TIME: {
                                return (float)entitylivingbase.hurtTime;
                            }
                            case IDLE_TIME: {
                                return (float)entitylivingbase.getAge();
                            }
                            case MAX_HEALTH: {
                                return entitylivingbase.getMaxHealth();
                            }
                            case MOVE_FORWARD: {
                                return entitylivingbase.moveForward;
                            }
                            case MOVE_STRAFING: {
                                return entitylivingbase.moveStrafing;
                            }
                            case POS_X: {
                                return (float)entitylivingbase.posX;
                            }
                            case POS_Y: {
                                return (float)entitylivingbase.posY;
                            }
                            case POS_Z: {
                                return (float)entitylivingbase.posZ;
                            }
                            case REVENGE_TIME: {
                                return (float)entitylivingbase.getRevengeTimer();
                            }
                            case SWING_PROGRESS: {
                                return entitylivingbase.getSwingProgress(rendererlivingentity.renderPartialTicks);
                            }
                            default: {
                                break Label_0249;
                            }
                        }
                        break;
                    }
                }
            }
        }
        return 0.0f;
    }
    
    public static RenderEntityParameterFloat parse(final String str) {
        if (str == null) {
            return null;
        }
        for (int i = 0; i < RenderEntityParameterFloat.VALUES.length; ++i) {
            final RenderEntityParameterFloat renderentityparameterfloat = RenderEntityParameterFloat.VALUES[i];
            if (renderentityparameterfloat.getName().equals(str)) {
                return renderentityparameterfloat;
            }
        }
        return null;
    }
}
