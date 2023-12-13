package slavikcodd3r.rainbow.module.modules.player;

import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import slavikcodd3r.rainbow.event.Event;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option.Op;
import net.minecraft.client.Minecraft;

@Module.Mod(displayName = "AutoGApple")
public class AutoGApple extends Module
{
    @Op(name = "Health", min = 0.0, max = 10.0, increment = 0.5)
    private double health;
    protected Minecraft mc;
    
    public AutoGApple() {
        this.health = 9.0;
        this.mc = Minecraft.getMinecraft();
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        final int foodSlot;
        if (event.getState() == Event.State.PRE && (foodSlot = this.getFoodSlotInHotbar()) != -1 && this.mc.thePlayer.getHealth() < this.health * 2.0 && this.mc.thePlayer.isCollidedVertically) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(foodSlot));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.mainInventory[foodSlot]));
            for (int i = 0; i < 32; ++i) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
            }
            this.mc.thePlayer.stopUsingItem();
            this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.mc.thePlayer.inventory.currentItem));
        }
    }
    
    private int getFoodSlotInHotbar() {
        for (int i = 0; i < 9; ++i) {
            if (this.mc.thePlayer.inventory.mainInventory[i] != null && this.mc.thePlayer.inventory.mainInventory[i].getItem() != null && this.mc.thePlayer.inventory.mainInventory[i].getItem() instanceof ItemFood) {
                return i;
            }
        }
        return -1;
    }
}
