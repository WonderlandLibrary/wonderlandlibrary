package info.sigmaclient.sigma.modules.misc;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.player.WorldEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.gui.hud.notification.NotificationManager;
import info.sigmaclient.sigma.utils.ChatUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.SwordItem;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class MurderFucker extends Module {
    BooleanValue autoSay = new BooleanValue("Auto Say Killer", false);
    BooleanValue autoSay2 = new BooleanValue("Auto Say Detective", false);
    BooleanValue detective = new BooleanValue("Detective", false);
    public MurderFucker() {
        super("MurderFucker", Category.Misc, "Auto fuck murder");
     registerValue(autoSay);
     registerValue(autoSay2);
     registerValue(detective);
    }
    public static PlayerEntity killer = null;
    public static PlayerEntity detectiver = null;

    @Override
    public void onDisable() {
        killer = null;
        detective = null;
        super.onDisable();
    }

    @Override
    public void onWorldEvent(WorldEvent event) {
        killer = null;
        detective = null;
        super.onWorldEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPost()) return;
        for(PlayerEntity PlayerEntity : mc.world.getPlayers()){
            if(PlayerEntity.equals(mc.player)) continue;
            if(PlayerEntity.inventory.getCurrentItem().getItem() instanceof SwordItem){
                if(killer != null) continue;
                killer = PlayerEntity;
                NotificationManager.notify("Murder Warning", PlayerEntity.getName() + " is murder.");
                ChatUtils.sendMessageWithPrefix(PlayerEntity.getName() + " is murder.");
                if(autoSay.isEnable()){
                    mc.player.sendChatMessage(PlayerEntity.getName() + " is murder");
                }
            }
            if(PlayerEntity.inventory.getCurrentItem().getItem() instanceof BowItem){
                if(detectiver != null) continue;
                detectiver = PlayerEntity;
                ChatUtils.sendMessageWithPrefix(PlayerEntity.getName() + " is detective.");
                if(autoSay2.isEnable()){
                    mc.player.sendChatMessage(PlayerEntity.getName() + " is detective");
                }
            }
        }
    }
}
