/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.combat.AntiBot;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NoteSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.render.ColorUtil;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Chams", description = "Allows you to view players through walls with a certain color and alpha", category = Category.RENDER)
public final class Chams extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Normal", "Normal", "CSGO");
    public static boolean enabled;

    private final NoteSetting rise6 = new NoteSetting("In Rise 6 there will be a color picker for this shit", this);
    private final BooleanSetting hand = new BooleanSetting("Hands", this, false);
    private final BooleanSetting rainbow = new BooleanSetting("Rainbow", this, false);
    // I hate doing this, but it's the only way I guess rip bozo.
    private final NumberSetting red = new NumberSetting("Red", this, 255, 0, 255, 1);
    private final NumberSetting green = new NumberSetting("Green", this, 0, 0, 255, 1);
    private final NumberSetting blue = new NumberSetting("Blue", this, 0, 0, 255, 1);
    private final NumberSetting alpha = new NumberSetting("Alpha", this, 100, 0, 255, 1);

    private final NoteSetting hands = new NoteSetting("Changes Color of Your Hand", this);
    private final NumberSetting redHand = new NumberSetting("Red Hand", this, 255, 0, 255, 1);
    private final NumberSetting greenHand = new NumberSetting("Green Hand", this, 0, 0, 255, 1);
    private final NumberSetting blueHand = new NumberSetting("Blue Hand", this, 0, 0, 255, 1);
    private final NumberSetting alphaHand = new NumberSetting("Alpha Hand", this, 100, 0, 255, 1);

    private final NoteSetting visible = new NoteSetting("Changes Color When Visible", this);
    private final NumberSetting redVisible = new NumberSetting("Red Visible", this, 255, 0, 255, 1);
    private final NumberSetting greenVisible = new NumberSetting("Green Visible", this, 0, 0, 255, 1);
    private final NumberSetting blueVisible = new NumberSetting("Blue Visible", this, 0, 0, 255, 1);
    private final NumberSetting alphaVisible = new NumberSetting("Alpha Visible", this, 100, 0, 255, 1);

    private final NoteSetting hidden = new NoteSetting("Changes Color Behind Walls", this);
    private final NumberSetting redHidden = new NumberSetting("Red Hidden", this, 255, 0, 255, 1);
    private final NumberSetting greenHidden = new NumberSetting("Green Hidden", this, 0, 0, 255, 1);
    private final NumberSetting blueHidden = new NumberSetting("Blue Hidden", this, 0, 0, 255, 1);
    private final NumberSetting alphaHidden = new NumberSetting("Alpha Hidden", this, 100, 0, 255, 1);

    @Override
    public void onUpdateAlwaysInGui() {
        // Hides settings when specific modes/things are on.
        red.hidden = !mode.is("Normal");
        green.hidden = !mode.is("Normal");
        blue.hidden = !mode.is("Normal");
        alpha.hidden = !mode.is("Normal");

        rise6.hidden = !mode.is("CSGO");
        hand.hidden = !mode.is("CSGO");
        hands.hidden = !(mode.is("CSGO") && hand.isEnabled()) || (rainbow.isEnabled());
        redHand.hidden = !(mode.is("CSGO") && hand.isEnabled()) || (rainbow.isEnabled());
        greenHand.hidden = !(mode.is("CSGO") && hand.isEnabled()) || (rainbow.isEnabled());
        blueHand.hidden = !(mode.is("CSGO") && hand.isEnabled()) || (rainbow.isEnabled());
        alphaHand.hidden = !(mode.is("CSGO") && hand.isEnabled());

        visible.hidden = !(mode.is("CSGO")) || (rainbow.isEnabled());
        redVisible.hidden = !(mode.is("CSGO")) || (rainbow.isEnabled());
        greenVisible.hidden =  !(mode.is("CSGO")) || (rainbow.isEnabled());
        blueVisible.hidden =  !(mode.is("CSGO")) || (rainbow.isEnabled());
        alphaVisible.hidden =  !(mode.is("CSGO"));

        hidden.hidden = !(mode.is("CSGO")) || (rainbow.isEnabled());
        redHidden.hidden =  !(mode.is("CSGO")) || (rainbow.isEnabled());
        greenHidden.hidden = !(mode.is("CSGO")) || (rainbow.isEnabled());
        blueHidden.hidden =  !(mode.is("CSGO")) || (rainbow.isEnabled());
        alphaHidden.hidden =  !(mode.is("CSGO"));
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        // Uses RenderPlayer.java line 55.
        // Uses RendererLivingEntity.java line 627.
        if (mode.is("Normal")) {
            if (rainbow.enabled) {
                final int rgb = ColorUtil.getRainbow();
                RendererLivingEntity.chamsRed = ((rgb >> 16) & 0xFF) / 255.0F;
                RendererLivingEntity.chamsGreen = ((rgb >> 8) & 0xFF) / 255.0F;
                RendererLivingEntity.chamsBlue = (rgb & 0xFF) / 255.0F;
            } else {
                RendererLivingEntity.chamsRed = (float) red.getValue() / 255;
                RendererLivingEntity.chamsGreen = (float) green.getValue() / 255;
                RendererLivingEntity.chamsBlue = (float) blue.getValue() / 255;
            }
            RendererLivingEntity.chamsAlpha = (float) alpha.getValue() / 255;
        }

        // Hands.
        // Uses ItemRenderer.java line 833.
        if (mode.is("CSGO") && hand.isEnabled()) {
            if (rainbow.enabled) {
                final int rgb = ColorUtil.getRainbow();
                ItemRenderer.chamsRed = ((rgb >> 16) & 0xFF);
                ItemRenderer.chamsGreen = ((rgb >> 8) & 0xFF);
                ItemRenderer.chamsBlue = (rgb & 0xFF);
            } else {
                ItemRenderer.chamsRed = (float) redHand.getValue();
                ItemRenderer.chamsGreen = (float) greenHand.getValue();
                ItemRenderer.chamsBlue = (float) blueHand.getValue();
            }
            ItemRenderer.chamsAlpha = (float) alphaHand.getValue();
        }

        // Visible.
        // Uses RendererLivingEntity.java line 532.
        if (mode.is("CSGO")) {
            if (rainbow.enabled) {
                final int rgb = ColorUtil.getRainbow();
                RendererLivingEntity.chamsRed = ((rgb >> 16) & 0xFF);
                RendererLivingEntity.chamsGreen = ((rgb >> 8) & 0xFF);
                RendererLivingEntity.chamsBlue = (rgb & 0xFF);
            } else {
                RendererLivingEntity.chamsRed = (int) redVisible.getValue();
                RendererLivingEntity.chamsGreen = (int) greenVisible.getValue();
                RendererLivingEntity.chamsBlue = (int) blueVisible.getValue();
            }
            RendererLivingEntity.chamsAlpha = (int) alphaVisible.getValue();
        }

        // Hidden.
        // Uses RendererLivingEntity.java line 514.
        if (mode.is("CSGO")) {
            if (rainbow.enabled) {
                final int rgb = ColorUtil.getRainbow();
                RendererLivingEntity.chamsRed2 = ((rgb >> 16) & 0xFF);
                RendererLivingEntity.chamsGreen2 = ((rgb >> 8) & 0xFF);
                RendererLivingEntity.chamsBlue2 = (rgb & 0xFF);
            } else {
                RendererLivingEntity.chamsRed2 = (int) redHidden.getValue();
                RendererLivingEntity.chamsGreen2 = (int) greenHidden.getValue();
                RendererLivingEntity.chamsBlue2 = (int) blueHidden.getValue();
            }
            RendererLivingEntity.chamsAlpha2 = (int) alphaHidden.getValue();
        }
    }

    public static boolean entity(EntityLivingBase entity) {
        return entity instanceof EntityPlayer && !AntiBot.bots.contains(entity) && entity.isEntityAlive() && (!entity.isInvisible());
    }

    @Override
    protected void onEnable() {
        enabled = true;
    }

    @Override
    protected void onDisable() {
        enabled = false;
    }
}
