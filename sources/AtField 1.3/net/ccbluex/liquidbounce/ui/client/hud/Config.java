/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.ui.client.hud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.hud.HUD;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.Value;

public final class Config {
    private JsonArray jsonArray = new JsonArray();

    public final String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)this.jsonArray);
    }

    public Config(HUD hUD) {
        for (Element element : hUD.getElements()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Type", element.getName());
            jsonObject.addProperty("X", (Number)element.getX());
            jsonObject.addProperty("Y", (Number)element.getY());
            jsonObject.addProperty("Scale", (Number)Float.valueOf(element.getScale()));
            jsonObject.addProperty("HorizontalFacing", element.getSide().getHorizontal().getSideName());
            jsonObject.addProperty("VerticalFacing", element.getSide().getVertical().getSideName());
            for (Value value : element.getValues()) {
                jsonObject.add(value.getName(), value.toJson());
            }
            this.jsonArray.add((JsonElement)jsonObject);
        }
    }

    public Config(String string) {
        this.jsonArray = (JsonArray)new Gson().fromJson(string, JsonArray.class);
    }

    public final HUD toHUD() {
        Element element;
        HUD hUD = new HUD();
        block0: for (JsonElement object : this.jsonArray) {
            if (!(object instanceof JsonObject) || !((JsonObject)object).has("Type")) continue;
            String string = ((JsonObject)object).get("Type").getAsString();
            for (Class clazz : HUD.Companion.getElements()) {
                Object object2 = clazz.getAnnotation(ElementInfo.class).name();
                if (!object2.equals(string)) continue;
                element = (Element)clazz.newInstance();
                element.setX(((JsonObject)object).get("X").getAsInt());
                element.setY(((JsonObject)object).get("Y").getAsInt());
                element.setScale(((JsonObject)object).get("Scale").getAsFloat());
                Side.Horizontal horizontal = Side.Horizontal.Companion.getByName(((JsonObject)object).get("HorizontalFacing").getAsString());
                if (horizontal == null) {
                    Intrinsics.throwNpe();
                }
                Side.Vertical vertical = Side.Vertical.Companion.getByName(((JsonObject)object).get("VerticalFacing").getAsString());
                if (vertical == null) {
                    Intrinsics.throwNpe();
                }
                element.setSide(new Side(horizontal, vertical));
                for (Object object3 : element.getValues()) {
                    if (!((JsonObject)object).has(((Value)object3).getName())) continue;
                    ((Value)object3).fromJson(((JsonObject)object).get(((Value)object3).getName()));
                }
                if (((JsonObject)object).has("font")) {
                    Object v2;
                    block12: {
                        Object object3;
                        object3 = element.getValues();
                        boolean bl = false;
                        Object object4 = object3;
                        boolean bl2 = false;
                        Iterator iterator2 = object4.iterator();
                        while (iterator2.hasNext()) {
                            Object t = iterator2.next();
                            Value value = (Value)t;
                            boolean bl3 = false;
                            if (!(value instanceof FontValue)) continue;
                            v2 = t;
                            break block12;
                        }
                        v2 = null;
                    }
                    Value value = v2;
                    if (value != null) {
                        value.fromJson(((JsonObject)object).get("font"));
                    }
                }
                hUD.addElement(element);
                continue block0;
            }
        }
        for (Class clazz : HUD.Companion.getElements()) {
            boolean bl;
            block13: {
                if (!clazz.getAnnotation(ElementInfo.class).force()) continue;
                Iterable iterable = hUD.getElements();
                boolean bl4 = false;
                if (iterable instanceof Collection && ((Collection)iterable).isEmpty()) {
                    bl = true;
                } else {
                    for (Object object2 : iterable) {
                        element = (Element)object2;
                        boolean bl5 = false;
                        if (!element.getClass().equals(clazz)) continue;
                        bl = false;
                        break block13;
                    }
                    bl = true;
                }
            }
            if (!bl) continue;
            hUD.addElement((Element)clazz.newInstance());
        }
        return hUD;
    }
}

