package rip.helium.cheat.impl.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.helium.UpdateValueEvent;
import rip.helium.event.minecraft.ExitGameEvent;
import rip.helium.event.minecraft.RunTickEvent;
import rip.helium.utils.property.impl.DoubleProperty;
import rip.helium.utils.property.impl.StringsProperty;

/**
 * @author antja03
 */
public class Brightness extends Cheat {

    private final StringsProperty modeProperty = new StringsProperty("Mode", "How this cheat will function.", null,
            false, true, new String[]{"Gamma", "Night Vision"}, new Boolean[]{false, true});

    private final DoubleProperty gammaProperty = new DoubleProperty("Gamma Multiplier", "", () -> modeProperty.getSelectedStrings().get(0) == "Gamma",
            0, 0, 250, 1, "%");

    private float userGammaSetting;
    private PotionEffect addedEffect;

    public Brightness() {
        super("Brightness", "Sets the brightness of your game.", CheatCategory.VISUAL);
        registerProperties(modeProperty, gammaProperty);
        userGammaSetting = getMc().gameSettings.gammaSetting;
    }

    @Override
    public void onEnable() {
        userGammaSetting = getMc().gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        if (getMc().gameSettings.gammaSetting == gammaProperty.getValue()) {
            getMc().gameSettings.gammaSetting = userGammaSetting;
        }
        if (getPlayer() != null) {
            getPlayer().getActivePotionEffects().remove(addedEffect);
        }
    }

    @Collect
    public void onRunTick(RunTickEvent runTickEvent) {
        if (modeProperty.getSelectedStrings().get(0).equalsIgnoreCase("Night Vision")) {
            if (getPlayer() != null) {
                for (PotionEffect effect : getPlayer().getActivePotionEffects()) {
                    if (effect.getPotionID() == Potion.nightVision.id) {
                        effect.setDuration(1000);
                        return;
                    }
                }
                getPlayer().addPotionEffect(addedEffect = new PotionEffect(Potion.nightVision.id, 1000, 1));
            }
        } else if (modeProperty.getSelectedStrings().get(0).equalsIgnoreCase("Gamma")) {
            getMc().gameSettings.gammaSetting = gammaProperty.getValue().floatValue() / 100;
        }
    }

    @Collect
    public void onUpdateValue(UpdateValueEvent event) {
        if (event.getValue().equals(modeProperty)) {
            onDisable();
        }
    }


    @Collect //Call disable method so the users gamma setting returns to normal
    public void onExitGame(ExitGameEvent event) {
        onDisable();
    }

}
