/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CommandResultStats {
    private static final String[] STRING_RESULT_TYPES;
    private String[] field_179673_d;
    private static final int NUM_RESULT_TYPES;
    private String[] field_179675_c = STRING_RESULT_TYPES;

    public void func_179671_a(CommandResultStats commandResultStats) {
        Type[] typeArray = Type.values();
        int n = typeArray.length;
        int n2 = 0;
        while (n2 < n) {
            Type type = typeArray[n2];
            CommandResultStats.func_179667_a(this, type, commandResultStats.field_179675_c[type.getTypeID()], commandResultStats.field_179673_d[type.getTypeID()]);
            ++n2;
        }
    }

    public static void func_179667_a(CommandResultStats commandResultStats, Type type, String string, String string2) {
        if (string != null && string.length() != 0 && string2 != null && string2.length() != 0) {
            if (commandResultStats.field_179675_c == STRING_RESULT_TYPES || commandResultStats.field_179673_d == STRING_RESULT_TYPES) {
                commandResultStats.field_179675_c = new String[NUM_RESULT_TYPES];
                commandResultStats.field_179673_d = new String[NUM_RESULT_TYPES];
            }
            commandResultStats.field_179675_c[type.getTypeID()] = string;
            commandResultStats.field_179673_d[type.getTypeID()] = string2;
        } else {
            CommandResultStats.func_179669_a(commandResultStats, type);
        }
    }

    public void readStatsFromNBT(NBTTagCompound nBTTagCompound) {
        if (nBTTagCompound.hasKey("CommandStats", 10)) {
            NBTTagCompound nBTTagCompound2 = nBTTagCompound.getCompoundTag("CommandStats");
            Type[] typeArray = Type.values();
            int n = typeArray.length;
            int n2 = 0;
            while (n2 < n) {
                Type type = typeArray[n2];
                String string = String.valueOf(type.getTypeName()) + "Name";
                String string2 = String.valueOf(type.getTypeName()) + "Objective";
                if (nBTTagCompound2.hasKey(string, 8) && nBTTagCompound2.hasKey(string2, 8)) {
                    String string3 = nBTTagCompound2.getString(string);
                    String string4 = nBTTagCompound2.getString(string2);
                    CommandResultStats.func_179667_a(this, type, string3, string4);
                }
                ++n2;
            }
        }
    }

    public void func_179672_a(final ICommandSender iCommandSender, Type type, int n) {
        String string = this.field_179675_c[type.getTypeID()];
        if (string != null) {
            Scoreboard scoreboard;
            ScoreObjective scoreObjective;
            String string2;
            ICommandSender iCommandSender2 = new ICommandSender(){

                @Override
                public void addChatMessage(IChatComponent iChatComponent) {
                    iCommandSender.addChatMessage(iChatComponent);
                }

                @Override
                public IChatComponent getDisplayName() {
                    return iCommandSender.getDisplayName();
                }

                @Override
                public BlockPos getPosition() {
                    return iCommandSender.getPosition();
                }

                @Override
                public boolean canCommandSenderUseCommand(int n, String string) {
                    return true;
                }

                @Override
                public boolean sendCommandFeedback() {
                    return iCommandSender.sendCommandFeedback();
                }

                @Override
                public void setCommandStat(Type type, int n) {
                    iCommandSender.setCommandStat(type, n);
                }

                @Override
                public Vec3 getPositionVector() {
                    return iCommandSender.getPositionVector();
                }

                @Override
                public Entity getCommandSenderEntity() {
                    return iCommandSender.getCommandSenderEntity();
                }

                @Override
                public World getEntityWorld() {
                    return iCommandSender.getEntityWorld();
                }

                @Override
                public String getName() {
                    return iCommandSender.getName();
                }
            };
            try {
                string2 = CommandBase.getEntityName(iCommandSender2, string);
            }
            catch (EntityNotFoundException entityNotFoundException) {
                return;
            }
            String string3 = this.field_179673_d[type.getTypeID()];
            if (string3 != null && (scoreObjective = (scoreboard = iCommandSender.getEntityWorld().getScoreboard()).getObjective(string3)) != null && scoreboard.entityHasObjective(string2, scoreObjective)) {
                Score score = scoreboard.getValueFromObjective(string2, scoreObjective);
                score.setScorePoints(n);
            }
        }
    }

    static {
        NUM_RESULT_TYPES = Type.values().length;
        STRING_RESULT_TYPES = new String[NUM_RESULT_TYPES];
    }

    public void writeStatsToNBT(NBTTagCompound nBTTagCompound) {
        NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
        Type[] typeArray = Type.values();
        int n = typeArray.length;
        int n2 = 0;
        while (n2 < n) {
            Type type = typeArray[n2];
            String string = this.field_179675_c[type.getTypeID()];
            String string2 = this.field_179673_d[type.getTypeID()];
            if (string != null && string2 != null) {
                nBTTagCompound2.setString(String.valueOf(type.getTypeName()) + "Name", string);
                nBTTagCompound2.setString(String.valueOf(type.getTypeName()) + "Objective", string2);
            }
            ++n2;
        }
        if (!nBTTagCompound2.hasNoTags()) {
            nBTTagCompound.setTag("CommandStats", nBTTagCompound2);
        }
    }

    private static void func_179669_a(CommandResultStats commandResultStats, Type type) {
        if (commandResultStats.field_179675_c != STRING_RESULT_TYPES && commandResultStats.field_179673_d != STRING_RESULT_TYPES) {
            commandResultStats.field_179675_c[type.getTypeID()] = null;
            commandResultStats.field_179673_d[type.getTypeID()] = null;
            boolean bl = true;
            Type[] typeArray = Type.values();
            int n = typeArray.length;
            int n2 = 0;
            while (n2 < n) {
                Type type2 = typeArray[n2];
                if (commandResultStats.field_179675_c[type2.getTypeID()] != null && commandResultStats.field_179673_d[type2.getTypeID()] != null) {
                    bl = false;
                    break;
                }
                ++n2;
            }
            if (bl) {
                commandResultStats.field_179675_c = STRING_RESULT_TYPES;
                commandResultStats.field_179673_d = STRING_RESULT_TYPES;
            }
        }
    }

    public CommandResultStats() {
        this.field_179673_d = STRING_RESULT_TYPES;
    }

    public static enum Type {
        SUCCESS_COUNT(0, "SuccessCount"),
        AFFECTED_BLOCKS(1, "AffectedBlocks"),
        AFFECTED_ENTITIES(2, "AffectedEntities"),
        AFFECTED_ITEMS(3, "AffectedItems"),
        QUERY_RESULT(4, "QueryResult");

        final int typeID;
        final String typeName;

        public static String[] getTypeNames() {
            String[] stringArray = new String[Type.values().length];
            int n = 0;
            Type[] typeArray = Type.values();
            int n2 = typeArray.length;
            int n3 = 0;
            while (n3 < n2) {
                Type type = typeArray[n3];
                stringArray[n++] = type.getTypeName();
                ++n3;
            }
            return stringArray;
        }

        public String getTypeName() {
            return this.typeName;
        }

        private Type(int n2, String string2) {
            this.typeID = n2;
            this.typeName = string2;
        }

        public int getTypeID() {
            return this.typeID;
        }

        public static Type getTypeByName(String string) {
            Type[] typeArray = Type.values();
            int n = typeArray.length;
            int n2 = 0;
            while (n2 < n) {
                Type type = typeArray[n2];
                if (type.getTypeName().equals(string)) {
                    return type;
                }
                ++n2;
            }
            return null;
        }
    }
}

