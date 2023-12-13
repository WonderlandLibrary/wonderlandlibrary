package com.ohare.client.module.modules.other;

import java.awt.Color;

import com.ohare.client.Client;
import com.ohare.client.event.events.input.MouseEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.Printer;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.entity.player.EntityPlayer;

public class MCF extends Module {

    public MCF() {
        super("MCF", Category.OTHER, new Color(255, 195, 215, 255).getRGB());
        setDescription("Middle click friends");
    }

    @Subscribe
    public void onMouseClick(MouseEvent event) {
        if (event.getButton() == 2 && mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) mc.objectMouseOver.entityHit;
            String name = player.getName();
            if (Client.INSTANCE.getFriendManager().isFriend(name)) {
                Client.INSTANCE.getFriendManager().removeFriend(name);
                Printer.print("Removed " + name + " as a friend!");
            } else {
                Client.INSTANCE.getFriendManager().addFriend(name);
                Printer.print("Added " + name + " as a friend!");
            }
        }
    }
}
