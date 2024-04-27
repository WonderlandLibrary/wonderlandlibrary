package me.AquaVit.liquidSense.modules.render

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.Aura
import net.ccbluex.liquidbounce.utils.misc.MiscUtils.random
import net.ccbluex.liquidbounce.utils.render.ColorUtils.rainbow
import net.ccbluex.liquidbounce.utils.render.RenderUtils.*
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.client.C02PacketUseEntity
import java.awt.Color

@ModuleInfo(name = "KillESP", description = "KillESP", category = ModuleCategory.RENDER)
class KillESP : Module() {
    private val modeValue = ListValue("Mode", arrayOf("Crystal", "Box", "Head", "Mark"), "Crystal")

    private val colorRedValue = IntegerValue("R", 0, 0, 255)
    private val colorGreenValue = IntegerValue("G", 160, 0, 255)
    private val colorBlueValue = IntegerValue("B", 255, 0, 255)
    private val alphaValue = IntegerValue("Alpha", 255, 0, 255)
    private val killLightningBoltValue = BoolValue("LightningBolt", true)
    private val rainbow = BoolValue("RainBow", false)
    private val hurt = BoolValue("HurtTime", true)
    private val killAura = LiquidBounce.moduleManager.getModule(Aura::class.java) as Aura

    private var targetList: HashMap<EntityLivingBase, Long> = HashMap()

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (killLightningBoltValue.get()) {
            for (entry in targetList) {
                if (entry.key.isDead || entry.key.health == 0F) {
                    if(entry.value > System.currentTimeMillis()){
                        val ent = EntityLightningBolt(mc.theWorld, entry.key.posX, entry.key.posY, entry.key.posZ)
                        mc.theWorld.addEntityToWorld(-1, ent)
                        mc.thePlayer.playSound("random.explode", 0.5f, 0.5f + random.nextFloat() * 0.2f)
                    }
                    targetList.remove(entry.key)
                }
            }
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (killLightningBoltValue.get()) {
            if (event.packet is C02PacketUseEntity) {
                if (event.packet.action == C02PacketUseEntity.Action.ATTACK) {
                    val entity = event.packet.getEntityFromWorld(mc.theWorld)
                    if (entity is EntityLivingBase) {
                        targetList[entity] = System.currentTimeMillis() + 3000
                    }
                }
            }
        }
    }

    @EventTarget
    fun onWorldChange(event: WorldEvent) {
        targetList.clear()
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent){
        var color = if (rainbow.get()) rainbow() else Color(colorRedValue.get(), colorGreenValue.get(), colorBlueValue.get(), alphaValue.get())
        killAura.target ?:return
        val RenderManager = mc.renderManager
        val entityLivingBase: EntityLivingBase = killAura.target as EntityLivingBase
        val pX = (entityLivingBase.lastTickPosX + (entityLivingBase.posX - entityLivingBase.lastTickPosX) * mc.timer.renderPartialTicks
                - RenderManager.renderPosX)
        val pY = (entityLivingBase.lastTickPosY + (entityLivingBase.posY - entityLivingBase.lastTickPosY) * mc.timer.renderPartialTicks
                - RenderManager.renderPosY)
        val pZ = (entityLivingBase.lastTickPosZ + (entityLivingBase.posZ - entityLivingBase.lastTickPosZ) * mc.timer.renderPartialTicks
                - RenderManager.renderPosZ)
        when (modeValue.get().toLowerCase()) {
            "crystal" -> drawCrystal(killAura.target, pX, pY + 2.25, pZ,if(hurt.get() && killAura.target!!.hurtTime > 3) Color(255, 50, 50, 75).rgb else color.rgb,if(hurt.get() && killAura.target!!.hurtTime > 3) Color(255, 50, 50, 75).rgb else color.rgb)
            "box" -> drawEntityBoxESP(killAura.target,if(hurt.get() && killAura.target!!.hurtTime > 3) Color(255, 50, 50, 75) else color)
            "head" -> drawPlatformESP(killAura.target,if(hurt.get() && killAura.target!!.hurtTime > 3) Color(255, 50, 50, 75) else color)
            "mark" -> drawPlatform(killAura.target,  if(hurt.get() && killAura.target!!.hurtTime > 3) Color(255, 50, 50, 75) else color)
        }
    }

}