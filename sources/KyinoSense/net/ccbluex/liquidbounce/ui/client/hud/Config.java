/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.hud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Collection;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.ui.client.hud.HUD;
import net.ccbluex.liquidbounce.ui.client.hud.element.Element;
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo;
import net.ccbluex.liquidbounce.ui.client.hud.element.Side;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.value.FontValue;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u000f\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0006\u0010\n\u001a\u00020\u0006J\u0006\u0010\u000b\u001a\u00020\u0003R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/hud/Config;", "", "config", "", "(Ljava/lang/String;)V", "hud", "Lnet/ccbluex/liquidbounce/ui/client/hud/HUD;", "(Lnet/ccbluex/liquidbounce/ui/client/hud/HUD;)V", "jsonArray", "Lcom/google/gson/JsonArray;", "toHUD", "toJson", "KyinoClient"})
public final class Config {
    private JsonArray jsonArray;

    @NotNull
    public final String toJson() {
        String string = new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)this.jsonArray);
        Intrinsics.checkExpressionValueIsNotNull(string, "GsonBuilder().setPrettyP\u2026reate().toJson(jsonArray)");
        return string;
    }

    @NotNull
    public final HUD toHUD() {
        HUD hud = new HUD();
        try {
            block4: for (JsonElement jsonObject : this.jsonArray) {
                try {
                    if (!(jsonObject instanceof JsonObject) || !((JsonObject)jsonObject).has("Type")) continue;
                    JsonElement jsonElement = ((JsonObject)jsonObject).get("Type");
                    Intrinsics.checkExpressionValueIsNotNull(jsonElement, "jsonObject[\"Type\"]");
                    String type = jsonElement.getAsString();
                    for (Class<? extends Element> elementClass : HUD.Companion.getElements()) {
                        String classType = elementClass.getAnnotation(ElementInfo.class).name();
                        if (!Intrinsics.areEqual(classType, type)) continue;
                        Element element = elementClass.newInstance();
                        JsonElement jsonElement2 = ((JsonObject)jsonObject).get("X");
                        Intrinsics.checkExpressionValueIsNotNull(jsonElement2, "jsonObject[\"X\"]");
                        element.setX(jsonElement2.getAsInt());
                        JsonElement jsonElement3 = ((JsonObject)jsonObject).get("Y");
                        Intrinsics.checkExpressionValueIsNotNull(jsonElement3, "jsonObject[\"Y\"]");
                        element.setY(jsonElement3.getAsInt());
                        JsonElement jsonElement4 = ((JsonObject)jsonObject).get("Scale");
                        Intrinsics.checkExpressionValueIsNotNull(jsonElement4, "jsonObject[\"Scale\"]");
                        element.setScale(jsonElement4.getAsFloat());
                        JsonElement jsonElement5 = ((JsonObject)jsonObject).get("HorizontalFacing");
                        Intrinsics.checkExpressionValueIsNotNull(jsonElement5, "jsonObject[\"HorizontalFacing\"]");
                        String string = jsonElement5.getAsString();
                        Intrinsics.checkExpressionValueIsNotNull(string, "jsonObject[\"HorizontalFacing\"].asString");
                        Side.Horizontal horizontal = Side.Horizontal.Companion.getByName(string);
                        if (horizontal == null) {
                            Intrinsics.throwNpe();
                        }
                        JsonElement jsonElement6 = ((JsonObject)jsonObject).get("VerticalFacing");
                        Intrinsics.checkExpressionValueIsNotNull(jsonElement6, "jsonObject[\"VerticalFacing\"]");
                        String string2 = jsonElement6.getAsString();
                        Intrinsics.checkExpressionValueIsNotNull(string2, "jsonObject[\"VerticalFacing\"].asString");
                        Side.Vertical vertical = Side.Vertical.Companion.getByName(string2);
                        if (vertical == null) {
                            Intrinsics.throwNpe();
                        }
                        element.setSide(new Side(horizontal, vertical));
                        for (Value<?> value : element.getValues()) {
                            if (!((JsonObject)jsonObject).has(value.getName())) continue;
                            JsonElement jsonElement7 = ((JsonObject)jsonObject).get(value.getName());
                            Intrinsics.checkExpressionValueIsNotNull(jsonElement7, "jsonObject[value.name]");
                            value.fromJson(jsonElement7);
                        }
                        if (((JsonObject)jsonObject).has("font")) {
                            Object v11;
                            block16: {
                                Value<?> value;
                                value = element.getValues();
                                boolean bl = false;
                                Value<?> value2 = value;
                                boolean bl2 = false;
                                Iterator iterator2 = value2.iterator();
                                while (iterator2.hasNext()) {
                                    Object t = iterator2.next();
                                    Value it = (Value)t;
                                    boolean bl3 = false;
                                    if (!(it instanceof FontValue)) continue;
                                    v11 = t;
                                    break block16;
                                }
                                v11 = null;
                            }
                            Value value = v11;
                            if (value != null) {
                                JsonElement jsonElement8 = ((JsonObject)jsonObject).get("font");
                                Intrinsics.checkExpressionValueIsNotNull(jsonElement8, "jsonObject[\"font\"]");
                                value.fromJson(jsonElement8);
                            }
                        }
                        Element element2 = element;
                        Intrinsics.checkExpressionValueIsNotNull(element2, "element");
                        hud.addElement(element2);
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
                            if (!Intrinsics.areEqual(it.getClass(), elementClass)) continue;
                            bl = false;
                            break block17;
                        }
                        bl = true;
                    }
                }
                if (!bl) continue;
                Element element = elementClass.newInstance();
                Intrinsics.checkExpressionValueIsNotNull(element, "elementClass.newInstance()");
                hud.addElement(element);
            }
        }
        catch (Exception e) {
            ClientUtils.getLogger().error("Error while loading custom hud config.", (Throwable)e);
            return HUD.Companion.createDefault();
        }
        return hud;
    }

    public Config(@NotNull String config) {
        Intrinsics.checkParameterIsNotNull(config, "config");
        this.jsonArray = new JsonArray();
        Object object = new Gson().fromJson(config, JsonArray.class);
        Intrinsics.checkExpressionValueIsNotNull(object, "Gson().fromJson(config, JsonArray::class.java)");
        this.jsonArray = (JsonArray)object;
    }

    public Config(@NotNull HUD hud) {
        Intrinsics.checkParameterIsNotNull(hud, "hud");
        this.jsonArray = new JsonArray();
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

