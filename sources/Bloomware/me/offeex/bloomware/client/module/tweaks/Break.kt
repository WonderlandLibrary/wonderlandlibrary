package me.offeex.bloomware.client.module.tweaks

import me.offeex.bloomware.api.helper.Stopwatch
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.manager.managers.ModuleManager
import me.offeex.bloomware.api.structure.ColorMutable
import me.offeex.bloomware.api.util.InventoryUtil
import me.offeex.bloomware.api.util.RenderUtil
import me.offeex.bloomware.api.util.RenderUtil.use
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.module.tweaks.Break.BreakTarget.breakingStopwatch
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.*
import net.minecraft.block.BlockState
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffectUtil
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket
import net.minecraft.registry.tag.FluidTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper

object Break : Module("Break", "", Category.TWEAKS) {

    /* *************** General settings *************** */

    private val mode = setting("Mode").enum("Normal", "Packet")
    private val autoSwitch = setting("AutoSwitch").enum("Silent", "Normal", "Off")

    /* *************** Packet mode settings *************** */

    private val mineRange = setting("Range").number(4, 2, 6, 0.1)
        .depend(mode) { mode.like("Packet") }

    private val abortOn = setting("AbortOn").group()
        .depend(mode) { mode.like("Packet") }
    private val finish = abortOn.setting("Finish").bool(true)
    private val maxTime = abortOn.setting("MaxTime").number(5, 3, 40, 0.1)
    private val maxProgress = abortOn.setting("MaxProgress").number(1.1, 1, 2, 0.01)

    private val retryOnSwitch = setting("RetryOnSwitch").bool(true)
        .depend(mode) { mode.like("Packet") }

    private val rebreak = setting("Rebreak").group(true).depend(mode) { mode.like("Packet") }
    private val limitRebreaks = rebreak.setting("Limit").bool(true)
    private val rebreakCount = rebreak.setting("Max").number(2, 1, 10, 1)
        .depend(limitRebreaks) { limitRebreaks.toggled }
    private val instant = rebreak.setting("Instant").bool(false)
    private val instantDelay = rebreak.setting("InstantDelay").number(0.2, 0, 1, 0.01)
        .depend(instant) { instant.toggled }

    /* *************** Normal mode settings *************** */

    private val delay = setting("Delay").number(1, 0, 1, 0.2)
        .depend(mode) { !mode.like("Packet") }
    private val ignoreCreative = setting("IgnoreCreative").bool(true)
        .depend(mode) { !mode.like("Packet") }
    private val haste = setting("Haste").group(true)
        .depend(mode) { !mode.like("Packet") }
    private val hasteLevel = haste.setting("HasteLevel").number(2, 1, 3)

    private var fakeHasteEffect: StatusEffectInstance? = null
    private var prevProgress: Float = 0f

    init {
        maxProgress.description = "Maximum progress of breaking block, after which the block breaking will be aborted"

        haste.toggledUpdateBus.subscribe { _, newValue ->
            ModuleManager.checkNull {
                if (newValue) createFakeHaste()
                else disableHaste()
            }
        }

        mode.selectedUpdateBus.subscribe { _, newValue ->
            ModuleManager.checkNull {
                if (newValue == "Packet") disableHaste()
                else if (haste.toggled) createFakeHaste(false)
            }
        }
    }

    @Subscribe
    fun onBlockBreakingCooldown(event: EventCooldown) {
        if ((cPlayer.isCreative && !ignoreCreative.toggled) || mode.like("Packet")) return
        if (delay.value != 1.0) event.cooldown = (delay.value * 5).toInt()
    }

    @Subscribe
    private fun onAttackBlock(event: EventAttack.Block) {
        if (mode.like("Packet")) {

            // Reset breaking progress if player starts breaking another block or re-clicks the same block
            if (BreakTarget.pos != null) BreakTarget.submit(Action.ABORT_DESTROY_BLOCK)
            BreakTarget.flush(event.pos, event.direction)
        }
    }

    @Subscribe
    private fun onBlockBreak(event: EventBreakBlock) {
        if (mode.like("Packet") && !BreakTarget.isNull()) {
            if (event is EventBreakBlock.Update) event.canceled = true
            else if (event is EventBreakBlock.Finish) event.cirValue = false
        }
    }

    @Subscribe
    private fun onPacketSend(event: EventPacket.Send) {
        if (event.shift) return

        if (event.packet is UpdateSelectedSlotC2SPacket && mode.like("Packet") && retryOnSwitch.toggled) {
            BreakTarget.reset()
        }
    }

    @Subscribe
    private fun onPacketReceive(event: EventPacket.Receive) {
        if (event.shift) return

        if (event.packet is BlockUpdateS2CPacket
            && !BreakTarget.isNull()
            && BreakTarget.pos == event.packet.pos
            && event.packet.state.isAir
        ) BreakTarget.increment()
    }

    @Subscribe
    private fun onRender(event: EventRender.World) {
        if (BreakTarget.isNull()) return
        val p = MathHelper.clamp(MathHelper.lerp(event.tickDelta, prevProgress, BreakTarget.progress), 0f, 1f)
        val pInv = 1 - p
        val pHalf = p * 0.5
        event.matrices.use {
            RenderUtil.translateToCamera(event.matrices, BreakTarget.pos!!.toCenterPos())
            RenderUtil.drawFilledBox(
                event.matrices,
                Box(-pHalf, -pHalf, -pHalf, pHalf, pHalf, pHalf),
                ColorMutable((255 * pInv).toInt(), (255 * p).toInt(), (255 * p).toInt(), 80)
            )
        }
    }

    override fun onEnable() {
        createFakeHaste()
        BreakTarget.flush()
    }

    override fun onDisable() {
        if (haste.toggled && fakeHasteEffect != null) disableHaste()
        else if (mode.like("Packet")) {
            if (!BreakTarget.isNull()) BreakTarget.submit(Action.ABORT_DESTROY_BLOCK)
            BreakTarget.flush()
        }
    }

    override fun onTick() {
        if (cPlayer.isCreative) return

        if (mode.like("Packet")) {

            // If no target is present, or target is air
            if (BreakTarget.isNull()) {
                return BreakTarget.flush()
            }

            // If target is out of range
            if (!BreakTarget.inRange()) {
                BreakTarget.submit(Action.ABORT_DESTROY_BLOCK)
                return BreakTarget.flush()
            }

            // We wait until target becomes solid block and then do stuff..
            if (cWorld.isAir(BreakTarget.pos)) {
                return
            }

            val allowRebreak = rebreak.toggled || BreakTarget.breaks == 0
            val withinLimitsRebreak = BreakTarget.breaks <= rebreakCount.value || !limitRebreaks.toggled

            /*
             * First we check, whether rebreak is not toggled AND the block has been broken at least once
             * Then, whether the block has been broken < maximum amount of times, OR the limit is disabled
             */
            if (!allowRebreak || !withinLimitsRebreak) {
                return BreakTarget.reset()
            }

            // Get slot with best tool for breaking the block. Returns null if no tool is present
            val swapSlot = InventoryUtil.findBestTool(cWorld.getBlockState(BreakTarget.pos))

            if (breakingStopwatch.passed(maxTime.value * 1000)) {
                BreakTarget.submit(Action.ABORT_DESTROY_BLOCK)
                return BreakTarget.flush()
            }

            prevProgress = BreakTarget.progress
            BreakTarget.progress += calcBlockBreakingDelta(cWorld.getBlockState(BreakTarget.pos), swapSlot)

            if (BreakTarget.progress > maxProgress.value) {
                BreakTarget.submit(Action.ABORT_DESTROY_BLOCK)
                return BreakTarget.flush()
            }

            // If we are instabreaking (breaks > 0), and the instaBreak delay has passed, we allow it
            val allowInstaBreak = BreakTarget.breaks > 0
                && instant.toggled
                && BreakTarget.progress > instantDelay.value

            if (BreakTarget.progress < 1 && !allowInstaBreak) return

            // The main logic: swap to the best tool, break the block, swap back

            val prevSlot = cPlayer.inventory.selectedSlot
            swap(swapSlot, prevSlot, true)

            BreakTarget.submit(Action.STOP_DESTROY_BLOCK)
            if (finish.toggled) BreakTarget.submit(Action.ABORT_DESTROY_BLOCK)

            swap(swapSlot, prevSlot)

        } else if (fakeHasteEffect != null) {

            // If haste is enabled and the fake haste effect is not null, apply it
            if (haste.toggled) cPlayer.addStatusEffect(fakeHasteEffect)
            else disableHaste()
        }
    }

    private fun swap(swapSlot: Int, prevSlot: Int, swapForward: Boolean = false) {
        if (swapSlot == -1 || swapSlot == prevSlot) return

        if (autoSwitch.like("Silent")) InventoryUtil.swap(swapSlot, prevSlot)
        else if (autoSwitch.like("Normal") && swapForward) cPlayer.inventory.selectedSlot = swapSlot % 9
    }

    private fun createFakeHaste(packetMineToggled: Boolean = mode.like("Packet")) {
        val hasteLevel = hasteLevel.value.toInt()
        val realEffect = cPlayer.getStatusEffect(StatusEffects.HASTE)
        val realCheck = realEffect != null && realEffect.amplifier + 1 >= hasteLevel

        /* If
         * haste is already applied AND the amplifier is higher than the one we want to apply,
         * OR if packet mine is enabled
         * OR if the haste setting is disabled
         * RETURN
         */
        if (!haste.toggled || packetMineToggled || realCheck) {
            return
        }

        // Otherwise, set the haste effect to the one we want to apply
        fakeHasteEffect = StatusEffectInstance(StatusEffects.HASTE, Int.MAX_VALUE, hasteLevel - 1)
    }

    private fun disableHaste() {
        cPlayer.removeStatusEffect(StatusEffects.HASTE)
        fakeHasteEffect = null
    }

    // Same as {@link AbstractBlock#calcBlockBreakingDelta}, but with slot param (instead of MainHand)
    private fun calcBlockBreakingDelta(state: BlockState, slot: Int): Float {
        val f = state.getHardness(cWorld, BlockPos.ORIGIN);
        if (f == -1.0F) return 0.0F
        else {
            val i = if (canHarvest(state, slot)) 30 else 100
            return getBlockBreakingSpeed(state, slot) / f / i;
        }
    }

    // Same as {@link PlayerEntity#getBlockBreakingSpeed}, but with slot param (instead of MainHand)
    private fun getBlockBreakingSpeed(block: BlockState, slot: Int): Float {
        val stack = InventoryUtil.getStack(slot)
        var f: Float = stack.getMiningSpeedMultiplier(block)
        if (f > 1.0f) {
            val i = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, stack)
            if (i > 0 && !stack.isEmpty) {
                f += i * i + 1
            }
        }
        if (StatusEffectUtil.hasHaste(cPlayer)) {
            f *= 1.0f + (StatusEffectUtil.getHasteAmplifier(cPlayer) + 1).toFloat() * 0.2f
        }
        if (cPlayer.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            val g: Float = when (cPlayer.getStatusEffect(StatusEffects.MINING_FATIGUE)!!.amplifier) {
                0 -> 0.3f
                1 -> 0.09f
                2 -> 0.0027f
                3 -> 8.1E-4f
                else -> 8.1E-4f
            }
            f *= g
        }
        if (cPlayer.isSubmergedIn(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(cPlayer)) {
            f /= 5.0f
        }
        if (!cPlayer.isOnGround) {
            f /= 5.0f
        }
        return f
    }

    // Same as {@link PlayerEntity#canHarvest}, but with slot param (instead of MainHand)
    private fun canHarvest(state: BlockState, slot: Int): Boolean {
        return !state.isToolRequired || InventoryUtil.getStack(slot).isSuitableFor(state)
    }

    private object BreakTarget {
        var pos: BlockPos? = null
        var direction: Direction? = null
        var progress = 0f
        var breaks = 0

        val breakingStopwatch = Stopwatch()

        // Used as an abstraction layer to send packets
        fun submit(action: Action) {
            sendPacket(PlayerActionC2SPacket(action, pos, direction))
        }

        fun flush(pos: BlockPos? = null, direction: Direction? = null) {
            this.pos = pos
            this.direction = direction
            this.breaks = 0
            this.reset()
        }

        fun increment() = this.reset().let { this.breaks++ }

        fun reset() {
            this.progress = 0f
            prevProgress = 0f
            breakingStopwatch.reset()
        }

        fun isNull() = pos == null || direction == null

        fun inRange() = pos!!.isWithinDistance(cPlayer.pos, mineRange.value)
    }
}