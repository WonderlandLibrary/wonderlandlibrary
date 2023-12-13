package today.flux.module.implement.Player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;
import today.flux.event.TickEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.ModeValue;
import today.flux.utility.BlockUtils;

public class FastUse extends Module {

    public static ModeValue mode = new ModeValue("FastUse","Mode", "NCP", "NCP", "Vanilla", "Timer");
    private boolean canBoost = false;

    public FastUse() {
        super("FastUse", Category.Player, mode);
    }

    public static BooleanValue inAir = new BooleanValue("FastUse", "In Air", false);

    @EventTarget
    public void onUpdate(TickEvent event) {
        if (!(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFood
                || mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBucketMilk || mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemPotion)){
            return;
        }
        if(mode.getValue().equals("NCP")) {
            if (this.mc.thePlayer.getItemInUseDuration() == 17)
                canBoost = true;
            if ((inAir.getValue() || this.mc.thePlayer.onGround) && notBad() && canBoost) {
                canBoost = false;
                for (int i = 0; i < 20; ++i) {
                    this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(this.mc.thePlayer.onGround));
                }
            }
        } else if(mode.getValue().equals("Vanilla")){
            if (this.mc.thePlayer.isEating() && (inAir.getValue() || this.mc.thePlayer.onGround)) {
                for (int i = 0; i < 30; ++i) {
                    this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(this.mc.thePlayer.onGround));
                }
            }
        } else if(mode.getValue().equals("Timer")) {
            if (this.mc.thePlayer.isEating()) {
                this.mc.getTimer().timerSpeed = 1.5f;
            } else if(this.mc.getTimer().timerSpeed == 1.5f) {
                this.mc.getTimer().timerSpeed = 1.0f;
            }
        }
    }

    private boolean notBad() {
        return !(!this.mc.thePlayer.isInWater() && BlockUtils.isInLiquid());
    }
}
