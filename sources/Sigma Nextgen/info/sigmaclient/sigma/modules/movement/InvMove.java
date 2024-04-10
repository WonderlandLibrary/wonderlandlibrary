package info.sigmaclient.sigma.modules.movement;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.gui.other.clickgui.NursultanClickGui;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.gui.clickgui.JelloClickGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.network.play.client.CEntityActionPacket;

import java.util.Arrays;
import java.util.List;

public final class InvMove extends Module {
    public BooleanValue aac = new BooleanValue("AACP", false);
    private static final List<KeyBinding> keys = Arrays.asList(
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindJump
    );
    public InvMove() {
        super("InvMove", Category.Movement, "lets you move in your inventory");
     registerValue(aac);
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if(event.packet instanceof CEntityActionPacket){
            if(((CEntityActionPacket) event.packet).getAction() == CEntityActionPacket.Action.START_SPRINTING){
                if(aac.isEnable()) event.cancelable = true;
            }
        }
        super.onPacketEvent(event);
    }

    public static void updateStates() {
        if (mc.currentScreen != null) {
            keys.forEach(k -> KeyBinding.setKeyBindState(k.keyCode, InputMappings.isKeyDown(k)));
        }
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()) {
            if (mc.currentScreen instanceof ContainerScreen || mc.currentScreen instanceof JelloClickGui || mc.currentScreen instanceof NursultanClickGui) {
                updateStates();
            }
        }
        super.onUpdateEvent(event);
    }
}
