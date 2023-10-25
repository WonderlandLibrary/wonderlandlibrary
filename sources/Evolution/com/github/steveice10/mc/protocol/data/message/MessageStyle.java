/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.data.message.ChatColor;
import com.github.steveice10.mc.protocol.data.message.ChatFormat;
import com.github.steveice10.mc.protocol.data.message.ClickEvent;
import com.github.steveice10.mc.protocol.data.message.HoverEvent;
import com.github.steveice10.mc.protocol.util.ObjectUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageStyle
implements Cloneable {
    private static final MessageStyle DEFAULT = new MessageStyle();
    private ChatColor color = ChatColor.WHITE;
    private List<ChatFormat> formats = new ArrayList<ChatFormat>();
    private ClickEvent click;
    private HoverEvent hover;
    private String insertion;
    private MessageStyle parent = DEFAULT;

    public boolean isDefault() {
        return this.equals(DEFAULT);
    }

    public ChatColor getColor() {
        return this.color;
    }

    public MessageStyle setColor(ChatColor color) {
        this.color = color;
        return this;
    }

    public List<ChatFormat> getFormats() {
        return new ArrayList<ChatFormat>(this.formats);
    }

    public MessageStyle setFormats(List<ChatFormat> formats) {
        this.formats = new ArrayList<ChatFormat>(formats);
        return this;
    }

    public ClickEvent getClickEvent() {
        return this.click;
    }

    public MessageStyle setClickEvent(ClickEvent event) {
        this.click = event;
        return this;
    }

    public HoverEvent getHoverEvent() {
        return this.hover;
    }

    public MessageStyle setHoverEvent(HoverEvent event) {
        this.hover = event;
        return this;
    }

    public String getInsertion() {
        return this.insertion;
    }

    public MessageStyle setInsertion(String insertion) {
        this.insertion = insertion;
        return this;
    }

    public MessageStyle getParent() {
        return this.parent;
    }

    protected MessageStyle setParent(MessageStyle parent) {
        if (parent == null) {
            parent = DEFAULT;
        }
        this.parent = parent;
        return this;
    }

    public MessageStyle addFormat(ChatFormat format) {
        this.formats.add(format);
        return this;
    }

    public MessageStyle removeFormat(ChatFormat format) {
        this.formats.remove((Object)format);
        return this;
    }

    public MessageStyle clearFormats() {
        this.formats.clear();
        return this;
    }

    public MessageStyle clone() {
        return new MessageStyle().setParent(this.parent).setColor(this.color).setFormats(this.formats).setClickEvent(this.click != null ? this.click.clone() : null).setHoverEvent(this.hover != null ? this.hover.clone() : null).setInsertion(this.insertion);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageStyle)) {
            return false;
        }
        MessageStyle that = (MessageStyle)o;
        return this.color == that.color && Objects.equals(this.formats, that.formats) && Objects.equals(this.click, that.click) && Objects.equals(this.hover, that.hover) && Objects.equals(this.insertion, that.insertion) && Objects.equals(this.parent, that.parent);
    }

    public int hashCode() {
        return ObjectUtil.hashCode(new Object[]{this.color, this.formats, this.click, this.hover, this.insertion, this.parent});
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

