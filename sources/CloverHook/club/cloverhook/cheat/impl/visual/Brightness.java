package club.cloverhook.cheat.impl.visual;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.cloverhook.UpdateValueEvent;
import club.cloverhook.event.minecraft.ExitGameEvent;
import club.cloverhook.event.minecraft.RunTickEvent;
import club.cloverhook.utils.property.impl.DoubleProperty;
import club.cloverhook.utils.property.impl.StringsProperty;
import me.hippo.systems.lwjeb.annotation.Collect;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * @author antja03
 */
public class Brightness extends Cheat {

    private StringsProperty modeProperty = new StringsProperty("Mode", "How this cheat will function.", null,
            false, true, new String[] {"Gamma", "Night Vision"}, new Boolean[] {false, true});

    private DoubleProperty gammaProperty = new DoubleProperty("Gamma Multiplier", "", () -> modeProperty.getSelectedStrings().get(0) == "Gamma",
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
        	if (getPlayer().getActivePotionEffects().contains(addedEffect)) {
                getPlayer().getActivePotionEffects().remove(addedEffect);
            }
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
