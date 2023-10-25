package com.google.gson;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public final class JsonParser {
  public JsonElement parse(String json) throws JsonSyntaxException {
    return parse(new StringReader(json));
  }
  
  public JsonElement parse(Reader json) throws JsonIOException, JsonSyntaxException {
    try {
      JsonReader jsonReader = new JsonReader(json);
      JsonElement element = parse(jsonReader);
      if (!element.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT)
        throw new JsonSyntaxException("Did not consume the entire document."); 
      return element;
    } catch (MalformedJsonException e) {
      throw new JsonSyntaxException(e);
    } catch (IOException e) {
      throw new JsonIOException(e);
    } catch (NumberFormatException e) {
      throw new JsonSyntaxException(e);
    } 
  }
  
  public JsonElement parse(JsonReader json) throws JsonIOException, JsonSyntaxException {
    boolean lenient = json.isLenient();
    json.setLenient(true);
    try {
      return Streams.parse(json);
    } catch (StackOverflowError e) {
      throw new JsonParseException("Failed parsing JSON source: " + json + " to Json", e);
    } catch (OutOfMemoryError e) {
      throw new JsonParseException("Failed parsing JSON source: " + json + " to Json", e);
    } finally {
      json.setLenient(lenient);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\JsonParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */