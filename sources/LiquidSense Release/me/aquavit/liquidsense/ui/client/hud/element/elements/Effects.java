package me.aquavit.liquidsense.ui.client.hud.element.elements;

import me.aquavit.liquidsense.ui.client.hud.element.Border;
import me.aquavit.liquidsense.ui.client.hud.element.Element;
import me.aquavit.liquidsense.ui.client.hud.element.ElementInfo;
import me.aquavit.liquidsense.ui.client.hud.element.Side;
import me.aquavit.liquidsense.ui.font.AWTFontRenderer;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FontValue;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ElementInfo(name = "Effects")
public class Effects extends Element {

    private FontValue fontValue = new FontValue("Font", Fonts.font18);
    private BoolValue shadow = new BoolValue("Shadow", true);

    public Effects(){
        super(2,10,1f,new Side(Side.Horizontal.RIGHT, Side.Vertical.DOWN));
    }

    @Override
    public Border drawElement() {
        float y = 0F;
        float width = 0F;

        FontRenderer fontRenderer = fontValue.get();

        AWTFontRenderer.Companion.setAssumeNonVolatile(true);

        for (PotionEffect effect : mc.thePlayer.getActivePotionEffects()) {

            Potion potion = Potion.potionTypes[effect.getPotionID()];

            String number = effect.getAmplifier() == 1 ? "II"
                    : (effect.getAmplifier() == 2 ? "III"
                    : (effect.getAmplifier() == 3 ? "IV"
                    : (effect.getAmplifier() == 4 ? "V"
                    : (effect.getAmplifier() == 5 ? "VI"
                    : (effect.getAmplifier() == 6 ? "VII"
                    : (effect.getAmplifier() == 7 ? "VIII"
                    : (effect.getAmplifier() == 8 ? "IX"
                    : (effect.getAmplifier() == 9 ? "X"
                    : (effect.getAmplifier() > 10 ? "X+"
                    : "I")))))))));

            String name = I18n.format(potion.getName())+" "+number+"§f: §7"+Potion.getDurationString(effect);
            float stringWidth = fontRenderer.getStringWidth(name);

            if (width < stringWidth) width = stringWidth;

            fontRenderer.drawString(name, -stringWidth, y, potion.getLiquidColor(), shadow.get());
            y -= fontRenderer.FONT_HEIGHT;
        }

        AWTFontRenderer.Companion.setAssumeNonVolatile(false);

        if (width == 0F) width = 40F;

        if (y == 0F) y = -10F;

        return new Border(2.0f, fontRenderer.FONT_HEIGHT, -width - 2.0f, y + (float)fontRenderer.FONT_HEIGHT - 2.0f);
    }
}
