/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Sets
 */
package net.minecraft.command.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

public class CommandScoreboard
extends CommandBase {
    protected ScoreObjective getObjective(String string, boolean bl) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard();
        ScoreObjective scoreObjective = scoreboard.getObjective(string);
        if (scoreObjective == null) {
            throw new CommandException("commands.scoreboard.objectiveNotFound", string);
        }
        if (bl && scoreObjective.getCriteria().isReadOnly()) {
            throw new CommandException("commands.scoreboard.objectiveReadOnly", string);
        }
        return scoreObjective;
    }

    protected void func_175778_p(ICommandSender iCommandSender, String[] stringArray, int n) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard();
        String string = CommandScoreboard.getEntityName(iCommandSender, stringArray[n++]);
        ScoreObjective scoreObjective = this.getObjective(stringArray[n++], true);
        String string2 = stringArray[n++];
        String string3 = CommandScoreboard.getEntityName(iCommandSender, stringArray[n++]);
        ScoreObjective scoreObjective2 = this.getObjective(stringArray[n], false);
        if (string.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", string, 40);
        }
        if (string3.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", string3, 40);
        }
        Score score = scoreboard.getValueFromObjective(string, scoreObjective);
        if (!scoreboard.entityHasObjective(string3, scoreObjective2)) {
            throw new CommandException("commands.scoreboard.players.operation.notFound", scoreObjective2.getName(), string3);
        }
        Score score2 = scoreboard.getValueFromObjective(string3, scoreObjective2);
        if (string2.equals("+=")) {
            score.setScorePoints(score.getScorePoints() + score2.getScorePoints());
        } else if (string2.equals("-=")) {
            score.setScorePoints(score.getScorePoints() - score2.getScorePoints());
        } else if (string2.equals("*=")) {
            score.setScorePoints(score.getScorePoints() * score2.getScorePoints());
        } else if (string2.equals("/=")) {
            if (score2.getScorePoints() != 0) {
                score.setScorePoints(score.getScorePoints() / score2.getScorePoints());
            }
        } else if (string2.equals("%=")) {
            if (score2.getScorePoints() != 0) {
                score.setScorePoints(score.getScorePoints() % score2.getScorePoints());
            }
        } else if (string2.equals("=")) {
            score.setScorePoints(score2.getScorePoints());
        } else if (string2.equals("<")) {
            score.setScorePoints(Math.min(score.getScorePoints(), score2.getScorePoints()));
        } else if (string2.equals(">")) {
            score.setScorePoints(Math.max(score.getScorePoints(), score2.getScorePoints()));
        } else {
            if (!string2.equals("><")) {
                throw new CommandException("commands.scoreboard.players.operation.invalidOperation", string2);
            }
            int n2 = score.getScorePoints();
            score.setScorePoints(score2.getScorePoints());
            score2.setScorePoints(n2);
        }
        CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.players.operation.success", new Object[0]);
    }

    @Override
    public boolean isUsernameIndex(String[] stringArray, int n) {
        return !stringArray[0].equalsIgnoreCase("players") ? (stringArray[0].equalsIgnoreCase("teams") ? n == 2 : false) : (stringArray.length > 1 && stringArray[1].equalsIgnoreCase("operation") ? n == 2 || n == 5 : n == 2);
    }

    protected void listTeams(ICommandSender iCommandSender, String[] stringArray, int n) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard();
        if (stringArray.length > n) {
            ScorePlayerTeam scorePlayerTeam = this.getTeam(stringArray[n]);
            if (scorePlayerTeam == null) {
                return;
            }
            Collection<String> collection = scorePlayerTeam.getMembershipCollection();
            iCommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
            if (collection.size() <= 0) {
                throw new CommandException("commands.scoreboard.teams.list.player.empty", scorePlayerTeam.getRegisteredName());
            }
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.scoreboard.teams.list.player.count", collection.size(), scorePlayerTeam.getRegisteredName());
            chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            iCommandSender.addChatMessage(chatComponentTranslation);
            iCommandSender.addChatMessage(new ChatComponentText(CommandScoreboard.joinNiceString(collection.toArray())));
        } else {
            Collection<ScorePlayerTeam> collection = scoreboard.getTeams();
            iCommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
            if (collection.size() <= 0) {
                throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
            }
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.scoreboard.teams.list.count", collection.size());
            chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            iCommandSender.addChatMessage(chatComponentTranslation);
            for (ScorePlayerTeam scorePlayerTeam : collection) {
                iCommandSender.addChatMessage(new ChatComponentTranslation("commands.scoreboard.teams.list.entry", scorePlayerTeam.getRegisteredName(), scorePlayerTeam.getTeamName(), scorePlayerTeam.getMembershipCollection().size()));
            }
        }
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.scoreboard.usage";
    }

    /*
     * Could not resolve type clashes
     * Unable to fully structure code
     */
    protected void joinTeam(ICommandSender var1_1, String[] var2_2, int var3_3) throws CommandException {
        block10: {
            var4_4 = this.getScoreboard();
            var5_5 = var2_2[var3_3++];
            var6_6 = Sets.newHashSet();
            var7_7 = Sets.newHashSet();
            if (!(var1_1 instanceof EntityPlayer) || var3_3 != var2_2.length) ** GOTO lbl32
            var8_8 = CommandScoreboard.getCommandSenderAsPlayer(var1_1).getName();
            if (var4_4.addPlayerToTeam(var8_8, var5_5)) {
                var6_6.add(var8_8);
            } else {
                var7_7.add(var8_8);
            }
            break block10;
lbl-1000:
            // 1 sources

            {
                if ((var8_9 = var2_2[var3_3++]).startsWith("@")) {
                    for (Object var9_10 : CommandScoreboard.func_175763_c(var1_1, var8_9)) {
                        var11_12 = CommandScoreboard.getEntityName(var1_1, var9_10.getUniqueID().toString());
                        if (var4_4.addPlayerToTeam(var11_12, var5_5)) {
                            var6_6.add(var11_12);
                            continue;
                        }
                        var7_7.add(var11_12);
                    }
                } else {
                    var9_10 = CommandScoreboard.getEntityName(var1_1, var8_9);
                    if (var4_4.addPlayerToTeam((String)var9_10, var5_5)) {
                        var6_6.add(var9_10);
                        continue;
                    }
                    var7_7.add(var9_10);
                }
lbl32:
                // 4 sources

                ** while (var3_3 < var2_2.length)
            }
        }
        if (!var6_6.isEmpty()) {
            var1_1.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, var6_6.size());
            CommandScoreboard.notifyOperators(var1_1, (ICommand)this, "commands.scoreboard.teams.join.success", new Object[]{var6_6.size(), var5_5, CommandScoreboard.joinNiceString(var6_6.toArray(new String[var6_6.size()]))});
        }
        if (!var7_7.isEmpty()) {
            throw new CommandException("commands.scoreboard.teams.join.failure", new Object[]{var7_7.size(), var5_5, CommandScoreboard.joinNiceString(var7_7.toArray(new String[var7_7.size()]))});
        }
    }

    protected void setObjectiveDisplay(ICommandSender iCommandSender, String[] stringArray, int n) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard();
        String string = stringArray[n++];
        int n2 = Scoreboard.getObjectiveDisplaySlotNumber(string);
        ScoreObjective scoreObjective = null;
        if (stringArray.length == 4) {
            scoreObjective = this.getObjective(stringArray[n], false);
        }
        if (n2 < 0) {
            throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", string);
        }
        scoreboard.setObjectiveInDisplaySlot(n2, scoreObjective);
        if (scoreObjective != null) {
            CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.objectives.setdisplay.successSet", Scoreboard.getObjectiveDisplaySlot(n2), scoreObjective.getName());
        } else {
            CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.objectives.setdisplay.successCleared", Scoreboard.getObjectiveDisplaySlot(n2));
        }
    }

    /*
     * Could not resolve type clashes
     * Unable to fully structure code
     */
    protected void leaveTeam(ICommandSender var1_1, String[] var2_2, int var3_3) throws CommandException {
        block10: {
            var4_4 = this.getScoreboard();
            var5_5 = Sets.newHashSet();
            var6_6 = Sets.newHashSet();
            if (!(var1_1 instanceof EntityPlayer) || var3_3 != var2_2.length) ** GOTO lbl31
            var7_7 = CommandScoreboard.getCommandSenderAsPlayer(var1_1).getName();
            if (var4_4.removePlayerFromTeams(var7_7)) {
                var5_5.add(var7_7);
            } else {
                var6_6.add(var7_7);
            }
            break block10;
lbl-1000:
            // 1 sources

            {
                if ((var7_8 = var2_2[var3_3++]).startsWith("@")) {
                    for (Object var8_9 : CommandScoreboard.func_175763_c(var1_1, var7_8)) {
                        var10_11 = CommandScoreboard.getEntityName(var1_1, var8_9.getUniqueID().toString());
                        if (var4_4.removePlayerFromTeams(var10_11)) {
                            var5_5.add(var10_11);
                            continue;
                        }
                        var6_6.add(var10_11);
                    }
                } else {
                    var8_9 = CommandScoreboard.getEntityName(var1_1, var7_8);
                    if (var4_4.removePlayerFromTeams((String)var8_9)) {
                        var5_5.add(var8_9);
                        continue;
                    }
                    var6_6.add(var8_9);
                }
lbl31:
                // 4 sources

                ** while (var3_3 < var2_2.length)
            }
        }
        if (!var5_5.isEmpty()) {
            var1_1.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, var5_5.size());
            CommandScoreboard.notifyOperators(var1_1, (ICommand)this, "commands.scoreboard.teams.leave.success", new Object[]{var5_5.size(), CommandScoreboard.joinNiceString(var5_5.toArray(new String[var5_5.size()]))});
        }
        if (!var6_6.isEmpty()) {
            throw new CommandException("commands.scoreboard.teams.leave.failure", new Object[]{var6_6.size(), CommandScoreboard.joinNiceString(var6_6.toArray(new String[var6_6.size()]))});
        }
    }

    protected void func_175781_o(ICommandSender iCommandSender, String[] stringArray, int n) throws CommandException {
        ScoreObjective scoreObjective;
        String string;
        Scoreboard scoreboard = this.getScoreboard();
        if ((string = CommandScoreboard.getEntityName(iCommandSender, stringArray[n++])).length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", string, 40);
        }
        if (!scoreboard.entityHasObjective(string, scoreObjective = this.getObjective(stringArray[n++], false))) {
            throw new CommandException("commands.scoreboard.players.test.notFound", scoreObjective.getName(), string);
        }
        int n2 = stringArray[n].equals("*") ? Integer.MIN_VALUE : CommandScoreboard.parseInt(stringArray[n]);
        int n3 = ++n < stringArray.length && !stringArray[n].equals("*") ? CommandScoreboard.parseInt(stringArray[n], n2) : Integer.MAX_VALUE;
        Score score = scoreboard.getValueFromObjective(string, scoreObjective);
        if (score.getScorePoints() < n2 || score.getScorePoints() > n3) {
            throw new CommandException("commands.scoreboard.players.test.failed", score.getScorePoints(), n2, n3);
        }
        CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.players.test.success", score.getScorePoints(), n2, n3);
    }

    protected List<String> func_175782_e() {
        Collection<ScoreObjective> collection = this.getScoreboard().getScoreObjectives();
        ArrayList arrayList = Lists.newArrayList();
        for (ScoreObjective scoreObjective : collection) {
            if (scoreObjective.getCriteria() != IScoreObjectiveCriteria.TRIGGER) continue;
            arrayList.add(scoreObjective.getName());
        }
        return arrayList;
    }

    protected void removeTeam(ICommandSender iCommandSender, String[] stringArray, int n) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard();
        ScorePlayerTeam scorePlayerTeam = this.getTeam(stringArray[n]);
        if (scorePlayerTeam != null) {
            scoreboard.removeTeam(scorePlayerTeam);
            CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.teams.remove.success", scorePlayerTeam.getRegisteredName());
        }
    }

    protected void emptyTeam(ICommandSender iCommandSender, String[] stringArray, int n) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard();
        ScorePlayerTeam scorePlayerTeam = this.getTeam(stringArray[n]);
        if (scorePlayerTeam != null) {
            ArrayList arrayList = Lists.newArrayList(scorePlayerTeam.getMembershipCollection());
            iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, arrayList.size());
            if (arrayList.isEmpty()) {
                throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", scorePlayerTeam.getRegisteredName());
            }
            for (String string : arrayList) {
                scoreboard.removePlayerFromTeam(string, scorePlayerTeam);
            }
            CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.teams.empty.success", arrayList.size(), scorePlayerTeam.getRegisteredName());
        }
    }

    protected void setPlayer(ICommandSender iCommandSender, String[] stringArray, int n) throws CommandException {
        Object object;
        Object object2;
        int n2;
        String string;
        String string2 = stringArray[n - 1];
        int n3 = n;
        if ((string = CommandScoreboard.getEntityName(iCommandSender, stringArray[n++])).length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", string, 40);
        }
        ScoreObjective scoreObjective = this.getObjective(stringArray[n++], true);
        int n4 = n2 = string2.equalsIgnoreCase("set") ? CommandScoreboard.parseInt(stringArray[n++]) : CommandScoreboard.parseInt(stringArray[n++], 0);
        if (stringArray.length > n) {
            object2 = CommandScoreboard.func_175768_b(iCommandSender, stringArray[n3]);
            object = JsonToNBT.getTagFromJson(CommandScoreboard.buildString(stringArray, n));
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            ((Entity)object2).writeToNBT(nBTTagCompound);
            if (!NBTUtil.func_181123_a((NBTBase)object, nBTTagCompound, true)) {
                throw new CommandException("commands.scoreboard.players.set.tagMismatch", string);
            }
        }
        object2 = this.getScoreboard();
        object = ((Scoreboard)object2).getValueFromObjective(string, scoreObjective);
        if (string2.equalsIgnoreCase("set")) {
            ((Score)object).setScorePoints(n2);
        } else if (string2.equalsIgnoreCase("add")) {
            ((Score)object).increseScore(n2);
        } else {
            ((Score)object).decreaseScore(n2);
        }
        CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.players.set.success", scoreObjective.getName(), string, ((Score)object).getScorePoints());
    }

    protected void func_175779_n(ICommandSender iCommandSender, String[] stringArray, int n) throws CommandException {
        String string;
        Scoreboard scoreboard = this.getScoreboard();
        if ((string = CommandScoreboard.getPlayerName(iCommandSender, stringArray[n++])).length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", string, 40);
        }
        ScoreObjective scoreObjective = this.getObjective(stringArray[n], false);
        if (scoreObjective.getCriteria() != IScoreObjectiveCriteria.TRIGGER) {
            throw new CommandException("commands.scoreboard.players.enable.noTrigger", scoreObjective.getName());
        }
        Score score = scoreboard.getValueFromObjective(string, scoreObjective);
        score.setLocked(false);
        CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.players.enable.success", scoreObjective.getName(), string);
    }

    protected Scoreboard getScoreboard() {
        return MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
    }

    protected List<String> func_147184_a(boolean bl) {
        Collection<ScoreObjective> collection = this.getScoreboard().getScoreObjectives();
        ArrayList arrayList = Lists.newArrayList();
        for (ScoreObjective scoreObjective : collection) {
            if (bl && scoreObjective.getCriteria().isReadOnly()) continue;
            arrayList.add(scoreObjective.getName());
        }
        return arrayList;
    }

    protected void removeObjective(ICommandSender iCommandSender, String string) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard();
        ScoreObjective scoreObjective = this.getObjective(string, false);
        scoreboard.removeObjective(scoreObjective);
        CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.objectives.remove.success", string);
    }

    protected void resetPlayers(ICommandSender iCommandSender, String[] stringArray, int n) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard();
        String string = CommandScoreboard.getEntityName(iCommandSender, stringArray[n++]);
        if (stringArray.length > n) {
            ScoreObjective scoreObjective = this.getObjective(stringArray[n++], false);
            scoreboard.removeObjectiveFromEntity(string, scoreObjective);
            CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.players.resetscore.success", scoreObjective.getName(), string);
        } else {
            scoreboard.removeObjectiveFromEntity(string, null);
            CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.players.reset.success", string);
        }
    }

    protected void addObjective(ICommandSender iCommandSender, String[] stringArray, int n) throws CommandException {
        String string = stringArray[n++];
        String string2 = stringArray[n++];
        Scoreboard scoreboard = this.getScoreboard();
        IScoreObjectiveCriteria iScoreObjectiveCriteria = IScoreObjectiveCriteria.INSTANCES.get(string2);
        if (iScoreObjectiveCriteria == null) {
            throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", string2);
        }
        if (scoreboard.getObjective(string) != null) {
            throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", string);
        }
        if (string.length() > 16) {
            throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", string, 16);
        }
        if (string.length() == 0) {
            throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
        }
        if (stringArray.length > n) {
            String string3 = CommandScoreboard.getChatComponentFromNthArg(iCommandSender, stringArray, n).getUnformattedText();
            if (string3.length() > 32) {
                throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", string3, 32);
            }
            if (string3.length() > 0) {
                scoreboard.addScoreObjective(string, iScoreObjectiveCriteria).setDisplayName(string3);
            } else {
                scoreboard.addScoreObjective(string, iScoreObjectiveCriteria);
            }
        } else {
            scoreboard.addScoreObjective(string, iScoreObjectiveCriteria);
        }
        CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.objectives.add.success", string);
    }

    protected void setTeamOption(ICommandSender iCommandSender, String[] stringArray, int n) throws CommandException {
        ScorePlayerTeam scorePlayerTeam;
        if ((scorePlayerTeam = this.getTeam(stringArray[n++])) != null) {
            String string;
            if (!((string = stringArray[n++].toLowerCase()).equalsIgnoreCase("color") || string.equalsIgnoreCase("friendlyfire") || string.equalsIgnoreCase("seeFriendlyInvisibles") || string.equalsIgnoreCase("nametagVisibility") || string.equalsIgnoreCase("deathMessageVisibility"))) {
                throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
            }
            if (stringArray.length == 4) {
                if (string.equalsIgnoreCase("color")) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", string, CommandScoreboard.joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false)));
                }
                if (!string.equalsIgnoreCase("friendlyfire") && !string.equalsIgnoreCase("seeFriendlyInvisibles")) {
                    if (!string.equalsIgnoreCase("nametagVisibility") && !string.equalsIgnoreCase("deathMessageVisibility")) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                    }
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", string, CommandScoreboard.joinNiceString(Team.EnumVisible.func_178825_a()));
                }
                throw new WrongUsageException("commands.scoreboard.teams.option.noValue", string, CommandScoreboard.joinNiceStringFromCollection(Arrays.asList("true", "false")));
            }
            String string2 = stringArray[n];
            if (string.equalsIgnoreCase("color")) {
                EnumChatFormatting enumChatFormatting = EnumChatFormatting.getValueByName(string2);
                if (enumChatFormatting == null || enumChatFormatting.isFancyStyling()) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", string, CommandScoreboard.joinNiceStringFromCollection(EnumChatFormatting.getValidValues(true, false)));
                }
                scorePlayerTeam.setChatFormat(enumChatFormatting);
                scorePlayerTeam.setNamePrefix(enumChatFormatting.toString());
                scorePlayerTeam.setNameSuffix(EnumChatFormatting.RESET.toString());
            } else if (string.equalsIgnoreCase("friendlyfire")) {
                if (!string2.equalsIgnoreCase("true") && !string2.equalsIgnoreCase("false")) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", string, CommandScoreboard.joinNiceStringFromCollection(Arrays.asList("true", "false")));
                }
                scorePlayerTeam.setAllowFriendlyFire(string2.equalsIgnoreCase("true"));
            } else if (string.equalsIgnoreCase("seeFriendlyInvisibles")) {
                if (!string2.equalsIgnoreCase("true") && !string2.equalsIgnoreCase("false")) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", string, CommandScoreboard.joinNiceStringFromCollection(Arrays.asList("true", "false")));
                }
                scorePlayerTeam.setSeeFriendlyInvisiblesEnabled(string2.equalsIgnoreCase("true"));
            } else if (string.equalsIgnoreCase("nametagVisibility")) {
                Team.EnumVisible enumVisible = Team.EnumVisible.func_178824_a(string2);
                if (enumVisible == null) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", string, CommandScoreboard.joinNiceString(Team.EnumVisible.func_178825_a()));
                }
                scorePlayerTeam.setNameTagVisibility(enumVisible);
            } else if (string.equalsIgnoreCase("deathMessageVisibility")) {
                Team.EnumVisible enumVisible = Team.EnumVisible.func_178824_a(string2);
                if (enumVisible == null) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", string, CommandScoreboard.joinNiceString(Team.EnumVisible.func_178825_a()));
                }
                scorePlayerTeam.setDeathMessageVisibility(enumVisible);
            }
            CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.teams.option.success", string, scorePlayerTeam.getRegisteredName(), string2);
        }
    }

    protected ScorePlayerTeam getTeam(String string) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard();
        ScorePlayerTeam scorePlayerTeam = scoreboard.getTeam(string);
        if (scorePlayerTeam == null) {
            throw new CommandException("commands.scoreboard.teamNotFound", string);
        }
        return scorePlayerTeam;
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        if (!this.func_175780_b(iCommandSender, stringArray)) {
            if (stringArray.length < 1) {
                throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
            }
            if (stringArray[0].equalsIgnoreCase("objectives")) {
                if (stringArray.length == 1) {
                    throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                }
                if (stringArray[1].equalsIgnoreCase("list")) {
                    this.listObjectives(iCommandSender);
                } else if (stringArray[1].equalsIgnoreCase("add")) {
                    if (stringArray.length < 4) {
                        throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
                    }
                    this.addObjective(iCommandSender, stringArray, 2);
                } else if (stringArray[1].equalsIgnoreCase("remove")) {
                    if (stringArray.length != 3) {
                        throw new WrongUsageException("commands.scoreboard.objectives.remove.usage", new Object[0]);
                    }
                    this.removeObjective(iCommandSender, stringArray[2]);
                } else {
                    if (!stringArray[1].equalsIgnoreCase("setdisplay")) {
                        throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                    }
                    if (stringArray.length != 3 && stringArray.length != 4) {
                        throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
                    }
                    this.setObjectiveDisplay(iCommandSender, stringArray, 2);
                }
            } else if (stringArray[0].equalsIgnoreCase("players")) {
                if (stringArray.length == 1) {
                    throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                }
                if (stringArray[1].equalsIgnoreCase("list")) {
                    if (stringArray.length > 3) {
                        throw new WrongUsageException("commands.scoreboard.players.list.usage", new Object[0]);
                    }
                    this.listPlayers(iCommandSender, stringArray, 2);
                } else if (stringArray[1].equalsIgnoreCase("add")) {
                    if (stringArray.length < 5) {
                        throw new WrongUsageException("commands.scoreboard.players.add.usage", new Object[0]);
                    }
                    this.setPlayer(iCommandSender, stringArray, 2);
                } else if (stringArray[1].equalsIgnoreCase("remove")) {
                    if (stringArray.length < 5) {
                        throw new WrongUsageException("commands.scoreboard.players.remove.usage", new Object[0]);
                    }
                    this.setPlayer(iCommandSender, stringArray, 2);
                } else if (stringArray[1].equalsIgnoreCase("set")) {
                    if (stringArray.length < 5) {
                        throw new WrongUsageException("commands.scoreboard.players.set.usage", new Object[0]);
                    }
                    this.setPlayer(iCommandSender, stringArray, 2);
                } else if (stringArray[1].equalsIgnoreCase("reset")) {
                    if (stringArray.length != 3 && stringArray.length != 4) {
                        throw new WrongUsageException("commands.scoreboard.players.reset.usage", new Object[0]);
                    }
                    this.resetPlayers(iCommandSender, stringArray, 2);
                } else if (stringArray[1].equalsIgnoreCase("enable")) {
                    if (stringArray.length != 4) {
                        throw new WrongUsageException("commands.scoreboard.players.enable.usage", new Object[0]);
                    }
                    this.func_175779_n(iCommandSender, stringArray, 2);
                } else if (stringArray[1].equalsIgnoreCase("test")) {
                    if (stringArray.length != 5 && stringArray.length != 6) {
                        throw new WrongUsageException("commands.scoreboard.players.test.usage", new Object[0]);
                    }
                    this.func_175781_o(iCommandSender, stringArray, 2);
                } else {
                    if (!stringArray[1].equalsIgnoreCase("operation")) {
                        throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                    }
                    if (stringArray.length != 7) {
                        throw new WrongUsageException("commands.scoreboard.players.operation.usage", new Object[0]);
                    }
                    this.func_175778_p(iCommandSender, stringArray, 2);
                }
            } else {
                if (!stringArray[0].equalsIgnoreCase("teams")) {
                    throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
                }
                if (stringArray.length == 1) {
                    throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                }
                if (stringArray[1].equalsIgnoreCase("list")) {
                    if (stringArray.length > 3) {
                        throw new WrongUsageException("commands.scoreboard.teams.list.usage", new Object[0]);
                    }
                    this.listTeams(iCommandSender, stringArray, 2);
                } else if (stringArray[1].equalsIgnoreCase("add")) {
                    if (stringArray.length < 3) {
                        throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
                    }
                    this.addTeam(iCommandSender, stringArray, 2);
                } else if (stringArray[1].equalsIgnoreCase("remove")) {
                    if (stringArray.length != 3) {
                        throw new WrongUsageException("commands.scoreboard.teams.remove.usage", new Object[0]);
                    }
                    this.removeTeam(iCommandSender, stringArray, 2);
                } else if (stringArray[1].equalsIgnoreCase("empty")) {
                    if (stringArray.length != 3) {
                        throw new WrongUsageException("commands.scoreboard.teams.empty.usage", new Object[0]);
                    }
                    this.emptyTeam(iCommandSender, stringArray, 2);
                } else if (stringArray[1].equalsIgnoreCase("join")) {
                    if (!(stringArray.length >= 4 || stringArray.length == 3 && iCommandSender instanceof EntityPlayer)) {
                        throw new WrongUsageException("commands.scoreboard.teams.join.usage", new Object[0]);
                    }
                    this.joinTeam(iCommandSender, stringArray, 2);
                } else if (stringArray[1].equalsIgnoreCase("leave")) {
                    if (stringArray.length < 3 && !(iCommandSender instanceof EntityPlayer)) {
                        throw new WrongUsageException("commands.scoreboard.teams.leave.usage", new Object[0]);
                    }
                    this.leaveTeam(iCommandSender, stringArray, 2);
                } else {
                    if (!stringArray[1].equalsIgnoreCase("option")) {
                        throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                    }
                    if (stringArray.length != 4 && stringArray.length != 5) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                    }
                    this.setTeamOption(iCommandSender, stringArray, 2);
                }
            }
        }
    }

    protected void listPlayers(ICommandSender iCommandSender, String[] stringArray, int n) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard();
        if (stringArray.length > n) {
            String string = CommandScoreboard.getEntityName(iCommandSender, stringArray[n]);
            Map<ScoreObjective, Score> map = scoreboard.getObjectivesForEntity(string);
            iCommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, map.size());
            if (map.size() <= 0) {
                throw new CommandException("commands.scoreboard.players.list.player.empty", string);
            }
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.scoreboard.players.list.player.count", map.size(), string);
            chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            iCommandSender.addChatMessage(chatComponentTranslation);
            for (Score score : map.values()) {
                iCommandSender.addChatMessage(new ChatComponentTranslation("commands.scoreboard.players.list.player.entry", score.getScorePoints(), score.getObjective().getDisplayName(), score.getObjective().getName()));
            }
        } else {
            Collection<String> collection = scoreboard.getObjectiveNames();
            iCommandSender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
            if (collection.size() <= 0) {
                throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
            }
            ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.scoreboard.players.list.count", collection.size());
            chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
            iCommandSender.addChatMessage(chatComponentTranslation);
            iCommandSender.addChatMessage(new ChatComponentText(CommandScoreboard.joinNiceString(collection.toArray())));
        }
    }

    private boolean func_175780_b(ICommandSender iCommandSender, String[] stringArray) throws CommandException {
        int n = -1;
        int n2 = 0;
        while (n2 < stringArray.length) {
            if (this.isUsernameIndex(stringArray, n2) && "*".equals(stringArray[n2])) {
                if (n >= 0) {
                    throw new CommandException("commands.scoreboard.noMultiWildcard", new Object[0]);
                }
                n = n2;
            }
            ++n2;
        }
        if (n < 0) {
            return false;
        }
        ArrayList arrayList = Lists.newArrayList(this.getScoreboard().getObjectiveNames());
        String string = stringArray[n];
        ArrayList arrayList2 = Lists.newArrayList();
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            String string2;
            stringArray[n] = string2 = (String)iterator.next();
            try {
                this.processCommand(iCommandSender, stringArray);
                arrayList2.add(string2);
            }
            catch (CommandException commandException) {
                ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation(commandException.getMessage(), commandException.getErrorObjects());
                chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.RED);
                iCommandSender.addChatMessage(chatComponentTranslation);
            }
        }
        stringArray[n] = string;
        iCommandSender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, arrayList2.size());
        if (arrayList2.size() == 0) {
            throw new WrongUsageException("commands.scoreboard.allMatchesFailed", new Object[0]);
        }
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    protected void addTeam(ICommandSender iCommandSender, String[] stringArray, int n) throws CommandException {
        String string = stringArray[n++];
        Scoreboard scoreboard = this.getScoreboard();
        if (scoreboard.getTeam(string) != null) {
            throw new CommandException("commands.scoreboard.teams.add.alreadyExists", string);
        }
        if (string.length() > 16) {
            throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", string, 16);
        }
        if (string.length() == 0) {
            throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
        }
        if (stringArray.length > n) {
            String string2 = CommandScoreboard.getChatComponentFromNthArg(iCommandSender, stringArray, n).getUnformattedText();
            if (string2.length() > 32) {
                throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", string2, 32);
            }
            if (string2.length() > 0) {
                scoreboard.createTeam(string).setTeamName(string2);
            } else {
                scoreboard.createTeam(string);
            }
        } else {
            scoreboard.createTeam(string);
        }
        CommandScoreboard.notifyOperators(iCommandSender, (ICommand)this, "commands.scoreboard.teams.add.success", string);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender iCommandSender, String[] stringArray, BlockPos blockPos) {
        if (stringArray.length == 1) {
            return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, "objectives", "players", "teams");
        }
        if (stringArray[0].equalsIgnoreCase("objectives")) {
            if (stringArray.length == 2) {
                return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, "list", "add", "remove", "setdisplay");
            }
            if (stringArray[1].equalsIgnoreCase("add")) {
                if (stringArray.length == 4) {
                    Set<String> set = IScoreObjectiveCriteria.INSTANCES.keySet();
                    return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, set);
                }
            } else if (stringArray[1].equalsIgnoreCase("remove")) {
                if (stringArray.length == 3) {
                    return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, this.func_147184_a(false));
                }
            } else if (stringArray[1].equalsIgnoreCase("setdisplay")) {
                if (stringArray.length == 3) {
                    return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, Scoreboard.getDisplaySlotStrings());
                }
                if (stringArray.length == 4) {
                    return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, this.func_147184_a(false));
                }
            }
        } else if (stringArray[0].equalsIgnoreCase("players")) {
            if (stringArray.length == 2) {
                return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, "set", "add", "remove", "reset", "list", "enable", "test", "operation");
            }
            if (!(stringArray[1].equalsIgnoreCase("set") || stringArray[1].equalsIgnoreCase("add") || stringArray[1].equalsIgnoreCase("remove") || stringArray[1].equalsIgnoreCase("reset"))) {
                if (stringArray[1].equalsIgnoreCase("enable")) {
                    if (stringArray.length == 3) {
                        return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames());
                    }
                    if (stringArray.length == 4) {
                        return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, this.func_175782_e());
                    }
                } else if (!stringArray[1].equalsIgnoreCase("list") && !stringArray[1].equalsIgnoreCase("test")) {
                    if (stringArray[1].equalsIgnoreCase("operation")) {
                        if (stringArray.length == 3) {
                            return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, this.getScoreboard().getObjectiveNames());
                        }
                        if (stringArray.length == 4) {
                            return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, this.func_147184_a(true));
                        }
                        if (stringArray.length == 5) {
                            return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, "+=", "-=", "*=", "/=", "%=", "=", "<", ">", "><");
                        }
                        if (stringArray.length == 6) {
                            return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames());
                        }
                        if (stringArray.length == 7) {
                            return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, this.func_147184_a(false));
                        }
                    }
                } else {
                    if (stringArray.length == 3) {
                        return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, this.getScoreboard().getObjectiveNames());
                    }
                    if (stringArray.length == 4 && stringArray[1].equalsIgnoreCase("test")) {
                        return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, this.func_147184_a(false));
                    }
                }
            } else {
                if (stringArray.length == 3) {
                    return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames());
                }
                if (stringArray.length == 4) {
                    return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, this.func_147184_a(true));
                }
            }
        } else if (stringArray[0].equalsIgnoreCase("teams")) {
            if (stringArray.length == 2) {
                return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, "add", "remove", "join", "leave", "empty", "list", "option");
            }
            if (stringArray[1].equalsIgnoreCase("join")) {
                if (stringArray.length == 3) {
                    return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, this.getScoreboard().getTeamNames());
                }
                if (stringArray.length >= 4) {
                    return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames());
                }
            } else {
                if (stringArray[1].equalsIgnoreCase("leave")) {
                    return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, MinecraftServer.getServer().getAllUsernames());
                }
                if (!(stringArray[1].equalsIgnoreCase("empty") || stringArray[1].equalsIgnoreCase("list") || stringArray[1].equalsIgnoreCase("remove"))) {
                    if (stringArray[1].equalsIgnoreCase("option")) {
                        if (stringArray.length == 3) {
                            return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, this.getScoreboard().getTeamNames());
                        }
                        if (stringArray.length == 4) {
                            return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, "color", "friendlyfire", "seeFriendlyInvisibles", "nametagVisibility", "deathMessageVisibility");
                        }
                        if (stringArray.length == 5) {
                            if (stringArray[3].equalsIgnoreCase("color")) {
                                return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, EnumChatFormatting.getValidValues(true, false));
                            }
                            if (stringArray[3].equalsIgnoreCase("nametagVisibility") || stringArray[3].equalsIgnoreCase("deathMessageVisibility")) {
                                return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, Team.EnumVisible.func_178825_a());
                            }
                            if (stringArray[3].equalsIgnoreCase("friendlyfire") || stringArray[3].equalsIgnoreCase("seeFriendlyInvisibles")) {
                                return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, "true", "false");
                            }
                        }
                    }
                } else if (stringArray.length == 3) {
                    return CommandScoreboard.getListOfStringsMatchingLastWord(stringArray, this.getScoreboard().getTeamNames());
                }
            }
        }
        return null;
    }

    protected void listObjectives(ICommandSender iCommandSender) throws CommandException {
        Scoreboard scoreboard = this.getScoreboard();
        Collection<ScoreObjective> collection = scoreboard.getScoreObjectives();
        if (collection.size() <= 0) {
            throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
        }
        ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation("commands.scoreboard.objectives.list.count", collection.size());
        chatComponentTranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
        iCommandSender.addChatMessage(chatComponentTranslation);
        for (ScoreObjective scoreObjective : collection) {
            iCommandSender.addChatMessage(new ChatComponentTranslation("commands.scoreboard.objectives.list.entry", scoreObjective.getName(), scoreObjective.getDisplayName(), scoreObjective.getCriteria().getName()));
        }
    }

    @Override
    public String getCommandName() {
        return "scoreboard";
    }
}

