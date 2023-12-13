package today.flux.addon;

import lombok.Getter;
import today.flux.addon.api.command.AddonCommandManager;
import today.flux.addon.api.module.AddonModuleManager;
import today.flux.addon.api.value.AddonValueManager;
import today.flux.config.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class FluxAPI {
    @Getter
    private final AddonManager addonManager;
    @Getter
    private final AddonModuleManager moduleManager;
    @Getter
    private final AddonValueManager valueManager;
    @Getter
    private final AddonEventManager eventManager;
    @Getter
    private final AddonCommandManager commandManager;

    public static FluxAPI FLUX_API;

    public FluxAPI() {
        FLUX_API = this;

        this.loadAddons();

        this.eventManager = new AddonEventManager();
        this.addonManager = new AddonManager();
        this.valueManager = new AddonValueManager();
        this.addonManager.loadAllAPI();

        this.moduleManager = new AddonModuleManager();
        this.commandManager = new AddonCommandManager();
    }

    public void saveAddons() {
        try {
            final FileWriter fileWriter = new FileWriter(Config.ROOT_DIR + "/enabledAddons.txt");
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (String enabledAddon : AddonManager.getEnabledAddonsName()) {
                bufferedWriter.write(enabledAddon + "\n");
            }
            bufferedWriter.close();
        } catch (Exception ex) {
        }
    }

    public boolean loadAddons() {
        try {
            AddonManager.getEnabledAddonsName().clear();
            String line = null;
            final FileReader fileReader = new FileReader(Config.ROOT_DIR + "/enabledAddons.txt");
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                AddonManager.getEnabledAddonsName().add(line);
            }
            bufferedReader.close();
        } catch (Exception ex) {
            return false;
        }

        return true;
    }
}
