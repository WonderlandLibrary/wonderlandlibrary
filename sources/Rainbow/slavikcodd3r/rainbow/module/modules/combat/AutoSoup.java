package slavikcodd3r.rainbow.module.modules.combat;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.option.Option;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.Timer;

@Mod(displayName = "AutoSoup")
public class AutoSoup extends Module
{
    @Option.Op(min = 0.5, max = 10.0, increment = 0.5)
    public static double health;
    @Option.Op(min = 200.0, max = 1000.0, increment = 50.0)
    public static double delay;
    @Option.Op(min = 0.0, max = 9.0, increment = 1.0)
    private double slot;
    public static Timer timer;
    public static boolean souping;
    
    static {
        AutoSoup.health = 4.5;
        AutoSoup.delay = 250.0;
        AutoSoup.timer = new Timer();
    }
    
    public AutoSoup() {
        this.slot = 6.0;
    }
    
    @EventTarget(3)
    private void onUpdate(final UpdateEvent event) {
        switch (event.getState()) {
            case POST: {
                final int soupSlot = getSoupFromInventory();
                if (getSoupFromInventory() != -1 && ClientUtils.player().getHealth() <= AutoSoup.health * 2.0 && AutoSoup.timer.delay((float)AutoSoup.delay)) {
                    this.swap(getSoupFromInventory(), (int)this.slot);
                    ClientUtils.packet(new C09PacketHeldItemChange((int)this.slot));
                    ClientUtils.packet(new C08PacketPlayerBlockPlacement(ClientUtils.player().inventory.getCurrentItem()));
                    ClientUtils.packet(new C09PacketHeldItemChange(ClientUtils.player().inventory.currentItem));
                    AutoSoup.timer.reset();
                    break;
                }
                break;
            }
        }
    }
    
    protected void swap(final int slot, final int hotbarNum) {
        ClientUtils.playerController().windowClick(ClientUtils.player().inventoryContainer.windowId, slot, hotbarNum, 2, ClientUtils.player());
    }
    
    public static int getSoupFromInventory() {
        int soup = -1;
        int counter = 0;
        for (int i = 1; i < 45; ++i) {
            if (ClientUtils.player().inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = ClientUtils.player().inventoryContainer.getSlot(i).getStack();
                final Item item = is.getItem();
                if (Item.getIdFromItem(item) == 282) {
                    ++counter;
                    soup = i;
                }
            }
        }
        return soup;
    }
}
