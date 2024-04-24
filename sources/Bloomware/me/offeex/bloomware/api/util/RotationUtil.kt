package me.offeex.bloomware.api.util

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.RotationManager
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

object RotationUtil {

    fun shouldRotate() = cPlayer.run {
        forwardSpeed == 0f && sidewaysSpeed == 0f && !input.jumping && !input.sneaking && RotationManager.packetYaw != RotationManager.targetYaw && RotationManager.packetPitch != RotationManager.targetPitch && age % 4 == 0
    }

    fun yawToPos(pos: Vec3d) =
        abs(MathHelper.wrapDegrees(getLookYaw(pos) - MathHelper.wrapDegrees(cPlayer.yaw)))

    fun movementVectorToYaw(): Float {
        val forward = cPlayer.input.movementForward.toDouble()
        val side = cPlayer.input.movementSideways.toDouble()
        return MathHelper.wrapDegrees(Math.toDegrees(-atan2(side, forward)).toFloat())
    }

    fun getLookYaw(target: Vec3d): Float {
        val dx: Double = target.getX() - cPlayer.eyePos.x
        val dz: Double = target.getZ() - cPlayer.eyePos.z
        return MathHelper.wrapDegrees((MathHelper.atan2(dz, dx) * 57.2957763671875).toFloat() - 90.0f)
    }

    fun getLookPitch(target: Vec3d): Float {
        val dx: Double = target.getX() + 0.5 - cPlayer.eyePos.x
        val dy: Double = target.getY() - cPlayer.eyePos.y
        val dz: Double = target.getZ() + 0.5 - cPlayer.eyePos.z
        val distance = sqrt(dx * dx + dz * dz)
        return MathHelper.wrapDegrees((-(MathHelper.atan2(dy, distance) * 57.2957763671875)).toFloat())
    }


}