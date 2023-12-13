/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.event.impl.other.UpdateEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "NoWeather", description = "Removes weather", category = Category.RENDER)
public final class NoWeather extends Module {

    @Override
    public void onUpdate(final UpdateEvent event) {
        mc.theWorld.setThunderStrength(0);
        mc.theWorld.setRainStrength(0);
    }
}
