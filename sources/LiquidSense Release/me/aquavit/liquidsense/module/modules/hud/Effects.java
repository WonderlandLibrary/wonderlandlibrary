package me.aquavit.liquidsense.module.modules.hud;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.ui.font.AWTFontRenderer;
import me.aquavit.liquidsense.ui.font.Fonts;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FontValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

@ModuleInfo(name = "Effects", description = "Drawing 2D Effects", category = ModuleCategory.HUD, array = false)
public class Effects extends Module {

    public Effects(double x, double y){
        this.setDefaultx(x);
        this.setDefaulty(y);

    }

    public Effects(){
        this(4,  9);
    }

    private final ListValue horizontal = new ListValue("Horizontal", new String[] {"Left", "Middle", "Right"}, "Right") {
        @Override
        protected void onChanged(String oldValue, String newValue) {
            switch (newValue.toLowerCase()) {
                case "left":
                    setRenderx(hudx);
                    break;
                case "middle":
                    setRenderx((float) new ScaledResolution(mc).getScaledWidth() / 2 - hudx);
                    break;
                case "right":
                    setRenderx(new ScaledResolution(mc).getScaledWidth() - hudx);
                    break;
            }
        }
    };
    private final ListValue vertical = new ListValue("Vertical", new String[] {"Up", "Middle", "Down"}, "Down") {
        @Override
        protected void onChanged(String oldValue, String newValue) {
            switch (newValue.toLowerCase()) {
                case "up":
                    setRendery(hudy);
                    break;
                case "middle":
                    setRendery((float) new ScaledResolution(mc).getScaledHeight() / 2 - hudy);
                    break;
                case "down":
                    setRendery(new ScaledResolution(mc).getScaledHeight() - hudy);
                    break;
            }
        }
    };
    int hudx,hudy;

    private FontValue fontValue = new FontValue("Font", Fonts.font18);
    private BoolValue shadow = new BoolValue("Shadow", true);

    @EventTarget
    public void onRender2D(final Render2DEvent event) {
        isLeft = horizontal.get().equalsIgnoreCase("Left");
        isUp = vertical.get().equalsIgnoreCase("Up");
        hudx = (int) getHUDX(horizontal.get().equalsIgnoreCase("Left"),
                horizontal.get().equalsIgnoreCase("Middle"),
                horizontal.get().equalsIgnoreCase("Right"));
        hudy = (int) getHUDY(vertical.get().equalsIgnoreCase("Up"),
                vertical.get().equalsIgnoreCase("Middle"),
                vertical.get().equalsIgnoreCase("Down"));

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

            fontRenderer.drawString(name, -stringWidth + hudx, y + hudy, potion.getLiquidColor(), shadow.get());
            y -= fontRenderer.FONT_HEIGHT;
        }

        AWTFontRenderer.Companion.setAssumeNonVolatile(false);

        if (width == 0F) width = 40F;

        if (y == 0F) y = -10F;

        drawBorder(2.0f + hudx, fontRenderer.FONT_HEIGHT + hudy, -width - 2.0f, y - 2.0f);
    }
}
