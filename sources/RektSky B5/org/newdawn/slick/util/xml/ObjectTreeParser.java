/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.util.xml;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;
import org.newdawn.slick.util.xml.XMLElementList;
import org.newdawn.slick.util.xml.XMLParser;

public class ObjectTreeParser {
    private HashMap nameToClass = new HashMap();
    private String defaultPackage;
    private ArrayList ignored = new ArrayList();
    private String addMethod = "add";

    public ObjectTreeParser() {
    }

    public ObjectTreeParser(String defaultPackage) {
        this.defaultPackage = defaultPackage;
    }

    public void addElementMapping(String elementName, Class elementClass) {
        this.nameToClass.put(elementName, elementClass);
    }

    public void addIgnoredElement(String elementName) {
        this.ignored.add(elementName);
    }

    public void setAddMethodName(String methodName) {
        this.addMethod = methodName;
    }

    public void setDefaultPackage(String defaultPackage) {
        this.defaultPackage = defaultPackage;
    }

    public Object parse(String ref) throws SlickXMLException {
        return this.parse(ref, ResourceLoader.getResourceAsStream(ref));
    }

    public Object parse(String name, InputStream in) throws SlickXMLException {
        XMLParser parser = new XMLParser();
        XMLElement root = parser.parse(name, in);
        return this.traverse(root);
    }

    public Object parseOnto(String ref, Object target) throws SlickXMLException {
        return this.parseOnto(ref, ResourceLoader.getResourceAsStream(ref), target);
    }

    public Object parseOnto(String name, InputStream in, Object target) throws SlickXMLException {
        XMLParser parser = new XMLParser();
        XMLElement root = parser.parse(name, in);
        return this.traverse(root, target);
    }

    private Class getClassForElementName(String name) {
        Class clazz = (Class)this.nameToClass.get(name);
        if (clazz != null) {
            return clazz;
        }
        if (this.defaultPackage != null) {
            try {
                return Class.forName(this.defaultPackage + "." + name);
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        return null;
    }

    private Object traverse(XMLElement current) throws SlickXMLException {
        return this.traverse(current, null);
    }

    private Object traverse(XMLElement current, Object instance) throws SlickXMLException {
        String name = current.getName();
        if (this.ignored.contains(name)) {
            return null;
        }
        Class clazz = instance == null ? this.getClassForElementName(name) : instance.getClass();
        if (clazz == null) {
            throw new SlickXMLException("Unable to map element " + name + " to a class, define the mapping");
        }
        try {
            if (instance == null) {
                Method contentMethod;
                instance = clazz.newInstance();
                Method elementNameMethod = this.getMethod(clazz, "setXMLElementName", new Class[]{String.class});
                if (elementNameMethod != null) {
                    this.invoke(elementNameMethod, instance, new Object[]{name});
                }
                if ((contentMethod = this.getMethod(clazz, "setXMLElementContent", new Class[]{String.class})) != null) {
                    this.invoke(contentMethod, instance, new Object[]{current.getContent()});
                }
            }
            String[] attrs = current.getAttributeNames();
            for (int i2 = 0; i2 < attrs.length; ++i2) {
                String methodName = "set" + attrs[i2];
                Method method = this.findMethod(clazz, methodName);
                if (method == null) {
                    Field field = this.findField(clazz, attrs[i2]);
                    if (field != null) {
                        String value = current.getAttribute(attrs[i2]);
                        Object typedValue = this.typeValue(value, field.getType());
                        this.setField(field, instance, typedValue);
                        continue;
                    }
                    Log.info("Unable to find property on: " + clazz + " for attribute: " + attrs[i2]);
                    continue;
                }
                String value = current.getAttribute(attrs[i2]);
                Object typedValue = this.typeValue(value, method.getParameterTypes()[0]);
                this.invoke(method, instance, new Object[]{typedValue});
            }
            XMLElementList children = current.getChildren();
            for (int i3 = 0; i3 < children.size(); ++i3) {
                XMLElement element = children.get(i3);
                Object child = this.traverse(element);
                if (child == null) continue;
                String methodName = this.addMethod;
                Method method = this.findMethod(clazz, methodName, child.getClass());
                if (method == null) {
                    Log.info("Unable to find method to add: " + child + " to " + clazz);
                    continue;
                }
                this.invoke(method, instance, new Object[]{child});
            }
            return instance;
        }
        catch (InstantiationException e2) {
            throw new SlickXMLException("Unable to instance " + clazz + " for element " + name + ", no zero parameter constructor?", e2);
        }
        catch (IllegalAccessException e3) {
            throw new SlickXMLException("Unable to instance " + clazz + " for element " + name + ", no zero parameter constructor?", e3);
        }
    }

    private Object typeValue(String value, Class clazz) throws SlickXMLException {
        if (clazz == String.class) {
            return value;
        }
        try {
            clazz = this.mapPrimitive(clazz);
            return clazz.getConstructor(String.class).newInstance(value);
        }
        catch (Exception e2) {
            throw new SlickXMLException("Failed to convert: " + value + " to the expected primitive type: " + clazz, e2);
        }
    }

    private Class mapPrimitive(Class clazz) {
        if (clazz == Integer.TYPE) {
            return Integer.class;
        }
        if (clazz == Double.TYPE) {
            return Double.class;
        }
        if (clazz == Float.TYPE) {
            return Float.class;
        }
        if (clazz == Boolean.TYPE) {
            return Boolean.class;
        }
        if (clazz == Long.TYPE) {
            return Long.class;
        }
        throw new RuntimeException("Unsupported primitive: " + clazz);
    }

    private Field findField(Class clazz, String name) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i2 = 0; i2 < fields.length; ++i2) {
            if (!fields[i2].getName().equalsIgnoreCase(name)) continue;
            if (fields[i2].getType().isPrimitive()) {
                return fields[i2];
            }
            if (fields[i2].getType() != String.class) continue;
            return fields[i2];
        }
        return null;
    }

    private Method findMethod(Class clazz, String name) {
        Method[] methods = clazz.getDeclaredMethods();
        for (int i2 = 0; i2 < methods.length; ++i2) {
            Method method;
            Class<?>[] params;
            if (!methods[i2].getName().equalsIgnoreCase(name) || (params = (method = methods[i2]).getParameterTypes()).length != 1) continue;
            return method;
        }
        return null;
    }

    private Method findMethod(Class clazz, String name, Class parameter) {
        Method[] methods = clazz.getDeclaredMethods();
        for (int i2 = 0; i2 < methods.length; ++i2) {
            Method method;
            Class<?>[] params;
            if (!methods[i2].getName().equalsIgnoreCase(name) || (params = (method = methods[i2]).getParameterTypes()).length != 1 || !method.getParameterTypes()[0].isAssignableFrom(parameter)) continue;
            return method;
        }
        return null;
    }

    private void setField(Field field, Object instance, Object value) throws SlickXMLException {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        }
        catch (IllegalArgumentException e2) {
            throw new SlickXMLException("Failed to set: " + field + " for an XML attribute, is it valid?", e2);
        }
        catch (IllegalAccessException e3) {
            throw new SlickXMLException("Failed to set: " + field + " for an XML attribute, is it valid?", e3);
        }
        finally {
            field.setAccessible(false);
        }
    }

    private void invoke(Method method, Object instance, Object[] params) throws SlickXMLException {
        try {
            method.setAccessible(true);
            method.invoke(instance, params);
        }
        catch (IllegalArgumentException e2) {
            throw new SlickXMLException("Failed to invoke: " + method + " for an XML attribute, is it valid?", e2);
        }
        catch (IllegalAccessException e3) {
            throw new SlickXMLException("Failed to invoke: " + method + " for an XML attribute, is it valid?", e3);
        }
        catch (InvocationTargetException e4) {
            throw new SlickXMLException("Failed to invoke: " + method + " for an XML attribute, is it valid?", e4);
        }
        finally {
            method.setAccessible(false);
        }
    }

    private Method getMethod(Class clazz, String name, Class[] params) {
        try {
            return clazz.getMethod(name, params);
        }
        catch (SecurityException e2) {
            return null;
        }
        catch (NoSuchMethodException e3) {
            return null;
        }
    }
}

