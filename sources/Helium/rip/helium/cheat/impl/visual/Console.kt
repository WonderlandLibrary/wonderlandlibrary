package rip.helium.cheat.impl.visual

import me.hippo.systems.lwjeb.annotation.Collect
import net.minecraft.client.gui.Gui
import org.lwjgl.input.Keyboard
import rip.helium.ChatUtil
import rip.helium.Helium
import rip.helium.cheat.Cheat
import rip.helium.cheat.CheatCategory
import rip.helium.event.minecraft.KeyPressEvent
import rip.helium.event.minecraft.PlayerMoveEvent
import rip.helium.event.minecraft.PlayerUpdateEvent
import rip.helium.event.minecraft.RenderOverlayEvent
import rip.helium.utils.font.Fonts
import java.awt.Color
import java.lang.Exception

/*/
Created by Kansio on 1/16/2021
 */

/*/
mmmm kotlin :D
 */

class Console : Cheat("Console", "A cool console to send chat commands", Keyboard.KEY_INSERT, CheatCategory.VISUAL) {

    companion object {
        @JvmField
        var output = ArrayList<String>()
    }

    var y = 0

    var start = 150;
    var top = 120;


    var command = ""

    override fun onEnable() {
    }

    override fun onDisable() {
        mc.setIngameFocus()
    }

    fun renderGUI() {
        Gui.drawRect(start, top, start + 145, top + 190, Color(23, 23, 23).rgb)
        Gui.drawRect(start, top, start + 145, top + 2, Color(19, 244, 25).rgb)

        Gui.drawRect(start + 4, top + 168, start + 140, top + 178, Color(74, 74, 74).rgb)

        Fonts.verdana3.drawString("§7$command", top.toDouble() + 35, top.toDouble() + 170, Color(255, 255, 255).rgb)
    }

    @Collect
    fun onPlayerMove(event: PlayerMoveEvent) {
        mc.thePlayer.setMoveSpeedAris(event, 0.0) //prevent player from moving when gui is open.
    }

    @Collect
    fun onPlayerUpdate(event: PlayerUpdateEvent) {
        mc.thePlayer.inventory.closeInventory(mc.thePlayer)
    }

    @Collect
    fun onRenderOverlay(event: RenderOverlayEvent) {
        if (output.size > 8) {
            output.clear()
        }

        if (mc.inGameHasFocus) {
            mc.setIngameNotInFocus();
        }

        renderGUI()

        for (string in output) {
            Fonts.verdana3.drawString(string, top.toDouble() + 33, top.toDouble() + 140 + y, Color(255, 255, 255).rgb)
            y -= mc.fontRendererObj.FONT_HEIGHT + 8
        }

        y = 0

    }

    @Collect
    fun onKeyPress(event: KeyPressEvent) {
        when (event.keyCode) {
            Keyboard.KEY_ESCAPE -> {
                this.setState(false,false)
            }
            Keyboard.KEY_RETURN -> {
                executeCommand(command)
                command = ""
            }
            Keyboard.KEY_BACK -> {
                command = "";
                ChatUtil.chat("done")
            }
            Keyboard.KEY_SPACE -> command += " "
            Keyboard.KEY_SLASH -> command += "-"
            Keyboard.KEY_MINUS -> command += "-" //us keyboard

        }

        var keyCode: String = Keyboard.getKeyName(event.keyCode)
        if (keyCode.length == 1) {
            command += keyCode.toLowerCase()
        }
    }

    fun executeCommand(command: String) {
        for (comm in Helium.instance!!.cmds!!.cmds) {
            val args: Array<String> = command.split(" ".toRegex()).toTypedArray()
            if (args[0] == comm.name) {
                try {
                    comm.run(args)
                } catch (e: Exception) {
                    ChatUtil.chat("Unknown command.")
                }
            }
        }
    }

    fun removeLast(s1: String, n: Int): String {
        var s = s1
        val strLength = s!!.length
        if (n > strLength) {
            println("bruh momento")
        } else if (null != s && !s.isEmpty()) {
            s = s.substring(0, s.length - n)
        }
        return s
    }
}