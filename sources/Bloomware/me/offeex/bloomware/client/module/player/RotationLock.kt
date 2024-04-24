package me.offeex.bloomware.client.module.player

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventUpdate
import net.minecraft.util.math.MathHelper
import kotlin.math.round

object RotationLock : Module("RotationLock", "Blocks rotations of your head.", Category.PLAYER) {
    private val yawLock = setting("Yaw").bool()
    private val pitchLock = setting("Pitch").bool()
    private val mode = setting("Mode").enum("Round", "Concrete")
    private val yawRound = setting("YawRound").number(45, 15, 90, 15).depend(mode) { mode.like("Round") }
    private val pitchRound = setting("PitchRound").number(15, 15, 45, 15).depend(mode) { mode.like("Round") }
    private val yawConcrete = setting("YawConcrete").number(0, -180, 180, 1).depend(mode) { mode.like("Concrete") }
    private val pitchConcrete = setting("PitchConcrete").number(0, -90, 90, 1).depend(mode) { mode.like("Concrete") }

    override fun onEnable() {
        var newYaw = cPlayer.yaw
        var newPitch = cPlayer.pitch

        if (mode.like("Round")) {
            val yr = yawRound.value.toFloat()
            val pr = pitchRound.value.toFloat()
            newYaw = round(yaw / yr) * yr
            newPitch = round(pitch / pr) * pr
        } else if (mode.like("Concrete")) {
            newYaw = yawConcrete.value.toFloat()
            newPitch = pitchConcrete.value.toFloat()
        }

        if (yawLock.toggled) cPlayer.yaw = newYaw
        if (pitchLock.toggled) cPlayer.pitch = newPitch
    }

    @Subscribe
    private fun onUpdateRotation(event: EventUpdate.Rotation) {
        if (event is EventUpdate.Rotation.Pitch && pitchLock.toggled) event.delta = 0f
        if (event is EventUpdate.Rotation.Yaw && yawLock.toggled) event.delta = 0f
    }

    private val yaw
        get() = cPlayer.yaw

    private val pitch
        get() = MathHelper.clamp(-90f, cPlayer.pitch, 90f)
}