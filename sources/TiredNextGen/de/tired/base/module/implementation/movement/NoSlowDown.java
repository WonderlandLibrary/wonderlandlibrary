package de.tired.base.module.implementation.movement;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.guis.newclickgui.setting.impl.BooleanSetting;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.EventPreMotion;
import de.tired.base.event.events.SlowDownEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@ModuleAnnotation(name = "NoSlowDown", category = ModuleCategory.MOVEMENT, clickG = "Disable slowdown while blocking or eating")
public class NoSlowDown extends Module {

    public ModeSetting packetMode = new ModeSetting("packetMode", this, new String[]{"ItemChange", "PlayerDigging", "SyncNCP", "Vanilla", "Watchdog"});

    private NumberSetting slowDownFactor = new NumberSetting("SlowdownFactor", this, .1, .1, 1, .1, () -> packetMode.getValue().equalsIgnoreCase("Vanilla"));

    public BooleanSetting justAir = new BooleanSetting("justAir", this, true);

    @EventTarget
    public void onUpdate(UpdateEvent e) {
        if (!this.isState()) return;
        if (MC.thePlayer.onGround && justAir.getValue()) return;
        if (MC.thePlayer.isBlocking() || MC.thePlayer.isEating() || MC.thePlayer.isUsingItem()) {
            switch (packetMode.getValue()) {
                case "Watchdog": {
                    if(getPlayer().isBlocking()) {
                        if (getPlayer().getHeldItem().getItem() instanceof ItemSword) {
                            if (getPlayer().ticksExisted % 9 == 0) {
                                sendPacket(new C09PacketHeldItemChange(getPlayer().inventory.currentItem + 1));
                            }else {
                                sendPacket(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
                            }
                        } else if (getPlayer().isUsingItem()){
                            if (getPlayer().ticksExisted % 3 == 0)
                                sendPacketUnlogged(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
                        }
                    }
                    break;
                }
                case "ItemChange":
                    sendPacketUnlogged(new C09PacketHeldItemChange(MC.thePlayer.inventory.currentItem));
                    break;
                case "PlayerDigging":
                    sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    break;
                case "SyncNCP": {
                    MC.playerController.syncCurrentPlayItem();
                    sendPacket(new C08PacketPlayerBlockPlacement(MC.thePlayer.getCurrentEquippedItem()));
                    break;
                }
            }

        }
    }

    @EventTarget
    public void onPre(EventPreMotion eventPreMotion) {
        if (MC.thePlayer.onGround && justAir.getValue()) return;
        if (MC.thePlayer.isBlocking() || MC.thePlayer.isEating() || MC.thePlayer.isUsingItem()) {
            switch (packetMode.getValue()) {
                case "SyncNCP": {
                    MC.playerController.syncCurrentPlayItem();
                    sendPacket(new C08PacketPlayerBlockPlacement(MC.thePlayer.getCurrentEquippedItem()));
                    MC.thePlayer.motionX *= .7F;
                    MC.thePlayer.motionZ *= .7F;
                    break;
                }
                case "Vanilla": {
                    MC.thePlayer.motionX *= slowDownFactor.getValue();
                    MC.thePlayer.motionZ *= slowDownFactor.getValue();
                    break;
                }
            }


        }
    }

    @EventTarget
    public void onEvent(SlowDownEvent e) {
        if (MC.thePlayer.onGround && justAir.getValue()) return;
        if (MC.thePlayer.isBlocking() || MC.thePlayer.isEating() || MC.thePlayer.isUsingItem()) {
            e.setCancelled(true);
        }
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
