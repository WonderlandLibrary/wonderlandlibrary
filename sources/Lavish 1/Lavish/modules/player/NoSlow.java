// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.modules.player;

import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import Lavish.utils.movement.MovementUtil;
import Lavish.event.events.EventMotion;
import Lavish.event.Event;
import Lavish.ClickGUI.settings.Setting;
import Lavish.Client;
import java.util.ArrayList;
import Lavish.modules.Category;
import Lavish.modules.Module;

public class NoSlow extends Module
{
    public NoSlow() {
        super("NoSlow", 0, true, Category.Player, "Makes you not have any slow down while using an item");
        final ArrayList<String> options = new ArrayList<String>();
        options.add("Vanilla");
        options.add("NCP");
        Client.instance.setmgr.rSetting(new Setting("NoSlow Mode", this, "Vanilla", options));
    }
    
    @Override
    public void onEvent(final Event e) {
        if (Client.instance.setmgr.getSettingByName("NoSlow Mode").getValString().equalsIgnoreCase("NCP") && e instanceof EventMotion && NoSlow.mc.thePlayer.onGround && MovementUtil.isMoving() && (NoSlow.mc.thePlayer.isBlocking() || NoSlow.mc.thePlayer.isEating())) {
            if (e.isPre()) {
                NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
            else {
                NoSlow.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(NoSlow.mc.thePlayer.inventory.getCurrentItem()));
            }
        }
    }
}
