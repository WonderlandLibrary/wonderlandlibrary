// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command.server;

import java.util.Collections;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.JsonToNBT;
import java.util.Map;
import net.minecraft.scoreboard.Score;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.command.EntitySelector;
import com.google.common.collect.Sets;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.scoreboard.Team;
import java.util.Collection;
import java.util.Arrays;
import java.util.Locale;
import net.minecraft.command.ICommand;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandResultStats;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextComponentTranslation;
import com.google.common.collect.Lists;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandScoreboard extends CommandBase
{
    @Override
    public String getName() {
        return "scoreboard";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.scoreboard.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (!this.handleUserWildcards(server, sender, args)) {
            if (args.length < 1) {
                throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
            }
            if ("objectives".equalsIgnoreCase(args[0])) {
                if (args.length == 1) {
                    throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                }
                if ("list".equalsIgnoreCase(args[1])) {
                    this.listObjectives(sender, server);
                }
                else if ("add".equalsIgnoreCase(args[1])) {
                    if (args.length < 4) {
                        throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
                    }
                    this.addObjective(sender, args, 2, server);
                }
                else if ("remove".equalsIgnoreCase(args[1])) {
                    if (args.length != 3) {
                        throw new WrongUsageException("commands.scoreboard.objectives.remove.usage", new Object[0]);
                    }
                    this.removeObjective(sender, args[2], server);
                }
                else {
                    if (!"setdisplay".equalsIgnoreCase(args[1])) {
                        throw new WrongUsageException("commands.scoreboard.objectives.usage", new Object[0]);
                    }
                    if (args.length != 3 && args.length != 4) {
                        throw new WrongUsageException("commands.scoreboard.objectives.setdisplay.usage", new Object[0]);
                    }
                    this.setDisplayObjective(sender, args, 2, server);
                }
            }
            else if ("players".equalsIgnoreCase(args[0])) {
                if (args.length == 1) {
                    throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                }
                if ("list".equalsIgnoreCase(args[1])) {
                    if (args.length > 3) {
                        throw new WrongUsageException("commands.scoreboard.players.list.usage", new Object[0]);
                    }
                    this.listPlayers(sender, args, 2, server);
                }
                else if ("add".equalsIgnoreCase(args[1])) {
                    if (args.length < 5) {
                        throw new WrongUsageException("commands.scoreboard.players.add.usage", new Object[0]);
                    }
                    this.addPlayerScore(sender, args, 2, server);
                }
                else if ("remove".equalsIgnoreCase(args[1])) {
                    if (args.length < 5) {
                        throw new WrongUsageException("commands.scoreboard.players.remove.usage", new Object[0]);
                    }
                    this.addPlayerScore(sender, args, 2, server);
                }
                else if ("set".equalsIgnoreCase(args[1])) {
                    if (args.length < 5) {
                        throw new WrongUsageException("commands.scoreboard.players.set.usage", new Object[0]);
                    }
                    this.addPlayerScore(sender, args, 2, server);
                }
                else if ("reset".equalsIgnoreCase(args[1])) {
                    if (args.length != 3 && args.length != 4) {
                        throw new WrongUsageException("commands.scoreboard.players.reset.usage", new Object[0]);
                    }
                    this.resetPlayerScore(sender, args, 2, server);
                }
                else if ("enable".equalsIgnoreCase(args[1])) {
                    if (args.length != 4) {
                        throw new WrongUsageException("commands.scoreboard.players.enable.usage", new Object[0]);
                    }
                    this.enablePlayerTrigger(sender, args, 2, server);
                }
                else if ("test".equalsIgnoreCase(args[1])) {
                    if (args.length != 5 && args.length != 6) {
                        throw new WrongUsageException("commands.scoreboard.players.test.usage", new Object[0]);
                    }
                    this.testPlayerScore(sender, args, 2, server);
                }
                else if ("operation".equalsIgnoreCase(args[1])) {
                    if (args.length != 7) {
                        throw new WrongUsageException("commands.scoreboard.players.operation.usage", new Object[0]);
                    }
                    this.applyPlayerOperation(sender, args, 2, server);
                }
                else {
                    if (!"tag".equalsIgnoreCase(args[1])) {
                        throw new WrongUsageException("commands.scoreboard.players.usage", new Object[0]);
                    }
                    if (args.length < 4) {
                        throw new WrongUsageException("commands.scoreboard.players.tag.usage", new Object[0]);
                    }
                    this.applyPlayerTag(server, sender, args, 2);
                }
            }
            else {
                if (!"teams".equalsIgnoreCase(args[0])) {
                    throw new WrongUsageException("commands.scoreboard.usage", new Object[0]);
                }
                if (args.length == 1) {
                    throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                }
                if ("list".equalsIgnoreCase(args[1])) {
                    if (args.length > 3) {
                        throw new WrongUsageException("commands.scoreboard.teams.list.usage", new Object[0]);
                    }
                    this.listTeams(sender, args, 2, server);
                }
                else if ("add".equalsIgnoreCase(args[1])) {
                    if (args.length < 3) {
                        throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
                    }
                    this.addTeam(sender, args, 2, server);
                }
                else if ("remove".equalsIgnoreCase(args[1])) {
                    if (args.length != 3) {
                        throw new WrongUsageException("commands.scoreboard.teams.remove.usage", new Object[0]);
                    }
                    this.removeTeam(sender, args, 2, server);
                }
                else if ("empty".equalsIgnoreCase(args[1])) {
                    if (args.length != 3) {
                        throw new WrongUsageException("commands.scoreboard.teams.empty.usage", new Object[0]);
                    }
                    this.emptyTeam(sender, args, 2, server);
                }
                else if ("join".equalsIgnoreCase(args[1])) {
                    if (args.length < 4 && (args.length != 3 || !(sender instanceof EntityPlayer))) {
                        throw new WrongUsageException("commands.scoreboard.teams.join.usage", new Object[0]);
                    }
                    this.joinTeam(sender, args, 2, server);
                }
                else if ("leave".equalsIgnoreCase(args[1])) {
                    if (args.length < 3 && !(sender instanceof EntityPlayer)) {
                        throw new WrongUsageException("commands.scoreboard.teams.leave.usage", new Object[0]);
                    }
                    this.leaveTeam(sender, args, 2, server);
                }
                else {
                    if (!"option".equalsIgnoreCase(args[1])) {
                        throw new WrongUsageException("commands.scoreboard.teams.usage", new Object[0]);
                    }
                    if (args.length != 4 && args.length != 5) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
                    }
                    this.setTeamOption(sender, args, 2, server);
                }
            }
        }
    }
    
    private boolean handleUserWildcards(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        int i = -1;
        for (int j = 0; j < args.length; ++j) {
            if (this.isUsernameIndex(args, j) && "*".equals(args[j])) {
                if (i >= 0) {
                    throw new CommandException("commands.scoreboard.noMultiWildcard", new Object[0]);
                }
                i = j;
            }
        }
        if (i < 0) {
            return false;
        }
        final List<String> list1 = (List<String>)Lists.newArrayList((Iterable)this.getScoreboard(server).getObjectiveNames());
        final String s = args[i];
        final List<String> list2 = (List<String>)Lists.newArrayList();
        for (final String s2 : list1) {
            args[i] = s2;
            try {
                this.execute(server, sender, args);
                list2.add(s2);
            }
            catch (CommandException commandexception) {
                final TextComponentTranslation textcomponenttranslation = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
                textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(textcomponenttranslation);
            }
        }
        args[i] = s;
        sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list2.size());
        if (list2.isEmpty()) {
            throw new WrongUsageException("commands.scoreboard.allMatchesFailed", new Object[0]);
        }
        return true;
    }
    
    protected Scoreboard getScoreboard(final MinecraftServer server) {
        return server.getWorld(0).getScoreboard();
    }
    
    protected ScoreObjective convertToObjective(final String name, final boolean forWrite, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        final ScoreObjective scoreobjective = scoreboard.getObjective(name);
        if (scoreobjective == null) {
            throw new CommandException("commands.scoreboard.objectiveNotFound", new Object[] { name });
        }
        if (forWrite && scoreobjective.getCriteria().isReadOnly()) {
            throw new CommandException("commands.scoreboard.objectiveReadOnly", new Object[] { name });
        }
        return scoreobjective;
    }
    
    protected ScorePlayerTeam convertToTeam(final String name, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        final ScorePlayerTeam scoreplayerteam = scoreboard.getTeam(name);
        if (scoreplayerteam == null) {
            throw new CommandException("commands.scoreboard.teamNotFound", new Object[] { name });
        }
        return scoreplayerteam;
    }
    
    protected void addObjective(final ICommandSender sender, final String[] commandArgs, int argStartIndex, final MinecraftServer server) throws CommandException {
        final String s = commandArgs[argStartIndex++];
        final String s2 = commandArgs[argStartIndex++];
        final Scoreboard scoreboard = this.getScoreboard(server);
        final IScoreCriteria iscorecriteria = IScoreCriteria.INSTANCES.get(s2);
        if (iscorecriteria == null) {
            throw new WrongUsageException("commands.scoreboard.objectives.add.wrongType", new Object[] { s2 });
        }
        if (scoreboard.getObjective(s) != null) {
            throw new CommandException("commands.scoreboard.objectives.add.alreadyExists", new Object[] { s });
        }
        if (s.length() > 16) {
            throw new SyntaxErrorException("commands.scoreboard.objectives.add.tooLong", new Object[] { s, 16 });
        }
        if (s.isEmpty()) {
            throw new WrongUsageException("commands.scoreboard.objectives.add.usage", new Object[0]);
        }
        if (commandArgs.length > argStartIndex) {
            final String s3 = CommandBase.getChatComponentFromNthArg(sender, commandArgs, argStartIndex).getUnformattedText();
            if (s3.length() > 32) {
                throw new SyntaxErrorException("commands.scoreboard.objectives.add.displayTooLong", new Object[] { s3, 32 });
            }
            if (s3.isEmpty()) {
                scoreboard.addScoreObjective(s, iscorecriteria);
            }
            else {
                scoreboard.addScoreObjective(s, iscorecriteria).setDisplayName(s3);
            }
        }
        else {
            scoreboard.addScoreObjective(s, iscorecriteria);
        }
        CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.objectives.add.success", s);
    }
    
    protected void addTeam(final ICommandSender sender, final String[] args, int startIndex, final MinecraftServer server) throws CommandException {
        final String s = args[startIndex++];
        final Scoreboard scoreboard = this.getScoreboard(server);
        if (scoreboard.getTeam(s) != null) {
            throw new CommandException("commands.scoreboard.teams.add.alreadyExists", new Object[] { s });
        }
        if (s.length() > 16) {
            throw new SyntaxErrorException("commands.scoreboard.teams.add.tooLong", new Object[] { s, 16 });
        }
        if (s.isEmpty()) {
            throw new WrongUsageException("commands.scoreboard.teams.add.usage", new Object[0]);
        }
        if (args.length > startIndex) {
            final String s2 = CommandBase.getChatComponentFromNthArg(sender, args, startIndex).getUnformattedText();
            if (s2.length() > 32) {
                throw new SyntaxErrorException("commands.scoreboard.teams.add.displayTooLong", new Object[] { s2, 32 });
            }
            if (s2.isEmpty()) {
                scoreboard.createTeam(s);
            }
            else {
                scoreboard.createTeam(s).setDisplayName(s2);
            }
        }
        else {
            scoreboard.createTeam(s);
        }
        CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.teams.add.success", s);
    }
    
    protected void setTeamOption(final ICommandSender sender, final String[] args, int startIndex, final MinecraftServer server) throws CommandException {
        final ScorePlayerTeam scoreplayerteam = this.convertToTeam(args[startIndex++], server);
        if (scoreplayerteam != null) {
            final String s = args[startIndex++].toLowerCase(Locale.ROOT);
            if (!"color".equalsIgnoreCase(s) && !"friendlyfire".equalsIgnoreCase(s) && !"seeFriendlyInvisibles".equalsIgnoreCase(s) && !"nametagVisibility".equalsIgnoreCase(s) && !"deathMessageVisibility".equalsIgnoreCase(s) && !"collisionRule".equalsIgnoreCase(s)) {
                throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
            }
            if (args.length == 4) {
                if ("color".equalsIgnoreCase(s)) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceStringFromCollection(TextFormatting.getValidValues(true, false)) });
                }
                if ("friendlyfire".equalsIgnoreCase(s) || "seeFriendlyInvisibles".equalsIgnoreCase(s)) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceStringFromCollection(Arrays.asList("true", "false")) });
                }
                if ("nametagVisibility".equalsIgnoreCase(s) || "deathMessageVisibility".equalsIgnoreCase(s)) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceString(Team.EnumVisible.getNames()) });
                }
                if ("collisionRule".equalsIgnoreCase(s)) {
                    throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceString(Team.CollisionRule.getNames()) });
                }
                throw new WrongUsageException("commands.scoreboard.teams.option.usage", new Object[0]);
            }
            else {
                final String s2 = args[startIndex];
                if ("color".equalsIgnoreCase(s)) {
                    final TextFormatting textformatting = TextFormatting.getValueByName(s2);
                    if (textformatting == null || textformatting.isFancyStyling()) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceStringFromCollection(TextFormatting.getValidValues(true, false)) });
                    }
                    scoreplayerteam.setColor(textformatting);
                    scoreplayerteam.setPrefix(textformatting.toString());
                    scoreplayerteam.setSuffix(TextFormatting.RESET.toString());
                }
                else if ("friendlyfire".equalsIgnoreCase(s)) {
                    if (!"true".equalsIgnoreCase(s2) && !"false".equalsIgnoreCase(s2)) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceStringFromCollection(Arrays.asList("true", "false")) });
                    }
                    scoreplayerteam.setAllowFriendlyFire("true".equalsIgnoreCase(s2));
                }
                else if ("seeFriendlyInvisibles".equalsIgnoreCase(s)) {
                    if (!"true".equalsIgnoreCase(s2) && !"false".equalsIgnoreCase(s2)) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceStringFromCollection(Arrays.asList("true", "false")) });
                    }
                    scoreplayerteam.setSeeFriendlyInvisiblesEnabled("true".equalsIgnoreCase(s2));
                }
                else if ("nametagVisibility".equalsIgnoreCase(s)) {
                    final Team.EnumVisible team$enumvisible = Team.EnumVisible.getByName(s2);
                    if (team$enumvisible == null) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceString(Team.EnumVisible.getNames()) });
                    }
                    scoreplayerteam.setNameTagVisibility(team$enumvisible);
                }
                else if ("deathMessageVisibility".equalsIgnoreCase(s)) {
                    final Team.EnumVisible team$enumvisible2 = Team.EnumVisible.getByName(s2);
                    if (team$enumvisible2 == null) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceString(Team.EnumVisible.getNames()) });
                    }
                    scoreplayerteam.setDeathMessageVisibility(team$enumvisible2);
                }
                else if ("collisionRule".equalsIgnoreCase(s)) {
                    final Team.CollisionRule team$collisionrule = Team.CollisionRule.getByName(s2);
                    if (team$collisionrule == null) {
                        throw new WrongUsageException("commands.scoreboard.teams.option.noValue", new Object[] { s, CommandBase.joinNiceString(Team.CollisionRule.getNames()) });
                    }
                    scoreplayerteam.setCollisionRule(team$collisionrule);
                }
                CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.teams.option.success", s, scoreplayerteam.getName(), s2);
            }
        }
    }
    
    protected void removeTeam(final ICommandSender sender, final String[] args, final int startIndex, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        final ScorePlayerTeam scoreplayerteam = this.convertToTeam(args[startIndex], server);
        if (scoreplayerteam != null) {
            scoreboard.removeTeam(scoreplayerteam);
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.teams.remove.success", scoreplayerteam.getName());
        }
    }
    
    protected void listTeams(final ICommandSender sender, final String[] args, final int startIndex, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        if (args.length > startIndex) {
            final ScorePlayerTeam scoreplayerteam = this.convertToTeam(args[startIndex], server);
            if (scoreplayerteam == null) {
                return;
            }
            final Collection<String> collection = scoreplayerteam.getMembershipCollection();
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
            if (collection.isEmpty()) {
                throw new CommandException("commands.scoreboard.teams.list.player.empty", new Object[] { scoreplayerteam.getName() });
            }
            final TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.scoreboard.teams.list.player.count", new Object[] { collection.size(), scoreplayerteam.getName() });
            textcomponenttranslation.getStyle().setColor(TextFormatting.DARK_GREEN);
            sender.sendMessage(textcomponenttranslation);
            sender.sendMessage(new TextComponentString(CommandBase.joinNiceString(collection.toArray())));
        }
        else {
            final Collection<ScorePlayerTeam> collection2 = scoreboard.getTeams();
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection2.size());
            if (collection2.isEmpty()) {
                throw new CommandException("commands.scoreboard.teams.list.empty", new Object[0]);
            }
            final TextComponentTranslation textcomponenttranslation2 = new TextComponentTranslation("commands.scoreboard.teams.list.count", new Object[] { collection2.size() });
            textcomponenttranslation2.getStyle().setColor(TextFormatting.DARK_GREEN);
            sender.sendMessage(textcomponenttranslation2);
            for (final ScorePlayerTeam scoreplayerteam2 : collection2) {
                sender.sendMessage(new TextComponentTranslation("commands.scoreboard.teams.list.entry", new Object[] { scoreplayerteam2.getName(), scoreplayerteam2.getDisplayName(), scoreplayerteam2.getMembershipCollection().size() }));
            }
        }
    }
    
    protected void joinTeam(final ICommandSender sender, final String[] args, int startIndex, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        final String s = args[startIndex++];
        final Set<String> set = (Set<String>)Sets.newHashSet();
        final Set<String> set2 = (Set<String>)Sets.newHashSet();
        if (sender instanceof EntityPlayer && startIndex == args.length) {
            final String s2 = CommandBase.getCommandSenderAsPlayer(sender).getName();
            if (scoreboard.addPlayerToTeam(s2, s)) {
                set.add(s2);
            }
            else {
                set2.add(s2);
            }
        }
        else {
            while (startIndex < args.length) {
                final String s3 = args[startIndex++];
                if (EntitySelector.isSelector(s3)) {
                    for (final Entity entity : CommandBase.getEntityList(server, sender, s3)) {
                        final String s4 = CommandBase.getEntityName(server, sender, entity.getCachedUniqueIdString());
                        if (scoreboard.addPlayerToTeam(s4, s)) {
                            set.add(s4);
                        }
                        else {
                            set2.add(s4);
                        }
                    }
                }
                else {
                    final String s5 = CommandBase.getEntityName(server, sender, s3);
                    if (scoreboard.addPlayerToTeam(s5, s)) {
                        set.add(s5);
                    }
                    else {
                        set2.add(s5);
                    }
                }
            }
        }
        if (!set.isEmpty()) {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, set.size());
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.teams.join.success", set.size(), s, CommandBase.joinNiceString(set.toArray(new String[set.size()])));
        }
        if (!set2.isEmpty()) {
            throw new CommandException("commands.scoreboard.teams.join.failure", new Object[] { set2.size(), s, CommandBase.joinNiceString(set2.toArray(new String[set2.size()])) });
        }
    }
    
    protected void leaveTeam(final ICommandSender sender, final String[] args, int startIndex, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        final Set<String> set = (Set<String>)Sets.newHashSet();
        final Set<String> set2 = (Set<String>)Sets.newHashSet();
        if (sender instanceof EntityPlayer && startIndex == args.length) {
            final String s3 = CommandBase.getCommandSenderAsPlayer(sender).getName();
            if (scoreboard.removePlayerFromTeams(s3)) {
                set.add(s3);
            }
            else {
                set2.add(s3);
            }
        }
        else {
            while (startIndex < args.length) {
                final String s4 = args[startIndex++];
                if (EntitySelector.isSelector(s4)) {
                    for (final Entity entity : CommandBase.getEntityList(server, sender, s4)) {
                        final String s5 = CommandBase.getEntityName(server, sender, entity.getCachedUniqueIdString());
                        if (scoreboard.removePlayerFromTeams(s5)) {
                            set.add(s5);
                        }
                        else {
                            set2.add(s5);
                        }
                    }
                }
                else {
                    final String s6 = CommandBase.getEntityName(server, sender, s4);
                    if (scoreboard.removePlayerFromTeams(s6)) {
                        set.add(s6);
                    }
                    else {
                        set2.add(s6);
                    }
                }
            }
        }
        if (!set.isEmpty()) {
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, set.size());
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.teams.leave.success", set.size(), CommandBase.joinNiceString(set.toArray(new String[set.size()])));
        }
        if (!set2.isEmpty()) {
            throw new CommandException("commands.scoreboard.teams.leave.failure", new Object[] { set2.size(), CommandBase.joinNiceString(set2.toArray(new String[set2.size()])) });
        }
    }
    
    protected void emptyTeam(final ICommandSender sender, final String[] args, final int startIndex, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        final ScorePlayerTeam scoreplayerteam = this.convertToTeam(args[startIndex], server);
        if (scoreplayerteam != null) {
            final Collection<String> collection = (Collection<String>)Lists.newArrayList((Iterable)scoreplayerteam.getMembershipCollection());
            sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, collection.size());
            if (collection.isEmpty()) {
                throw new CommandException("commands.scoreboard.teams.empty.alreadyEmpty", new Object[] { scoreplayerteam.getName() });
            }
            for (final String s : collection) {
                scoreboard.removePlayerFromTeam(s, scoreplayerteam);
            }
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.teams.empty.success", collection.size(), scoreplayerteam.getName());
        }
    }
    
    protected void removeObjective(final ICommandSender sender, final String name, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        final ScoreObjective scoreobjective = this.convertToObjective(name, false, server);
        scoreboard.removeObjective(scoreobjective);
        CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.objectives.remove.success", name);
    }
    
    protected void listObjectives(final ICommandSender sender, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        final Collection<ScoreObjective> collection = scoreboard.getScoreObjectives();
        if (collection.isEmpty()) {
            throw new CommandException("commands.scoreboard.objectives.list.empty", new Object[0]);
        }
        final TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.scoreboard.objectives.list.count", new Object[] { collection.size() });
        textcomponenttranslation.getStyle().setColor(TextFormatting.DARK_GREEN);
        sender.sendMessage(textcomponenttranslation);
        for (final ScoreObjective scoreobjective : collection) {
            sender.sendMessage(new TextComponentTranslation("commands.scoreboard.objectives.list.entry", new Object[] { scoreobjective.getName(), scoreobjective.getDisplayName(), scoreobjective.getCriteria().getName() }));
        }
    }
    
    protected void setDisplayObjective(final ICommandSender sender, final String[] args, int startIndex, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        final String s = args[startIndex++];
        final int i = Scoreboard.getObjectiveDisplaySlotNumber(s);
        ScoreObjective scoreobjective = null;
        if (args.length == 4) {
            scoreobjective = this.convertToObjective(args[startIndex], false, server);
        }
        if (i < 0) {
            throw new CommandException("commands.scoreboard.objectives.setdisplay.invalidSlot", new Object[] { s });
        }
        scoreboard.setObjectiveInDisplaySlot(i, scoreobjective);
        if (scoreobjective != null) {
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.objectives.setdisplay.successSet", Scoreboard.getObjectiveDisplaySlot(i), scoreobjective.getName());
        }
        else {
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.objectives.setdisplay.successCleared", Scoreboard.getObjectiveDisplaySlot(i));
        }
    }
    
    protected void listPlayers(final ICommandSender sender, final String[] args, final int startIndex, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        if (args.length > startIndex) {
            final String s = CommandBase.getEntityName(server, sender, args[startIndex]);
            final Map<ScoreObjective, Score> map = scoreboard.getObjectivesForEntity(s);
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, map.size());
            if (map.isEmpty()) {
                throw new CommandException("commands.scoreboard.players.list.player.empty", new Object[] { s });
            }
            final TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.scoreboard.players.list.player.count", new Object[] { map.size(), s });
            textcomponenttranslation.getStyle().setColor(TextFormatting.DARK_GREEN);
            sender.sendMessage(textcomponenttranslation);
            for (final Score score : map.values()) {
                sender.sendMessage(new TextComponentTranslation("commands.scoreboard.players.list.player.entry", new Object[] { score.getScorePoints(), score.getObjective().getDisplayName(), score.getObjective().getName() }));
            }
        }
        else {
            final Collection<String> collection = scoreboard.getObjectiveNames();
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, collection.size());
            if (collection.isEmpty()) {
                throw new CommandException("commands.scoreboard.players.list.empty", new Object[0]);
            }
            final TextComponentTranslation textcomponenttranslation2 = new TextComponentTranslation("commands.scoreboard.players.list.count", new Object[] { collection.size() });
            textcomponenttranslation2.getStyle().setColor(TextFormatting.DARK_GREEN);
            sender.sendMessage(textcomponenttranslation2);
            sender.sendMessage(new TextComponentString(CommandBase.joinNiceString(collection.toArray())));
        }
    }
    
    protected void addPlayerScore(final ICommandSender sender, final String[] args, int startIndex, final MinecraftServer server) throws CommandException {
        final String s = args[startIndex - 1];
        final int i = startIndex;
        final String s2 = CommandBase.getEntityName(server, sender, args[startIndex++]);
        if (s2.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s2, 40 });
        }
        final ScoreObjective scoreobjective = this.convertToObjective(args[startIndex++], true, server);
        final int j = "set".equalsIgnoreCase(s) ? CommandBase.parseInt(args[startIndex++]) : CommandBase.parseInt(args[startIndex++], 0);
        if (args.length > startIndex) {
            final Entity entity = CommandBase.getEntity(server, sender, args[i]);
            try {
                final NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(CommandBase.buildString(args, startIndex));
                final NBTTagCompound nbttagcompound2 = CommandBase.entityToNBT(entity);
                if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound2, true)) {
                    throw new CommandException("commands.scoreboard.players.set.tagMismatch", new Object[] { s2 });
                }
            }
            catch (NBTException nbtexception) {
                throw new CommandException("commands.scoreboard.players.set.tagError", new Object[] { nbtexception.getMessage() });
            }
        }
        final Scoreboard scoreboard = this.getScoreboard(server);
        final Score score = scoreboard.getOrCreateScore(s2, scoreobjective);
        if ("set".equalsIgnoreCase(s)) {
            score.setScorePoints(j);
        }
        else if ("add".equalsIgnoreCase(s)) {
            score.increaseScore(j);
        }
        else {
            score.decreaseScore(j);
        }
        CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.set.success", scoreobjective.getName(), s2, score.getScorePoints());
    }
    
    protected void resetPlayerScore(final ICommandSender sender, final String[] args, int startIndex, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        final String s = CommandBase.getEntityName(server, sender, args[startIndex++]);
        if (args.length > startIndex) {
            final ScoreObjective scoreobjective = this.convertToObjective(args[startIndex++], false, server);
            scoreboard.removeObjectiveFromEntity(s, scoreobjective);
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.resetscore.success", scoreobjective.getName(), s);
        }
        else {
            scoreboard.removeObjectiveFromEntity(s, null);
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.reset.success", s);
        }
    }
    
    protected void enablePlayerTrigger(final ICommandSender sender, final String[] args, int startIndex, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        final String s = CommandBase.getPlayerName(server, sender, args[startIndex++]);
        if (s.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, 40 });
        }
        final ScoreObjective scoreobjective = this.convertToObjective(args[startIndex], false, server);
        if (scoreobjective.getCriteria() != IScoreCriteria.TRIGGER) {
            throw new CommandException("commands.scoreboard.players.enable.noTrigger", new Object[] { scoreobjective.getName() });
        }
        final Score score = scoreboard.getOrCreateScore(s, scoreobjective);
        score.setLocked(false);
        CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.enable.success", scoreobjective.getName(), s);
    }
    
    protected void testPlayerScore(final ICommandSender sender, final String[] args, int startIndex, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        final String s = CommandBase.getEntityName(server, sender, args[startIndex++]);
        if (s.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, 40 });
        }
        final ScoreObjective scoreobjective = this.convertToObjective(args[startIndex++], false, server);
        if (!scoreboard.entityHasObjective(s, scoreobjective)) {
            throw new CommandException("commands.scoreboard.players.test.notFound", new Object[] { scoreobjective.getName(), s });
        }
        final int i = args[startIndex].equals("*") ? Integer.MIN_VALUE : CommandBase.parseInt(args[startIndex]);
        final int j = (++startIndex < args.length && !args[startIndex].equals("*")) ? CommandBase.parseInt(args[startIndex], i) : Integer.MAX_VALUE;
        final Score score = scoreboard.getOrCreateScore(s, scoreobjective);
        if (score.getScorePoints() >= i && score.getScorePoints() <= j) {
            CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.test.success", score.getScorePoints(), i, j);
            return;
        }
        throw new CommandException("commands.scoreboard.players.test.failed", new Object[] { score.getScorePoints(), i, j });
    }
    
    protected void applyPlayerOperation(final ICommandSender sender, final String[] args, int startIndex, final MinecraftServer server) throws CommandException {
        final Scoreboard scoreboard = this.getScoreboard(server);
        final String s = CommandBase.getEntityName(server, sender, args[startIndex++]);
        final ScoreObjective scoreobjective = this.convertToObjective(args[startIndex++], true, server);
        final String s2 = args[startIndex++];
        final String s3 = CommandBase.getEntityName(server, sender, args[startIndex++]);
        final ScoreObjective scoreobjective2 = this.convertToObjective(args[startIndex], false, server);
        if (s.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s, 40 });
        }
        if (s3.length() > 40) {
            throw new SyntaxErrorException("commands.scoreboard.players.name.tooLong", new Object[] { s3, 40 });
        }
        final Score score = scoreboard.getOrCreateScore(s, scoreobjective);
        if (!scoreboard.entityHasObjective(s3, scoreobjective2)) {
            throw new CommandException("commands.scoreboard.players.operation.notFound", new Object[] { scoreobjective2.getName(), s3 });
        }
        final Score score2 = scoreboard.getOrCreateScore(s3, scoreobjective2);
        if ("+=".equals(s2)) {
            score.setScorePoints(score.getScorePoints() + score2.getScorePoints());
        }
        else if ("-=".equals(s2)) {
            score.setScorePoints(score.getScorePoints() - score2.getScorePoints());
        }
        else if ("*=".equals(s2)) {
            score.setScorePoints(score.getScorePoints() * score2.getScorePoints());
        }
        else if ("/=".equals(s2)) {
            if (score2.getScorePoints() != 0) {
                score.setScorePoints(score.getScorePoints() / score2.getScorePoints());
            }
        }
        else if ("%=".equals(s2)) {
            if (score2.getScorePoints() != 0) {
                score.setScorePoints(score.getScorePoints() % score2.getScorePoints());
            }
        }
        else if ("=".equals(s2)) {
            score.setScorePoints(score2.getScorePoints());
        }
        else if ("<".equals(s2)) {
            score.setScorePoints(Math.min(score.getScorePoints(), score2.getScorePoints()));
        }
        else if (">".equals(s2)) {
            score.setScorePoints(Math.max(score.getScorePoints(), score2.getScorePoints()));
        }
        else {
            if (!"><".equals(s2)) {
                throw new CommandException("commands.scoreboard.players.operation.invalidOperation", new Object[] { s2 });
            }
            final int i = score.getScorePoints();
            score.setScorePoints(score2.getScorePoints());
            score2.setScorePoints(i);
        }
        CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.operation.success", new Object[0]);
    }
    
    protected void applyPlayerTag(final MinecraftServer server, final ICommandSender sender, final String[] args, int startIndex) throws CommandException {
        final String s = CommandBase.getEntityName(server, sender, args[startIndex]);
        final Entity entity = CommandBase.getEntity(server, sender, args[startIndex++]);
        final String s2 = args[startIndex++];
        final Set<String> set = entity.getTags();
        if ("list".equals(s2)) {
            if (!set.isEmpty()) {
                final TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.scoreboard.players.tag.list", new Object[] { s });
                textcomponenttranslation.getStyle().setColor(TextFormatting.DARK_GREEN);
                sender.sendMessage(textcomponenttranslation);
                sender.sendMessage(new TextComponentString(CommandBase.joinNiceString(set.toArray())));
            }
            sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, set.size());
        }
        else {
            if (args.length < 5) {
                throw new WrongUsageException("commands.scoreboard.players.tag.usage", new Object[0]);
            }
            final String s3 = args[startIndex++];
            if (args.length > startIndex) {
                try {
                    final NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(CommandBase.buildString(args, startIndex));
                    final NBTTagCompound nbttagcompound2 = CommandBase.entityToNBT(entity);
                    if (!NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound2, true)) {
                        throw new CommandException("commands.scoreboard.players.tag.tagMismatch", new Object[] { s });
                    }
                }
                catch (NBTException nbtexception) {
                    throw new CommandException("commands.scoreboard.players.tag.tagError", new Object[] { nbtexception.getMessage() });
                }
            }
            if ("add".equals(s2)) {
                if (!entity.addTag(s3)) {
                    throw new CommandException("commands.scoreboard.players.tag.tooMany", new Object[] { 1024 });
                }
                CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.tag.success.add", s3);
            }
            else {
                if (!"remove".equals(s2)) {
                    throw new WrongUsageException("commands.scoreboard.players.tag.usage", new Object[0]);
                }
                if (!entity.removeTag(s3)) {
                    throw new CommandException("commands.scoreboard.players.tag.notFound", new Object[] { s3 });
                }
                CommandBase.notifyCommandListener(sender, this, "commands.scoreboard.players.tag.success.remove", s3);
            }
        }
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "objectives", "players", "teams");
        }
        if ("objectives".equalsIgnoreCase(args[0])) {
            if (args.length == 2) {
                return CommandBase.getListOfStringsMatchingLastWord(args, "list", "add", "remove", "setdisplay");
            }
            if ("add".equalsIgnoreCase(args[1])) {
                if (args.length == 4) {
                    final Set<String> set = IScoreCriteria.INSTANCES.keySet();
                    return CommandBase.getListOfStringsMatchingLastWord(args, set);
                }
            }
            else if ("remove".equalsIgnoreCase(args[1])) {
                if (args.length == 3) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, this.getObjectiveNames(false, server));
                }
            }
            else if ("setdisplay".equalsIgnoreCase(args[1])) {
                if (args.length == 3) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, Scoreboard.getDisplaySlotStrings());
                }
                if (args.length == 4) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, this.getObjectiveNames(false, server));
                }
            }
        }
        else if ("players".equalsIgnoreCase(args[0])) {
            if (args.length == 2) {
                return CommandBase.getListOfStringsMatchingLastWord(args, "set", "add", "remove", "reset", "list", "enable", "test", "operation", "tag");
            }
            if (!"set".equalsIgnoreCase(args[1]) && !"add".equalsIgnoreCase(args[1]) && !"remove".equalsIgnoreCase(args[1]) && !"reset".equalsIgnoreCase(args[1])) {
                if ("enable".equalsIgnoreCase(args[1])) {
                    if (args.length == 3) {
                        return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
                    }
                    if (args.length == 4) {
                        return CommandBase.getListOfStringsMatchingLastWord(args, this.getTriggerNames(server));
                    }
                }
                else if (!"list".equalsIgnoreCase(args[1]) && !"test".equalsIgnoreCase(args[1])) {
                    if ("operation".equalsIgnoreCase(args[1])) {
                        if (args.length == 3) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, this.getScoreboard(server).getObjectiveNames());
                        }
                        if (args.length == 4) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, this.getObjectiveNames(true, server));
                        }
                        if (args.length == 5) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, "+=", "-=", "*=", "/=", "%=", "=", "<", ">", "><");
                        }
                        if (args.length == 6) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
                        }
                        if (args.length == 7) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, this.getObjectiveNames(false, server));
                        }
                    }
                    else if ("tag".equalsIgnoreCase(args[1])) {
                        if (args.length == 3) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, this.getScoreboard(server).getObjectiveNames());
                        }
                        if (args.length == 4) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove", "list");
                        }
                    }
                }
                else {
                    if (args.length == 3) {
                        return CommandBase.getListOfStringsMatchingLastWord(args, this.getScoreboard(server).getObjectiveNames());
                    }
                    if (args.length == 4 && "test".equalsIgnoreCase(args[1])) {
                        return CommandBase.getListOfStringsMatchingLastWord(args, this.getObjectiveNames(false, server));
                    }
                }
            }
            else {
                if (args.length == 3) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
                }
                if (args.length == 4) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, this.getObjectiveNames(true, server));
                }
            }
        }
        else if ("teams".equalsIgnoreCase(args[0])) {
            if (args.length == 2) {
                return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove", "join", "leave", "empty", "list", "option");
            }
            if ("join".equalsIgnoreCase(args[1])) {
                if (args.length == 3) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, this.getScoreboard(server).getTeamNames());
                }
                if (args.length >= 4) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
                }
            }
            else {
                if ("leave".equalsIgnoreCase(args[1])) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
                }
                if (!"empty".equalsIgnoreCase(args[1]) && !"list".equalsIgnoreCase(args[1]) && !"remove".equalsIgnoreCase(args[1])) {
                    if ("option".equalsIgnoreCase(args[1])) {
                        if (args.length == 3) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, this.getScoreboard(server).getTeamNames());
                        }
                        if (args.length == 4) {
                            return CommandBase.getListOfStringsMatchingLastWord(args, "color", "friendlyfire", "seeFriendlyInvisibles", "nametagVisibility", "deathMessageVisibility", "collisionRule");
                        }
                        if (args.length == 5) {
                            if ("color".equalsIgnoreCase(args[3])) {
                                return CommandBase.getListOfStringsMatchingLastWord(args, TextFormatting.getValidValues(true, false));
                            }
                            if ("nametagVisibility".equalsIgnoreCase(args[3]) || "deathMessageVisibility".equalsIgnoreCase(args[3])) {
                                return CommandBase.getListOfStringsMatchingLastWord(args, Team.EnumVisible.getNames());
                            }
                            if ("collisionRule".equalsIgnoreCase(args[3])) {
                                return CommandBase.getListOfStringsMatchingLastWord(args, Team.CollisionRule.getNames());
                            }
                            if ("friendlyfire".equalsIgnoreCase(args[3]) || "seeFriendlyInvisibles".equalsIgnoreCase(args[3])) {
                                return CommandBase.getListOfStringsMatchingLastWord(args, "true", "false");
                            }
                        }
                    }
                }
                else if (args.length == 3) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, this.getScoreboard(server).getTeamNames());
                }
            }
        }
        return Collections.emptyList();
    }
    
    protected List<String> getObjectiveNames(final boolean writableOnly, final MinecraftServer server) {
        final Collection<ScoreObjective> collection = this.getScoreboard(server).getScoreObjectives();
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final ScoreObjective scoreobjective : collection) {
            if (!writableOnly || !scoreobjective.getCriteria().isReadOnly()) {
                list.add(scoreobjective.getName());
            }
        }
        return list;
    }
    
    protected List<String> getTriggerNames(final MinecraftServer server) {
        final Collection<ScoreObjective> collection = this.getScoreboard(server).getScoreObjectives();
        final List<String> list = (List<String>)Lists.newArrayList();
        for (final ScoreObjective scoreobjective : collection) {
            if (scoreobjective.getCriteria() == IScoreCriteria.TRIGGER) {
                list.add(scoreobjective.getName());
            }
        }
        return list;
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        if (!"players".equalsIgnoreCase(args[0])) {
            return "teams".equalsIgnoreCase(args[0]) && index == 2;
        }
        if (args.length > 1 && "operation".equalsIgnoreCase(args[1])) {
            return index == 2 || index == 5;
        }
        return index == 2;
    }
}
