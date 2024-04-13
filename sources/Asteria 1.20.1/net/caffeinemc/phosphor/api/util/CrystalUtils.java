package net.caffeinemc.phosphor.api.util;

import net.caffeinemc.phosphor.api.event.events.AttackEntityEvent;
import net.caffeinemc.phosphor.api.event.events.EntityRemoveEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.event.orbit.EventPriority;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.HashMap;
import java.util.List;

import static net.caffeinemc.phosphor.common.Phosphor.mc;

public class CrystalUtils {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.target instanceof EndCrystalEntity crystal) {
            brokenCrystals.put(crystal, true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityRemove(EntityRemoveEvent event) {
        if (event.entity instanceof EndCrystalEntity crystal) {
            if (isCrystalBroken(crystal))
                brokenCrystals.remove(crystal);
        }
    }

    private static final HashMap<Entity, Boolean> brokenCrystals = new HashMap<>();

    public static boolean isCrystalBroken(Entity crystal) {
        return brokenCrystals.containsKey(crystal);
    }

    public static boolean canPlaceCrystalServer(BlockPos pos) {
        if (mc.world == null) return false;

        BlockState state = mc.world.getBlockState(pos);
        if (state.getBlock() != Blocks.OBSIDIAN && state.getBlock() != Blocks.BEDROCK) {
            return false;
        }

        BlockPos crystalPos = pos.up();
        BlockState crystalState = mc.world.getBlockState(crystalPos);
        if (!mc.world.isAir(crystalPos) || crystalState.getBlock() == Blocks.OBSIDIAN || crystalState.getBlock() == Blocks.BEDROCK) {
            return false;
        }

        Box box = new Box(crystalPos).offset(0.5, 0.0, 0.5).stretch(0.0, 2.0, 0.0);
        List<Entity> entities = mc.world.getEntitiesByClass(Entity.class, box, e -> !(e instanceof ClientPlayerEntity));
        for (Entity entity : entities) {
            if (entity instanceof EndCrystalEntity) {
                return false;
            }
        }
        return true;
    }
}
