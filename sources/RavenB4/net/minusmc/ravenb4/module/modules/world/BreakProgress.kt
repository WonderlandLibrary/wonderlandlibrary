package net.minusmc.ravenb4.module.modules.world;

import net.minecraft.util.BlockPos
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.TickSetting


class BreakProgress: Module("BreakProgress", ModuleCategory.world){

    private val blockPos: BlockPos? = null
    private val showManual = TickSetting("Show Manual", true);
    private val showBedAura = TickSetting("Show BedAura", true);

    init {
        this.settings.add(showManual);
        this.settings.add(showBedAura);
    }

    @SubscribeEvent
    fun onRenderWorldLast(renderWorldLastEvent: RenderWorldLastEvent) {

    }

}