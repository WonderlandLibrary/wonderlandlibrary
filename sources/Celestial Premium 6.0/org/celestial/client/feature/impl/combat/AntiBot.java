/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventAttackClient;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.helpers.player.MovementHelper;
import org.celestial.client.helpers.world.EntityHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class AntiBot
extends Feature {
    public static ArrayList<Entity> isRealPlayer = new ArrayList();
    public static ListSetting mode;
    public static NumberSetting botTicks;
    public static List<Entity> isBotPlayer;
    private final BooleanSetting removeFromWorld;
    ArrayList<Entity> playerList = new ArrayList();
    ArrayList<Entity> notAlwaysInRadius = new ArrayList();
    HashMap<Entity, Double> xHash = new HashMap();
    HashMap<Entity, Double> zHash = new HashMap();
    private boolean botCheck;
    private boolean nextPlayer;

    public AntiBot() {
        super("AntiBot", "\u0414\u043e\u0431\u0430\u0432\u043b\u044f\u0435\u0442 \u0441\u0443\u0449\u043d\u043e\u0441\u0442\u0435\u0439 \u0437\u0430\u0441\u043f\u0430\u0432\u043d\u0435\u043d\u044b\u0445 \u0430\u043d\u0442\u0438\u0447\u0438\u0442\u043e\u043c \u0432 \u0431\u043b\u044d\u043a-\u043b\u0438\u0441\u0442", Type.Combat);
        mode = new ListSetting("AntiBot Mode", "Matrix New", () -> true, "Matrix New", "Matrix", "Wellmore", "Jartex", "Need Hit");
        botTicks = new NumberSetting("Bot Ticks", 20.0f, 5.0f, 100.0f, 5.0f, () -> AntiBot.mode.currentMode.equals("Matrix New"));
        this.removeFromWorld = new BooleanSetting("Remove from world", false, () -> AntiBot.mode.currentMode.equals("Matrix New") || AntiBot.mode.currentMode.equals("Matrix"));
        this.addSettings(mode, botTicks, this.removeFromWorld);
    }

    private boolean isDuplicated(Entity entity) {
        if (entity == null) {
            return false;
        }
        return AntiBot.mc.player.connection.getPlayerInfoMap().stream().filter(networkPlayer -> entity.getDisplayName().getUnformattedText().equals(PaletteHelper.stripColor(EntityHelper.getName(networkPlayer)))).count() > 1L;
    }

    private boolean isBot(Entity entity, double speed) {
        boolean wellmore = true;
        return speed > 7.5 && AntiBot.mc.player.getDistanceToEntity(entity) <= 5.0f && entity.getCustomNameTag().length() < 2 && this.checkPosition(entity.posY, AntiBot.mc.player.posY - 2.0, AntiBot.mc.player.posY + 2.0) && EntityHelper.checkArmor(entity) && (AntiBot.mc.getCurrentServerData().serverIP.toLowerCase().contains("wellmore") || AntiBot.mc.getCurrentServerData().serverIP.toLowerCase().contains("mstnw") ? wellmore && ((EntityLivingBase)entity).getTotalArmorValue() < 20 : this.isDuplicated(entity));
    }

    private boolean checkPosition(double pos1, double pos2, double pos3) {
        return pos1 <= pos3 && pos1 >= pos2;
    }

    @Override
    public void onDisable() {
        isBotPlayer.clear();
        isRealPlayer.clear();
        super.onDisable();
    }

    @Override
    public void onEnable() {
        this.nextPlayer = false;
        super.onEnable();
    }

    @EventTarget
    public void onUpdatePost(EventUpdate event) {
        String antiBotMode = mode.getOptions();
        if (antiBotMode.equalsIgnoreCase("Matrix")) {
            if (this.notAlwaysInRadius.size() > 10000) {
                this.notAlwaysInRadius.clear();
            }
            for (Entity entity : AntiBot.mc.world.loadedEntityList) {
                if (entity == null) {
                    return;
                }
                if (!(entity instanceof EntityPlayer) || !(AntiBot.mc.player.getDistanceToEntity(entity) > 16.0f) && this.checkPosition(entity.posY, AntiBot.mc.player.posY - 3.0, AntiBot.mc.player.posY + 3.0) || this.notAlwaysInRadius.contains(entity)) continue;
                this.notAlwaysInRadius.add(entity);
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        block13: {
            String antiBotMode;
            block14: {
                block12: {
                    antiBotMode = mode.getOptions();
                    this.setSuffix(antiBotMode);
                    if (!antiBotMode.equalsIgnoreCase("Matrix")) break block12;
                    if (!this.nextPlayer) {
                        for (Entity entity : AntiBot.mc.world.loadedEntityList) {
                            if (entity == null) {
                                return;
                            }
                            if (!(entity instanceof EntityPlayer) || this.notAlwaysInRadius.contains(entity)) continue;
                            this.playerList.add(entity);
                            this.xHash.put(entity, entity.posX);
                            this.zHash.put(entity, entity.posZ);
                        }
                    } else {
                        for (Entity entity : this.playerList) {
                            double zDiff;
                            if (entity == null) {
                                return;
                            }
                            double xDiff = entity.posX - this.xHash.get(entity);
                            double speed = Math.sqrt(xDiff * xDiff + (zDiff = entity.posZ - this.zHash.get(entity)) * zDiff) * 10.0;
                            if (!this.isBot(entity, speed) || entity == AntiBot.mc.player) continue;
                            if (this.removeFromWorld.getCurrentValue()) {
                                AntiBot.mc.world.removeEntity(entity);
                                continue;
                            }
                            isBotPlayer.add(entity);
                        }
                        this.playerList.clear();
                        this.xHash.clear();
                        this.zHash.clear();
                    }
                    this.nextPlayer = !this.nextPlayer;
                    break block13;
                }
                if (!antiBotMode.equalsIgnoreCase("Matrix New")) break block14;
                for (Entity target : AntiBot.mc.world.loadedEntityList) {
                    if ((float)target.ticksExisted >= botTicks.getCurrentValue() || !(target instanceof EntityOtherPlayerMP) || ((EntityOtherPlayerMP)target).getHealth() >= 20.0f || MovementHelper.getEntityDirection((EntityLivingBase)target) == MovementHelper.getPlayerDirection() || target.isDead || ((EntityOtherPlayerMP)target).hurtTime <= 0 || !this.checkPosition(target.posY, AntiBot.mc.player.posY - 3.0, AntiBot.mc.player.posY + 3.0) || ((EntityOtherPlayerMP)target).getTotalArmorValue() != 0 || !(AntiBot.mc.player.getDistanceToEntity(target) <= 25.0f) || AntiBot.mc.player.connection.getPlayerInfo(target.getUniqueID()).getResponseTime() == 0 || AntiBot.mc.player.connection.getPlayerInfo(target.getUniqueID()).getResponseTime() > 40 || ((EntityOtherPlayerMP)target).getLastDamageSource() != null) continue;
                    if (this.removeFromWorld.getCurrentValue()) {
                        AntiBot.mc.world.removeEntity(target);
                        NotificationManager.publicity("AntiBot", "Bot " + target.getName() + " removed from the world!", 4, NotificationType.SUCCESS);
                        continue;
                    }
                    isBotPlayer.add(target);
                }
                break block13;
            }
            if (!antiBotMode.equalsIgnoreCase("Wellmore")) break block13;
            if (!AntiBot.mc.getCurrentServerData().serverIP.contains("wellmore")) {
                return;
            }
            for (Entity target : AntiBot.mc.world.loadedEntityList) {
                if (target == null || !(target instanceof EntityPlayer)) continue;
                if (target.isInvisible() && target != AntiBot.mc.player) {
                    AntiBot.mc.world.removeEntity(target);
                }
                float health = ((EntityPlayer)target).getHealth();
                if (target.ticksExisted >= 1 || String.valueOf(health).length() <= 3 || target == AntiBot.mc.player || !(AntiBot.mc.player.getDistanceToEntity(target) <= 15.0f)) continue;
                AntiBot.mc.world.removeEntity(target);
            }
        }
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        String antibotMode = mode.getCurrentMode();
        if (antibotMode.equalsIgnoreCase("Jartex")) {
            if (event.getPacket() instanceof SPacketServerDifficulty) {
                this.botCheck = false;
            }
            SPacketPlayerListItem packetPlayerListItem = (SPacketPlayerListItem)event.getPacket();
            if (AntiBot.mc.player != null && event.getPacket() instanceof SPacketPlayerListItem && packetPlayerListItem.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
                String name = packetPlayerListItem.getEntries().get(0).getProfile().getName();
                if (!this.botCheck) {
                    this.botCheck = name.equals(AntiBot.mc.player.getName());
                } else if (!AntiBot.mc.player.isSpectator() && !AntiBot.mc.player.capabilities.allowFlying) {
                    for (SPacketPlayerListItem.AddPlayerData ignored : ((SPacketPlayerListItem)event.getPacket()).getEntries()) {
                        if (ignored.getGameMode().equals("NOT_SET") || ignored.getPing() == 0) continue;
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventTarget
    public void onMouse(EventAttackClient event) {
        if (!this.getState()) {
            return;
        }
        String antiBotMode = mode.getOptions();
        if (antiBotMode.equalsIgnoreCase("Need Hit")) {
            EntityPlayer entityPlayer = (EntityPlayer)AntiBot.mc.objectMouseOver.entityHit;
            String name = entityPlayer.getName();
            if (entityPlayer == null) {
                return;
            }
            if (Celestial.instance.friendManager.getFriends().contains(entityPlayer.getName())) {
                return;
            }
            if (isRealPlayer.contains(entityPlayer)) {
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + name + (Object)((Object)ChatFormatting.WHITE) + " Already in AntiBot-List!");
            } else {
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + name + (Object)((Object)ChatFormatting.WHITE) + " Was added in AntiBot-List!");
            }
        }
    }

    static {
        isBotPlayer = new ArrayList<Entity>();
    }
}

