package de.tired.base.guis.newaltmanager.save;

import com.google.gson.*;
import de.tired.base.guis.newaltmanager.storage.AltData;
import de.tired.base.guis.newaltmanager.storage.AltStorage;
import de.tired.base.interfaces.IHook;

import java.io.*;
import java.util.Map;

public class AltFile {

    public static void save() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File fDir = new File(IHook.MC.mcDataDir, "Tired-NextGen");
        File f = new File(fDir, fDir + "AltData.json");
        final FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        gson.toJson(saveJson(), fileWriter);
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonObject saveJson() {
        final JsonObject jsonObject = new JsonObject();
        AltStorage.alts.forEach(altData -> {
            JsonObject nameJson = new JsonObject();
            nameJson.addProperty("AltName", altData.getName());
            final JsonObject emailPassJson = new JsonObject();
            emailPassJson.addProperty("Email", altData.getEmailAddress());
            emailPassJson.addProperty("Password", altData.getPassword());
            emailPassJson.addProperty("UUID", altData.getUuid());
            nameJson.add("Data", emailPassJson);
            jsonObject.add(altData.getName(), nameJson);
        });
        return jsonObject;
    }

    public static void loadAlts() {
        File fDir = new File(IHook.MC.mcDataDir, "Tired-NextGen");
        File f = new File(fDir, fDir + "AltData.json");

        if (f.exists()) {
            try {
                if (load(new JsonParser().parse(new FileReader(f)))) {
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private static boolean load(final JsonElement jsonElement) {
        try {
            if (jsonElement instanceof JsonNull)
                return false;


            for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                final JsonObject jsonModule = (JsonObject) entry.getValue();
                if (jsonModule.has("Data")) {
                    String email = jsonModule.get("Data").getAsJsonObject().get("Email").getAsString();
                    String password = jsonModule.get("Data").getAsJsonObject().get("Password").getAsString();
                    String uuid = jsonModule.get("Data").getAsJsonObject().get("UUID").getAsString();
                    String user = entry.getKey();
                    AltStorage.alts.add(new AltData(user, email, password, uuid));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }


}
