package xyz.northclient.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import xyz.northclient.NorthSingleton;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Value;
import xyz.northclient.features.values.BoolValue;
import xyz.northclient.features.values.DoubleValue;
import xyz.northclient.features.values.ModeValue;

import java.util.Map;

public class Config {
    public static void load(String m) {
        try {
            JsonObject js = new Gson().fromJson(m, JsonObject.class);

            for(AbstractModule me : NorthSingleton.INSTANCE.getModules().getModules()) {
                if(me.isEnabled()){
                    me.toggle();
                }
            }

            for(Map.Entry<String, JsonElement> s : js.entrySet()) {
                JsonObject modObj = js.getAsJsonObject(s.getKey());
                AbstractModule mod = NorthSingleton.INSTANCE.getModules().get(s.getKey());

                mod.setKeyCode(modObj.get("keybind").getAsInt());
                if(modObj.get("enabled").getAsBoolean()) {
                    mod.toggle();
                }

                for(JsonElement el : modObj.getAsJsonArray("settings")) {
                    JsonObject oba = (JsonObject) el;
                    Value v = null;
                    for(Value va : mod.getValues()) {
                        if(va.getName().equalsIgnoreCase(oba.get("name").getAsString())) {
                            v = va;
                        }
                    }

                    if(v != null) {
                        switch(oba.get("type").getAsString()) {
                            case "bool":{
                                BoolValue v1 = (BoolValue) v;
                                v1.set(oba.get("value").getAsBoolean());
                                break;
                            }
                            case "double": {
                                DoubleValue v1 = (DoubleValue) v;
                                v1.set(oba.get("value").getAsNumber());
                                break;
                            }
                            case "mode": {
                                ModeValue v1 = (ModeValue) v;
                                v1.setDefault(oba.get("value").getAsString());
                                break;
                            }
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String save() {
        JsonObject js = new JsonObject();

        for(AbstractModule m : NorthSingleton.INSTANCE.getModules().getModules()) {
            JsonObject moduleObj = new JsonObject();
            moduleObj.addProperty("keybind",m.getKeyCode());
            moduleObj.addProperty("enabled",m.isEnabled());

            JsonArray arr = new JsonArray();

            for(Value v : m.getValues()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("name",v.getName());
                if(v instanceof BoolValue) {
                    obj.addProperty("type","bool");
                    obj.addProperty("value",((BoolValue) v).get());
                }

                if(v instanceof DoubleValue) {
                    obj.addProperty("type","double");
                    obj.addProperty("value",((DoubleValue) v).get());
                }

                if(v instanceof ModeValue) {
                    obj.addProperty("type","mode");
                    obj.addProperty("value", ((ModeValue) v).getOptions().get(((ModeValue) v).getSelected()).getName());
                }

                arr.add(obj);
            }

            moduleObj.add("settings",arr);

            js.add(m.getName(),moduleObj);
        }

        return new Gson().toJson(js);
    }
}
