package me.aquavit.liquidsense.file.configs;

import com.google.gson.*;
import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.data.Pair;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.command.shortcuts.Shortcut;
import me.aquavit.liquidsense.file.FileConfig;
import me.aquavit.liquidsense.file.FileManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShortcutsConfig extends FileConfig {

    public ShortcutsConfig(final File file) {
        super(file);
    }

    @Override
    protected void loadConfig() throws IOException {

        final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(getFile())));

        if (!(jsonElement instanceof JsonArray)) {
            return;
        }

        for (JsonElement shortcutJson : (JsonArray)jsonElement) {

            if (!(shortcutJson instanceof JsonObject)) continue;

            String name = ((JsonObject)shortcutJson).get("name").getAsString();
            if (name == null) continue;
            JsonArray scriptJson = ((JsonObject)shortcutJson).get("script").getAsJsonArray();
            if (scriptJson == null) continue;

            List script = new ArrayList<>();

            for (JsonElement scriptCommand : scriptJson) {
                if (!(scriptCommand instanceof JsonObject)) continue;

                String commandName = ((JsonObject)scriptCommand).get("name").getAsString();
                if (commandName == null) continue;
                JsonArray arguments = ((JsonObject)scriptCommand).get("arguments").getAsJsonArray();
                if (arguments == null) continue;

                Command command = LiquidSense.commandManager.getCommand(commandName);
                if (command == null) continue;

                List temp = new ArrayList<>();
                for (JsonElement argument : arguments) {
                    temp.add(argument.getAsString());
                }

                script.add(new Pair(command, temp.toArray()));
            }
            LiquidSense.commandManager.registerCommand(new Shortcut(name, script));
        }
    }

    @Override
    protected void saveConfig() throws IOException {
        JsonArray jsonArray = new JsonArray();

        for (Command command : LiquidSense.commandManager.getCommands()) {
            if (!(command instanceof Shortcut)) continue;

            JsonObject jsonCommand = new JsonObject();
            jsonCommand.addProperty("name", command.getCommand());

            JsonArray scriptArray = new JsonArray();

            for (Pair<Command, String[]> pair : ((Shortcut) command).getScript()) {
                JsonObject pairObject = new JsonObject();


                pairObject.addProperty("name", pair.getFirst().getCommand());

                JsonArray argumentsObject = new JsonArray();
                for (String argument : pair.getSecond()) {
                    argumentsObject.add(argument);
                }

                pairObject.add("arguments", argumentsObject);

                scriptArray.add(pairObject);
            }

            jsonCommand.add("script", scriptArray);

            jsonArray.add(jsonCommand);
        }
        final PrintWriter printWriter = new PrintWriter(new FileWriter(getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson(jsonArray));
        printWriter.close();
    }
}
