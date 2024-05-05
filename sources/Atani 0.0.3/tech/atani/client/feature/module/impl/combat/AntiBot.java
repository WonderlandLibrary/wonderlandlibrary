package tech.atani.client.feature.module.impl.combat;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.world.WorldSettings;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.feature.value.impl.StringBoxValue;
import tech.atani.client.feature.combat.CombatManager;
import tech.atani.client.feature.combat.interfaces.IgnoreList;

import java.util.ArrayList;
import java.util.List;

@Native
@ModuleData(name = "AntiBot", description = "Blocks anticheat bots", category = Category.COMBAT)
public class AntiBot extends Module implements IgnoreList {

    private final ArrayList<Entity> bots = new ArrayList<>();

    public final StringBoxValue mode = new StringBoxValue("Mode", "Which mode will the module use?", this, new String[]{"Watchdog", "Matrix", "Twerion"});

    private boolean wasAdded = false;
    private String name;

    public AntiBot() {
        CombatManager.getInstance().addIgnoreList(this);
    }
    
    @Override
    public String getSuffix() {
    	return mode.getValue();
    }

    @Listen
    public final void onPacket(PacketEvent packetEvent) {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null) {
            return;
        }
        switch (mode.getValue()) {
            case "Matrix":
                Packet<?> packet = packetEvent.getPacket();

                if (packet instanceof S41PacketServerDifficulty) {
                    wasAdded = false;
                }

                if (packet instanceof S38PacketPlayerListItem) {
                    S38PacketPlayerListItem packetListItem = (S38PacketPlayerListItem) packet;
                    S38PacketPlayerListItem.AddPlayerData data = packetListItem.getPlayers().get(0);

                    if (data.getProfile() != null && data.getProfile().getName() != null) {
                        name = data.getProfile().getName();

                        if (!wasAdded) {
                            wasAdded = name.equals(Methods.mc.thePlayer.getCommandSenderName());
                        } else if (!Methods.mc.thePlayer.isSpectator() && !Methods.mc.thePlayer.capabilities.allowFlying &&
                                (data.getPing() != 0) &&
                                (data.getGameMode() != WorldSettings.GameType.NOT_SET)) {
                            packetEvent.setCancelled(true);
                        }
                    }
                }
                break;
        }
    }


    @Listen
    public final void onUpdate(UpdateEvent updateEvent) {
        switch (mode.getValue()) {
            case "Twerion":
                List<Entity> list = new ArrayList<>();
                Methods.mc.theWorld.loadedEntityList.forEach(entity -> {
                    if(entity instanceof EntityZombie) {
                        list.add(entity);
                    }
                });
                for(Entity entity : list)
                    Methods.mc.theWorld.removeEntity(entity);
                break;
            case "Watchdog":
                Methods.mc.theWorld.playerEntities.forEach(player -> {
                    final NetworkPlayerInfo info = Methods.mc.getNetHandler().getPlayerInfo(player.getUniqueID());
                    if (info == null) {
                        this.bots.add(player);
                    } else {
                        this.bots.remove(player);
                    }
                });
                break;
        }
    }

    @Override
    public boolean shouldSkipEntity(Entity entity) {
        if(!this.isEnabled())
            return false;
        return this.bots.contains(entity);
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null) {
            return;
        }
        this.bots.clear();
    }

}
