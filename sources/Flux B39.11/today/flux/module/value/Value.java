package today.flux.module.value;

import lombok.Getter;

public class Value {
    @Getter
    protected String group;
    @Getter
    protected String key;
    protected Object value;

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
