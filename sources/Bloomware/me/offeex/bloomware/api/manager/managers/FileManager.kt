package me.offeex.bloomware.api.manager.managers

import me.offeex.bloomware.api.manager.Manager
import me.offeex.bloomware.api.helper.FilePath
import net.fabricmc.loader.api.FabricLoader
import java.io.File

object FileManager : Manager(), Runnable {
    val mainPath = File(FabricLoader.getInstance().configDir.toFile(), "bloomware")

    override fun run() {
        FilePath.values().forEach {
            if (it.isDir) it.file.mkdirs()
            else it.file.createNewFile()
        }
    }

    fun File.loadFileContent(): List<String>? = if (this.exists()) this.readLines() else null
}