/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package com.wallhacks.losebypass.systems.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.InputEvent;
import com.wallhacks.losebypass.systems.module.Module;
import net.minecraft.entity.player.EntityPlayer;

@Module.Registration(name="MiddleClickFriend", description="Middle click players to friend them")
public class MCF
extends Module {
    @SubscribeEvent
    public void onInput(InputEvent event) {
        if (event.getKey() != -4) return;
        if (!(MCF.mc.objectMouseOver.entityHit instanceof EntityPlayer)) return;
        if (LoseBypass.socialManager.isFriend(MCF.mc.objectMouseOver.entityHit.getName())) {
            LoseBypass.sendInfo("Removed " + ChatFormatting.RED + MCF.mc.objectMouseOver.entityHit.getName() + ChatFormatting.RESET + " from your friends list");
            LoseBypass.socialManager.removeFriend(MCF.mc.objectMouseOver.entityHit.getName());
            return;
        }
        LoseBypass.sendInfo("Added " + ChatFormatting.AQUA + MCF.mc.objectMouseOver.entityHit.getName() + ChatFormatting.RESET + " as friend");
        LoseBypass.socialManager.addFriend(MCF.mc.objectMouseOver.entityHit.getName());
    }
}

