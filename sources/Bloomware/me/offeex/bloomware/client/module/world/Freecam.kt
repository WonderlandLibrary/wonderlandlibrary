package me.offeex.bloomware.client.module.world

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.util.MovementUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventCamera
import me.offeex.bloomware.event.events.EventEntrypoint
import me.offeex.bloomware.event.events.EventInput
import me.offeex.bloomware.event.events.EventUpdate
import net.minecraft.client.input.KeyboardInput
import net.minecraft.client.option.Perspective
import net.minecraft.util.math.Vec3d

object Freecam : Module("Freecam", "Allows you fly like spectator.", Category.WORLD) {
    private val speed = setting("Speed").number(1.0, 0.01, 1.0, 0.01)

    private var oldPerspective: Perspective? = null
    private var previousPos = Vec3d.ZERO
    private var currentPos = Vec3d.ZERO
    private lateinit var rotations: Pair<Float, Float>

    override fun onEnable() {
        oldPerspective = mc.options.perspective
        mc.options.perspective = Perspective.THIRD_PERSON_BACK

        val cam = mc.gameRenderer.camera
        rotations = cam.yaw to cam.pitch
        currentPos = cam.pos
        previousPos = currentPos

        mc.chunkCullingEnabled = false
    }

    override fun onDisable() {
        mc.options.perspective = oldPerspective
        mc.chunkCullingEnabled = true
    }

    @Subscribe
    private fun onStop(event: EventEntrypoint.Stop) {
        disable()
    }

    @Subscribe
    private fun onUpdate(event: EventUpdate) {
        when (event) {
            is EventUpdate.Perspective -> event.perspective = Perspective.THIRD_PERSON_BACK
            is EventUpdate.Rotation.Both -> {
                rotations = rotations.first + event.yawDelta to rotations.second + event.pitchDelta
                event.canceled = true
            }
        }
    }

    @Subscribe
    private fun onCamera(event: EventCamera) {
        when (event) {
            is EventCamera.Position -> {
                val lerped = previousPos.lerp(currentPos, mc.tickDelta.toDouble())
                event.x = lerped.x
                event.y = lerped.y
                event.z = lerped.z
            }

            is EventCamera.Rotation -> {
                event.yaw = rotations.first
                event.pitch = rotations.second
            }

            is EventCamera.ClipToSpace -> event.cirValue = 0.0
        }
    }

    @Subscribe
    private fun onKeyBinding(event: EventInput.Movement) {
        CameraInput.tick(false, 1.0f)

        previousPos = currentPos
        currentPos = currentPos.add(MovementUtil.getMovementOnKey(speed.value, speed.value, rotations.first, CameraInput))

        event.input.movementForward = 0.0f
        event.input.movementSideways = 0.0f
        event.input.jumping = false
        event.input.sneaking = false
        event.canceled = true
    }

    private object CameraInput : KeyboardInput(mc.options)
}
