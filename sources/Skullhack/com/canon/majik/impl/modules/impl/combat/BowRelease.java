package com.canon.majik.impl.modules.impl.combat;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.events.TickEvent;
import com.canon.majik.impl.modules.api.Category;
import com.canon.majik.impl.modules.api.Module;
import com.canon.majik.impl.setting.settings.NumberSetting;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.math.BlockPos;

public class BowRelease extends Module {

    NumberSetting speed = setting("Speed", 3, 0, 5);

    public BowRelease(String name, Category category) {
        super(name, category);
    }

    @EventListener
    public void onTick(TickEvent event){
        if(nullCheck()) return;
        if(mc.player.inventory.getCurrentItem().getItem() == Items.BOW && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() > speed.getValue().intValue()){
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
             mc.player.stopActiveHand();
        }
    }
}
