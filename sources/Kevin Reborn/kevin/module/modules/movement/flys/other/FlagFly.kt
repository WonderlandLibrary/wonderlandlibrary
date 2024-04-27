package kevin.module.modules.movement.flys.other

import kevin.event.UpdateEvent
import kevin.module.modules.movement.flys.FlyMode

object FlagFly : FlyMode("Flag") {
    override fun onUpdate(event: UpdateEvent) {
        mc.thePlayer.setPosition(mc.thePlayer.posX,mc.thePlayer.posY+9.25,mc.thePlayer.posZ)
        mc.thePlayer.motionY=1.0
    }
}