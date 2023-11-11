package me.nyan.flush.module.impl.misc;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.utils.other.ChatUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import org.lwjgl.input.Mouse;

public class MCF extends Module {
    private boolean middleClickPressed;

    public MCF() {
        super("MCF", Category.MISC);
    }

    @SubscribeEvent
    public void onUpdate(EventUpdate e) {
        if (Mouse.isButtonDown(2) && !middleClickPressed) {
            if (mc.pointedEntity instanceof EntityOtherPlayerMP) {
                if (!flush.getFriendManager().getFriends().contains(mc.pointedEntity.getName())) {
                    flush.getFriendManager().addFriend(mc.pointedEntity.getName());
                    ChatUtils.println("\"" + mc.pointedEntity.getName() + "\" is now your friend.");
                } else {
                    flush.getFriendManager().removeFriend(mc.pointedEntity.getName());
                    ChatUtils.println("\"" + mc.pointedEntity.getName() + "\" is no longer your friend.");
                }
            }

            middleClickPressed = true;
        }

        if (!Mouse.isButtonDown(2) && middleClickPressed) {
            middleClickPressed = false;
        }
    }
}
