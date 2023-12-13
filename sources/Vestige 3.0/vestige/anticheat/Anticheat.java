package vestige.anticheat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import vestige.Vestige;
import vestige.anticheat.impl.BotCheck;
import vestige.anticheat.impl.FlyCheck;
import vestige.anticheat.impl.SpeedCheck;
import vestige.event.Listener;
import vestige.event.impl.EntityMoveEvent;
import vestige.event.impl.PacketReceiveEvent;
import vestige.event.impl.PacketSendEvent;
import vestige.util.IMinecraft;
import vestige.util.network.ServerUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class Anticheat implements IMinecraft {

    public final ArrayList<ACPlayer> players = new ArrayList<>();
    private boolean shouldClear;

    private boolean isInLobby;

    public Anticheat() {
        Vestige.instance.getEventManager().register(this);
    }

    @Listener
    public void onEntityMove(EntityMoveEvent event) {
        Entity entity = event.getEntity();

        if(entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;

            boolean found = false;

            for(ACPlayer acPlayer : players) {
                if(acPlayer.getEntity().equals(player)) {
                    found = true;
                    acPlayer.getData().updateInfos();

                    if(!isInLobby) {
                        acPlayer.getChecks().forEach(c -> c.check());
                    }
                }
            }

            if(!found) {
                if(!player.getGameProfile().getName().contains("-")) {
                    ACPlayer acPlayer = new ACPlayer(player);
                    acPlayer.getChecks().addAll(Arrays.asList(getChecks(acPlayer)));

                    players.add(acPlayer);
                }
            }
        }
    }

    @Listener
    public void onReceive(PacketReceiveEvent event) {
        if(event.getPacket() instanceof S3DPacketDisplayScoreboard) {
            S3DPacketDisplayScoreboard packet = event.getPacket();

            String scoreboardName = packet.func_149370_d();

            if(ServerUtil.isOnHypixel()) {
                if(!scoreboardName.contains("PreScoreboard") && !scoreboardName.contains("SForeboard") && !scoreboardName.contains("health") && !scoreboardName.contains("health_tab")) {
                    isInLobby = true;
                }
            }
        }
    }

    @Listener
    public void onSend(PacketSendEvent event) {
        if(mc.thePlayer == null || mc.thePlayer.ticksExisted < 5) {
            if(shouldClear) {
                players.clear();
                shouldClear = false;
                isInLobby = false;
            }
        } else {
            shouldClear = true;
        }
    }

    public ACPlayer getACPlayer(EntityPlayer entity) {
        for(ACPlayer player : players) {
            if(player.getEntity() == entity) {
                return player;
            }
        }

        return null;
    }

    private AnticheatCheck[] getChecks(ACPlayer player) {
        AnticheatCheck[] checks = {
                new FlyCheck(player),
                new SpeedCheck(player),
                new BotCheck(player)
        };

        return checks;
    }

}
