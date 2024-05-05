/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.resources.I18n
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package me.report.liquidware.modules.render;

import java.util.HashMap;
import java.util.Map;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.render.Colors;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils3;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="HanabiEffects", description="lol", category=ModuleCategory.RENDER)
public class HanabiEffects
extends Module {
    public static final FloatValue bordRad = new FloatValue("BorderRadius", 0.0f, 0.0f, 8.0f);
    public static final IntegerValue r = new IntegerValue("R", 0, 0, 255);
    public static final IntegerValue g = new IntegerValue("G", 160, 0, 255);
    public static final IntegerValue b = new IntegerValue("B", 255, 0, 255);
    Map<Potion, Double> timerMap = new HashMap<Potion, Double>();
    private int x;

    public HanabiEffects() {
        this.setState(false);
    }

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        ScaledResolution sr = new ScaledResolution(mc);
        float width1 = sr.func_78326_a();
        float height1 = sr.func_78328_b();
        this.renderPotionStatus((int)width1, (int)height1);
        if (HanabiEffects.mc.field_71462_r instanceof GuiHudDesigner) {
            return;
        }
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (event.getEventState() == EventState.POST) {
            double xDist = HanabiEffects.mc.field_71439_g.field_70165_t - HanabiEffects.mc.field_71439_g.field_70169_q;
            double d = HanabiEffects.mc.field_71439_g.field_70161_v - HanabiEffects.mc.field_71439_g.field_70166_s;
        }
    }

    public void renderPotionStatus(int width, int height) {
        this.x = 0;
        ScaledResolution sr = new ScaledResolution(mc);
        for (PotionEffect effect : HanabiEffects.mc.field_71439_g.func_70651_bq()) {
            Potion potion = Potion.field_76425_a[effect.func_76456_a()];
            String PType = I18n.func_135052_a((String)potion.func_76393_a(), (Object[])new Object[0]);
            int minutes = -1;
            int seconds = -2;
            try {
                minutes = Integer.parseInt(Potion.func_76389_a((PotionEffect)effect).split(":")[0]);
                seconds = Integer.parseInt(Potion.func_76389_a((PotionEffect)effect).split(":")[1]);
            }
            catch (Exception ex) {
                minutes = 0;
                seconds = 0;
            }
            double total = minutes * 60 + seconds;
            if (!this.timerMap.containsKey(potion)) {
                this.timerMap.put(potion, total);
            }
            if (this.timerMap.get(potion) == 0.0 || total > this.timerMap.get(potion)) {
                this.timerMap.replace(potion, total);
            }
            switch (effect.func_76458_c()) {
                case 0: {
                    PType = PType + " I";
                    break;
                }
                case 1: {
                    PType = PType + " II";
                    break;
                }
                case 2: {
                    PType = PType + " III";
                    break;
                }
                case 3: {
                    PType = PType + " IV";
                    break;
                }
                case 4: {
                    PType = PType + " V";
                    break;
                }
                case 5: {
                    PType = PType + " VI";
                    break;
                }
                case 6: {
                    PType = PType + " VII";
                    break;
                }
                case 7: {
                    PType = PType + " VIII";
                    break;
                }
                case 8: {
                    PType = PType + " IX";
                    break;
                }
                case 9: {
                    PType = PType + " X";
                    break;
                }
                case 10: {
                    PType = PType + " X+";
                    break;
                }
            }
            int color = Colors.WHITE.c;
            if (effect.func_76459_b() < 600 && effect.func_76459_b() > 300) {
                color = Colors.YELLOW.c;
            } else if (effect.func_76459_b() < 300) {
                color = Colors.RED.c;
            } else if (effect.func_76459_b() > 600) {
                color = Colors.WHITE.c;
            }
            int x1 = (int)((float)(width - 6) * 1.33f);
            int y1 = (int)((float)(height - 52 - HanabiEffects.mc.field_71466_p.field_78288_b + this.x + 5) * 1.33f);
            RenderUtils.drawRoundedRect(width - 120, height - 60 + this.x, width - 10, height - 30 + this.x, ((Float)bordRad.get()).floatValue(), RenderUtils3.reAlpha(Colors.BLACK.c, 0.41f));
            if (potion.func_76400_d()) {
                GlStateManager.func_179094_E();
                GL11.glDisable((int)2929);
                GL11.glEnable((int)3042);
                GL11.glDepthMask((boolean)false);
                OpenGlHelper.func_148821_a((int)770, (int)771, (int)1, (int)0);
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                int index = potion.func_76392_e();
                ResourceLocation location = new ResourceLocation("textures/gui/container/inventory.png");
                mc.func_110434_K().func_110577_a(location);
                GlStateManager.func_179139_a((double)0.75, (double)0.75, (double)0.75);
                HanabiEffects.mc.field_71456_v.func_73729_b(x1 - 138, y1 + 8, 0 + index % 8 * 18, 198 + index / 8 * 18, 18, 18);
                GL11.glDepthMask((boolean)true);
                GL11.glDisable((int)3042);
                GL11.glEnable((int)2929);
                GlStateManager.func_179121_F();
            }
            int y = height - HanabiEffects.mc.field_71466_p.field_78288_b + this.x - 38;
            Fonts.fontMiSansNormal35.drawString(PType.replaceAll("\u00a7.", ""), (float)width - 91.0f, y - HanabiEffects.mc.field_71466_p.field_78288_b + 1, potion.func_76401_j());
            Fonts.fontMiSansNormal35.drawString(Potion.func_76389_a((PotionEffect)effect).replaceAll("\u00a7.", ""), (float)width - 91.0f, y + 4, RenderUtils3.reAlpha(-1, 0.8f));
            this.x -= 35;
        }
    }
}

