// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import java.util.Iterator;
import java.util.List;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.util.ColorUtils;
import com.elementars.eclient.module.core.Global;
import com.elementars.eclient.Xulu;
import java.awt.Color;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.elementars.eclient.util.PotionUtil;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Module;
import dev.xulu.settings.Value;
import java.text.DecimalFormat;
import com.elementars.eclient.guirewrite.Element;

public class Potions extends Element
{
    DecimalFormat df;
    Value<Boolean> pc;
    Value<Integer> red;
    Value<Integer> green;
    Value<Integer> blue;
    Value<Boolean> onlysw;
    Value<String> align;
    Value<String> order;
    
    public Potions() {
        super("Potions");
        this.df = new DecimalFormat("00");
        this.pc = this.register(new Value<Boolean>("Use Potion Color", this, true));
        this.red = this.register(new Value<Integer>("Red", this, 0, 0, 255));
        this.green = this.register(new Value<Integer>("Green", this, 0, 0, 255));
        this.blue = this.register(new Value<Integer>("Blue", this, 0, 0, 255));
        this.onlysw = this.register(new Value<Boolean>("Only Str & Weak", this, false));
        this.align = this.register(new Value<String>("Align", this, "Left", new ArrayList<String>(Arrays.asList("Left", "Right"))));
        this.order = this.register(new Value<String>("Order", this, "Up", new ArrayList<String>(Arrays.asList("Up", "Down"))));
    }
    
    @Override
    public void onEnable() {
        this.width = 50.0;
        this.height = 50.0;
    }
    
    @Override
    public void onRender() {
        float yCount = (float)this.y;
        final float right = (float)this.x;
        if (Potions.mc.player == null) {
            return;
        }
        final List<String> potions = new ArrayList<String>();
        if (this.order.getValue().equalsIgnoreCase("Down")) {
            yCount += (float)(this.height - 10.0);
        }
        for (final PotionEffect potionEffect : Potions.mc.player.getActivePotionEffects()) {
            if (this.onlysw.getValue() && potionEffect.getPotion() != MobEffects.STRENGTH && potionEffect.getPotion() != MobEffects.WEAKNESS) {
                continue;
            }
            final String text = PotionUtil.getPotionName(potionEffect.getPotion()) + " " + ((potionEffect.getAmplifier() + 1 != 1) ? (potionEffect.getAmplifier() + 1 + " ") : "") + ChatFormatting.GRAY + ((this.getTime(potionEffect.getDuration() / 20).length() == 1) ? "0" : "") + this.getTime(potionEffect.getDuration() / 20);
            int color = new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB();
            if (this.pc.getValue()) {
                color = potionEffect.getPotion().getLiquidColor();
            }
            if (Xulu.CustomFont) {
                Xulu.cFontRenderer.drawStringWithShadow(text, (this.align.getValue().equalsIgnoreCase("Right") ? (right - Xulu.cFontRenderer.getStringWidth(text) + this.getFrame().width) : right) + 1.0, yCount + 1.0f, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
            }
            else {
                Wrapper.getMinecraft().fontRenderer.drawStringWithShadow(text, (float)(this.align.getValue().equalsIgnoreCase("Right") ? (right - Wrapper.getMinecraft().fontRenderer.getStringWidth(text) + this.getFrame().width) : right) + 1.0f, yCount + 1.0f, ColorUtils.changeAlpha(color, Global.hudAlpha.getValue()));
            }
            potions.add(text);
            yCount += (this.order.getValue().equalsIgnoreCase("Up") ? 10 : -10);
        }
    }
    
    public String getTime(int duration) {
        int min;
        for (min = 0; duration > 59; duration -= 60, ++min) {}
        return min + ":" + this.df.format(duration);
    }
}
