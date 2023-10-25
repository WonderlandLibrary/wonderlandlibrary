/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.advancement.Advancement;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerAdvancementsPacket
extends MinecraftPacket {
    private boolean reset;
    private List<Advancement> advancements;
    private List<String> removedAdvancements;
    private Map<String, Map<String, Long>> progress;

    private ServerAdvancementsPacket() {
    }

    public ServerAdvancementsPacket(boolean reset, List<Advancement> advancements, List<String> removedAdvancements, Map<String, Map<String, Long>> progress) {
        this.reset = reset;
        this.advancements = advancements;
        this.removedAdvancements = removedAdvancements;
        this.progress = progress;
    }

    public boolean doesReset() {
        return this.reset;
    }

    public List<Advancement> getAdvancements() {
        return this.advancements;
    }

    public List<String> getRemovedAdvancements() {
        return this.removedAdvancements;
    }

    public Map<String, Map<String, Long>> getProgress() {
        return this.progress;
    }

    public Map<String, Long> getProgress(String advancementId) {
        return this.getProgress().get(advancementId);
    }

    public Long getAchievedDate(String advancementId, String criterionId) {
        return this.getProgress(advancementId).get(criterionId);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.reset = in.readBoolean();
        this.advancements = new ArrayList<Advancement>();
        int advancementCount = in.readVarInt();
        int i = 0;
        while (i < advancementCount) {
            String id = in.readString();
            String parentId = in.readBoolean() ? in.readString() : null;
            Advancement.DisplayData displayData = null;
            if (in.readBoolean()) {
                Message title = Message.fromString(in.readString());
                Message description = Message.fromString(in.readString());
                ItemStack icon = NetUtil.readItem(in);
                Advancement.DisplayData.FrameType frameType = MagicValues.key(Advancement.DisplayData.FrameType.class, in.readVarInt());
                int flags = in.readInt();
                boolean hasBackgroundTexture = (flags & 1) != 0;
                boolean showToast = (flags & 2) != 0;
                boolean hidden = (flags & 4) != 0;
                String backgroundTexture = hasBackgroundTexture ? in.readString() : null;
                float posX = in.readFloat();
                float posY = in.readFloat();
                displayData = new Advancement.DisplayData(title, description, icon, frameType, showToast, hidden, posX, posY, backgroundTexture);
            }
            ArrayList<String> criteria = new ArrayList<String>();
            int criteriaCount = in.readVarInt();
            int j = 0;
            while (j < criteriaCount) {
                criteria.add(in.readString());
                ++j;
            }
            ArrayList<List<String>> requirements = new ArrayList<List<String>>();
            int requirementCount = in.readVarInt();
            int j2 = 0;
            while (j2 < requirementCount) {
                ArrayList<String> requirement = new ArrayList<String>();
                int componentCount = in.readVarInt();
                int k2 = 0;
                while (k2 < componentCount) {
                    requirement.add(in.readString());
                    ++k2;
                }
                requirements.add(requirement);
                ++j2;
            }
            this.advancements.add(new Advancement(id, parentId, criteria, requirements, displayData));
            ++i;
        }
        this.removedAdvancements = new ArrayList<String>();
        int removedCount = in.readVarInt();
        int i2 = 0;
        while (i2 < removedCount) {
            this.removedAdvancements.add(in.readString());
            ++i2;
        }
        this.progress = new HashMap<String, Map<String, Long>>();
        int progressCount = in.readVarInt();
        int i3 = 0;
        while (i3 < progressCount) {
            String advancementId = in.readString();
            HashMap<String, Long> advancementProgress = new HashMap<String, Long>();
            int criterionCount = in.readVarInt();
            int j = 0;
            while (j < criterionCount) {
                String criterionId = in.readString();
                Long achievedDate = in.readBoolean() ? Long.valueOf(in.readLong()) : null;
                advancementProgress.put(criterionId, achievedDate);
                ++j;
            }
            this.progress.put(advancementId, advancementProgress);
            ++i3;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeBoolean(this.reset);
        out.writeVarInt(this.advancements.size());
        for (Advancement advancement : this.advancements) {
            out.writeString(advancement.getId());
            if (advancement.getParentId() != null) {
                out.writeBoolean(true);
                out.writeString(advancement.getParentId());
            } else {
                out.writeBoolean(false);
            }
            Advancement.DisplayData displayData = advancement.getDisplayData();
            if (displayData != null) {
                out.writeBoolean(true);
                out.writeString(displayData.getTitle().toJsonString());
                out.writeString(displayData.getDescription().toJsonString());
                NetUtil.writeItem(out, displayData.getIcon());
                out.writeVarInt(MagicValues.value(Integer.class, (Object)displayData.getFrameType()));
                String string = displayData.getBackgroundTexture();
                int flags = 0;
                if (string != null) {
                    flags |= 1;
                }
                if (displayData.doesShowToast()) {
                    flags |= 2;
                }
                if (displayData.isHidden()) {
                    flags |= 4;
                }
                out.writeInt(flags);
                if (string != null) {
                    out.writeString(string);
                }
                out.writeFloat(displayData.getPosX());
                out.writeFloat(displayData.getPosY());
            } else {
                out.writeBoolean(false);
            }
            out.writeVarInt(advancement.getCriteria().size());
            for (String string : advancement.getCriteria()) {
                out.writeString(string);
            }
            out.writeVarInt(advancement.getRequirements().size());
            for (List list : advancement.getRequirements()) {
                out.writeVarInt(list.size());
                for (Object criterion : list) {
                    out.writeString(String.valueOf(criterion));
                }
            }
        }
        out.writeVarInt(this.removedAdvancements.size());
        for (String string : this.removedAdvancements) {
            out.writeString(string);
        }
        out.writeVarInt(this.progress.size());
        for (Map.Entry<String, Map<String, Long>> entry : this.progress.entrySet()) {
            out.writeString((String)entry.getKey());
            Map<String, Long> advancementProgress = entry.getValue();
            out.writeVarInt(advancementProgress.size());
            for (Map.Entry entry3 : advancementProgress.entrySet()) {
                out.writeString((String)entry3.getKey());
                if (entry3.getValue() != null) {
                    out.writeBoolean(true);
                    out.writeLong((Long)entry3.getValue());
                    continue;
                }
                out.writeBoolean(false);
            }
        }
    }
}

