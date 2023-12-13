package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.item.ItemStack;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option.Op;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.TimeHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer;

@Module.Mod(displayName = "InvCleaner")
public class InvCleaner extends Module
{
    @Op(name = "Delay", min = 0.0, max = 1000.0, increment = 50.0)
    private double delay;
    private TimeHelper timer;
    
    public InvCleaner() {
        this.delay = 50.0;
        this.timer = new TimeHelper();
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.getState() == Event.State.POST) {
            final InventoryPlayer invp = ClientUtils.player().inventory;
            for (int i = 9; i < 45; ++i) {
                final ItemStack itemStack = ClientUtils.player().inventoryContainer.getSlot(i).getStack();
                if (itemStack != null) {
                    itemStack.getItem();
                    if (this.timer.hasReached((float)this.delay)) {
                        ClientUtils.playerController().windowClick(0, i, 0, 0, ClientUtils.player());
                        ClientUtils.playerController().windowClick(0, -999, 0, 0, ClientUtils.player());
                        this.timer.reset();
                    }
                }
            }
        }
    }
}
