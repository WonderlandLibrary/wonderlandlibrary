package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.RotationManager
import me.offeex.bloomware.api.util.BlockUtil
import me.offeex.bloomware.api.util.ChatUtil
import me.offeex.bloomware.api.util.InventoryUtil
import me.offeex.bloomware.api.util.InventoryUtil.findHand
import me.offeex.bloomware.api.util.InventoryUtil.quickAccessible
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.client.setting.settings.SettingEnum
import net.minecraft.item.Items
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

abstract class Obsidian(n: String, d: String, c: Category) : Module(n, d, c) {
    private val autoSwitch = setting("AutoSwitch").bool(true)
    private val delay = setting("TickDelay").number(1, 1, 20)
    private val bpt = setting("BlocksPerTick").number(4, 1, 20)
    protected abstract val rotation: SettingEnum

    protected var complete = false
    protected var wasComplete = false
    private var placedPerTick = 0
    private var oldSlot = -1

    override fun onEnable() {
        oldSlot = cPlayer.inventory.selectedSlot
    }

    override fun onDisable() {
        cPlayer.inventory.selectedSlot = oldSlot
        RotationManager.reset()
    }

    override fun onTick() {
        val accessible = Items.OBSIDIAN.quickAccessible()
        if (!accessible) {
            ChatUtil.failMessage("No blocks found. Module disabled", this)
            return disable()
        }

        complete = isComplete()
        if (complete) resetSlotAndRotation()

        onTickExtend()

        wasComplete = complete
    }

    abstract fun onTickExtend()

    protected fun fillPositions(positions: List<BlockPos> = positionsToFill()) {
        if (cPlayer.age % delay.value != 0.0) return

        oldSlot = cPlayer.inventory.selectedSlot
        if (autoSwitch.toggled) InventoryUtil.switchToItem(Items.OBSIDIAN)

        for (pos in positions) {
            if (placedPerTick >= bpt.value) {
                placedPerTick = 0
                return
            }

            val hand = Items.OBSIDIAN.findHand() ?: break

            handleRotations(pos)
            cInteractManager.interactBlock(cPlayer, hand, genBlockHitResult(pos))
            cPlayer.swingHand(hand)
            placedPerTick++
        }

        placedPerTick = 0
    }

    abstract fun isComplete(): Boolean
    abstract fun positionsToFill(): List<BlockPos>

    private fun resetSlotAndRotation() {
        RotationManager.reset()
        cPlayer.inventory.selectedSlot = oldSlot
    }

    protected open fun handleRotations(pos: BlockPos) {
        val centerPos = Vec3d.ofCenter(pos).add(0.5, 1.0, 0.5)
        if (rotation.like("Strict")) RotationManager.setRotation(centerPos)
        if (!rotation.like("Off")) RotationManager.sendPacket()
    }

    private fun genBlockHitResult(pos: BlockPos): BlockHitResult {
        val dir = BlockUtil.findBlockSide(pos)
        return BlockHitResult(
            Vec3d.of(pos),
            if (dir != null) dir.opposite else Direction.DOWN,
            if (dir != null) pos.offset(dir) else pos,
            false
        )
    }

    protected fun settingRotations(vararg modes: String = arrayOf()) = setting("Rotation").enum("Strict", "Off", *modes)
}