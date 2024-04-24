package me.offeex.bloomware.api.extension.plugin

import me.offeex.bloomware.api.manager.managers.ModuleManager.addModule
import me.offeex.bloomware.api.helper.FilePath
import me.offeex.bloomware.client.module.Module
import net.fabricmc.loader.impl.launch.FabricLauncherBase

object PluginClassLoader : ClassLoader(FabricLauncherBase.getLauncher().targetClassLoader), Runnable {
    override fun run() {
        FilePath.PLUGINS.file.walkTopDown().filter { it.name.endsWith(".class") }.forEach {
            addModule(defineClass(it.readBytes()).getDeclaredConstructor().newInstance() as Module)
        }
    }

    private fun defineClass(array: ByteArray): Class<*> = defineClass(null, array, 0, array.size)
}