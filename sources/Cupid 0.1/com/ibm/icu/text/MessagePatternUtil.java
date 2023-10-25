package com.ibm.icu.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MessagePatternUtil {
  public static MessageNode buildMessageNode(String patternString) {
    return buildMessageNode(new MessagePattern(patternString));
  }
  
  public static MessageNode buildMessageNode(MessagePattern pattern) {
    int limit = pattern.countParts() - 1;
    if (limit < 0)
      throw new IllegalArgumentException("The MessagePattern is empty"); 
    if (pattern.getPartType(0) != MessagePattern.Part.Type.MSG_START)
      throw new IllegalArgumentException("The MessagePattern does not represent a MessageFormat pattern"); 
    return buildMessageNode(pattern, 0, limit);
  }
  
  public static class Node {
    private Node() {}
  }
  
  public static class MessageNode extends Node {
    private List<MessagePatternUtil.MessageContentsNode> list;
    
    public List<MessagePatternUtil.MessageContentsNode> getContents() {
      return this.list;
    }
    
    public String toString() {
      return this.list.toString();
    }
    
    private MessageNode() {
      this.list = new ArrayList<MessagePatternUtil.MessageContentsNode>();
    }
    
    private void addContentsNode(MessagePatternUtil.MessageContentsNode node) {
      if (node instanceof MessagePatternUtil.TextNode && !this.list.isEmpty()) {
        MessagePatternUtil.MessageContentsNode lastNode = this.list.get(this.list.size() - 1);
        if (lastNode instanceof MessagePatternUtil.TextNode) {
          MessagePatternUtil.TextNode textNode = (MessagePatternUtil.TextNode)lastNode;
          textNode.text = textNode.text + ((MessagePatternUtil.TextNode)node).text;
          return;
        } 
      } 
      this.list.add(node);
    }
    
    private MessageNode freeze() {
      this.list = Collections.unmodifiableList(this.list);
      return this;
    }
  }
  
  public static class MessageContentsNode extends Node {
    private Type type;
    
    public enum Type {
      TEXT, ARG, REPLACE_NUMBER;
    }
    
    public Type getType() {
      return this.type;
    }
    
    public String toString() {
      return "{REPLACE_NUMBER}";
    }
    
    private MessageContentsNode(Type type) {
      this.type = type;
    }
    
    private static MessageContentsNode createReplaceNumberNode() {
      return new MessageContentsNode(Type.REPLACE_NUMBER);
    }
  }
  
  public static class TextNode extends MessageContentsNode {
    private String text;
    
    public String getText() {
      return this.text;
    }
    
    public String toString() {
      return "«" + this.text + "»";
    }
    
    private TextNode(String text) {
      super(MessagePatternUtil.MessageContentsNode.Type.TEXT);
      this.text = text;
    }
  }
  
  public static class ArgNode extends MessageContentsNode {
    private MessagePattern.ArgType argType;
    
    private String name;
    
    private int number;
    
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
      StringBuilder sb = new StringBuilder();
      sb.append('{').append(this.name);
      if (this.argType != MessagePattern.ArgType.NONE) {
        sb.append(',').append(this.typeName);
        if (this.argType == MessagePattern.ArgType.SIMPLE) {
          if (this.style != null)
            sb.append(',').append(this.style); 
        } else {
          sb.append(',').append(this.complexStyle.toString());
        } 
      } 
      return sb.append('}').toString();
    }
    
    private ArgNode() {
      super(MessagePatternUtil.MessageContentsNode.Type.ARG);
      this.number = -1;
    }
    
    private static ArgNode createArgNode() {
      return new ArgNode();
    }
  }
  
  public static class ComplexArgStyleNode extends Node {
    private MessagePattern.ArgType argType;
    
    private double offset;
    
    private boolean explicitOffset;
    
    private List<MessagePatternUtil.VariantNode> list;
    
    public MessagePattern.ArgType getArgType() {
      return this.argType;
    }
    
    public boolean hasExplicitOffset() {
      return this.explicitOffset;
    }
    
    public double getOffset() {
      return this.offset;
    }
    
    public List<MessagePatternUtil.VariantNode> getVariants() {
      return this.list;
    }
    
    public MessagePatternUtil.VariantNode getVariantsByType(List<MessagePatternUtil.VariantNode> numericVariants, List<MessagePatternUtil.VariantNode> keywordVariants) {
      if (numericVariants != null)
        numericVariants.clear(); 
      keywordVariants.clear();
      MessagePatternUtil.VariantNode other = null;
      for (MessagePatternUtil.VariantNode variant : this.list) {
        if (variant.isSelectorNumeric()) {
          numericVariants.add(variant);
          continue;
        } 
        if ("other".equals(variant.getSelector())) {
          if (other == null)
            other = variant; 
          continue;
        } 
        keywordVariants.add(variant);
      } 
      return other;
    }
    
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append('(').append(this.argType.toString()).append(" style) ");
      if (hasExplicitOffset())
        sb.append("offset:").append(this.offset).append(' '); 
      return sb.append(this.list.toString()).toString();
    }
    
    private ComplexArgStyleNode(MessagePattern.ArgType argType) {
      this.list = new ArrayList<MessagePatternUtil.VariantNode>();
      this.argType = argType;
    }
    
    private void addVariant(MessagePatternUtil.VariantNode variant) {
      this.list.add(variant);
    }
    
    private ComplexArgStyleNode freeze() {
      this.list = Collections.unmodifiableList(this.list);
      return this;
    }
  }
  
  public static class VariantNode extends Node {
    private String selector;
    
    private double numericValue;
    
    private MessagePatternUtil.MessageNode msgNode;
    
    public String getSelector() {
      return this.selector;
    }
    
    public boolean isSelectorNumeric() {
      return (this.numericValue != -1.23456789E8D);
    }
    
    public double getSelectorValue() {
      return this.numericValue;
    }
    
    public MessagePatternUtil.MessageNode getMessage() {
      return this.msgNode;
    }
    
    public String toString() {
      StringBuilder sb = new StringBuilder();
      if (isSelectorNumeric()) {
        sb.append(this.numericValue).append(" (").append(this.selector).append(") {");
      } else {
        sb.append(this.selector).append(" {");
      } 
      return sb.append(this.msgNode.toString()).append('}').toString();
    }
    
    private VariantNode() {
      this.numericValue = -1.23456789E8D;
    }
  }
  
  private static MessageNode buildMessageNode(MessagePattern pattern, int start, int limit) {
    int prevPatternIndex = pattern.getPart(start).getLimit();
    MessageNode node = new MessageNode();
    for (int i = start + 1;; i++) {
      MessagePattern.Part part = pattern.getPart(i);
      int patternIndex = part.getIndex();
      if (prevPatternIndex < patternIndex)
        node.addContentsNode(new TextNode(pattern.getPatternString().substring(prevPatternIndex, patternIndex))); 
      if (i == limit)
        break; 
      MessagePattern.Part.Type partType = part.getType();
      if (partType == MessagePattern.Part.Type.ARG_START) {
        int argLimit = pattern.getLimitPartIndex(i);
        node.addContentsNode(buildArgNode(pattern, i, argLimit));
        i = argLimit;
        part = pattern.getPart(i);
      } else if (partType == MessagePattern.Part.Type.REPLACE_NUMBER) {
        node.addContentsNode(MessageContentsNode.createReplaceNumberNode());
      } 
      prevPatternIndex = part.getLimit();
    } 
    return node.freeze();
  }
  
  private static ArgNode buildArgNode(MessagePattern pattern, int start, int limit) {
    ArgNode node = ArgNode.createArgNode();
    MessagePattern.Part part = pattern.getPart(start);
    MessagePattern.ArgType argType = node.argType = part.getArgType();
    part = pattern.getPart(++start);
    node.name = pattern.getSubstring(part);
    if (part.getType() == MessagePattern.Part.Type.ARG_NUMBER)
      node.number = part.getValue(); 
    start++;
    switch (argType) {
      case SIMPLE:
        node.typeName = pattern.getSubstring(pattern.getPart(start++));
        if (start < limit)
          node.style = pattern.getSubstring(pattern.getPart(start)); 
        break;
      case CHOICE:
        node.typeName = "choice";
        node.complexStyle = buildChoiceStyleNode(pattern, start, limit);
        break;
      case PLURAL:
        node.typeName = "plural";
        node.complexStyle = buildPluralStyleNode(pattern, start, limit, argType);
        break;
      case SELECT:
        node.typeName = "select";
        node.complexStyle = buildSelectStyleNode(pattern, start, limit);
        break;
      case SELECTORDINAL:
        node.typeName = "selectordinal";
        node.complexStyle = buildPluralStyleNode(pattern, start, limit, argType);
        break;
    } 
    return node;
  }
  
  private static ComplexArgStyleNode buildChoiceStyleNode(MessagePattern pattern, int start, int limit) {
    ComplexArgStyleNode node = new ComplexArgStyleNode(MessagePattern.ArgType.CHOICE);
    while (start < limit) {
      int valueIndex = start;
      MessagePattern.Part part = pattern.getPart(start);
      double value = pattern.getNumericValue(part);
      start += 2;
      int msgLimit = pattern.getLimitPartIndex(start);
      VariantNode variant = new VariantNode();
      variant.selector = pattern.getSubstring(pattern.getPart(valueIndex + 1));
      variant.numericValue = value;
      variant.msgNode = buildMessageNode(pattern, start, msgLimit);
      node.addVariant(variant);
      start = msgLimit + 1;
    } 
    return node.freeze();
  }
  
  private static ComplexArgStyleNode buildPluralStyleNode(MessagePattern pattern, int start, int limit, MessagePattern.ArgType argType) {
    ComplexArgStyleNode node = new ComplexArgStyleNode(argType);
    MessagePattern.Part offset = pattern.getPart(start);
    if (offset.getType().hasNumericValue()) {
      node.explicitOffset = true;
      node.offset = pattern.getNumericValue(offset);
      start++;
    } 
    while (start < limit) {
      MessagePattern.Part selector = pattern.getPart(start++);
      double value = -1.23456789E8D;
      MessagePattern.Part part = pattern.getPart(start);
      if (part.getType().hasNumericValue()) {
        value = pattern.getNumericValue(part);
        start++;
      } 
      int msgLimit = pattern.getLimitPartIndex(start);
      VariantNode variant = new VariantNode();
      variant.selector = pattern.getSubstring(selector);
      variant.numericValue = value;
      variant.msgNode = buildMessageNode(pattern, start, msgLimit);
      node.addVariant(variant);
      start = msgLimit + 1;
    } 
    return node.freeze();
  }
  
  private static ComplexArgStyleNode buildSelectStyleNode(MessagePattern pattern, int start, int limit) {
    ComplexArgStyleNode node = new ComplexArgStyleNode(MessagePattern.ArgType.SELECT);
    while (start < limit) {
      MessagePattern.Part selector = pattern.getPart(start++);
      int msgLimit = pattern.getLimitPartIndex(start);
      VariantNode variant = new VariantNode();
      variant.selector = pattern.getSubstring(selector);
      variant.msgNode = buildMessageNode(pattern, start, msgLimit);
      node.addVariant(variant);
      start = msgLimit + 1;
    } 
    return node.freeze();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\MessagePatternUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */