package me.offeex.bloomware.client.module.player

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.manager.managers.FriendManager.PersonType
import me.offeex.bloomware.api.manager.managers.FriendManager.addPerson
import me.offeex.bloomware.api.manager.managers.FriendManager.getType
import me.offeex.bloomware.api.manager.managers.FriendManager.removePerson
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.api.util.ChatUtil.plus
import me.offeex.bloomware.api.util.InventoryUtil.findHotbarSlot
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items.ENDER_PEARL
import net.minecraft.item.Items.EXPERIENCE_BOTTLE
import net.minecraft.util.Formatting
import net.minecraft.util.Hand

object MiddleClick :
	Module("MiddleClick", "Allows you to use different items when you click middle button.", Category.PLAYER) {
	private val friends = setting("Friend").bool(true)
	private val mode = setting("Mode").enum("Pearl", "EXP", "Off")
	override fun onTick() {
		if (mc.options.pickItemKey.isPressed) {
			if (friends.toggled && mc.targetedEntity is PlayerEntity) {
				val player = mc.targetedEntity as PlayerEntity
				val target = player.entityName
				val type = getType(target)
				if (type !== PersonType.FRIEND) {
					addMessage(Formatting.GREEN + player.entityName + " now is your friend.")
					addPerson(target, PersonType.FRIEND)
				} else {
					addMessage(Formatting.GREEN + player.entityName + " was removed from your friends list.")
					removePerson(target)
				}
				mc.options.pickItemKey.isPressed = false
				return
			}

			if (mode.like("Off")) return

			val itemSlot = (if (mode.like("Pearl")) ENDER_PEARL else EXPERIENCE_BOTTLE).findHotbarSlot()
			val oldSlot = cPlayer.inventory.selectedSlot
			if (itemSlot != -1) {
				if (mode.like("Pearl")) mc.options.pickItemKey.isPressed = false
				cPlayer.inventory.selectedSlot = itemSlot
				cPlayer.mainHandStack.use(mc.world, cPlayer, Hand.MAIN_HAND)
				cPlayer.inventory.selectedSlot = oldSlot
			}
		}
	}
}