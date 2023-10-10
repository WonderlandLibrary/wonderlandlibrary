package today.flux.module.implement.Command;

import today.flux.Flux;
import today.flux.addon.AddonManager;
import today.flux.addon.api.FluxAddon;
import today.flux.addon.FluxAPI;
import today.flux.addon.api.command.AddonCommand;
import today.flux.addon.api.module.AddonModule;
import today.flux.module.Command;
import today.flux.utility.ChatUtils;

import java.util.List;

@Command.Info(name = "addon", syntax = { "list", "reload", "enable <id>", "disable <id>" }, help = "Manage Addons")
public class AddonCmd extends Command {

    @Override
    public void execute(String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                List<FluxAddon> fluxAddonList = Flux.INSTANCE.api.getAddonManager().getFluxAddonList();
                ChatUtils.sendOutputMessage(String.format("[Flux API] \247a%d Addon(s) has been loaded.", fluxAddonList.size()));
                for (FluxAddon fluxAddon : fluxAddonList) {
                    ChatUtils.sendOutputMessage(String.format("[Flux API] \2472[%s] \2477ID: %d, Author: %s, Enabled: %s", fluxAddon.getAPIName(), fluxAddonList.indexOf(fluxAddon), fluxAddon.getAuthor(), Flux.INSTANCE.api.getAddonManager().getEnabledFluxAddonList().contains(fluxAddon)));
                    for (AddonModule module : fluxAddon.getModules())
                        ChatUtils.sendOutputMessage(String.format("[Flux API] \2472[%s] \2477Registered Module: %s", fluxAddon.getAPIName(), module.getName()));
                    for (AddonCommand command : fluxAddon.getCommands())
                        ChatUtils.sendOutputMessage(String.format("[Flux API] \2472[%s] \2477Registered Command: %s", fluxAddon.getAPIName(), command.getName()));
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                ChatUtils.sendOutputMessage("[Flux API] \2477Reloading Flux API...");
                AddonManager.reload();
            } else {
                syntaxError();
            }
        } else if (args.length == 2) {
            List<FluxAddon> fluxAddonList = Flux.INSTANCE.api.getAddonManager().getFluxAddonList();
            try {
                int id = Integer.parseInt(args[1]);
                FluxAddon targetAddon = fluxAddonList.get(id);
                if (targetAddon != null) {
                    if (args[0].equalsIgnoreCase("enable")) {
                        if (Flux.INSTANCE.api.getAddonManager().getEnabledFluxAddonList().contains(targetAddon)) {
                            ChatUtils.sendErrorToPlayer(String.format("[Flux API] \2477%s has already enabled!", targetAddon.getAPIName()));
                        } else {
                            AddonManager.getEnabledAddonsName().add(targetAddon.getAPIName());
                            FluxAPI.FLUX_API.saveAddons();
                            Flux.INSTANCE.api = new FluxAPI();
                            ChatUtils.sendOutputMessage(String.format("[Flux API] \2477%s enabled successfully!", targetAddon.getAPIName()));
                        }
                    } else if (args[0].equalsIgnoreCase("disable")) {
                        if (!Flux.INSTANCE.api.getAddonManager().getEnabledFluxAddonList().contains(targetAddon)) {
                            ChatUtils.sendErrorToPlayer(String.format("[Flux API] \2477%s has already disabled!", targetAddon.getAPIName()));
                        } else {
                            AddonManager.getEnabledAddonsName().remove(targetAddon.getAPIName());
                            FluxAPI.FLUX_API.saveAddons();
                            Flux.INSTANCE.api = new FluxAPI();
                            ChatUtils.sendOutputMessage(String.format("[Flux API] \2477%s disabled successfully!", targetAddon.getAPIName()));
                        }
                    } else {
                        syntaxError();
                    }
                } else {
                    syntaxError();
                }
            } catch (Exception e) {
                syntaxError();
                e.printStackTrace();
            }

        } else {
            syntaxError();
        }
    }
}
