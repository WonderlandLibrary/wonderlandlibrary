package today.flux.addon.api.value;

import com.soterdev.SoterObfuscator;
import today.flux.module.value.Value;
import today.flux.module.value.ValueManager;

import java.util.ArrayList;
import java.util.List;

public class AddonValueManager {
    private List<AddonValue> allValues;
    public AddonValueManager() {
        ValueManager.apiValues.clear();
    }

    
    public void registerValue(AddonValue value) {
        ValueManager.apiValues.add(value.getValue());
    }

    
    public List<AddonValue> getAllValues() {
        if (allValues == null) {
            List<AddonValue> values = new ArrayList<>();
            for (Value value : ValueManager.getValues()) {
                values.add(new AddonValue(value));
            }
            allValues = values;
        }
        return allValues;
    }
}
