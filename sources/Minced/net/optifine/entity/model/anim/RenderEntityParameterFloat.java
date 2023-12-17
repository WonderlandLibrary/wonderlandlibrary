// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.entity.model.anim;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.optifine.expr.IExpressionFloat;

public enum RenderEntityParameterFloat implements IExpressionFloat
{
    LIMB_SWING("limb_swing"), 
    LIMB_SWING_SPEED("limb_speed"), 
    AGE("age"), 
    HEAD_YAW("head_yaw"), 
    HEAD_PITCH("head_pitch"), 
    SCALE("scale"), 
    HEALTH("health"), 
    HURT_TIME("hurt_time"), 
    IDLE_TIME("idle_time"), 
    MAX_HEALTH("max_health"), 
    MOVE_FORWARD("move_forward"), 
    MOVE_STRAFING("move_strafing"), 
    PARTIAL_TICKS("partial_ticks"), 
    POS_X("pos_x"), 
    POS_Y("pos_y"), 
    POS_Z("pos_z"), 
    REVENGE_TIME("revenge_time"), 
    SWING_PROGRESS("swing_progress");
    
    private String name;
    private RenderManager renderManager;
    private static final RenderEntityParameterFloat[] VALUES;
    
    private RenderEntityParameterFloat(final String name) {
        this.name = name;
        this.renderManager = Minecraft.getMinecraft().getRenderManager();
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public float eval() {
        final Render render = this.renderManager.renderRender;
        if (render == null) {
            return 0.0f;
        }
        Label_0245: {
            if (render instanceof RenderLivingBase) {
                final RenderLivingBase renderlivingbase = (RenderLivingBase)render;
                switch (this) {
                    case LIMB_SWING: {
                        return renderlivingbase.renderLimbSwing;
                    }
                    case LIMB_SWING_SPEED: {
                        return renderlivingbase.renderLimbSwingAmount;
                    }
                    case AGE: {
                        return renderlivingbase.renderAgeInTicks;
                    }
                    case HEAD_YAW: {
                        return renderlivingbase.renderHeadYaw;
                    }
                    case HEAD_PITCH: {
                        return renderlivingbase.renderHeadPitch;
                    }
                    case SCALE: {
                        return renderlivingbase.renderScaleFactor;
                    }
                    default: {
                        final EntityLivingBase entitylivingbase = renderlivingbase.renderEntity;
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
                                return (float)entitylivingbase.getIdleTime();
                            }
                            case MAX_HEALTH: {
                                return entitylivingbase.getMaxHealth();
                            }
                            case MOVE_FORWARD: {
                                return entitylivingbase.moveVertical;
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
                                return entitylivingbase.getSwingProgress(renderlivingbase.renderPartialTicks);
                            }
                            default: {
                                break Label_0245;
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
    
    static {
        VALUES = values();
    }
}
