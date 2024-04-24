package me.offeex.bloomware.client.module.pvp

import me.offeex.bloomware.api.helper.cNetHandler
import me.offeex.bloomware.api.helper.cPlayer
import me.offeex.bloomware.api.util.WorldUtil
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventInteract
import net.minecraft.item.Items
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket

object BowBomb : Module("BowBomb", "Bow exploit", Category.PVP) {
	private val amount = setting("Amount").number(5.0, 5.0, 127.0, 1.0)

	//    TODO: future...
	@Subscribe
	private fun onShoot(event: EventInteract) {
		cNetHandler.apply {
//        Hand hand = InventoryUtil.findHandItem(Items.ANVIL);
			if (cPlayer.mainHandStack.isOf(Items.BOW)) {
				sendPacket(ClientCommandC2SPacket(cPlayer, ClientCommandC2SPacket.Mode.START_SPRINTING))
				val t = WorldUtil.target(12.0, "Distance", true, false, true, true)
				val x: Double
				val y: Double
				val z: Double
				val yaw: Float
				val pitch: Float
				if (FGM148Javelin.enabled) {
					if (t == null) return
					x = t.x - 2.2
					y = t.y
					z = t.z
					yaw = -90f
					pitch = 0f
				} else {
					x = cPlayer.x
					y = cPlayer.y
					z = cPlayer.z
					yaw = cPlayer.yaw
					pitch = cPlayer.pitch
				}

//            getY() packet iteration way
				var i: Byte = 0
				while (i < amount.value) {
					if (cPlayer.hasVehicle()) {
						val vehicle = cPlayer.vehicle
						vehicle!!.setPos(vehicle.x, vehicle.y + 1.0E-9, vehicle.z)
						sendPacket(VehicleMoveC2SPacket(vehicle))
						vehicle.setPos(vehicle.x, vehicle.y - 1.0E-9, vehicle.z)
						sendPacket(VehicleMoveC2SPacket(vehicle))
					} else {
						sendPacket(Full(x, y, z, yaw, pitch, true))
						sendPacket(Full(x, y + 1.0E-9, z, yaw, pitch, false))
					}
					i++
				}
			}
		}
	}
}