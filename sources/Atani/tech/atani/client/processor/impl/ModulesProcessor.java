package tech.atani.client.processor.impl;

import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.listener.event.minecraft.game.RunTickEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.processor.Processor;
import tech.atani.client.processor.data.ProcessorInfo;

@ProcessorInfo(name = "ModulesProcessor")
public class ModulesProcessor extends Processor {

    @Listen
    public void onTick(RunTickEvent runTickEvent) {
        for(Module module : ModuleStorage.getInstance().getList()) {
            if(module.getCategory() == Category.SERVER) {
                if(!module.correctServer()) {
                    if(module.isEnabled())
                        module.setEnabled(false);
                }
            }
         }
    }

}
