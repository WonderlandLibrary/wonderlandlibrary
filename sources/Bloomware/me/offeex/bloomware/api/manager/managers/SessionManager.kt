package me.offeex.bloomware.api.manager.managers

import me.offeex.bloomware.Bloomware
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.manager.Manager
import me.offeex.bloomware.api.helper.ProtectionMark
import me.offeex.bloomware.event.events.EventBeginRenderTick
import me.offeex.bloomware.event.events.EventMovement
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.network.packet.s2c.play.BundleS2CPacket
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket
import net.minecraft.util.math.Vec3d
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt
import kotlin.math.sqrt

object SessionManager : Manager() {
	var pops = 0
	var deaths = 0
	var kills = 0
	var tps = 0f

	var timeOnline: Long = 0
		get() = (System.currentTimeMillis() - field) / 1000

	var timer = 1f
	var deltaPos: Vec3d = Vec3d.ZERO
	lateinit var lastTimePacket: WorldTimeUpdateS2CPacket

	private var rubberband = false


	private fun calculateTps(n: Double) = 20.0 / ((n - 1000.0) / 500.0).coerceAtLeast(1.0)

	@Subscribe
	private fun onPacketReceive(event: EventPacket.Receive) {
		when(val p = event.packet) {
			is BundleS2CPacket -> {
				p.packets.forEach { Bloomware.EVENTBUS.post(EventPacket.Receive(it)) } // катун блять спасибо
			}
			is EntityStatusS2CPacket -> {
				if (p.status.toInt() == 35 && p.getEntity(mc.world) === mc.player)
					pops++
			}
			is WorldTimeUpdateS2CPacket -> {
				lastTimePacket = p
				val tmp = (calculateTps(System.currentTimeMillis() - p.time.toDouble()) * 100).roundToInt()
				tps = tmp / 100f
			}
			is PlayerPositionLookS2CPacket -> {
				rubberband = true
			}
		}
	}

	@Subscribe
	private fun onMovementPackets(event: EventMovement.Packets) {
		if (rubberband) {
			deltaPos = Vec3d.ZERO
			rubberband = false
			return
		}
		deltaPos = Vec3d(event.deltaX, event.deltaY, event.deltaZ)
	}

	@Subscribe
	private fun onBeginRenderTick(event: EventBeginRenderTick) {
		event.multiplier = timer
	}

	@Subscribe
	private fun onScreenOpen(event: EventPacket) {
		protection()
	}

	fun horizontalSpeed() = sqrt(deltaPos.x * deltaPos.x + deltaPos.z * deltaPos.z)

	fun reset() {
		pops = 0
		deaths = 0
		kills = 0
		timeOnline = 0
	}

	fun start() {
		timeOnline = System.currentTimeMillis()
	}

	fun convertTime(time: Long): String {
		val calendar = Calendar.getInstance()
		calendar.timeInMillis = time
		return SimpleDateFormat("HH hours, mm minutes, ss minutes").format(Date(time))
	}

	@ProtectionMark
	fun protection() {
	}
}