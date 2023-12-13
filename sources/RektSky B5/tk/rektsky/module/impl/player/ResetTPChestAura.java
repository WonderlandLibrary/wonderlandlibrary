/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S07PacketRespawn;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.Client;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.impl.player.TPChestAura;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.utils.display.ColorUtil;

public class ResetTPChestAura
extends Module {
    public ResetTPChestAura() {
        super("ResetTPChestAura", "Auto Reset opened chests of TPChestAura, and it will reset if it's enabled", Category.PLAYER);
    }

    @Override
    public void onEnable() {
        this.reset();
    }

    @Subscribe
    public void onPacket(PacketReceiveEvent event) {
        if (event.getPacket() instanceof S07PacketRespawn || event.getPacket() instanceof S01PacketJoinGame) {
            this.reset();
        }
    }

    public void reset() {
        TPChestAura.position.clear();
        Client.notify(new Notification.PopupMessage("TPChestAura Reset", "Successfully reset TPChestAura!", ColorUtil.NotificationColors.GREEN, 20));
    }
}

