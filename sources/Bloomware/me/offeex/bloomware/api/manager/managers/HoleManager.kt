package me.offeex.bloomware.api.manager.managers

import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.ProtectionMark
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.Manager
import me.offeex.bloomware.api.structure.Hole
import me.offeex.bloomware.api.util.WorldUtil.forEachBlock
import me.offeex.bloomware.client.module.client.GlobalControls
import me.offeex.bloomware.client.module.pvp.HoleFiller
import me.offeex.bloomware.client.module.visuals.ESP
import me.offeex.bloomware.event.events.EventChunk
import me.offeex.bloomware.event.events.EventUpdate
import me.offeex.bloomware.event.events.EventWorld
import net.minecraft.block.Blocks
import net.minecraft.util.crash.CrashException
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors


object HoleManager : Manager() {
    private val executor = Executors.newSingleThreadExecutor()
    private val dependencies: () -> Boolean = { ESP.holes.toggled || HoleFiller.enabled }
    var cachedHoles = ConcurrentHashMap<BlockPos, Hole>()

    @Subscribe
    private fun onChunk(event: EventChunk) {
        if (!dependencies()) return
        executor.submit {
            event.chunk.forEachBlock { x, y, z ->
                if (event is EventChunk.Load) updateHole(BlockPos(x, y, z))
                else if (event is EventChunk.Unload) cachedHoles.remove(BlockPos(x, y, z))
            }
        }
    }

    @Subscribe
    private fun onBlocksInChunkUpdate(event: EventUpdate.BlocksInChunk) {
        if (mc.player == null || !dependencies() || !event.blockPos.isWithinDistance(cPlayer.pos, GlobalControls.holeRange.value)) return
        executor.submit {
            Direction.values().forEach { updateHole(event.blockPos.add(it.vector)) }
        }
    }

    @Subscribe
    private fun onWorld(event: EventWorld) {
        cachedHoles.clear()
    }

    fun has(blockPos: BlockPos) = cachedHoles.containsKey(blockPos)

    private fun updateHole(relativePos: BlockPos) {
        val type = resolveHoleType(relativePos)
        if (type == null) cachedHoles.remove(relativePos)
        else cachedHoles[relativePos] = type
    }

    private fun resolveHoleType(pos: BlockPos): Hole? {
        try {
            fun BlockPos.state() = cWorld.getBlockState(this)
            if (!pos.up().state().isAir || !pos.state().isAir || pos.down().state().isAir) return null

            var obsidian = 0
            var bedrock = 0
            for (bp in Direction.Type.HORIZONTAL) {
                val offsetPos = pos.offset(bp)
                val b = offsetPos.state().block
                if (b === Blocks.BEDROCK) bedrock++
                else if (b === Blocks.OBSIDIAN || b === Blocks.CRYING_OBSIDIAN) obsidian++
            }

            return if (bedrock == 4) Hole.BEDROCK
            else if (bedrock + obsidian == 4) Hole.SAFE
            else null
        } catch (e: CrashException) {
            Bloomware.LOGGER.error("CrashException in HoleManager#resolveHoleType: " + e.message)
            return null
        }
    }

    @ProtectionMark
    fun protection() {
    }
}