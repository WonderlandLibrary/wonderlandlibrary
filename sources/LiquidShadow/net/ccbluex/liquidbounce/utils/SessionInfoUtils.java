package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;

public class SessionInfoUtils extends MinecraftInstance implements Listenable {
    private static int deaths = 0;
    private static int kills = 0;

    private static int wins = 0;
    private static int loses = 0;


    private static int playTimeH = 0;
    private static int playTimeM = 0;
    private static int playTimeS = 0;

    private static boolean deathAdded;

    private static final MSTimer loseTitleTimer = new MSTimer();
    private static final MSTimer loseChatTimer = new MSTimer();

    private static final MSTimer addedLoseTimer = new MSTimer();

    private static int ticks;

    @EventTarget
    public void onUpdate(UpdateEvent updateEvent) {
        if (mc.thePlayer.isDead && !deathAdded) {
            LiquidBounce.eventManager.callEvent(new PlayerDeathEvent());
            deathAdded = true;
        }
        if (deathAdded && !mc.thePlayer.isDead){
            deathAdded = false;
        }

        if (ticks > 20) {
            playTimeS++;
            ticks = 0;
        }
        if (playTimeS >= 60) {
            playTimeM++;
            playTimeS = 0;
        }
        if (playTimeM >= 60) {
            playTimeH++;
            playTimeM = 0;
        }

        ticks++;
    }

    @EventTarget
    public void onKill(KillEvent killEvent) {
        kills++;
    }

//    @EventTarget
//    public void onAttack(AttackEvent attackEvent) {
//        if (attackEvent.getTargetEntity() instanceof EntityPlayer) {
//            EntityPlayer player = (EntityPlayer) attackEvent.getTargetEntity();
//            if (player.getHealth() < 0) {
//                kills++;
//            }
//        }
//    }

    @EventTarget
    public void onPacket(PacketEvent packetEvent) {
        if (ServerUtils.getRemoteIp().toLowerCase().contains("Mineland".toLowerCase()) || ServerUtils.getRemoteIp().toLowerCase().contains("join-ml".toLowerCase())) {
            if (packetEvent.getPacket() instanceof S45PacketTitle) {
                String message = ((S45PacketTitle) packetEvent.getPacket()).getMessage().getFormattedText();
                if (message.toLowerCase().contains("Wasted".toLowerCase())) {
                    LiquidBounce.eventManager.callEvent(new PlayerDeathEvent());
                    loseTitleTimer.reset();
                    if (!loseChatTimer.hasTimePassed(1000) && addedLoseTimer.hasTimePassed(10000)) {
                        LiquidBounce.eventManager.callEvent(new LoseGameEvent());
                        addedLoseTimer.reset();
                    }
                }
            }
            if (packetEvent.getPacket() instanceof S02PacketChat) {
                String message = ((S02PacketChat) packetEvent.getPacket()).getChatComponent().getFormattedText();
                if ((message.toLowerCase().contains("was slain by".toLowerCase()) || message.toLowerCase().contains("was thrown out of the world by".toLowerCase()) || message.toLowerCase().contains("hit the ground too hard whilst trying to escape".toLowerCase())) && message.toLowerCase().contains(mc.session.getUsername().toLowerCase())) {
                    if ((message.split(" ")[message.split(" ").length - 1].toLowerCase().contains(mc.session.getUsername().toLowerCase())) && !message.contains("»") && !message.contains(":")) {
                        LiquidBounce.eventManager.callEvent(new KillEvent(mc.theWorld.getPlayerEntityByName(message.split(" ")[2].replace("§","").substring(2))));
                    }
                }
                if (message.toLowerCase().contains("§".toLowerCase()) && message.toLowerCase().contains("for the game.".toLowerCase()) && !message.toLowerCase().contains("[".toLowerCase()) && !message.contains("»")) {
                    if (!loseTitleTimer.hasTimePassed(1000) && addedLoseTimer.hasTimePassed(10000)) {
                        LiquidBounce.eventManager.callEvent(new LoseGameEvent());
                        addedLoseTimer.reset();
                    }
                    loseChatTimer.reset();
                }
                if (message.toLowerCase().contains("Victory".toLowerCase()) && !message.toLowerCase().contains("[".toLowerCase())) {
                    if (loseTitleTimer.hasTimePassed(3000)) {
                        LiquidBounce.eventManager.callEvent(new WinGameEvent());
                    }
                }
            }
        }
        if (packetEvent.getPacket() instanceof C00Handshake) {
            reset();
        }
    }

    @EventTarget
    public void onDeath(PlayerDeathEvent event) {
        deaths++;
    }

    @EventTarget
    public void onWin(WinGameEvent event) {
        wins++;
    }

    @EventTarget
    public void onLose(LoseGameEvent event) {
        loses++;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    public static int getDeaths() {
        return deaths;
    }

    public static int getKills() {
        return kills;
    }

    public static int getWins() {
        return wins;
    }

    public static int getLoses() {
        return loses;
    }

    public static boolean isInSupportedServer() {
        if (ServerUtils.serverData == null) {
            return false;
        }

        return ServerUtils.serverData.serverIP.toLowerCase().contains("Mineland".toLowerCase())
                || ServerUtils.serverData.serverIP.toLowerCase().contains("join-ml".toLowerCase());
    }

    public static int getPlayTimeH() {
        return playTimeH;
    }

    public static int getPlayTimeM() {
        return playTimeM;
    }

    public static int getPlayTimeS() {
        return playTimeS;
    }

    public static void reset() {
        deaths = 0;
        kills = 0;
        wins = 0;
        loses = 0;
        playTimeH = 0;
        playTimeM = 0;
        playTimeS = 0;
        ticks = 0;
        deathAdded = false;
        addedLoseTimer.reset();
        loseTitleTimer.reset();
        loseChatTimer.reset();
    }

    @EventTarget
    public void onConnectServer(ConnectServerEvent connectServerEvent) {
        reset();
    }
}
