package dev.tenacity.module.impl.combat;

import dev.tenacity.module.ModuleCategory;
import dev.tenacity.module.Module;
import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.setting.impl.ModeSetting;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Mouse;


public final class FastBow extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Ghostly");

    private final IEventListener<MotionEvent> motionEventEventListener = event -> {
        if(mc.thePlayer.getCurrentEquippedItem() == null) return;
        switch (mode.getCurrentMode()) {
            case "Vanilla":
                if (Mouse.isButtonDown(1) && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                    for (int i = 0; i < 20; ++i) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                        mc.rightClickDelayTimer = 0;
                    }
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    mc.playerController.onStoppedUsingItem(mc.thePlayer);
                }
                break;
            case "Ghostly":
                if(Mouse.isButtonDown(1) && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow){
                    for(int i = 0; i < 20; i++){

                        mc.rightClickDelayTimer = 0;
                       mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    }
                    mc.playerController.onStoppedUsingItem(mc.thePlayer);
                }
                break;
        }
    };

    @Override
    public void onDisable(){
        mc.rightClickDelayTimer = 4;
        super.onDisable();
    }

    public FastBow() {
        super("Fastbow", "shoot bows faster", ModuleCategory.COMBAT);
        initializeSettings(mode);
    }
}
