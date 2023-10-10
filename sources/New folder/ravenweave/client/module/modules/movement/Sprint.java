package ravenweave.client.module.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.Utils;

public class Sprint extends Module {
    public static TickSetting multiDir, ignoreBlindness;

    public Sprint() {
        super("Sprint", ModuleCategory.movement);
        this.registerSetting(multiDir = new TickSetting("All Directions", false));
        this.registerSetting(ignoreBlindness = new TickSetting("Ignore Blindness", false));
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if (Utils.Player.isPlayerInGame() && mc.inGameHasFocus) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
        }
    }

}
