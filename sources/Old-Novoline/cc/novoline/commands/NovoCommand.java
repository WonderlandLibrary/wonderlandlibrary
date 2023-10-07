package cc.novoline.commands;

import cc.novoline.Novoline;
import cc.novoline.utils.java.Lazy;
import cc.novoline.utils.messages.HelpMessage.UsageMessage;
import cc.novoline.utils.messages.MessageFactory;
import cc.novoline.utils.messages.TextMessage;
import cc.novoline.utils.notifications.NotificationType;
import static cc.novoline.utils.notifications.NotificationType.*;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.EnumChatFormatting.LIGHT_PURPLE;
import net.minecraft.util.IChatComponent;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class NovoCommand extends CommandBase {

    public static final TextMessage PREFIX = MessageFactory.text("Novoline", LIGHT_PURPLE).append(" \u00bb ", GRAY);
    public static final IChatComponent EMPTY_COMPONENT = new ChatComponentText("");

    /* fields */
    protected final Lazy<EntityPlayerSP> player = Lazy.create(() -> Minecraft.getInstance().player);

    protected final Novoline novoline;
    protected final String name;
    protected final String description;
    protected final List<String> aliases;

    /* constructors */
    protected NovoCommand(@NotNull Novoline novoline,
                          @NotNull String name,
                          @Nullable String description,
                          @Nullable Iterable<String> aliases) {
        this.novoline = novoline;
        this.name = name;
        this.description = description;
        this.aliases = aliases != null ? Lists.newArrayList(aliases) : Collections.emptyList();
    }

    protected NovoCommand(@NotNull Novoline novoline,
                          @NotNull String name,
                          @Nullable String description,
                          @Nullable String alias) {
        this(novoline, name, description, alias != null ? Collections.singleton(alias) : null);
    }

    protected NovoCommand(@NotNull Novoline novoline,
                          @NotNull String name,
                          @Nullable String alias) {
        this(novoline, name, null, alias != null ? Collections.singleton(alias) : null);
    }

    protected NovoCommand(@NotNull Novoline novoline,
                          @NotNull String name,
                          @Nullable Iterable<String> aliases) {
        this(novoline, name, null, aliases);
    }

    protected NovoCommand(@NotNull Novoline novoline,
                          @NotNull String name) {
        this(novoline, name, null, (Iterable<String>) null);
    }

    /* methods */
    public abstract void process(String[] args) throws CommandException, IOException;

    public @Nullable List<String> completeTabOptions(String[] args) {
        return null;
    }

    protected int getInt(String s) throws NumberInvalidException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new NumberInvalidException(s + " is not a valid number");
        }
    }

    protected double getDouble(String s) throws NumberInvalidException {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new NumberInvalidException(s + " is not a valid number");
        }
    }

    protected void send(@Nullable TextMessage component, boolean prefix) {
        EntityPlayerSP player = this.player.get();
        IChatComponent chatComponent;

        if (component != null) {
            if (prefix) {
                chatComponent = component.prefix(PREFIX);
            } else {
                chatComponent = component;
            }
        } else {
            chatComponent = EMPTY_COMPONENT;
        }

        player.addChatComponentMessage(chatComponent);
    }

    protected void send(@Nullable TextMessage component) {
        send(component, false);
    }

    protected void send(@Nullable String s, boolean prefix) {
        send(MessageFactory.text(s), prefix);
    }

    protected void send(@Nullable String s) {
        send(MessageFactory.text(s));
    }

    protected void send(@Nullable String s, @NotNull EnumChatFormatting color) {
        TextMessage text = MessageFactory.text(s);
        text.setColor(color);

        send(text);
    }

    protected void send(@Nullable Object o) {
        send(o != null ? o.toString() : null);
    }

    protected void sendEmpty() {
        player.get().addChatComponentMessage(MessageFactory.empty());
    }

    protected void sendUsage(@NotNull String command, @NotNull String description) {
        send(MessageFactory.usage(command, description));
    }

    protected void sendHelp(@NotNull String name, @NotNull String command, @NotNull UsageMessage... subCommands) {
        send(MessageFactory.help(name, command, subCommands), true);
    }

    protected void notify(@NotNull String s, int delay, @Nullable NotificationType type) {
        Novoline.getInstance().getNotificationManager().pop(s, delay, type);
    }

    protected void notify(@NotNull String s, int delay) {
        notify(s, delay, null);
    }

    protected void notify(@NotNull String s, @Nullable NotificationType type) {
        notify(s, 2_000, type);
    }

    protected void notify(@NotNull String s) {
        notify(s, 2_000, INFO);
    }

    protected void notifyClient(@NotNull String reason, @NotNull String text, int delay, NotificationType type) {
        novoline.getNotificationManager().pop(reason, text, delay, type);
    }

    protected void notifyWarning(@NotNull String s, int delay) {
        notify(s, delay, WARNING);
    }

    protected void notifyWarning(@NotNull String s) {
        notifyWarning(s, 2_000);
    }

    protected void notifyError(@NotNull String s, int delay) {
        notify(s, delay, ERROR);
    }

    protected void notifyError(@NotNull String s) {
        notifyError(s, 2_000);
    }

    protected List<String> completeTab(@NotNull Stream<String> lookIn, @NotNull String lookFor, boolean ignoreCase) {
        if(ignoreCase) {
            String lookForLowercase = lookFor.toLowerCase(Locale.ROOT);
            return lookIn.map(s -> s.toLowerCase(Locale.ROOT))
                    .filter(s -> s.startsWith(lookForLowercase))
                    .collect(Collectors.toCollection(ObjectArrayList::new));
        } else {
            return lookIn.filter(s -> s.startsWith(lookFor))
                    .collect(Collectors.toCollection(ObjectArrayList::new));
        }
    }

    protected List<String> completeTab(@NotNull Collection<String> lookIn, @NotNull String lookFor,
                                       boolean ignoreCase) {
        return completeTab(lookIn.stream(), lookFor, ignoreCase);
    }

    protected Logger getLogger() {
        return Novoline.getLogger();
    }

    @Override
    public final void processCommand(ICommandSender sender, String[] args) throws CommandException {
        try {
            process(args);
        } catch (CommandException e) {
            getLogger().warn(e);
            throw e;
        } catch (Throwable t) {
            getLogger().warn(t);
            throw new CommandException("Unexpected error occurred while executing command", t);
        }
    }

    @Override
    public final String getCommandName() {
        return name;
    }

    @Override
    public final List<String> getCommandAliases() {
        return aliases;
    }

    @Override
    public final @Nullable String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public final int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public final boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public final @Nullable List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return completeTabOptions(args);
    }

    @Override
    public final boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
}
