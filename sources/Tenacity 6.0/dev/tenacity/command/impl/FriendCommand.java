package dev.tenacity.command.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.tenacity.command.AbstractCommand;
import dev.tenacity.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FriendCommand extends AbstractCommand {

    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    private static final File file = new File(Minecraft.getMinecraft().mcDataDir + "/Tenacity/Friends.json");
    public static final List<String> friends = new ArrayList<>();

    public FriendCommand() {
        super("friend", "Manage friends", ".friend [add/remove] [username] | .f [add/remove] [username]", 1);
        load();
    }

    @Override
    public void onCommand(final String[] arguments) {
        boolean usage = false;

        if(arguments.length == 0) {
            ChatUtil.print("Friend list (§d" + friends.size() + "§7):");
            if(friends.isEmpty()) {
                ChatUtil.print("§7- You do not have any friends! :(");
            } else {
                friends.forEach(friend -> ChatUtil.print("§7- " + friend));
            }
        } else if(arguments.length >= 2) {
            String name = String.join(" ", Arrays.copyOfRange(arguments, 1, arguments.length));

            switch (arguments[0].toLowerCase()) {
                case "add": {
                    if (isFriend(name)) {
                        ChatUtil.print("That player is already in your friends list!");
                    } else {
                        friends.add(name);
                        ChatUtil.print("Added §d" + name + " §7to your friends list!");
                    }
                    save();
                    break;
                }

                case "remove": {
                    if (isFriend(name)) {
                        friends.removeIf(f -> f.equalsIgnoreCase(name));
                        ChatUtil.print("Removed §d" + name + " §7from your friends list.");
                    } else {
                        ChatUtil.print("That player is not in your friends list!");
                    }
                    save();
                    break;
                }

                default:
                    usage = true;
                    break;
            }
        } else {
            usage = true;
        }

        if(usage) {
            ChatUtil.error("Usage: " + getUsage());
        }
    }

    private void load() {
        if (!file.exists()) save();
        try {
            JsonObject object = gson.fromJson(new String(Files.readAllBytes(file.toPath())), JsonObject.class);
            object.get("friends").getAsJsonArray().forEach(f -> friends.add(f.getAsString()));
        } catch (Exception e) {
            System.out.println("Failed to load " + file);
            e.printStackTrace();
        }
    }

    public static boolean save() {
        try {
            if (!file.exists() && file.getParentFile().mkdirs()) {
                file.createNewFile();
            }
            Files.write(file.toPath(), serialize().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String serialize() {
        JsonObject obj = new JsonObject();
        JsonArray arr = new JsonArray();
        friends.forEach(arr::add);
        obj.add("friends", arr);
        return gson.toJson(obj);
    }

    public static boolean isFriend(String name) {
        if (name == null) return false;
        return friends.stream().anyMatch(name::equalsIgnoreCase);
    }

}

