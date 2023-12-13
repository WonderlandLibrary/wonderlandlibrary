package net.minecraft.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;

public class ChatStyle {
   private EnumChatFormatting color;
   private ChatStyle parentStyle;
   private static final ChatStyle rootStyle = new ChatStyle() {
      public ChatStyle setStrikethrough(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      public String toString() {
         return "Style.ROOT";
      }

      public ChatStyle setItalic(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      public HoverEvent getChatHoverEvent() {
         return null;
      }

      public EnumChatFormatting getColor() {
         return null;
      }

      public ChatStyle setChatClickEvent(ClickEvent var1) {
         throw new UnsupportedOperationException();
      }

      public String getInsertion() {
         return null;
      }

      public ChatStyle setParentStyle(ChatStyle var1) {
         throw new UnsupportedOperationException();
      }

      public ChatStyle setColor(EnumChatFormatting var1) {
         throw new UnsupportedOperationException();
      }

      public ChatStyle createShallowCopy() {
         return this;
      }

      public boolean getItalic() {
         return false;
      }

      public ChatStyle setBold(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      public ChatStyle setUnderlined(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      public boolean getUnderlined() {
         return false;
      }

      public ChatStyle setObfuscated(Boolean var1) {
         throw new UnsupportedOperationException();
      }

      public ClickEvent getChatClickEvent() {
         return null;
      }

      public ChatStyle setChatHoverEvent(HoverEvent var1) {
         throw new UnsupportedOperationException();
      }

      public ChatStyle createDeepCopy() {
         return this;
      }

      public boolean getBold() {
         return false;
      }

      public String getFormattingCode() {
         return "";
      }

      public boolean getStrikethrough() {
         return false;
      }

      public boolean getObfuscated() {
         return false;
      }
   };
   private Boolean underlined;
   private Boolean italic;
   private HoverEvent chatHoverEvent;
   private ClickEvent chatClickEvent;
   private Boolean obfuscated;
   private String insertion;
   private Boolean bold;
   private Boolean strikethrough;

   static ClickEvent access$16(ChatStyle var0) {
      return var0.chatClickEvent;
   }

   static EnumChatFormatting access$14(ChatStyle var0) {
      return var0.color;
   }

   public ChatStyle setUnderlined(Boolean var1) {
      this.underlined = var1;
      return this;
   }

   static void access$2(ChatStyle var0, Boolean var1) {
      var0.underlined = var1;
   }

   public ChatStyle setItalic(Boolean var1) {
      this.italic = var1;
      return this;
   }

   static Boolean access$12(ChatStyle var0) {
      return var0.strikethrough;
   }

   static void access$1(ChatStyle var0, Boolean var1) {
      var0.italic = var1;
   }

   public ChatStyle setInsertion(String var1) {
      this.insertion = var1;
      return this;
   }

   static void access$8(ChatStyle var0, HoverEvent var1) {
      var0.chatHoverEvent = var1;
   }

   public ChatStyle setStrikethrough(Boolean var1) {
      this.strikethrough = var1;
      return this;
   }

   public int hashCode() {
      int var1 = this.color.hashCode();
      var1 = 31 * var1 + this.bold.hashCode();
      var1 = 31 * var1 + this.italic.hashCode();
      var1 = 31 * var1 + this.underlined.hashCode();
      var1 = 31 * var1 + this.strikethrough.hashCode();
      var1 = 31 * var1 + this.obfuscated.hashCode();
      var1 = 31 * var1 + this.chatClickEvent.hashCode();
      var1 = 31 * var1 + this.chatHoverEvent.hashCode();
      var1 = 31 * var1 + this.insertion.hashCode();
      return var1;
   }

   static Boolean access$13(ChatStyle var0) {
      return var0.obfuscated;
   }

   public String getFormattingCode() {
      if (this != false) {
         return this.parentStyle != null ? this.parentStyle.getFormattingCode() : "";
      } else {
         StringBuilder var1 = new StringBuilder();
         if (this.getColor() != null) {
            var1.append(this.getColor());
         }

         if (this != false) {
            var1.append(EnumChatFormatting.BOLD);
         }

         if (this != false) {
            var1.append(EnumChatFormatting.ITALIC);
         }

         if (this != false) {
            var1.append(EnumChatFormatting.UNDERLINE);
         }

         if (this != false) {
            var1.append(EnumChatFormatting.OBFUSCATED);
         }

         if (this != false) {
            var1.append(EnumChatFormatting.STRIKETHROUGH);
         }

         return String.valueOf(var1);
      }
   }

   public ChatStyle setColor(EnumChatFormatting var1) {
      this.color = var1;
      return this;
   }

   static void access$5(ChatStyle var0, EnumChatFormatting var1) {
      var0.color = var1;
   }

   public String getInsertion() {
      return this.insertion == null ? this.getParent().getInsertion() : this.insertion;
   }

   public String toString() {
      return "Style{hasParent=" + (this.parentStyle != null) + ", color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", obfuscated=" + this.obfuscated + ", clickEvent=" + this.getChatClickEvent() + ", hoverEvent=" + this.getChatHoverEvent() + ", insertion=" + this.getInsertion() + '}';
   }

   public ClickEvent getChatClickEvent() {
      return this.chatClickEvent == null ? this.getParent().getChatClickEvent() : this.chatClickEvent;
   }

   public ChatStyle setParentStyle(ChatStyle var1) {
      this.parentStyle = var1;
      return this;
   }

   public EnumChatFormatting getColor() {
      return this.color == null ? this.getParent().getColor() : this.color;
   }

   public ChatStyle createDeepCopy() {
      ChatStyle var1 = new ChatStyle();
      var1.setBold(this.getBold());
      var1.setItalic(this.getItalic());
      var1.setStrikethrough(this.getStrikethrough());
      var1.setUnderlined(this.getUnderlined());
      var1.setObfuscated(this.getObfuscated());
      var1.setColor(this.getColor());
      var1.setChatClickEvent(this.getChatClickEvent());
      var1.setChatHoverEvent(this.getChatHoverEvent());
      var1.setInsertion(this.getInsertion());
      return var1;
   }

   static void access$6(ChatStyle var0, String var1) {
      var0.insertion = var1;
   }

   public HoverEvent getChatHoverEvent() {
      return this.chatHoverEvent == null ? this.getParent().getChatHoverEvent() : this.chatHoverEvent;
   }

   static Boolean access$11(ChatStyle var0) {
      return var0.underlined;
   }

   static Boolean access$9(ChatStyle var0) {
      return var0.bold;
   }

   private ChatStyle getParent() {
      return this.parentStyle == null ? rootStyle : this.parentStyle;
   }

   static void access$3(ChatStyle var0, Boolean var1) {
      var0.strikethrough = var1;
   }

   static String access$15(ChatStyle var0) {
      return var0.insertion;
   }

   public ChatStyle setChatClickEvent(ClickEvent var1) {
      this.chatClickEvent = var1;
      return this;
   }

   static void access$7(ChatStyle var0, ClickEvent var1) {
      var0.chatClickEvent = var1;
   }

   public ChatStyle createShallowCopy() {
      ChatStyle var1 = new ChatStyle();
      var1.bold = this.bold;
      var1.italic = this.italic;
      var1.strikethrough = this.strikethrough;
      var1.underlined = this.underlined;
      var1.obfuscated = this.obfuscated;
      var1.color = this.color;
      var1.chatClickEvent = this.chatClickEvent;
      var1.chatHoverEvent = this.chatHoverEvent;
      var1.parentStyle = this.parentStyle;
      var1.insertion = this.insertion;
      return var1;
   }

   public ChatStyle setObfuscated(Boolean var1) {
      this.obfuscated = var1;
      return this;
   }

   public ChatStyle setBold(Boolean var1) {
      this.bold = var1;
      return this;
   }

   static HoverEvent access$17(ChatStyle var0) {
      return var0.chatHoverEvent;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ChatStyle)) {
         return false;
      } else {
         ChatStyle var3 = (ChatStyle)var1;
         this.getBold();
         boolean var2;
         if (var3 == null && this.getColor() == var3.getColor()) {
            this.getItalic();
            if (var3 == null) {
               this.getObfuscated();
               if (var3 == null) {
                  this.getStrikethrough();
                  if (var3 == null) {
                     this.getUnderlined();
                     if (var3 == null) {
                        label71: {
                           if (this.getChatClickEvent() != null) {
                              if (!this.getChatClickEvent().equals(var3.getChatClickEvent())) {
                                 break label71;
                              }
                           } else if (var3.getChatClickEvent() != null) {
                              break label71;
                           }

                           if (this.getChatHoverEvent() != null) {
                              if (!this.getChatHoverEvent().equals(var3.getChatHoverEvent())) {
                                 break label71;
                              }
                           } else if (var3.getChatHoverEvent() != null) {
                              break label71;
                           }

                           if (this.getInsertion() != null) {
                              if (!this.getInsertion().equals(var3.getInsertion())) {
                                 break label71;
                              }
                           } else if (var3.getInsertion() != null) {
                              break label71;
                           }

                           var2 = true;
                           return var2;
                        }
                     }
                  }
               }
            }
         }

         var2 = false;
         return var2;
      }
   }

   static void access$0(ChatStyle var0, Boolean var1) {
      var0.bold = var1;
   }

   public ChatStyle setChatHoverEvent(HoverEvent var1) {
      this.chatHoverEvent = var1;
      return this;
   }

   static Boolean access$10(ChatStyle var0) {
      return var0.italic;
   }

   static void access$4(ChatStyle var0, Boolean var1) {
      var0.obfuscated = var1;
   }

   public static class Serializer implements JsonSerializer, JsonDeserializer {
      public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         return this.deserialize(var1, var2, var3);
      }

      public ChatStyle deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
         if (var1.isJsonObject()) {
            ChatStyle var4 = new ChatStyle();
            JsonObject var5 = var1.getAsJsonObject();
            if (var5 == null) {
               return null;
            } else {
               if (var5.has("bold")) {
                  ChatStyle.access$0(var4, var5.get("bold").getAsBoolean());
               }

               if (var5.has("italic")) {
                  ChatStyle.access$1(var4, var5.get("italic").getAsBoolean());
               }

               if (var5.has("underlined")) {
                  ChatStyle.access$2(var4, var5.get("underlined").getAsBoolean());
               }

               if (var5.has("strikethrough")) {
                  ChatStyle.access$3(var4, var5.get("strikethrough").getAsBoolean());
               }

               if (var5.has("obfuscated")) {
                  ChatStyle.access$4(var4, var5.get("obfuscated").getAsBoolean());
               }

               if (var5.has("color")) {
                  ChatStyle.access$5(var4, (EnumChatFormatting)var3.deserialize(var5.get("color"), EnumChatFormatting.class));
               }

               if (var5.has("insertion")) {
                  ChatStyle.access$6(var4, var5.get("insertion").getAsString());
               }

               JsonObject var6;
               JsonPrimitive var7;
               if (var5.has("clickEvent")) {
                  var6 = var5.getAsJsonObject("clickEvent");
                  if (var6 != null) {
                     var7 = var6.getAsJsonPrimitive("action");
                     ClickEvent.Action var8 = var7 == null ? null : ClickEvent.Action.getValueByCanonicalName(var7.getAsString());
                     JsonPrimitive var9 = var6.getAsJsonPrimitive("value");
                     String var10 = var9 == null ? null : var9.getAsString();
                     if (var8 != null && var10 != null && var8.shouldAllowInChat()) {
                        ChatStyle.access$7(var4, new ClickEvent(var8, var10));
                     }
                  }
               }

               if (var5.has("hoverEvent")) {
                  var6 = var5.getAsJsonObject("hoverEvent");
                  if (var6 != null) {
                     var7 = var6.getAsJsonPrimitive("action");
                     HoverEvent.Action var12 = var7 == null ? null : HoverEvent.Action.getValueByCanonicalName(var7.getAsString());
                     IChatComponent var13 = (IChatComponent)var3.deserialize(var6.get("value"), IChatComponent.class);
                     if (var12 != null && var13 != null && var12.shouldAllowInChat()) {
                        ChatStyle.access$8(var4, new HoverEvent(var12, var13));
                     }
                  }
               }

               return var4;
            }
         } else {
            return null;
         }
      }

      public JsonElement serialize(Object var1, Type var2, JsonSerializationContext var3) {
         return this.serialize((ChatStyle)var1, var2, var3);
      }

      public JsonElement serialize(ChatStyle var1, Type var2, JsonSerializationContext var3) {
         if (var1.isEmpty()) {
            return null;
         } else {
            JsonObject var4 = new JsonObject();
            if (ChatStyle.access$9(var1) != null) {
               var4.addProperty("bold", ChatStyle.access$9(var1));
            }

            if (ChatStyle.access$10(var1) != null) {
               var4.addProperty("italic", ChatStyle.access$10(var1));
            }

            if (ChatStyle.access$11(var1) != null) {
               var4.addProperty("underlined", ChatStyle.access$11(var1));
            }

            if (ChatStyle.access$12(var1) != null) {
               var4.addProperty("strikethrough", ChatStyle.access$12(var1));
            }

            if (ChatStyle.access$13(var1) != null) {
               var4.addProperty("obfuscated", ChatStyle.access$13(var1));
            }

            if (ChatStyle.access$14(var1) != null) {
               var4.add("color", var3.serialize(ChatStyle.access$14(var1)));
            }

            if (ChatStyle.access$15(var1) != null) {
               var4.add("insertion", var3.serialize(ChatStyle.access$15(var1)));
            }

            JsonObject var5;
            if (ChatStyle.access$16(var1) != null) {
               var5 = new JsonObject();
               var5.addProperty("action", ChatStyle.access$16(var1).getAction().getCanonicalName());
               var5.addProperty("value", ChatStyle.access$16(var1).getValue());
               var4.add("clickEvent", var5);
            }

            if (ChatStyle.access$17(var1) != null) {
               var5 = new JsonObject();
               var5.addProperty("action", ChatStyle.access$17(var1).getAction().getCanonicalName());
               var5.add("value", var3.serialize(ChatStyle.access$17(var1).getValue()));
               var4.add("hoverEvent", var5);
            }

            return var4;
         }
      }
   }
}
