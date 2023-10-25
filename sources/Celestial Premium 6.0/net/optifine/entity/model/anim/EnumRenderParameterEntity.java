/*
 * Decompiled with CFR 0.150.
 */
package net.optifine.entity.model.anim;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.optifine.entity.model.anim.IExpression;

public enum EnumRenderParameterEntity implements IExpression
{
    LIMB_SWING("limb_swing"),
    LIMB_SWING_SPEED("limb_speed"),
    AGE("age"),
    HEAD_YAW("head_yaw"),
    HEAD_PITCH("head_pitch"),
    SCALE("scale");

    private String name;
    private RenderManager renderManager;
    private static final EnumRenderParameterEntity[] VALUES;

    private EnumRenderParameterEntity(String name) {
        this.name = name;
        this.renderManager = Minecraft.getMinecraft().getRenderManager();
    }

    public String getName() {
        return this.name;
    }

    @Override
    public float eval() {
        Render render = this.renderManager.renderRender;
        if (render == null) {
            return 0.0f;
        }
        if (render instanceof RenderLivingBase) {
            RenderLivingBase renderlivingbase = (RenderLivingBase)render;
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
            }
        }
        return 0.0f;
    }

    public static EnumRenderParameterEntity parse(String str) {
        if (str == null) {
            return null;
        }
        for (int i = 0; i < VALUES.length; ++i) {
            EnumRenderParameterEntity enumrenderparameterentity = VALUES[i];
            if (!enumrenderparameterentity.getName().equals(str)) continue;
            return enumrenderparameterentity;
        }
        return null;
    }

    static {
        VALUES = EnumRenderParameterEntity.values();
    }
}

