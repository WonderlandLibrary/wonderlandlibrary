package ru.smertnix.celestial.feature.impl.misc;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.input.EventMouse;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.files.impl.FriendConfig;
import ru.smertnix.celestial.friend.Friend;
import ru.smertnix.celestial.utils.other.ChatUtils;

public class MiddleClickFriend extends Feature {


    public MiddleClickFriend() {
        super("Click Friend", "Удаляет и добавляет друзей Кликом", FeatureCategory.Player);
    }

    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.getKey() == 2 && mc.pointedEntity instanceof EntityPlayer) {
            if (Celestial.instance.friendManager.getFriends().stream().anyMatch(friend -> friend.getName().equals(mc.pointedEntity.getName()))) {
                Celestial.instance.friendManager.getFriends().remove(Celestial.instance.friendManager.getFriend(mc.pointedEntity.getName()));
                ChatUtils.addChatMessage(ChatFormatting.RED + "Removed " + ChatFormatting.RESET + "'" + mc.pointedEntity.getName() + "'" + " as Friend!");
            } else {
                Celestial.instance.friendManager.addFriend(new Friend(mc.pointedEntity.getName()));
                try {
                    Celestial.instance.fileManager.getFile(FriendConfig.class).saveFile();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                ChatUtils.addChatMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.RESET + "'" + mc.pointedEntity.getName() + "'" + " as Friend!");
            }
        }
    }
}