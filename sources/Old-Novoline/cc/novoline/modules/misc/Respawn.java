package cc.novoline.modules.misc;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import net.minecraft.client.gui.GuiGameOver;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class Respawn extends AbstractModule {

    /* constructors */
    public Respawn(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "Respawn", EnumModuleType.MISC, "Respawns you");
    }

    /* events */
    @EventTarget
    public void onUpdate(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (this.mc.currentScreen instanceof GuiGameOver) {
                this.mc.player.respawnPlayer();
            }
        }
    }

}
