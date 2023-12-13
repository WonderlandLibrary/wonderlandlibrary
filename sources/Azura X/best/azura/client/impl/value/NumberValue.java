package best.azura.client.impl.value;

import best.azura.client.api.value.Value;
import best.azura.client.api.value.ValueChangeCallback;
import best.azura.client.api.value.dependency.Dependency;

@SuppressWarnings("unchecked")
public class NumberValue<T extends Number> extends Value<T> {
	
	private final T step;

    public NumberValue(String name, String description, T object, T step, T min, T max) {
        super(name, description, object, min, max);
        this.step = step;
    }
    public NumberValue(String name, String description, Dependency dependency, T object, T step, T min, T max) {
        super(name, description, dependency, object, min, max);
        this.step = step;
    }

    public NumberValue(String name, String description, ValueChangeCallback callback, T object, T min, T max) {
        super(name, description, callback, object, min, max);
        this.step = null;
    }

    public NumberValue(String name, String description, ValueChangeCallback callback, T object, T step, T min, T max) {
        super(name, description, callback, object, min, max);
        this.step = step;
    }
    public NumberValue(String name, String description, ValueChangeCallback callback, Dependency dependency, T object, T step, T min, T max) {
        super(name, description, callback, dependency, object, min, max);
        this.step = step;
    }

    public NumberValue(String name, String description, T object, T min, T max) {
        super(name, description, object, min, max);
        this.step = null;
    }

    public NumberValue(String name, String description, Dependency dependency, T object, T min, T max) {
        super(name, description, dependency, object, min, max);
        this.step = null;
    }

    public T getStep() {
        return step;
    }

    public T getMin() {
		return getObjects()[0];
	}
	
	public T getMax() {
		return getObjects()[1];
	}

    public void setMin(T min) {
        getObjects()[0] = min;
    }

    public void setMax(T max) {
        getObjects()[1] = max;
    }
	
	@Override
	public void setObject(T object) {
        if (this.getObject() instanceof Integer) super.setObject((T) Integer.valueOf(object.intValue()));
        else if (this.getObject() instanceof Float) super.setObject((T) Float.valueOf(object.floatValue()));
        else if (this.getObject() instanceof Long) super.setObject((T) Long.valueOf(object.longValue()));
        else if (this.getObject() instanceof Byte) super.setObject((T) Byte.valueOf(object.byteValue()));
        else if (this.getObject() instanceof Short) super.setObject((T) Short.valueOf(object.shortValue()));
        else if (this.getObject() instanceof Double) super.setObject((T) Double.valueOf(object.doubleValue()));
        if(step != null) {
            if (this.getObject() instanceof Integer) {
                if (object.intValue() % step.intValue() != 0) {
                    int remainder = object.intValue() % step.intValue();
                    int s = remainder > 0 ? step.intValue() : -step.intValue();
                    super.setObject((T) Integer.valueOf(object.intValue() - (remainder) + s));
                }
            } else if (this.getObject() instanceof Float) {
                if (this.getObject().floatValue() % step.floatValue() != 0) {
                    float remainder = object.floatValue() % step.floatValue();
                    float s = remainder > 0 ? step.floatValue() : -step.floatValue();
                    super.setObject((T) Float.valueOf(object.floatValue() - (remainder) + s));
                }
            } else if (this.getObject() instanceof Long) {
                if (this.getObject().longValue() % step.longValue() != 0) {
                    long remainder = object.longValue() % step.longValue();
                    long s = remainder > 0 ? step.longValue() : -step.longValue();
                    super.setObject((T) Long.valueOf(object.longValue() - (remainder) + s));
                }
            } else if (this.getObject() instanceof Byte) {
                if (this.getObject().byteValue() % step.byteValue() != 0) {
                    byte remainder = (byte) (object.byteValue() % step.byteValue());
                    byte s = remainder > 0 ? step.byteValue() : (byte) -step.byteValue();
                    super.setObject((T) Byte.valueOf((byte) (object.byteValue() - (remainder) + s)));
                }
            } else if (this.getObject() instanceof Short) {
                if (this.getObject().shortValue() % step.shortValue() != 0) {
                    short remainder = (short) (object.shortValue() % step.shortValue());
                    short s = remainder > 0 ? step.shortValue() : (short) -step.shortValue();
                    super.setObject((T) Short.valueOf((short) (object.shortValue() - (remainder) + s)));
                }
            } else if (this.getObject() instanceof Double) {
                if (this.getObject().doubleValue() % step.doubleValue() != 0) {
                    double remainder = object.doubleValue() % step.doubleValue();
                    double s = remainder > 0 ? step.doubleValue() : -step.doubleValue();
                    super.setObject((T) Double.valueOf(object.doubleValue() - (remainder) + s));
                }
            }
        }
		if(getObject() instanceof Integer) {
            int curr = getObject().intValue();
            int min = getMin().intValue();
            int max = getMax().intValue();
            if(curr < min) {
                super.setObject(getMin());
            } else if(curr > max) {
                super.setObject(getMax());
            }
        } else if(getObject() instanceof Float) {
            float curr = getObject().floatValue();
            float min = getMin().floatValue();
            float max = getMax().floatValue();
            if(curr < min) {
                super.setObject(getMin());
            } else if(curr > max) {
                super.setObject(getMax());
            }
        } else if(getObject() instanceof Long) {
            long curr = getObject().longValue();
            long min = getMin().longValue();
            long max = getMax().longValue();
            if(curr < min) {
                super.setObject(getMin());
            } else if(curr > max) {
                super.setObject(getMax());
            }
        } else if(getObject() instanceof Short) {
            short curr = getObject().shortValue();
            short min = getMin().shortValue();
            short max = getMax().shortValue();
            if(curr < min) {
                super.setObject(getMin());
            } else if(curr > max) {
                super.setObject(getMax());
            }
        } else if(getObject() instanceof Double) {
            double curr = getObject().doubleValue();
            double min = getMin().doubleValue();
            double max = getMax().doubleValue();
            if(curr < min) {
                super.setObject(getMin());
            } else if(curr > max) {
                super.setObject(getMax());
            }
        } else if(getObject() instanceof Byte) {
            byte curr = getObject().byteValue();
            byte min = getMin().byteValue();
            byte max = getMax().byteValue();
            if(curr < min) {
                super.setObject(getMin());
            } else if(curr > max) {
                super.setObject(getMax());
            }
        }
        if (this.getCallback() != null) this.getCallback().onValueChange(this);
	}
	
}