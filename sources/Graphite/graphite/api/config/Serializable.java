package graphite.api.config;

import com.google.gson.JsonObject;

public interface Serializable {
    void load(final JsonObject object);

    void save(final JsonObject object);
}