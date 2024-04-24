package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.util.CPlayerUtil
import me.offeex.bloomware.api.util.RotationUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventSprint
import net.minecraft.util.math.MathHelper

object Sprint : Module("Sprint", "", Category.MOTION) {
    private val mode = setting("Mode").enum("Rage", "Legit")

    @Subscribe
    private fun onSprintForward(event: EventSprint.ForwardMovement) {
        // If we're in rage mode and we're moving in any direction, set sprinting to true
        if (mode.like("Rage")) event.has = CPlayerUtil.isMovingH
    }

    @Subscribe
    private fun onSprintJump(event: EventSprint.JumpAcceleration) {
        if (mode.like("Rage")) {
            // Retrieve our movement vector, normalize it and convert it to yaw
            event.yaw = RotationUtil.movementVectorToYaw() + MathHelper.wrapDegrees(cPlayer.yaw)
        }
    }

    // Overriding isSprinting field, so we can sprint without pressing W or CTRL
    override fun onTick() {
        val isMovementLegit = cPlayer.input.hasForwardMovement()
        if (isMovementLegit || mode.like("Rage")) cPlayer.isSprinting = true
    }
}