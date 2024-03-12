// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonObject;
import dev.tenacity.utils.misc.Multithreading;
import dev.tenacity.Tenacity;
import net.minecraft.client.Minecraft;
import java.io.File;
import dev.tenacity.intent.cloud.CloudUtils;
import dev.tenacity.commands.Command;

public class LoadCommand extends Command
{
    public LoadCommand() {
        super("load", "Loads a script or config from the cloud.", ".load <share code>", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length != 1) {
            this.usage();
        }
        else {
            final String shareCode = args[0];
            final String shareCode2;
            final JsonObject object;
            String[] meta;
            String name;
            boolean isScript;
            String objectData;
            final File file;
            File scriptFile;
            Multithreading.runAsync(() -> {
                this.sendChatWithPrefix("Loading script or config from the cloud...");
                object = CloudUtils.getData(shareCode2);
                if (object == null) {
                    this.sendChatError("The share code was invalid!");
                }
                else {
                    meta = object.get("meta").getAsString().split(":");
                    name = object.get("name").getAsString();
                    isScript = meta[1].equals("true");
                    objectData = object.get("body").getAsString();
                    if (isScript) {
                        new File(Minecraft.getMinecraft().mcDataDir + "/Tenacity/Scripts/" + name + ".js");
                        scriptFile = file;
                        this.downloadScriptToFile(scriptFile, objectData);
                        Tenacity.INSTANCE.getSideGui().getScriptPanel().refresh();
                    }
                    else if (Tenacity.INSTANCE.getConfigManager().loadConfig(objectData, false)) {
                        this.sendChatWithPrefix("Config loaded successfully!");
                    }
                    else {
                        this.sendChatError("The online config did not load successfully!");
                    }
                }
            });
        }
    }
    
    public void downloadScriptToFile(final File file, final String content) {
        try {
            Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
            this.sendChatWithPrefix("Script downloaded to " + file.getPath());
        }
        catch (IOException e) {
            e.printStackTrace();
            this.sendChatError("Could not download script to " + file.getAbsolutePath());
        }
    }
}
