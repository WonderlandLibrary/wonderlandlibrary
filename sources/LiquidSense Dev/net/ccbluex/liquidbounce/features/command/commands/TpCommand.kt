package net.ccbluex.liquidbounce.features.command.commands

import me.AquaVit.liquidSense.modules.world.Tp
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.command.Command
import java.lang.Float

class TpCommand  : Command("tp", "t") {

    override fun execute(args: Array<String>) {
        if (args.size >= 3) {
            try {
                Tp.x = Float.valueOf(args[1])
                Tp.y = Float.valueOf(args[2])
                Tp.z = Float.valueOf(args[3])
                LiquidBounce.moduleManager.getModule(Tp::class.java)!!.state = true
                return
            } catch(e : ArrayIndexOutOfBoundsException) {
                chat("[Teleport] Please type X Y Z")
            }
        }
        chatSyntax("Teleport <x> <y> <z>")
    }
}