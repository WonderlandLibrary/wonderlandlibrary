package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import me.offeex.bloomware.mixin.accessor.IPlayerMoveC2SPacket
import net.minecraft.block.FluidBlock
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.util.math.BlockPos

object NoFall : Module("NoFall", "Allows you not to take damage when you fall.", Category.MOTION) {
	private val fallDistance = setting("FallDistance").number(2.5, 2.5, 20.0, 0.5)
	private val mode = setting("Mode").enum("Normal", "Reverse")

	@Subscribe
	private fun onPacketSend(event: EventPacket.Send) {
		mc.player?.apply {
			if (event.packet !is PlayerMoveC2SPacket || isFallFlying || fallDistance <= this@NoFall.fallDistance.value) return@apply

			val packet = event.packet
			when (mode.selected) {
				"Normal" -> if (velocity.getY() < -0.6f) (packet as IPlayerMoveC2SPacket).setOnGround(true)
				"Reverse" -> if (predict(
						blockPos, 3
					) && (!abilities.flying || mc.options.sneakKey.isPressed)
				) (packet as IPlayerMoveC2SPacket).setY((yaw + 2.0))
			}
		} ?: return
	}

	private fun predict(blockPos: BlockPos, down: Int): Boolean {
		var downCount = down
		while (downCount > 1) {
			val point = cWorld.getBlockState(blockPos.down(downCount))
			if (!point.isAir && point.block !is FluidBlock) return true
			downCount--
		}
		return false
	}
}