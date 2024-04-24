package me.offeex.bloomware.api.helper

import me.offeex.bloomware.api.manager.managers.FileManager
import java.io.File

enum class FilePath(path: String, val isDir: Boolean) {

    PLUGINS("plugin/", true),
    PEOPLE("people.json", false),
    SPAMMER("spammer.txt", false),

    PREFIX("prefix.json", false),
    GUI("gui.json", false),
    MODULES("config.json", false),
    TOKEN("token.json", false);


    val file: File

    init {
        file = File(FileManager.mainPath.toString() + "/" + path)
    }
}