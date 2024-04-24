package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.Stopwatch
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.util.EntityUtil.getFullHealth
import me.offeex.bloomware.api.util.InventoryUtil
import me.offeex.bloomware.api.util.InventoryUtil.findSlots
import me.offeex.bloomware.api.util.MathUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.module.motion.Flight
import me.offeex.bloomware.client.setting.setting
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.decoration.EndCrystalEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.item.SwordItem
import net.minecraft.util.Hand
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext

object Offhand : Module("Offhand", "Automagically manages your offhand.", Category.PVP) {
    private val delay = setting("TickDelay").number(1, 1, 20)
    private val preferredItem = setting("Item").enum(*OffhandItem.values().map { it.name }.toTypedArray())

    private val totemHealth = setting("CriticalHP").number(10, 0, 20).depend(preferredItem) { !preferredItem.like("Totem") }

    private val lethal = setting("Lethal").group(true).depend(preferredItem) { !preferredItem.like("Totem") }
    private val fall = lethal.setting("Fall").bool(true)
    private val crystal = lethal.setting("Crystal").group(true)
    private val crystalHealth = crystal.setting("Distance").number(6, 0, 12)
    private val fly = lethal.setting("Fly").bool(true)
    private val gappleCooldown = lethal.setting("GappleCooldown").number(10, 0, 20)
    private val illegals = lethal.setting("Illegals").bool(true)

    private val swordGap = setting("SwordGap").bool(true).depend(preferredItem) { !preferredItem.like("Gapple") }


    private val stopwatch = Stopwatch()

    override fun onTick() {
        val offhandItem = cPlayer.offHandStack.item

        // LETHAL handing
        val lethalCheck = lethal.toggled && threatened()
        if (cPlayer.getFullHealth() <= totemHealth.value || lethalCheck) {

            // in case we already have totem in offhand, return
            if (offhandItem == Items.TOTEM_OF_UNDYING) return

            // If slot with totem is found and totem is not in offhand, swap to totem.
            val totemSlot = Items.TOTEM_OF_UNDYING.findSlots().lastOrNull()
            if (totemSlot != null) {
                return InventoryUtil.swap(totemSlot, PlayerInventory.OFF_HAND_SLOT)
            }
        }

        // SWORDGAP handing
        // If swordGap is toggled, player is holding sword in main hand AND gapple is not preferred, swap to gapple.
        val swordInHand = InventoryUtil.findHand<SwordItem>() == Hand.MAIN_HAND
        if (swordGap.toggled && swordInHand && mc.options.useKey.isPressed) {

            // in case gapple we already have gapple in offhand, return
            if (offhandItem == Items.ENCHANTED_GOLDEN_APPLE) return

            // If slot with gapple is found and gapple is not in offhand, swap to gapple.
            val gappleSlot = Items.ENCHANTED_GOLDEN_APPLE.findSlots().lastOrNull()
            if (gappleSlot != null && stopwatch.passed((delay.value * 50L).toLong())) {
                InventoryUtil.swap(gappleSlot, PlayerInventory.OFF_HAND_SLOT)
                return stopwatch.reset()
            }
        }

        // If item is not found OR item is already in offhand, return
        val itemPreferred = OffhandItem.valueOf(preferredItem.selected).item
        val slot = itemPreferred.findSlots().lastOrNull()
        if (slot == null || cPlayer.offHandStack.isOf(itemPreferred)) return

        // And finally, if delay is passed, swap to preferred item.
        if (stopwatch.passedTicks(delay.value)) {
            InventoryUtil.swap(slot, PlayerInventory.OFF_HAND_SLOT)
            stopwatch.reset()
        }
    }

    // Finds out if player is threatened, so totem can be used.
    private fun threatened(): Boolean {
        val canTakeDamage = !cPlayer.abilities.creativeMode

        val canHeal = cPlayer.itemUseTimeLeft <= gappleCooldown.value && cPlayer.isUsingItem

        val flyThreat = (Flight.enabled || cPlayer.abilities.flying) && fly.toggled

        // checks if crystal would kill player
        fun isCrystalLethal(pos: Vec3d) = MathUtil.getCrystalDamage(pos, cPlayer) >= cPlayer.getFullHealth()
        val crystalThreat = crystal.toggled && cWorld.entities
            .filterIsInstance<EndCrystalEntity>()
            .any { it.isAlive && cPlayer.distanceTo(it) <= crystalHealth.value && isCrystalLethal(it.pos) }

        // checks if player is falling from high place, and lowest block is not water
        val fallDamage = (cPlayer.fallDistance - 3) / 2 + 3.5
        val pos = cPlayer.pos
        val downPos = pos.withAxis(Direction.Axis.Y, cWorld.bottomY.toDouble())
        val ray = cWorld.raycast(RaycastContext(pos, downPos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.WATER, cPlayer))
        val fallThreat = !cWorld.isWater(ray.blockPos) && fallDamage >= cPlayer.getFullHealth() && fall.toggled

        // checks if player is holding item with sharpness > 5
        val illegalThreat: Boolean = illegals.toggled && cWorld.entities
            .filterIsInstance<PlayerEntity>()
            .filter { it.distanceTo(cPlayer) < 8 }
            .any { EnchantmentHelper.getLevel(Enchantments.SHARPNESS, it.activeItem) > 5 }

        return (!canHeal && canTakeDamage) && (crystalThreat || fallThreat || flyThreat || illegalThreat)
    }


    private enum class OffhandItem(val item: Item) {
        Totem(Items.TOTEM_OF_UNDYING), Gapple(Items.ENCHANTED_GOLDEN_APPLE), Crystal(Items.END_CRYSTAL)
    }
}