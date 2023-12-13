/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.wintware.client.module.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventAttackClient;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.other.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class AntiBot
extends Module {
    public static ArrayList<Entity> isRealPlayer = new ArrayList();

    public AntiBot() {
        super("AntiBot", Category.Combat);
        Main.instance.setmgr.rSetting(new Setting("Invisible Remove", this, false));
    }

    @EventTarget
    public void onMouse(EventAttackClient event) {
        if (this.getState()) {
            EntityPlayer entityPlayer = (EntityPlayer)AntiBot.mc.objectMouseOver.entityHit;
            String name = entityPlayer.getName();
            if (entityPlayer != null) {
                if (Main.instance.friendManager.getFriends().contains(entityPlayer.getName())) {
                    return;
                }
                if (isRealPlayer.contains(entityPlayer)) {
                    ChatUtils.addChatMessage(ChatFormatting.RED + name + ChatFormatting.WHITE + " Already in AntiBot-List!");
                } else {
                    ChatUtils.addChatMessage(ChatFormatting.RED + name + ChatFormatting.WHITE + " Was added in AntiBot-List!");
                }
            }
        }
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        for (Entity entity : AntiBot.mc.world.loadedEntityList) {
            if (Minecraft.player != null) {
                return;
            }
            if (!Main.instance.setmgr.getSettingByName("Invisible Remove").getValue() || entity instanceof EntityPlayer && entity.isInvisible()) continue;
            AntiBot.mc.world.removeEntity(entity);
        }
    }
}

