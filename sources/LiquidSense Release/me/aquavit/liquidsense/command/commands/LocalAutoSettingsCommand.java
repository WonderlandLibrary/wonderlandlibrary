package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.misc.StringUtils;
import me.aquavit.liquidsense.utils.client.SettingsUtils;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.ColorType;
import me.aquavit.liquidsense.ui.client.hud.element.elements.extend.Notification;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LocalAutoSettingsCommand extends Command {

    public LocalAutoSettingsCommand(){
        super("localautosettings", "localsetting", "localsettings", "localconfig");
    }

    @Override
    public void execute(String[] args) {
        if (args.length > 1) {
            String arg = args[1].toLowerCase();
            switch (arg) {
                case "load":
                    if (args.length > 2) {
                        File scriptFile = new File(LiquidSense.fileManager.settingsDir, args[2]);
                        if (scriptFile.exists()) {
                            try {
                                chat("§9Loading settings...");
                                final String settings = new String(Files.readAllBytes(scriptFile.toPath()));
                                chat("§9Set settings...");
                                new SettingsUtils().executeScript(settings);
                                chat("§6Settings applied successfully.");
                                LiquidSense.hud.addNotification(new Notification("Updated Settings", "Setting was updated.", ColorType.INFO,1500,500));
                                playEdit();

                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }

                        chat("§cSettings file does not exist!");
                        return;
                    }
                    chatSyntax("localautosettings load <name>");
                    break;
                case "save":
                    if (args.length > 2) {
                        File scriptFile = new File(LiquidSense.fileManager.settingsDir, args[2]);

                        try {
                            if (scriptFile.exists()) {
                                if (scriptFile.delete()) {
                                    System.out.println("Existing file deleted.");
                                } else {
                                    System.out.println("Failed to delete the existing file.");
                                }
                            }

                            if (scriptFile.createNewFile()) {
                                System.out.println("File created successfully.");
                            } else {
                                System.out.println("Failed to create the file.");
                            }

                            String option = args.length > 3 ? StringUtils.toCompleteString(args, 3).toLowerCase() : "values";
                            boolean values = option.contains("all") || option.contains("values");
                            boolean binds = option.contains("all") || option.contains("binds");
                            boolean states = option.contains("all") || option.contains("states");
                            if (!values && !binds && !states) {
                                chatSyntaxError();
                                return;
                            }

                            chat("§9Creating settings...");
                            String settingsScript = new SettingsUtils().generateScript(values, binds, states);
                            chat("§9Saving settings...");
                            try {
                                FileWriter fileWriter = new FileWriter(scriptFile);
                                fileWriter.write(settingsScript);
                                fileWriter.close();
                                chat("§6Settings saved successfully.");
                            } catch (IOException e) {
                                chat("§cFailed to save settings: " + e.getMessage());
                                e.printStackTrace();
                            }
                        } catch (Throwable throwable) {
                            chat("§cFailed to create local config: §3"+throwable.getMessage());
                            ClientUtils.getLogger().error("Failed to create local config.", throwable);
                        }
                        return;
                    }

                    chatSyntax("localsettings save <name> [all/values/binds/states]...");
                    break;
                case "delete":
                    if (args.length > 2) {
                        File scriptFile = new File(LiquidSense.fileManager.settingsDir, args[2]);

                        if (scriptFile.exists()) {
                            if (scriptFile.delete()) {
                                System.out.println("Existing file deleted.");
                            } else {
                                System.out.println("Failed to delete the existing file.");
                            }
                            chat("§6Settings file deleted successfully.");
                            return;
                        }

                        chat("§cSettings file does not exist!");
                        return;
                    }

                    chatSyntax("localsettings delete <name>");
                    break;
                case "list":
                    chat("§cSettings:");

                    File[] settings = this.getLocalSettings();

                    if (settings == null) {
                        return;
                    }

                    for (File file : settings)
                        chat("> " + file.getName());
                    break;
            }
            return;
        }
        chatSyntax("localsettings <load/save/list/delete>");
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 0) return new ArrayList<>();

        switch (args.length) {
            case 1:
                return Arrays.stream(new String[]{"delete", "list", "load", "save"})
                        .filter(it -> it.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            case 2:{
                switch (args[0].toLowerCase()){
                    case "delete":
                    case "load":{
                        File[] settings = this.getLocalSettings();

                        if (settings == null) {
                            return new ArrayList<>();
                        }

                        return Arrays.stream(settings).map(File::getName).filter(it -> it.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
                    }
                    default:
                        return new ArrayList<>();
                }
            }
            default:
                return new ArrayList<>();
        }
    }

    private File[] getLocalSettings(){
        return LiquidSense.fileManager.settingsDir.listFiles();
    }
}
