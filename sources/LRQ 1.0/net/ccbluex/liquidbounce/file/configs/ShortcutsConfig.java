/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  kotlin.Pair
 *  kotlin.TypeCastException
 *  kotlin.collections.CollectionsKt
 *  kotlin.io.FilesKt
 */
package net.ccbluex.liquidbounce.file.configs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Pair;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.command.shortcuts.Shortcut;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.file.FileManager;

public final class ShortcutsConfig
extends FileConfig {
    /*
     * WARNING - void declaration
     */
    @Override
    protected void loadConfig() {
        JsonElement jsonElement = new JsonParser().parse(FilesKt.readText$default((File)this.getFile(), null, (int)1, null));
        if (!(jsonElement instanceof JsonArray)) {
            return;
        }
        for (JsonElement shortcutJson : (JsonArray)jsonElement) {
            if (!(shortcutJson instanceof JsonObject)) continue;
            Object object = ((JsonObject)shortcutJson).get("name");
            if (object == null || (object = object.getAsString()) == null) {
                continue;
            }
            Object name = object;
            JsonElement jsonElement2 = ((JsonObject)shortcutJson).get("script");
            if (jsonElement2 == null || (jsonElement2 = jsonElement2.getAsJsonArray()) == null) {
                continue;
            }
            JsonElement scriptJson = jsonElement2;
            boolean bl = false;
            List script = new ArrayList();
            for (JsonElement scriptCommand : scriptJson) {
                void $this$toTypedArray$iv;
                Collection<String> collection;
                void $this$mapTo$iv$iv;
                Collection $this$map$iv;
                Command command;
                if (!(scriptCommand instanceof JsonObject)) continue;
                Object object2 = ((JsonObject)scriptCommand).get("name");
                if (object2 == null || (object2 = object2.getAsString()) == null) {
                    continue;
                }
                Object commandName = object2;
                JsonElement jsonElement3 = ((JsonObject)scriptCommand).get("arguments");
                if (jsonElement3 == null || (jsonElement3 = jsonElement3.getAsJsonArray()) == null) {
                    continue;
                }
                JsonElement arguments = jsonElement3;
                if (LiquidBounce.INSTANCE.getCommandManager().getCommand((String)commandName) == null) {
                    continue;
                }
                Iterable iterable = (Iterable)arguments;
                Command command2 = command;
                List list = script;
                boolean $i$f$map = false;
                void var14_15 = $this$map$iv;
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault((Iterable)$this$map$iv, (int)10));
                boolean $i$f$mapTo = false;
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    void it;
                    JsonElement jsonElement4 = (JsonElement)item$iv$iv;
                    collection = destination$iv$iv;
                    boolean bl2 = false;
                    String string = it.getAsString();
                    collection.add(string);
                }
                collection = (List)destination$iv$iv;
                $this$map$iv = collection;
                boolean $i$f$toTypedArray = false;
                void thisCollection$iv = $this$toTypedArray$iv;
                String[] stringArray = thisCollection$iv.toArray(new String[0]);
                if (stringArray == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                }
                Collection<String> collection2 = collection = stringArray;
                Command command3 = command2;
                list.add(new Pair((Object)command3, collection2));
            }
            LiquidBounce.INSTANCE.getCommandManager().registerCommand(new Shortcut((String)name, script));
        }
    }

    @Override
    protected void saveConfig() {
        JsonArray jsonArray = new JsonArray();
        for (Command command : LiquidBounce.INSTANCE.getCommandManager().getCommands()) {
            if (!(command instanceof Shortcut)) continue;
            JsonObject jsonCommand = new JsonObject();
            jsonCommand.addProperty("name", command.getCommand());
            JsonArray scriptArray = new JsonArray();
            for (Pair<Command, String[]> pair : ((Shortcut)command).getScript()) {
                JsonObject pairObject = new JsonObject();
                pairObject.addProperty("name", ((Command)pair.getFirst()).getCommand());
                JsonArray argumentsObject = new JsonArray();
                for (String argument : (String[])pair.getSecond()) {
                    argumentsObject.add(argument);
                }
                pairObject.add("arguments", (JsonElement)argumentsObject);
                scriptArray.add((JsonElement)pairObject);
            }
            jsonCommand.add("script", (JsonElement)scriptArray);
            jsonArray.add((JsonElement)jsonCommand);
        }
        FilesKt.writeText$default((File)this.getFile(), (String)FileManager.PRETTY_GSON.toJson((JsonElement)jsonArray), null, (int)2, null);
    }

    public ShortcutsConfig(File file) {
        super(file);
    }
}

