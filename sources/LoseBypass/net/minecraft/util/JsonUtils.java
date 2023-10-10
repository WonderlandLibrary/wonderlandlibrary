/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;

public class JsonUtils {
    public static boolean isString(JsonObject p_151205_0_, String p_151205_1_) {
        if (!JsonUtils.isJsonPrimitive(p_151205_0_, p_151205_1_)) {
            return false;
        }
        boolean bl = p_151205_0_.getAsJsonPrimitive(p_151205_1_).isString();
        return bl;
    }

    public static boolean isString(JsonElement p_151211_0_) {
        if (!p_151211_0_.isJsonPrimitive()) {
            return false;
        }
        boolean bl = p_151211_0_.getAsJsonPrimitive().isString();
        return bl;
    }

    public static boolean isBoolean(JsonObject p_180199_0_, String p_180199_1_) {
        if (!JsonUtils.isJsonPrimitive(p_180199_0_, p_180199_1_)) {
            return false;
        }
        boolean bl = p_180199_0_.getAsJsonPrimitive(p_180199_1_).isBoolean();
        return bl;
    }

    public static boolean isJsonArray(JsonObject p_151202_0_, String p_151202_1_) {
        if (!JsonUtils.hasField(p_151202_0_, p_151202_1_)) {
            return false;
        }
        boolean bl = p_151202_0_.get(p_151202_1_).isJsonArray();
        return bl;
    }

    public static boolean isJsonPrimitive(JsonObject p_151201_0_, String p_151201_1_) {
        if (!JsonUtils.hasField(p_151201_0_, p_151201_1_)) {
            return false;
        }
        boolean bl = p_151201_0_.get(p_151201_1_).isJsonPrimitive();
        return bl;
    }

    public static boolean hasField(JsonObject p_151204_0_, String p_151204_1_) {
        if (p_151204_0_ == null) {
            return false;
        }
        if (p_151204_0_.get(p_151204_1_) == null) return false;
        return true;
    }

    public static String getString(JsonElement p_151206_0_, String p_151206_1_) {
        if (!p_151206_0_.isJsonPrimitive()) throw new JsonSyntaxException("Expected " + p_151206_1_ + " to be a string, was " + JsonUtils.toString(p_151206_0_));
        return p_151206_0_.getAsString();
    }

    public static String getString(JsonObject p_151200_0_, String p_151200_1_) {
        if (!p_151200_0_.has(p_151200_1_)) throw new JsonSyntaxException("Missing " + p_151200_1_ + ", expected to find a string");
        return JsonUtils.getString(p_151200_0_.get(p_151200_1_), p_151200_1_);
    }

    public static String getString(JsonObject p_151219_0_, String p_151219_1_, String p_151219_2_) {
        String string;
        if (p_151219_0_.has(p_151219_1_)) {
            string = JsonUtils.getString(p_151219_0_.get(p_151219_1_), p_151219_1_);
            return string;
        }
        string = p_151219_2_;
        return string;
    }

    public static boolean getBoolean(JsonElement p_151216_0_, String p_151216_1_) {
        if (!p_151216_0_.isJsonPrimitive()) throw new JsonSyntaxException("Expected " + p_151216_1_ + " to be a Boolean, was " + JsonUtils.toString(p_151216_0_));
        return p_151216_0_.getAsBoolean();
    }

    public static boolean getBoolean(JsonObject p_151212_0_, String p_151212_1_) {
        if (!p_151212_0_.has(p_151212_1_)) throw new JsonSyntaxException("Missing " + p_151212_1_ + ", expected to find a Boolean");
        return JsonUtils.getBoolean(p_151212_0_.get(p_151212_1_), p_151212_1_);
    }

    public static boolean getBoolean(JsonObject p_151209_0_, String p_151209_1_, boolean p_151209_2_) {
        boolean bl;
        if (p_151209_0_.has(p_151209_1_)) {
            bl = JsonUtils.getBoolean(p_151209_0_.get(p_151209_1_), p_151209_1_);
            return bl;
        }
        bl = p_151209_2_;
        return bl;
    }

    public static float getFloat(JsonElement p_151220_0_, String p_151220_1_) {
        if (!p_151220_0_.isJsonPrimitive()) throw new JsonSyntaxException("Expected " + p_151220_1_ + " to be a Float, was " + JsonUtils.toString(p_151220_0_));
        if (!p_151220_0_.getAsJsonPrimitive().isNumber()) throw new JsonSyntaxException("Expected " + p_151220_1_ + " to be a Float, was " + JsonUtils.toString(p_151220_0_));
        return p_151220_0_.getAsFloat();
    }

    public static float getFloat(JsonObject p_151217_0_, String p_151217_1_) {
        if (!p_151217_0_.has(p_151217_1_)) throw new JsonSyntaxException("Missing " + p_151217_1_ + ", expected to find a Float");
        return JsonUtils.getFloat(p_151217_0_.get(p_151217_1_), p_151217_1_);
    }

    public static float getFloat(JsonObject p_151221_0_, String p_151221_1_, float p_151221_2_) {
        float f;
        if (p_151221_0_.has(p_151221_1_)) {
            f = JsonUtils.getFloat(p_151221_0_.get(p_151221_1_), p_151221_1_);
            return f;
        }
        f = p_151221_2_;
        return f;
    }

    public static int getInt(JsonElement p_151215_0_, String p_151215_1_) {
        if (!p_151215_0_.isJsonPrimitive()) throw new JsonSyntaxException("Expected " + p_151215_1_ + " to be a Int, was " + JsonUtils.toString(p_151215_0_));
        if (!p_151215_0_.getAsJsonPrimitive().isNumber()) throw new JsonSyntaxException("Expected " + p_151215_1_ + " to be a Int, was " + JsonUtils.toString(p_151215_0_));
        return p_151215_0_.getAsInt();
    }

    public static int getInt(JsonObject p_151203_0_, String p_151203_1_) {
        if (!p_151203_0_.has(p_151203_1_)) throw new JsonSyntaxException("Missing " + p_151203_1_ + ", expected to find a Int");
        return JsonUtils.getInt(p_151203_0_.get(p_151203_1_), p_151203_1_);
    }

    public static int getInt(JsonObject p_151208_0_, String p_151208_1_, int p_151208_2_) {
        int n;
        if (p_151208_0_.has(p_151208_1_)) {
            n = JsonUtils.getInt(p_151208_0_.get(p_151208_1_), p_151208_1_);
            return n;
        }
        n = p_151208_2_;
        return n;
    }

    public static JsonObject getJsonObject(JsonElement p_151210_0_, String p_151210_1_) {
        if (!p_151210_0_.isJsonObject()) throw new JsonSyntaxException("Expected " + p_151210_1_ + " to be a JsonObject, was " + JsonUtils.toString(p_151210_0_));
        return p_151210_0_.getAsJsonObject();
    }

    public static JsonObject getJsonObject(JsonObject base, String key) {
        if (!base.has(key)) throw new JsonSyntaxException("Missing " + key + ", expected to find a JsonObject");
        return JsonUtils.getJsonObject(base.get(key), key);
    }

    public static JsonObject getJsonObject(JsonObject p_151218_0_, String p_151218_1_, JsonObject p_151218_2_) {
        JsonObject jsonObject;
        if (p_151218_0_.has(p_151218_1_)) {
            jsonObject = JsonUtils.getJsonObject(p_151218_0_.get(p_151218_1_), p_151218_1_);
            return jsonObject;
        }
        jsonObject = p_151218_2_;
        return jsonObject;
    }

    public static JsonArray getJsonArray(JsonElement p_151207_0_, String p_151207_1_) {
        if (!p_151207_0_.isJsonArray()) throw new JsonSyntaxException("Expected " + p_151207_1_ + " to be a JsonArray, was " + JsonUtils.toString(p_151207_0_));
        return p_151207_0_.getAsJsonArray();
    }

    public static JsonArray getJsonArray(JsonObject p_151214_0_, String p_151214_1_) {
        if (!p_151214_0_.has(p_151214_1_)) throw new JsonSyntaxException("Missing " + p_151214_1_ + ", expected to find a JsonArray");
        return JsonUtils.getJsonArray(p_151214_0_.get(p_151214_1_), p_151214_1_);
    }

    public static JsonArray getJsonArray(JsonObject p_151213_0_, String p_151213_1_, JsonArray p_151213_2_) {
        JsonArray jsonArray;
        if (p_151213_0_.has(p_151213_1_)) {
            jsonArray = JsonUtils.getJsonArray(p_151213_0_.get(p_151213_1_), p_151213_1_);
            return jsonArray;
        }
        jsonArray = p_151213_2_;
        return jsonArray;
    }

    public static String toString(JsonElement p_151222_0_) {
        String s = StringUtils.abbreviateMiddle(String.valueOf(p_151222_0_), "...", 10);
        if (p_151222_0_ == null) {
            return "null (missing)";
        }
        if (p_151222_0_.isJsonNull()) {
            return "null (json)";
        }
        if (p_151222_0_.isJsonArray()) {
            return "an array (" + s + ")";
        }
        if (p_151222_0_.isJsonObject()) {
            return "an object (" + s + ")";
        }
        if (!p_151222_0_.isJsonPrimitive()) return s;
        JsonPrimitive jsonprimitive = p_151222_0_.getAsJsonPrimitive();
        if (jsonprimitive.isNumber()) {
            return "a number (" + s + ")";
        }
        if (!jsonprimitive.isBoolean()) return s;
        return "a boolean (" + s + ")";
    }
}

