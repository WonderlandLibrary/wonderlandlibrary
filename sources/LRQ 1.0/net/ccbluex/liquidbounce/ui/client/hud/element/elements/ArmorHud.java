/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.hud.element.elements;

import java.awt.Color;
import jx.utils.BlurUtils;
import jx.utils.novoline.impl.Fonts;
import net.ccbluex.liquidbounce.ui.client.hud.element.Border;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.utils.render.AnimationHelper;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

@ElementInfo(name="ArmorHud")
public class ArmorHud
extends Element {
    public final ListValue shadowValue = new ListValue("Shadow", new String[]{"None", "Basic", "Thick"}, "None");
    public BoolValue BlurValue = new BoolValue("blur", false);
    public FloatValue blurStrength = new FloatValue("Blur-Strength", 1.0f, 0.01f, 40.0f);
    public FloatValue indx = new FloatValue("noting", 1.0E-7f, 0.0f, 4.0E-6f);
    public float animWidth;
    public float animatedCircleEnd;
    float Width = 41.0f;
    float Height = 42.0f;
    int x = 0;
    int y = 0;
    public Float x2 = (Float)this.indx.get();
    Double renderX;
    Double renderY;

    @Override
    public Border drawElement() {
        float armorValue = ArmorHud.mc2.field_71439_g.func_70658_aO();
        double armorPercentage = armorValue / 20.0f;
        armorPercentage = MathHelper.func_151237_a((double)armorPercentage, (double)0.0, (double)1.0);
        double newWidth = 51.0 * armorPercentage;
        this.animWidth = (float)AnimationHelper.animate(newWidth, this.animWidth, 0.0229999852180481);
        RenderUtils.drawSmoothRect(this.x, this.y, (float)this.x + 40.0f, (float)this.y + 43.0f, new Color(0, 0, 0, 100).getRGB());
        switch ((String)this.shadowValue.get()) {
            case "Basic": {
                RenderUtils.drawShadow((float)this.x - 0.5f, (float)this.y - 0.5f, (float)this.x + 40.0f + 1.0f, (float)this.y + 43.0f);
                break;
            }
            case "Thick": {
                RenderUtils.drawShadow((float)this.x - 0.5f, (float)this.y - 0.5f, (float)this.x + 40.0f + 1.0f, (float)this.y + 43.0f);
                RenderUtils.drawShadow((float)this.x - 0.5f, (float)this.y - 0.5f, (float)this.x + 40.0f + 1.0f, (float)this.y + 43.0f);
            }
        }
        Fonts.SFBOLD.SFBOLD_16.SFBOLD_16.drawCenteredString("Armor", this.x + 20, this.y + 14 - 10, -1);
        RenderUtils.drawCircle(this.x + 15 + 5, (double)this.y + 23.5, 11.5, -5.0f, 360.0f, Color.DARK_GRAY.darker().getRGB(), 5.5f);
        float coef = this.animWidth / 100.0f;
        double scoef = (float)ArmorHud.mc2.field_71439_g.func_70658_aO() / 20.0f * 100.0f;
        this.animatedCircleEnd = coef * 360.0f;
        RenderUtils.drawCircle(this.x + 15 + 5, (double)this.y + 23.5, 11.5, -5.0f, this.animatedCircleEnd * 2.0f + this.x2.floatValue(), new Color(255, 255, 255).getRGB(), 5.5f);
        Fonts.SFBOLD.SFBOLD_15.SFBOLD_15.drawCenteredString(Math.round(scoef) + "%", this.x + 20, this.y + 22, -1);
        if (((Boolean)this.BlurValue.get()).booleanValue()) {
            GL11.glTranslated((double)(-this.renderX.doubleValue()), (double)(-this.renderY.doubleValue()), (double)0.0);
            GL11.glPushMatrix();
            BlurUtils.blurArea(this.x, this.y, (float)this.x + 40.0f, (float)this.y + 43.0f, ((Float)this.blurStrength.get()).floatValue());
            GL11.glPopMatrix();
            GL11.glTranslated((double)this.renderX, (double)this.renderY, (double)0.0);
        }
        return new Border(0.0f, 0.0f, this.Width, this.Height);
    }
}

