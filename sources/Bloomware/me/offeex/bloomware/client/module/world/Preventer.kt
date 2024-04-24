package me.offeex.bloomware.client.module.world

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket

object Preventer : Module("Preventer", "Allows you to prevent many thing from happening on client-side", Category.WORLD) {
	private val lags = setting("Lags").group()
	private val itemLag = lags.setting("Items").bool(true)
	private val particleLag = lags.setting("Particles").bool(true)

	private val notify = setting("Notify").bool(true)
	private val itemMaxLength = setting("MaxLength").number(28.0, 1.0, 100.0, 1.0)
	private val particleLimit = setting("ParticleLimit").number(100.0, 1.0, 1000.0, 1.0)

	private val effects = setting("Effects").group()
	private val levitation = effects.setting("Levitation").bool(true)
	private val blindness = effects.setting("Blindness").bool(true)
	private val nausea = effects.setting("Nausea").bool(true)
	private val miningFatigue = effects.setting("MiningFatigue").bool(true)
	private val slowFalling = effects.setting("SlowFalling").bool()

	override fun onTick() {
		if (levitation.toggled) cPlayer.removeStatusEffect(StatusEffects.LEVITATION)
		if (blindness.toggled) cPlayer.removeStatusEffect(StatusEffects.BLINDNESS)
		if (nausea.toggled) cPlayer.removeStatusEffect(StatusEffects.NAUSEA)
		if (miningFatigue.toggled) cPlayer.removeStatusEffect(StatusEffects.MINING_FATIGUE)
		if (slowFalling.toggled) cPlayer.removeStatusEffect(StatusEffects.SLOW_FALLING)

		if (!itemLag.toggled) return
		val stack = cPlayer.mainHandStack
		if (stack.hasCustomName() && stack.name.string.length >= itemMaxLength.value) {
			cPlayer.mainHandStack.removeCustomName()
			if (notify.toggled) addMessage("Found lag item! Fixing...")
		}
	}

	@Subscribe
	private fun onPacketReceive(event: EventPacket.Receive) {
		if (event.packet is ParticleS2CPacket && particleLag.toggled) {
			val packet = event.packet
			if (packet.count >= particleLimit.value) {
				event.canceled = true
				if (notify.toggled) addMessage("Received packet with " + packet.count + " particles! Canceling...")
			}
		}
	}
}