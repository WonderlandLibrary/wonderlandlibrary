package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.SessionManager
import me.offeex.bloomware.api.util.CPlayerUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventInteract
import net.minecraft.item.Items
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket
import net.minecraft.util.ActionResult
import net.minecraft.util.math.MathHelper

object TickShift : Module(
    "TickShift",
    "Accumulates and then releases burst of movement packets, hence giving you an advantage in PvP",
    Category.MOTION
) {
    private val ticks = setting("Ticks", "Ticks to shift").number(20, 5, 120)
    private val timer = setting("Timer", "Speed of tick burst").number(1, 1, 10, 0.1)
//    private val items = setting("Items", "Speeds up item use").bool(true)
    private val autoDisable = setting("AutoDisable").bool(true)

    private var oldTimer = 1f
    private var packets = 0

//    @Subscribe
//    private fun onItemUse(event: EventInteract.Item) {
//        if (items.toggled) {
//            val itemStack = event.player.getStackInHand(event.hand)
//
//            if (itemStack.isOf(Items.POTION)) {
//                val useTime = itemStack.maxUseTime
//
//                if (packets > useTime) {
//                    for (i in 0 until useTime) {
//                        cPlayer.run { sendPacket(PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, isOnGround)) }
//                    }
//
//                    event.cirValue = ActionResult.PASS
//                    itemStack.finishUsing(cWorld, event.player)
//                }
//            }
//        }
//    }

    override fun onEnable() {
        oldTimer = SessionManager.timer
    }

    override fun onDisable() {
        SessionManager.timer = oldTimer
    }

    override fun onTick() {
        val ticks = ticks.value.toInt()
        val timer = timer.value.toFloat()

        if (CPlayerUtil.isFlying) return

        if (CPlayerUtil.isMovingH || !cPlayer.isOnGround) {

            if (packets >= ticks) {
                oldTimer = SessionManager.timer
                SessionManager.timer = timer
            } else if (packets <= 0) {
                SessionManager.timer = oldTimer
                if (autoDisable.toggled) disable()
            }

            packets--
        } else {
            packets++
        }

        packets = MathHelper.clamp(packets, 0, ticks)
    }


}