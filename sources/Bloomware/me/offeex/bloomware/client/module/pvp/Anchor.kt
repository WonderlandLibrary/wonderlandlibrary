package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.HoleManager
import me.offeex.bloomware.api.util.MovementUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.util.math.BlockPos

object Anchor : Module("Anchor", "Stops all motion when you are over a hole", Category.PVP) {
	private val autoDisable = setting("AutoDisable").bool(true)
	private val holeDistance = setting("HoleDistance").number(1.0, 1.0, 3.0, 1.0)

	override fun onTick() {
		if (isHole(cPlayer.blockPos) && cPlayer.isOnGround) {
			MovementUtil.center()
			cPlayer.setVelocity(0.0, 0.0, 0.0)
			if (autoDisable.toggled) disable()
		}
	}

	private fun isHole(pos: BlockPos): Boolean {
		var i = 1
		while (i <= holeDistance.value) {
			if (HoleManager.has(pos.down(i))) return true
			i++
		}
		return false
	}
}