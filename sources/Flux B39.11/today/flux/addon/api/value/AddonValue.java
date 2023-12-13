package today.flux.addon.api.value;

import com.soterdev.SoterObfuscator;
import lombok.Getter;
import today.flux.addon.FluxAPI;
import today.flux.addon.api.exception.NoSuchValueException;
import today.flux.addon.api.exception.WrongValueTypeException;
import today.flux.module.value.*;

public class AddonValue {
    @Getter
    public ValueType valueType;

    // Native Value Access
    public AddonValue(String moduleName, String valueName) throws NoSuchValueException {
        for (Value value : ValueManager.getValueByModName(moduleName)) {
            if (value.getKey().equalsIgnoreCase(valueName)) {
                if (value instanceof ModeValue) {
                    valueType = ValueType.Mode;
                    modeValue = (ModeValue) value;
                } else if (value instanceof FloatValue) {
                    valueType = ValueType.Float;
                    floatValue = (FloatValue) value;
                } else if (value instanceof BooleanValue) {
                    valueType = ValueType.Boolean;
                    booleanValue = (BooleanValue) value;
                }
                break;
            }
        }

        if (valueType == null)
            throw new NoSuchValueException(String.format("Can not find value in %s called %s!", moduleName, valueName));
    }

    public AddonValue(Value value) {
        if (value instanceof ModeValue) {
            valueType = ValueType.Mode;
            modeValue = (ModeValue) value;
        } else if (value instanceof FloatValue) {
            valueType = ValueType.Float;
            floatValue = (FloatValue) value;
        } else if (value instanceof BooleanValue) {
            valueType = ValueType.Boolean;
            booleanValue = (BooleanValue) value;
        }
    }

    // Create Boolean Value
    private BooleanValue booleanValue;

    public AddonValue(String moduleName, String valueName, Boolean defaultStage) {
        this.booleanValue = new BooleanValue(moduleName, valueName, defaultStage, true);
        this.valueType = ValueType.Boolean;
        FluxAPI.FLUX_API.getValueManager().registerValue(this);
    }

    
    public Boolean getBooleanValue() throws WrongValueTypeException {
        if (this.valueType != ValueType.Boolean)
            throw new WrongValueTypeException(String.format("Trying to get %s value by Boolean!", valueType.toString()));
        return booleanValue.getValue();
    }

    
    public void setValue(Boolean stage) {
        this.booleanValue.setValue(stage);
    }

    // Create Float Value
    private FloatValue floatValue;

    public AddonValue(String moduleName, String valueName, Float defaultValue, Float minValue, Float maxValue, Float step) {
        this.floatValue = new FloatValue(moduleName, valueName, defaultValue, minValue, maxValue, step, true);
        this.valueType = ValueType.Float;
        FluxAPI.FLUX_API.getValueManager().registerValue(this);
    }

    
    public Float getFloatValue() throws WrongValueTypeException {
        if (this.valueType != ValueType.Float)
            throw new WrongValueTypeException(String.format("Trying to get %s value by Float!", valueType.toString()));
        return floatValue.getValue();
    }

    
    public void setValue(Float stage) {
        this.floatValue.setValue(stage);
    }

    // Create Mode Value
    private ModeValue modeValue;

    public AddonValue(String moduleName, String valueName, String defaultValue, String... allValues) {
        this.modeValue = new ModeValue(moduleName, valueName, defaultValue, true, allValues);
        this.valueType = ValueType.Mode;
        FluxAPI.FLUX_API.getValueManager().registerValue(this);
    }

    
    public String getModeValue() throws WrongValueTypeException {
        if (this.valueType != ValueType.Mode)
            throw new WrongValueTypeException(String.format("Trying to get %s value by Mode!", valueType.toString()));
        return modeValue.getValue();
    }

    
    public Boolean isCurrentMode(String mode) throws WrongValueTypeException {
        if (this.valueType != ValueType.Mode)
            throw new WrongValueTypeException(String.format("Trying to get %s value by Mode!", valueType.toString()));
        return modeValue.isCurrentMode(mode);
    }

    
    public void setValue(String mode) {
        this.modeValue.setValue(mode);
    }

    
    public Value getValue() {
        if (valueType == ValueType.Boolean)
            return booleanValue;
        else if (valueType == ValueType.Float)
            return floatValue;
        else
            return modeValue;
    }

    enum ValueType {
        Boolean, Float, Mode
    }
}
