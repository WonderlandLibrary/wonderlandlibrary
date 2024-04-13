package dev.tenacity.module.impl.render;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.module.impl.combat.KillAuraModule;
import dev.tenacity.setting.impl.ColorSetting;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.util.render.ColorUtil;
import dev.tenacity.util.render.RoundedUtil;
import dev.tenacity.util.render.Theme;
import dev.tenacity.util.render.font.CustomFont;
import dev.tenacity.util.render.font.FontUtil;
import dev.tenacity.util.tuples.Pair;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.text.DecimalFormat;

import static dev.tenacity.module.impl.render.HUDModule.blurtint;
import static dev.tenacity.module.impl.render.HUDModule.opacity;

public class TargetInfoModule extends Module {
    private NumberSetting opacitys = new NumberSetting("Opacity", 0.3, 0, 1, 0.1);
    private ColorSetting pimcolors;

    {
        {
            pimcolors = new ColorSetting("Background Tint", ColorUtil.applyOpacity(new Color(19, 19, 19), 0.7f));
        }
    }
    public TargetInfoModule() {
        super("TargetInfo", "Target HUD with different name lmao", ModuleCategory.RENDER);
        //   opacitys.addParent(mode, modeSetting -> mode.isMode("Sync"));
        //   blurtint.addParent(mode, modeSetting -> mode.isMode("Sync"));
        //   pimcolors.addParent(mode, modeSetting -> mode.isMode("Separate"));
        //   opacity.addParent(mode, modeSetting -> mode.isMode("Separate"));
        //   initializeSettings(mode);
        //   initializeSettings(blurtint);
        initializeSettings(pimcolors, opacitys);
    }

    //   private ModeSetting mode = new ModeSetting("Mode", "Sync", "Separate");

    private final IEventListener<Render2DEvent> onRender2DEvent = event -> {
        EntityLivingBase target = KillAuraModule.getCurrentTarget();
        Pair<Color, Color> clientColors = Theme.getThemeColors(HUDModule.theme.getCurrentMode());
        if (target != null) {
            CustomFont name = FontUtil.getFont("OpenSans-Medium", 22);
            CustomFont health = FontUtil.getFont("OpenSans-Medium", 16);
            float healthPercentage = (target.getHealth() + target.getAbsorptionAmount()) / (target.getMaxHealth() + target.getAbsorptionAmount());
            RoundedUtil.drawRound((float) event.getScaledResolution().getScaledWidth() / 2 - 37.5f, (float) event.getScaledResolution().getScaledHeight() / 2 + 10, 75f, 30f, 8, ColorUtil.applyOpacity(new Color(pimcolors.getColor().getRGB()), (float) opacitys.getCurrentValue()));

            if(target instanceof EntityPlayer) {
                name.drawString(((EntityPlayer) target).getName(), (float) event.getScaledResolution().getScaledWidth() / 2 - 35f, (float) event.getScaledResolution().getScaledHeight() / 2 + 17.5f, -1);
            } else {
                name.drawString(target.getDisplayName().getUnformattedText(), (float) event.getScaledResolution().getScaledWidth() / 2 - 35f, (float) event.getScaledResolution().getScaledHeight() / 2 + 17.5f, -1);
            }

            DecimalFormat df = new DecimalFormat("##.#");
            health.drawString(df.format(target.getHealth()), (float) event.getScaledResolution().getScaledWidth() / 2 + 22.5f, (float) event.getScaledResolution().getScaledHeight() / 2 + 20f, -1);
            RoundedUtil.drawRound((float) event.getScaledResolution().getScaledWidth() / 2 - 35f, (float) event.getScaledResolution().getScaledHeight() / 2 + 32.5f, 70f * healthPercentage, 5f, 2, ColorUtil.interpolateColorsBackAndForth(5, 1, clientColors.getFirst(), clientColors.getSecond(), false));

        }
    };
    private final IEventListener<ShaderEvent> onShaderEvent = event -> {
        ScaledResolution sr = new ScaledResolution(mc);
        EntityLivingBase target = KillAuraModule.getCurrentTarget();
        if (target != null) {
            RoundedUtil.drawRound((float) sr.getScaledWidth() / 2 - 37.5f, (float) sr.getScaledHeight() / 2 + 10, 75f, 30f, 8, ColorUtil.applyOpacity(new Color(pimcolors.getColor().getRGB()), (float) opacitys.getCurrentValue()));
        }
    };
}