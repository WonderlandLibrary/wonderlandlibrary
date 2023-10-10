package vestige.module.impl.player;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Mouse;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.Priority;
import vestige.event.impl.TickEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;

public class AutoTool extends Module {

    private int oldSlot;

    private boolean wasDigging;

    private final BooleanSetting spoof = new BooleanSetting("Item spoof", false);

    public AutoTool() {
        super("AutoTool", Category.PLAYER);
        this.addSettings(spoof);
    }

    @Override
    public void onDisable() {
        if(wasDigging) {
            mc.thePlayer.inventory.currentItem = oldSlot;
            wasDigging = false;
        }

        Vestige.instance.getSlotSpoofHandler().stopSpoofing();
    }

    @Listener(Priority.LOW)
    public void onTick(TickEvent event) {
        if((Mouse.isButtonDown(0) || mc.gameSettings.keyBindAttack.isKeyDown()) && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            Block block = mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();

            float strength = 0;

            if(!wasDigging) {
                oldSlot = mc.thePlayer.inventory.currentItem;

                if(spoof.isEnabled()) {
                    Vestige.instance.getSlotSpoofHandler().startSpoofing(oldSlot);
                }
            }

            for(int i = 0; i <= 8; i++) {
                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);

                if(stack != null) {
                    float slotStrength = stack.getStrVsBlock(block);

                    if(slotStrength > strength) {
                        mc.thePlayer.inventory.currentItem = i;
                        strength = slotStrength;
                    }
                }
            }

            wasDigging = true;
        } else {
            if(wasDigging) {
                mc.thePlayer.inventory.currentItem = oldSlot;

                Vestige.instance.getSlotSpoofHandler().stopSpoofing();

                wasDigging = false;
            } else {
                oldSlot = mc.thePlayer.inventory.currentItem;
            }
        }
    }

}
