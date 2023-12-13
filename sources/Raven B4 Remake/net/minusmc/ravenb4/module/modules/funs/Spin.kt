package net.minusmc.ravenb4.module.modules.funs

import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.SliderSetting

class Spin: Module("Spin", ModuleCategory.funs){
    private val yawSetting = SliderSetting("Rotation yaw", 360.0, 30.0, 360.0, 1.0);
    private val speedSetting = SliderSetting("Speed", 25.0, 1.0, 80.0, 1.0);

    init {
        this.addSetting(yawSetting)
        this.addSetting(speedSetting)
    }

}