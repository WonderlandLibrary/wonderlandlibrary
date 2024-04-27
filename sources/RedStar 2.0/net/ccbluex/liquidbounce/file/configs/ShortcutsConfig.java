package net.ccbluex.liquidbounce.file.configs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.command.shortcuts.Shortcut;
import net.ccbluex.liquidbounce.file.FileConfig;
import net.ccbluex.liquidbounce.file.FileManager;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\n\n\b\u000020B\r0¢J\b0HJ\b0H¨\b"}, d2={"Lnet/ccbluex/liquidbounce/file/configs/ShortcutsConfig;", "Lnet/ccbluex/liquidbounce/file/FileConfig;", "file", "Ljava/io/File;", "(Ljava/io/File;)V", "loadConfig", "", "saveConfig", "Pride"})
public final class ShortcutsConfig
extends FileConfig {
    /*
     * WARNING - void declaration
     */
    @Override
    protected void loadConfig() {
        JsonParser jsonParser = new JsonParser();
        File file = this.getFile();
        Intrinsics.checkExpressionValueIsNotNull(file, "file");
        JsonElement jsonElement = jsonParser.parse(FilesKt.readText$default(file, null, 1, null));
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
                Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
                boolean $i$f$mapTo = false;
                for (Object item$iv$iv : $this$mapTo$iv$iv) {
                    void it;
                    JsonElement jsonElement4 = (JsonElement)item$iv$iv;
                    collection = destination$iv$iv;
                    boolean bl2 = false;
                    void v6 = it;
                    Intrinsics.checkExpressionValueIsNotNull(v6, "it");
                    String string = v6.getAsString();
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
                list.add(new Pair(command3, collection2));
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
                pairObject.addProperty("name", pair.getFirst().getCommand());
                JsonArray argumentsObject = new JsonArray();
                for (String argument : pair.getSecond()) {
                    argumentsObject.add(argument);
                }
                pairObject.add("arguments", (JsonElement)argumentsObject);
                scriptArray.add((JsonElement)pairObject);
            }
            jsonCommand.add("script", (JsonElement)scriptArray);
            jsonArray.add((JsonElement)jsonCommand);
        }
        File file = this.getFile();
        Intrinsics.checkExpressionValueIsNotNull(file, "file");
        String string = FileManager.PRETTY_GSON.toJson((JsonElement)jsonArray);
        Intrinsics.checkExpressionValueIsNotNull(string, "FileManager.PRETTY_GSON.toJson(jsonArray)");
        FilesKt.writeText$default(file, string, null, 2, null);
    }

    public ShortcutsConfig(@NotNull File file) {
        Intrinsics.checkParameterIsNotNull(file, "file");
        super(file);
    }
}
