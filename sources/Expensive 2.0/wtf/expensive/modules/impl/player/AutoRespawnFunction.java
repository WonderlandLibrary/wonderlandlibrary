package wtf.expensive.modules.impl.player;

import net.minecraft.client.gui.screen.DeathScreen;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
/**
 * @author dedinside
 * @since 04.06.2023
 */
@FunctionAnnotation(name = "AutoRespawn", type = Type.Player)
public class AutoRespawnFunction extends Function {

    @Override
    public void onEvent(final Event event) {
        if (event instanceof EventUpdate) {
            if (mc.currentScreen instanceof DeathScreen && mc.player.deathTime > 2) {
                mc.player.respawnPlayer();
                mc.displayGuiScreen(null);
            }
        }
    }
}
