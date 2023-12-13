package me.nyan.flush.module.impl.player;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventMotion;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.module.settings.NumberSetting;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Mouse;

public class FastEat extends Module {
    private final BooleanSetting instant = new BooleanSetting("Instant", this, false),
            onGround = new BooleanSetting("On Ground", this, true);
    private final NumberSetting speed = new NumberSetting("Eating Speed", this, 5, 1, 20);

    public FastEat() {
        super("FastEat", Category.PLAYER);
    }

    @SubscribeEvent
    public void onMotion(EventMotion e) {
        if (!mc.thePlayer.isEating() || e.isPost() ||
                (!mc.thePlayer.onGround && onGround.getValue()) ||
                mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            return;
        }

        for (int i = 0; i < (instant.getValue() ? 35 : speed.getValueInt()); i++) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
        }

        if (instant.getValue()) {
            mc.playerController.onStoppedUsingItem(mc.thePlayer);
        }
    }

    @Override
    public String getSuffix() {
        return instant.getValue() ? "Instant" : null;
    }
}

