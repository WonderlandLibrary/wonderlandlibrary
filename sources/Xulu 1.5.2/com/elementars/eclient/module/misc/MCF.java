// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.misc;

import com.elementars.eclient.event.EventTarget;
import net.minecraft.entity.Entity;
import com.elementars.eclient.command.Command;
import com.elementars.eclient.friend.Friends;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import com.elementars.eclient.event.events.EventMiddleClick;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class MCF extends Module
{
    private final Value<Boolean> message;
    
    public MCF() {
        super("MCF", "Middle Click Friends", 0, Category.MISC, true);
        this.message = this.register(new Value<Boolean>("Send Message", this, true));
    }
    
    @EventTarget
    public void onMiddleClick(final EventMiddleClick event) {
        final RayTraceResult ray = MCF.mc.objectMouseOver;
        if (ray.typeOfHit == RayTraceResult.Type.ENTITY) {
            final Entity entity = ray.entityHit;
            if (entity instanceof EntityPlayer) {
                final String name = ((EntityPlayer)entity).getDisplayNameString();
                if (Friends.isFriend(name)) {
                    Friends.delFriend(name);
                    if (this.message.getValue()) {
                        Command.sendChatMessage("&b" + name + "&r has been unfriended.");
                    }
                }
                else {
                    Friends.addFriend(name);
                    if (this.message.getValue()) {
                        Command.sendChatMessage("&b" + name + "&r has been friended.");
                    }
                }
            }
        }
    }
}
