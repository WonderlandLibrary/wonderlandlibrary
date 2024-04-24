package me.offeex.bloomware.client.module.world

import com.mojang.authlib.GameProfile
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventWorld
import net.minecraft.client.network.OtherClientPlayerEntity
import java.util.*

object FakePlayer : Module("FakePlayer", "Summons a fake interact entity", Category.WORLD) {
	private lateinit var fakeman: OtherClientPlayerEntity

	override fun onEnable() {
		if (mc.world == null && mc.player == null) disable()
		fakeman = OtherClientPlayerEntity(
			mc.world, GameProfile(UUID.randomUUID(), "bozo")
		)
		fakeman.copyFrom(cPlayer)
		cWorld.addEntity(-100, fakeman)
	}

	override fun onDisable() = fakeman.discard()

	@Subscribe
	private fun onWorld(event: EventWorld) = disable()
}