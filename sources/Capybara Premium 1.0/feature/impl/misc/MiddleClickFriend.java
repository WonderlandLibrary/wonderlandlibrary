package fun.expensive.client.feature.impl.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.Rich;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.input.EventMouse;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.files.impl.FriendConfig;
import fun.rich.client.friend.Friend;
import fun.rich.client.utils.other.ChatUtils;
import net.minecraft.entity.EntityLivingBase;

public class MiddleClickFriend extends Feature {


    public MiddleClickFriend() {
        super("MiddleClickFriend", "Добавляет игрока в френд лист при нажатии на кнопку мыши", FeatureCategory.Misc);
    }

    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.getKey() == 2 && mc.pointedEntity instanceof EntityLivingBase) {
            if (Rich.instance.friendManager.getFriends().stream().anyMatch(friend -> friend.getName().equals(mc.pointedEntity.getName()))) {
                Rich.instance.friendManager.getFriends().remove(Rich.instance.friendManager.getFriend(mc.pointedEntity.getName()));
                ChatUtils.addChatMessage(ChatFormatting.RED + "Removed " + ChatFormatting.RESET + "'" + mc.pointedEntity.getName() + "'" + " as Friend!");
            } else {
                Rich.instance.friendManager.addFriend(new Friend(mc.pointedEntity.getName()));
                try {
                    Rich.instance.fileManager.getFile(FriendConfig.class).saveFile();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                ChatUtils.addChatMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.RESET + "'" + mc.pointedEntity.getName() + "'" + " as Friend!");
            }
        }
    }
}