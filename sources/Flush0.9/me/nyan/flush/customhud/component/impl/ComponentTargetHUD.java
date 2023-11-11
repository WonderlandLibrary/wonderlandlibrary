package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.setting.impl.ColorSetting;
import me.nyan.flush.customhud.setting.impl.ModeSetting;
import me.nyan.flush.customhud.setting.impl.NumberSetting;
import me.nyan.flush.module.impl.combat.Aura;
import me.nyan.flush.module.impl.combat.TPAura;
import me.nyan.flush.module.impl.combat.targethud.TargetHUD;
import me.nyan.flush.module.impl.combat.targethud.targethuds.AstolfoTargetHUD;
import me.nyan.flush.module.impl.combat.targethud.targethuds.FlushTargetHUD;
import me.nyan.flush.utils.render.ColorUtils;
import net.minecraft.entity.EntityLivingBase;

public class ComponentTargetHUD extends Component {
    private Aura aura;
    private TPAura tpAura;
    private ModeSetting mode;
    private ModeSetting theme;
    private NumberSetting saturation, speed;
    private ColorSetting color;
    private TargetHUD targetHud;

    @Override
    public void onAdded() {
        aura = flush.getModuleManager().getModule(Aura.class);
        tpAura = flush.getModuleManager().getModule(TPAura.class);

        settings.add(mode = new ModeSetting("Mode", "Flush", "Flush", "Astolfo"));
        settings.add(theme = new ModeSetting("Theme", "Custom", "Rainbow", "Pulsing", "Astolfo", "Custom"));

        settings.add(saturation = new NumberSetting("Color Saturation", 0.8, 0.3, 1, 0.1,
                () -> theme.is("rainbow") || theme.is("astolfo")));
        settings.add(speed = new NumberSetting("Color Speed", 2, 0.4, 3, 0.1,
                () -> theme.is("rainbow")));
        settings.add(color = new ColorSetting("Color", 0xFF00A5FF,
                () -> theme.is("pulsing") || theme.is("custom")));
    }

    @Override
    public void draw(float x, float y) {
        EntityLivingBase entity = null;
        if (aura.target != null) {
            entity = aura.target;
        } else if (tpAura.target != null) {
            entity = tpAura.target;
        }

        switch (mode.getValue().toUpperCase()) {
            case "FLUSH":
                if (!(targetHud instanceof FlushTargetHUD)) {
                    targetHud = new FlushTargetHUD();
                }
                break;

            case "ASTOLFO":
                if (!(targetHud instanceof AstolfoTargetHUD)) {
                    targetHud = new AstolfoTargetHUD();
                }
                break;
        }

        if (targetHud == null) {
            return;
        }

        targetHud.draw(entity, x, y, getColor());
    }

    public int getColor() {
        switch (theme.getValue().toLowerCase()) {
            case "custom":
                return color.getRGB();
            case "pulsing":
                return ColorUtils.fade(color.getRGB(), 100, 1);
            case "astolfo":
                return ColorUtils.getAstolfo(saturation.getValueFloat(), 3000F * 0.75F, 1);
            default:
                return ColorUtils.getRainbow(1, saturation.getValueFloat(), speed.getValueFloat());
        }
    }

    @Override
    public int width() {
        if (targetHud == null) {
            return 150;
        }
        return targetHud.getWidth();
    }

    @Override
    public int height() {
        if (targetHud == null) {
            return 70;
        }
        return targetHud.getHeight();
    }
}
