package me.offeex.bloomware.client.module.visuals

import me.offeex.bloomware.Bloomware.mc
import me.offeex.bloomware.api.helper.cWorld
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import me.offeex.bloomware.event.events.EventUpdate
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket
import java.text.SimpleDateFormat
import java.util.*

object Environment : Module("Environment", "Changes various color of the utilities", Category.RENDER) {

    val sky = setting("Sky").group(true)
    val skyColor = sky.setting("Color").color(100, 100, 100, 255)
    val clouds = setting("Clouds").group(true)
    val cloudsColor = clouds.setting("Color").color()
    val fog = setting("Fog").group(true)
    val fogColor = fog.setting("Color").color()

    val grass = setting("Grass").group(true)
    val grassColor = grass.setting("Color").color(0, 206, 68, 255)
    val water = setting("Water").group(true)
    val waterColor = water.setting("Color").color(0, 206, 68)
    val leaves = setting("Leaves").group(true)
    val leavesColor = leaves.setting("Color").color(27, 252, 42, 255)

    private val customTime = setting("CustomTime").group(false)
    val time = customTime.setting("Time").number(18000.0, 0.0, 24000.0, 1.0)
    val timeMode = customTime.setting("Mode").enum("Static", "Sync")

    private var serverTimeOfDay: Long = 0
    private var oldGrassC = 0
    private var oldWaterC = 0
    private var oldLeavesC = 0
    private var oldTrigger = false
    var trigger = false

    init {
        clouds.toggledUpdateBus.subscribe { _, _ -> updateTrigger(trigger, oldTrigger) }
        grass.toggledUpdateBus.subscribe { _, _ -> mc.worldRenderer.reload() }
        water.toggledUpdateBus.subscribe { _, _ -> mc.worldRenderer.reload() }
        leaves.toggledUpdateBus.subscribe { _, _ -> mc.worldRenderer.reload() }
    }

    override fun onEnable() = mc.worldRenderer.reload()

    override fun onDisable() {
        mc.worldRenderer.reload()
        if (customTime.toggled) cWorld.timeOfDay = serverTimeOfDay
    }

    override fun onTick() {
        if (mc.world == null) return
        if (grassColor.color.argb != oldGrassC || waterColor.color.argb != oldWaterC || leavesColor.color.argb != oldLeavesC) mc.worldRenderer.reload()
        oldGrassC = grassColor.color.argb
        oldWaterC = waterColor.color.argb
        oldLeavesC = leavesColor.color.argb
        if (oldTrigger == trigger) trigger = false
    }

    @Subscribe
    private fun onUpdate(event: EventUpdate.TimeOfDay) {
        if (!customTime.toggled) return
        event.time =
			if (timeMode.like("Static")) time.value.toLong()
			else convertToTicks(SimpleDateFormat("HH:mm").format(Calendar.getInstance().time)) - 3000
    }

    @Subscribe
    private fun onPacketReceive(event: EventPacket.Receive) {
        if (event.packet is WorldTimeUpdateS2CPacket && !event.shift && customTime.toggled)
            serverTimeOfDay = event.packet.timeOfDay
    }

    private fun convertToTicks(time: String): Long {
        val data = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return data[0].toInt() * 1000L + data[1].toInt() * 16L
    }

    private fun updateTrigger(trigger: Boolean, oldTrigger: Boolean) {
        this.trigger = trigger
        this.oldTrigger = oldTrigger
    }
}