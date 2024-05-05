package tech.atani.client.feature.module.impl.movement;

import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;

// Hooked in EntityLivingBase.java
@ModuleData(name = "NoJumpDelay", description = "Removes jump delay", category = Category.PLAYER)
public class NoJumpDelay extends Module {

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}