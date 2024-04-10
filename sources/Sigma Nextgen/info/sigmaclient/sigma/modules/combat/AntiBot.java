package info.sigmaclient.sigma.modules.combat;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.player.WorldEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.utils.player.MovementUtils;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.server.SPlayerListItemPacket;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.UUID;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class AntiBot extends Module {
    public static ModeValue mode = new ModeValue("Type", "Matrix", new String[]{
            "Matrix", "Matrix2", "Hypixel", "Intave", "GommeHD", "AriaCraft", "Reflex", "RMC", "Hyt"
    });
    static ArrayList<PlayerEntity> rangeOut = new ArrayList<>();
    static ArrayList<UUID> matrixBot = new ArrayList<>();
    public AntiBot() {
        super("AntiBot", Category.Combat, "Dont attack server bot");
     registerValue(mode);
    }
    public static boolean isServerBots(Entity LivingEntity2){
        PlayerEntity LivingEntity;
        if(LivingEntity2 instanceof PlayerEntity){
            LivingEntity = (PlayerEntity) LivingEntity2;
        }else{
            return false;
        }
        if(!SigmaNG.SigmaNG.moduleManager.getModule(AntiBot.class).enabled){
            return false;
        }
        switch (mode.getValue()){
            case "Matrix":
                return !rangeOut.contains(LivingEntity);
            case "Matrix2":
                return matrixBot.contains(LivingEntity.getUniqueID());
            case "Hypixel":
                return  LivingEntity.getName().getString().startsWith("\u00a7c") || !rangeOut.contains(LivingEntity);
            case "Intave":
                return LivingEntity.ticksExisted < 20;
            case "Reflex":
                return !hasPlayer(LivingEntity.getName().getString()) || rangeOut.contains(LivingEntity);
            case "GommeHD":
                return rangeOut.contains(LivingEntity);
            case "AriaCraft":
                return !isNotBot(LivingEntity.getGameProfile().getId());
            case "Hyt":
                return !rangeOut.contains(LivingEntity);
            case "RMC":
                return !isNotBot(LivingEntity.getGameProfile().getId()) || !hasPlayer(LivingEntity.getName().getString()) || rangeOut.contains(LivingEntity);
        }
        return false;
    }

    @Override
    public void onWorldEvent(WorldEvent event) {
        rangeOut.clear();
        matrixBot.clear();
        super.onWorldEvent(event);
    }

    @Override
    public void onEnable() {
        matrixBot.clear();
        rangeOut.clear();
        super.onEnable();
    }
    /**
     * Returns the name that should be renderd for the player supplied
     */
    public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn)
    {
        return networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getString() : ScorePlayerTeam.func_237500_a_(networkPlayerInfoIn.getPlayerTeam(), new StringTextComponent(networkPlayerInfoIn.getGameProfile().getName())).getString();
    }
    public static boolean isNotBot(UUID name){
        return mc.getConnection() != null && mc.getConnection().playerInfoMap.containsKey(name) && getPlayerName(mc.getConnection().playerInfoMap.get(name)).startsWith("\u00a7");
    }
    public static boolean hasPlayer(String name){
        return mc.getConnection() != null && mc.getConnection().getPlayerInfo(name) != null;
    }
    @Override
    public void onPacketEvent(PacketEvent event) {
        switch (mode.getValue()) {
            case "Matrix":
                break;
            case "Matrix2":
            case "RMC":
                if(event.isRevive()){
                    if(event.packet instanceof SPlayerListItemPacket){
                        for(SPlayerListItemPacket.AddPlayerData data : ((SPlayerListItemPacket) event.packet).getEntries()) {
                            if(
                                    ((SPlayerListItemPacket) event.packet).getAction().equals(SPlayerListItemPacket.Action.ADD_PLAYER)
                                    && hasPlayer(data.getProfile().getName())
                            ){
                                if(!matrixBot.contains(data.getProfile().getId())){
                                    matrixBot.add(data.getProfile().getId());
                                }
                            }
                        }
                    }
                }
                break;
        }
        super.onPacketEvent(event);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            switch (mode.getValue()){
                case "Matrix":
                    for(PlayerEntity PlayerEntity : mc.world.getPlayers()){
                        double xDiff = Math.abs(PlayerEntity.lastTickPosX - PlayerEntity.getPosX());
                        double zDiff = Math.abs(PlayerEntity.lastTickPosZ - PlayerEntity.getPosZ());
                        double yDiff = Math.abs(PlayerEntity.lastTickPosY - PlayerEntity.getPosY());
                        if(((xDiff > 0 && zDiff > 0) || yDiff > 0) &&
                                !rangeOut.contains(PlayerEntity) &&
                                PlayerEntity.getDistance(mc.player) > 10 + MovementUtils.getSpeed() * 3 && mc.player.ticksExisted > 20){
                            rangeOut.add(PlayerEntity);
                        }
                    }
                    break;
                case "Hyt":
                    for(PlayerEntity PlayerEntity : mc.world.getPlayers()){
                        double xDiff = Math.abs(PlayerEntity.lastTickPosX - PlayerEntity.getPosX());
                        double zDiff = Math.abs(PlayerEntity.lastTickPosZ - PlayerEntity.getPosZ());
                        double yDiff = Math.abs(PlayerEntity.lastTickPosY - PlayerEntity.getPosY());
                        if((xDiff > 0 || zDiff > 0 || yDiff > 0) && !rangeOut.contains(PlayerEntity) && PlayerEntity.ticksExisted > 10){
                            rangeOut.add(PlayerEntity);
                        }
                    }
                    break;
                case "Matrix2":
                    break;
                case "GommeHD":
                    for(PlayerEntity PlayerEntity : mc.world.getPlayers()){
                        if(PlayerEntity.ticksExisted < 20 && PlayerEntity.isInvisible() && PlayerEntity.getDistance(mc.player) <= 20){
                            rangeOut.add(PlayerEntity);
                        }
                    }
                    break;
                case "RMC":
                case "Reflex":
                    for(PlayerEntity PlayerEntity : mc.world.getPlayers()){
                        if(PlayerEntity.ticksExisted < 15 && PlayerEntity.isInvisible() && PlayerEntity.getDistance(mc.player) <= 20){
                            rangeOut.add(PlayerEntity);
                        }
                    }
                    break;
                case "Hypixel":
                    for(PlayerEntity PlayerEntity : mc.world.getPlayers()){
                        double xDiff = Math.abs(PlayerEntity.lastTickPosX - PlayerEntity.getPosX());
                        double zDiff = Math.abs(PlayerEntity.lastTickPosZ - PlayerEntity.getPosZ());
                        double szDiff = Math.abs(mc.player.lastTickPosY - mc.player.getPosZ());
                        double yDiff = Math.abs(PlayerEntity.lastTickPosY - PlayerEntity.getPosY());
                        if(PlayerEntity.ticksExisted > 10 && ((xDiff > 0 && zDiff > 0) || yDiff > 0) &&
                                !rangeOut.contains(PlayerEntity) &&
                                PlayerEntity.getDistance(mc.player) > 10 + MovementUtils.getSpeed() * 3 + szDiff){
                            rangeOut.add(PlayerEntity);
                        }
                    }
                    break;
                case "Intave":
                    break;
            }
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}
