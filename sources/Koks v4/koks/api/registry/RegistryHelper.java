package koks.api.registry;

import koks.api.registry.command.CommandRegistry;
import koks.api.registry.credits.CreditsRegistry;
import koks.api.registry.file.FileRegistry;
import koks.api.registry.module.ModuleRegistry;
import koks.api.registry.spoof.SpoofRegistry;
import koks.api.utils.Logger;

import java.util.ArrayList;

/**
 * @author kroko
 * @created on 21.01.2021 : 11:37
 */
public class RegistryHelper {

    public static final ArrayList<Registry> REGISTRY = new ArrayList<>();

    public RegistryHelper() {
        addRegistry(new ModuleRegistry());
        addRegistry(new CommandRegistry());
        addRegistry(new FileRegistry());
        addRegistry(new SpoofRegistry());
        addRegistry(new CreditsRegistry());
    }

    public void initialize() {
        for(Registry manager : REGISTRY) {
            manager.initialize();
        }
    }

    public void addRegistry(Registry registry) {
        REGISTRY.add(registry);
    }
}
