package wtf.evolution.module.impl.Player;

import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ListSetting;

@ModuleInfo(name = "NoDelay", type = Category.Player)
public class NoDelay extends Module {
    public ListSetting delay = new ListSetting("NoDelay", "Jump", "LeftClick", "Block", "RightClick").call(this);


    @EventTarget
    public void onMotion(EventUpdate e) {
        if (delay.selected.contains("Jump")) {
            mc.player.jumpTicks = 0;
        }
        if (delay.selected.contains("LeftClick")) {
            mc.leftClickCounter = 0;
        }
        if (delay.selected.contains("Block")) {
            mc.playerController.blockHitDelay = 0;
        }
        if (delay.selected.contains("RightClick")) {
            mc.rightClickDelayTimer = 0;
        }
    }
}