package info.sigmaclient.sigma.config.values;

public class NumberValue extends Value<Number> {
    public enum NUMBER_TYPE {
        FLOAT, INT, LOW_FLOAT
    }
    public Number min, max;
    public NUMBER_TYPE inc;
    public NumberValue(String name, Number value, Number min, Number max, NUMBER_TYPE inc){
        super(name);
        this.min = Math.min(min.floatValue(), max.floatValue());
        this.max = Math.max(min.floatValue(), max.floatValue());
        this.inc = inc;
        this.pureSetValue(value);
    }

    public void pureSetValue(Number value) {
        super.setValue(value);
    }
    @Override
    public void setValue(Number value) {
        super.setValue(value);
        onSetValue();
    }

    public void onSetValue(){

    }
}
