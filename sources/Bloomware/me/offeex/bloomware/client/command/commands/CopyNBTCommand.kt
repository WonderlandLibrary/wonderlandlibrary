package me.offeex.bloomware.client.command.commands

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.util.ChatUtil.addMessage
import me.offeex.bloomware.api.util.ChatUtil.errorMessage
import me.offeex.bloomware.api.util.ChatUtil.plus
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.client.command.Command
import net.minecraft.item.ItemStack
import net.minecraft.util.Formatting

object CopyNBTCommand : Command("copynbt", {
	fun copy(item: ItemStack) {
		if (!item.isEmpty) {
			mc.keyboard.clipboard = item.nbt.toString().replace("\u00a7", "\\247")
			addMessage(Formatting.GREEN + "NBT was copied to clipboard!")
		} else errorMessage("Your main hand slot is empty!")
	}

	literal("mainhand").runs { copy(cPlayer.mainHandStack) }
	literal("offhand").runs { copy(cPlayer.offHandStack) }
}) {
	override val description = "allows you to copy nbt data from held item"
}