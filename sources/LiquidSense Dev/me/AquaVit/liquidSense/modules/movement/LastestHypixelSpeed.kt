package me.AquaVit.liquidSense.modules.movement

import net.ccbluex.liquidbounce.Gui.Notifications.Notificationsn
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventState
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.render.HUD
import net.ccbluex.liquidbounce.utils.ChatUtil
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue

@ModuleInfo(name = "LastetHypixelSpeed", description = "AquaVit!",
        category = ModuleCategory.MOVEMENT)
class LastestHypixelSpeed : Module() {
    var a = 0
    var speed = 0.0
    var lastDist = 0.0
    var yn = false
    val SpeedValue = IntegerValue("Speed", 0, 0, 20)
    val timerValue = FloatValue("Timer", 0f, -0.15f, 0.1f)
    val BoostValue = FloatValue("DamageBoost", 0f, 0f, 1f)
    var hd = LiquidBounce.moduleManager.getModule("HUD") as HUD
    override fun onEnable() {
        val sp = LiquidBounce.moduleManager.getModule("Stair") as Stair
        if (sp!!.state) {
            yn = true
            LiquidBounce.moduleManager.getModule("Stair")!!.state = false
            if(hd.no.get()) {
                ChatUtil.sendClientMessage("Debug Disable Stair", Notificationsn.Type.INFO)
            }
        }
        MovementUtils.SpeedSetMotion(MovementUtils.getBaseMoveSpeed() * 0.8)
        mc.timer.timerSpeed = 1f
    }

    override fun onDisable() {
        mc.timer.timerSpeed = 1f
        if (yn) {
            LiquidBounce.moduleManager.getModule("Stair")!!.state = true
            if(hd.no.get()) {
                ChatUtil.sendClientMessage("Debug Enable Stair", Notificationsn.Type.INFO)
            }
            yn = false
        }
    }
    @EventTarget
    fun onMotion(e: MotionEvent) {
        when (e.eventState) {
            EventState.PRE -> if (mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround) {
                mc.thePlayer.motionY = 1.0E-14
            }
            EventState.POST -> {
                val xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX
                val zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ
                lastDist = Math.sqrt(xDist * xDist + zDist * zDist)
            }
        }
    }

    @EventTarget
    fun onMove(event: MoveEvent) {
        val boost = true
        if (!MovementUtils.isMoving() || mc.thePlayer.onGround || a == 1 && mc.thePlayer.isCollidedVertically) {
            speed = MovementUtils.getBaseMoveSpeed()
        }
        if (mc.thePlayer.onGround && MovementUtils.isMoving() && a == 2) {
            if (boost) mc.timer.timerSpeed = 1.0f//可以削弱到1.0
            mc.thePlayer.jump()
            mc.thePlayer.motionY = 0.4001999986886975 + MovementUtils.getJumpEffect() * 0.1
            event.y = mc.thePlayer.motionY
            speed *= 1.5 + SpeedValue.get() / 100
            if (boost) mc.timer.timerSpeed = 1.55f + timerValue.get()//可以削弱到1.0
        } else if (a == 3) {
            val diff = 0.66 * (lastDist - MovementUtils.getBaseMoveSpeed())
            speed = lastDist - diff
            if (boost) mc.timer.timerSpeed = 1.45f + timerValue.get()//可以削弱到1.0
        } else {
            if (a <= 6 && event.y > 0.4) {
                event.y = getMotion(event, a - 4)
            }
            if (boost) mc.timer.timerSpeed = 1.0f
            val collidingList: List<*> = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.entityBoundingBox.offset(0.0, mc.thePlayer.motionY, 0.0))
            if ((collidingList.size > 0 || mc.thePlayer.isCollidedVertically || mc.thePlayer.isCollidedHorizontally) && a > 0) {
                a = if (MovementUtils.isMoving()) 1 else 0
            }
            speed = lastDist - lastDist / 159.0
        }
        speed = Math.max(speed, MovementUtils.getBaseMoveSpeed())
        if (a > 0 && MovementUtils.isMoving()) {
            MovementUtils.setMotion(event, speed * (1 + mc.thePlayer.hurtTime * BoostValue.get() / 20))
        }
        ++a
    }

    fun getMotion(event: MoveEvent, stage: Int): Double {
        if (stage == 0) {
            return event.y * 0.99
        } else if (stage == 1) {
            return event.y * 0.985
        } else if (stage == 2) {
            return event.y * 0.98
        }
        return event.y
    }

}




























