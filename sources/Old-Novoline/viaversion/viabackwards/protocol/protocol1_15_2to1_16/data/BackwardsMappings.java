package viaversion.viabackwards.protocol.protocol1_15_2to1_16.data;

import com.google.gson.JsonObject;
import viaversion.viaversion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;

import java.util.HashMap;
import java.util.Map;

public class BackwardsMappings extends viaversion.viabackwards.api.data.BackwardsMappings {
    private final Map<String, String> attributeMappings = new HashMap<>();

    public BackwardsMappings() {
        super("1.16", "1.15", Protocol1_16To1_15_2.class, true);
    }

    @Override
    protected void loadVBExtras(JsonObject oldMappings, JsonObject newMappings) {
        for (Map.Entry<String, String> entry : Protocol1_16To1_15_2.MAPPINGS.getAttributeMappings().entrySet()) {
            attributeMappings.put(entry.getValue(), entry.getKey());
        }
    }

    public Map<String, String> getAttributeMappings() {
        return attributeMappings;
    }
}