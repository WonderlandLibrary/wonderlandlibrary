package net.minecraft.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

public class JsonUtils
{
    /**
     * Does the given JsonObject contain a string field with the given name?
     */
    public static boolean isString(JsonObject object, String key)
    {
        return isJsonPrimitive(object, key) && object.getAsJsonPrimitive(key).isString();
    }

    /**
     * Is the given JsonElement a string?
     */
    public static boolean isString(JsonElement element)
    {
        return element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
    }

    public static boolean isBoolean(JsonObject object, String key)
    {
        return isJsonPrimitive(object, key) && object.getAsJsonPrimitive(key).isBoolean();
    }

    /**
     * Does the given JsonObject contain an array field with the given name?
     */
    public static boolean isJsonArray(JsonObject object, String key)
    {
        return hasField(object, key) && object.get(key).isJsonArray();
    }

    /**
     * Does the given JsonObject contain a field with the given name whose type is primitive (String, Java primitive, or
     * Java primitive wrapper)?
     */
    public static boolean isJsonPrimitive(JsonObject object, String key)
    {
        return hasField(object, key) && object.get(key).isJsonPrimitive();
    }

    /**
     * Does the given JsonObject contain a field with the given name?
     */
    public static boolean hasField(JsonObject object, String key)
    {
        return object != null && object.get(key) != null;
    }

    /**
     * Gets the string value of the given JsonElement.  Expects the second parameter to be the name of the element's
     * field if an error message needs to be thrown.
     */
    public static String getString(JsonElement element, String key)
    {
        if (element.isJsonPrimitive()) return element.getAsString();
        else throw new JsonSyntaxException("Expected " + key + " to be a string, was " + toString(element));
    }

    /**
     * Gets the string value of the field on the JsonObject with the given name.
     */
    public static String getString(JsonObject object, String key)
    {
        if (object.has(key)) return getString(object.get(key), key);
        else throw new JsonSyntaxException("Missing " + key + ", expected to find a string");
    }

    /**
     * Gets the string value of the field on the JsonObject with the given name, or the given default value if the field
     * is missing.
     */
    public static String getString(JsonObject object, String key, String defaultValue)
    {
        return object.has(key) ? getString(object.get(key), key) : defaultValue;
    }

    /**
     * Gets the boolean value of the given JsonElement.  Expects the second parameter to be the name of the element's
     * field if an error message needs to be thrown.
     */
    public static boolean getBoolean(JsonElement element, String key)
    {
        if (element.isJsonPrimitive()) return element.getAsBoolean();
        else throw new JsonSyntaxException("Expected " + key + " to be a Boolean, was " + toString(element));
    }

    /**
     * Gets the boolean value of the field on the JsonObject with the given name.
     */
    public static boolean getBoolean(JsonObject object, String key)
    {
        if (object.has(key)) return getBoolean(object.get(key), key);
        else throw new JsonSyntaxException("Missing " + key + ", expected to find a Boolean");
    }

    /**
     * Gets the boolean value of the field on the JsonObject with the given name, or the given default value if the
     * field is missing.
     */
    public static boolean getBoolean(JsonObject object, String key, boolean defaultValue)
    {
        return object.has(key) ? getBoolean(object.get(key), key) : defaultValue;
    }

    /**
     * Gets the float value of the given JsonElement.  Expects the second parameter to be the name of the element's
     * field if an error message needs to be thrown.
     */
    public static float getFloat(JsonElement element, String key)
    {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) return element.getAsFloat();
        else throw new JsonSyntaxException("Expected " + key + " to be a Float, was " + toString(element));
    }

    /**
     * Gets the float value of the field on the JsonObject with the given name.
     */
    public static float getFloat(JsonObject object, String key)
    {
        if (object.has(key)) return getFloat(object.get(key), key);
        else throw new JsonSyntaxException("Missing " + key + ", expected to find a Float");
    }

    /**
     * Gets the float value of the field on the JsonObject with the given name, or the given default value if the field
     * is missing.
     */
    public static float getFloat(JsonObject object, String key, float defaultValue)
    {
        return object.has(key) ? getFloat(object.get(key), key) : defaultValue;
    }

    /**
     * Gets the integer value of the given JsonElement.  Expects the second parameter to be the name of the element's
     * field if an error message needs to be thrown.
     */
    public static int getInt(JsonElement element, String key)
    {
        if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) return element.getAsInt();
        else throw new JsonSyntaxException("Expected " + key + " to be a Int, was " + toString(element));
    }

    /**
     * Gets the integer value of the field on the JsonObject with the given name.
     */
    public static int getInt(JsonObject object, String key)
    {
        if (object.has(key)) return getInt(object.get(key), key);
        else throw new JsonSyntaxException("Missing " + key + ", expected to find a Int");
    }

    /**
     * Gets the integer value of the field on the JsonObject with the given name, or the given default value if the
     * field is missing.
     */
    public static int getInt(JsonObject object, String key, int defaultValue)
    {
        return object.has(key) ? getInt(object.get(key), key) : defaultValue;
    }

    /**
     * Gets the given JsonElement as a JsonObject.  Expects the second parameter to be the name of the element's field
     * if an error message needs to be thrown.
     */
    public static JsonObject getJsonObject(JsonElement element, String key)
    {
        if (element.isJsonObject()) return element.getAsJsonObject();
        else throw new JsonSyntaxException("Expected " + key + " to be a JsonObject, was " + toString(element));
    }

    public static JsonObject getJsonObject(JsonObject base, String key)
    {
        if (base.has(key)) return getJsonObject(base.get(key), key);
        else throw new JsonSyntaxException("Missing " + key + ", expected to find a JsonObject");
    }

    /**
     * Gets the JsonObject field on the JsonObject with the given name, or the given default value if the field is
     * missing.
     */
    public static JsonObject getJsonObject(JsonObject object, String key, JsonObject defaultValue)
    {
        return object.has(key) ? getJsonObject(object.get(key), key) : defaultValue;
    }

    /**
     * Gets the given JsonElement as a JsonArray.  Expects the second parameter to be the name of the element's field if
     * an error message needs to be thrown.
     */
    public static JsonArray getJsonArray(JsonElement element, String key)
    {
        if (element.isJsonArray()) return element.getAsJsonArray();
        else throw new JsonSyntaxException("Expected " + key + " to be a JsonArray, was " + toString(element));
    }

    /**
     * Gets the JsonArray field on the JsonObject with the given name.
     */
    public static JsonArray getJsonArray(JsonObject object, String key)
    {
        if (object.has(key)) return getJsonArray(object.get(key), key);
        else throw new JsonSyntaxException("Missing " + key + ", expected to find a JsonArray");
    }

    /**
     * Gets the JsonArray field on the JsonObject with the given name, or the given default value if the field is
     * missing.
     */
    public static JsonArray getJsonArray(JsonObject object, String key, JsonArray defaultValue)
    {
        return object.has(key) ? getJsonArray(object.get(key), key) : defaultValue;
    }

    /**
     * Gets a human-readable description of the given JsonElement's type.  For example: "a number (4)"
     */
    public static String toString(JsonElement element)
    {
        String s = org.apache.commons.lang3.StringUtils.abbreviateMiddle(String.valueOf(element), "...", 10);

        if (element == null) return "null (missing)";
        else if (element.isJsonNull()) return "null (json)";
        else if (element.isJsonArray()) return "an array (" + s + ")";
        else if (element.isJsonObject()) return "an object (" + s + ")";
        else
        {
            if (element.isJsonPrimitive())
            {
                JsonPrimitive jsonprimitive = element.getAsJsonPrimitive();
                if (jsonprimitive.isNumber()) return "a number (" + s + ")";
                if (jsonprimitive.isBoolean()) return "a boolean (" + s + ")";
            }

            return s;
        }
    }
}
