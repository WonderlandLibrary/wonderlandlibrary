package me.offeex.bloomware.client.module.visuals

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.structure.ColorMutable
import me.offeex.bloomware.api.util.EntityUtil.getFullHealth
import me.offeex.bloomware.api.util.RenderUtil
import me.offeex.bloomware.api.util.RenderUtil.drawFilledBox
import me.offeex.bloomware.api.util.RenderUtil.drawOutline
import me.offeex.bloomware.api.util.RenderUtil.drawText3D
import me.offeex.bloomware.api.util.RenderUtil.use
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventEntity
import me.offeex.bloomware.event.events.EventPacket
import me.offeex.bloomware.event.events.EventRender
import me.offeex.bloomware.event.events.EventWorld
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.network.OtherClientPlayerEntity
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket
import net.minecraft.util.math.Box
import java.util.*

object LogoutSpot : Module("LogoutSpot", "Renders players logout spots", Category.RENDER) {
    private val nickname = setting("Nickname").bool(true)
    private val health = setting("Health").bool(true)
    private val coordinates = setting("Coordinates").bool()

    private val mode = setting("Mode").enum("Fill", "Outline")
    private val boxColor = setting("BoxColor").color(ColorMutable(255, 0, 0, 255))
    private val textColor = setting("TextColor").color(ColorMutable(255, 0, 0, 255))
    private val time = setting("Time").bool(true)
    private val lineWidth = setting("Width").number(4.0, 1.0, 5.0, 0.1)

    private var playersMap: HashMap<UUID, AbstractClientPlayerEntity> = hashMapOf()
    private var logouts: HashMap<UUID, PlayerInfo> = hashMapOf()

    override fun onDisable() {
        logouts.clear()
    }

    @Subscribe
    private fun onWorld(event: EventWorld) {
        playersMap.clear()
        logouts.clear()
    }

    @Subscribe
    private fun onEntity(event: EventEntity) {
        if (event.entity !is OtherClientPlayerEntity) return
        playersMap = HashMap(cWorld.players.associateBy { it.uuid })
    }

    @Subscribe
    private fun onWorldRender(event: EventRender.World) {
        logouts.forEach { (_: UUID, info: PlayerInfo) ->
            val matrices = event.matrices
            matrices.use {
                val pos = info.player.pos
                val box = Box(0.4, 0.0, 0.4, -0.4, 2.0, -0.4)
                val color = boxColor.color

                RenderUtil.translateToCamera(matrices, pos)

                if (mode.like("Outline")) drawOutline(matrices, box, color, lineWidth.value)
                else drawFilledBox(matrices, box, color)

                matrices.use {
                    RenderUtil.rotateToCamera(matrices)
                    matrices.translate(0.0, 2.5, 0.0)
                    drawStrings(event.matrices, info)
                }
            }
        }
    }

    @Subscribe
    private fun onPacketReceive(event: EventPacket.Receive) {
        if (event.shift) return
        val packet = event.packet

        if (packet is PlayerListS2CPacket && (packet.actions.any { a -> a == PlayerListS2CPacket.Action.ADD_PLAYER || a == PlayerListS2CPacket.Action.UPDATE_LISTED })) {
            for (entry in packet.entries) {
                val uuid = entry.profile.id
                if (entry.listed) {
                    logouts.remove(uuid)
                } else {
                    val player = playersMap[uuid] ?: continue
                    if (player == cPlayer) continue
                    logouts[uuid] = PlayerInfo(player, System.currentTimeMillis())
                }
            }
            playersMap.clear()
        }
    }

    private data class PlayerInfo(val player: AbstractClientPlayerEntity, val time: Long)

    private fun getWidth(player: PlayerEntity): Float {
        var length = 0f
        if (nickname.toggled) length += mc.textRenderer.getWidth(player.entityName + " ")
        if (health.toggled) length += mc.textRenderer.getWidth(
            player.getFullHealth().toInt().toString() + " HP"
        )
        return length
    }

    private fun drawStrings(matrices: MatrixStack, info: PlayerInfo) {
        var offsetX = getWidth(info.player) / -2
        var offsetY = 0f

        if (nickname.toggled) {
            val nickname: String = info.player.entityName + " "
            drawText3D(matrices, nickname, offsetX, 0f, textColor.color)
            offsetX += mc.textRenderer.getWidth(nickname)
            offsetY += -12f
        }
        if (health.toggled) {
            val health = info.player.getFullHealth()
            drawText3D(
                matrices, health.toInt().toString() + " HP", offsetX, offsetY, textColor.color
            )
            offsetX += mc.textRenderer.getWidth(health.toInt().toString() + " HP")
            offsetY += -12f
        }
        if (coordinates.toggled) {
            val pos = info.player.pos
            val text = "XYZ: " + arrayOf(pos.x, pos.y, pos.z).map { "$it, " }
            offsetX = -(mc.textRenderer.getWidth(text) / 2f)
            drawText3D(matrices, text, offsetX, offsetY, textColor.color)
            offsetY += -12f
        }
        if (time.toggled) {
            val text = "${((System.currentTimeMillis() - info.time) * 0.001 / 60).toInt()} mins ago"
            offsetX = -(mc.textRenderer.getWidth(text) / 2f)
            drawText3D(matrices, text, offsetX, offsetY, textColor.color)
        }
    }
}