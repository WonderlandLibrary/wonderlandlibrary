package net.caffeinemc.phosphor.api.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.caffeinemc.phosphor.common.Phosphor;
import net.caffeinemc.phosphor.module.modules.misc.TeamsModule;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.caffeinemc.phosphor.common.Phosphor.mc;

@SuppressWarnings("unchecked")
public class PlayerUtils {
    public static JsonObject friends = new JsonObject();

    public static boolean isFriend(PlayerEntity player) {
        return friends.get(player.getUuidAsString()) != null || Phosphor.moduleManager().getModule(TeamsModule.class).teammates.contains(player);
    }

    public static void addFriend(PlayerEntity player) {
        if (!isFriend(player))
            friends.add(player.getUuidAsString(), new JsonPrimitive(player.getName().getString()));
    }

    public static void removeFriend(PlayerEntity player) {
        if (isFriend(player))
            friends.remove(player.getUuidAsString());
    }

    public static PlayerEntity findNearestPlayer(PlayerEntity toPlayer, float range, boolean seeOnly) {
        float minRange = Float.MAX_VALUE;
        PlayerEntity minPlayer = null;

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (isFriend(player))
                continue;

            float distance = player.distanceTo(toPlayer);

            if (!player.getName().equals(toPlayer.getName()) &&
                    player != toPlayer &&
                    distance <= range &&
                    (!seeOnly || toPlayer.canSee(player))) {
                if (distance < minRange) {
                    minRange = distance;
                    minPlayer = player;
                }
            }
        }

        return minPlayer;
    }

    public static LivingEntity findNearestEntity(PlayerEntity toPlayer, float range, boolean seeOnly) {
        float minRange = Float.MAX_VALUE;
        LivingEntity minEntity = null;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity.isDead())
                    continue;

                if (entity instanceof PlayerEntity player && isFriend(player))
                    continue;

                float distance = entity.distanceTo(toPlayer);

                if (entity != toPlayer && distance <= range && (!seeOnly || toPlayer.canSee(entity))) {
                    if (distance < minRange) {
                        minRange = distance;
                        minEntity = livingEntity;
                    }
                }
            }
        }

        return minEntity;
    }

    public static <T extends Entity> List<T> findNearestEntities(Class<T> findEntity, PlayerEntity toPlayer, float range, boolean seeOnly) {
        List<T> entities = new ArrayList<>();

        for (Entity entity : mc.world.getEntities()) {
            if (!findEntity.isAssignableFrom(entity.getClass())) continue;

            if (entity instanceof PlayerEntity player && isFriend(player)) continue;

            float distance = entity.distanceTo(toPlayer);

            if (entity != toPlayer && distance <= range && (!seeOnly || toPlayer.canSee(entity))) {
                entities.add((T) entity);
            }
        }

        return entities;
    }

    @Nullable
    public static ItemStack findShield(PlayerEntity player) {
        ItemStack result = null;

        for (Hand hand : Hand.values()) {
            ItemStack handStack = player.getStackInHand(hand);
            if (handStack.isOf(Items.SHIELD)) {
                result = handStack;
            }
        }

        return result;
    }

    public static float getShieldCooldownProgress(PlayerEntity player) {
        ItemStack shieldStack = findShield(player);
        if (shieldStack == null)
            return 0.0f;

        return player.getItemCooldownManager().getCooldownProgress(shieldStack.getItem(), mc.getTickDelta());
    }

    public static float getItemCooldownProgress(PlayerEntity player, ItemStack itemStack) {
        return player.getItemCooldownManager().getCooldownProgress(itemStack.getItem(), mc.getTickDelta());
    }

    public static float getItemCooldownProgress(PlayerEntity player, Hand hand) {
        return player.getItemCooldownManager().getCooldownProgress(player.getStackInHand(hand).getItem(), mc.getTickDelta());
    }

    public static float getItemCooldownProgress(Hand hand) {
        return mc.player.getItemCooldownManager().getCooldownProgress(mc.player.getStackInHand(hand).getItem(), mc.getTickDelta());
    }

    public static void attackEntity(Entity entity) {
        mc.interactionManager.attackEntity(mc.player, entity);
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    public static GameMode getGameMode(PlayerEntity player) {
        if (player == null) return null;
        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (playerListEntry == null) return null;
        return playerListEntry.getGameMode();
    }
}
