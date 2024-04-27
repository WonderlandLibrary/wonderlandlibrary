package net.ccbluex.liquidbounce.features.command.commands

import com.google.common.base.Splitter
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventManager
import net.ccbluex.liquidbounce.features.command.Command
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.file.FileManager
import net.minecraft.util.EnumChatFormatting
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils

class SetNameCommand : Command("name") {


    override fun execute(args: Array<String>) {

        if (args.size > 1) {
            when {
                args[1].equals("load", true) -> {
                    LiquidBounce.fileManager.loadConfig(LiquidBounce.fileManager.setnameConfig)
                    return
                }
                args[1].equals("list", true) -> {
                    for (module in LiquidBounce.moduleManager.modules) {
                        if (module.arrayListName.equals(module::class.java.getAnnotation(ModuleInfo::class.java).name))
                            continue

                        chat("Module <" + module.arrayListName + "> is §7" + module::class.java.getAnnotation(ModuleInfo::class.java).name)
                    }
                    return
                }
                args[1].equals("cleaner", true) -> {
                    for (module in LiquidBounce.moduleManager.modules) {
                        if (args.size > 2) {

                            var newValue = getname(args, 2)

                            val oldmodule = LiquidBounce.moduleManager.getModule(newValue) ?: return


                            if (module.name.equals(oldmodule.name)) {
                                module.arrayListName = module::class.java.getAnnotation(ModuleInfo::class.java).name

                                LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.setnameConfig)
                                chat("reset Module <$newValue>")
                                return
                            }
                        } else {
                            module.arrayListName = module::class.java.getAnnotation(ModuleInfo::class.java).name
                            LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.setnameConfig)
                        }
                    }
                    chat("reset Module name ")
                    return
                }

                else -> {
                    if (args.size > 2) {

                        var newValue = getname(args, 2)
                        var oldVaule = getname(args, 1)

                        // Get module by name
                        val module = LiquidBounce.moduleManager.getModule(oldVaule)

                        if (module == null) {
                            chat("Module §a§l" + args[1] + "§3 not found.")
                            return
                        }

                        module.setNameCommad(newValue)
                        LiquidBounce.fileManager.saveConfig(LiquidBounce.fileManager.setnameConfig)
                        chat("Module §a§l" + args[1] + "§l§a to " + newValue)
                        return
                    }
                }
            }
        }
        chatSyntax(arrayOf("<module> <name>", "<cleaner/load/list> "))
    }

    override fun tabComplete(args: Array<String>): List<String> {
        if (args.isEmpty()) return emptyList()

        val moduleName = args[0]

        return when (args.size) {
            1 -> LiquidBounce.moduleManager.modules
                .map { it.name }
                .filter { it.startsWith(moduleName, true) }
                .toList()
            else -> emptyList()
        }
    }

    fun getname(args: Array<String>, size: Int): String {
       return when {
            args[size].contains("#") -> args[size].replace("#", " ")
            args[size].contains("#l") -> args[size].replace("#l", EnumChatFormatting.BOLD.toString())
            args[size].contains("#o") -> args[size].replace("#o", EnumChatFormatting.ITALIC.toString())
            args[size].contains("#m") -> args[size].replace("#m", EnumChatFormatting.STRIKETHROUGH.toString())
            args[size].contains("#n") -> args[size].replace("#n", EnumChatFormatting.UNDERLINE.toString())
            else -> args[size]
        }
    }
}
