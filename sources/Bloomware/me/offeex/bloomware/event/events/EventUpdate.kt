package me.offeex.bloomware.event.events

import me.offeex.bloomware.event.Event
import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

abstract class EventUpdate : Event() {
    class Health(val health: Float) : EventUpdate()
    class Block(val blockPos: BlockPos, val oldBlock: BlockState, val newBlock: BlockState) : EventUpdate()
    class BlocksInChunk(val chunkOrigin: BlockPos, val blockPos: BlockPos) : EventUpdate()

    abstract class Rotation : EventUpdate() {
        class Yaw(var delta: Float) : Rotation()
        class Pitch(var delta: Float) : Rotation()
        class Both(val yawDelta: Float, val pitchDelta: Float) : Rotation()
    }
    class Perspective(var perspective: net.minecraft.client.option.Perspective) : EventUpdate()
    class TimeOfDay(var time: Long) : EventUpdate()
}