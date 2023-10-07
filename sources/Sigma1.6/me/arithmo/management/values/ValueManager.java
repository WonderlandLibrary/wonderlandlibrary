/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package me.arithmo.management.values;

import java.io.File;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.arithmo.Client;
import me.arithmo.management.values.BooleanValue;
import me.arithmo.management.values.NumberValue;
import me.arithmo.management.values.StringValue;
import me.arithmo.management.values.Value;
import me.arithmo.module.Module;
import me.arithmo.util.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class ValueManager {
    private static final File VALUELOCATION = FileUtils.getConfigFile("Values");
    private static CopyOnWriteArrayList<Value> values = new CopyOnWriteArrayList();

    public static void setup() {
        try {
            for (Module mod : Client.getModuleManager().getArray()) {
                System.out.println(mod.getName() + "    +");
                Field[] declaredFields = mod.getClass().getDeclaredFields();
                int length = declaredFields.length;
                for (int i = 0; i < length; ++i) {
                    Number value3;
                    Value.Val opAnnotation;
                    String name;
                    System.out.println(mod.getName() + "Theres' something");
                    Field field = declaredFields[i];
                    field.setAccessible(true);
                    if (!field.isAnnotationPresent(Value.Val.class)) continue;
                    System.out.println(mod.getName() + "Value Present");
                    if (!(field.getType().isAssignableFrom(Float.TYPE) || field.getType().isAssignableFrom(Double.TYPE) || field.getType().isAssignableFrom(Integer.TYPE) || field.getType().isAssignableFrom(Long.TYPE) || field.getType().isAssignableFrom(Short.TYPE) || field.getType().isAssignableFrom(Byte.TYPE))) {
                        String value2;
                        if (field.getType().isAssignableFrom(Boolean.TYPE)) {
                            boolean value;
                            try {
                                value = field.getBoolean(mod);
                            }
                            catch (IllegalArgumentException e2) {
                                value = false;
                            }
                            opAnnotation = field.getAnnotation(Value.Val.class);
                            name = opAnnotation.name().equalsIgnoreCase("null") ? StringUtils.capitalize((String)field.getName()) : StringUtils.capitalize((String)opAnnotation.name());
                            values.add(new BooleanValue(field.getName(), value, mod, false));
                            continue;
                        }
                        if (!field.getType().isAssignableFrom(String.class)) continue;
                        try {
                            value2 = (String)field.get(mod);
                        }
                        catch (IllegalArgumentException e2) {
                            value2 = "";
                        }
                        opAnnotation = field.getAnnotation(Value.Val.class);
                        name = opAnnotation.name().equalsIgnoreCase("null") ? StringUtils.capitalize((String)field.getName()) : StringUtils.capitalize((String)opAnnotation.name());
                        values.add(new StringValue(field.getName(), value2, mod));
                        continue;
                    }
                    try {
                        value3 = (Number)field.get(mod);
                    }
                    catch (IllegalArgumentException e2) {
                        value3 = 0;
                    }
                    opAnnotation = field.getAnnotation(Value.Val.class);
                    name = opAnnotation.name().equalsIgnoreCase("null") ? StringUtils.capitalize((String)field.getName()) : StringUtils.capitalize((String)opAnnotation.name());
                    NumberValue Value2 = new NumberValue(field.getName(), value3, mod);
                    Value2.setMin(opAnnotation.minVal());
                    Value2.setMax(opAnnotation.maxVal());
                    Value2.setIncrement(opAnnotation.valInc());
                    values.add(Value2);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ValueManager.load();
        ValueManager.save();
    }

    public static void load() {
        List<String> fileContent = FileUtils.read(VALUELOCATION);
        for (String line : fileContent) {
            try {
                String[] split = line.split(":");
                String ValueId = split[0];
                String ValueValue = split[1];
                String modId = split[2];
                Value Value2 = ValueManager.getValue(ValueId, split[2]);
                if (Value2 == null) continue;
                if (Value2 instanceof NumberValue) {
                    ((NumberValue)Value2).setValue(ValueValue);
                    continue;
                }
                if (Value2 instanceof BooleanValue) {
                    ((BooleanValue)Value2).setValueHard(Boolean.parseBoolean(ValueValue));
                    continue;
                }
                if (!(Value2 instanceof StringValue)) continue;
                Value2.setValue(ValueValue);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        ArrayList<String> fileContent = new ArrayList<String>();
        for (Value Value2 : values) {
            String ValueId = Value2.getValueName();
            String ValueVal = Value2.getValue().toString();
            Module mod = Value2.getModule();
            fileContent.add(String.format("%s:%s:%s", ValueId, ValueVal, mod.getName()));
        }
        FileUtils.write(VALUELOCATION, fileContent, true);
    }

    public static Value getValue(String ValueName, String modId) {
        for (Value Value2 : values) {
            if (!Value2.getValueName().equalsIgnoreCase(ValueName) && !Value2.getValueName().equalsIgnoreCase(ValueName) || !Value2.getModule().getName().equalsIgnoreCase(modId)) continue;
            return Value2;
        }
        return null;
    }

    public static List<Value> getValueList() {
        return values;
    }
}

