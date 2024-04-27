package me.aquavit.liquidsense.module.modules.player;

import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.MoveEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.FloatValue;
import me.aquavit.liquidsense.value.IntegerValue;
import me.aquavit.liquidsense.value.ListValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

@ModuleInfo(name = "FastUse", description = "Allows you to use items faster.", category = ModuleCategory.PLAYER)
public class FastUse extends Module {

    private ListValue modeValue = new ListValue("Mode", new String[]{"Instant","Hypixel" ,"NCP", "AAC", "CustomDelay"}, "AAC");
    private BoolValue noMoveValue = new BoolValue("NoMove", false);
    private IntegerValue delayValue = new IntegerValue("CustomDelay", 0, 0, 300);
    private IntegerValue customSpeedValue = new IntegerValue("CustomSpeed", 2, 1, 35);
    private FloatValue customTimer = new FloatValue("CustomTimer", 1.1f, 0.5f, 2f);

    private MSTimer msTimer = new MSTimer();
    private boolean usedTimer = false;

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (usedTimer) {
            mc.timer.timerSpeed = 1F;
            usedTimer = false;
        }

        if (!mc.thePlayer.isUsingItem()) {
            msTimer.reset();
            return;
        }

        Item usingItem = mc.thePlayer.getItemInUse().getItem();

        if (usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion) {
            switch (modeValue.get().toLowerCase()) {
                case "instant": {
                    for (int i=0; i<=35;i++){
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                    }

                    mc.playerController.onStoppedUsingItem(mc.thePlayer);
                    break;
                }
                case "hypixel": {
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getItemInUse()));
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    break;
                }
                case "ncp": {
                    if (mc.thePlayer.getItemInUseDuration() > 14) {
                        for (int i=0; i<=20;i++){
                            mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                        }
                        mc.playerController.onStoppedUsingItem(mc.thePlayer);
                    }
                    break;
                }
                case "aac": {
                    mc.timer.timerSpeed = 1.22F;
                    usedTimer = true;
                    break;
                }
                case "customdelay": {
                    mc.timer.timerSpeed = customTimer.get();
                    usedTimer = true;

                    if (!msTimer.hasTimePassed((long)delayValue.get()))
                        return;

                    for (int i=0; i<=customSpeedValue.get();i++){
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                    }

                    msTimer.reset();
                    break;
                }
            }
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (event == null) return;

        if (!state || !mc.thePlayer.isUsingItem() || !noMoveValue.get()) return;
        Item usingItem = mc.thePlayer.getItemInUse().getItem();
        if ((usingItem instanceof ItemFood || usingItem instanceof ItemBucketMilk || usingItem instanceof ItemPotion))
            event.zero();
    }

    @Override
    public void onDisable() {
        if (usedTimer) {
            mc.timer.timerSpeed = 1F;
            usedTimer = false;
        }
    }
}
