package club.cloverhook.cheat.impl.misc;

import java.util.Objects;

import org.lwjgl.input.Keyboard;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.event.minecraft.SendPacketEvent;
import club.cloverhook.utils.Stopwatch;
import club.cloverhook.utils.property.impl.BooleanProperty;
import me.hippo.systems.lwjeb.annotation.Collect;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C0EPacketClickWindow;

public class GuiMove extends Cheat {

	private BooleanProperty   prop_desync      = new BooleanProperty("Desyncronize", "Desyncronizes the inventory with the server (spoof not being inside, bypasses most inventory move checks)", null, false);

	public static boolean closed, megalul, cancancel;

	private Stopwatch stopwatch;

	public GuiMove() {
		super("InvMove", "Allows you to move around, sprint, crouch and jump while in gui's.", CheatCategory.MOVEMENT);
		registerProperties(prop_desync);
		stopwatch = new Stopwatch();
	}
	
	@Collect
	public void onSendPacket(SendPacketEvent sendPacketEvent) {
		if (prop_desync.getValue() && !(mc.getMinecraft().currentScreen instanceof GuiChest)) {
			if (sendPacketEvent.getPacket() instanceof C0BPacketEntityAction) {
				C0BPacketEntityAction packet = (C0BPacketEntityAction)sendPacketEvent.getPacket();
				if (packet.getAction() == C0BPacketEntityAction.Action.OPEN_INVENTORY) {
					closed = false;
				}
			}

			if (sendPacketEvent.getPacket() instanceof C0EPacketClickWindow) {
				mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.OPEN_INVENTORY));
				closed = false;
				megalul = true;
				sendPacketEvent.setCancelled(cancancel);
			}
		}
	}
	
	@Collect
	public void onPlayerUpdate(PlayerUpdateEvent playerUpdateEvent) {
		if (this.getState()) {
			if (prop_desync.getValue() && !(mc.getMinecraft().currentScreen instanceof GuiChest)) {
				if (!closed) {
					if (stopwatch.hasPassed(51)) {
						mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(0));
						closed = true;
						stopwatch.reset();
					}
				} else {
					if (megalul) {
						if (stopwatch.hasPassed(52)) {
							cancancel = true;
							stopwatch.reset();
						} else {
							cancancel = false;
						}
					}
				}
			}
			KeyBinding[] moveKeys = { getMc().gameSettings.keyBindRight, getMc().gameSettings.keyBindLeft,
					getMc().gameSettings.keyBindBack, getMc().gameSettings.keyBindForward, getMc().gameSettings.keyBindJump,
					getMc().gameSettings.keyBindSprint, };
			if ((getMc().currentScreen instanceof GuiScreen)
					&& !(getMc().currentScreen instanceof GuiChat)) {
				KeyBinding[] array;
				int length = (array = moveKeys).length;
				for (int i = 0; i < length; i++) {
					KeyBinding key = array[i];
					key.pressed = Keyboard.isKeyDown(key.getKeyCode());
				}
			} else if (Objects.isNull(getMc().currentScreen)) {
				KeyBinding[] array2;
				int length2 = (array2 = moveKeys).length;
				for (int j = 0; j < length2; j++) {
					KeyBinding bind = array2[j];
					if (!Keyboard.isKeyDown(bind.getKeyCode())) {
						KeyBinding.setKeyBindState(bind.getKeyCode(), false);
					}
				}
			}
		}
	}

}
