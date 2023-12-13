package me.earth.phobos.manager;

import me.earth.phobos.Phobos;
import me.earth.phobos.features.Feature;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.client.Notifications;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TotemPopManager
        extends Feature {
    private Notifications notifications;
    private Map<EntityPlayer, Integer> poplist = new ConcurrentHashMap<EntityPlayer, Integer>();
    private final Set<EntityPlayer> toAnnounce = new HashSet<EntityPlayer>();

    public void onUpdate() {
        if (this.notifications.totemAnnounce.passedMs(this.notifications.delay.getValue().intValue()) && this.notifications.isOn() && this.notifications.totemPops.getValue().booleanValue()) {
            for (EntityPlayer player : this.toAnnounce) {
                if (player == null) continue;
                int playerNumber = 0;
                for (char character : player.getName().toCharArray()) {
                    playerNumber += character;
                    playerNumber *= 10;
                }
                Command.sendOverwriteMessage("\u00a7c" + player.getName() + " popped " + "\u00a7a" + this.getTotemPops(player) + "\u00a7c" + " Totem" + (this.getTotemPops(player) == 1 ? "" : "s") + ".", playerNumber, this.notifications.totemNoti.getValue());
                this.toAnnounce.remove(player);
                this.notifications.totemAnnounce.reset();
                break;
            }
        }
    }

    public void onLogout() {
        this.onOwnLogout(this.notifications.clearOnLogout.getValue());
    }

    public void init() {
        this.notifications = Phobos.moduleManager.getModuleByClass(Notifications.class);
    }

    public void onTotemPop(EntityPlayer player) {
        this.popTotem(player);
        if (!player.equals(TotemPopManager.mc.player)) {
            this.toAnnounce.add(player);
            this.notifications.totemAnnounce.reset();
        }
    }

    public void onDeath(EntityPlayer player) {
        if (this.getTotemPops(player) != 0 && !player.equals(TotemPopManager.mc.player) && this.notifications.isOn() && this.notifications.totemPops.getValue().booleanValue()) {
            int playerNumber = 0;
            for (char character : player.getName().toCharArray()) {
                playerNumber += character;
                playerNumber *= 10;
            }
            Command.sendOverwriteMessage("\u00a7c" + player.getName() + " died after popping " + "\u00a7a" + this.getTotemPops(player) + "\u00a7c" + " Totem" + (this.getTotemPops(player) == 1 ? "" : "s") + ".", playerNumber, this.notifications.totemNoti.getValue());
            this.toAnnounce.remove(player);
        }
        this.resetPops(player);
    }

    public void onLogout(EntityPlayer player, boolean clearOnLogout) {
        if (clearOnLogout) {
            this.resetPops(player);
        }
    }

    public void onOwnLogout(boolean clearOnLogout) {
        if (clearOnLogout) {
            this.clearList();
        }
    }

    public void clearList() {
        this.poplist = new ConcurrentHashMap<EntityPlayer, Integer>();
    }

    public void resetPops(EntityPlayer player) {
        this.setTotemPops(player, 0);
    }

    public void popTotem(EntityPlayer player) {
        this.poplist.merge(player, 1, Integer::sum);
    }

    public void setTotemPops(EntityPlayer player, int amount) {
        this.poplist.put(player, amount);
    }

    public int getTotemPops(EntityPlayer player) {
        Integer pops = this.poplist.get(player);
        if (pops == null) {
            return 0;
        }
        return pops;
    }

    public String getTotemPopString(EntityPlayer player) {
        return "\u00a7f" + (this.getTotemPops(player) <= 0 ? "" : "-" + this.getTotemPops(player) + " ");
    }
}

