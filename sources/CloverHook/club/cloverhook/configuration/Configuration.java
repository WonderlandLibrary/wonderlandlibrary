package club.cloverhook.configuration;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author antja03
 */
public class Configuration {

    //The name of the configuration
    private String identifier;

    //The configuration file
    private final File configurationFile;

    //A list of cheat data
    private final ArrayList<CheatData> data;

    //Whether or not the config should be automatically loaded on startup
    private boolean isDefault;

    public Configuration(File configurationFile, boolean refresh) {
        this.identifier = "";
        this.configurationFile = configurationFile;
        this.data = new ArrayList<>();
        this.isDefault = false;
        loadFile(refresh);
    }

    public Configuration(String identifier) {
        this.identifier = identifier;
        this.configurationFile = new File(ConfigurationManager.CONFIGURATION_DIR + File.separator + identifier + ".conf");
        this.data = new ArrayList<>();
        this.isDefault = false;
        save();
    }

    /**
     * Reads the current settings and saves them into CheatData objects
     * which can later be loaded by calling load()
     */
    public void save() {
        Cloverhook.instance.cheatManager.getCheatRegistry().values()
                .forEach(cheat -> data.add(new CheatData(cheat)));
        saveFile();
    }

    /**s
     * Writes the configuration data to the actual cheat objects
     * and overwrites previous settings
     */
    public void load() {
        data.forEach(data -> {
            if (Cloverhook.instance.cheatManager.getCheatRegistry().containsKey(data.getName())) {
                Cheat cheat = Cloverhook.instance.cheatManager.getCheatRegistry().get(data.getName());
                cheat.setState(data.isState(), false);
                cheat.setBind(data.getBind());
                cheat.getPropertyRegistry().values().forEach(property -> {
                    if (data.getValueData().containsKey(property.getId())) {
                        property.setValue(data.getValueData().get(property.getId()));
                    }
                });
            }
        });
    }

    public void saveFile() {
        try {
            if (!configurationFile.exists()) {
                configurationFile.createNewFile();
            }

            JsonObject configurationObject = new JsonObject();
            configurationObject.addProperty("default", isDefault);
            for (CheatData cheatData : data) {
                JsonObject cheatObject = new JsonObject();
                JsonObject valueObject = new JsonObject();
                cheatData.getValueData().forEach(valueObject::addProperty);
                cheatObject.addProperty("state", cheatData.isState());
                cheatObject.addProperty("bind", cheatData.getBind());
                cheatObject.add("values", valueObject);
                configurationObject.add(cheatData.getName(), cheatObject);
            }

            String jsonAsText = new GsonBuilder().create().toJson(configurationObject);

            FileWriter fileWriter = new FileWriter(configurationFile);
            fileWriter.write(jsonAsText);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!configurationFile.exists()) {
            try {
                configurationFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void loadFile(boolean refresh) {
        if (!configurationFile.getName().endsWith(".conf"))
            return;

        this.identifier = configurationFile.getName().replace(".conf", "");

        try {
            String text = FileUtils.readFileToString(configurationFile);

            if (text.isEmpty())
                return;

            JsonObject configurationObject = new GsonBuilder().create().fromJson(text, JsonObject.class);

            if (configurationObject == null)
                return;

            if (configurationObject.has("default")
                    && configurationObject.get("default").getAsBoolean())
                this.isDefault = true;

            for(Map.Entry<String, JsonElement> entry : configurationObject.entrySet()) {
                if (entry.getValue() instanceof JsonObject) {
                    JsonObject cheatObejct = (JsonObject) entry.getValue();

                    HashMap<String, String> valueData = new HashMap<>();
                    if (cheatObejct.has("values"))
                        cheatObejct.getAsJsonObject("values").entrySet()
                                .forEach(valueEntry -> valueData.put(valueEntry.getKey(), valueEntry.getValue().getAsString()));

                    data.add(new CheatData(entry.getKey(),
                            cheatObejct.has("state") && cheatObejct.get("state").getAsBoolean(),
                            cheatObejct.has("bind") ? cheatObejct.get("bind").getAsInt() : 0,
                            valueData));
                }
            }

            if (!refresh && this.isDefault)
                load();
        } catch (IOException | JsonSyntaxException e) {
           //Silently catch
        }
    }

    public void delete() {
        if (configurationFile.exists())
            configurationFile.delete();
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
