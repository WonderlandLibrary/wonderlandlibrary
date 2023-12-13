package today.flux.module.implement.Combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import today.flux.Flux;
import today.flux.event.PostUpdateEvent;
import today.flux.event.TickEvent;
import today.flux.event.UpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.FloatValue;
import today.flux.module.value.ModeValue;
import today.flux.utility.BlockUtils;

public class Regen extends Module {
    public static ModeValue mode = new ModeValue("Regen", "Mode", "Old", "Old", "Potion");
    public static FloatValue amount = new FloatValue("Regen", "Packets", 30f, 10f, 100f, 10f);

    public Regen() {
        super("Regen", Category.Combat, mode);
    }

    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (mode.getValue().equals("Potion")) {
            if (mc.thePlayer.getActivePotionEffect(Potion.regeneration) != null && (mc.thePlayer.onGround || BlockUtils.isOnLadder() || BlockUtils.isInLiquid() || BlockUtils.isOnLiquid()) && mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth()) {
                for (int i = 0; i < mc.thePlayer.getMaxHealth() - mc.thePlayer.getHealth(); ++i) {
                    if (mc.thePlayer.getActivePotionEffect(Potion.regeneration) == null)
                        break;
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                }
            }
        }

        if (mode.getValue().equals("Old")) {
            if (mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() && mc.thePlayer.getFoodStats().getFoodLevel() > 0) {
                for (int i = 0; i < amount.getValue(); i++) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                }
            }
        }
    }
}
