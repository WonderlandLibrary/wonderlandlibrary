package wtf.expensive.modules.impl.util;

import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.util.text.TextFormatting;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventUpdate;
import wtf.expensive.modules.Function;
import wtf.expensive.modules.FunctionAnnotation;
import wtf.expensive.modules.Type;
import wtf.expensive.util.ClientUtil;

/**
 * @author dedinside
 * @since 12.06.2023
 */
@FunctionAnnotation(name = "DeathCoords", type = Type.Util)
public class DeathCoordsFunction extends Function {
    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            checkDeathCoordinates();
        }
    }

    public void checkDeathCoordinates() {
        if (isPlayerDead()) {
            int positionX = mc.player.getPosition().getX();
            int positionY = mc.player.getPosition().getY();
            int positionZ = mc.player.getPosition().getZ();

            if (mc.player.deathTime < 1) {
                printDeathCoordinates(positionX, positionY, positionZ);
            }
        }
    }

    private boolean isPlayerDead() {
        return mc.player.getHealth() < 1.0f && mc.currentScreen instanceof DeathScreen;
    }

    private void printDeathCoordinates(int x, int y, int z) {
        String message = "Координаты смерти: " + TextFormatting.GRAY + "X: " + x + " Y: " + y + " Z: " + z + TextFormatting.RESET;
        ClientUtil.sendMesage(message);
    }
}
