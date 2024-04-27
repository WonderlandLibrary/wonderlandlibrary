/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonNull
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  kotlin.TypeCastException
 *  kotlin.collections.ArraysKt
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.apache.commons.io.FileUtils
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

public final class ConfigCommand
extends Command {
    public ConfigCommand() {
        super("config", new String[0]);
    }

    @Override
    public List tabComplete(String[] stringArray) {
        List list;
        Object object = stringArray;
        boolean bl = false;
        if (((String[])object).length == 0) {
            return CollectionsKt.emptyList();
        }
        switch (stringArray.length) {
            case 1: {
                list = ArraysKt.toList((Object[])new String[]{"list", "save", "load", "delete"});
                break;
            }
            case 2: {
                object = new ArrayList();
                File file = LiquidBounce.INSTANCE.getFileManager().configsDir;
                if (file == null) {
                    Intrinsics.throwNpe();
                }
                for (File file2 : file.listFiles()) {
                    ((ArrayList)object).add(file2.getName());
                }
                list = (List)object;
                break;
            }
            default: {
                list = CollectionsKt.emptyList();
            }
        }
        return list;
    }

    /*
     * Unable to fully structure code
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void execute(String[] var1_1) {
        block44: {
            if (var1_1.length < 2) break block44;
            var2_2 = var1_1[1];
            v0 = LiquidBounce.INSTANCE.getFileManager().configsDir;
            if (v0 == null) {
                Intrinsics.throwNpe();
            }
            var3_3 = v0;
            var4_4 = var2_2;
            tmp = -1;
            switch (var4_4.hashCode()) {
                case 3327206: {
                    if (!var4_4.equals("load")) return;
                    tmp = 1;
                    ** GOTO lbl25
                }
                case 3522941: {
                    if (!var4_4.equals("save")) return;
                    tmp = 2;
                    ** GOTO lbl25
                }
                case 3322014: {
                    if (!var4_4.equals("list")) return;
                    tmp = 3;
                    ** GOTO lbl25
                }
                case -1335458389: {
                    if (!var4_4.equals("delete")) return;
                    tmp = 4;
                }
lbl25:
                // 5 sources

                default: {
                    switch (tmp) {
                        case 3: {
                            this.chat("Configs :");
                            for (File var5_14 : var3_3.listFiles()) {
                                this.chat(var5_14.getName());
                            }
                            return;
                        }
                        case 4: {
                            if (var1_1.length == 3) {
                                if (new File(LiquidBounce.INSTANCE.getFileManager().configsDir, var1_1[2]).exists()) {
                                    try {
                                        FileUtils.forceDelete((File)new File(LiquidBounce.INSTANCE.getFileManager().configsDir, var1_1[2]));
                                        this.chat("Deleted config: " + var1_1[2]);
                                        return;
                                    }
                                    catch (Exception var5_15) {
                                        this.chat("Failed to delete config: " + var1_1[2]);
                                    }
                                    return;
                                }
                                this.chat("Config " + var1_1[2] + " not found.");
                                return;
                            }
                            this.chatSyntax(".config < list / save <name> / load <name> / delete <name> >");
                            return;
                        }
                        case 1: {
                            if (var1_1.length == 3) {
                                try {
                                    var5_16 = new JsonParser().parse((Reader)new BufferedReader(new FileReader(new File(LiquidBounce.INSTANCE.getFileManager().configsDir, var1_1[2]))));
                                    if (var5_16 instanceof JsonNull) {
                                        this.chatSyntax("Config " + var1_1[2] + " not found.");
                                        return;
                                    }
                                    v1 = var5_16;
                                    if (v1 == null) {
                                        throw new TypeCastException("null cannot be cast to non-null type com.google.gson.JsonObject");
                                    }
                                    var6_12 = (JsonObject)v1;
                                    for (Map.Entry var7_6 : var6_12.entrySet()) {
                                        var9_19 = (String)var7_6.getKey();
                                        var10_21 = (JsonElement)var7_6.getValue();
                                        if (StringsKt.equals((String)var9_19, (String)"CommandPrefix", (boolean)true)) {
                                            LiquidBounce.INSTANCE.getCommandManager().setPrefix(var10_21.getAsCharacter());
                                            continue;
                                        }
                                        if (StringsKt.equals((String)var9_19, (String)"targets", (boolean)true)) {
                                            v2 = var10_21;
                                            if (v2 == null) {
                                                throw new TypeCastException("null cannot be cast to non-null type com.google.gson.JsonObject");
                                            }
                                            var11_22 = (JsonObject)v2;
                                            if (var11_22.has("TargetPlayer")) {
                                                EntityUtils.targetPlayer = var11_22.get("TargetPlayer").getAsBoolean();
                                            }
                                            if (var11_22.has("TargetMobs")) {
                                                EntityUtils.targetMobs = var11_22.get("TargetMobs").getAsBoolean();
                                            }
                                            if (var11_22.has("TargetAnimals")) {
                                                EntityUtils.targetAnimals = var11_22.get("TargetAnimals").getAsBoolean();
                                            }
                                            if (var11_22.has("TargetInvisible")) {
                                                EntityUtils.targetInvisible = var11_22.get("TargetInvisible").getAsBoolean();
                                            }
                                            if (!var11_22.has("TargetDead")) continue;
                                            EntityUtils.targetDead = var11_22.get("TargetDead").getAsBoolean();
                                            continue;
                                        }
                                        if (StringsKt.equals((String)var9_19, (String)"features", (boolean)true)) {
                                            v3 = var10_21;
                                            if (v3 == null) {
                                                throw new TypeCastException("null cannot be cast to non-null type com.google.gson.JsonObject");
                                            }
                                            var11_22 = (JsonObject)v3;
                                            if (var11_22.has("AntiForge")) {
                                                AntiForge.enabled = var11_22.get("AntiForge").getAsBoolean();
                                            }
                                            if (var11_22.has("AntiForgeFML")) {
                                                AntiForge.blockFML = var11_22.get("AntiForgeFML").getAsBoolean();
                                            }
                                            if (var11_22.has("AntiForgeProxy")) {
                                                AntiForge.blockProxyPacket = var11_22.get("AntiForgeProxy").getAsBoolean();
                                            }
                                            if (var11_22.has("AntiForgePayloads")) {
                                                AntiForge.blockPayloadPackets = var11_22.get("AntiForgePayloads").getAsBoolean();
                                            }
                                            if (var11_22.has("BungeeSpoof")) {
                                                BungeeCordSpoof.enabled = var11_22.get("BungeeSpoof").getAsBoolean();
                                            }
                                            if (!var11_22.has("AutoReconnectDelay")) continue;
                                            AutoReconnect.INSTANCE.setDelay(var11_22.get("AutoReconnectDelay").getAsInt());
                                            continue;
                                        }
                                        if (StringsKt.equals((String)var9_19, (String)"thealtening", (boolean)true)) {
                                            v4 = var10_21;
                                            if (v4 == null) {
                                                throw new TypeCastException("null cannot be cast to non-null type com.google.gson.JsonObject");
                                            }
                                            var11_22 = (JsonObject)v4;
                                            if (!var11_22.has("API-Key")) continue;
                                            GuiTheAltening.Companion.setApiKey(var11_22.get("API-Key").getAsString());
                                            continue;
                                        }
                                        var11_22 = LiquidBounce.INSTANCE.getModuleManager().getModule(var9_19);
                                        if (var11_22 == null) continue;
                                        v5 = var10_21;
                                        if (v5 == null) {
                                            throw new TypeCastException("null cannot be cast to non-null type com.google.gson.JsonObject");
                                        }
                                        var12_23 = (JsonObject)v5;
                                        var11_22.setState(var12_23.get("State").getAsBoolean());
                                        var11_22.setKeyBind(var12_23.get("KeyBind").getAsInt());
                                        if (var12_23.has("Array")) {
                                            var11_22.setArray(var12_23.get("Array").getAsBoolean());
                                        }
                                        for (Value var13_24 : var11_22.getValues()) {
                                            var15_26 = var12_23.get(var13_24.getName());
                                            if (var15_26 == null) continue;
                                            var13_24.fromJson(var15_26);
                                        }
                                    }
                                    this.chat("Loaded config: " + var1_1[2]);
                                    return;
                                }
                                catch (Throwable var5_17) {
                                    this.chat("Failed to load config: " + var1_1[2]);
                                }
                                return;
                            }
                            this.chatSyntax(".config < list / save <name> / load <name> / delete <name> >");
                            return;
                        }
                        case 2: {
                            if (var1_1.length == 3) {
                                var5_18 = new JsonObject();
                                var5_18.addProperty("CommandPrefix", Character.valueOf(LiquidBounce.INSTANCE.getCommandManager().getPrefix()));
                                var6_13 = new JsonObject();
                                var6_13.addProperty("TargetPlayer", Boolean.valueOf(EntityUtils.targetPlayer));
                                var6_13.addProperty("TargetMobs", Boolean.valueOf(EntityUtils.targetMobs));
                                var6_13.addProperty("TargetAnimals", Boolean.valueOf(EntityUtils.targetAnimals));
                                var6_13.addProperty("TargetInvisible", Boolean.valueOf(EntityUtils.targetInvisible));
                                var6_13.addProperty("TargetDead", Boolean.valueOf(EntityUtils.targetDead));
                                var5_18.add("targets", (JsonElement)var6_13);
                                var7_7 = new JsonObject();
                                var7_7.addProperty("AntiForge", Boolean.valueOf(AntiForge.enabled));
                                var7_7.addProperty("AntiForgeFML", Boolean.valueOf(AntiForge.blockFML));
                                var7_7.addProperty("AntiForgeProxy", Boolean.valueOf(AntiForge.blockProxyPacket));
                                var7_7.addProperty("AntiForgePayloads", Boolean.valueOf(AntiForge.blockPayloadPackets));
                                var7_7.addProperty("BungeeSpoof", Boolean.valueOf(BungeeCordSpoof.enabled));
                                var7_7.addProperty("AutoReconnectDelay", (Number)AutoReconnect.INSTANCE.getDelay());
                                var5_18.add("features", (JsonElement)var7_7);
                                var8_10 = new JsonObject();
                                var8_10.addProperty("API-Key", GuiTheAltening.Companion.getApiKey());
                                var5_18.add("thealtening", (JsonElement)var8_10);
                                LiquidBounce.INSTANCE.getModuleManager().getModules().stream().forEach(new Consumer(var5_18){
                                    final JsonObject $jsonObject;

                                    public final void accept(Module module) {
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("State", Boolean.valueOf(module.getState()));
                                        jsonObject.addProperty("KeyBind", (Number)module.getKeyBind());
                                        jsonObject.addProperty("Array", Boolean.valueOf(module.getArray()));
                                        module.getValues().forEach(new Consumer(jsonObject){
                                            final JsonObject $jsonModule;

                                            public void accept(Object object) {
                                                this.accept((Value)object);
                                            }
                                            {
                                                this.$jsonModule = jsonObject;
                                            }

                                            static {
                                            }

                                            public final void accept(Value value) {
                                                this.$jsonModule.add(value.getName(), value.toJson());
                                            }
                                        });
                                        this.$jsonObject.add(module.getName(), (JsonElement)jsonObject);
                                    }

                                    static {
                                    }
                                    {
                                        this.$jsonObject = jsonObject;
                                    }

                                    public void accept(Object object) {
                                        this.accept((Module)object);
                                    }
                                });
                                var9_20 = new PrintWriter(new FileWriter(new File(LiquidBounce.INSTANCE.getFileManager().configsDir, var1_1[2])));
                                var9_20.println(FileManager.PRETTY_GSON.toJson((JsonElement)var5_18));
                                var9_20.close();
                                this.chat("Saved config " + var1_1[2]);
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
}

