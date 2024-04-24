package me.offeex.bloomware.client.module.network

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.api.util.ChatUtil
import me.offeex.bloomware.api.util.ChatUtil.plus
import me.offeex.bloomware.api.util.InventoryUtil.getDurability
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventDamage
import me.offeex.bloomware.event.events.EventEntity
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Formatting

object Notifier : Module("Notifier", "Notifies you about different events", Category.NETWORK) {
	private val joins = setting("Joins").group(true)
	private val threshold = joins.setting("TicksThreshold").number(30, 0, 80, 1)
	private val totemPops = setting("TotemPops").group(true)
	private val visualRange = setting("VisualRange").group(true)
	private val armorAlert = setting("ArmorAlert").group(true)
	private val durability = armorAlert.setting("Durability").number(30, 1, 100, 1)
	private val sound = armorAlert.setting("Sound").bool(true)

	private var pops: HashMap<String, Int> = HashMap()
	private val armorMap = hashMapOf(
		EquipmentSlot.HEAD to false,
		EquipmentSlot.CHEST to false,
		EquipmentSlot.LEGS to false,
		EquipmentSlot.FEET to false,
	)

	@Subscribe
	private fun onPacketReceive(event: EventPacket.Receive) {
		when (val packet = event.packet) {
			is PlayerListS2CPacket -> {
				if (cPlayer.age < threshold.value || !joins.toggled) return

				val entry = packet.entries[0]
				val name = entry.profile.name
				if (entry == null || name == null || name == cNetHandler.profile.name) return

				val actionStr = if (entry.listed) "joined" else "left"
				ChatUtil.addMessage("${Formatting.GRAY}$name $actionStr the server.")
			}

			is EntityStatusS2CPacket -> {
				if (packet.status.toInt() != 35 || !totemPops.toggled) return
				applyPop(packet.getEntity(Bloomware.mc.world) ?: return)
			}
		}
	}

	override fun onEnable() {
		if (armorAlert.toggled) for (m in armorMap) m.setValue(false)
	}

	override fun onDisable() {
		pops.clear()
		for (m in armorMap) m.setValue(false)
	}

	override fun onTick() {
		if (!totemPops.toggled) {
			cWorld.players.forEach {
				if (it.health > 0 || !pops.containsKey(it.entityName)) return@forEach
				val totems = pops[it.entityName]!!.totems()
				ChatUtil.addMessage("${Formatting.ITALIC} ${it.entityName} ${Formatting.RESET} died after popping ${Formatting.RED} $totems")
				pops.remove(it.entityName, pops[it.entityName])
			}
		}
	}

	@Subscribe
	private fun onDamageArmor(event: EventDamage.Armor) {
		if (!armorAlert.toggled) return

		cPlayer.itemsEquipped.iterator().forEachRemaining {
			val item = it.item
			if (item !is ArmorItem || it === cPlayer.mainHandStack || it === cPlayer.offHandStack) return@forEachRemaining
			val alerted = armorMap[item.slotType] ?: return@forEachRemaining

			if (it.getDurability() * 100 >= durability.value) {
				armorMap[item.slotType] = false
				return@forEachRemaining
			}

			if (!alerted) {
				warning(it)
				armorMap[item.slotType] = true
			}
		}
	}

	@Subscribe
	private fun onEntity(event: EventEntity) {
		if (!visualRange.toggled) return

		val e = cWorld.getEntityById(event.id)
		val presence = if (event is EventEntity.Add) "entered" else "left"
		if (e is PlayerEntity && e != cPlayer) ChatUtil.addMessage("${e.entityName} has $presence your visual range!")
	}

	private fun applyPop(entity: Entity) {
		pops[entity.entityName] = if (pops[entity.entityName] == null) 1 else pops[entity.entityName]!! + 1
		val totems = pops[entity.entityName]!!.totems()
		ChatUtil.addMessage("${Formatting.ITALIC} ${entity.entityName} ${Formatting.RESET} popped ${Formatting.RED} ${pops[entity.entityName]} ${Formatting.WHITE} $totems")
	}

	private fun warning(stack: ItemStack) {
		val item = Formatting.AQUA + stack.item.name.string + Formatting.RED
		ChatUtil.addMessage("${Formatting.RED}Your $item is about to break!")
		if (sound.toggled) {
			cWorld.playSound(null, cPlayer.blockPos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1f, 1f)
		}
	}

	private fun Int.totems() = "totem" + if (this > 1) "s" else ""

}