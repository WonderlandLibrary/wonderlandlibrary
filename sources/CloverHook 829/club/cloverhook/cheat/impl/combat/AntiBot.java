package club.cloverhook.cheat.impl.combat;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.utils.property.impl.StringsProperty;
import me.hippo.systems.lwjeb.annotation.Collect;

public class AntiBot extends Cheat {

    public static StringsProperty prop_mode = new StringsProperty("Mode", "Changes the mode.", null, false, true, new String[]{"Watchdog"}, new Boolean[]{true});

    public AntiBot(){
        super("AntiBot", "Stops killaura from targetting bots", CheatCategory.COMBAT);
        registerProperties(prop_mode);
    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent e) {
        setMode(prop_mode.getSelectedStrings().get(0));
    }
}
