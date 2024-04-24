package me.offeex.bloomware.api.util

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.ProtectionMark
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.client.module.world.Freecam
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.Difficulty
import net.minecraft.world.RaycastContext
import net.minecraft.world.explosion.Explosion
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

object MathUtil {
	fun roundDecimal(value: Double, power: Int) = (value * 10.0.pow(power)).roundToInt() / 10.0.pow(power)
	fun roundDecimal(value: Float, power: Int) = (value * 10.0.pow(power)).roundToInt() / 10.0.pow(power)

	fun getScale(entity: LivingEntity): Float {
		val distance =
			if (Freecam.enabled) mc.gameRenderer.camera.pos.distanceTo(entity.pos).toFloat()
			else cPlayer.distanceTo(entity)
		return if (distance <= 10) 1f else distance * 0.1f
	}

	fun getCrystalDamage(blockPos: BlockPos, entity: LivingEntity = cPlayer, ignoreTerrain: Boolean = false): Double {
		return getCrystalDamage(Vec3d(blockPos.x + 0.5, blockPos.y.toDouble(), blockPos.z + 0.5), entity, ignoreTerrain)
	}

	fun getCrystalDamage(pos: Vec3d, entity: LivingEntity = cPlayer, ignoreTerrain: Boolean = false): Double {
		var damage = 0.0f
		val doublePower = 12.0f
		val distanceFactor = sqrt(entity.squaredDistanceTo(pos)) / doublePower
		if (distanceFactor <= 1.0) {
			var distX = entity.x - pos.x
			var distY = entity.eyeY - pos.y
			var distZ = entity.z - pos.z
			val distLength = sqrt(distX * distX + distY * distY + distZ * distZ)
			if (distLength != 0.0) {
				distX /= distLength
				distY /= distLength
				distZ /= distLength
				val density =
					if (ignoreTerrain) getExposureIgnoreTerrain(pos, entity)
					else Explosion.getExposure(pos, entity)
				val exposureNormalized = (1.0 - distanceFactor) * density

				val initialDmg = ((exposureNormalized * exposureNormalized + exposureNormalized) / 2.0 * 7.0 * doublePower.toDouble() + 1.0).toInt().toFloat()

				damage = transformForDifficulty(initialDmg)
				damage = entity.applyArmorToDamage(cWorld.damageSources.explosion(null), damage)
				damage = entity.modifyAppliedDamage(cWorld.damageSources.explosion(null), damage)
			}
		}
		return damage.toDouble()
	}

	private fun getExposureIgnoreTerrain(source: Vec3d, entity: Entity): Float {
		val box = entity.boundingBox

		val d = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0)
		val e = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0)
		val f = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0)

		val g = (1.0 - floor(1.0 / d) * d) / 2.0
		val h = (1.0 - floor(1.0 / f) * f) / 2.0

		return if (d >= 0.0 && e >= 0.0 && f >= 0.0) {
			var i = 0
			var j = 0

			var k = 0.0
			while (k <= 1.0) {

				var l = 0.0
				while (l <= 1.0) {

					var m = 0.0
					while (m <= 1.0) {

						val n = MathHelper.lerp(k, box.minX, box.maxX)
						val o = MathHelper.lerp(l, box.minY, box.maxY)
						val p = MathHelper.lerp(m, box.minZ, box.maxZ)

						val vec3d = Vec3d(n + g, o, p + h)
						val ray = entity.world.raycast(RaycastContext(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity))
						val ignoreTerrain = cWorld.getBlockState(ray.blockPos).block.blastResistance < 600

						if (ray.type == HitResult.Type.MISS || ignoreTerrain) {
							++i
						}

						++j

						m += f
					}

					l += e
				}

				k += d
			}

			i.toFloat() / j.toFloat()

		} else 0.0f
	}

	private fun transformForDifficulty(f: Float): Float {
		var ff = f
		if (cWorld.difficulty == Difficulty.PEACEFUL) ff = 0.0f
		if (cWorld.difficulty == Difficulty.EASY) ff = (ff / 2.0f + 1.0f).coerceAtMost(ff)
		if (cWorld.difficulty == Difficulty.HARD) ff = ff * 3.0f / 2.0f
		return ff
	}

	fun toMS(value: Double) = value * 20
	fun toKmH(value: Double) = value * 20 * 3.6
	fun fromKmH(value: Double) = value * 0.05 / 3.6

	@ProtectionMark
	fun protection() {}
}