/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package me.wintware.client.module.world;

import me.wintware.client.Main;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.friendsystem.Friend;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.other.ChatUtils;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Mouse;

public class MCF
extends Module {
    public boolean onFriend = true;

    public MCF() {
        super("MCF", Category.Player);
    }

    @Override
    public void onDisable() {
        this.onFriend = true;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdate event) {
        if (Mouse.isButtonDown(2)) {
            if (MCF.mc.pointedEntity != null) {
                if (MCF.mc.pointedEntity instanceof EntityLivingBase && this.onFriend) {
                    this.onFriend = false;
                    if (Main.instance.friendManager.getFriends().stream().anyMatch(paramFriend -> paramFriend.getName().equals(MCF.mc.pointedEntity.getName()))) {
                        Main.instance.friendManager.getFriends().remove(Main.instance.friendManager.getFriend(MCF.mc.pointedEntity.getName()));
                        ChatUtils.addChatMessage("Removed '" + MCF.mc.pointedEntity.getName() + "' as Friend!");
                    } else {
                        Main.instance.friendManager.addFriend(new Friend("", MCF.mc.pointedEntity.getName(), false));
                        ChatUtils.addChatMessage("Added " + MCF.mc.pointedEntity.getName() + " as Friend!");
                    }
                }
            }
        }
        if (!Mouse.isButtonDown(2)) {
            this.onFriend = true;
        }
    }
}

