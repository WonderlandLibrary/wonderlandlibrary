package best.azura.client.impl.module.impl.movement;

import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.value.BooleanValue;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("unused")
@ModuleInfo(name = "Gui Move", category = Category.MOVEMENT, description = "Lets you move in Guis")
public class GuiMove extends Module {

	private final BooleanValue motionBypass = new BooleanValue("Motion Bypass", "Bypass inventory move checks by stopping every few ticks", false);
	private final BooleanValue noSprint = new BooleanValue("No Sprint", "Bypass inventory move checks by not sprinting", false);
	private final BooleanValue silentNoSprint = new BooleanValue("Silent No Sprint", "Silently stop sprinting", noSprint::getObject, false);
	private final BooleanValue noInv = new BooleanValue("No Inventories", "Only move outside of inventories", false);

	@EventHandler
	public final Listener<EventUpdate> eventUpdateListener = eventUpdate -> {

		if (mc.currentScreen instanceof GuiChat || (noInv.getObject() && mc.currentScreen instanceof GuiContainer)) {
			mc.gameSettings.keyBindForward.pressed = false;
			mc.gameSettings.keyBindBack.pressed = false;
			mc.gameSettings.keyBindLeft.pressed = false;
			mc.gameSettings.keyBindRight.pressed = false;
			mc.gameSettings.keyBindJump.pressed = false;
			return;
		}
		mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
		mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode());
		mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode());
		mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode());
		mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());

		if (motionBypass.getObject() && mc.currentScreen instanceof GuiContainer && mc.thePlayer.ticksExisted % 4 != 0 && mc.thePlayer.isMoving()) {
            mc.gameSettings.keyBindForward.pressed =
            mc.gameSettings.keyBindBack.pressed =
            mc.gameSettings.keyBindLeft.pressed =
            mc.gameSettings.keyBindRight.pressed =
            mc.gameSettings.keyBindJump.pressed = false;
        }
	};

	@EventHandler
	public final Listener<EventMotion> eventMotionListener = e -> {
		if (noSprint.getObject() && mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
			if (silentNoSprint.getObject()) e.sprinting = false;
			else mc.thePlayer.setSprinting(false);
		}
	};

	@Override
	public void onDisable() {
		super.onDisable();
	}
}
