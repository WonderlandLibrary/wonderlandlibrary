/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonPrimitive
 *  com.google.gson.JsonSyntaxException
 *  org.apache.commons.lang3.StringUtils
 */
package net.minecraft.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang3.StringUtils;

public class JsonUtils {
    public static boolean isBoolean(JsonObject jsonObject, String string) {
        return !JsonUtils.isJsonPrimitive(jsonObject, string) ? false : jsonObject.getAsJsonPrimitive(string).isBoolean();
    }

    public static float getFloat(JsonObject jsonObject, String string) {
        if (jsonObject.has(string)) {
            return JsonUtils.getFloat(jsonObject.get(string), string);
        }
        throw new JsonSyntaxException("Missing " + string + ", expected to find a Float");
    }

    public static boolean getBoolean(JsonObject jsonObject, String string, boolean bl) {
        return jsonObject.has(string) ? JsonUtils.getBoolean(jsonObject.get(string), string) : bl;
    }

    public static JsonArray getJsonArray(JsonObject jsonObject, String string) {
        if (jsonObject.has(string)) {
            return JsonUtils.getJsonArray(jsonObject.get(string), string);
        }
        throw new JsonSyntaxException("Missing " + string + ", expected to find a JsonArray");
    }

    public static JsonObject getJsonObject(JsonObject jsonObject, String string, JsonObject jsonObject2) {
        return jsonObject.has(string) ? JsonUtils.getJsonObject(jsonObject.get(string), string) : jsonObject2;
    }

    public static boolean isJsonPrimitive(JsonObject jsonObject, String string) {
        return !JsonUtils.hasField(jsonObject, string) ? false : jsonObject.get(string).isJsonPrimitive();
    }

    public static JsonObject getJsonObject(JsonElement jsonElement, String string) {
        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        }
        throw new JsonSyntaxException("Expected " + string + " to be a JsonObject, was " + JsonUtils.toString(jsonElement));
    }

    public static JsonArray getJsonArray(JsonObject jsonObject, String string, JsonArray jsonArray) {
        return jsonObject.has(string) ? JsonUtils.getJsonArray(jsonObject.get(string), string) : jsonArray;
    }

    public static JsonArray getJsonArray(JsonElement jsonElement, String string) {
        if (jsonElement.isJsonArray()) {
            return jsonElement.getAsJsonArray();
        }
        throw new JsonSyntaxException("Expected " + string + " to be a JsonArray, was " + JsonUtils.toString(jsonElement));
    }

    public static JsonObject getJsonObject(JsonObject jsonObject, String string) {
        if (jsonObject.has(string)) {
            return JsonUtils.getJsonObject(jsonObject.get(string), string);
        }
        throw new JsonSyntaxException("Missing " + string + ", expected to find a JsonObject");
    }

    public static boolean getBoolean(JsonObject jsonObject, String string) {
        if (jsonObject.has(string)) {
            return JsonUtils.getBoolean(jsonObject.get(string), string);
        }
        throw new JsonSyntaxException("Missing " + string + ", expected to find a Boolean");
    }

    public static String getString(JsonObject jsonObject, String string, String string2) {
        return jsonObject.has(string) ? JsonUtils.getString(jsonObject.get(string), string) : string2;
    }

    public static boolean hasField(JsonObject jsonObject, String string) {
        return jsonObject == null ? false : jsonObject.get(string) != null;
    }

    public static String getString(JsonObject jsonObject, String string) {
        if (jsonObject.has(string)) {
            return JsonUtils.getString(jsonObject.get(string), string);
        }
        throw new JsonSyntaxException("Missing " + string + ", expected to find a string");
    }

    public static int getInt(JsonObject jsonObject, String string, int n) {
        return jsonObject.has(string) ? JsonUtils.getInt(jsonObject.get(string), string) : n;
    }

    public static float getFloat(JsonElement jsonElement, String string) {
        if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber()) {
            return jsonElement.getAsFloat();
        }
        throw new JsonSyntaxException("Expected " + string + " to be a Float, was " + JsonUtils.toString(jsonElement));
    }

    public static boolean isString(JsonElement jsonElement) {
        return !jsonElement.isJsonPrimitive() ? false : jsonElement.getAsJsonPrimitive().isString();
    }

    public static boolean isJsonArray(JsonObject jsonObject, String string) {
        return !JsonUtils.hasField(jsonObject, string) ? false : jsonObject.get(string).isJsonArray();
    }

    public static float getFloat(JsonObject jsonObject, String string, float f) {
        return jsonObject.has(string) ? JsonUtils.getFloat(jsonObject.get(string), string) : f;
    }

    public static String toString(JsonElement jsonElement) {
        String string = StringUtils.abbreviateMiddle((String)String.valueOf(jsonElement), (String)"...", (int)10);
        if (jsonElement == null) {
            return "null (missing)";
        }
        if (jsonElement.isJsonNull()) {
            return "null (json)";
        }
        if (jsonElement.isJsonArray()) {
            return "an array (" + string + ")";
        }
        if (jsonElement.isJsonObject()) {
            return "an object (" + string + ")";
        }
        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (jsonPrimitive.isNumber()) {
                return "a number (" + string + ")";
            }
            if (jsonPrimitive.isBoolean()) {
                return "a boolean (" + string + ")";
            }
        }
        return string;
    }

    public static String getString(JsonElement jsonElement, String string) {
        if (jsonElement.isJsonPrimitive()) {
            return jsonElement.getAsString();
        }
        throw new JsonSyntaxException("Expected " + string + " to be a string, was " + JsonUtils.toString(jsonElement));
    }

    public static boolean getBoolean(JsonElement jsonElement, String string) {
        if (jsonElement.isJsonPrimitive()) {
            return jsonElement.getAsBoolean();
        }
        throw new JsonSyntaxException("Expected " + string + " to be a Boolean, was " + JsonUtils.toString(jsonElement));
    }

    public static boolean isString(JsonObject jsonObject, String string) {
        return !JsonUtils.isJsonPrimitive(jsonObject, string) ? false : jsonObject.getAsJsonPrimitive(string).isString();
    }

    public static int getInt(JsonObject jsonObject, String string) {
        if (jsonObject.has(string)) {
            return JsonUtils.getInt(jsonObject.get(string), string);
        }
        throw new JsonSyntaxException("Missing " + string + ", expected to find a Int");
    }

    public static int getInt(JsonElement jsonElement, String string) {
        if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber()) {
            return jsonElement.getAsInt();
        }
        throw new JsonSyntaxException("Expected " + string + " to be a Int, was " + JsonUtils.toString(jsonElement));
    }
}

