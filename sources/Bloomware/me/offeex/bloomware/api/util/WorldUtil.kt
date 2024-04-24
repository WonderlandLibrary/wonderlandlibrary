package me.offeex.bloomware.api.util

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.manager.managers.FriendManager
import me.offeex.bloomware.api.manager.managers.FriendManager.getType
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.util.EntityUtil.getFullHealth
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.AmbientEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.mob.WaterCreatureEntity
import net.minecraft.entity.passive.*
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.WorldChunk
import java.util.*

object WorldUtil {
	private val loadedChunks: List<WorldChunk>
		get() {
			val chunks = mutableListOf<WorldChunk>()
			val viewDist = mc.options.viewDistance.value
			for (x in -viewDist..viewDist) for (z in -viewDist..viewDist) chunks.add(
				cWorld.chunkManager.getWorldChunk(
					cPlayer.x.toInt() / 16 + x, cPlayer.z.toInt() / 16 + z
				) ?: continue
			)
			return chunks
		}

	val blockEntities: List<BlockEntity>
		get() = loadedChunks.flatMap { it.blockEntities.values }

	val dimension: Int
		get() {
			val dim = cPlayer.getWorld().registryKey.value.path
			return if (dim.contains("end")) 2 else if (dim.contains("nether")) 1 else 0
		}

	fun target(
		range: Double,
		sort: String,
		players: Boolean,
		friends: Boolean,
		hostiles: Boolean,
		animals: Boolean,
	): LivingEntity? {
		var last: LivingEntity? = null
		var minDist = range
		var minHp = Float.MAX_VALUE
		for (e in cWorld.entities) {
			if (cPlayer.distanceTo(e) > range) continue

//            EntityTypes sort
			if (!e.isLiving || !e.isAlive || !e.isAttackable || e === mc.player) continue
			if (e.isPlayer) {
				if (!players) continue else if (getType(e.entityName) != null && getType(e.entityName) === FriendManager.PersonType.FRIEND && !friends) continue
			} else if (e is Monster && !hostiles) continue else if ((e is PassiveEntity || e is WaterCreatureEntity || e is AmbientEntity || e is SnowGolemEntity || e is IronGolemEntity) && !animals) continue
			when (sort) {
				"Distance" -> {
					if (cPlayer.distanceTo(e) < minDist) {
						minDist = cPlayer.distanceTo(e).toDouble()
						minHp = Float.MAX_VALUE
						last = e as LivingEntity
					}
				}

				"Health" -> {
					if ((e as LivingEntity).getFullHealth() <= minHp) {
						minHp = e.getFullHealth()
						minDist = range
						last = e
					}
				}
			}
		}
		return last
	}

	inline fun forEachBlock(radius: Double, callback: (Int, Int, Int) -> Unit) {
		val r = radius.toInt()
		for (x in -r..r) for (y in -r..r) for (z in -r..r) {
			val pos = cPlayer.blockPos.add(x, y, z)

			if (pos.isWithinDistance(cPlayer.eyePos, radius)) {
				callback(pos.x, pos.y, pos.z)
			}
		}
	}

	inline fun Chunk.forEachBlock(callback: (Int, Int, Int) -> Unit) {
		for (x in pos.startX..pos.endX) for (y in bottomY..topY) for (z in pos.startZ..pos.endZ)
			callback(x, y, z)
	}
}