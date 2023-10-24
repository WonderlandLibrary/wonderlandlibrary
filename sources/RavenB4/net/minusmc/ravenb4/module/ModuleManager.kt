package net.minusmc.ravenb4.module

import net.minecraft.client.gui.FontRenderer
import net.minusmc.ravenb4.module.modules.combat.*
import net.minusmc.ravenb4.module.modules.funs.*
import net.minusmc.ravenb4.module.modules.others.*
import net.minusmc.ravenb4.module.modules.world.*

object ModuleManager {
    val modules = mutableListOf<Module>()

    init {
        //Combat
        modules.add(AntiKnockback());

        //World
        modules.add(BreakProgress());

        //Other
        modules.add(FakeChat());

        //Fun
        modules.add(Spin());

        modules.sortedBy { it.name }
    }

    fun addModule(module: Module) = modules.add(module)

    fun getModulesInCategory(category: ModuleCategory) = modules.filter {it.category == category}

    fun getLongestActiveModule(fontRenderer: FontRenderer) = modules.filter { it.enabled }.maxByOrNull { fontRenderer.getStringWidth(it.name) }

    fun getBoxHeight(fontRenderer: FontRenderer, margin: Int) = modules.filter { it.enabled }.sumOf { fontRenderer.FONT_HEIGHT + margin }
}