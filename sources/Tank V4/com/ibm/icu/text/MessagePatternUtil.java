package com.ibm.icu.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class MessagePatternUtil {
   private MessagePatternUtil() {
   }

   public static MessagePatternUtil.MessageNode buildMessageNode(String var0) {
      return buildMessageNode(new MessagePattern(var0));
   }

   public static MessagePatternUtil.MessageNode buildMessageNode(MessagePattern var0) {
      int var1 = var0.countParts() - 1;
      if (var1 < 0) {
         throw new IllegalArgumentException("The MessagePattern is empty");
      } else if (var0.getPartType(0) != MessagePattern.Part.Type.MSG_START) {
         throw new IllegalArgumentException("The MessagePattern does not represent a MessageFormat pattern");
      } else {
         return buildMessageNode(var0, 0, var1);
      }
   }

   private static MessagePatternUtil.MessageNode buildMessageNode(MessagePattern var0, int var1, int var2) {
      int var3 = var0.getPart(var1).getLimit();
      MessagePatternUtil.MessageNode var4 = new MessagePatternUtil.MessageNode();
      int var5 = var1 + 1;

      while(true) {
         MessagePattern.Part var6 = var0.getPart(var5);
         int var7 = var6.getIndex();
         if (var3 < var7) {
            MessagePatternUtil.MessageNode.access$500(var4, new MessagePatternUtil.TextNode(var0.getPatternString().substring(var3, var7)));
         }

         if (var5 == var2) {
            return MessagePatternUtil.MessageNode.access$700(var4);
         }

         MessagePattern.Part.Type var8 = var6.getType();
         if (var8 == MessagePattern.Part.Type.ARG_START) {
            int var9 = var0.getLimitPartIndex(var5);
            MessagePatternUtil.MessageNode.access$500(var4, buildArgNode(var0, var5, var9));
            var5 = var9;
            var6 = var0.getPart(var9);
         } else if (var8 == MessagePattern.Part.Type.REPLACE_NUMBER) {
            MessagePatternUtil.MessageNode.access$500(var4, MessagePatternUtil.MessageContentsNode.access$600());
         }

         var3 = var6.getLimit();
         ++var5;
      }
   }

   private static MessagePatternUtil.ArgNode buildArgNode(MessagePattern var0, int var1, int var2) {
      MessagePatternUtil.ArgNode var3 = MessagePatternUtil.ArgNode.access$800();
      MessagePattern.Part var4 = var0.getPart(var1);
      MessagePattern.ArgType var5 = MessagePatternUtil.ArgNode.access$902(var3, var4.getArgType());
      ++var1;
      var4 = var0.getPart(var1);
      MessagePatternUtil.ArgNode.access$1002(var3, var0.getSubstring(var4));
      if (var4.getType() == MessagePattern.Part.Type.ARG_NUMBER) {
         MessagePatternUtil.ArgNode.access$1102(var3, var4.getValue());
      }

      ++var1;
      switch(var5) {
      case SIMPLE:
         MessagePatternUtil.ArgNode.access$1202(var3, var0.getSubstring(var0.getPart(var1++)));
         if (var1 < var2) {
            MessagePatternUtil.ArgNode.access$1302(var3, var0.getSubstring(var0.getPart(var1)));
         }
         break;
      case CHOICE:
         MessagePatternUtil.ArgNode.access$1202(var3, "choice");
         MessagePatternUtil.ArgNode.access$1402(var3, buildChoiceStyleNode(var0, var1, var2));
         break;
      case PLURAL:
         MessagePatternUtil.ArgNode.access$1202(var3, "plural");
         MessagePatternUtil.ArgNode.access$1402(var3, buildPluralStyleNode(var0, var1, var2, var5));
         break;
      case SELECT:
         MessagePatternUtil.ArgNode.access$1202(var3, "select");
         MessagePatternUtil.ArgNode.access$1402(var3, buildSelectStyleNode(var0, var1, var2));
         break;
      case SELECTORDINAL:
         MessagePatternUtil.ArgNode.access$1202(var3, "selectordinal");
         MessagePatternUtil.ArgNode.access$1402(var3, buildPluralStyleNode(var0, var1, var2, var5));
      }

      return var3;
   }

   private static MessagePatternUtil.ComplexArgStyleNode buildChoiceStyleNode(MessagePattern var0, int var1, int var2) {
      MessagePatternUtil.ComplexArgStyleNode var3;
      int var8;
      for(var3 = new MessagePatternUtil.ComplexArgStyleNode(MessagePattern.ArgType.CHOICE); var1 < var2; var1 = var8 + 1) {
         int var4 = var1;
         MessagePattern.Part var5 = var0.getPart(var1);
         double var6 = var0.getNumericValue(var5);
         var1 += 2;
         var8 = var0.getLimitPartIndex(var1);
         MessagePatternUtil.VariantNode var9 = new MessagePatternUtil.VariantNode();
         MessagePatternUtil.VariantNode.access$1702(var9, var0.getSubstring(var0.getPart(var4 + 1)));
         MessagePatternUtil.VariantNode.access$1802(var9, var6);
         MessagePatternUtil.VariantNode.access$1902(var9, buildMessageNode(var0, var1, var8));
         MessagePatternUtil.ComplexArgStyleNode.access$2000(var3, var9);
      }

      return MessagePatternUtil.ComplexArgStyleNode.access$2100(var3);
   }

   private static MessagePatternUtil.ComplexArgStyleNode buildPluralStyleNode(MessagePattern var0, int var1, int var2, MessagePattern.ArgType var3) {
      MessagePatternUtil.ComplexArgStyleNode var4 = new MessagePatternUtil.ComplexArgStyleNode(var3);
      MessagePattern.Part var5 = var0.getPart(var1);
      if (var5.getType().hasNumericValue()) {
         MessagePatternUtil.ComplexArgStyleNode.access$2202(var4, true);
         MessagePatternUtil.ComplexArgStyleNode.access$2302(var4, var0.getNumericValue(var5));
         ++var1;
      }

      while(var1 < var2) {
         MessagePattern.Part var6 = var0.getPart(var1++);
         double var7 = -1.23456789E8D;
         MessagePattern.Part var9 = var0.getPart(var1);
         if (var9.getType().hasNumericValue()) {
            var7 = var0.getNumericValue(var9);
            ++var1;
         }

         int var10 = var0.getLimitPartIndex(var1);
         MessagePatternUtil.VariantNode var11 = new MessagePatternUtil.VariantNode();
         MessagePatternUtil.VariantNode.access$1702(var11, var0.getSubstring(var6));
         MessagePatternUtil.VariantNode.access$1802(var11, var7);
         MessagePatternUtil.VariantNode.access$1902(var11, buildMessageNode(var0, var1, var10));
         MessagePatternUtil.ComplexArgStyleNode.access$2000(var4, var11);
         var1 = var10 + 1;
      }

      return MessagePatternUtil.ComplexArgStyleNode.access$2100(var4);
   }

   private static MessagePatternUtil.ComplexArgStyleNode buildSelectStyleNode(MessagePattern var0, int var1, int var2) {
      MessagePatternUtil.ComplexArgStyleNode var3;
      int var5;
      for(var3 = new MessagePatternUtil.ComplexArgStyleNode(MessagePattern.ArgType.SELECT); var1 < var2; var1 = var5 + 1) {
         MessagePattern.Part var4 = var0.getPart(var1++);
         var5 = var0.getLimitPartIndex(var1);
         MessagePatternUtil.VariantNode var6 = new MessagePatternUtil.VariantNode();
         MessagePatternUtil.VariantNode.access$1702(var6, var0.getSubstring(var4));
         MessagePatternUtil.VariantNode.access$1902(var6, buildMessageNode(var0, var1, var5));
         MessagePatternUtil.ComplexArgStyleNode.access$2000(var3, var6);
      }

      return MessagePatternUtil.ComplexArgStyleNode.access$2100(var3);
   }

   public static class VariantNode extends MessagePatternUtil.Node {
      private String selector;
      private double numericValue;
      private MessagePatternUtil.MessageNode msgNode;

      public String getSelector() {
         return this.selector;
      }

      public double getSelectorValue() {
         return this.numericValue;
      }

      public MessagePatternUtil.MessageNode getMessage() {
         return this.msgNode;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         if (this != false) {
            var1.append(this.numericValue).append(" (").append(this.selector).append(") {");
         } else {
            var1.append(this.selector).append(" {");
         }

         return var1.append(this.msgNode.toString()).append('}').toString();
      }

      private VariantNode() {
         super(null);
         this.numericValue = -1.23456789E8D;
      }

      VariantNode(Object var1) {
         this();
      }

      static String access$1702(MessagePatternUtil.VariantNode var0, String var1) {
         return var0.selector = var1;
      }

      static double access$1802(MessagePatternUtil.VariantNode var0, double var1) {
         return var0.numericValue = var1;
      }

      static MessagePatternUtil.MessageNode access$1902(MessagePatternUtil.VariantNode var0, MessagePatternUtil.MessageNode var1) {
         return var0.msgNode = var1;
      }
   }

   public static class ComplexArgStyleNode extends MessagePatternUtil.Node {
      private MessagePattern.ArgType argType;
      private double offset;
      private boolean explicitOffset;
      private List list;

      public MessagePattern.ArgType getArgType() {
         return this.argType;
      }

      public boolean hasExplicitOffset() {
         return this.explicitOffset;
      }

      public double getOffset() {
         return this.offset;
      }

      public List getVariants() {
         return this.list;
      }

      public MessagePatternUtil.VariantNode getVariantsByType(List var1, List var2) {
         if (var1 != null) {
            var1.clear();
         }

         var2.clear();
         MessagePatternUtil.VariantNode var3 = null;
         Iterator var4 = this.list.iterator();

         while(var4.hasNext()) {
            MessagePatternUtil.VariantNode var5 = (MessagePatternUtil.VariantNode)var4.next();
            if (var5.isSelectorNumeric()) {
               var1.add(var5);
            } else if ("other".equals(var5.getSelector())) {
               if (var3 == null) {
                  var3 = var5;
               }
            } else {
               var2.add(var5);
            }
         }

         return var3;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append('(').append(this.argType.toString()).append(" style) ");
         if (this.hasExplicitOffset()) {
            var1.append("offset:").append(this.offset).append(' ');
         }

         return var1.append(this.list.toString()).toString();
      }

      private ComplexArgStyleNode(MessagePattern.ArgType var1) {
         super(null);
         this.list = new ArrayList();
         this.argType = var1;
      }

      private void addVariant(MessagePatternUtil.VariantNode var1) {
         this.list.add(var1);
      }

      private MessagePatternUtil.ComplexArgStyleNode freeze() {
         this.list = Collections.unmodifiableList(this.list);
         return this;
      }

      ComplexArgStyleNode(MessagePattern.ArgType var1, Object var2) {
         this(var1);
      }

      static void access$2000(MessagePatternUtil.ComplexArgStyleNode var0, MessagePatternUtil.VariantNode var1) {
         var0.addVariant(var1);
      }

      static MessagePatternUtil.ComplexArgStyleNode access$2100(MessagePatternUtil.ComplexArgStyleNode var0) {
         return var0.freeze();
      }

      static boolean access$2202(MessagePatternUtil.ComplexArgStyleNode var0, boolean var1) {
         return var0.explicitOffset = var1;
      }

      static double access$2302(MessagePatternUtil.ComplexArgStyleNode var0, double var1) {
         return var0.offset = var1;
      }
   }

   public static class ArgNode extends MessagePatternUtil.MessageContentsNode {
      private MessagePattern.ArgType argType;
      private String name;
      private int number = -1;
      private String typeName;
      private String style;
      private MessagePatternUtil.ComplexArgStyleNode complexStyle;

      public MessagePattern.ArgType getArgType() {
         return this.argType;
      }

      public String getName() {
         return this.name;
      }

      public int getNumber() {
         return this.number;
      }

      public String getTypeName() {
         return this.typeName;
      }

      public String getSimpleStyle() {
         return this.style;
      }

      public MessagePatternUtil.ComplexArgStyleNode getComplexStyle() {
         return this.complexStyle;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append('{').append(this.name);
         if (this.argType != MessagePattern.ArgType.NONE) {
            var1.append(',').append(this.typeName);
            if (this.argType == MessagePattern.ArgType.SIMPLE) {
               if (this.style != null) {
                  var1.append(',').append(this.style);
               }
            } else {
               var1.append(',').append(this.complexStyle.toString());
            }
         }

         return var1.append('}').toString();
      }

      private ArgNode() {
         super(MessagePatternUtil.MessageContentsNode.Type.ARG, null);
      }

      private static MessagePatternUtil.ArgNode createArgNode() {
         return new MessagePatternUtil.ArgNode();
      }

      static MessagePatternUtil.ArgNode access$800() {
         return createArgNode();
      }

      static MessagePattern.ArgType access$902(MessagePatternUtil.ArgNode var0, MessagePattern.ArgType var1) {
         return var0.argType = var1;
      }

      static String access$1002(MessagePatternUtil.ArgNode var0, String var1) {
         return var0.name = var1;
      }

      static int access$1102(MessagePatternUtil.ArgNode var0, int var1) {
         return var0.number = var1;
      }

      static String access$1202(MessagePatternUtil.ArgNode var0, String var1) {
         return var0.typeName = var1;
      }

      static String access$1302(MessagePatternUtil.ArgNode var0, String var1) {
         return var0.style = var1;
      }

      static MessagePatternUtil.ComplexArgStyleNode access$1402(MessagePatternUtil.ArgNode var0, MessagePatternUtil.ComplexArgStyleNode var1) {
         return var0.complexStyle = var1;
      }
   }

   public static class TextNode extends MessagePatternUtil.MessageContentsNode {
      private String text;

      public String getText() {
         return this.text;
      }

      public String toString() {
         return "«" + this.text + "»";
      }

      private TextNode(String var1) {
         super(MessagePatternUtil.MessageContentsNode.Type.TEXT, null);
         this.text = var1;
      }

      static String access$102(MessagePatternUtil.TextNode var0, String var1) {
         return var0.text = var1;
      }

      static String access$100(MessagePatternUtil.TextNode var0) {
         return var0.text;
      }

      TextNode(String var1, Object var2) {
         this(var1);
      }
   }

   public static class MessageContentsNode extends MessagePatternUtil.Node {
      private MessagePatternUtil.MessageContentsNode.Type type;

      public MessagePatternUtil.MessageContentsNode.Type getType() {
         return this.type;
      }

      public String toString() {
         return "{REPLACE_NUMBER}";
      }

      private MessageContentsNode(MessagePatternUtil.MessageContentsNode.Type var1) {
         super(null);
         this.type = var1;
      }

      private static MessagePatternUtil.MessageContentsNode createReplaceNumberNode() {
         return new MessagePatternUtil.MessageContentsNode(MessagePatternUtil.MessageContentsNode.Type.REPLACE_NUMBER);
      }

      MessageContentsNode(MessagePatternUtil.MessageContentsNode.Type var1, Object var2) {
         this(var1);
      }

      static MessagePatternUtil.MessageContentsNode access$600() {
         return createReplaceNumberNode();
      }

      public static enum Type {
         TEXT,
         ARG,
         REPLACE_NUMBER;

         private static final MessagePatternUtil.MessageContentsNode.Type[] $VALUES = new MessagePatternUtil.MessageContentsNode.Type[]{TEXT, ARG, REPLACE_NUMBER};
      }
   }

   public static class MessageNode extends MessagePatternUtil.Node {
      private List list;

      public List getContents() {
         return this.list;
      }

      public String toString() {
         return this.list.toString();
      }

      private MessageNode() {
         super(null);
         this.list = new ArrayList();
      }

      private void addContentsNode(MessagePatternUtil.MessageContentsNode var1) {
         if (var1 instanceof MessagePatternUtil.TextNode && !this.list.isEmpty()) {
            MessagePatternUtil.MessageContentsNode var2 = (MessagePatternUtil.MessageContentsNode)this.list.get(this.list.size() - 1);
            if (var2 instanceof MessagePatternUtil.TextNode) {
               MessagePatternUtil.TextNode var3 = (MessagePatternUtil.TextNode)var2;
               MessagePatternUtil.TextNode.access$102(var3, MessagePatternUtil.TextNode.access$100(var3) + MessagePatternUtil.TextNode.access$100((MessagePatternUtil.TextNode)var1));
               return;
            }
         }

         this.list.add(var1);
      }

      private MessagePatternUtil.MessageNode freeze() {
         this.list = Collections.unmodifiableList(this.list);
         return this;
      }

      MessageNode(Object var1) {
         this();
      }

      static void access$500(MessagePatternUtil.MessageNode var0, MessagePatternUtil.MessageContentsNode var1) {
         var0.addContentsNode(var1);
      }

      static MessagePatternUtil.MessageNode access$700(MessagePatternUtil.MessageNode var0) {
         return var0.freeze();
      }
   }

   public static class Node {
      private Node() {
      }

      Node(Object var1) {
         this();
      }
   }
}
