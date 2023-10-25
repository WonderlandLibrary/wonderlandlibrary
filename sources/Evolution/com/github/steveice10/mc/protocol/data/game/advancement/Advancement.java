/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.game.advancement;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.util.ObjectUtil;
import java.util.List;
import java.util.Objects;

public class Advancement {
    private String id;
    private String parentId;
    private DisplayData displayData;
    private List<String> criteria;
    private List<List<String>> requirements;

    public Advancement(String id, String parentId, List<String> criteria, List<List<String>> requirements) {
        this.id = id;
        this.parentId = parentId;
        this.criteria = criteria;
        this.requirements = requirements;
    }

    public Advancement(String id, String parentId, List<String> criteria, List<List<String>> requirements, DisplayData displayData) {
        this(id, parentId, criteria, requirements);
        this.displayData = displayData;
    }

    public String getId() {
        return this.id;
    }

    public String getParentId() {
        return this.parentId;
    }

    public DisplayData getDisplayData() {
        return this.displayData;
    }

    public List<String> getCriteria() {
        return this.criteria;
    }

    public List<List<String>> getRequirements() {
        return this.requirements;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Advancement)) {
            return false;
        }
        Advancement that = (Advancement)o;
        return Objects.equals(this.id, that.id) && Objects.equals(this.parentId, that.parentId) && Objects.equals(this.displayData, that.displayData) && Objects.equals(this.criteria, that.criteria) && Objects.equals(this.requirements, that.requirements);
    }

    public int hashCode() {
        return Objects.hash(this.id, this.parentId, this.displayData, this.criteria, this.requirements);
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }

    public static class DisplayData {
        private Message title;
        private Message description;
        private ItemStack icon;
        private FrameType frameType;
        private boolean showToast;
        private boolean hidden;
        private String backgroundTexture;
        private float posX;
        private float posY;

        public DisplayData(Message title, Message description, ItemStack icon, FrameType frameType, boolean showToast, boolean hidden, float posX, float posY) {
            this.title = title;
            this.description = description;
            this.icon = icon;
            this.frameType = frameType;
            this.showToast = showToast;
            this.hidden = hidden;
            this.posX = posX;
            this.posY = posY;
        }

        public DisplayData(Message title, Message description, ItemStack icon, FrameType frameType, boolean showToast, boolean hidden, float posX, float posY, String backgroundTexture) {
            this(title, description, icon, frameType, showToast, hidden, posX, posY);
            this.backgroundTexture = backgroundTexture;
        }

        public Message getTitle() {
            return this.title;
        }

        public Message getDescription() {
            return this.description;
        }

        public ItemStack getIcon() {
            return this.icon;
        }

        public FrameType getFrameType() {
            return this.frameType;
        }

        public boolean doesShowToast() {
            return this.showToast;
        }

        public boolean isHidden() {
            return this.hidden;
        }

        public String getBackgroundTexture() {
            return this.backgroundTexture;
        }

        public float getPosX() {
            return this.posX;
        }

        public float getPosY() {
            return this.posY;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof DisplayData)) {
                return false;
            }
            DisplayData that = (DisplayData)o;
            return this.showToast == that.showToast && this.hidden == that.hidden && Float.compare(that.posX, this.posX) == 0 && Float.compare(that.posY, this.posY) == 0 && Objects.equals(this.title, that.title) && Objects.equals(this.description, that.description) && Objects.equals(this.icon, that.icon) && this.frameType == that.frameType && Objects.equals(this.backgroundTexture, that.backgroundTexture);
        }

        public int hashCode() {
            return ObjectUtil.hashCode(new Object[]{this.title, this.description, this.icon, this.frameType, this.showToast, this.hidden, this.backgroundTexture, Float.valueOf(this.posX), Float.valueOf(this.posY)});
        }

        public String toString() {
            return ObjectUtil.toString(this);
        }

        public static enum FrameType {
            TASK,
            CHALLENGE,
            GOAL;

        }
    }
}

