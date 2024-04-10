package info.sigmaclient.sigma.commands.impl;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.ConfigManager;
import info.sigmaclient.sigma.commands.Command;
import info.sigmaclient.sigma.utils.ChatUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class ConfigCommand extends Command {
    private static boolean isFileNameValid(String name) {
        if (name == null || name.length() > 255) {
            return false;
        } else {
            return name.matches("^[a-zA-Z0-9](?:[a-zA-Z0-9 ._-]*[a-zA-Z0-9])?");
        }
    }

    @Override
    public void onChat(String[] args, String joinArgs) {
        if (args.length == 0 || args.length > 2) {
            sendUsages();
            return;
        }
        switch (args[0]) {
            case "save":
                if(args.length == 1){
                    sendUsages();
                    return;
                }
                String fileName = args[1];
                if(!isFileNameValid(fileName)){
                    ChatUtils.sendMessageWithPrefix("Bad file name!");
                    return;
                }
                ConfigManager.currentProfile = fileName;
                SigmaNG.getSigmaNG().configManager.saveDefaultConfigWithoutAlt();
                ChatUtils.sendMessageWithPrefix("Save config to .minecraft/sigma5ng/configs/" + fileName + ConfigManager.profileLast + "\n\u00a7eWarning: current profile: " + fileName);
                SigmaNG.getSigmaNG().configManager.saveLastConfigData();
                break;
            case "delete":
                if(args.length == 1){
                    sendUsages();
                    return;
                }
                String fileNameD = args[1];
                if(!isFileNameValid(fileNameD)){
                    ChatUtils.sendMessageWithPrefix("Bad file name!");
                    return;
                }
                if(fileNameD.equals(ConfigManager.currentProfile)){
                    ChatUtils.sendMessageWithPrefix("You cant delete current profile!");
                    return;
                }
                File f = SigmaNG.getSigmaNG().configManager.getConfigFile(fileNameD);
                if(f.isDirectory() || !f.exists()){
                    ChatUtils.sendMessageWithPrefix(fileNameD + ConfigManager.profileLast + " is a directory or not exists!");
                    return;
                }
                f.delete();
                ChatUtils.sendMessageWithPrefix("Delete " + fileNameD + ConfigManager.profileLast + ".");
                break;
            case "list":
                Iterator<File> files = Arrays.stream(Objects.requireNonNull(ConfigManager.configDir.listFiles())).filter((ff) -> ff.isFile() && ff.getName().endsWith(".profile")).iterator();
                StringBuilder availableProfiles = new StringBuilder();
                for (Iterator<File> it = Objects.requireNonNull(files); it.hasNext(); ) {
                    File ff = it.next();
                    availableProfiles.append(ff.getName().replace(".profile", "")).append(!files.hasNext() ? "" : ", ");
                }
                ChatUtils.sendMessageWithPrefix("Available profiles: " + availableProfiles);
                break;
            case "load":
                if(args.length == 1){
                    sendUsages();
                    return;
                }
                String fileName2 = args[1];
                if(!isFileNameValid(fileName2)){
                    ChatUtils.sendMessageWithPrefix("Bad file name!");
                    return;
                }
                if(fileName2.equals(ConfigManager.currentProfile)){
                    ChatUtils.sendMessageWithPrefix("You cant load current profile!");
                    return;
                }
                File ff2 = SigmaNG.getSigmaNG().configManager.getConfigFile(fileName2);
                if(ff2.isDirectory() || !ff2.exists()){
                    ChatUtils.sendMessageWithPrefix(fileName2 + ConfigManager.profileLast + " is a directory or not exists!");
                    return;
                }
                SigmaNG.getSigmaNG().configManager.saveDefaultConfigWithoutAlt();
                ConfigManager.currentProfile = fileName2;
                SigmaNG.getSigmaNG().configManager.saveLastConfigData();
                SigmaNG.getSigmaNG().configManager.loadDefaultConfig();
                ChatUtils.sendMessageWithPrefix("Load config: " + fileName2 + ConfigManager.profileLast + "\n§eWarning: current profile: " + fileName2);
                break;
            case "current":
                ChatUtils.sendMessageWithPrefix("Current file: " + ConfigManager.currentProfile);
                break;
            default:
                sendUsages();
                break;
        }
    }

    @Override
    public String usages() {
        return "(save, delete, list, load, current)";
    }

    @Override
    public String describe() {
        return "Edit configs.";
    }

    @Override
    public String[] getName() {
        return new String[]{"config"};
    }
}
