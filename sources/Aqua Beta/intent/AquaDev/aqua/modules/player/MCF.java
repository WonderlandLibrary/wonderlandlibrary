package intent.AquaDev.aqua.modules.player;

import events.Event;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.utils.FriendSystem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

public class MCF
extends Module {
    private long milliSeconds = System.currentTimeMillis();

    public MCF() {
        super("MCF", "MCF", 0, Category.Player);
    }

    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEvent(Event event) {
        Entity entity;
        if (event instanceof EventUpdate && Mouse.isButtonDown((int)2) && (entity = MCF.mc.objectMouseOver.entityHit) != null && entity instanceof EntityPlayer && System.currentTimeMillis() - this.milliSeconds > 300L) {
            if (FriendSystem.getFriends().contains((Object)entity.getName())) {
                FriendSystem.removeFriend((String)entity.getName());
                this.milliSeconds = System.currentTimeMillis();
            } else {
                FriendSystem.addFriend((String)entity.getName());
                this.milliSeconds = System.currentTimeMillis();
            }
        }
    }
}
