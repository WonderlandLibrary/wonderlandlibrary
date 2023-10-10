package client.module.impl.movement.inventorymove;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.input.MoveInputEvent;
import client.module.impl.movement.InventoryMove;
import client.value.Mode;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;

public class VanillaInventoryMove extends Mode<InventoryMove> {

    public VanillaInventoryMove(final String name, final InventoryMove parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = event -> {
        if (mc.currentScreen == null || mc.currentScreen instanceof GuiChat) return;
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) event.setForward(event.getForward() + 1.0F);
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) event.setForward(event.getForward() - 1.0F);
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) event.setStrafe(event.getStrafe() + 1.0F);
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) event.setStrafe(event.getStrafe() - 1.0F);
        event.setJumping(Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()));
    };

}
