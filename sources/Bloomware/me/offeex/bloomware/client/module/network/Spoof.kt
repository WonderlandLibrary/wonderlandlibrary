package me.offeex.bloomware.client.module.network

import io.netty.buffer.Unpooled
import me.offeex.bloomware.client.module.Module
import me.offeex.bloomware.client.setting.setting
import me.offeex.bloomware.event.dispatcher.Subscribe
import me.offeex.bloomware.event.events.EventPacket
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket
import java.nio.charset.StandardCharsets

object Spoof : Module("Spoof", "Sends false data to server", Category.NETWORK) {
    private val client = setting("Client").bool(true)
    private val ping = setting("Ping").group(false)
    private val pingValue = ping.setting("Value").number(300.0, 200.0, 1000.0, 1.0)

    private var lastPacket: KeepAliveC2SPacket? = null
    private var timer: Long = 0

    @Subscribe
    private fun onPacketSend(event: EventPacket.Send) {
        val packet = event.packet

        if (packet is CustomPayloadC2SPacket && client.toggled) {
            if (packet.channel == CustomPayloadC2SPacket.BRAND) packet.write(PacketByteBuf(Unpooled.buffer()).writeString("vanilla"))
            event.canceled = packet.data.toString(StandardCharsets.UTF_8).contains("fabric")
        } else if (packet is KeepAliveC2SPacket) {
            if (packet === lastPacket) return
            lastPacket = packet
            event.canceled = true
            timer = System.currentTimeMillis()
        }
    }

    override fun onTick() {
        if (System.currentTimeMillis() - timer >= pingValue.value && lastPacket != null) {
            sendPacket(lastPacket!!)
            lastPacket = null
        }
    }
}