package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventMovement
import net.minecraft.util.math.Direction
import net.minecraft.world.RaycastContext

object FastFall : Module("FastFall", "Makes you fall faster", Category.MOTION) {
    private val height = setting("Height", "Sets height of falling").number(2, 0.1, 5, 0.1)
    private val speed = setting("Speed", "Sets speed of falling").number(0.5, 0.01, 5, 0.01)

    @Subscribe
    private fun onMove(event: EventMovement.Move) {
        if (!cPlayer.isOnGround || cPlayer.velocity.y > 0) return

        val pos = cPlayer.pos
        val downPos = pos.subtract(0.0, height.value + 1, 0.0)
        val ray = cWorld.raycast(RaycastContext(pos, downPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, cPlayer))

        val distance = pos.y - ray.pos.y
        if (distance <= height.value) {
            event.velocity = event.velocity.withAxis(Direction.Axis.Y, -speed.value)
        }
    }
}