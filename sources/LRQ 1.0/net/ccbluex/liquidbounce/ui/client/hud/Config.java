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
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.Value;

public final class Config {
    private JsonArray jsonArray = new JsonArray();

    public final String toJson() {
        return new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)this.jsonArray);
    }

    public final HUD toHUD() {
        HUD hud = new HUD();
        try {
            block4: for (JsonElement jsonObject : this.jsonArray) {
                try {
                    if (!(jsonObject instanceof JsonObject) || !((JsonObject)jsonObject).has("Type")) continue;
                    String type = ((JsonObject)jsonObject).get("Type").getAsString();
                    for (Class<? extends Element> elementClass : HUD.Companion.getElements()) {
                        String classType = elementClass.getAnnotation(ElementInfo.class).name();
                        if (!classType.equals(type)) continue;
                        Element element = elementClass.newInstance();
                        element.setX(((JsonObject)jsonObject).get("X").getAsInt());
                        element.setY(((JsonObject)jsonObject).get("Y").getAsInt());
                        element.setScale(((JsonObject)jsonObject).get("Scale").getAsFloat());
                        Side.Horizontal horizontal = Side.Horizontal.Companion.getByName(((JsonObject)jsonObject).get("HorizontalFacing").getAsString());
                        if (horizontal == null) {
                            Intrinsics.throwNpe();
                        }
                        Side.Vertical vertical = Side.Vertical.Companion.getByName(((JsonObject)jsonObject).get("VerticalFacing").getAsString());
                        if (vertical == null) {
                            Intrinsics.throwNpe();
                        }
                        element.setSide(new Side(horizontal, vertical));
                        for (Value<?> value : element.getValues()) {
                            if (!((JsonObject)jsonObject).has(value.getName())) continue;
                            value.fromJson(((JsonObject)jsonObject).get(value.getName()));
                        }
                        if (((JsonObject)jsonObject).has("font")) {
                            Object v2;
                            block16: {
                                Value<?> value;
                                value = element.getValues();
                                boolean bl = false;
                                Value<?> value2 = value;
                                boolean bl2 = false;
                                Iterator iterator = value2.iterator();
                                while (iterator.hasNext()) {
                                    Object t = iterator.next();
                                    Value it = (Value)t;
                                    boolean bl3 = false;
                                    if (!(it instanceof FontValue)) continue;
                                    v2 = t;
                                    break block16;
                                }
                                v2 = null;
                            }
                            Value value = v2;
                            if (value != null) {
                                value.fromJson(((JsonObject)jsonObject).get("font"));
                            }
                        }
                        hud.addElement(element);
                        continue block4;
                    }
                }
                catch (Exception e) {
                    ClientUtils.getLogger().error("Error while loading custom hud element from config.", (Throwable)e);
                }
            }
            for (Class<? extends Element> elementClass : HUD.Companion.getElements()) {
                boolean bl;
                block17: {
                    if (!elementClass.getAnnotation(ElementInfo.class).force()) continue;
                    Iterable $this$none$iv = hud.getElements();
                    boolean $i$f$none = false;
                    if ($this$none$iv instanceof Collection && ((Collection)$this$none$iv).isEmpty()) {
                        bl = true;
                    } else {
                        for (Object element$iv : $this$none$iv) {
                            Element it = (Element)element$iv;
                            boolean bl4 = false;
                            if (!it.getClass().equals(elementClass)) continue;
                            bl = false;
                            break block17;
                        }
                        bl = true;
                    }
                }
                if (!bl) continue;
                hud.addElement(elementClass.newInstance());
            }
        }
        catch (Exception e) {
            ClientUtils.getLogger().error("Error while loading custom hud config.", (Throwable)e);
            return HUD.Companion.createDefault();
        }
        return hud;
    }

    public Config(String config) {
        this.jsonArray = (JsonArray)new Gson().fromJson(config, JsonArray.class);
    }

    public Config(HUD hud) {
        for (Element element : hud.getElements()) {
            JsonObject elementObject = new JsonObject();
            elementObject.addProperty("Type", element.getName());
            elementObject.addProperty("X", (Number)element.getX());
            elementObject.addProperty("Y", (Number)element.getY());
            elementObject.addProperty("Scale", (Number)Float.valueOf(element.getScale()));
            elementObject.addProperty("HorizontalFacing", element.getSide().getHorizontal().getSideName());
            elementObject.addProperty("VerticalFacing", element.getSide().getVertical().getSideName());
            for (Value<?> value : element.getValues()) {
                elementObject.add(value.getName(), value.toJson());
            }
            this.jsonArray.add((JsonElement)elementObject);
        }
    }
}

