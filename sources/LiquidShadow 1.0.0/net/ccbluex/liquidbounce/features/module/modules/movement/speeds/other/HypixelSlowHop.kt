/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils

class HypixelSlowHop : SpeedMode("HypixelSlowHop") {
    override fun onMotion() {

    }

    override fun onUpdate() {
        val thePlayer = mc.thePlayer

        if (!MovementUtils.isMoving || thePlayer.isInWater || thePlayer.isInLava ||
            thePlayer.isOnLadder || thePlayer.isRiding) return

        if (thePlayer.onGround)
            thePlayer.jump();
        else {
            if (thePlayer.fallDistance <= 0.1)
                mc.timer.timerSpeed = 1.6f;
            else if (thePlayer.fallDistance < 1.3)
                mc.timer.timerSpeed = 0.7f;
            else
                mc.timer.timerSpeed = 1.1f;
        }

        //MovementUtils.strafe(0.28f)
    }

    override fun onMove(event: MoveEvent) {}

    override fun onDisable() {
        super.onDisable()
        mc.thePlayer!!.speedInAir = 0.02f
        mc.timer.timerSpeed = 1f
    }
}