package de.theBest.MythicX.modules.movement;

import de.Hero.settings.Setting;
import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.events.EventPostMotion;
import de.theBest.MythicX.events.EventPreMotion;
import de.theBest.MythicX.events.EventUpdate;
import de.theBest.MythicX.modules.Category;
import de.theBest.MythicX.modules.Module;
import eventapi.EventTarget;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0CPacketInput;

import java.awt.*;
import java.util.ArrayList;

public class NoSlow extends Module {

    public NoSlow() {
        super("NoSlow", Type.Movement, 0, Category.MOVEMENT, Color.orange, "Prevents you from slowing down while using items");
    }



    @EventTarget
    public void onPreMotion(EventPreMotion event){
        //if(mc.thePlayer.isBlocking()) {
            //mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 1));
            //mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        //}
    }

    @EventTarget
    public void onPostMotion(EventPostMotion eventPostMotion){
        //if(mc.thePlayer.isBlocking())
            //mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventoryContainer.getSlot(mc.thePlayer.inventory.currentItem + 36).getStack()));
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate){
        int curSlot = mc.thePlayer.inventory.currentItem;
        int spoof = (curSlot == 0) ? 1 : -1;
        if (mc.thePlayer.isUsingItem() && MythicX.setmgr.getSettingByName("NoSlow").getValString().equalsIgnoreCase("Blocksmc")) {
            mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(curSlot + spoof));
            mc.getNetHandler().addToSendQueue((Packet)new C09PacketHeldItemChange(curSlot));
        }
    }



    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void setup(){
        ArrayList<String> options = new ArrayList<>();
        options.add("Vanilla");
        options.add("Blocksmc");
        MythicX.setmgr.rSetting(new Setting("NoSlow", this, "Vanilla", options));
    }
}
