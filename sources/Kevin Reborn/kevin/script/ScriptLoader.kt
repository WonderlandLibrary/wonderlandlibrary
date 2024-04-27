package kevin.script

import kevin.main.KevinClient
import kevin.plugin.PluginManager
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import java.io.File
import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object ScriptLoader {
    fun load() {
        val dir = KevinClient.fileManager.scripts
        if (!dir.exists()) return
        val files = dir.listFiles() ?: return
        if (files.isEmpty() || files.none { it.name.endsWith(".py", true) }) {
            Minecraft.logger.info("[ScriptManager] There is no script to load")
            return
        }
        // unpack script if isn't exist
        val file = File(KevinClient.fileManager.libraries, "PyScript.jar")
        ScriptLoader.javaClass.getResourceAsStream("/PyScript.jar")!!.let {
            Files.copy(it, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
        if (!file.exists()) throw RuntimeException("Failed to copy PyScript lib!")
        PluginManager.load(file)
        Minecraft.logger.info("[ScriptManager] Script loader loaded")
    }
}