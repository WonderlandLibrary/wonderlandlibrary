package best.azura.client.impl.config;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.value.Value;
import best.azura.client.impl.Client;
import best.azura.client.impl.module.impl.other.ClientModule;
import best.azura.client.impl.module.impl.other.IRCModule;
import best.azura.client.impl.value.*;
import best.azura.client.util.color.HSBColor;
import best.azura.client.util.other.FileUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.awt.*;
import java.io.File;
import java.io.FileReader;

@SuppressWarnings("unchecked")
public class Config {

    private String creator, creationDate, name;
    private boolean saveBinds, loadBinds, saveVisuals, loadVisuals;

    public Config(String name, String creator, String creationDate, boolean saveBinds, boolean loadBinds, boolean saveVisuals, boolean loadVisuals) {
        this.name = name;
        this.creator = creator;
        this.creationDate = creationDate;
        this.saveBinds = saveBinds;
        this.loadBinds = loadBinds;
        this.saveVisuals = saveVisuals;
        this.loadVisuals = loadVisuals;
    }

    public boolean load(File parent) {
        Gson gson = new Gson();
        File file = new File(parent, name + ".json");
        try {
            JsonObject object = gson.fromJson(new FileReader(file), JsonObject.class);
            load(object, parent);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void value(JsonObject valueObject, Value<?> value) {
        if (value != null) {
            if (value instanceof ColorValue)
                ((ColorValue) value).setObject(HSBColor.fromColor(new Color(valueObject.get("value").getAsInt())));
            else if (value instanceof NumberValue)
                ((NumberValue<Number>) value).setObject(valueObject.get("value").getAsDouble());
            else if (value instanceof ModeValue)
                ((ModeValue) value).setObject(valueObject.get("value").getAsString());
            else if (value instanceof BooleanValue)
                ((BooleanValue) value).setObject(valueObject.get("value").getAsBoolean());
            else if (value instanceof StringValue)
                ((StringValue) value).setObject(valueObject.get("value").getAsString());
            else if (value instanceof ComboValue) {
                JsonObject comboObject = valueObject.get("value").getAsJsonObject();
                for (ComboSelection s : ((ComboValue) value).getObject()) {
                    if (comboObject.has(s.getName()))
                        s.setObject(comboObject.get(s.getName()).getAsBoolean());
                }
            }
        }
    }

    public boolean load(JsonObject object, File parent) {
        if (object.has("modules") && object.get("modules").isJsonArray()) {
            JsonArray modulesArray = object.get("modules").getAsJsonArray();
            for (int i = 0; i < modulesArray.size(); i++) {
                if (!modulesArray.get(i).isJsonObject()) continue;
                JsonObject moduleObject = modulesArray.get(i).getAsJsonObject();
                if (moduleObject.has("name")) {
                    Module module = Client.INSTANCE.getModuleManager().getModule(moduleObject.get("name").getAsString());
                    if (parent == null && (module instanceof ClientModule || module instanceof IRCModule)) continue;
                    if ((module instanceof ClientModule || module instanceof IRCModule) && !parent.equals(Client.INSTANCE.getConfigManager().getClientDirectory())) continue;
                    if (module != null) {
                        if (!loadVisuals && module.getCategory().equals(Category.RENDER)) continue;
                        try {
                            if (moduleObject.has("keyBind") && loadBinds) module.setKeyBind(moduleObject.get("keyBind").getAsInt());
                        } catch (Exception ignored) {}
                        try {
                            if (moduleObject.has("enabled")) module.setEnabled(moduleObject.get("enabled").getAsBoolean());
                        } catch (Exception ignored) {}
                        try {
                            if (moduleObject.has("values") && moduleObject.get("values").isJsonArray()
                                    && Client.INSTANCE.getValueManager().isRegistered(module)
                                    && !Client.INSTANCE.getValueManager().getValues(module).isEmpty()) {
                                JsonArray valuesArray = moduleObject.get("values").getAsJsonArray();
                                try {
                                    for (int i1 = 0; i1 < valuesArray.size(); i1++) {
                                        try {
                                            if (!valuesArray.get(i1).isJsonObject()) continue;
                                            JsonObject valueObject = valuesArray.get(i1).getAsJsonObject();
                                            if (valueObject.has("name") && valueObject.has("value")) {
                                                Value<?> value = Client.INSTANCE.getValueManager().getValue(module, valueObject.get("name").getAsString());
                                                value(valueObject, value);
                                            }
                                        } catch (Exception ignored) {
                                            return false;
                                        }
                                    }
                                } catch (Exception ignored) {
                                    return false;
                                }
                            }
                        } catch (Exception ignored) {
                            return false;
                        }
                    }
                }
            }
            if (object.has("name")) name = object.get("name").getAsString();
            if (object.has("creator")) creator = object.get("creator").getAsString();
            if (object.has("creationDate")) creationDate = object.get("creationDate").getAsString();
        } else {
            return false;
        }
        return true;
    }

    public boolean load() {
        return load(Client.INSTANCE.getConfigManager().getConfigDirectory());
    }

    public JsonObject buildJson() {
        JsonObject object = new JsonObject();
        JsonArray moduleArray = new JsonArray();
        for (Module module : Client.INSTANCE.getModuleManager().getRegistered()) {
            if (!saveVisuals && module.getCategory().equals(Category.RENDER)) continue;
            JsonObject moduleObject = new JsonObject();
            moduleObject.addProperty("name", module.getName());
            if (saveBinds) moduleObject.addProperty("keyBind", module.getKeyBind());
            moduleObject.addProperty("enabled", module.isEnabled());
            JsonArray valuesArray = new JsonArray();
            if (Client.INSTANCE.getValueManager().isRegistered(module) && !Client.INSTANCE.getValueManager().getValues(module).isEmpty()) {
                for (Value<?> v : Client.INSTANCE.getValueManager().getValues(module)) {
                    if (v instanceof CategoryValue) continue;
                    JsonObject valueObject = new JsonObject();
                    valueObject.addProperty("name", v.getName());
                    if (v instanceof ColorValue)
                        valueObject.addProperty("value", ((ColorValue)v).getObject().getRGB());
                    else if (v instanceof NumberValue)
                        valueObject.addProperty("value", ((NumberValue<Number>) v).getObject().doubleValue());
                    else if (v instanceof ModeValue)
                        valueObject.addProperty("value", ((ModeValue) v).getObject());
                    else if (v instanceof BooleanValue)
                        valueObject.addProperty("value", ((BooleanValue) v).getObject());
                    else if (v instanceof StringValue)
                        valueObject.addProperty("value", ((StringValue) v).getObject());
                    else if (v instanceof ComboValue) {
                        JsonObject comboObject = new JsonObject();
                        for (ComboSelection s : ((ComboValue) v).getObject()) comboObject.addProperty(s.getName(), s.getObject());
                        valueObject.add("value", comboObject);
                    }
                    valuesArray.add(valueObject);
                }
                moduleObject.add("values", valuesArray);
            }
            moduleArray.add(moduleObject);
        }
        object.add("modules", moduleArray);
        object.addProperty("name", name);
        object.addProperty("creator", creator);
        object.addProperty("creationDate", creationDate);
        object.addProperty("creationVersion", Client.VERSION + "-" + Client.RELEASE);
        return object;
    }

    public boolean save() {
        return save(Client.INSTANCE.getConfigManager().getConfigDirectory());
    }

    public boolean save(File parent) {
        return FileUtil.writeContentToFile(new File(parent, name + ".json"), new GsonBuilder().setPrettyPrinting().create().toJson(buildJson()), true);
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getCreator() {
        return creator;
    }

    public String getName() {
        return name;
    }

    public void setLoadVisuals(boolean loadVisuals) {
        this.loadVisuals = loadVisuals;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLoadBinds(boolean loadBinds) {
        this.loadBinds = loadBinds;
    }

    public void setSaveBinds(boolean saveBinds) {
        this.saveBinds = saveBinds;
    }

    public void setSaveVisuals(boolean saveVisuals) {
        this.saveVisuals = saveVisuals;
    }
}