package org.dreamcore.client.feature.impl.combat;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.input.StringHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.ListSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AntiBot extends Feature {

    public static List<Entity> isBotPlayer = new ArrayList<>();

    public ListSetting antiBotMode = new ListSetting("Anti Bot Mode", "Matrix", () -> true, "Matrix", "Custom", "Reflex");

    public BooleanSetting checkPing = new BooleanSetting("Check Ping", false, () -> antiBotMode.currentMode.equals("Custom"));
    public BooleanSetting checkEntityId = new BooleanSetting("Check Entity Id", false, () -> antiBotMode.currentMode.equals("Custom"));
    public BooleanSetting checkDuplicate = new BooleanSetting("Check Duplicate", false, () -> antiBotMode.currentMode.equals("Custom"));
    public BooleanSetting checkDuplicateTab = new BooleanSetting("Check Duplicate Tab", false, () -> antiBotMode.currentMode.equals("Custom"));
    public BooleanSetting checkInvisible = new BooleanSetting("Check Invisible", false, () -> antiBotMode.currentMode.equals("Custom"));
    public BooleanSetting checkNameBot = new BooleanSetting("Check Name", false, () -> antiBotMode.currentMode.equals("Custom"));
    public BooleanSetting checkDistance = new BooleanSetting("Check Distance", false, () -> antiBotMode.currentMode.equals("Custom"));
    public NumberSetting distance = new NumberSetting("Distance", 25, 0, 100, 0.1F, () -> antiBotMode.currentMode.equals("Custom") && checkDistance.getBoolValue());
    public BooleanSetting checkHurtTime = new BooleanSetting("Check HurtTime", false, () -> antiBotMode.currentMode.equals("Custom"));
    public NumberSetting hurtTime = new NumberSetting("HurtTime", 0, -10, 10, 0.1F, () -> antiBotMode.currentMode.equals("Custom") && checkHurtTime.getBoolValue());
    public BooleanSetting checkOnGround = new BooleanSetting("Check onGround", false, () -> antiBotMode.currentMode.equals("Custom"));
    public BooleanSetting checkTick = new BooleanSetting("Check Tick", false, () -> antiBotMode.currentMode.equals("Custom"));
    public NumberSetting ticks = new NumberSetting("Tick", 5, 0, 10000, 1, () -> antiBotMode.currentMode.equals("Custom") && checkTick.getBoolValue());

    public AntiBot() {
        super("AntiBot", "Добавляет сущностей заспавненых античитом в блэк-лист", Type.Combat);
        addSettings(antiBotMode, checkPing, checkEntityId, checkDuplicate, checkDuplicateTab, checkInvisible, checkNameBot, checkDistance, distance, checkHurtTime, hurtTime, checkOnGround, checkTick, ticks);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        for (Entity entity : mc.world.loadedEntityList) {
            switch (antiBotMode.currentMode) {
                case "Matrix":
                    if (entity != mc.player && entity.ticksExisted < 5 && entity instanceof EntityOtherPlayerMP) {
                        if (((EntityOtherPlayerMP) entity).hurtTime > 0 && mc.player.getDistanceToEntity(entity) <= 25 && mc.player.connection.getPlayerInfo(entity.getUniqueID()).getResponseTime() != 0 && entity.onGround) {
                            isBotPlayer.add(entity);
                        }
                    }
                    break;
                case "Custom":
                    if (isBot(entity)) {
                        isBotPlayer.add(entity);
                    }
                    break;
                case "Reflex":
                    if (entity.ticksExisted >= 5 || !entity.onGround || !(mc.player.getDistanceToEntity(entity) <= 25) || entity.getName().contains(mc.player.getName())) {
                        continue;
                    }
                    //    if(entity.ticksExisted < 3 && !entity.onGround && ((EntityOtherPlayerMP) entity).getHealth() > ((EntityOtherPlayerMP) entity).getMaxHealth() || (entity.ticksExisted < 3 && entity.isInvisible())){
                    isBotPlayer.add(entity);
                    //}
            }
        }
    }

    public boolean isBot(Entity entity) {

        if (checkPing.getBoolValue()) {
            if (entity instanceof EntityPlayer) {
                UUID id = entity.getUniqueID();
                NetworkPlayerInfo info = mc.player.connection.getPlayerInfo(id);
                if (info.getResponseTime() == 0) {
                    return true;
                }
            }
        }

        if (checkDistance.getBoolValue()) {
            if (mc.player.getDistanceToEntity(entity) <= distance.getNumberValue()) {
                return true;
            }
        }

        if (checkHurtTime.getBoolValue()) {
            if (((EntityPlayer) entity).hurtTime >= hurtTime.getNumberValue()) {
                return true;
            }
        }

        if (checkTick.getBoolValue()) {
            if (entity.ticksExisted >= ticks.getNumberValue()) {
                return true;
            }
        }

        if (checkOnGround.getBoolValue()) {
            if (entity.onGround) {
                return true;
            }
        } else {
            if (!entity.onGround) {
                return true;
            }
        }

        if (checkEntityId.getBoolValue()) {
            int id = entity.getEntityId();
            if (id <= -1 || id >= 1000000000) {
                return true;
            }
        }

        if (checkInvisible.getBoolValue()) {
            if (entity.isInvisible() && entity != mc.player) {
                return true;
            }
        }

        if (checkDuplicate.getBoolValue()) {
            if (mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityPlayer && entity instanceof EntityPlayer && entity.getName().equals(StringHelper.format(e.getName()))).count() > 1) {
                return true;
            }
        }

        if (checkDuplicateTab.getBoolValue()) {
            if (mc.player.connection.getPlayerInfoMap().stream().filter(entityPlayer -> entity instanceof EntityPlayer && entity.getName().equals(StringHelper.format(entityPlayer.getGameProfile().getName()))).count() > 1) {
                return true;
            }
        }
        return entity.getName().isEmpty() || entity.getName().equals(mc.player.getName());
    }

}
