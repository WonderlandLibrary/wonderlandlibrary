package me.offeex.bloomware.api.util

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.api.helper.cPlayer
import net.minecraft.client.input.Input
import net.minecraft.entity.Entity
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.joml.Vector2d
import kotlin.math.*

object MovementUtil {
    fun center() {
        cPlayer.apply {
            val centerX = MathHelper.floor(x) + 0.5
            val centerZ = MathHelper.floor(z) + 0.5
            if (isSprinting) cNetHandler.sendPacket(ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING))
            updatePosition(centerX, y, centerZ)
            cNetHandler.sendPacket(PlayerMoveC2SPacket.PositionAndOnGround(centerX, y, centerZ, isOnGround))
        }
    }

    fun getMovementOnKey(hSpeed: Double, vSpeed: Double = hSpeed, yaw: Float = cPlayer.yaw, input: Input = cPlayer.input): Vec3d {
        return input.run {
            val forward = sign(movementForward)
            val strafe = sign(movementSideways)
            val vertical = if (jumping != sneaking) if (jumping) 1.0f else -1.0f else 0f

            var tweakedSpeed = hSpeed
            if (forward != 0f && strafe != 0f) tweakedSpeed *= 0.70710678118
            val yawRad = Math.toRadians(yaw + 90.0)

            Vec3d(
                tweakedSpeed * (forward * cos(yawRad) + strafe * sin(yawRad)),
                vSpeed * vertical,
                tweakedSpeed * (forward * sin(yawRad) - strafe * cos(yawRad))
            )
        }
    }

    fun getVelocityXZ(e: Entity) = abs(e.velocity.getX()) + abs(e.velocity.getZ())
    fun getVelocity(e: Entity) = getVelocityXZ(e) + abs(e.velocity.getY())

    // Checks if player is moving in direction of target
    fun inDirectionOf(target: Vec3d): Boolean {
        val pos = cPlayer.pos
        val prevPos = Vec3d(cPlayer.prevX, cPlayer.prevY, cPlayer.prevZ)
        return pos.squaredDistanceTo(target) < prevPos.squaredDistanceTo(target)
    }

    // Calculates knockback in same direction as player is moving, needed for anticheats
    fun kbInDir(velX: Double, velZ: Double): Double {
        val velocityX = if (sign(velX) == sign(cPlayer.velocity.x)) velX else 0.0
        val velocityZ = if (sign(velZ) == sign(cPlayer.velocity.z)) velZ else 0.0
        return hypot(velocityX, velocityZ)
    }
}