package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventVelocity

object Velocity : Module("Velocity", "Changes your velocity.", Category.MOTION) {
    private val hor = setting("Horizontal").number(0, 0, 1, 0.01)
    private val vert = setting("Vertical").number(0, 0, 1, 0.01)

    private val fluids = setting("Fluids").number(0, 0, 1, 0.01)

    private val explosions = setting("ExplosionMult").group(false)
    private val explosionHor = explosions.setting("Horizontal").number(0, 0, 1, 0.01)
    private val explosionVert = explosions.setting("Vertical").number(0, 0, 1, 0.01)

    private val push = setting("Push").group()
    private val pushEntities = push.setting("Entities").number(0, 0, 1, 0.01)
    private val pushBlocks = push.setting("Blocks").bool()

    @Subscribe
    private fun onVelocity(event: EventVelocity) {
        when (event) {
            is EventVelocity.Player -> onPlayerVelocity(event)
            is EventVelocity.Explosion -> onExplosionVelocity(event)
            is EventVelocity.Fluid -> onFluidVelocity(event)
            is EventVelocity.Push -> onPlayerPush(event)
        }
    }

    private fun onPlayerVelocity(event: EventVelocity.Player) {
        val pv = event.player.velocity
        event.x = pv.x + event.x * hor.value
        event.y = pv.y + event.y * vert.value
        event.z = pv.z + event.z * hor.value
    }

    private fun onExplosionVelocity(event: EventVelocity.Explosion) {
        event.x *= explosionHor.value
        event.y *= explosionVert.value
        event.z *= explosionHor.value
    }

    private fun onFluidVelocity(event: EventVelocity.Fluid) {
        event.x *= fluids.value
        event.z *= fluids.value
    }

    private fun onPlayerPush(event: EventVelocity.Push) {
        if (event is EventVelocity.Push.Entities) {
            event.x *= pushEntities.value
            event.z *= pushEntities.value
        } else if (event is EventVelocity.Push.Blocks && pushBlocks.toggled) {
			event.canceled = true
		}
    }
}