package me.offeex.bloomware.client.module.visuals

import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.client.setting.settings.SettingColor
import me.offeex.bloomware.client.setting.settings.SettingGroup
import me.offeex.bloomware.client.setting.settings.SettingNumber
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventParticle
import me.offeex.bloomware.mixin.accessor.IParticle
import net.minecraft.client.particle.*
import net.minecraft.util.math.MathHelper
import java.util.*

object Particles : Module("Particles", "Customizable totem pop particles", Category.RENDER) {
	/**
	 * For amount [MixinEmitterParticle.tick]
	 */
	private val totem = setting("Totem").group(true)
	val tAmount = totem.setting("Amount").number(16.0, 1.0, 100.0, 1.0)
	private val tVelocity = totem.setting("Velocity").group()
	private val tVelocityX = tVelocity.setting("MultiplyX").number(1.0, 0.0, 5.0, 0.1)
	private val tVelocityY = tVelocity.setting("MultiplyY").number(1.0, 0.0, 5.0, 0.1)
	private val tVelocityZ = tVelocity.setting("MultiplyZ").number(1.0, 0.0, 5.0, 0.1)
	private val tGravity = tVelocity.setting("Gravity").number(1.0, -2.0, 2.0, 0.1)
	private val tMaxTime = totem.setting("Lifetime").group()
	private val tMaxTimeValue = tMaxTime.setting("Value").number(3.0, 0.0, 10.0, 0.1)
	private val tMaxTimeDiff = tMaxTime.setting("Difference").number(1.0, 0.1, 5.0, 0.1)
	private val tColor = totem.setting("Color").group()
	private val tColorValue = tColor.setting("Value").color(140, 245, 165)
	private val tColorDiff = tColor.setting("Difference").number(1.0, 1.0, 127.0, 1.0)
	private val tSize = totem.setting("Size").group()
	private val tSizeValue = tSize.setting("Value").number(1.0, 0.1, 3.0, 0.1)
	private val tSizeDiff = tSize.setting("Difference").number(0.5, 0.0, 1.5, 0.1)

	private val blockDust = setting("BlockDust").group(true)
	val bdAmount = blockDust.setting("Amount").number(16.0, 1.0, 100.0, 1.0)
	private val bdVelocity = blockDust.setting("Velocity").group()
	private val bdVelocityX = bdVelocity.setting("MultiplyX").number(1.0, 0.0, 5.0, 0.1)
	private val bdVelocityY = bdVelocity.setting("MultiplyY").number(1.0, 0.0, 5.0, 0.1)
	private val bdVelocityZ = bdVelocity.setting("MultiplyZ").number(1.0, 0.0, 5.0, 0.1)
	private val bdGravity = bdVelocity.setting("Gravity").number(1.0, -2.0, 2.0, 0.1)
	private val bdMaxTime = blockDust.setting("Lifetime").group()
	private val bdMaxTimeValue = bdMaxTime.setting("Value").number(3.0, 0.0, 10.0, 0.1)
	private val bdMaxTimeDiff = bdMaxTime.setting("Difference").number(1.0, 0.1, 5.0, 0.1)
	private val bdColor = blockDust.setting("Color").group()
	private val bdColorValue = bdColor.setting("Value").color(140, 245, 165)
	private val bdColorDiff = bdColor.setting("Difference").number(1.0, 1.0, 127.0, 1.0)
	private val bdSize = blockDust.setting("Size").group()
	private val bdSizeValue = bdSize.setting("Value").number(1.0, 0.1, 3.0, 0.1)
	private val bdSizeDiff = bdSize.setting("Difference").number(0.5, 0.0, 1.5, 0.1)

	/**
	 * For amount [MixinLivingEntity.spawnConsumptionEffects]
	 */
	val crack = setting("Crack").group(true)
	val cAmount = crack.setting("Amount").number(16.0, 1.0, 100.0, 1.0)
	private val cVelocity = crack.setting("Velocity").group()
	private val cVelocityX = cVelocity.setting("MultiplyX").number(1.0, 0.0, 5.0, 0.1)
	private val cVelocityY = cVelocity.setting("MultiplyY").number(1.0, 0.0, 5.0, 0.1)
	private val cVelocityZ = cVelocity.setting("MultiplyZ").number(1.0, 0.0, 5.0, 0.1)
	private val cGravity = cVelocity.setting("Gravity").number(1.0, -2.0, 2.0, 0.1)
	private val cMaxTime = crack.setting("Lifetime").group()
	private val cMaxTimeValue = cMaxTime.setting("Value").number(3.0, 0.0, 10.0, 0.1)
	private val cMaxTimeDiff = cMaxTime.setting("Difference").number(1.0, 0.1, 5.0, 0.1)
	private val cColor = crack.setting("Color").group()
	private val cColorValue = cColor.setting("Value").color(140, 245, 165)
	private val cColorDiff = cColor.setting("Difference").number(1.0, 1.0, 127.0, 1.0)
	private val cSize = crack.setting("Size").group()
	private val cSizeValue = cSize.setting("Value").number(1.0, 0.1, 3.0, 0.1)
	private val cSizeDiff = cSize.setting("Difference").number(0.5, 0.0, 1.5, 0.1)

	/**
	 * For amount [MixinExplosionEmitterParticle.tick]
	 */
	private val explosions = setting("Explosions").group(true)
	val eAmount = explosions.setting("Amount").number(6.0, 1.0, 50.0, 1.0)
	private val eVelocity = explosions.setting("Velocity").group()
	private val eVelocityX = eVelocity.setting("MultiplyX").number(1.0, 0.0, 5.0, 0.1)
	private val eVelocityY = eVelocity.setting("MultiplyY").number(1.0, 0.0, 5.0, 0.1)
	private val eVelocityZ = eVelocity.setting("MultiplyZ").number(1.0, 0.0, 5.0, 0.1)
	private val eGravity = eVelocity.setting("Gravity").number(1.0, -2.0, 2.0, 0.1)
	private val eMaxTime = explosions.setting("Lifetime").group()
	private val eMaxTimeValue = eMaxTime.setting("Value").number(3.0, 0.0, 10.0, 0.1)
	private val eMaxTimeDiff = eMaxTime.setting("Difference").number(1.0, 0.1, 5.0, 0.1)
	private val eColor = explosions.setting("Color").group()
	private val eColorValue = eColor.setting("Value").color(140, 245, 165)
	private val eColorDiff = eColor.setting("Difference").number(1.0, 1.0, 127.0, 1.0)
	private val eSize = explosions.setting("Size").group()
	private val eSizeValue = eSize.setting("Value").number(1.0, 0.1, 3.0, 0.1)
	private val eSizeDiff = eSize.setting("Difference").number(0.5, 0.0, 1.5, 0.1)

	@Subscribe
	private fun onParticle(event: EventParticle) {
		val group = findGroup(event.particle)
		if (group != null) modify(group, event)
	}

	private fun modify(sg: SettingGroup, event: EventParticle) {
		if (sg.toggled) {
			val list = sg.settings.filterIsInstance<SettingGroup>()
			val v = list[0].settings
			val mt = list[1].settings
			val c = list[2].settings
			val s = list[3].settings
			val random = Random()
			event.particle.setVelocity(
				event.velocityX * (v[0] as SettingNumber).value,
				event.velocityY * (v[1] as SettingNumber).value,
				event.velocityZ * (v[2] as SettingNumber).value
			)
			(event.particle as IParticle).setGravityStrength((v[3] as SettingNumber).value.toFloat())
			event.particle.maxAge = (((mt[0] as SettingNumber).value + random.nextDouble(
				-(mt[1] as SettingNumber).value, (mt[1] as SettingNumber).value
			)) * 20).toInt()
			val rc =
				random.nextInt(-(c[1] as SettingNumber).value.toInt(), (c[1] as SettingNumber).value.toInt())
			event.particle.setColor(
				MathHelper.clamp(MathHelper.abs((c[0] as SettingColor).color.red + rc) / 255f, 0f, 1f),
				MathHelper.clamp(MathHelper.abs((c[0] as SettingColor).color.green + rc) / 255f, 0f, 1f),
				MathHelper.clamp(MathHelper.abs((c[0] as SettingColor).color.blue + rc) / 255f, 0f, 1f)
			)
			event.particle.scale(
				((s[0] as SettingNumber).value + random.nextDouble(
					-(s[1] as SettingNumber).value, (s[1] as SettingNumber).value
				)).toFloat()
			)
		}
	}

	private fun findGroup(particle: Particle): SettingGroup? {
		return when (particle) {
			is TotemParticle -> totem
			is BlockDustParticle -> blockDust
			is CrackParticle -> crack
			is ExplosionLargeParticle -> explosions
			else -> null
		}
	}
}