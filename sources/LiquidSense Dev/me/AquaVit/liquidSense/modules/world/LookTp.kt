package me.AquaVit.liquidSense.modules.world

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventState
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.RaycastUtils
import net.ccbluex.liquidbounce.utils.RaycastUtils.raycastEntity
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.event.ClickEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatStyle
import org.lwjgl.input.Mouse
import java.util.*


@ModuleInfo(name = "LookTp", description = "Tp.",
        category = ModuleCategory.WORLD)
class LookTp : Module() {
    var targetEntity : EntityLivingBase?= null
    var xx = 0.0f
    var yy = 0.0f
    var zz = 0.0f

    fun x(): Float {
        return xx
    }

    fun y(): Float {
        return yy
    }

    fun z(): Float {
        return zz
    }

    @EventTarget
    fun onMotion(event : MotionEvent){
        if(event!!.eventState == EventState.PRE) {
            val thePlayer = mc.thePlayer ?: return

            val facedEntity = raycastEntity(100.0, object : RaycastUtils.IEntityFilter {
                override fun canRaycast(entity: Entity): Boolean {
                    return entity is EntityLivingBase
                }
            }) ?: return

            if(EntityUtils.isSelected(facedEntity, true) && facedEntity!!.getDistanceSqToEntity(thePlayer) >= 10.0){
                targetEntity = facedEntity as EntityLivingBase
                val text = ChatComponentText("§8[§9§l${LiquidBounce.CLIENT_NAME}§8] §3 ClickMe TP , §r" + targetEntity!!.posX + " " + targetEntity!!.posY + " " + targetEntity!!.posZ)
                text.chatStyle = ChatStyle().setChatClickEvent(ClickEvent(ClickEvent.Action.RUN_COMMAND, "Start TP"))
                Minecraft.getMinecraft().thePlayer.addChatMessage(text)
                targetEntity = null
            }


            /*
            if(mc.gameSettings.keyBindAttack.isKeyDown && EntityUtils.isSelected(facedEntity, true) && facedEntity!!.getDistanceSqToEntity(thePlayer) >= 10.0) {
                targetEntity = facedEntity as EntityLivingBase
                Tp.x = targetEntity!!.posX.toFloat()
                Tp.y = targetEntity!!.posY.toFloat() + 5
                Tp.z = targetEntity!!.posZ.toFloat()
                xx = targetEntity!!.posX.toFloat()
                yy = targetEntity!!.posY.toFloat() + 5
                zz = targetEntity!!.posZ.toFloat()
                ClientUtils.displayChatMessage("§8[§9§l${LiquidBounce.CLIENT_NAME}§8] §3 Set TP , §r" + targetEntity!!.posX + " " + targetEntity!!.posY + " " + targetEntity!!.posZ)
                LiquidBounce.moduleManager.getModule(Tp::class.java)!!.state = true
            }

             */
        }
    }
}