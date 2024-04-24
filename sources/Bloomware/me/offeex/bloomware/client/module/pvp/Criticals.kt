package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.client.setting.settings.SettingNumber
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import me.offeex.bloomware.mixin.accessor.IPlayerInteractEntityC2SPacket
import net.minecraft.entity.LivingEntity
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket

object Criticals : Module("Criticals", "Crits when you hit enemy", Category.PVP) {
    private val mode = setting("Mode").enum("Jump", "Packet")

    private val packetAmount = setting("Amount").number(3, 1, 4).depend(mode) { mode.selected == "Packet" }
    private val offsets = setting("Offsets").group().depend(mode) { mode.selected == "Packet" }

    private val firstOffset = offsets.setting("First").number(0.05, 0, 0.12, 0.0005)
    private val secondOffset = offsets.setting("Second").number(0, 0, 0.12, 0.0005).depend(packetAmount) { packetAmount.value >= 2.0 }
    private val thirdOffset = offsets.setting("Third").number(0.012, 0, 0.12, 0.0005).depend(packetAmount) { packetAmount.value >= 3.0 }
    private val fourthOffset = offsets.setting("Fourth").number(0, 0, 0.12, 0.0005).depend(packetAmount) { packetAmount.value == 4.0 }

    private val jumpMultiplier = setting("JumpMultiplier").number(0.5, 0, 1, 0.01).depend(mode) { mode.selected == "Jump" }

    @Subscribe
    fun onPacketSend(event: EventPacket.Send) {
        if (event.shift) return

        val p = event.packet
        if (p is IPlayerInteractEntityC2SPacket
            && p.type.type == PlayerInteractEntityC2SPacket.InteractType.ATTACK
            && canCrit) {

            val enemy = cWorld.getEntityById(p.entityId) ?: return
            if (enemy !is LivingEntity) return

            if (mode.like("Jump")) {
                cPlayer.jump()
                cPlayer.velocity = cPlayer.velocity.multiply(1.0, jumpMultiplier.value, 1.0)
            } else if (mode.like("Packet")) {
                for (i in offsets.settings.indices) {
                    if (i >= packetAmount.value) break
                    sendCritPacket((offsets.settings[i] as SettingNumber).value)
                }
            }
        }
    }

    private fun sendCritPacket(offset: Double) {
        sendPacket(PlayerMoveC2SPacket.PositionAndOnGround(cPlayer.x, cPlayer.y + offset, cPlayer.z, false))
    }

    private val canCrit: Boolean
        get() = cPlayer.isOnGround
            && !mc.options.jumpKey.isPressed
            && !(cPlayer.isSubmergedInWater || cPlayer.isInLava)
}