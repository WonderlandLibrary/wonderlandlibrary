package ru.smertnix.celestial.ui.config;

import com.google.gson.JsonObject;

public interface ConfigUpdater {

    JsonObject save();

    void load(JsonObject object);

}