package me.AquaVit.liquidSense.utils;

import net.ccbluex.liquidbounce.features.module.Module
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.util.AxisAlignedBB

class MoveHackUtil {

    val mc = Minecraft.getMinecraft()

    fun onHypixelFly(ListValue: String, fallHeight: Float) {

        var thePlayer = mc.thePlayer ?: return

        when (ListValue.toLowerCase()) {
            "boosthypixel" -> {

                if (!thePlayer.onGround)
                    return

                var fallDistance = fallHeight + 0.0125//add 0.0125 to ensure we get the fall dmg


                while (fallDistance > 0) {
                    mc.netHandler.addToSendQueue(
                            C03PacketPlayer.C04PacketPlayerPosition(
                                    thePlayer.posX,
                                    thePlayer.posY + 0.0624986421,
                                    thePlayer.posZ,
                                    false
                            )
                    )
                    mc.netHandler.addToSendQueue(
                            C03PacketPlayer.C04PacketPlayerPosition(
                                    thePlayer.posX,
                                    thePlayer.posY + 0.0625,
                                    thePlayer.posZ,
                                    false
                            )
                    )
                    mc.netHandler.addToSendQueue(
                            C03PacketPlayer.C04PacketPlayerPosition(
                                    thePlayer.posX,
                                    thePlayer.posY + 0.0624986421,
                                    thePlayer.posZ,
                                    false
                            )
                    )
                    mc.netHandler.addToSendQueue(
                            C03PacketPlayer.C04PacketPlayerPosition(
                                    thePlayer.posX,
                                    thePlayer.posY + 0.0000013579,
                                    thePlayer.posZ,
                                    false
                            )
                    )
                    fallDistance -= 0.0624986421f
                }

                mc.netHandler.addToSendQueue(
                        C03PacketPlayer.C04PacketPlayerPosition(
                                thePlayer.posX,
                                thePlayer.posY,
                                thePlayer.posZ,
                                true
                        )
                )
            }
        }
    }

    fun checkVoid(entity: EntityLivingBase): Boolean {
        for (x in -2..0) {
            for (z in -2..0) {
                if (isVoid(x, z, entity)) {
                    return true
                }
            }
        }
        return false
    }

    fun isVoid(X: Int, Z: Int, entity: EntityLivingBase): Boolean {
        if (mc.thePlayer!!.posY < 0.0) {
            return true
        }
        var off = 0
        while (off < entity.posY.toInt() + 2) {
            val bb: AxisAlignedBB = entity.entityBoundingBox.offset(X.toDouble(), (-off).toDouble(), Z.toDouble())
            if (mc.theWorld!!.getCollidingBoundingBoxes(entity as Entity, bb).isEmpty()) {
                off += 2
                continue
            }
            return false
            off += 2
        }
        return true
    }
}