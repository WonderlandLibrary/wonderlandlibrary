// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.command;

import java.util.Collections;
import net.minecraft.util.ResourceLocation;
import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.PlayerAdvancements;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class AdvancementCommand extends CommandBase
{
    @Override
    public String getName() {
        return "advancement";
    }
    
    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
    
    @Override
    public String getUsage(final ICommandSender sender) {
        return "commands.advancement.usage";
    }
    
    @Override
    public void execute(final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException("commands.advancement.usage", new Object[0]);
        }
        final ActionType advancementcommand$actiontype = ActionType.byName(args[0]);
        if (advancementcommand$actiontype != null) {
            if (args.length < 3) {
                throw advancementcommand$actiontype.wrongUsage();
            }
            final EntityPlayerMP entityplayermp = CommandBase.getPlayer(server, sender, args[1]);
            final Mode advancementcommand$mode = Mode.byName(args[2]);
            if (advancementcommand$mode == null) {
                throw advancementcommand$actiontype.wrongUsage();
            }
            this.perform(server, sender, args, entityplayermp, advancementcommand$actiontype, advancementcommand$mode);
        }
        else {
            if (!"test".equals(args[0])) {
                throw new WrongUsageException("commands.advancement.usage", new Object[0]);
            }
            if (args.length == 3) {
                this.testAdvancement(sender, CommandBase.getPlayer(server, sender, args[1]), findAdvancement(server, args[2]));
            }
            else {
                if (args.length != 4) {
                    throw new WrongUsageException("commands.advancement.test.usage", new Object[0]);
                }
                this.testCriterion(sender, CommandBase.getPlayer(server, sender, args[1]), findAdvancement(server, args[2]), args[3]);
            }
        }
    }
    
    private void perform(final MinecraftServer server, final ICommandSender sender, final String[] args, final EntityPlayerMP player, final ActionType p_193516_5_, final Mode p_193516_6_) throws CommandException {
        if (p_193516_6_ == Mode.EVERYTHING) {
            if (args.length != 3) {
                throw p_193516_6_.usage(p_193516_5_);
            }
            final int j = p_193516_5_.perform(player, server.getAdvancementManager().getAdvancements());
            if (j == 0) {
                throw p_193516_6_.fail(p_193516_5_, player.getName());
            }
            p_193516_6_.success(sender, this, p_193516_5_, player.getName(), j);
        }
        else {
            if (args.length < 4) {
                throw p_193516_6_.usage(p_193516_5_);
            }
            final Advancement advancement = findAdvancement(server, args[3]);
            if (p_193516_6_ == Mode.ONLY && args.length == 5) {
                final String s = args[4];
                if (!advancement.getCriteria().keySet().contains(s)) {
                    throw new CommandException("commands.advancement.criterionNotFound", new Object[] { advancement.getId(), args[4] });
                }
                if (!p_193516_5_.performCriterion(player, advancement, s)) {
                    throw new CommandException(p_193516_5_.baseTranslationKey + ".criterion.failed", new Object[] { advancement.getId(), player.getName(), s });
                }
                CommandBase.notifyCommandListener(sender, this, p_193516_5_.baseTranslationKey + ".criterion.success", advancement.getId(), player.getName(), s);
            }
            else {
                if (args.length != 4) {
                    throw p_193516_6_.usage(p_193516_5_);
                }
                final List<Advancement> list = this.getAdvancements(advancement, p_193516_6_);
                final int i = p_193516_5_.perform(player, list);
                if (i == 0) {
                    throw p_193516_6_.fail(p_193516_5_, advancement.getId(), player.getName());
                }
                p_193516_6_.success(sender, this, p_193516_5_, advancement.getId(), player.getName(), i);
            }
        }
    }
    
    private void addChildren(final Advancement p_193515_1_, final List<Advancement> p_193515_2_) {
        for (final Advancement advancement : p_193515_1_.getChildren()) {
            p_193515_2_.add(advancement);
            this.addChildren(advancement, p_193515_2_);
        }
    }
    
    private List<Advancement> getAdvancements(final Advancement p_193514_1_, final Mode p_193514_2_) {
        final List<Advancement> list = (List<Advancement>)Lists.newArrayList();
        if (p_193514_2_.parents) {
            for (Advancement advancement = p_193514_1_.getParent(); advancement != null; advancement = advancement.getParent()) {
                list.add(advancement);
            }
        }
        list.add(p_193514_1_);
        if (p_193514_2_.children) {
            this.addChildren(p_193514_1_, list);
        }
        return list;
    }
    
    private void testCriterion(final ICommandSender p_192554_1_, final EntityPlayerMP p_192554_2_, final Advancement p_192554_3_, final String p_192554_4_) throws CommandException {
        final PlayerAdvancements playeradvancements = p_192554_2_.getAdvancements();
        final CriterionProgress criterionprogress = playeradvancements.getProgress(p_192554_3_).getCriterionProgress(p_192554_4_);
        if (criterionprogress == null) {
            throw new CommandException("commands.advancement.criterionNotFound", new Object[] { p_192554_3_.getId(), p_192554_4_ });
        }
        if (!criterionprogress.isObtained()) {
            throw new CommandException("commands.advancement.test.criterion.notDone", new Object[] { p_192554_2_.getName(), p_192554_3_.getId(), p_192554_4_ });
        }
        CommandBase.notifyCommandListener(p_192554_1_, this, "commands.advancement.test.criterion.success", p_192554_2_.getName(), p_192554_3_.getId(), p_192554_4_);
    }
    
    private void testAdvancement(final ICommandSender p_192552_1_, final EntityPlayerMP p_192552_2_, final Advancement p_192552_3_) throws CommandException {
        final AdvancementProgress advancementprogress = p_192552_2_.getAdvancements().getProgress(p_192552_3_);
        if (!advancementprogress.isDone()) {
            throw new CommandException("commands.advancement.test.advancement.notDone", new Object[] { p_192552_2_.getName(), p_192552_3_.getId() });
        }
        CommandBase.notifyCommandListener(p_192552_1_, this, "commands.advancement.test.advancement.success", p_192552_2_.getName(), p_192552_3_.getId());
    }
    
    @Override
    public List<String> getTabCompletions(final MinecraftServer server, final ICommandSender sender, final String[] args, @Nullable final BlockPos targetPos) {
        if (args.length == 1) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "grant", "revoke", "test");
        }
        final ActionType advancementcommand$actiontype = ActionType.byName(args[0]);
        if (advancementcommand$actiontype != null) {
            if (args.length == 2) {
                return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
            }
            if (args.length == 3) {
                return CommandBase.getListOfStringsMatchingLastWord(args, Mode.NAMES);
            }
            final Mode advancementcommand$mode = Mode.byName(args[2]);
            if (advancementcommand$mode != null && advancementcommand$mode != Mode.EVERYTHING) {
                if (args.length == 4) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, this.getAdvancementNames(server));
                }
                if (args.length == 5 && advancementcommand$mode == Mode.ONLY) {
                    final Advancement advancement = server.getAdvancementManager().getAdvancement(new ResourceLocation(args[3]));
                    if (advancement != null) {
                        return CommandBase.getListOfStringsMatchingLastWord(args, advancement.getCriteria().keySet());
                    }
                }
            }
        }
        if ("test".equals(args[0])) {
            if (args.length == 2) {
                return CommandBase.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
            }
            if (args.length == 3) {
                return CommandBase.getListOfStringsMatchingLastWord(args, this.getAdvancementNames(server));
            }
            if (args.length == 4) {
                final Advancement advancement2 = server.getAdvancementManager().getAdvancement(new ResourceLocation(args[2]));
                if (advancement2 != null) {
                    return CommandBase.getListOfStringsMatchingLastWord(args, advancement2.getCriteria().keySet());
                }
            }
        }
        return Collections.emptyList();
    }
    
    private List<ResourceLocation> getAdvancementNames(final MinecraftServer server) {
        final List<ResourceLocation> list = (List<ResourceLocation>)Lists.newArrayList();
        for (final Advancement advancement : server.getAdvancementManager().getAdvancements()) {
            list.add(advancement.getId());
        }
        return list;
    }
    
    @Override
    public boolean isUsernameIndex(final String[] args, final int index) {
        return args.length > 1 && ("grant".equals(args[0]) || "revoke".equals(args[0]) || "test".equals(args[0])) && index == 1;
    }
    
    public static Advancement findAdvancement(final MinecraftServer server, final String id) throws CommandException {
        final Advancement advancement = server.getAdvancementManager().getAdvancement(new ResourceLocation(id));
        if (advancement == null) {
            throw new CommandException("commands.advancement.advancementNotFound", new Object[] { id });
        }
        return advancement;
    }
    
    enum ActionType
    {
        GRANT("grant") {
            @Override
            protected boolean perform(final EntityPlayerMP p_193537_1_, final Advancement p_193537_2_) {
                final AdvancementProgress advancementprogress = p_193537_1_.getAdvancements().getProgress(p_193537_2_);
                if (advancementprogress.isDone()) {
                    return false;
                }
                for (final String s : advancementprogress.getRemaningCriteria()) {
                    p_193537_1_.getAdvancements().grantCriterion(p_193537_2_, s);
                }
                return true;
            }
            
            @Override
            protected boolean performCriterion(final EntityPlayerMP p_193535_1_, final Advancement p_193535_2_, final String p_193535_3_) {
                return p_193535_1_.getAdvancements().grantCriterion(p_193535_2_, p_193535_3_);
            }
        }, 
        REVOKE("revoke") {
            @Override
            protected boolean perform(final EntityPlayerMP p_193537_1_, final Advancement p_193537_2_) {
                final AdvancementProgress advancementprogress = p_193537_1_.getAdvancements().getProgress(p_193537_2_);
                if (!advancementprogress.hasProgress()) {
                    return false;
                }
                for (final String s : advancementprogress.getCompletedCriteria()) {
                    p_193537_1_.getAdvancements().revokeCriterion(p_193537_2_, s);
                }
                return true;
            }
            
            @Override
            protected boolean performCriterion(final EntityPlayerMP p_193535_1_, final Advancement p_193535_2_, final String p_193535_3_) {
                return p_193535_1_.getAdvancements().revokeCriterion(p_193535_2_, p_193535_3_);
            }
        };
        
        final String name;
        final String baseTranslationKey;
        
        private ActionType(final String nameIn) {
            this.name = nameIn;
            this.baseTranslationKey = "commands.advancement." + nameIn;
        }
        
        @Nullable
        static ActionType byName(final String nameIn) {
            for (final ActionType advancementcommand$actiontype : values()) {
                if (advancementcommand$actiontype.name.equals(nameIn)) {
                    return advancementcommand$actiontype;
                }
            }
            return null;
        }
        
        CommandException wrongUsage() {
            return new CommandException(this.baseTranslationKey + ".usage", new Object[0]);
        }
        
        public int perform(final EntityPlayerMP p_193532_1_, final Iterable<Advancement> p_193532_2_) {
            int i = 0;
            for (final Advancement advancement : p_193532_2_) {
                if (this.perform(p_193532_1_, advancement)) {
                    ++i;
                }
            }
            return i;
        }
        
        protected abstract boolean perform(final EntityPlayerMP p0, final Advancement p1);
        
        protected abstract boolean performCriterion(final EntityPlayerMP p0, final Advancement p1, final String p2);
    }
    
    enum Mode
    {
        ONLY("only", false, false), 
        THROUGH("through", true, true), 
        FROM("from", false, true), 
        UNTIL("until", true, false), 
        EVERYTHING("everything", true, true);
        
        static final String[] NAMES;
        final String name;
        final boolean parents;
        final boolean children;
        
        private Mode(final String p_i47556_3_, final boolean p_i47556_4_, final boolean p_i47556_5_) {
            this.name = p_i47556_3_;
            this.parents = p_i47556_4_;
            this.children = p_i47556_5_;
        }
        
        CommandException fail(final ActionType p_193543_1_, final Object... p_193543_2_) {
            return new CommandException(p_193543_1_.baseTranslationKey + "." + this.name + ".failed", p_193543_2_);
        }
        
        CommandException usage(final ActionType p_193544_1_) {
            return new CommandException(p_193544_1_.baseTranslationKey + "." + this.name + ".usage", new Object[0]);
        }
        
        void success(final ICommandSender sender, final AdvancementCommand p_193546_2_, final ActionType type, final Object... args) {
            CommandBase.notifyCommandListener(sender, p_193546_2_, type.baseTranslationKey + "." + this.name + ".success", args);
        }
        
        @Nullable
        static Mode byName(final String nameIn) {
            for (final Mode advancementcommand$mode : values()) {
                if (advancementcommand$mode.name.equals(nameIn)) {
                    return advancementcommand$mode;
                }
            }
            return null;
        }
        
        static {
            NAMES = new String[values().length];
            for (int i = 0; i < values().length; ++i) {
                Mode.NAMES[i] = values()[i].name;
            }
        }
    }
}
