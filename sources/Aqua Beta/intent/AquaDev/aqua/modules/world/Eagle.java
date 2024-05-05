package intent.AquaDev.aqua.modules.world;

import events.Event;
import events.listeners.EventPreMotion;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class Eagle
extends Module {
    public Eagle() {
        super("Eagle", "Eagle", 0, Category.World);
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        KeyBinding.setKeyBindState((int)Eagle.mc.gameSettings.keyBindSneak.getKeyCode(), (boolean)false);
        super.onDisable();
    }

    public void onEvent(Event event) {
        if (event instanceof EventPreMotion) {
            // empty if block
        }
        if (event instanceof EventUpdate) {
            Eagle.mc.thePlayer.setSprinting(false);
            Eagle.mc.gameSettings.keyBindSprint.pressed = false;
            BlockPos blockPos = new BlockPos(Eagle.mc.thePlayer.posX, Eagle.mc.thePlayer.posY - 1.0, Eagle.mc.thePlayer.posZ);
            if (Eagle.mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.air) {
                mc.rightClickMouse();
            }
            Eagle.mc.gameSettings.keyBindSneak.pressed = Eagle.mc.theWorld.getBlockState(blockPos).getBlock() == Blocks.air;
        }
    }
}
