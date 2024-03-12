// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands.impl;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;
import java.util.function.Predicate;
import com.google.gson.JsonElement;
import java.util.function.Consumer;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.charset.StandardCharsets;
import dev.tenacity.Tenacity;
import java.nio.file.Files;
import com.google.gson.JsonObject;
import java.util.Arrays;
import dev.tenacity.utils.player.ChatUtil;
import java.util.List;
import java.io.File;
import com.google.gson.Gson;
import dev.tenacity.commands.Command;

public class FriendCommand extends Command
{
    private static final Gson gson;
    private static final File file;
    public static final List<String> friends;
    
    public FriendCommand() {
        super("friend", "Manage friends", ".f [add/remove] [username]", new String[] { "f" });
        this.load();
    }
    
    @Override
    public void execute(final String[] args) {
        boolean usage = false;
        if (args.length == 0) {
            ChatUtil.print("Friend list (§d" + FriendCommand.friends.size() + "§7):");
            if (FriendCommand.friends.isEmpty()) {
                ChatUtil.print(false, "§7- You do not have any friends! :(");
            }
            else {
                FriendCommand.friends.forEach(f -> ChatUtil.print(false, "§7- " + f));
            }
        }
        else if (args.length >= 2) {
            final String name = String.join(" ", (CharSequence[])Arrays.copyOfRange(args, 1, args.length));
            final String lowerCase = args[0].toLowerCase();
            switch (lowerCase) {
                case "add": {
                    if (isFriend(name)) {
                        ChatUtil.print("That player is already in your friends list!");
                    }
                    else {
                        FriendCommand.friends.add(name);
                        ChatUtil.print("Added §d" + name + " §7to your friends list!");
                    }
                    save();
                    break;
                }
                case "remove": {
                    if (isFriend(name)) {
                        FriendCommand.friends.removeIf(f -> f.equalsIgnoreCase(name));
                        ChatUtil.print("Removed §d" + name + " §7from your friends list.");
                    }
                    else {
                        ChatUtil.print("That player is not in your friends list!");
                    }
                    save();
                    break;
                }
                default: {
                    usage = true;
                    break;
                }
            }
        }
        else {
            usage = true;
        }
        if (usage) {
            ChatUtil.print("Usage: " + this.getUsage());
        }
    }
    
    private void load() {
        if (!FriendCommand.file.exists()) {
            save();
        }
        try {
            final JsonObject object = (JsonObject)FriendCommand.gson.fromJson(new String(Files.readAllBytes(FriendCommand.file.toPath())), (Class)JsonObject.class);
            object.get("friends").getAsJsonArray().forEach(f -> FriendCommand.friends.add(f.getAsString()));
        }
        catch (Exception e) {
            Tenacity.LOGGER.error("Failed to load " + FriendCommand.file, (Throwable)e);
        }
    }
    
    public static boolean save() {
        try {
            if (!FriendCommand.file.exists() && FriendCommand.file.getParentFile().mkdirs()) {
                FriendCommand.file.createNewFile();
            }
            Files.write(FriendCommand.file.toPath(), serialize().getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static String serialize() {
        final JsonObject obj = new JsonObject();
        final JsonArray arr = new JsonArray();
        FriendCommand.friends.forEach(arr::add);
        obj.add("friends", (JsonElement)arr);
        return FriendCommand.gson.toJson((JsonElement)obj);
    }
    
    public static boolean isFriend(final String name) {
        return name != null && FriendCommand.friends.stream().anyMatch((Predicate<? super Object>)name::equalsIgnoreCase);
    }
    
    static {
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        file = new File(Tenacity.DIRECTORY, "Friends.json");
        friends = new ArrayList<String>();
    }
}
