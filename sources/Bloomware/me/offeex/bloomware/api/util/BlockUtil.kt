package me.offeex.bloomware.api.util

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.ExperienceOrbEntity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction

object BlockUtil {

    fun isPlaceable(pos: BlockPos, entityCheck: Boolean): Boolean {
        if (!cWorld.getBlockState(pos).material.isReplaceable) return false

        val entities = cWorld.getEntitiesByClass(Entity::class.java, Box(pos)) {
            it !is ExperienceBottleEntity && it !is ItemEntity && it !is ExperienceOrbEntity
        }

        return !entityCheck || entities.isEmpty()
    }

    fun findBlockSide(pos: BlockPos): Direction? {
        for (d in Direction.values())
            if (!cWorld.getBlockState(pos.offset(d)).material.isReplaceable)
                return d
        return null
    }

}