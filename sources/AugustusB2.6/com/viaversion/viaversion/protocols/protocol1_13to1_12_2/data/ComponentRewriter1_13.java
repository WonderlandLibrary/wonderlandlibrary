// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data;

import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viaversion.libs.gson.JsonPrimitive;
import java.util.Iterator;
import com.viaversion.viaversion.api.minecraft.item.Item;
import java.io.IOException;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ShortTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.nbt.BinaryTagIO;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.rewriter.ComponentRewriter;

public class ComponentRewriter1_13 extends ComponentRewriter
{
    public ComponentRewriter1_13(final Protocol protocol) {
        super(protocol);
    }
    
    @Override
    protected void handleHoverEvent(final JsonObject hoverEvent) {
        super.handleHoverEvent(hoverEvent);
        final String action = hoverEvent.getAsJsonPrimitive("action").getAsString();
        if (!action.equals("show_item")) {
            return;
        }
        final JsonElement value = hoverEvent.get("value");
        if (value == null) {
            return;
        }
        final String text = this.findItemNBT(value);
        if (text == null) {
            return;
        }
        CompoundTag tag;
        try {
            tag = BinaryTagIO.readString(text);
        }
        catch (Exception e) {
            if (!Via.getConfig().isSuppressConversionWarnings() || Via.getManager().isDebug()) {
                Via.getPlatform().getLogger().warning("Error reading NBT in show_item:" + text);
                e.printStackTrace();
            }
            return;
        }
        final CompoundTag itemTag = tag.get("tag");
        final ShortTag damageTag = tag.get("Damage");
        final short damage = (short)((damageTag != null) ? damageTag.asShort() : 0);
        final Item item = new DataItem();
        item.setData(damage);
        item.setTag(itemTag);
        this.protocol.getItemRewriter().handleItemToClient(item);
        if (damage != item.data()) {
            tag.put("Damage", new ShortTag(item.data()));
        }
        if (itemTag != null) {
            tag.put("tag", itemTag);
        }
        final JsonArray array = new JsonArray();
        final JsonObject object = new JsonObject();
        array.add(object);
        try {
            final String serializedNBT = BinaryTagIO.writeString(tag);
            object.addProperty("text", serializedNBT);
            hoverEvent.add("value", array);
        }
        catch (IOException e2) {
            Via.getPlatform().getLogger().warning("Error writing NBT in show_item:" + text);
            e2.printStackTrace();
        }
    }
    
    protected String findItemNBT(final JsonElement element) {
        if (element.isJsonArray()) {
            for (final JsonElement jsonElement : element.getAsJsonArray()) {
                final String value = this.findItemNBT(jsonElement);
                if (value != null) {
                    return value;
                }
            }
        }
        else if (element.isJsonObject()) {
            final JsonPrimitive text = element.getAsJsonObject().getAsJsonPrimitive("text");
            if (text != null) {
                return text.getAsString();
            }
        }
        else if (element.isJsonPrimitive()) {
            return element.getAsJsonPrimitive().getAsString();
        }
        return null;
    }
    
    @Override
    protected void handleTranslate(final JsonObject object, final String translate) {
        super.handleTranslate(object, translate);
        String newTranslate = Protocol1_13To1_12_2.MAPPINGS.getTranslateMapping().get(translate);
        if (newTranslate == null) {
            newTranslate = Protocol1_13To1_12_2.MAPPINGS.getMojangTranslation().get(translate);
        }
        if (newTranslate != null) {
            object.addProperty("translate", newTranslate);
        }
    }
}
