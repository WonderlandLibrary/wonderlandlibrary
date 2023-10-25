/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.MessageStyle;
import com.github.steveice10.mc.protocol.util.ObjectUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.Objects;

public class TranslationMessage
extends Message {
    private String translationKey;
    private Message[] translationParams;

    public TranslationMessage(String translationKey, Message ... translationParams) {
        this.translationKey = translationKey;
        this.translationParams = translationParams;
        Message[] messageArray = this.translationParams = this.getTranslationParams();
        int n = this.translationParams.length;
        int n2 = 0;
        while (n2 < n) {
            Message param = messageArray[n2];
            param.getStyle().setParent(this.getStyle());
            ++n2;
        }
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public Message[] getTranslationParams() {
        Message[] copy = Arrays.copyOf(this.translationParams, this.translationParams.length);
        int index = 0;
        while (index < copy.length) {
            copy[index] = copy[index].clone();
            ++index;
        }
        return copy;
    }

    @Override
    public Message setStyle(MessageStyle style) {
        super.setStyle(style);
        Message[] messageArray = this.translationParams;
        int n = this.translationParams.length;
        int n2 = 0;
        while (n2 < n) {
            Message param = messageArray[n2];
            param.getStyle().setParent(this.getStyle());
            ++n2;
        }
        return this;
    }

    @Override
    public String getText() {
        return this.translationKey;
    }

    @Override
    public TranslationMessage clone() {
        return (TranslationMessage)new TranslationMessage(this.getTranslationKey(), this.getTranslationParams()).setStyle(this.getStyle().clone()).setExtra(this.getExtra());
    }

    @Override
    public JsonElement toJson() {
        JsonElement e = super.toJson();
        if (e.isJsonObject()) {
            JsonObject json = e.getAsJsonObject();
            json.addProperty("translate", this.translationKey);
            JsonArray params = new JsonArray();
            Message[] messageArray = this.translationParams;
            int n = this.translationParams.length;
            int n2 = 0;
            while (n2 < n) {
                Message param = messageArray[n2];
                params.add(param.toJson());
                ++n2;
            }
            json.add("with", params);
            return json;
        }
        return e;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TranslationMessage)) {
            return false;
        }
        TranslationMessage that = (TranslationMessage)o;
        return super.equals(o) && Objects.equals(this.translationKey, that.translationKey) && Arrays.equals(this.translationParams, that.translationParams);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(super.hashCode(), this.translationKey, this.translationParams);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}

