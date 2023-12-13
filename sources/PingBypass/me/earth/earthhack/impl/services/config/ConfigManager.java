package me.earth.earthhack.impl.services.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.impl.services.client.FriendManager;
import me.earth.earthhack.impl.services.client.ModuleManager;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

/**
 * Quick and dirty so I can release.
 */
public class ConfigManager
{
    private static final ConfigManager INSTANCE = new ConfigManager();
    private static final String PATH = "earthhack/";
    private static final JsonParser PARSER = new JsonParser();

    private ConfigManager() { /* This is a Singleton. */ }

    public static ConfigManager getInstance()
    {
        return INSTANCE;
    }

    public void load()
    {
        loadFriends();
        loadModules();
    }

    public void save()
    {
        saveModules();
        saveFriends();
    }

    private void loadFriends()
    {
        try
        {
            String name = PATH + "Friends" + ".json";
            Path path = Paths.get(name);
            if (!Files.exists(path))
            {
                return;
            }

            InputStream stream = Files.newInputStream(path);

            try
            {
                loadFriends(PARSER.parse(new InputStreamReader(stream)).getAsJsonObject());
            }
            catch (IllegalStateException e)
            {
                e.printStackTrace();
                loadFriends(new JsonObject());
            }

            stream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void loadFriends(JsonObject object)
    {
        for (Map.Entry<String, JsonElement> entry : object.entrySet())
        {
            String name = entry.getKey();
            UUID uuid   = UUID.fromString(entry.getValue().toString());
            FriendManager.getInstance().addFriend(name, uuid);
        }
    }

    private void loadModules()
    {
        for (Module module : ModuleManager.getInstance().getModules())
        {
            try
            {
                String name = PATH + module.getName() + ".json";
                Path path = Paths.get(name);
                if (!Files.exists(path))
                {
                    continue;
                }

                InputStream stream = Files.newInputStream(path);

                try
                {
                    loadObject(PARSER.parse(new InputStreamReader(stream)).getAsJsonObject(), module);
                }
                catch (IllegalStateException e)
                {
                    e.printStackTrace();
                    loadObject(new JsonObject(), module);
                }

                stream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void loadObject(JsonObject object, Module module)
    {
        for (Map.Entry<String, JsonElement> entry : object.entrySet())
        {
            Setting<?> setting = module.getSetting(entry.getKey());
            if (setting != null)
            {
                setting.fromJson(entry.getValue());
            }
        }
    }

    private void saveFriends()
    {
        try
        {
            String name = PATH + "Friends" + ".json";
            Path outputFile = Paths.get(name);

            if (!Files.exists(outputFile))
            {
                Files.createFile(outputFile);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject object = new JsonObject();

            for (Map.Entry<String, UUID> entry : FriendManager.getInstance().getFriendsWithUUID().entrySet())
            {
                object.add(entry.getKey(), PARSER.parse(entry.getValue().toString()));
            }

            String json = gson.toJson(object);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile)));
            writer.write(json);
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void saveModules()
    {
        for (Module module : ModuleManager.getInstance().getModules())
        {
            try
            {
                String name = PATH + module.getName() + ".json";
                Path outputFile = Paths.get(name);

                if (!Files.exists(outputFile))
                {
                    Files.createFile(outputFile);
                }

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject object = new JsonObject();

                for (Setting<?> setting : module.getSettings())
                {
                    object.add(setting.getName(), PARSER.parse(setting.getValue().toString()));
                }

                String json = gson.toJson(object);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(outputFile)));
                writer.write(json);
                writer.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
