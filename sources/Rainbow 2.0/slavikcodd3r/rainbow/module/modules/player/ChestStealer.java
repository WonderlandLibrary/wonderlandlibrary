package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.inventory.ContainerChest;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.modules.combat.Velocity;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.TimeHelper;
import net.minecraft.client.Minecraft;

@Module.Mod(displayName = "ChestStealer")
public class ChestStealer extends Module
{
    @Op(min = 0.0, max = 500.0, increment = 1.0, name = "Delay")
    private long delay;
    public static Minecraft mc;
    public TimeHelper time;
    
    static {
        ChestStealer.mc = Minecraft.getMinecraft();
    }
    
    public ChestStealer() {
        this.time = new TimeHelper();
        this.delay = 20;
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (ChestStealer.mc.thePlayer.openContainer != null && ChestStealer.mc.thePlayer.openContainer instanceof ContainerChest) {
            final ContainerChest chest = (ContainerChest)ChestStealer.mc.thePlayer.openContainer;
            for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
                if (chest.getLowerChestInventory().getStackInSlot(i) != null) {
                    if (this.time.hasReached(this.delay)) {
                        ChestStealer.mc.playerController.windowClick(chest.windowId, i, 0, 1, ChestStealer.mc.thePlayer);
                        this.time.reset();
                    }
                }
                else if (this.isChestEmpty(chest)) {
                    ChestStealer.mc.thePlayer.closeScreen();
                }
            }
        }
    }
    
    public boolean isChestEmpty(final ContainerChest chest) {
        for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
            if (chest.getLowerChestInventory().getStackInSlot(i) != null) {
                return false;
            }
        }
        return true;
    }
}
