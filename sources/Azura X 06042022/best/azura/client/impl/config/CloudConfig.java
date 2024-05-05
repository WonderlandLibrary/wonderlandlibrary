package best.azura.client.impl.config;

import best.azura.client.impl.Client;
import best.azura.client.util.other.FileUtil;
import com.google.gson.*;

import java.io.File;

public class CloudConfig {
    private final String name, id, version, lastUpdate, creatorId, creatorName, creatorRank, note;
    private final boolean killaura, velocity, speed, fly, scaffold, noslow, nofall;
    private boolean favourite;
    private final JsonObject jsonObject;

    public CloudConfig(String name, String id, String version, String lastUpdate, String creatorId, String creatorName, String creatorRank, JsonObject infoObject, JsonObject jsonObject) {
        this.name = name;
        this.id = id;
        this.version = version;
        this.lastUpdate = lastUpdate;
        this.creatorId = creatorId;
        this.jsonObject = jsonObject;
        this.creatorName = creatorName;
        this.creatorRank = creatorRank;

        if (infoObject != null) {
            if (infoObject.has("killaura") &&
                    infoObject.has("velocity") &&
                    infoObject.has("speed") &&
                    infoObject.has("fly") &&
                    infoObject.has("scaffold") &&
                    infoObject.has("noslow") &&
                    infoObject.has("nofall") &&
                    infoObject.has("note") &&
                    infoObject.get("killaura").isJsonPrimitive() &&
                    infoObject.get("velocity").isJsonPrimitive() &&
                    infoObject.get("speed").isJsonPrimitive() &&
                    infoObject.get("fly").isJsonPrimitive() &&
                    infoObject.get("scaffold").isJsonPrimitive() &&
                    infoObject.get("noslow").isJsonPrimitive() &&
                    infoObject.get("nofall").isJsonPrimitive() &&
                    infoObject.get("note").isJsonPrimitive()) {
                JsonPrimitive killaura = infoObject.getAsJsonPrimitive("killaura"),
                        velocity = infoObject.getAsJsonPrimitive("velocity"),
                        speed = infoObject.getAsJsonPrimitive("speed"),
                        fly = infoObject.getAsJsonPrimitive("fly"),
                        scaffold = infoObject.getAsJsonPrimitive("scaffold"),
                        noslow = infoObject.getAsJsonPrimitive("noslow"),
                        nofall = infoObject.getAsJsonPrimitive("nofall"),
                        note = infoObject.getAsJsonPrimitive("note");

                if (killaura.isBoolean() && velocity.isBoolean() && speed.isBoolean() && fly.isBoolean() && scaffold.isBoolean() &&
                        noslow.isBoolean() && nofall.isBoolean() && note.isString()) {
                    this.killaura = killaura.getAsBoolean();
                    this.velocity = velocity.getAsBoolean();
                    this.speed = speed.getAsBoolean();
                    this.fly = fly.getAsBoolean();
                    this.scaffold = scaffold.getAsBoolean();
                    this.noslow = noslow.getAsBoolean();
                    this.nofall = nofall.getAsBoolean();
                    this.note = note.getAsString();
                } else {
                    this.killaura = false;
                    this.velocity = false;
                    this.speed = false;
                    this.fly = false;
                    this.scaffold = false;
                    this.noslow = false;
                    this.nofall = false;
                    this.note = "";
                }
            } else {
                this.killaura = false;
                this.velocity = false;
                this.speed = false;
                this.fly = false;
                this.scaffold = false;
                this.noslow = false;
                this.nofall = false;
                this.note = "";
            }
        } else {
            this.killaura = false;
            this.velocity = false;
            this.speed = false;
            this.fly = false;
            this.scaffold = false;
            this.noslow = false;
            this.nofall = false;
            this.note = "";
        }
    }

    public void setFavourite(boolean favourite, boolean save) {
        if (save) {
            File settingsFile = new File(Client.INSTANCE.getConfigManager().getClientDirectory() + "/data", "settings.json");

            if (settingsFile.exists()) {

                JsonObject settingsObject = new JsonParser().parse(FileUtil.getContentFromFileAsString(settingsFile)).getAsJsonObject();

                if (settingsObject.has("favouriteConfigs")) {
                    if (settingsObject.get("favouriteConfigs").isJsonArray()) {
                        JsonArray jsonArray = settingsObject.getAsJsonArray("favouriteConfigs");

                        if (!favourite) {
                            JsonArray newJsonArray = new JsonArray();

                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                                if (jsonObject.has("id") && jsonObject.get("id").getAsInt() != Integer.parseInt(getId())) {
                                    JsonObject jsonObject1 = new JsonObject();
                                    jsonObject1.addProperty("id", jsonObject.get("id").getAsInt());
                                    newJsonArray.add(jsonObject1);
                                }
                            }

                            settingsObject.add("favouriteConfigs", newJsonArray);
                            FileUtil.writeContentToFile(settingsFile, new GsonBuilder().setPrettyPrinting().create().toJson(settingsObject), true);
                        } else {
                            boolean exists = false;

                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                                if (jsonObject.has("id") && jsonObject.get("id").getAsInt() == Integer.parseInt(getId())) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (!exists) {
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty("id", getId());
                                jsonArray.add(jsonObject);

                                settingsObject.add("favouriteConfigs", jsonArray);

                                FileUtil.writeContentToFile(settingsFile, new GsonBuilder().setPrettyPrinting().create().toJson(settingsObject), true);
                            }
                        }
                    }
                } else {
                    JsonArray jsonArray = new JsonArray();

                    if (favourite) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("id", getId());
                        jsonArray.add(jsonObject);
                    }

                    settingsObject.add("favouriteConfigs", jsonArray);
                }

                FileUtil.writeContentToFile(settingsFile, new GsonBuilder().setPrettyPrinting().create().toJson(settingsObject), true);
            } else {
                JsonObject settingsObject = new JsonObject();

                settingsObject.add("favouriteConfigs", new JsonArray());

                FileUtil.writeContentToFile(settingsFile, new GsonBuilder().setPrettyPrinting().create().toJson(settingsObject), true);
            }
        }
        this.favourite = favourite;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getVersion() {
        return version;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatorRank() {
        return creatorRank;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public String getNote() {
        return note;
    }

    public boolean isKillaura() {
        return killaura;
    }

    public boolean isVelocity() {
        return velocity;
    }

    public boolean isSpeed() {
        return speed;
    }

    public boolean isFly() {
        return fly;
    }

    public boolean isScaffold() {
        return scaffold;
    }

    public boolean isNoslow() {
        return noslow;
    }

    public boolean isNofall() {
        return nofall;
    }

    public boolean isFavourite() {
        return favourite;
    }
}