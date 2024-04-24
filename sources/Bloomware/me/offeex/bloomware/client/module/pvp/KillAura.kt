package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cInteractManager
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.RotationManager.reset
import me.offeex.bloomware.api.manager.managers.RotationManager.sendPacket
import me.offeex.bloomware.api.manager.managers.RotationManager.setRotation
import me.offeex.bloomware.api.util.CPlayerUtil
import me.offeex.bloomware.api.util.InventoryUtil.findHotbarSlot
import me.offeex.bloomware.api.util.RotationUtil
import me.offeex.bloomware.api.util.WorldUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.entity.Entity
import net.minecraft.item.*
import net.minecraft.util.Hand

object KillAura : Module("KillAura", "Automatically attacks entities", Category.PVP) {
    private val range = setting("Range").number(4.0, 1.0, 6.0, 0.1)
    private val fov = setting("FOV").number(90.0, 0.0, 180.0, 1.0)
    private val sort = setting("Sort").enum("Distance", "Health")

    private val onlyWeapon = setting("OnlyWeapon").bool()
    private val autoSwitch = setting("AutoSwitch").bool()

    private val delay = setting("Delay").bool(true)
    private val rotate = setting("Rotate").bool()
    private val box = setting("Box").enum("Head", "Chest", "Legs").depend(rotate) { rotate.toggled }

    private val targets = setting("Targets").group()
    private val players = targets.setting("Players").bool(true)
    private val friends = targets.setting("Friends").bool()
    private val animals = targets.setting("Animals").bool()
    private val hostiles = targets.setting("Hostiles").bool(true)

    private val disableOnDeath = setting("DisableOnDeath").bool(true)

    override fun onDisable() {
        reset()
    }

    override fun onTick() {
        val target = WorldUtil.target(
            range.value, sort.selected, players.toggled, friends.toggled, hostiles.toggled, animals.toggled
        )

        if (target == null || RotationUtil.yawToPos(target.pos) > fov.value) return reset()

        if (rotate.toggled) {
            when (box.selected) {
                "Head" -> setRotation(target.eyePos)
                "Chest" -> setRotation(target.pos.add(0.0, 1.0, 0.0))
                "Legs" -> setRotation(target.pos)
            }
            if (RotationUtil.shouldRotate()) sendPacket()
        }

        cPlayer.also {
            if (!delay.toggled || it.getAttackCooldownProgress(mc.tickDelta) == 1.0f) {
                val swordSlot = findHotbarSlot<SwordItem>()
                val axeSlot = findHotbarSlot<AxeItem>()
                val pickSlot = findHotbarSlot<PickaxeItem>()
                val shovelSlot = findHotbarSlot<ShovelItem>()
                val isPresent = shovelSlot != -1 || pickSlot != -1 || axeSlot != -1 || swordSlot != -1
                if (autoSwitch.toggled && isPresent) it.inventory.selectedSlot =
                    if (swordSlot != -1) swordSlot
                    else (if (axeSlot != 1) axeSlot
                    else if (pickSlot != -1) pickSlot
                    else if (shovelSlot != -1) shovelSlot
                    else it.inventory.selectedSlot)

                if (!onlyWeapon.toggled || isWeapon(it.mainHandStack.item)) attack(target)

                if (it.isDead && disableOnDeath.toggled) disable()
            }
        }

//        reset()
    }

    private fun isWeapon(item: Item) = (item is SwordItem || item is AxeItem || item is PickaxeItem || item is ShovelItem)

    private fun attack(e: Entity) {
        cInteractManager.attackEntity(cPlayer, e)
        cPlayer.swingHand(Hand.MAIN_HAND)
    }
}
