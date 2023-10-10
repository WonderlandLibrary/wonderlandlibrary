package client.util.file.config;

import client.Client;
import client.module.Module;
import client.module.impl.render.ClickGUI;
import client.value.Value;
import client.value.impl.*;
import com.google.gson.JsonObject;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigFile extends client.util.file.File {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd.MM.yyyy");

    private boolean loadKeyCodes;

    public ConfigFile(final java.io.File file) {
        super(file);
    }

    @Override
    public boolean read() {
        if (!this.getFile().exists()) return false;
        try {
            final FileReader fileReader = new FileReader(getFile());
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            final JsonObject jsonObject = GSON.fromJson(bufferedReader, JsonObject.class);
            bufferedReader.close();
            fileReader.close();
            if (jsonObject == null) return false;
            for (final Module module : Client.INSTANCE.getModuleManager().getAll()) {
                if (!jsonObject.has(module.getInfo().name())) continue;
                module.setEnabled(false);
                final JsonObject moduleJsonObject = jsonObject.getAsJsonObject(module.getInfo().name());
                int index = 0;
                for (final Value<?> value : module.getAllValues()) {
                    index++;
                    if (!moduleJsonObject.has(value.getName() + "*" + index)) continue;
                    final JsonObject valueJsonObject = moduleJsonObject.getAsJsonObject(value.getName() + "*" + index);
                    try {
                        if (value instanceof ModeValue) {
                            final ModeValue enumValue = (ModeValue) value;
                            if (valueJsonObject.has("value")) enumValue.setDefault(valueJsonObject.get("value").getAsString());
                        } else if (value instanceof BooleanValue) {
                            final BooleanValue booleanValue = (BooleanValue) value;
                            if (valueJsonObject.has("value")) booleanValue.setValue(valueJsonObject.get("value").getAsBoolean());
                        } else if (value instanceof StringValue) {
                            final StringValue stringValue = (StringValue) value;
                            if (valueJsonObject.has("value")) {
                                String load = valueJsonObject.get("value").getAsString();
                                load = load.replace("<percentsign>", "%");
                                stringValue.setValue(load);
                            }
                        } else if (value instanceof NumberValue) {
                            final NumberValue numberValue = (NumberValue) value;
                            if (valueJsonObject.has("value")) numberValue.setValue(valueJsonObject.get("value").getAsDouble());
                        } else if (value instanceof BoundsNumberValue) {
                            final BoundsNumberValue boundsNumberValue = (BoundsNumberValue) value;
                            if (valueJsonObject.has("first")) boundsNumberValue.setValue(valueJsonObject.get("first").getAsDouble());
                            if (valueJsonObject.has("second")) boundsNumberValue.setSecondValue(valueJsonObject.get("second").getAsDouble());
                        } else if (value instanceof ColorValue) {
                            final ColorValue colorValue = (ColorValue) value;
                            int red = 0, green = 0, blue = 0, alpha = 0;
                            if (valueJsonObject.has("red")) red = valueJsonObject.get("red").getAsInt();
                            if (valueJsonObject.has("green")) green = valueJsonObject.get("green").getAsInt();
                            if (valueJsonObject.has("blue")) blue = valueJsonObject.get("blue").getAsInt();
                            if (valueJsonObject.has("alpha")) alpha = valueJsonObject.get("alpha").getAsInt();
                            colorValue.setValue(new Color(red, green, blue, alpha));
                        } else if (value instanceof ListValue) {
                            final ListValue<?> enumValue = (ListValue<?>) value;
                            for (Object mode : enumValue.getModes()) {
                                if (mode.toString().equals(valueJsonObject.get("value").getAsString())) enumValue.setValueAsObject(mode);
                            }
                        }
                    } catch (final Exception e) {
                        if (Client.DEVELOPMENT_SWITCH) e.printStackTrace();
                    }
                }
                try {
                    if (moduleJsonObject.has("state")) {
                        final boolean state = moduleJsonObject.get("state").getAsBoolean();
                        module.setEnabled(state);
                    }
                } catch (Exception e) {
                    if (Client.DEVELOPMENT_SWITCH) e.printStackTrace();
                }
                if (!loadKeyCodes) continue;
                if (moduleJsonObject.has("keyCode")) {
                    module.setKeyCode(moduleJsonObject.get("keyCode").getAsInt());
                }
            }
        } catch (final IOException ignored) {
            return false;
        }
        return true;
    }

    @Override
    public boolean write() {
        try {
            this.getFile().createNewFile();
            final JsonObject jsonObject = new JsonObject();
            final JsonObject metadataJsonObject = new JsonObject();
            metadataJsonObject.addProperty("version", Client.VERSION);
            metadataJsonObject.addProperty("creationDate", DATE_FORMATTER.format(new Date()));
            jsonObject.add("Metadata", metadataJsonObject);
            for (final Module module : Client.INSTANCE.getModuleManager().getAll()) {
                final JsonObject moduleJsonObject = new JsonObject();
                if (!(module instanceof ClickGUI)) moduleJsonObject.addProperty("state", module.isEnabled());
                moduleJsonObject.addProperty("keyCode", module.getKeyCode());
                int index = 0;
                for (final Value<?> value : module.getAllValues()) {
                    index++;
                    final JsonObject valueJsonObject = new JsonObject();
                    if (value instanceof ModeValue) {
                        final ModeValue enumValue = (ModeValue) value;
                        valueJsonObject.addProperty("value", enumValue.getValue().getName());
                    } else if (value instanceof BooleanValue) {
                        final BooleanValue booleanValue = (BooleanValue) value;
                        valueJsonObject.addProperty("value", booleanValue.getValue());
                    } else if (value instanceof NumberValue) {
                        final NumberValue numberValue = (NumberValue) value;
                        valueJsonObject.addProperty("value", numberValue.getValue().doubleValue());
                    } else if (value instanceof StringValue) {
                        final StringValue stringValue = (StringValue) value;
                        String save = stringValue.getValue();
                        save = save.replace("%", "<percentsign>");
                        valueJsonObject.addProperty("value", save);
                    } else if (value instanceof BoundsNumberValue) {
                        final BoundsNumberValue boundsNumberValue = (BoundsNumberValue) value;
                        valueJsonObject.addProperty("first", boundsNumberValue.getValue().doubleValue());
                        valueJsonObject.addProperty("second", boundsNumberValue.getSecondValue().doubleValue());
                    } else if (value instanceof ColorValue) {
                        final ColorValue colorValue = (ColorValue) value;
                        valueJsonObject.addProperty("red", colorValue.getValue().getRed());
                        valueJsonObject.addProperty("green", colorValue.getValue().getGreen());
                        valueJsonObject.addProperty("blue", colorValue.getValue().getBlue());
                        valueJsonObject.addProperty("alpha", colorValue.getValue().getAlpha());
                    } else if (value instanceof ListValue) {
                        valueJsonObject.addProperty("value", ((ListValue<?>) value).getValue().toString());
                    }
                    moduleJsonObject.add(value.getName() + "*" + index, valueJsonObject);
                }
                jsonObject.add(module.getInfo().name(), moduleJsonObject);
            }
            final FileWriter fileWriter = new FileWriter(getFile());
            final BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            GSON.toJson(jsonObject, bufferedWriter);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            if (Client.DEVELOPMENT_SWITCH) e.printStackTrace();
            return false;
        }
        return true;
    }

    public void allowKeyCodeLoading() {
        loadKeyCodes = true;
    }
}