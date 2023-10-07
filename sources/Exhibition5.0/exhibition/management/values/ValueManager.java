// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.values;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import exhibition.util.FileUtils;
import java.lang.reflect.Field;
import org.apache.commons.lang3.StringUtils;
import java.lang.annotation.Annotation;
import exhibition.Client;
import exhibition.module.Module;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.File;

public class ValueManager
{
    private static final File VALUELOCATION;
    private static CopyOnWriteArrayList<Value> values;
    
    public static void setup() {
        try {
            for (final Module mod : Client.getModuleManager().getArray()) {
                System.out.println(mod.getName() + "    +");
                Field[] declaredFields;
                for (int length = (declaredFields = mod.getClass().getDeclaredFields()).length, i = 0; i < length; ++i) {
                    System.out.println(mod.getName() + "Theres' something");
                    final Field field = declaredFields[i];
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Value.Val.class)) {
                        System.out.println(mod.getName() + "Value Present");
                        if (!field.getType().isAssignableFrom(Float.TYPE) && !field.getType().isAssignableFrom(Double.TYPE) && !field.getType().isAssignableFrom(Integer.TYPE) && !field.getType().isAssignableFrom(Long.TYPE) && !field.getType().isAssignableFrom(Short.TYPE) && !field.getType().isAssignableFrom(Byte.TYPE)) {
                            if (field.getType().isAssignableFrom(Boolean.TYPE)) {
                                boolean value;
                                try {
                                    value = field.getBoolean(mod);
                                }
                                catch (IllegalArgumentException e2) {
                                    value = false;
                                }
                                final Value.Val opAnnotation = field.getAnnotation(Value.Val.class);
                                final String name = opAnnotation.name().equalsIgnoreCase("null") ? StringUtils.capitalize(field.getName()) : StringUtils.capitalize(opAnnotation.name());
                                ValueManager.values.add(new BooleanValue(field.getName(), value, mod, false));
                            }
                            else if (field.getType().isAssignableFrom(String.class)) {
                                String value2;
                                try {
                                    value2 = (String)field.get(mod);
                                }
                                catch (IllegalArgumentException e2) {
                                    value2 = "";
                                }
                                final Value.Val opAnnotation = field.getAnnotation(Value.Val.class);
                                final String name = opAnnotation.name().equalsIgnoreCase("null") ? StringUtils.capitalize(field.getName()) : StringUtils.capitalize(opAnnotation.name());
                                ValueManager.values.add(new StringValue(field.getName(), value2, mod));
                            }
                        }
                        else {
                            Number value3;
                            try {
                                value3 = (Number)field.get(mod);
                            }
                            catch (IllegalArgumentException e2) {
                                value3 = 0;
                            }
                            final Value.Val opAnnotation = field.getAnnotation(Value.Val.class);
                            final String name = opAnnotation.name().equalsIgnoreCase("null") ? StringUtils.capitalize(field.getName()) : StringUtils.capitalize(opAnnotation.name());
                            final NumberValue Value = new NumberValue(field.getName(), value3, mod);
                            Value.setMin(opAnnotation.minVal());
                            Value.setMax(opAnnotation.maxVal());
                            Value.setIncrement(opAnnotation.valInc());
                            ValueManager.values.add(Value);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        load();
        save();
    }
    
    public static void load() {
        final List<String> fileContent = FileUtils.read(ValueManager.VALUELOCATION);
        for (final String line : fileContent) {
            try {
                final String[] split = line.split(":");
                final String ValueId = split[0];
                final String ValueValue = split[1];
                final String modId = split[2];
                final Value Value = getValue(ValueId, split[2]);
                if (Value == null) {
                    continue;
                }
                if (Value instanceof NumberValue) {
                    ((NumberValue)Value).setValue(ValueValue);
                }
                else if (Value instanceof BooleanValue) {
                    ((BooleanValue)Value).setValueHard(Boolean.parseBoolean(ValueValue));
                }
                else {
                    if (!(Value instanceof StringValue)) {
                        continue;
                    }
                    Value.setValue(ValueValue);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void save() {
        final List<String> fileContent = new ArrayList<String>();
        for (final Value Value : ValueManager.values) {
            final String ValueId = Value.getValueName();
            final String ValueVal = Value.getValue().toString();
            final Module mod = Value.getModule();
            fileContent.add(String.format("%s:%s:%s", ValueId, ValueVal, mod.getName()));
        }
        FileUtils.write(ValueManager.VALUELOCATION, fileContent, true);
    }
    
    public static Value getValue(final String ValueName, final String modId) {
        for (final Value Value : ValueManager.values) {
            if ((Value.getValueName().equalsIgnoreCase(ValueName) || Value.getValueName().equalsIgnoreCase(ValueName)) && Value.getModule().getName().equalsIgnoreCase(modId)) {
                return Value;
            }
        }
        return null;
    }
    
    public static List<Value> getValueList() {
        return ValueManager.values;
    }
    
    static {
        VALUELOCATION = FileUtils.getConfigFile("Values");
        ValueManager.values = new CopyOnWriteArrayList<Value>();
    }
}
