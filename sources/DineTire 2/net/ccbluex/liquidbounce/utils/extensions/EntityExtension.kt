/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package net.ccbluex.liquidbounce.utils.extensions

import net.ccbluex.liquidbounce.utils.ClientUtils.mc
import net.ccbluex.liquidbounce.utils.RenderUtils2.mc
import net.ccbluex.liquidbounce.utils.Rotation
import net.ccbluex.liquidbounce.utils.RotationUtils
import net.ccbluex.liquidbounce.utils.render.GLUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.DefaultPlayerSkin
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.ResourceLocation
import net.minecraft.util.Vec3
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk
import javax.vecmath.Vector3d
import kotlin.math.*

/**
 * Allows to get the distance between the current entity and [entity] from the nearest corner of the bounding box
 */


fun Entity.rayTrace(blockReachDistance: Double): MovingObjectPosition {
    return this.rayTrace(blockReachDistance, 1f)
}


fun EntityPlayer.getEyeVec3(): Vec3 {
    return Vec3(this.posX, this.entityBoundingBox.minY + this.getEyeHeight(), this.posZ)
}

val EntityLivingBase.renderHurtTime: Float
    get() = this.hurtTime - if (this.hurtTime != 0) { Minecraft.getMinecraft().timer.renderPartialTicks } else { 0f }

val EntityLivingBase.hurtPercent: Float
    get() = (this.renderHurtTime) / 10

val EntityLivingBase.skin: ResourceLocation // TODO: add special skin for mobs
    get() = if (this is EntityPlayer) { Minecraft.getMinecraft().netHandler.getPlayerInfo(this.uniqueID)?.locationSkin } else { null } ?: DefaultPlayerSkin.getDefaultSkinLegacy()

val EntityLivingBase.ping: Int
    get() = if (this is EntityPlayer) { Minecraft.getMinecraft().netHandler.getPlayerInfo(this.uniqueID)?.responseTime?.coerceAtLeast(0) } else { null } ?: -1

/**
 * Render entity position
 */
val Entity.renderPos: Vector3d
    get() {
        val x = GLUtils.interpolate(lastTickPosX, posX) - mc.renderManager.viewerPosX
        val y = GLUtils.interpolate(lastTickPosY, posY) - mc.renderManager.viewerPosY
        val z = GLUtils.interpolate(lastTickPosZ, posZ) - mc.renderManager.viewerPosZ

        return Vector3d(x, y, z)
    }

val Entity.renderBoundingBox: AxisAlignedBB
    get() {
        return this.entityBoundingBox
            .offset(-this.posX, -this.posY, -this.posZ)
            .offset(this.renderPos.x, this.renderPos.y, this.renderPos.z)
    }

/**
 * Gets render distance to [entity]
 */
fun Entity.renderDistanceTo(entity: Entity): Double {
    val fromPos = this.renderPos
    val toPos = entity.renderPos

    val x = fromPos.x - toPos.x
    val y = fromPos.y - toPos.y
    val z = fromPos.z - toPos.z

    return sqrt(x * x + y * y + z * z)
}

fun World.getEntitiesInRadius(entity: Entity, radius: Double = 16.0): List<Entity> {
    val box = entity.entityBoundingBox.expand(radius, radius, radius)
    val chunkMinX = floor(box.minX * 0.0625).toInt()
    val chunkMaxX = ceil(box.maxX * 0.0625).toInt()
    val chunkMinZ = floor(box.minZ * 0.0625).toInt()
    val chunkMaxZ = ceil(box.maxZ * 0.0625).toInt()

    val entities = mutableListOf<Entity>()
    (chunkMinX..chunkMaxX).forEach { x ->
        (chunkMinZ..chunkMaxZ)
                .asSequence()
                .map { z -> getChunkFromChunkCoords(x, z) }
                .filter(Chunk::isLoaded)
                .forEach { it.getEntitiesWithinAABBForEntity(entity, box, entities, null) }
    }
    return entities
}
