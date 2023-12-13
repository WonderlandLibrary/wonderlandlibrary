package summer.cheat.cheats.player;

import org.lwjgl.input.Mouse;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import summer.base.manager.Selection;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.base.manager.config.Cheats;

public class AutoTool extends Cheats {
    public Minecraft mc = Minecraft.getMinecraft();
    private int oldSlot = -1;
    private boolean wasBreaking = false;

    public AutoTool() {
        super("AutoTool", "Switches to the best tool", Selection.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (this.mc.currentScreen == null && Minecraft.thePlayer != null && Minecraft.theWorld != null
                && this.mc.objectMouseOver != null && this.mc.objectMouseOver.getBlockPos() != null
                && this.mc.objectMouseOver.entityHit == null && Mouse.isButtonDown(0)) {
            float bestSpeed = 1.0F;
            int bestSlot = -1;
            Block block = Minecraft.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock();
            for (int k = 0; k < 9; k++) {
                ItemStack item = Minecraft.thePlayer.inventory.getStackInSlot(k);
                if (item != null) {
                    float speed = item.getStrVsBlock(block);
                    if (speed > bestSpeed) {
                        bestSpeed = speed;
                        bestSlot = k;
                    }
                }
            }
            if (bestSlot != -1 && Minecraft.thePlayer.inventory.currentItem != bestSlot) {
                Minecraft.thePlayer.inventory.currentItem = bestSlot;
                this.wasBreaking = true;
            } else if (bestSlot == -1) {
                if (this.wasBreaking) {
                    Minecraft.thePlayer.inventory.currentItem = this.oldSlot;
                    this.wasBreaking = false;
                }
                this.oldSlot = Minecraft.thePlayer.inventory.currentItem;
            }
        } else if (Minecraft.thePlayer != null && Minecraft.theWorld != null) {
            if (this.wasBreaking) {
                Minecraft.thePlayer.inventory.currentItem = this.oldSlot;
                this.wasBreaking = false;
            }
            this.oldSlot = Minecraft.thePlayer.inventory.currentItem;
        }
    }
}
