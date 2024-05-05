package rip.helium.cheat.impl.player

import rip.helium.cheat.Cheat
import rip.helium.cheat.CheatCategory
import me.hippo.systems.lwjeb.annotation.Collect
import rip.helium.event.minecraft.PlayerUpdateEvent
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.util.AxisAlignedBB
import rip.helium.utils.property.impl.DoubleProperty

class Regen : Cheat("Regen", "Makes you heal faster", CheatCategory.PLAYER) {

    @JvmField
    var waittime = 0
    var health = DoubleProperty("Health", "You'll start regenerating health after you go below this.", null, 15.0, 0.0, 20.0, 0.5, null)

    @Collect
    fun onPlayerUpdate(playerUpdateEvent: PlayerUpdateEvent) {
        if (mc.thePlayer.health <= health.getValue()) {
            for (i in 0..19) {
                if (mc.thePlayer.onGround) {
                    mc.netHandler.addToSendQueue(C03PacketPlayer(true))
                }
            }
        }
    }

    val groundLevel: Double
        get() {
            for (i in Math.round(getPlayer().posY).toInt() downTo 1) {
                val box = getPlayer().entityBoundingBox.addCoord(0.0, 0.0, 0.0)
                box.minY = (i - 1).toDouble()
                box.maxY = i.toDouble()
                if (isColliding(box) && box.minY <= getPlayer().posY) {
                    return i.toDouble()
                }
            }
            return 0.0
        }

    private fun isColliding(box: AxisAlignedBB): Boolean {
        return Cheat.mc.theWorld.checkBlockCollision(box)
    }

    init {
        registerProperties(health)
    }
}