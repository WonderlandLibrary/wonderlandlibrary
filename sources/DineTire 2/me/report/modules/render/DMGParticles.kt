package me.report.modules.render

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render3DEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.math.BigDecimal
import java.util.*
import kotlin.math.abs

@ModuleInfo(
    name = "DamageParticles",
    description = "Allows you to see targets damage.",
    category = ModuleCategory.RENDER
)
class DMGParticles : Module() {
    private val healthData=HashMap<Int,Float>()
    private val particles=ArrayList<Particle>()

    private val aliveTicks=IntegerValue("AliveTime",1000,1,5000)
    private val sizeValue=IntegerValue("Size",3,1,7)

    @EventTarget
    fun onUpdate(event: UpdateEvent){
        synchronized(particles){
            for(entity in mc.theWorld.loadedEntityList){
                if(entity is EntityLivingBase && EntityUtils.isSelected(entity,true)){
                    val lastHealth=healthData.getOrDefault(entity.entityId,entity.maxHealth)
                    healthData[entity.entityId] = entity.health
                    if(lastHealth==entity.health) continue

                    val prefix=if(lastHealth>entity.health){"§e"}else{"§e"}
                    particles.add(
                        Particle(prefix+BigDecimal(abs(lastHealth-entity.health).toDouble()).setScale(1,BigDecimal.ROUND_HALF_UP).toDouble()
                        ,entity.posX - 0.5 + Random(System.currentTimeMillis()).nextInt(5).toDouble() * 0.1
                        ,entity.entityBoundingBox.minY + (entity.entityBoundingBox.maxY - entity.entityBoundingBox.minY) / 2.0
                        ,entity.posZ - 0.5 + Random(System.currentTimeMillis() + 1L).nextInt(5).toDouble() * 0.1)
                    )
                }
            }

            val needRemove=ArrayList<Particle>()
            particles.forEach {
                it.ticks++
                if(it.ticks > aliveTicks.get() / 50){
                    needRemove.add(it)
                }
            }
            needRemove.forEach { particles.remove(it) }
        }
    }

    @EventTarget
    fun onRender3d(event: Render3DEvent){
        synchronized(particles){
            val renderManager=mc.renderManager
            val size = sizeValue.get()*0.01

            for(particle in particles){
                val n: Double = particle.posX - renderManager.renderPosX
                val n2: Double = particle.posY - renderManager.renderPosY
                val n3: Double = particle.posZ - renderManager.renderPosZ
                GlStateManager.pushMatrix()
                GlStateManager.enablePolygonOffset()
                GlStateManager.doPolygonOffset(1.0f, -1500000.0f)
                GlStateManager.translate(n.toFloat(), n2.toFloat(), n3.toFloat())
                GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
                val textY = if (mc.gameSettings.thirdPersonView == 2) { -1.0f } else { 1.0f }

                GlStateManager.rotate(renderManager.playerViewX, textY, 0.0f, 0.0f)
                GlStateManager.scale(-size, -size, size)
                GL11.glDepthMask(false)
                mc.fontRendererObj.drawStringWithShadow(
                    particle.str,
                    (-(mc.fontRendererObj.getStringWidth(particle.str) / 2)).toFloat(),
                    (-(mc.fontRendererObj.FONT_HEIGHT - 1)).toFloat(),
                    0
                )
                GL11.glColor4f(255.0f, 255.0f, 255.0f, 1.0f)
                //Color(187, 255, 255).rgb
                GL11.glDepthMask(true)
                GlStateManager.doPolygonOffset(1.0f, 1500000.0f)
                GlStateManager.disablePolygonOffset()
                GlStateManager.popMatrix()
            }
        }
    }

    @EventTarget
    fun onWorld(event: WorldEvent){
        particles.clear()
        healthData.clear()
    }
}

class Particle(val str:String,val posX:Double,val posY:Double,val posZ:Double){
    var ticks = 0
}