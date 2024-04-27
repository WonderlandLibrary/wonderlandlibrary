/*
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
@file:Suppress("UNCHECKED_CAST")

package kevin.module.modules.combat

import kevin.event.*
import kevin.hud.element.elements.Notification
import kevin.main.KevinClient
import kevin.module.*
import kevin.utils.*
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.NetworkManager
import net.minecraft.network.Packet
import net.minecraft.network.ThreadQuickExitException
import net.minecraft.network.play.INetHandlerPlayClient
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.server.*
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Vec3
import org.lwjgl.opengl.GL11.*
import java.awt.Color
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max

class BackTrack: Module("BackTrack", "Lets you attack people in their previous locations", category = ModuleCategory.COMBAT) {
    private val minDistance: FloatValue = object : FloatValue("MinDistance", 2.9f, 2f, 4f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            if (newValue > maxStartDistance.get()) set(maxStartDistance.get())
        }
    }
    private val maxStartDistance : FloatValue = object : FloatValue("MaxStartDistance", 3.2f, 2f, 4f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            if (newValue < minDistance.get()) set(minDistance.get())
            else if (newValue > maxDistance.get()) set(maxDistance.get())
        }
    }
    private val maxDistance: FloatValue = object : FloatValue("MaxActiveDistance", 5f, 2f, 6f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            if (newValue < maxStartDistance.get()) set(maxStartDistance.get())
        }
    }
    private val mode = ListValue("Mode", arrayOf("Legacy", "Smooth"), "Legacy")
    private val smoothRangeToKeep by object : FloatValue("Smooth-RangeToKeep", 3f, 2f..6f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            if (newValue > maxDistance.get()) set(maxDistance.get())
        }

        override fun isSupported(): Boolean = mode equal "Smooth"
    }
    private val minTime : IntegerValue = object : IntegerValue("MinTime", 100, 0, 500) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            if (newValue > maxTime.get()) set(maxTime.get())
        }

        override fun isSupported(): Boolean = mode equal "Legacy"
    }
    private val maxTime : IntegerValue = object : IntegerValue("MaxTime", 200, 0, 1000) {
        override fun onChanged(oldValue: Int, newValue: Int) {
            if (newValue < minTime.get()) set(minTime.get())
        }
    }
    private val smartPacket = BooleanValue("Smart", true)
    private val maxHurtTime = IntegerValue("MaxHurtTime", 6, 0, 10)
    private val hurtTimeWithPing = BooleanValue("CalculateHurtTimeWithPing", true)
    private val minAttackReleaseRange = FloatValue("MinAttackReleaseRange", 3.5F, 2f, 6f) { mode equal "Legacy" }

    private val onlyKillAura = BooleanValue("OnlyKillAura", true)
    private val onlyPlayer = BooleanValue("OnlyPlayer", true)
    private val resetOnVelocity = BooleanValue("ResetOnVelocity", true)
    private val resetOnLagging = BooleanValue("ResetOnLagging", true)
    private val setPosOnStop = BooleanValue("SetPositionOnStop", false)
    private val rangeCheckMode = ListValue("RangeCheckMode", arrayOf("RayCast", "DirectDistance"), "DirectDistance")

    private val reverse = BooleanValue("Reverse", false)
    private val reverseRange : FloatValue = object : FloatValue("ReverseStartRange", 5f, 1f, 6f) {
        override fun onChanged(oldValue: Float, newValue: Float) = set(newValue.coerceAtMost(reverseMaxRange.get()))

        override fun isSupported(): Boolean = reverse.get()
    }

    private val reverseMaxRange : FloatValue = object : FloatValue("ReverseMaxRange", 6f, 1f, 6f) {
        override fun onChanged(oldValue: Float, newValue: Float) = set(newValue.coerceAtLeast(reverseRange.get()))

        override fun isSupported(): Boolean = reverse.get()
    }
    private val reverseSelfMaxHurtTime = IntegerValue("ReverseSelfMaxHurtTime", 1, 0, 10) { reverse.get() }
    private val reverseTargetMaxHurtTime = IntegerValue("ReverseTargetMaxHurtTime", 10, 0, 10) { reverse.get() }
    private val maxReverseTime = IntegerValue("MaxReverseTime", 100, 1, 500) { reverse.get() }

    private val espMode = ListValue("ESPMode", arrayOf("FullBox", "OutlineBox", "NormalBox", "OtherOutlineBox", "OtherFullBox", "Model", "None"), "Box")
    private val espRed by IntegerValue("EspRed", 32, 0..255)
    private val espGreen by IntegerValue("EspGreen", 255, 0..255)
    private val espBlue by IntegerValue("EspBlue", 32, 0..255)
    private val espAlpha by IntegerValue("EspAlpha", 35, 0..255)
    private val outlineRed by IntegerValue("OutlineRed", 32, 0..255)
    private val outlineGreen by IntegerValue("OutlineGreen", 200, 0..255)
    private val outlineBlue by IntegerValue("OutlineBlue", 32, 0..255)
    private val outlineAlpha by IntegerValue("OutlineAlpha", 255, 0..255)
    private val alwaysRenderESP = BooleanValue("AlwaysRenderESP", false)

    private val storagePackets = ArrayList<ServerPacketStorage>()
    private val storageSendPackets = ArrayList<Packet<INetHandlerPlayServer>>()
    private val storageEntities : ArrayList<Entity> = object : ArrayList<Entity>() {
        override fun add(element: Entity): Boolean {
            if (element is EntityOtherPlayerMP && espMode equal "Model") {
                val mp = EntityOtherPlayerMP(mc.theWorld, element.gameProfile)
                mp.entityId = mdfId(element.entityId)
                mp.isInvisible = false
                mp.posX = element.serverPosX / 32.0
                mp.posY = element.serverPosY / 32.0
                mp.posZ = element.serverPosZ / 32.0
                mp.prevPosX = mp.posX
                mp.prevPosY = mp.posY
                mp.prevPosZ = mp.posZ
                mp.lastTickPosX = mp.prevPosX
                mp.lastTickPosY = mp.prevPosY
                mp.lastTickPosZ = mp.prevPosZ
                mp.rotationYaw = element.rotationYaw
                mp.rotationPitch = element.rotationPitch
                mp.rotationYawHead = element.rotationYawHead
                mp.prevRotationYaw = mp.rotationYaw
                mp.prevRotationPitch = mp.rotationPitch
                mp.prevRotationYawHead = mp.rotationYawHead
                mp.swingProgress = element.swingProgress
                mp.swingProgressInt = element.swingProgressInt
                mp.hurtTime = element.hurtTime
                mp.hurtResistantTime = element.hurtResistantTime
                element.getInventory().copyInto(mp.getInventory())
                RenderUtils.regFakePlayer(mp)
            }
            return super.add(element)
        }

        override fun removeAt(index: Int): Entity {
            val removeAt = super.removeAt(index)
            if (removeAt is EntityOtherPlayerMP) RenderUtils.removeFakePlayer(mdfId(removeAt.entityId))
            return removeAt
        }
    }

    private val storageEntityMove = LinkedList<EntityPacketLoc>()

    private val killAura: KillAura by lazy { KevinClient.moduleManager.getModule(KillAura::class.java) }
//    private var currentTarget : EntityLivingBase? = null
    private var timer = MSTimer()
    private val reverseTimer = MSTimer()
    private var hasAttackInReversing = false
    private var lastPosition = Vec3(0.0, 0.0, 0.0)
    private var attacked : Entity? = null

    private var smoothPointer = System.nanoTime()
    var needFreeze = false
    var reversing = false

//    @EventTarget
    // for safety, see in met.minecraft.network.NetworkManager
    fun onPacket(event: PacketEvent) {
        mc.thePlayer ?: return
        val packet = event.packet
        val theWorld = mc.theWorld!!
        if (packet.javaClass.name.contains("net.minecraft.network.play.server.", true)) {
            val storage = ServerPacketStorage(packet as Packet<INetHandlerPlayClient>)
            if (packet is S14PacketEntity) {
                val entity = packet.getEntity(theWorld)?: return
                if (entity !is EntityLivingBase) return
                if (onlyPlayer.get() && entity !is EntityPlayer) return
                entity.serverPosX += packet.func_149062_c().toInt()
                entity.serverPosY += packet.func_149061_d().toInt()
                entity.serverPosZ += packet.func_149064_e().toInt()
                val x = entity.serverPosX.toDouble() / 32.0
                val y = entity.serverPosY.toDouble() / 32.0
                val z = entity.serverPosZ.toDouble() / 32.0
                if ((!onlyKillAura.get() || killAura.state || needFreeze) && EntityUtils.isSelected(entity, true)) {
                    val afterBB = AxisAlignedBB(x - 0.4F, y - 0.1F, z - 0.4F, x + 0.4F, y + 1.9F, z + 0.4F)
                    var afterRange: Double
                    var beforeRange: Double
                    if (rangeCheckMode equal "RayCast") {
                        afterRange = afterBB.getLookingTargetRange(mc.thePlayer!!)
                        beforeRange = mc.thePlayer.getLookDistanceToEntityBox(entity)
                        if (afterRange == Double.MAX_VALUE) {
                            val eyes = mc.thePlayer!!.getPositionEyes(1F)
                            afterRange = getNearestPointBB(eyes, afterBB).distanceTo(eyes) + 0.075
                        }
                        if (beforeRange == Double.MAX_VALUE) beforeRange = mc.thePlayer!!.getDistanceToEntityBox(entity) + 0.075
                    } else {
                        val eyes = mc.thePlayer!!.getPositionEyes(1F)
                        afterRange = getNearestPointBB(eyes, afterBB).distanceTo(eyes)
                        beforeRange = mc.thePlayer!!.getDistanceToEntityBox(entity)
                    }

                    if (beforeRange <= maxStartDistance.get()) {
                        if (afterRange in minDistance.get()..maxDistance.get() && (!smartPacket.get() || afterRange > beforeRange + 0.02) && entity.hurtTime <= calculatedMaxHurtTime) {
                            if (!needFreeze) {
                                timer.reset()
                                needFreeze = true
                                smoothPointer = System.nanoTime()
                                stopReverse()
                            }
                            if (!storageEntities.contains(entity)) storageEntities.add(entity)
                            event.cancelEvent()
                            if (mode equal "Smooth") {
                                storageEntityMove.add(EntityPacketLoc(entity, x, y, z))
                            }
                            return
                        }
                    } else {
                        if (smartPacket.get()) {
                            if (afterRange < beforeRange && (mode notEqual "Smooth" || afterRange <= smoothRangeToKeep - 0.7)) {
                                if (needFreeze) releasePackets()
                            }
                        }
                    }
                }
                if (needFreeze) {
                    if (!storageEntities.contains(entity)) storageEntities.add(entity)
                    if (mode equal "Smooth") {
                        storageEntityMove.add(EntityPacketLoc(entity, x, y, z))
                    }
                    event.cancelEvent()
                    return
                }
                if (!event.isCancelled && !needFreeze) {
                    KevinClient.eventManager.callEvent(EntityMovementEvent(entity))
                    val f = if (packet.func_149060_h()) (packet.func_149066_f() * 360).toFloat() / 256.0f else entity.rotationYaw
                    val f1 = if (packet.func_149060_h()) (packet.func_149063_g() * 360).toFloat() / 256.0f else entity.rotationPitch
                    entity.setPositionAndRotation2(x, y, z, f, f1, 3, false)
                    entity.onGround = packet.onGround
                }
                event.cancelEvent()
     //                storageEntities.add(entity)
            } else if (packet is S18PacketEntityTeleport) {
                val entity = theWorld.getEntityByID(packet.entityId)
                if (entity !is EntityLivingBase) return
                if (onlyPlayer.get() && entity !is EntityPlayer) return
                entity.serverPosX = packet.x
                entity.serverPosY = packet.y
                entity.serverPosZ = packet.z
                val d0 = entity.serverPosX.toDouble() / 32.0
                val d1 = entity.serverPosY.toDouble() / 32.0
                val d2 = entity.serverPosZ.toDouble() / 32.0
                val f: Float = (packet.yaw * 360).toFloat() / 256.0f
                val f1: Float = (packet.pitch * 360).toFloat() / 256.0f
                if (!needFreeze) {
                    if (!(abs(entity.posX - d0) >= 0.03125) && !(abs(entity.posY - d1) >= 0.015625) && !(abs(entity.posZ - d2) >= 0.03125)) {
                        entity.setPositionAndRotation2(entity.posX, entity.posY, entity.posZ, f, f1, 3, true)
                    } else {
                        entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, true)
                    }
                    entity.onGround = packet.onGround
                } else storageEntityMove.add(EntityPacketLoc(entity, d0, d1, d2))
                event.cancelEvent()
            } else {
                if ((packet is S12PacketEntityVelocity && resetOnVelocity.get()) || (packet is S08PacketPlayerPosLook && resetOnLagging.get())) {
                    storagePackets.add(storage)
                    event.cancelEvent()
                    releasePackets()
                    return
                }
                if (needFreeze && !event.isCancelled) {
                    if (packet is S19PacketEntityStatus) {
                        if (packet.opCode == 2.toByte()) return
                    }
                    storagePackets.add(storage)
                    event.cancelEvent()
                }
            }
        } else {
            if (reversing) {
                event.cancelEvent()
                storageSendPackets.add(packet as Packet<INetHandlerPlayServer>)
                if (reverseTimer.hasTimePassed(maxReverseTime.get().toLong())) {
                    stopReverse()
                }
            }
            if (packet is C02PacketUseEntity) {
                if (packet.action == C02PacketUseEntity.Action.ATTACK) {
                    if (needFreeze) attacked = packet.getEntityFromWorld(theWorld)
                    if (reversing) hasAttackInReversing = true
                }
            } else if (packet is C03PacketPlayer) {
                if (!needFreeze && reverse.get()) {
                    val vec = Vec3(packet.x, packet.y, packet.z)
                    KevinClient.combatManager.target?.let {
                        val loc = Vec3(it.posX, it.posY + it.eyeHeight, it.posZ)
                        val bp = getNearestPointBB(loc, mc.thePlayer.entityBoundingBox.expand(0.1, 0.1, 0.1))
                        val distance = loc.distanceTo(bp)
                        if (reversing) {
                            val lastBB = AxisAlignedBB(
                                lastPosition.xCoord - 0.4f,
                                lastPosition.yCoord - 0.1f,
                                lastPosition.zCoord - 0.4f,
                                lastPosition.xCoord + 0.4f,
                                lastPosition.yCoord + 1.9f,
                                lastPosition.zCoord + 0.4f
                            )
                            val d = getNearestPointBB(loc, lastBB).distanceTo(loc)
                            if (distance > d || distance > reverseMaxRange.get() || (it.hurtTime <= (1 + (mc.thePlayer.getPing() / 50.0).toInt()) && hasAttackInReversing)) {
                                stopReverse()
                            } else {
                                val rot = (if (it is EntityOtherPlayerMP) Rotation(it.otherPlayerMPYaw.toFloat(), it.otherPlayerMPPitch.toFloat()) else Rotation(it.rotationYaw, it.rotationPitch)).toDirection().multiply(4.0).add(loc)
                                val movingObjectPosition = lastBB.calculateIntercept(loc, rot) ?: return@let
                                val m2 = mc.thePlayer.entityBoundingBox.expand(0.11, 0.1, 0.11).calculateIntercept(loc, rot)
                                if (movingObjectPosition.hitVec != null) {
                                    val d2 = movingObjectPosition.hitVec.distanceTo(loc)
                                    if (d2 <= 3.0 && (m2?.hitVec == null || m2.hitVec.distanceTo(loc) > d2)) stopReverse()
                                }
                            }
                        } else if (distance <= reverseRange.get() &&
                            it.hurtTime <= reverseTargetMaxHurtTime.get() && mc.thePlayer.hurtTime <= reverseSelfMaxHurtTime.get() &&
                            loc.distanceTo(bp) >= distance && (!onlyKillAura.get() || killAura.state)) {
                            reversing = true
                            lastPosition = vec
                            hasAttackInReversing = false
                            reverseTimer.reset()
                        }
                    }
                }
            }
        }
    }

    @EventTarget fun onMotion(event: MotionEvent) {
        if (event.eventState == EventState.PRE) return
        if (needFreeze) {
            if (mode equal "Legacy") {
                if (timer.hasTimePassed(maxTime.get().toLong())) {
                    releasePackets()
                    return
                }
            } else {
                doSmoothRelease()
            }
            if (storageEntities.isNotEmpty()) {
                var release = false // for-each
                for (entity in storageEntities) {
                    val x = entity.serverPosX.toDouble() / 32.0
                    val y = entity.serverPosY.toDouble() / 32.0
                    val z = entity.serverPosZ.toDouble() / 32.0
                    val entityBB = AxisAlignedBB(x - 0.4F, y -0.1F, z - 0.4F, x + 0.4F, y + 1.9F, z + 0.4F)
                    var range = entityBB.getLookingTargetRange(mc.thePlayer!!)
                    if (range == Double.MAX_VALUE) {
                        val eyes = mc.thePlayer!!.getPositionEyes(1F)
                        range = getNearestPointBB(eyes, entityBB).distanceTo(eyes) + 0.075
                    }
                    if (range <= minDistance.get()) {
                        release = true
                        break
                    }
                    val entity1 = attacked
                    if (entity1 != entity) continue
                    if (timer.hasTimePassed(minTime.get().toLong())) {
                        if (range >= minAttackReleaseRange.get()) {
                            release = true
                            break
                        }
                    }
                }
                if (release) releasePackets()
            }
            if (espMode equal "Model") {
                for (entity in storageEntities) {
                    if (entity !is EntityOtherPlayerMP) return
                    RenderUtils.getFakePlayer(mdfId(entity.entityId))?.let {mp ->
                        mp.entityId = mdfId(entity.entityId)
                        mp.prevPosX = mp.posX
                        mp.prevPosY = mp.posY
                        mp.prevPosZ = mp.posZ
                        mp.posX = entity.serverPosX / 32.0
                        mp.posY = entity.serverPosY / 32.0
                        mp.posZ = entity.serverPosZ / 32.0
                        mp.lastTickPosX = mp.prevPosX
                        mp.lastTickPosY = mp.prevPosY
                        mp.lastTickPosZ = mp.prevPosZ
                        mp.rotationYaw = entity.rotationYaw
                        mp.rotationPitch = entity.rotationPitch
                        mp.rotationYawHead = entity.rotationYawHead
                        mp.prevRotationYaw = mp.rotationYaw
                        mp.prevRotationPitch = mp.rotationPitch
                        mp.prevRotationYawHead = mp.rotationYawHead
                        mp.swingProgress = entity.swingProgress
                        mp.swingProgressInt = entity.swingProgressInt
                        mp.hurtTime = entity.hurtTime
                        mp.hurtResistantTime = entity.hurtResistantTime
                        mp.isInvisible = false
                    }
                }
            }
        }
    }

    @EventTarget fun onWorld(event: WorldEvent) {
        attacked = null
        storageEntities.forEach {
            RenderUtils.removeFakePlayer(mdfId(it.entityId))
        }
        storageEntities.clear()
        if (event.worldClient == null) storagePackets.clear()
    }

    @EventTarget fun onRender3D(event: Render3DEvent) {
        if (reversing) {
            val renderManager = mc.renderManager

            val vec = lastPosition.addVector(-renderManager.renderPosX, -renderManager.renderPosY, -renderManager.renderPosZ)
            RenderUtils.drawAxisAlignedBB(AxisAlignedBB(vec.xCoord - 0.4, vec.yCoord + 0.2, vec.zCoord - 0.4, vec.xCoord + 0.4, vec.yCoord, vec.zCoord + 0.4), Color(37, 126, 255, 70))
        }

        if (espMode equal "None" || !needFreeze || espMode equal "Model") return

        val entitiesToRender = if (alwaysRenderESP.get()) mc.theWorld.loadedEntityList else storageEntities

        var outline = false
        var filled = false
        var other = false
        when (espMode.get()) {
            "NormalBox" -> {
                outline = true
                filled = true
            }
            "FullBox" -> {
                filled = true
            }
            "OtherOutlineBox" -> {
                other = true
                outline = true
            }
            "OtherFullBox" -> {
                other = true
                filled = true
            }
            else -> {
                outline = true
            }
        }

        // pre draw
        glPushMatrix()
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glEnable(GL_BLEND)
        glDisable(GL_TEXTURE_2D)
        glDisable(GL_DEPTH_TEST)

        glDepthMask(false)

        if (outline) {
            glLineWidth(1f)
            glEnable(GL_LINE_SMOOTH)
        }
        // drawing
        val renderManager = mc.renderManager
        for (entity in entitiesToRender) {
            val x = entity.serverPosX.toDouble() / 32.0 - renderManager.renderPosX
            val y = entity.serverPosY.toDouble() / 32.0 - renderManager.renderPosY
            val z = entity.serverPosZ.toDouble() / 32.0 - renderManager.renderPosZ
            if (other) {
                if (outline) {
                    RenderUtils.glColor(outlineRed, outlineGreen, outlineBlue, outlineAlpha)
                    RenderUtils.otherDrawOutlinedBoundingBox(entity.rotationYawHead, x, y, z, entity.width / 2.0 + 0.1, entity.height + 0.1)
                }
                if (filled) {
                    RenderUtils.glColor(espRed, espGreen, espBlue, espAlpha)
                    RenderUtils.otherDrawBoundingBox(entity.rotationYawHead, x, y, z, entity.width / 2.0 + 0.1, entity.height + 0.1)
                }
            } else {
                val bb = AxisAlignedBB(x - 0.4F, y, z - 0.4F, x + 0.4F, y + 1.9F, z + 0.4F)
                if (outline) {
                    RenderUtils.glColor(outlineRed, outlineGreen, outlineBlue, outlineAlpha)
                    RenderUtils.drawSelectionBoundingBox(bb)
                }
                if (filled) {
                    RenderUtils.glColor(espRed, espGreen, espBlue, espAlpha)
                    RenderUtils.drawFilledBox(bb)
                }
            }
        }

        // post draw
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        glDepthMask(true)
        if (outline) {
            glDisable(GL_LINE_SMOOTH)
        }
        glDisable(GL_BLEND)
        glEnable(GL_TEXTURE_2D)
        glEnable(GL_DEPTH_TEST)
        glPopMatrix()
    }
    private fun mdfId(id: Int) = id shl 4 or 0xFA00000

    private fun releasePackets() {
        attacked = null
        smoothPointer = System.nanoTime()
        val netHandler: INetHandlerPlayClient = mc.netHandler
        if (storagePackets.isEmpty()) return
        while (storagePackets.isNotEmpty()) {
            storagePackets.removeAt(0).let{
                val packet = it.packet
                try {
                    val packetEvent = PacketEvent(packet)
                    KevinClient.eventManager.callEvent(packetEvent)
                    if (!packetEvent.isCancelled) packet.processPacket(netHandler)
                } catch (_: ThreadQuickExitException) { }
            }
        }
        while (storageEntities.isNotEmpty()) {
            storageEntities.removeAt(0).let { entity ->
                if (!entity.isDead) {
                    val x = entity.serverPosX.toDouble() / 32.0
                    val y = entity.serverPosY.toDouble() / 32.0
                    val z = entity.serverPosZ.toDouble() / 32.0
                    if (setPosOnStop.get()) entity.setPosition(x, y, z)
                    else entity.setPositionAndRotation2(x, y, z, entity.rotationYaw, entity.rotationPitch, 3, true)
                }
            }
        }
        storageEntityMove.clear()
        needFreeze = false
    }

    private fun releasePacket(untilNS: Long) {
        val netHandler: INetHandlerPlayClient = mc.netHandler
        if (storagePackets.isEmpty()) return
        while (storagePackets.isNotEmpty()) {
            val it = storagePackets[0]
            val packet = it.packet
            if (it.time <= untilNS) {
                storagePackets.remove(it)
                try {
                    val packetEvent = PacketEvent(packet)
                    KevinClient.eventManager.callEvent(packetEvent)
                    if (!packetEvent.isCancelled) packet.processPacket(netHandler)
                } catch (_: ThreadQuickExitException) {}
            } else {
                break
            }
        }
        smoothPointer = untilNS
        while (storageEntityMove.isNotEmpty()) {
            val first = storageEntityMove.first
            if (first.time <= untilNS) {
                val loc = storageEntityMove.removeFirst()
                val entity = loc.entity
                if (entity is EntityOtherPlayerMP) entity.setPositionAndRotation2(loc.x, loc.y, loc.z, entity.otherPlayerMPYaw.toFloat(), entity.otherPlayerMPPitch.toFloat(), 3, true)
                else if (entity is EntityLivingBase) entity.setPositionAndRotation2(loc.x, loc.y, loc.z, entity.rotationYawHead, entity.rotationPitch, 3, true)
                else entity.setPositionAndUpdate(loc.x, loc.y, loc.z)
            }
            else break
        }
    }

    private fun releaseUntilBefore(ms: Int) = releasePacket(System.nanoTime() - ms * 1000000)

    private fun doSmoothRelease() {
        // what I wrote?
        val target = killAura.target
        var found = false
        var bestTimeStamp = max(smoothPointer, System.nanoTime() - maxTime.get() * 1000000)
        for (it in storageEntityMove) {
            if (target == it.entity) {
                found = true
                val width = it.entity.width / 2.0
                val height = it.entity.height
                val bb = AxisAlignedBB(it.x - width, it.y, it.z - width, it.x + width, it.y + height, it.z + width).expands(0.1)
                val range = mc.thePlayer.eyesLoc.distanceTo(bb)
                if (range < smoothRangeToKeep && range < smoothRangeToKeep ||
                    mc.thePlayer.getPositionEyes(3F).distanceTo(bb) < smoothRangeToKeep - 0.1) {
                    bestTimeStamp = max(bestTimeStamp, it.time)
                }
            }
        }
        // simply release them all
        // TODO: Multi targets support
        if (!found) releasePackets()
        else releasePacket(bestTimeStamp)
    }

    fun stopReverse() {
        if (storageSendPackets.isEmpty()) return
        while (storageSendPackets.isNotEmpty()) {
            storageSendPackets.removeAt(0).let {
                try {
                    val packetEvent = PacketEvent(it)
                    KevinClient.eventManager.callEvent(packetEvent)
                    if (!packetEvent.isCancelled) mc.netHandler.networkManager.sendPacketNoEvent(it)
                } catch (e: Exception) {
                    KevinClient.hud.addNotification(Notification("Something went wrong when sending packet reversing", "BackTrack"))
                }
                // why kotlin
                return@let
            }
        }
        if (storageSendPackets.isEmpty()) {
            reversing = false
            hasAttackInReversing = false
        }
    }

    private val calculatedMaxHurtTime : Int
        get() = maxHurtTime.get() + if (hurtTimeWithPing.get()) ceil(mc.thePlayer.getPing() / 50.0).toInt() else 0

    fun update() {}

    init {
        NetworkManager.backTrack = this
    }

    private data class ServerPacketStorage(val packet: Packet<INetHandlerPlayClient>) {
        val time = System.nanoTime()
    }

    private data class EntityPacketLoc(val entity: Entity, val x: Double, val y: Double, val z: Double) {
        val time = System.nanoTime()
    }
}