package me.aquavit.liquidsense.module.modules.player;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.exploit.Ghost;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import net.minecraft.client.gui.GuiGameOver;

@ModuleInfo(name = "AutoSpawn", description = "Automatically respawns you after dying.", category = ModuleCategory.PLAYER)
public class AutoSpawn extends Module {
    private BoolValue instantValue = new BoolValue("Instant", true);

    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (LiquidSense.moduleManager.getModule(Ghost.class).getState())
            return;
        if (instantValue.get() ? mc.thePlayer.getHealth() == 0F || mc.thePlayer.isDead : mc.currentScreen instanceof GuiGameOver && (((GuiGameOver)mc.currentScreen).enableButtonsTimer) >= 20) {
            mc.thePlayer.respawnPlayer();
            mc.displayGuiScreen(null);
        }
    }
}
