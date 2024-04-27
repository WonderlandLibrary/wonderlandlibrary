package me.aquavit.liquidsense.module.modules.misc;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.Render2DEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.file.configs.FriendsConfig;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

@ModuleInfo(name = "MidClick", description = "Allows you to add a player as a friend by right clicking him.", category = ModuleCategory.MISC)
public class MidClick extends Module {
    private boolean wasDown;

    @EventTarget
    public void onRender(Render2DEvent event) {
        if(mc.currentScreen != null)
            return;

        if(!wasDown && Mouse.isButtonDown(2)) {
            final Entity entity = mc.objectMouseOver.entityHit;

            if(entity instanceof EntityPlayer) {
                final String playerName = ColorUtils.stripColor(entity.getName());
                final FriendsConfig friendsConfig = LiquidSense.fileManager.friendsConfig;

                if(!friendsConfig.isFriend(playerName)) {
                    friendsConfig.addFriend(playerName);
                    LiquidSense.fileManager.saveConfig(friendsConfig);
                    ClientUtils.displayChatMessage("§a§l" + playerName + "§c was added to your friends.");
                }else{
                    friendsConfig.removeFriend(playerName);
                    LiquidSense.fileManager.saveConfig(friendsConfig);
                    ClientUtils.displayChatMessage("§a§l" + playerName + "§c was removed from your friends.");
                }
            }else
                ClientUtils.displayChatMessage("§c§lError: §aYou need to select a player.");
        }

        wasDown = Mouse.isButtonDown(2);
    }
}
