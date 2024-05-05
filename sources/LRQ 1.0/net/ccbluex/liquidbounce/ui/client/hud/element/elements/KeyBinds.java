/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Unit
 *  kotlin.jvm.functions.Function0
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import jx.utils.ShadowUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="KeyBinds")
public final class KeyBinds
extends Element {
    private final BoolValue onlyState = new BoolValue("OnlyModuleState", false);
    private final FloatValue radiusValue = new FloatValue("Radius", 4.25f, 0.0f, 10.0f);
    private final FloatValue shadowValue = new FloatValue("shadow-Value", 10.0f, 0.0f, 20.0f);
    private float anmitY;

    public final BoolValue getOnlyState() {
        return this.onlyState;
    }

    @Override
    public Border drawElement() {
        int y2 = 0;
        this.anmitY = (float)RenderUtils.getAnimationState2(this.anmitY, 17 + this.getmoduley(), 200.0);
        RenderUtils.drawRoundedRect(0.0f, 0.0f, 114.0f, this.anmitY, ((Number)this.radiusValue.get()).floatValue(), new Color(32, 30, 30).getRGB());
        GL11.glTranslated((double)(-this.getRenderX()), (double)(-this.getRenderY()), (double)0.0);
        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPushMatrix();
        ShadowUtils.INSTANCE.shadow(((Number)this.shadowValue.get()).floatValue(), (Function0<Unit>)((Function0)new Function0<Unit>(this){
            final /* synthetic */ KeyBinds this$0;

            public final void invoke() {
                GL11.glPushMatrix();
                GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                GL11.glScalef((float)this.this$0.getScale(), (float)this.this$0.getScale(), (float)this.this$0.getScale());
                RenderUtils.originalRoundedRect(0.0f, 0.0f, 114.0f, KeyBinds.access$getAnmitY$p(this.this$0), 0.0f, new Color(0, 0, 0).getRGB());
                GL11.glPopMatrix();
            }
            {
                this.this$0 = keyBinds;
                super(0);
            }
        }), (Function0<Unit>)((Function0)new Function0<Unit>(this){
            final /* synthetic */ KeyBinds this$0;

            public final void invoke() {
                GL11.glPushMatrix();
                GL11.glTranslated((double)this.this$0.getRenderX(), (double)this.this$0.getRenderY(), (double)0.0);
                GL11.glScalef((float)this.this$0.getScale(), (float)this.this$0.getScale(), (float)this.this$0.getScale());
                GlStateManager.func_179147_l();
                GlStateManager.func_179090_x();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                RenderUtils.fastRoundedRect(0.0f, 0.0f, 114.0f, KeyBinds.access$getAnmitY$p(this.this$0), 0.0f);
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
                GL11.glPopMatrix();
            }
            {
                this.this$0 = keyBinds;
                super(0);
            }
        }));
        GL11.glPopMatrix();
        GL11.glScalef((float)this.getScale(), (float)this.getScale(), (float)this.getScale());
        GL11.glTranslated((double)this.getRenderX(), (double)this.getRenderY(), (double)0.0);
        float fwidth = 57.0f - (float)Fonts.tenacitybold35.getStringWidth("KeyBinds") / 2.0f;
        Fonts.tenacitybold35.drawString("KeyBinds", fwidth, 4.5f, -1, true);
        for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
            if (module.getKeyBind() == 0 || ((Boolean)this.onlyState.get()).booleanValue() && !module.getState()) continue;
            Fonts.tenacitybold35.drawString(module.getName(), 3.0f, (float)y2 + 19.0f, -1, true);
            Fonts.tenacitybold35.drawString(module.getState() ? "[True]" : "[False]", 108 - Fonts.tenacitybold35.getStringWidth(module.getState() ? "[True]" : "[False]"), (float)y2 + 21.0f, module.getState() ? new Color(255, 255, 255).getRGB() : new Color(100, 100, 100).getRGB(), true);
            y2 += 12;
        }
        return new Border(0.0f, 0.0f, 114.0f, 17 + this.getmoduley());
    }

    public final int getmoduley() {
        int y = 0;
        for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
            if (module.getKeyBind() == 0 || ((Boolean)this.onlyState.get()).booleanValue() && !module.getState()) continue;
            y += 12;
        }
        return y;
    }

    public KeyBinds() {
        super(0.0, 0.0, 0.0f, null, 15, null);
    }

    public static final /* synthetic */ float access$getAnmitY$p(KeyBinds $this) {
        return $this.anmitY;
    }

    public static final /* synthetic */ void access$setAnmitY$p(KeyBinds $this, float f) {
        $this.anmitY = f;
    }
}

