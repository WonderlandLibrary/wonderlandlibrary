/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.features.module.modules.fun;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.file.configs.FriendsConfig;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

@ModuleInfo(name="FriendClick", description="Allows you to add a player as a friend by right clicking him.", category=ModuleCategory.FUN)
public class MidClick
extends Module {
    private boolean wasDown;

    @EventTarget
    public void onRender(Render2DEvent event) {
        if (MidClick.mc.field_71462_r != null) {
            return;
        }
        if (!this.wasDown && Mouse.isButtonDown((int)2)) {
            Entity entity = MidClick.mc.field_71476_x.field_72308_g;
            if (entity instanceof EntityPlayer) {
                FriendsConfig friendsConfig = LiquidBounce.fileManager.friendsConfig;
                String playerName = ColorUtils.stripColor(entity.func_70005_c_());
                if (!friendsConfig.isFriend(playerName)) {
                    friendsConfig.addFriend(playerName);
                    LiquidBounce.fileManager.saveConfig(friendsConfig);
                    ClientUtils.displayChatMessage("\u00a7a\u00a7l" + playerName + "\u00a7c was added to your friends.");
                } else {
                    friendsConfig.removeFriend(playerName);
                    LiquidBounce.fileManager.saveConfig(friendsConfig);
                    ClientUtils.displayChatMessage("\u00a7a\u00a7l" + playerName + "\u00a7c was removed from your friends.");
                }
            } else {
                ClientUtils.displayChatMessage("\u00a7c\u00a7lError: \u00a7aYou need to select a player.");
            }
        }
        this.wasDown = Mouse.isButtonDown((int)2);
    }
}

