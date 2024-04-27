/*
 * GlassClient PVP Client
 * A free open-source mixin-based PVP client based on liquidbounce with all cheats removed.
 * https://github.com/GlassClient/GlassClient
 */
package net.ccbluex.liquidbounce.features.module.modules.mods

import net.ccbluex.liquidbounce.event.AttackEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.EntityUtils
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.block.Block
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.init.Blocks
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.ResourceLocation

@ModuleInfo(name = "OnHitParticles", category = ModuleCategory.MODS)
class OnHitParticles : Module() {

    private val modeValue = ListValue("Mode", arrayOf("Lighting", "Blood", "Fire", "Critical", "MagicCritical", "Rise"), "Blood")
    private val timesValue = IntegerValue("Times", 1, 1, 10)
    private val lightingSoundValue = BoolValue("LightingSound", true).displayable { modeValue.equals("Lighting") }

    private val blockState = Block.getStateId(Blocks.redstone_block.defaultState)

    val amount = IntegerValue("Amount", 10, 1, 20)

    val physics = BoolValue("Physics", true)


    @EventTarget
    fun onAttack(event: AttackEvent) {
        if(EntityUtils.isSelected(event.targetEntity, true)) {
            displayEffectFor(event.targetEntity as EntityLivingBase)
        }
    }

    private fun displayEffectFor(entity: EntityLivingBase) {
        repeat(timesValue.get()) {
            when(modeValue.get().lowercase()) {
                "lighting" -> {
                    mc.netHandler.handleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity(EntityLightningBolt(mc.theWorld, entity.posX, entity.posY, entity.posZ)))
                    if(lightingSoundValue.get()) {
                        mc.soundHandler.playSound(PositionedSoundRecord.create(ResourceLocation("random.explode"), 1.0f))
                        mc.soundHandler.playSound(PositionedSoundRecord.create(ResourceLocation("ambient.weather.thunder"), 1.0f))
                    }
                }
                "blood" -> {
                    repeat(10) {
                        mc.effectRenderer.spawnEffectParticle(EnumParticleTypes.BLOCK_CRACK.particleID, entity.posX, entity.posY + entity.height / 2, entity.posZ,
                            entity.motionX + RandomUtils.nextFloat(-0.5f, 0.5f), entity.motionY + RandomUtils.nextFloat(-0.5f, 0.5f), entity.motionZ + RandomUtils.nextFloat(-0.5f, 0.5f), blockState)
                    }
                }
                "fire" ->
                    mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.LAVA)
                "critical" -> mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT)
                "magiccritical" -> mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC)
            }
        }
    }
}