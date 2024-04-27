package kevin.module.modules.movement.speeds.other

import kevin.event.BlockBBEvent
import kevin.event.UpdateEvent
import kevin.module.modules.movement.speeds.SpeedMode
import kevin.utils.MovementUtils
import net.minecraft.block.BlockAir
import net.minecraft.util.AxisAlignedBB
import kotlin.math.floor

object CollisionLowHop : SpeedMode("CollisionLowHop") {
    var i = 200
    override fun onUpdate(event: UpdateEvent) {
        if (!MovementUtils.isMoving
            || mc.thePlayer.isInLava
            || mc.thePlayer.isInWater
            || mc.thePlayer.inWeb
            || mc.thePlayer.isOnLadder) {
            return
        }
        if (mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown) {
            mc.thePlayer.jump()
        }
        if (mc.thePlayer.motionY <= 0.3) {
            i = floor(mc.thePlayer.posY + 3.0).toInt()
        }
    }

    override fun onBlockBB(event: BlockBBEvent) {
        if (event.block is BlockAir && event.y in i..(i+1) && mc.thePlayer.motionY in 0.0..0.3) {
            event.boundingBox = AxisAlignedBB.fromBounds(event.x.toDouble(), event.y.toDouble(), event.z.toDouble(), event.x + 1.0, event.y + 1.0, event.z + 1.0)
        }
    }
}