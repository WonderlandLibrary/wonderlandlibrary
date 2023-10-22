package me.ratsiel.json;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import me.ratsiel.json.abstracts.JsonValue;
import me.ratsiel.json.model.JsonArray;
import me.ratsiel.json.model.JsonBoolean;
import me.ratsiel.json.model.JsonNull;
import me.ratsiel.json.model.JsonNumber;
import me.ratsiel.json.model.JsonObject;
import me.ratsiel.json.model.JsonString;

public class JsonParser {
   private char[] jsonBuffer;
   private int jsonBufferPosition = 0;

   public JsonValue parse(InputStream inputStream, Class clazz) {
      return (JsonValue)clazz.cast(this.parse(inputStream));
   }

   public JsonValue parse(InputStream inputStream) {
      try {
         this.prepareParser(inputStream);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      this.skipWhiteSpace();
      switch (this.peekChar()) {
         case '"':
            return this.parseJsonString();
         case '#':
         case '$':
         case '%':
         case '&':
         case '\'':
         case '(':
         case ')':
         case '*':
         case ',':
         case '/':
         case ':':
         case ';':
         case '<':
         case '=':
         case '>':
         case '?':
         case '@':
         case 'A':
         case 'B':
         case 'C':
         case 'D':
         case 'F':
         case 'G':
         case 'H':
         case 'I':
         case 'J':
         case 'K':
         case 'L':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'S':
         case 'T':
         case 'U':
         case 'V':
         case 'W':
         case 'X':
         case 'Y':
         case 'Z':
         case '\\':
         case ']':
         case '^':
         case '_':
         case '`':
         case 'a':
         case 'b':
         case 'c':
         case 'd':
         case 'g':
         case 'h':
         case 'i':
         case 'j':
         case 'k':
         case 'l':
         case 'm':
         case 'o':
         case 'p':
         case 'q':
         case 'r':
         case 's':
         case 'u':
         case 'v':
         case 'w':
         case 'x':
         case 'y':
         case 'z':
         default:
            throw new RuntimeException("Could not parse file to Json!");
         case '+':
         case '-':
         case '.':
         case '0':
         case '1':
         case '2':
         case '3':
         case '4':
         case '5':
         case '6':
         case '7':
         case '8':
         case '9':
         case 'E':
         case 'e':
            return this.parseJsonNumber();
         case '[':
            JsonArray jsonArray = this.parseJsonArray();
            this.jsonBuffer = null;
            this.jsonBufferPosition = 0;
            return jsonArray;
         case 'f':
         case 't':
            return this.parseJsonBoolean();
         case 'n':
            return this.parseJsonNull();
         case '{':
            JsonObject jsonObject = this.parseJsonObject();
            this.jsonBuffer = null;
            this.jsonBufferPosition = 0;
            return jsonObject;
      }
   }

   public JsonValue parse(String json, Class clazz) {
      return (JsonValue)clazz.cast(this.parse(json));
   }

   public JsonValue parse(String json) {
      return this.parse((InputStream)(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))));
   }

   public JsonValue parse(File file, Class clazz) throws FileNotFoundException {
      return (JsonValue)clazz.cast(this.parse(file));
   }

   public JsonValue parse(File file) throws FileNotFoundException {
      return this.parse((InputStream)(new FileInputStream(file)));
   }

   public void prepareParser(InputStream inputStream) throws IOException {
      StringBuilder stringBuilder = new StringBuilder();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

      String line;
      while((line = bufferedReader.readLine()) != null) {
         stringBuilder.append(line).append("\n");
      }

      this.jsonBuffer = stringBuilder.toString().toCharArray();
      bufferedReader.close();
   }

   public char parseChar() {
      try {
         return this.jsonBuffer[this.jsonBufferPosition++];
      } catch (IndexOutOfBoundsException var2) {
         return '\uffff';
      }
   }

   public char peekChar() {
      char currentChar = this.parseChar();
      --this.jsonBufferPosition;
      return currentChar;
   }

   public void skipWhiteSpace() {
      while(this.isWhiteSpace()) {
         this.parseChar();
      }

   }

   public JsonObject parseJsonObject() {
      JsonObject jsonObject = new JsonObject();
      this.skipWhiteSpace();
      this.parseChar();
      char currentChar = this.peekChar();

      while(currentChar != '}') {
         this.skipWhiteSpace();
         switch (this.peekChar()) {
            case '"':
               String key = this.parseJsonString().getValue();
               this.skipWhiteSpace();
               currentChar = this.parseChar();
               this.skipWhiteSpace();
               switch (this.peekChar()) {
                  case '"':
                     JsonString jsonString = this.parseJsonString();
                     jsonString.setKey(key);
                     jsonObject.add((JsonValue)jsonString);
                  case '#':
                  case '$':
                  case '%':
                  case '&':
                  case '\'':
                  case '(':
                  case ')':
                  case '*':
                  case ',':
                  case '/':
                  case ':':
                  case ';':
                  case '<':
                  case '=':
                  case '>':
                  case '?':
                  case '@':
                  case 'A':
                  case 'B':
                  case 'C':
                  case 'D':
                  case 'F':
                  case 'G':
                  case 'H':
                  case 'I':
                  case 'J':
                  case 'K':
                  case 'L':
                  case 'M':
                  case 'N':
                  case 'O':
                  case 'P':
                  case 'Q':
                  case 'R':
                  case 'S':
                  case 'T':
                  case 'U':
                  case 'V':
                  case 'W':
                  case 'X':
                  case 'Y':
                  case 'Z':
                  case '\\':
                  case ']':
                  case '^':
                  case '_':
                  case '`':
                  case 'a':
                  case 'b':
                  case 'c':
                  case 'd':
                  case 'g':
                  case 'h':
                  case 'i':
                  case 'j':
                  case 'k':
                  case 'l':
                  case 'm':
                  case 'o':
                  case 'p':
                  case 'q':
                  case 'r':
                  case 's':
                  case 'u':
                  case 'v':
                  case 'w':
                  case 'x':
                  case 'y':
                  case 'z':
                  default:
                     continue;
                  case '+':
                  case '-':
                  case '.':
                  case '0':
                  case '1':
                  case '2':
                  case '3':
                  case '4':
                  case '5':
                  case '6':
                  case '7':
                  case '8':
                  case '9':
                  case 'E':
                  case 'e':
                     JsonNumber jsonNumber = this.parseJsonNumber();
                     jsonNumber.setKey(key);
                     jsonObject.add((JsonValue)jsonNumber);
                     continue;
                  case '[':
                     JsonArray jsonArray = this.parseJsonArray();
                     jsonArray.setKey(key);
                     jsonObject.add((JsonValue)jsonArray);
                     continue;
                  case 'f':
                  case 't':
                     JsonBoolean jsonBoolean = this.parseJsonBoolean();
                     jsonBoolean.setKey(key);
                     jsonObject.add((JsonValue)jsonBoolean);
                     continue;
                  case 'n':
                     JsonNull jsonNull = this.parseJsonNull();
                     jsonNull.setKey(key);
                     jsonObject.add((JsonValue)jsonNull);
                     continue;
                  case '{':
                     JsonObject object = this.parseJsonObject();
                     object.setKey(key);
                     jsonObject.add((JsonValue)object);
                     continue;
               }
            case ',':
            case '}':
               currentChar = this.parseChar();
               break;
            default:
               throw new RuntimeException(String.format("Could not parse JsonObject stuck at Index: %s as Char: %s", this.jsonBufferPosition, this.jsonBuffer[this.jsonBufferPosition]));
         }
      }

      return jsonObject;
   }

   public JsonArray parseJsonArray() {
      JsonArray jsonArray = new JsonArray();
      this.skipWhiteSpace();
      this.parseChar();
      char currentChar = this.peekChar();

      while(currentChar != ']') {
         this.skipWhiteSpace();
         switch (this.peekChar()) {
            case '"':
               jsonArray.add((JsonValue)this.parseJsonString());
               break;
            case '#':
            case '$':
            case '%':
            case '&':
            case '\'':
            case '(':
            case ')':
            case '*':
            case '/':
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '\\':
            case '^':
            case '_':
            case '`':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
            default:
               throw new RuntimeException(String.format("Could not parse JsonArray stuck at Index: %s as Char: %s", this.jsonBufferPosition, this.jsonBuffer[this.jsonBufferPosition]));
            case '+':
            case '-':
            case '.':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case 'E':
            case 'e':
               jsonArray.add((JsonValue)this.parseJsonNumber());
               break;
            case ',':
            case ']':
               currentChar = this.parseChar();
               break;
            case '[':
               jsonArray.add((JsonValue)this.parseJsonArray());
               break;
            case 'f':
            case 't':
               jsonArray.add((JsonValue)this.parseJsonBoolean());
               break;
            case 'n':
               jsonArray.add((JsonValue)this.parseJsonNull());
               break;
            case '{':
               jsonArray.add((JsonValue)this.parseJsonObject());
         }
      }

      return jsonArray;
   }

   public JsonString parseJsonString() {
      JsonString jsonString = new JsonString();
      StringBuilder stringBuilder = new StringBuilder();
      this.skipWhiteSpace();
      this.parseChar();

      for(char currentChar = this.parseChar(); currentChar != '"'; currentChar = this.parseChar()) {
         if (currentChar == '\\') {
            stringBuilder.append(currentChar);
            currentChar = this.parseChar();
         }

         stringBuilder.append(currentChar);
      }

      this.skipWhiteSpace();
      jsonString.setValue(stringBuilder.toString());
      return jsonString;
   }

   public JsonNull parseJsonNull() {
      char currentChar = this.parseChar();
      if (currentChar == 'n' && this.isNextChar('u', 0) && this.isNextChar('l', 1) && this.isNextChar('l', 2)) {
         for(int i = 0; i < 3; ++i) {
            ++this.jsonBufferPosition;
         }

         return new JsonNull();
      } else {
         return null;
      }
   }

   public JsonBoolean parseJsonBoolean() {
      char currentChar = this.parseChar();
      JsonBoolean jsonBoolean = null;
      int i;
      if (currentChar == 't' && this.isNextChar('r', 0) && this.isNextChar('u', 1) && this.isNextChar('e', 2)) {
         for(i = 0; i < 3; ++i) {
            ++this.jsonBufferPosition;
         }

         jsonBoolean = new JsonBoolean();
         jsonBoolean.setValue(true);
      } else if (currentChar == 'f' && this.isNextChar('a', 0) && this.isNextChar('l', 1) && this.isNextChar('s', 2) && this.isNextChar('e', 3)) {
         for(i = 0; i < 4; ++i) {
            ++this.jsonBufferPosition;
         }

         jsonBoolean = new JsonBoolean();
         jsonBoolean.setValue(false);
      }

      return jsonBoolean;
   }

   public JsonNumber parseJsonNumber() {
      StringBuilder stringBuilder = new StringBuilder();

      for(char currentChar = this.parseChar(); this.isNumber(currentChar); currentChar = this.parseChar()) {
         stringBuilder.append(currentChar);
      }

      --this.jsonBufferPosition;
      JsonNumber jsonNumber = new JsonNumber();
      jsonNumber.setValue(stringBuilder.toString());
      return jsonNumber;
   }

   public boolean isWhiteSpace() {
      char currentChar = this.jsonBuffer[this.jsonBufferPosition];
      return currentChar == ' ' || currentChar == '\t' || currentChar == '\r' || currentChar == '\n';
   }

   public boolean isNextChar(char currentChar, int position) {
      for(int i = 0; i < position; ++i) {
         this.parseChar();
      }

      char nextChar = this.peekChar();

      for(int i = 0; i < position; ++i) {
         --this.jsonBufferPosition;
      }

      return nextChar == currentChar;
   }

   public boolean isNumber(char currentChar) {
      char[] numberElements = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', 'E', 'e', '+', '-'};
      char[] var3 = numberElements;
      int var4 = numberElements.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char listChar = var3[var5];
         if (listChar == currentChar) {
            return true;
         }
      }

      return false;
   }
}
