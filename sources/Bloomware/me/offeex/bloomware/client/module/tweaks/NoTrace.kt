package me.offeex.bloomware.client.module.tweaks

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.util.InventoryUtil.findHand
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventRaycast
import net.minecraft.item.PickaxeItem
import net.minecraft.util.Hand
import net.minecraft.util.hit.HitResult

object NoTrace : Module("NoTrace", "Allows you to interact with blocks through entities", Category.TWEAKS) {
    private val onlyPickaxe = setting("OnlyPickaxe").bool(true)

    @Subscribe
    private fun onRaycast(event: EventRaycast.TargetedEntity) {
        val cancel = !onlyPickaxe.toggled || findHand<PickaxeItem>() == Hand.MAIN_HAND
        if (cancel && mc.crosshairTarget?.type == HitResult.Type.BLOCK) event.entityHitResult = null
    }
}