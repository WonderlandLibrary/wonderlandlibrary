package tech.atani.client.feature.module.impl.movement;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.feature.value.impl.CheckBoxValue;

@ModuleData(name = "InventoryMove", description = "Move inside your inventory.", category = Category.MOVEMENT)
public class InventoryMove extends Module {
    private final CheckBoxValue openPacket = new CheckBoxValue("No Open Packet", "Should the module send open packets?", this, false),
            carry = new CheckBoxValue("Carry", "Should the crafting table allow carrying items?", this, false);

    @Listen
    public final void onUpdateMotion(UpdateMotionEvent updateMotionEvent) {
        if (updateMotionEvent.getType() == UpdateMotionEvent.Type.MID) {
            block3 : {
                KeyBinding[] moveKeys;
                block2 : {
                    moveKeys = new KeyBinding[]{Methods.mc.gameSettings.keyBindForward, Methods.mc.gameSettings.keyBindLeft, Methods.mc.gameSettings.keyBindBack, Methods.mc.gameSettings.keyBindRight, Methods.mc.gameSettings.keyBindJump, Methods.mc.gameSettings.keyBindSneak, Methods.mc.gameSettings.keyBindSprint};
                    if (Methods.mc.currentScreen == null || (Methods.mc.currentScreen instanceof GuiChat))
                        break block2;
                    for (KeyBinding key : moveKeys) {
                        key.pressed = Keyboard.isKeyDown(key.getKeyCode());
                    }
                    break block3;
                }

                for (KeyBinding bind : moveKeys) {
                    if (Keyboard.isKeyDown(bind.getKeyCode()))
                        continue;
                    KeyBinding.setKeyBindState(bind.getKeyCode(), false);
                }
            }
        }
    }

    @Listen
    public final void onPacketEvent(PacketEvent packetEvent) {
        if(mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        if (packetEvent.getType() == PacketEvent.Type.OUTGOING) {
            if(openPacket.getValue() && (packetEvent.getPacket() instanceof S2DPacketOpenWindow || packetEvent.getPacket() instanceof S2EPacketCloseWindow)) {
                packetEvent.setCancelled(true);
            }

            if(carry.getValue() && packetEvent.getPacket() instanceof C0DPacketCloseWindow) {
                packetEvent.setCancelled(true);
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
