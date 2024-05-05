package rip.helium.cheat.impl.misc

import rip.helium.cheat.Cheat
import rip.helium.cheat.CheatCategory
import me.hippo.systems.lwjeb.annotation.Collect
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityEnderPearl
import org.lwjgl.input.Keyboard
import rip.helium.ChatUtil
import rip.helium.event.minecraft.PlayerUpdateEvent
import rip.helium.utils.property.impl.BooleanProperty
import kotlin.math.roundToInt

/*/
Created by Kansio on 1/16/2021
 */

class PearlFinder : Cheat("Pearl Finder", "Finds pearls and teleports to them", CheatCategory.MISC) {

    val teleport = BooleanProperty("Teleport", "Teleports to the enderpearl", null, true)
    val teleportOnKey = BooleanProperty("Teleports on Tab", "Teleports when pressing a keybind", null, false)

    override fun onEnable() {
        if (teleportOnKey.value) {
            ChatUtil.chat("Press the ENTER key to start teleporting to the enderpearl.")
        }
    }

    @Collect
    fun onUpdate(event: PlayerUpdateEvent) {
        ChatUtil.chat("called update")
        for (obj in Cheat.mc.theWorld.getLoadedEntityList()) {

            if (obj !is EntityEnderPearl) {
                return;
            }
            val enderPearl = obj
            ChatUtil.chat("pearl found")
            var x = enderPearl.posX.roundToInt()
            var y = enderPearl.posY.roundToInt()
            var z = enderPearl.posZ.roundToInt()

            if (shouldTeleport()) {
                mc.thePlayer.setPosition(x.toDouble(), y.toDouble(), z.toDouble())
                ChatUtil.chat("Teleported...")
            }
        }
    }

    fun shouldTeleport(): Boolean {
        if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
            return true
        }
        return false
    }

    fun formatCoords(x: Int, y: Int, z: Int): String {
        return "$x, $y, $z"
    }

    init {
        registerProperties(teleport, teleportOnKey)
    }
}