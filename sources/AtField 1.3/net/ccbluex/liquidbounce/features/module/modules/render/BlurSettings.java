/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.shader.Framebuffer
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.BlurEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.BloomUtil;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.blur.GaussianBlur;
import net.ccbluex.liquidbounce.utils.render.blur.KawaseBlur;
import net.ccbluex.liquidbounce.utils.render.blur.StencilUtil;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.shader.Framebuffer;

@ModuleInfo(name="BlurSettings", description="Shader effect.", category=ModuleCategory.RENDER)
public class BlurSettings
extends Module {
    public IntegerValue shadowRadius;
    public ListValue blurMode;
    private final IntegerValue offset;
    private final IntegerValue iterations = new IntegerValue("BlurIterations", 4, 1, 15);
    public IntegerValue blurRadius;
    public IntegerValue shadowOffset;
    public Framebuffer bloomFramebuffer;
    public BoolValue blurValue;
    public BoolValue shadowValue;

    public void drawBlurShadow() {
        if (((Boolean)this.blurValue.get()).booleanValue()) {
            StencilUtil.initStencilToWrite();
            LiquidBounce.eventManager.callEvent(new BlurEvent(false));
            StencilUtil.readStencilBuffer(1);
            switch ((String)this.blurMode.get()) {
                case "Gaussian": {
                    GaussianBlur.renderBlur(((Integer)this.blurRadius.getValue()).floatValue());
                    break;
                }
                case "Kawase": {
                    KawaseBlur.renderBlur((Integer)this.iterations.getValue(), (Integer)this.offset.getValue());
                }
            }
            StencilUtil.uninitStencilBuffer();
        }
        if (((Boolean)this.shadowValue.get()).booleanValue()) {
            this.bloomFramebuffer = RenderUtils.createFrameBuffer(this.bloomFramebuffer);
            this.bloomFramebuffer.func_147614_f();
            this.bloomFramebuffer.func_147610_a(true);
            LiquidBounce.eventManager.callEvent(new BlurEvent(true));
            this.bloomFramebuffer.func_147609_e();
            BloomUtil.renderBlur(this.bloomFramebuffer.field_147617_g, (Integer)this.shadowRadius.get(), (Integer)this.shadowOffset.get());
        }
    }

    public BlurSettings() {
        this.offset = new IntegerValue("BlurOffset", 3, 1, 20);
        this.blurValue = new BoolValue("Blur", false);
        this.blurMode = new ListValue("BlurMode", new String[]{"Kawase", "Gaussian"}, "Gaussian");
        this.blurRadius = new IntegerValue("BlurRadius", 6, 1, 60);
        this.shadowValue = new BoolValue("ShadowOn", false);
        this.shadowRadius = new IntegerValue("ShadowRadius", 6, 1, 60);
        this.shadowOffset = new IntegerValue("ShadowOffset", 2, 1, 15);
        this.bloomFramebuffer = new Framebuffer(1, 1, false);
    }
}

