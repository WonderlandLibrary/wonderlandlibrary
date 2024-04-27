package kevin.module.modules.misc

import kevin.module.*
import kevin.event.*
import kevin.main.KevinClient
import kevin.module.modules.player.Blink
import kevin.module.modules.render.FreeCam
import kevin.utils.RandomUtils
import net.minecraft.network.play.client.*
import net.minecraft.network.play.client.C03PacketPlayer.*
import net.minecraft.util.Vec3

// From IDK where originally(FDP maybe), but I changed something
class BadPacketPrevention: Module("AntiBadPackets", "Prevent you get flags from some bad packets we sent", category = ModuleCategory.MISC) {
    // settings
    private val fixBlinkAndFreecam = BooleanValue("CancelKeepAliveWhenPacketFreeze", true)
    private val fixPacketPlayer = BooleanValue("FixC03Streak", true)
    private val fixItemSwap = BooleanValue("FixItemSwap", true)
    private val fixGround = BooleanValue("FixMathGround", true)
    private val fixIdleFly = BooleanValue("NoPacketWhenIdle", false)
    private val fixAB = BooleanValue("FixAutoBlockBadPacket", false)
    private val fixAutoBlockInteract = BooleanValue("FixAutoBlockInteract", false)

    // local variables
    private var x = 0.0
    private var y = 0.0
    private var z = 0.0
    private var yaw = 0.0F
    private var pitch = 0.0F
    private var jam = 0
    private var packetCount = 0
    private var prevSlot = -1
    private var hasAction = false
    private var attackPacket: C02PacketUseEntity? = null

    // events
    override fun onEnable() {
        jam = 0
        packetCount = 0
        prevSlot = -1

        if (mc.thePlayer == null) return
        x = mc.thePlayer.posX
        y = mc.thePlayer.posY
        z = mc.thePlayer.posZ
        yaw = mc.thePlayer.rotationYaw
        pitch = mc.thePlayer.rotationPitch
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        if (mc.thePlayer == null || mc.theWorld == null) return

        val packet = event.packet

        // fix ground check (4I)
        if (fixGround.get() && packet is C03PacketPlayer && packet !is C04PacketPlayerPosition && packet !is C06PacketPlayerPosLook) {
            if ((mc.thePlayer.motionY == 0.0 || (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically)) && !packet.onGround)
                packet.onGround = true
        }

        // some info things
        if (packet is C04PacketPlayerPosition) {
            x = packet.x
            y = packet.y
            z = packet.z
            jam = 0
        }

        if (packet is C05PacketPlayerLook) {
            yaw = packet.yaw
            pitch = packet.pitch
        }

        if (packet is C06PacketPlayerPosLook) {
            x = packet.x
            y = packet.y
            z = packet.z
            jam = 0

            yaw = packet.yaw
            pitch = packet.pitch
        }

        // fix bad packets, caused by timer or fast use
        if (fixPacketPlayer.get() && packet is C03PacketPlayer && packet !is C04PacketPlayerPosition && packet !is C06PacketPlayerPosLook) {
            jam++
            if (jam > 20) {
                jam = 0
                event.cancelEvent()
                mc.netHandler.networkManager.sendPacketNoEvent(C04PacketPlayerPosition(x, y, z, packet.onGround))
            }
        }

        // fix duplicated hot bar switch
        if (fixItemSwap.get() && packet is C09PacketHeldItemChange) {
            if (packet.slotId == prevSlot) {
                event.cancelEvent()
            } else {
                prevSlot = packet.slotId
            }
        }

        // fix blink and freecam cancelling c03s while sending c00
        if (fixBlinkAndFreecam.get() && (KevinClient.moduleManager.getModule(Blink::class.java).state || KevinClient.moduleManager.getModule(FreeCam::class.java).state) && packet is C00PacketKeepAlive)
            event.cancelEvent()

        // fix fly while not moving, reduce some checks (4C)
        if (fixIdleFly.get() && packet is C03PacketPlayer && !packet.onGround) {
            if (packet !is C04PacketPlayerPosition && packet !is C05PacketPlayerLook && packet !is C06PacketPlayerPosLook) {
                packetCount++
                if (packetCount >= 2)
                    event.cancelEvent()
            } else {
                packetCount = 0
            }
        }

        if (fixAB.get()) {
            when (packet) {
                is C02PacketUseEntity -> {
                    if (hasAction) {
                        event.cancelEvent()
                    } else attackPacket =
                        if (packet.action == C02PacketUseEntity.Action.ATTACK) packet
                        else null
                }
                is C03PacketPlayer -> {
                    hasAction = false
                    attackPacket = null
                }
                is C07PacketPlayerDigging -> {
                    if (attackPacket == null) {
                        if (packet.status == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM) hasAction = true
                    } else event.cancelEvent()
                }
                is C08PacketPlayerBlockPlacement -> {
                    if (fixAutoBlockInteract.get()) attackPacket?.let {
                        val entity = it.getEntityFromWorld(mc.theWorld)
                        mc.netHandler.networkManager.sendPacketNoEvent(C02PacketUseEntity(entity, Vec3(RandomUtils.nextDouble(-0.1, 0.1), RandomUtils.nextDouble(0.0, 0.5), RandomUtils.nextDouble(-0.1, 0.1))))
                    }
                }
            }
        }
    }

}