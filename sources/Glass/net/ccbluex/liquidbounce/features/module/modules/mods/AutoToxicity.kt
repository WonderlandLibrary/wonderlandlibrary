/*
 * GlassClient PVP Client
 * A free open-source mixin-based PVP client based on liquidbounce with all cheats removed.
 * https://github.com/GlassClient/GlassClient
 */
package net.ccbluex.liquidbounce.features.module.modules.mods

import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EntityKilledEvent
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.utils.FileUtils
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.player.EntityPlayer
import java.io.File

@ModuleInfo(name = "AutoToxicity", category = ModuleCategory.MODS)
object AutoToxicity : Module() {


    private val Testit = BoolValue("Test", false)

    private val insultFile = File(LiquidBounce.fileManager.dir, "insult.json")
    var insultWords = mutableListOf<String>()

    init {
        loadFile()
    }
    fun loadFile() {
        fun convertJson() {
            insultWords.clear()
            insultWords.addAll(insultFile.readLines(Charsets.UTF_8).filter { it.isNotBlank() })

            val json = JsonArray()
            insultWords.map { JsonPrimitive(it) }.forEach(json::add)
            insultFile.writeText(FileManager.PRETTY_GSON.toJson(json), Charsets.UTF_8)
        }

        try {
            // check file exists
            if (!insultFile.exists()) {
                FileUtils.unpackFile(insultFile, "assets/minecraft/fdpclient/misc/insult.json")
            }
            // read it
            val json = JsonParser().parse(insultFile.readText(Charsets.UTF_8))
            if (json.isJsonArray) {
                insultWords.clear()
                json.asJsonArray.forEach {
                    insultWords.add(it.asString)
                }
            } else {
                // not jsonArray convert it to jsonArray
                convertJson()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            convertJson()
        }
    }

    fun getRandomOne(): String {
        return insultWords[RandomUtils.nextInt(0, insultWords.size - 1)]
    }

    @EventTarget
    fun onKilled(event: EntityKilledEvent) {
        val target = event.targetEntity

        if (target !is EntityPlayer) {
            return
        }

        sendInsultWords(getRandomOne(), target.name)
    }

    private fun sendInsultWords(msg: String, name: String) {
        var message = msg.replace("%name%", name)
        mc.thePlayer.sendChatMessage(message)
    }

}