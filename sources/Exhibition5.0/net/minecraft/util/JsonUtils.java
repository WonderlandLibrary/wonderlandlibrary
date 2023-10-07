// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import com.google.gson.JsonPrimitive;
import org.apache.commons.lang3.StringUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtils
{
    private static final String __OBFID = "CL_00001484";
    
    public static boolean jsonObjectFieldTypeIsString(final JsonObject p_151205_0_, final String p_151205_1_) {
        return jsonObjectFieldTypeIsPrimitive(p_151205_0_, p_151205_1_) && p_151205_0_.getAsJsonPrimitive(p_151205_1_).isString();
    }
    
    public static boolean jsonElementTypeIsString(final JsonElement p_151211_0_) {
        return p_151211_0_.isJsonPrimitive() && p_151211_0_.getAsJsonPrimitive().isString();
    }
    
    public static boolean func_180199_c(final JsonObject p_180199_0_, final String p_180199_1_) {
        return jsonObjectFieldTypeIsPrimitive(p_180199_0_, p_180199_1_) && p_180199_0_.getAsJsonPrimitive(p_180199_1_).isBoolean();
    }
    
    public static boolean jsonObjectFieldTypeIsArray(final JsonObject p_151202_0_, final String p_151202_1_) {
        return jsonObjectHasNamedField(p_151202_0_, p_151202_1_) && p_151202_0_.get(p_151202_1_).isJsonArray();
    }
    
    public static boolean jsonObjectFieldTypeIsPrimitive(final JsonObject p_151201_0_, final String p_151201_1_) {
        return jsonObjectHasNamedField(p_151201_0_, p_151201_1_) && p_151201_0_.get(p_151201_1_).isJsonPrimitive();
    }
    
    public static boolean jsonObjectHasNamedField(final JsonObject p_151204_0_, final String p_151204_1_) {
        return p_151204_0_ != null && p_151204_0_.get(p_151204_1_) != null;
    }
    
    public static String getJsonElementStringValue(final JsonElement p_151206_0_, final String p_151206_1_) {
        if (p_151206_0_.isJsonPrimitive()) {
            return p_151206_0_.getAsString();
        }
        throw new JsonSyntaxException("Expected " + p_151206_1_ + " to be a string, was " + getJsonElementTypeDescription(p_151206_0_));
    }
    
    public static String getJsonObjectStringFieldValue(final JsonObject p_151200_0_, final String p_151200_1_) {
        if (p_151200_0_.has(p_151200_1_)) {
            return getJsonElementStringValue(p_151200_0_.get(p_151200_1_), p_151200_1_);
        }
        throw new JsonSyntaxException("Missing " + p_151200_1_ + ", expected to find a string");
    }
    
    public static String getJsonObjectStringFieldValueOrDefault(final JsonObject p_151219_0_, final String p_151219_1_, final String p_151219_2_) {
        return p_151219_0_.has(p_151219_1_) ? getJsonElementStringValue(p_151219_0_.get(p_151219_1_), p_151219_1_) : p_151219_2_;
    }
    
    public static boolean getJsonElementBooleanValue(final JsonElement p_151216_0_, final String p_151216_1_) {
        if (p_151216_0_.isJsonPrimitive()) {
            return p_151216_0_.getAsBoolean();
        }
        throw new JsonSyntaxException("Expected " + p_151216_1_ + " to be a Boolean, was " + getJsonElementTypeDescription(p_151216_0_));
    }
    
    public static boolean getJsonObjectBooleanFieldValue(final JsonObject p_151212_0_, final String p_151212_1_) {
        if (p_151212_0_.has(p_151212_1_)) {
            return getJsonElementBooleanValue(p_151212_0_.get(p_151212_1_), p_151212_1_);
        }
        throw new JsonSyntaxException("Missing " + p_151212_1_ + ", expected to find a Boolean");
    }
    
    public static boolean getJsonObjectBooleanFieldValueOrDefault(final JsonObject p_151209_0_, final String p_151209_1_, final boolean p_151209_2_) {
        return p_151209_0_.has(p_151209_1_) ? getJsonElementBooleanValue(p_151209_0_.get(p_151209_1_), p_151209_1_) : p_151209_2_;
    }
    
    public static float getJsonElementFloatValue(final JsonElement p_151220_0_, final String p_151220_1_) {
        if (p_151220_0_.isJsonPrimitive() && p_151220_0_.getAsJsonPrimitive().isNumber()) {
            return p_151220_0_.getAsFloat();
        }
        throw new JsonSyntaxException("Expected " + p_151220_1_ + " to be a Float, was " + getJsonElementTypeDescription(p_151220_0_));
    }
    
    public static float getJsonObjectFloatFieldValue(final JsonObject p_151217_0_, final String p_151217_1_) {
        if (p_151217_0_.has(p_151217_1_)) {
            return getJsonElementFloatValue(p_151217_0_.get(p_151217_1_), p_151217_1_);
        }
        throw new JsonSyntaxException("Missing " + p_151217_1_ + ", expected to find a Float");
    }
    
    public static float getJsonObjectFloatFieldValueOrDefault(final JsonObject p_151221_0_, final String p_151221_1_, final float p_151221_2_) {
        return p_151221_0_.has(p_151221_1_) ? getJsonElementFloatValue(p_151221_0_.get(p_151221_1_), p_151221_1_) : p_151221_2_;
    }
    
    public static int getJsonElementIntegerValue(final JsonElement p_151215_0_, final String p_151215_1_) {
        if (p_151215_0_.isJsonPrimitive() && p_151215_0_.getAsJsonPrimitive().isNumber()) {
            return p_151215_0_.getAsInt();
        }
        throw new JsonSyntaxException("Expected " + p_151215_1_ + " to be a Int, was " + getJsonElementTypeDescription(p_151215_0_));
    }
    
    public static int getJsonObjectIntegerFieldValue(final JsonObject p_151203_0_, final String p_151203_1_) {
        if (p_151203_0_.has(p_151203_1_)) {
            return getJsonElementIntegerValue(p_151203_0_.get(p_151203_1_), p_151203_1_);
        }
        throw new JsonSyntaxException("Missing " + p_151203_1_ + ", expected to find a Int");
    }
    
    public static int getJsonObjectIntegerFieldValueOrDefault(final JsonObject p_151208_0_, final String p_151208_1_, final int p_151208_2_) {
        return p_151208_0_.has(p_151208_1_) ? getJsonElementIntegerValue(p_151208_0_.get(p_151208_1_), p_151208_1_) : p_151208_2_;
    }
    
    public static JsonObject getElementAsJsonObject(final JsonElement p_151210_0_, final String p_151210_1_) {
        if (p_151210_0_.isJsonObject()) {
            return p_151210_0_.getAsJsonObject();
        }
        throw new JsonSyntaxException("Expected " + p_151210_1_ + " to be a JsonObject, was " + getJsonElementTypeDescription(p_151210_0_));
    }
    
    public static JsonObject getJsonObject(final JsonObject base, final String key) {
        if (base.has(key)) {
            return getElementAsJsonObject(base.get(key), key);
        }
        throw new JsonSyntaxException("Missing " + key + ", expected to find a JsonObject");
    }
    
    public static JsonObject getJsonObjectFieldOrDefault(final JsonObject p_151218_0_, final String p_151218_1_, final JsonObject p_151218_2_) {
        return p_151218_0_.has(p_151218_1_) ? getElementAsJsonObject(p_151218_0_.get(p_151218_1_), p_151218_1_) : p_151218_2_;
    }
    
    public static JsonArray getJsonElementAsJsonArray(final JsonElement p_151207_0_, final String p_151207_1_) {
        if (p_151207_0_.isJsonArray()) {
            return p_151207_0_.getAsJsonArray();
        }
        throw new JsonSyntaxException("Expected " + p_151207_1_ + " to be a JsonArray, was " + getJsonElementTypeDescription(p_151207_0_));
    }
    
    public static JsonArray getJsonObjectJsonArrayField(final JsonObject p_151214_0_, final String p_151214_1_) {
        if (p_151214_0_.has(p_151214_1_)) {
            return getJsonElementAsJsonArray(p_151214_0_.get(p_151214_1_), p_151214_1_);
        }
        throw new JsonSyntaxException("Missing " + p_151214_1_ + ", expected to find a JsonArray");
    }
    
    public static JsonArray getJsonObjectJsonArrayFieldOrDefault(final JsonObject p_151213_0_, final String p_151213_1_, final JsonArray p_151213_2_) {
        return p_151213_0_.has(p_151213_1_) ? getJsonElementAsJsonArray(p_151213_0_.get(p_151213_1_), p_151213_1_) : p_151213_2_;
    }
    
    public static String getJsonElementTypeDescription(final JsonElement p_151222_0_) {
        final String var1 = StringUtils.abbreviateMiddle(String.valueOf(p_151222_0_), "...", 10);
        if (p_151222_0_ == null) {
            return "null (missing)";
        }
        if (p_151222_0_.isJsonNull()) {
            return "null (json)";
        }
        if (p_151222_0_.isJsonArray()) {
            return "an array (" + var1 + ")";
        }
        if (p_151222_0_.isJsonObject()) {
            return "an object (" + var1 + ")";
        }
        if (p_151222_0_.isJsonPrimitive()) {
            final JsonPrimitive var2 = p_151222_0_.getAsJsonPrimitive();
            if (var2.isNumber()) {
                return "a number (" + var1 + ")";
            }
            if (var2.isBoolean()) {
                return "a boolean (" + var1 + ")";
            }
        }
        return var1;
    }
}
