/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.MessagePattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MessagePatternUtil {
    private MessagePatternUtil() {
    }

    public static MessageNode buildMessageNode(String patternString) {
        return MessagePatternUtil.buildMessageNode(new MessagePattern(patternString));
    }

    public static MessageNode buildMessageNode(MessagePattern pattern) {
        int limit = pattern.countParts() - 1;
        if (limit < 0) {
            throw new IllegalArgumentException("The MessagePattern is empty");
        }
        if (pattern.getPartType(0) != MessagePattern.Part.Type.MSG_START) {
            throw new IllegalArgumentException("The MessagePattern does not represent a MessageFormat pattern");
        }
        return MessagePatternUtil.buildMessageNode(pattern, 0, limit);
    }

    private static MessageNode buildMessageNode(MessagePattern pattern, int start, int limit) {
        int prevPatternIndex = pattern.getPart(start).getLimit();
        MessageNode node = new MessageNode();
        int i = start + 1;
        while (true) {
            MessagePattern.Part part;
            int patternIndex;
            if (prevPatternIndex < (patternIndex = (part = pattern.getPart(i)).getIndex())) {
                node.addContentsNode(new TextNode(pattern.getPatternString().substring(prevPatternIndex, patternIndex)));
            }
            if (i == limit) break;
            MessagePattern.Part.Type partType = part.getType();
            if (partType == MessagePattern.Part.Type.ARG_START) {
                int argLimit = pattern.getLimitPartIndex(i);
                node.addContentsNode(MessagePatternUtil.buildArgNode(pattern, i, argLimit));
                i = argLimit;
                part = pattern.getPart(i);
            } else if (partType == MessagePattern.Part.Type.REPLACE_NUMBER) {
                node.addContentsNode(MessageContentsNode.createReplaceNumberNode());
            }
            prevPatternIndex = part.getLimit();
            ++i;
        }
        return node.freeze();
    }

    private static ArgNode buildArgNode(MessagePattern pattern, int start, int limit) {
        ArgNode node = ArgNode.createArgNode();
        MessagePattern.Part part = pattern.getPart(start);
        MessagePattern.ArgType argType = node.argType = part.getArgType();
        part = pattern.getPart(++start);
        node.name = pattern.getSubstring(part);
        if (part.getType() == MessagePattern.Part.Type.ARG_NUMBER) {
            node.number = part.getValue();
        }
        ++start;
        switch (argType) {
            case SIMPLE: {
                node.typeName = pattern.getSubstring(pattern.getPart(start++));
                if (start >= limit) break;
                node.style = pattern.getSubstring(pattern.getPart(start));
                break;
            }
            case CHOICE: {
                node.typeName = "choice";
                node.complexStyle = MessagePatternUtil.buildChoiceStyleNode(pattern, start, limit);
                break;
            }
            case PLURAL: {
                node.typeName = "plural";
                node.complexStyle = MessagePatternUtil.buildPluralStyleNode(pattern, start, limit, argType);
                break;
            }
            case SELECT: {
                node.typeName = "select";
                node.complexStyle = MessagePatternUtil.buildSelectStyleNode(pattern, start, limit);
                break;
            }
            case SELECTORDINAL: {
                node.typeName = "selectordinal";
                node.complexStyle = MessagePatternUtil.buildPluralStyleNode(pattern, start, limit, argType);
                break;
            }
        }
        return node;
    }

    private static ComplexArgStyleNode buildChoiceStyleNode(MessagePattern pattern, int start, int limit) {
        ComplexArgStyleNode node = new ComplexArgStyleNode(MessagePattern.ArgType.CHOICE);
        while (start < limit) {
            int valueIndex = start;
            MessagePattern.Part part = pattern.getPart(start);
            double value = pattern.getNumericValue(part);
            int msgLimit = pattern.getLimitPartIndex(start += 2);
            VariantNode variant = new VariantNode();
            variant.selector = pattern.getSubstring(pattern.getPart(valueIndex + 1));
            variant.numericValue = value;
            variant.msgNode = MessagePatternUtil.buildMessageNode(pattern, start, msgLimit);
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
            ++start;
        }
        while (start < limit) {
            MessagePattern.Part selector = pattern.getPart(start++);
            double value = -1.23456789E8;
            MessagePattern.Part part = pattern.getPart(start);
            if (part.getType().hasNumericValue()) {
                value = pattern.getNumericValue(part);
                ++start;
            }
            int msgLimit = pattern.getLimitPartIndex(start);
            VariantNode variant = new VariantNode();
            variant.selector = pattern.getSubstring(selector);
            variant.numericValue = value;
            variant.msgNode = MessagePatternUtil.buildMessageNode(pattern, start, msgLimit);
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
            variant.msgNode = MessagePatternUtil.buildMessageNode(pattern, start, msgLimit);
            node.addVariant(variant);
            start = msgLimit + 1;
        }
        return node.freeze();
    }

    public static class VariantNode
    extends Node {
        private String selector;
        private double numericValue = -1.23456789E8;
        private MessageNode msgNode;

        public String getSelector() {
            return this.selector;
        }

        public boolean isSelectorNumeric() {
            return this.numericValue != -1.23456789E8;
        }

        public double getSelectorValue() {
            return this.numericValue;
        }

        public MessageNode getMessage() {
            return this.msgNode;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.isSelectorNumeric()) {
                sb.append(this.numericValue).append(" (").append(this.selector).append(") {");
            } else {
                sb.append(this.selector).append(" {");
            }
            return sb.append(this.msgNode.toString()).append('}').toString();
        }

        private VariantNode() {
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class ComplexArgStyleNode
    extends Node {
        private MessagePattern.ArgType argType;
        private double offset;
        private boolean explicitOffset;
        private List<VariantNode> list = new ArrayList<VariantNode>();

        public MessagePattern.ArgType getArgType() {
            return this.argType;
        }

        public boolean hasExplicitOffset() {
            return this.explicitOffset;
        }

        public double getOffset() {
            return this.offset;
        }

        public List<VariantNode> getVariants() {
            return this.list;
        }

        public VariantNode getVariantsByType(List<VariantNode> numericVariants, List<VariantNode> keywordVariants) {
            if (numericVariants != null) {
                numericVariants.clear();
            }
            keywordVariants.clear();
            VariantNode other = null;
            for (VariantNode variant : this.list) {
                if (variant.isSelectorNumeric()) {
                    numericVariants.add(variant);
                    continue;
                }
                if ("other".equals(variant.getSelector())) {
                    if (other != null) continue;
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
            if (this.hasExplicitOffset()) {
                sb.append("offset:").append(this.offset).append(' ');
            }
            return sb.append(this.list.toString()).toString();
        }

        private ComplexArgStyleNode(MessagePattern.ArgType argType) {
            this.argType = argType;
        }

        private void addVariant(VariantNode variant) {
            this.list.add(variant);
        }

        private ComplexArgStyleNode freeze() {
            this.list = Collections.unmodifiableList(this.list);
            return this;
        }
    }

    public static class ArgNode
    extends MessageContentsNode {
        private MessagePattern.ArgType argType;
        private String name;
        private int number = -1;
        private String typeName;
        private String style;
        private ComplexArgStyleNode complexStyle;

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

        public ComplexArgStyleNode getComplexStyle() {
            return this.complexStyle;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('{').append(this.name);
            if (this.argType != MessagePattern.ArgType.NONE) {
                sb.append(',').append(this.typeName);
                if (this.argType == MessagePattern.ArgType.SIMPLE) {
                    if (this.style != null) {
                        sb.append(',').append(this.style);
                    }
                } else {
                    sb.append(',').append(this.complexStyle.toString());
                }
            }
            return sb.append('}').toString();
        }

        private ArgNode() {
            super(MessageContentsNode.Type.ARG);
        }

        private static ArgNode createArgNode() {
            return new ArgNode();
        }
    }

    public static class TextNode
    extends MessageContentsNode {
        private String text;

        public String getText() {
            return this.text;
        }

        public String toString() {
            return "\u00ab" + this.text + "\u00bb";
        }

        private TextNode(String text) {
            super(MessageContentsNode.Type.TEXT);
            this.text = text;
        }
    }

    public static class MessageContentsNode
    extends Node {
        private Type type;

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

        /*
         * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
         */
        public static enum Type {
            TEXT,
            ARG,
            REPLACE_NUMBER;

        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class MessageNode
    extends Node {
        private List<MessageContentsNode> list = new ArrayList<MessageContentsNode>();

        public List<MessageContentsNode> getContents() {
            return this.list;
        }

        public String toString() {
            return this.list.toString();
        }

        private MessageNode() {
        }

        private void addContentsNode(MessageContentsNode node) {
            MessageContentsNode lastNode;
            if (node instanceof TextNode && !this.list.isEmpty() && (lastNode = this.list.get(this.list.size() - 1)) instanceof TextNode) {
                TextNode textNode = (TextNode)lastNode;
                textNode.text = textNode.text + ((TextNode)node).text;
                return;
            }
            this.list.add(node);
        }

        private MessageNode freeze() {
            this.list = Collections.unmodifiableList(this.list);
            return this;
        }
    }

    public static class Node {
        private Node() {
        }
    }
}

