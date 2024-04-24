package me.offeex.bloomware.client.module.world

import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.util.RenderUtil
import me.offeex.bloomware.api.util.RenderUtil.drawFilledBox
import me.offeex.bloomware.api.util.RenderUtil.drawOutline
import me.offeex.bloomware.api.util.RenderUtil.use
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import me.offeex.bloomware.event.events.EventRender
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Vec3d
import kotlin.math.sqrt

object NewChunks : Module("NewChunks", "Shows new generated chunks.", Category.WORLD) {
    private val outline = setting("Outline").group(true)
    private val outlineColor = outline.setting("Color").color(255, 0, 0, 255)
    private val width = outline.setting("Width").number(1.0, 0.1, 3.0, 0.1)
    private val fill = setting("Fill").group(false)
    private val fillColor = fill.setting("Color").color(255, 0, 0, 100)
    private val yOffset = setting("YOffset").number(-50, -100, 100, 1)
    private val range = setting("Range").number(500.0, 100.0, 1000.0, 1.0)
    private val cleaning = setting("Cleaning").group()
    private val afterDisable = cleaning.setting("AfterDisable").bool()
    private val afterDistance = cleaning.setting("AfterDistance").bool()

    private val chunks = mutableListOf<Chunk>()

    override fun onDisable() {
        if (afterDisable.toggled) chunks.clear()
    }

    @Subscribe
    private fun onPacketReceive(event: EventPacket.Receive) {
        if (event.packet is ChunkDeltaUpdateS2CPacket) {
            val packet = event.packet
            packet.visitUpdates { pos, state ->
                val chunkPos = ChunkPos(pos)
                if (!state.fluidState.isEmpty && !state.fluidState.isStill && !`in`(
                        chunkPos.startPos.x, chunkPos.startPos.z
                    )
                ) chunks.add(Chunk(chunkPos))
            }
        }
    }

    private fun `in`(x: Int, z: Int): Boolean {
        return chunks.any { chunk: Chunk -> chunk.pos.startPos.x == x && chunk.pos.startPos.z == z }
    }

    @Subscribe
    private fun onWorldRender(event: EventRender.World) {
        chunks.forEach {
            if (sqrt(cPlayer.squaredDistanceTo(Vec3d.of(it.pos.startPos))) > range.value) {
                if (afterDistance.toggled) chunks.remove(it)
                return@forEach
            }
            val box = Box(
                BlockPos.ORIGIN.down(-yOffset.value.toInt()),
                BlockPos.ORIGIN.add(16, yOffset.value.toInt(), 16)
            )
            val matrices = event.matrices

            matrices.use {
                RenderUtil.translateToCamera(matrices, it.pos.startPos)
                if (fill.toggled) drawFilledBox(matrices, box, fillColor.color)
                if (outline.toggled) drawOutline(matrices, box, outlineColor.color, width.value)
            }
        }
    }

    private data class Chunk(val pos: ChunkPos)
}