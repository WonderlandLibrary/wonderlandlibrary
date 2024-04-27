/*
 * GlassClient PVP Client
 * A free open-source mixin-based PVP client based on liquidbounce with all cheats removed.
 * https://github.com/GlassClient/GlassClient
 */
package net.ccbluex.liquidbounce.features.command.commands

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.command.Command

class PrefixCommand : Command("prefix", emptyArray()) {
    /**
     * Execute commands with provided [args]
     */
    override fun execute(args: Array<String>) {
        if (args.size <= 1) {
            chatSyntax("prefix <character>")
            return
        }

        val prefix = args[1]

        if (prefix.length > 1) {
            alert("§cPrefix can only be one character long!")
            return
        }

        LiquidBounce.commandManager.prefix = prefix.single()
        LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.specialConfig)

        alert("Successfully changed command prefix to '§8$prefix§3'")
    }
}