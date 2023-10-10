package today.flux.addon.api.module;

import lombok.Getter;
import today.flux.Flux;
import today.flux.addon.FluxAPI;
import today.flux.addon.api.FluxAddon;
import today.flux.addon.api.exception.NoSuchModuleException;
import today.flux.gui.clickgui.skeet.SkeetClickGUI;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.utility.ChatUtils;

import java.util.ArrayList;
import java.util.List;

public class AddonModuleManager {
    @Getter
    public List<AddonModule> addonModules = new ArrayList<>();
    private List<AddonModule> allModules;

    public AddonModuleManager() {
        ModuleManager.apiModules.clear();

        for (FluxAddon fluxAddon : FluxAPI.FLUX_API.getAddonManager().getEnabledFluxAddonList()) {
            for (AddonModule module : fluxAddon.getModules()) {
                try {
                    ModuleManager.apiModules.add(module.module);
                    addonModules.add(module);
                    ChatUtils.sendOutputMessage(String.format("[Flux API] \2472[Module] \247a[%s] \2477Loaded!", module.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                    ChatUtils.sendErrorToPlayer(String.format("[Flux API] \2472[Module] \247a[%s] \2477Error: %s", module.getName(), e.getMessage()));
                }
            }
        }

        Flux.INSTANCE.setSkeetClickGUI(new SkeetClickGUI());
        Flux.INSTANCE.getClickGUI().setup();
    }
    
    public AddonModule getModule(String moduleName) throws NoSuchModuleException {
        Module module = Flux.INSTANCE.getModuleManager().getModuleByName(moduleName);
        if (module != null)
            return new AddonModule(module);
        else
            throw new NoSuchModuleException("Can't found module called " + moduleName);
    }

    
    public List<AddonModule> getAllModules() {
        if (allModules == null) {
            List<AddonModule> modules = new ArrayList<>();
            for (Module module : ModuleManager.getModList()) {
                modules.add(new AddonModule(module));
            }
            allModules = modules;
        }
        return allModules;
    }
}
