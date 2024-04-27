/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  org.lwjgl.input.Mouse
 */
package net.dev.important.modules.module.modules.misc;

import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render2DEvent;
import net.dev.important.file.configs.FriendsConfig;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.render.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

@Info(name="MCF", description="Allows you to add a player as a friend by middle clicking him.", category=Category.MISC, cnName="\u6dfb\u52a0\u6740\u622e\u767d\u540d\u5355")
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
                FriendsConfig friendsConfig = Client.fileManager.friendsConfig;
                String playerName = ColorUtils.stripColor(entity.func_70005_c_());
                if (!friendsConfig.isFriend(playerName)) {
                    friendsConfig.addFriend(playerName);
                    Client.fileManager.saveConfig(friendsConfig);
                    ClientUtils.displayChatMessage("\u00a7a\u00a7l" + playerName + "\u00a7c was added to your friends.");
                } else {
                    friendsConfig.removeFriend(playerName);
                    Client.fileManager.saveConfig(friendsConfig);
                    ClientUtils.displayChatMessage("\u00a7a\u00a7l" + playerName + "\u00a7c was removed from your friends.");
                }
            } else {
                ClientUtils.displayChatMessage("\u00a7c\u00a7lError: \u00a7aYou need to select a player.");
            }
        }
        this.wasDown = Mouse.isButtonDown((int)2);
    }
}

