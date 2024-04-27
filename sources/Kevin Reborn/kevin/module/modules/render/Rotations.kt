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
package kevin.module.modules.render

import kevin.event.*
import kevin.main.KevinClient
import kevin.module.BooleanValue
import kevin.module.FloatValue
import kevin.module.Module
import kevin.module.ModuleCategory
import kevin.module.modules.combat.KillAura
import kevin.utils.RandomUtils
import kevin.utils.RotationUtils
import net.minecraft.network.play.client.C03PacketPlayer

object Rotations : Module("Rotations", description = "Allows you to see server-sided head and body rotations.", category = ModuleCategory.RENDER) {
    private val bodyValue = BooleanValue("Body", true)
    private val smoothBackValue by BooleanValue("SmoothBackRotation", true)
    private val smoothBackMinYawSpeed: Float by object : FloatValue("SmoothBackMinYawSpeed", 30F, 1F..180F) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            if (newValue > smoothBackMaxYawSpeed) set(smoothBackMaxYawSpeed)
        }
    }
    private val smoothBackMaxYawSpeed: Float by object : FloatValue("SmoothBackMaxYawSpeed", 80F, 1F..180F) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            if (newValue < smoothBackMinYawSpeed) set(smoothBackMinYawSpeed)
        }
    }
    private val smoothBackMinPitchSpeed: Float by object : FloatValue("SmoothBackMinPitchSpeed", 10F, 1F..180F) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            if (newValue > smoothBackMaxPitchSpeed) set(smoothBackMaxPitchSpeed)
        }
    }
    private val smoothBackMaxPitchSpeed: Float by object : FloatValue("SmoothBackMaxPitchSpeed", 70F, 1F..180F) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            if (newValue < smoothBackMinYawSpeed) set(smoothBackMinPitchSpeed)
        }
    }

    private var playerYaw: Float? = null

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        if (RotationUtils.serverRotation != null && !bodyValue.get())
            mc.thePlayer?.rotationYawHead = RotationUtils.serverRotation.yaw
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val thePlayer = mc.thePlayer

        if (!bodyValue.get()/* || !shouldRotate()*/ || thePlayer == null)
            return

        val packet = event.packet

        if ((packet) is C03PacketPlayer.C06PacketPlayerPosLook || (packet) is C03PacketPlayer.C05PacketPlayerLook) {
            val packetPlayer = packet as C03PacketPlayer

            playerYaw = packetPlayer.yaw

            thePlayer.renderYawOffset = packetPlayer.yaw
            thePlayer.rotationYawHead = packetPlayer.yaw
        } else {
            if (playerYaw != null)
                thePlayer.renderYawOffset = this.playerYaw!!

            thePlayer.rotationYawHead = thePlayer.renderYawOffset
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (event.eventState == EventState.PRE) return
        mc.thePlayer.prevRotationYawHead = mc.thePlayer.rotationYawHead
    }

    private fun getState(module: String) = KevinClient.moduleManager.getModuleByName(module)!!.state

    private fun shouldRotate(): Boolean {
        val killAura = KevinClient.moduleManager.getModule(KillAura::class.java)
        return (getState("KillAura") && (killAura.target != null || killAura.sTarget != null))
                || getState("Scaffold") || getState("Breaker") || getState("Nuker")/**getState(Tower::class.java) ||
                getState(BowAimbot::class.java) ||
                 || getState(CivBreak::class.java)  ||
                getState(ChestAura::class.java)**/
    }

    init {
        state = true
    }

    @JvmStatic
    fun doSb() = smoothBackValue

    @JvmStatic
    fun sbYawSpeed() = if (smoothBackValue) RandomUtils.nextFloat(smoothBackMinYawSpeed, smoothBackMaxYawSpeed) else 180F

    @JvmStatic
    fun sbPitchSpeed() = if (smoothBackValue) RandomUtils.nextFloat(smoothBackMinPitchSpeed, smoothBackMaxPitchSpeed) else 180F
}