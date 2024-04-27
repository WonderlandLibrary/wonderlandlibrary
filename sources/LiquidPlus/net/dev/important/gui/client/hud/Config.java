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
package net.dev.important.gui.client.hud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.gui.client.hud.HUD;
import net.dev.important.gui.client.hud.element.Element;
import net.dev.important.gui.client.hud.element.ElementInfo;
import net.dev.important.gui.client.hud.element.Side;
import net.dev.important.utils.ClientUtils;
import net.dev.important.value.FontValue;
import net.dev.important.value.Value;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u000f\b\u0016\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0006\u0010\n\u001a\u00020\u0006J\u0006\u0010\u000b\u001a\u00020\u0003R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2={"Lnet/dev/important/gui/client/hud/Config;", "", "config", "", "(Ljava/lang/String;)V", "hud", "Lnet/dev/important/gui/client/hud/HUD;", "(Lnet/dev/important/gui/client/hud/HUD;)V", "jsonArray", "Lcom/google/gson/JsonArray;", "toHUD", "toJson", "LiquidBounce"})
public final class Config {
    @NotNull
    private JsonArray jsonArray;

    public Config(@NotNull String config) {
        Intrinsics.checkNotNullParameter(config, "config");
        this.jsonArray = new JsonArray();
        Object object = new Gson().fromJson(config, JsonArray.class);
        Intrinsics.checkNotNullExpressionValue(object, "Gson().fromJson(config, JsonArray::class.java)");
        this.jsonArray = (JsonArray)object;
    }

    public Config(@NotNull HUD hud) {
        Intrinsics.checkNotNullParameter(hud, "hud");
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

    @NotNull
    public final String toJson() {
        String string = new GsonBuilder().setPrettyPrinting().create().toJson((JsonElement)this.jsonArray);
        Intrinsics.checkNotNullExpressionValue(string, "GsonBuilder().setPrettyP\u2026reate().toJson(jsonArray)");
        return string;
    }

    @NotNull
    public final HUD toHUD() {
        HUD hud = new HUD();
        try {
            for (JsonElement jsonObject : this.jsonArray) {
                try {
                    if (!(jsonObject instanceof JsonObject) || !((JsonObject)jsonObject).has("Type")) continue;
                    String type = ((JsonObject)jsonObject).get("Type").getAsString();
                    for (Class<? extends Element> elementClass : HUD.Companion.getElements()) {
                        String classType = elementClass.getAnnotation(ElementInfo.class).name();
                        if (!Intrinsics.areEqual(classType, type)) continue;
                        Element element = elementClass.newInstance();
                        element.setX(((JsonObject)jsonObject).get("X").getAsInt());
                        element.setY(((JsonObject)jsonObject).get("Y").getAsInt());
                        element.setScale(((JsonObject)jsonObject).get("Scale").getAsFloat());
                        Object object = ((JsonObject)jsonObject).get("HorizontalFacing").getAsString();
                        Intrinsics.checkNotNullExpressionValue(object, "jsonObject[\"HorizontalFacing\"].asString");
                        Side.Horizontal horizontal = Side.Horizontal.Companion.getByName((String)object);
                        Intrinsics.checkNotNull((Object)horizontal);
                        object = ((JsonObject)jsonObject).get("VerticalFacing").getAsString();
                        Intrinsics.checkNotNullExpressionValue(object, "jsonObject[\"VerticalFacing\"].asString");
                        Side.Vertical vertical = Side.Vertical.Companion.getByName((String)object);
                        Intrinsics.checkNotNull((Object)vertical);
                        element.setSide(new Side(horizontal, vertical));
                        for (Value value : element.getValues()) {
                            if (!((JsonObject)jsonObject).has(value.getName())) continue;
                            JsonElement jsonElement = ((JsonObject)jsonObject).get(value.getName());
                            Intrinsics.checkNotNullExpressionValue(jsonElement, "jsonObject[value.name]");
                            value.fromJson(jsonElement);
                        }
                        if (((JsonObject)jsonObject).has("font")) {
                            Object v2;
                            block14: {
                                for (Object t : (Iterable)element.getValues()) {
                                    Value it = (Value)t;
                                    boolean bl = false;
                                    if (!(it instanceof FontValue)) continue;
                                    v2 = t;
                                    break block14;
                                }
                                v2 = null;
                            }
                            Value value = v2;
                            if (value != null) {
                                JsonElement jsonElement = ((JsonObject)jsonObject).get("font");
                                Intrinsics.checkNotNullExpressionValue(jsonElement, "jsonObject[\"font\"]");
                                value.fromJson(jsonElement);
                            }
                        }
                        Intrinsics.checkNotNullExpressionValue(element, "element");
                        hud.addElement(element);
                    }
                }
                catch (Exception e) {
                    ClientUtils.getLogger().error("Error while loading custom hud element from config.", (Throwable)e);
                }
            }
            Class<? extends Element>[] classArray = HUD.Companion.getElements();
            int n = 0;
            int n2 = classArray.length;
            while (n < n2) {
                boolean bl;
                Class<? extends Element> elementClass;
                block15: {
                    elementClass = classArray[n];
                    ++n;
                    if (!elementClass.getAnnotation(ElementInfo.class).force()) continue;
                    Iterable $this$none$iv = hud.getElements();
                    boolean $i$f$none = false;
                    if ($this$none$iv instanceof Collection && ((Collection)$this$none$iv).isEmpty()) {
                        bl = true;
                    } else {
                        for (Object element$iv : $this$none$iv) {
                            Element it = (Element)element$iv;
                            boolean bl2 = false;
                            if (!Intrinsics.areEqual(it.getClass(), elementClass)) continue;
                            bl = false;
                            break block15;
                        }
                        bl = true;
                    }
                }
                if (!bl) continue;
                Element element = elementClass.newInstance();
                Intrinsics.checkNotNullExpressionValue(element, "elementClass.newInstance()");
                hud.addElement(element);
            }
        }
        catch (Exception e) {
            ClientUtils.getLogger().error("Error while loading custom hud config.", (Throwable)e);
            return HUD.Companion.createDefault();
        }
        return hud;
    }
}

