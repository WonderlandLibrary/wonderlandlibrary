/*
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kevin.command.commands

import kevin.command.ICommand
import kevin.main.KevinClient
import kevin.module.modules.render.ClickGui
import kevin.utils.ChatUtils
import net.minecraft.client.Minecraft
import java.util.ArrayList

class ClearMainConfigCommand : ICommand {
    override fun run(args: Array<out String>?) {
        KevinClient.moduleManager.getModules().forEach { KevinClient.eventManager.unregisterListener(it) }
        KevinClient.eventManager.unregisterListener(KevinClient.moduleManager)
        KevinClient.moduleManager.getModules().clear()
        KevinClient.moduleManager.load()
//        ScriptManager.reAdd()
        KevinClient.clickGUI = ClickGui.ClickGUI()
        KevinClient.newClickGui = ClickGui.NewClickGui()
        Minecraft.logger.info("[ScriptManager] Reloaded ClickGui.")
        reAddHook.forEach {
            it.reAdd()
        }
        KevinClient.fileManager.saveConfig(KevinClient.fileManager.modulesConfig)
        ChatUtils.messageWithStart("§aCleared main config.")
    }

    interface IReAddHook {
        fun reAdd()
    }

    companion object {
        private val reAddHook: MutableList<IReAddHook> = ArrayList()

        fun addHook(hook: IReAddHook) {
            if (reAddHook.contains(hook)) return
            reAddHook.add(hook)
        }
    }
}