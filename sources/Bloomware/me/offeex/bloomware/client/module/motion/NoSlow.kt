package me.offeex.bloomware.client.module.motion

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.gui.screen.ClickGUI
import me.offeex.bloomware.api.gui.screen.HudEditor
import me.offeex.bloomware.api.util.ClientUtil.isActuallyPressed
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.client.setting.settings.SettingNumber
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventInput
import me.offeex.bloomware.event.events.EventInput.Slowdown
import me.offeex.bloomware.mixin.accessor.IAbstractBlock
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Blocks
import net.minecraft.client.gui.screen.GameMenuScreen
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.option.KeyBinding

object NoSlow : Module("NoSlow", "Allows you to go faster in different situations", Category.MOTION) {
    val items = setting("Items").bool()
    val soulSand = setting("SoulSand").bool()
    val webs = setting("Web").bool()
    val slimeBlock = setting("SlimeBlock").bool()
    val berryBush = setting("BerryBush").bool()
    private val sneak = setting("Sneak").bool()
    private val invMove = setting("InventoryMove").bool()

    private val iceSpeed = setting("IceSpeed").group()
    private val defaultIce = iceSpeed.setting("Default").number(0.5, 0.1, 0.98, 0.01)
    private val frostedIce = iceSpeed.setting("Frosted").number(0.5, 0.1, 0.98, 0.01)
    private val blueIce = iceSpeed.setting("Blue").number(0.98, 0.1, 0.98, 0.01)
    private val packedIce = iceSpeed.setting("Packed").number(0.98, 0.1, 0.98, 0.01)

    private val ices = hashMapOf<SettingNumber, AbstractBlock>(
        defaultIce to Blocks.ICE,
        frostedIce to Blocks.FROSTED_ICE,
        blueIce to Blocks.BLUE_ICE,
        packedIce to Blocks.PACKED_ICE
    )

    override fun onEnable() {
        ices.forEach {
            (it.value as IAbstractBlock).setSlipperiness(it.key.value.toFloat())
        }
    }

    override fun onDisable() {
        ices.forEach {
            (it.value as IAbstractBlock).setSlipperiness(it.key.defaultValue.toFloat())
        }
    }

    @Subscribe
    private fun onShouldSlowDown(event: Slowdown) {
        event.canceled = sneak.toggled
    }

    @Subscribe
    private fun onKeyBinding(event: EventInput.Binding) {
        if (event.binding.category != KeyBinding.MOVEMENT_CATEGORY) return
        val actuallyPressed: Boolean = event.binding.isActuallyPressed()
        val whitelist = mc.currentScreen is HandledScreen<*>
            || mc.currentScreen is GameMenuScreen
            || mc.currentScreen is ClickGUI
            || mc.currentScreen is HudEditor
        if (whitelist && actuallyPressed && invMove.toggled) event.pressed = true // InventoryMove
    }
}