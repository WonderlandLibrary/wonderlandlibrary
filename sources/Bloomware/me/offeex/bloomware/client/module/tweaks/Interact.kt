package me.offeex.bloomware.client.module.tweaks

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.client.setting.settings.SettingMap
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventInteract
import me.offeex.bloomware.event.events.EventItemUse
import me.offeex.bloomware.event.events.EventPacket
import me.offeex.bloomware.mixin.accessor.IBlockHitResult
import me.offeex.bloomware.mixin.accessor.IMinecraftClient
import net.minecraft.block.*
import net.minecraft.item.ItemUsageContext
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult

object Interact : Module("Interact", "", Category.TWEAKS) {
    private val place = setting("Place").group()
    private val placeDelay = place.setting("PlaceDelay").number(0, 0, 1, 0.25)
    private val buildHeight = place.setting("BuildHeight").bool()
    private val airPlace = place.setting("InAir").bool()

    private val antiInteract = place.setting("AntiInteract").group(true)
    private val forcePlace = antiInteract.setting("ForcePlace").bool()

    private val blocks = antiInteract.setting("Blocks").group()
    private val craftingTable = blocks.setting("CraftingTable").map(CraftingTableBlock::class.java)
    private val anvil = blocks.setting("Anvil").map(AnvilBlock::class.java)
    private val enchantmentTable = blocks.setting("EnchantmentTable").map(EnchantingTableBlock::class.java)
    private val furnace = blocks.setting("Furnace").map(FurnaceBlock::class.java)
    private val hopper = blocks.setting("Hopper").map(HopperBlock::class.java)
    private val brewingStand = blocks.setting("BrewingStand").map(BrewingStandBlock::class.java)
    private val shulkerBox = blocks.setting("ShulkerBox").map(ShulkerBoxBlock::class.java)
    private val chest = blocks.setting("Chest").map(ChestBlock::class.java)
    private val enderChest = blocks.setting("EnderChest").map(EnderChestBlock::class.java)

    private val useDelay = setting("UseDelay").number(0, 0, 1, 0.25)

    @Subscribe
    fun onPacketSend(event: EventPacket.Send) {
        if (event.packet is PlayerInteractItemC2SPacket) (mc as IMinecraftClient).setItemUseCooldown((useDelay.value * 4).toInt())
    }

    @Subscribe
    private fun onItemUse(event: EventItemUse) {
        val target = mc.crosshairTarget ?: return
        if (airPlace.toggled && target is BlockHitResult) (target as IBlockHitResult).setMissed(false)
    }

    @Subscribe
    private fun onInteractBlock(event: EventInteract.Block) {
        if (antiInteract.toggled && blockCheck(cWorld.getBlockState(event.hitResult.blockPos).block)) {
            if (forcePlace.toggled) {
                val stack = cPlayer.getStackInHand(event.hand)
                if (!cPlayer.itemCooldownManager.isCoolingDown(stack.item)) event.cirValue = ActionResult.PASS

                cNetHandler.apply {
                    sendPacket(ClientCommandC2SPacket(cPlayer, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY))
                    sendPacket(PlayerInteractBlockC2SPacket(event.hand, event.hitResult, 0))
                    sendPacket(ClientCommandC2SPacket(cPlayer, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY))
                }

                event.cirValue = stack.useOnBlock(ItemUsageContext(mc.player, event.hand, event.hitResult))
            } else event.cirValue = ActionResult.PASS
        }

        (mc as IMinecraftClient).setItemUseCooldown((placeDelay.value * 4).toInt())

        if (!event.hitResult.side.axis.isVertical || !buildHeight.toggled) return
        val y = event.hitResult.blockPos.y
        val side = event.hitResult.side
        (event.hitResult as IBlockHitResult).setSide(if (y >= 320 || y <= -64) side.opposite else side)
    }

    private fun blockCheck(b: Block) = blocks.settings.filterIsInstance<SettingMap>().any { it.key == b::class.java && it.toggled }
}