/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.collections.ArraysKt
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.apache.commons.io.FileUtils
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.special.AntiForge;
import net.ccbluex.liquidbounce.features.special.AutoReconnect;
import net.ccbluex.liquidbounce.features.special.BungeeCordSpoof;
import net.ccbluex.liquidbounce.file.FileManager;
import net.ccbluex.liquidbounce.ui.client.altmanager.sub.altgenerator.GuiTheAltening;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.value.Value;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\bJ!\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\n2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/ConfigCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "tabComplete", "", "([Ljava/lang/String;)Ljava/util/List;", "LiKingSense"})
public final class ConfigCommand
extends Command {
    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void execute(@NotNull String[] args) {
        block43: {
            Intrinsics.checkParameterIsNotNull((Object)args, (String)"args");
            if (args.length < 2) break block43;
            command = args[1];
            dir = LiquidBounce.INSTANCE.getFileManager().configsDir;
            var4_4 = command;
            tmp = -1;
            switch (var4_4.hashCode()) {
                case 3327206: {
                    if (!var4_4.equals("load")) return;
                    tmp = 1;
                    ** GOTO lbl23
                }
                case 3522941: {
                    if (!var4_4.equals("save")) return;
                    tmp = 2;
                    ** GOTO lbl23
                }
                case 3322014: {
                    if (!var4_4.equals("list")) return;
                    tmp = 3;
                    ** GOTO lbl23
                }
                case -1335458389: {
                    if (!var4_4.equals("delete")) return;
                    tmp = 4;
                }
lbl23:
                // 5 sources

                default: {
                    switch (tmp) {
                        case 3: {
                            this.chat("Configs :");
                            var7_5 = dir.listFiles();
                            var8_8 = var7_5.length;
                            for (var6_11 = 0; var6_11 < var8_8; ++var6_11) {
                                v0 = listFile = var7_5[var6_11];
                                Intrinsics.checkExpressionValueIsNotNull((Object)v0, (String)"listFile");
                                v1 = v0.getName();
                                Intrinsics.checkExpressionValueIsNotNull((Object)v1, (String)"listFile.name");
                                this.chat(v1);
                            }
                            return;
                        }
                        case 4: {
                            if (args.length == 3) {
                                if (new File(LiquidBounce.INSTANCE.getFileManager().configsDir, args[2]).exists()) {
                                    try {
                                        FileUtils.forceDelete((File)new File(LiquidBounce.INSTANCE.getFileManager().configsDir, args[2]));
                                        this.chat("Deleted config: " + args[2]);
                                        return;
                                    }
                                    catch (Exception e) {
                                        this.chat("Failed to delete config: " + args[2]);
                                    }
                                    return;
                                }
                                this.chat("Config " + args[2] + " not found.");
                                return;
                            }
                            this.chatSyntax(".config < list / save <name> / load <name> / delete <name> >");
                            return;
                        }
                        case 1: {
                            if (args.length == 3) {
                                try {
                                    jsonElement = new JsonParser().parse((Reader)new BufferedReader(new FileReader(new File(LiquidBounce.INSTANCE.getFileManager().configsDir, args[2]))));
                                    if (jsonElement instanceof JsonNull) {
                                        this.chatSyntax("Config " + args[2] + " not found.");
                                        return;
                                    }
                                    v2 = jsonElement;
                                    if (v2 == null) {
                                        throw new TypeCastException("null cannot be cast to non-null type com.google.gson.JsonObject");
                                    }
                                    jsonObject = (JsonObject)v2;
                                    for (Map.Entry set : jsonObject.entrySet()) {
                                        key = (String)set.getKey();
                                        value = (JsonElement)set.getValue();
                                        if (StringsKt.equals((String)key, (String)"CommandPrefix", (boolean)true)) {
                                            v3 = LiquidBounce.INSTANCE.getCommandManager();
                                            v4 = value;
                                            Intrinsics.checkExpressionValueIsNotNull((Object)v4, (String)"value");
                                            v3.setPrefix(v4.getAsCharacter());
                                            continue;
                                        }
                                        if (StringsKt.equals((String)key, (String)"targets", (boolean)true)) {
                                            v5 = value;
                                            if (v5 == null) {
                                                throw new TypeCastException("null cannot be cast to non-null type com.google.gson.JsonObject");
                                            }
                                            jsonValue = (JsonObject)v5;
                                            if (jsonValue.has("TargetPlayer")) {
                                                v6 = jsonValue.get("TargetPlayer");
                                                Intrinsics.checkExpressionValueIsNotNull((Object)v6, (String)"jsonValue[\"TargetPlayer\"]");
                                                EntityUtils.targetPlayer = v6.getAsBoolean();
                                            }
                                            if (jsonValue.has("TargetMobs")) {
                                                v7 = jsonValue.get("TargetMobs");
                                                Intrinsics.checkExpressionValueIsNotNull((Object)v7, (String)"jsonValue[\"TargetMobs\"]");
                                                EntityUtils.targetMobs = v7.getAsBoolean();
                                            }
                                            if (jsonValue.has("TargetAnimals")) {
                                                v8 = jsonValue.get("TargetAnimals");
                                                Intrinsics.checkExpressionValueIsNotNull((Object)v8, (String)"jsonValue[\"TargetAnimals\"]");
                                                EntityUtils.targetAnimals = v8.getAsBoolean();
                                            }
                                            if (jsonValue.has("TargetInvisible")) {
                                                v9 = jsonValue.get("TargetInvisible");
                                                Intrinsics.checkExpressionValueIsNotNull((Object)v9, (String)"jsonValue[\"TargetInvisible\"]");
                                                EntityUtils.targetInvisible = v9.getAsBoolean();
                                            }
                                            if (!jsonValue.has("TargetDead")) continue;
                                            v10 = jsonValue.get("TargetDead");
                                            Intrinsics.checkExpressionValueIsNotNull((Object)v10, (String)"jsonValue[\"TargetDead\"]");
                                            EntityUtils.targetDead = v10.getAsBoolean();
                                            continue;
                                        }
                                        if (StringsKt.equals((String)key, (String)"features", (boolean)true)) {
                                            v11 = value;
                                            if (v11 == null) {
                                                throw new TypeCastException("null cannot be cast to non-null type com.google.gson.JsonObject");
                                            }
                                            jsonValue = (JsonObject)v11;
                                            if (jsonValue.has("AntiForge")) {
                                                v12 = jsonValue.get("AntiForge");
                                                Intrinsics.checkExpressionValueIsNotNull((Object)v12, (String)"jsonValue[\"AntiForge\"]");
                                                AntiForge.enabled = v12.getAsBoolean();
                                            }
                                            if (jsonValue.has("AntiForgeFML")) {
                                                v13 = jsonValue.get("AntiForgeFML");
                                                Intrinsics.checkExpressionValueIsNotNull((Object)v13, (String)"jsonValue[\"AntiForgeFML\"]");
                                                AntiForge.blockFML = v13.getAsBoolean();
                                            }
                                            if (jsonValue.has("AntiForgeProxy")) {
                                                v14 = jsonValue.get("AntiForgeProxy");
                                                Intrinsics.checkExpressionValueIsNotNull((Object)v14, (String)"jsonValue[\"AntiForgeProxy\"]");
                                                AntiForge.blockProxyPacket = v14.getAsBoolean();
                                            }
                                            if (jsonValue.has("AntiForgePayloads")) {
                                                v15 = jsonValue.get("AntiForgePayloads");
                                                Intrinsics.checkExpressionValueIsNotNull((Object)v15, (String)"jsonValue[\"AntiForgePayloads\"]");
                                                AntiForge.blockPayloadPackets = v15.getAsBoolean();
                                            }
                                            if (jsonValue.has("BungeeSpoof")) {
                                                v16 = jsonValue.get("BungeeSpoof");
                                                Intrinsics.checkExpressionValueIsNotNull((Object)v16, (String)"jsonValue[\"BungeeSpoof\"]");
                                                BungeeCordSpoof.enabled = v16.getAsBoolean();
                                            }
                                            if (!jsonValue.has("AutoReconnectDelay")) continue;
                                            v17 = jsonValue.get("AutoReconnectDelay");
                                            Intrinsics.checkExpressionValueIsNotNull((Object)v17, (String)"jsonValue[\"AutoReconnectDelay\"]");
                                            AutoReconnect.INSTANCE.setDelay(v17.getAsInt());
                                            continue;
                                        }
                                        if (StringsKt.equals((String)key, (String)"thealtening", (boolean)true)) {
                                            v18 = value;
                                            if (v18 == null) {
                                                throw new TypeCastException("null cannot be cast to non-null type com.google.gson.JsonObject");
                                            }
                                            jsonValue = (JsonObject)v18;
                                            if (!jsonValue.has("API-Key")) continue;
                                            v19 = jsonValue.get("API-Key");
                                            Intrinsics.checkExpressionValueIsNotNull((Object)v19, (String)"jsonValue[\"API-Key\"]");
                                            v20 = v19.getAsString();
                                            Intrinsics.checkExpressionValueIsNotNull((Object)v20, (String)"jsonValue[\"API-Key\"].asString");
                                            GuiTheAltening.Companion.setApiKey(v20);
                                            continue;
                                        }
                                        module = LiquidBounce.INSTANCE.getModuleManager().getModule(key);
                                        if (module == null) continue;
                                        v21 = value;
                                        if (v21 == null) {
                                            throw new TypeCastException("null cannot be cast to non-null type com.google.gson.JsonObject");
                                        }
                                        jsonModule = (JsonObject)v21;
                                        v22 = jsonModule.get("State");
                                        Intrinsics.checkExpressionValueIsNotNull((Object)v22, (String)"jsonModule[\"State\"]");
                                        module.setState(v22.getAsBoolean());
                                        v23 = jsonModule.get("KeyBind");
                                        Intrinsics.checkExpressionValueIsNotNull((Object)v23, (String)"jsonModule[\"KeyBind\"]");
                                        module.setKeyBind(v23.getAsInt());
                                        if (jsonModule.has("Array")) {
                                            v24 = jsonModule.get("Array");
                                            Intrinsics.checkExpressionValueIsNotNull((Object)v24, (String)"jsonModule[\"Array\"]");
                                            module.setArray(v24.getAsBoolean());
                                        }
                                        for (Value<?> moduleValue : module.getValues()) {
                                            element = jsonModule.get(moduleValue.getName());
                                            if (element == null) continue;
                                            moduleValue.fromJson(element);
                                        }
                                    }
                                    this.chat("Loaded config: " + args[2]);
                                    return;
                                }
                                catch (Throwable e) {
                                    this.chat("Failed to load config: " + args[2]);
                                }
                                return;
                            }
                            this.chatSyntax(".config < list / save <name> / load <name> / delete <name> >");
                            return;
                        }
                        case 2: {
                            if (args.length == 3) {
                                jsonObject = new JsonObject();
                                jsonObject.addProperty("CommandPrefix", Character.valueOf(LiquidBounce.INSTANCE.getCommandManager().getPrefix()));
                                jsonTargets = new JsonObject();
                                jsonTargets.addProperty("TargetPlayer", Boolean.valueOf(EntityUtils.targetPlayer));
                                jsonTargets.addProperty("TargetMobs", Boolean.valueOf(EntityUtils.targetMobs));
                                jsonTargets.addProperty("TargetAnimals", Boolean.valueOf(EntityUtils.targetAnimals));
                                jsonTargets.addProperty("TargetInvisible", Boolean.valueOf(EntityUtils.targetInvisible));
                                jsonTargets.addProperty("TargetDead", Boolean.valueOf(EntityUtils.targetDead));
                                jsonObject.add("targets", (JsonElement)jsonTargets);
                                jsonFeatures = new JsonObject();
                                jsonFeatures.addProperty("AntiForge", Boolean.valueOf(AntiForge.enabled));
                                jsonFeatures.addProperty("AntiForgeFML", Boolean.valueOf(AntiForge.blockFML));
                                jsonFeatures.addProperty("AntiForgeProxy", Boolean.valueOf(AntiForge.blockProxyPacket));
                                jsonFeatures.addProperty("AntiForgePayloads", Boolean.valueOf(AntiForge.blockPayloadPackets));
                                jsonFeatures.addProperty("BungeeSpoof", Boolean.valueOf(BungeeCordSpoof.enabled));
                                jsonFeatures.addProperty("AutoReconnectDelay", (Number)AutoReconnect.INSTANCE.getDelay());
                                jsonObject.add("features", (JsonElement)jsonFeatures);
                                theAlteningObject = new JsonObject();
                                theAlteningObject.addProperty("API-Key", GuiTheAltening.Companion.getApiKey());
                                jsonObject.add("thealtening", (JsonElement)theAlteningObject);
                                LiquidBounce.INSTANCE.getModuleManager().getModules().stream().forEach((Consumer)new Consumer<Module>(jsonObject){
                                    final /* synthetic */ JsonObject $jsonObject;

                                    public final void accept(@NotNull Module module) {
                                        Intrinsics.checkParameterIsNotNull((Object)module, (String)"module");
                                        JsonObject jsonModule = new JsonObject();
                                        jsonModule.addProperty("State", Boolean.valueOf(module.getState()));
                                        jsonModule.addProperty("KeyBind", (Number)module.getKeyBind());
                                        jsonModule.addProperty("Array", Boolean.valueOf(module.getArray()));
                                        module.getValues().forEach(new Consumer<Value<?>>(jsonModule){
                                            final /* synthetic */ JsonObject $jsonModule;

                                            public final void accept(@NotNull Value<?> value) {
                                                Intrinsics.checkParameterIsNotNull(value, (String)"value");
                                                this.$jsonModule.add(value.getName(), value.toJson());
                                            }
                                            {
                                                this.$jsonModule = jsonObject;
                                            }
                                        });
                                        this.$jsonObject.add(module.getName(), (JsonElement)jsonModule);
                                    }
                                    {
                                        this.$jsonObject = jsonObject;
                                    }
                                });
                                printWriter = new PrintWriter(new FileWriter(new File(LiquidBounce.INSTANCE.getFileManager().configsDir, args[2])));
                                printWriter.println(FileManager.PRETTY_GSON.toJson((JsonElement)jsonObject));
                                printWriter.close();
                                this.chat("Saved config " + args[2]);
                                return;
                            }
                            this.chatSyntax(".config < list / save <name> / load <name> / delete <name> >");
                            return;
                        }
                        default: {
                            return;
                        }
                    }
                }
            }
        }
        this.chatSyntax(".config < list / save <name> / load <name> / delete <name> >");
    }

    @Override
    @NotNull
    public List<String> tabComplete(@NotNull String[] args) {
        List list;
        Intrinsics.checkParameterIsNotNull((Object)args, (String)"args");
        String[] stringArray = args;
        boolean bl = false;
        if (stringArray.length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (args.length) {
            case 1: {
                list = ArraysKt.toList((Object[])new String[]{"list", "save", "load", "delete"});
                break;
            }
            case 2: {
                ArrayList<String> array = new ArrayList<String>();
                File[] fileArray = LiquidBounce.INSTANCE.getFileManager().configsDir.listFiles();
                int n = fileArray.length;
                for (int i = 0; i < n; ++i) {
                    File listFile;
                    File file = listFile = fileArray[i];
                    Intrinsics.checkExpressionValueIsNotNull((Object)file, (String)"listFile");
                    array.add(file.getName());
                }
                list = array;
                break;
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    public ConfigCommand() {
        super("config", new String[0]);
    }
}

