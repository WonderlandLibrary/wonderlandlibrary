package dev.rise.module.impl.render;

import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.NumberSetting;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Giants", description = "Changes the size of entities", category = Category.RENDER)
public class Giants extends Module {

    private final NumberSetting size = new NumberSetting("size", this, 1, 0.1, 10, 0.2);
}