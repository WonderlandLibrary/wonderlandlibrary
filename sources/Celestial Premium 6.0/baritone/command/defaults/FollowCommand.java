/*
 * Decompiled with CFR 0.150.
 */
package baritone.command.defaults;

import baritone.KeepName;
import baritone.api.IBaritone;
import baritone.api.command.Command;
import baritone.api.command.argument.IArgConsumer;
import baritone.api.command.datatypes.EntityClassById;
import baritone.api.command.datatypes.IDatatypeFor;
import baritone.api.command.datatypes.NearbyPlayer;
import baritone.api.command.exception.CommandException;
import baritone.api.command.helpers.TabCompleteHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class FollowCommand
extends Command {
    public FollowCommand(IBaritone baritone) {
        super(baritone, "follow");
    }

    @Override
    public void execute(String label, IArgConsumer args) throws CommandException {
        FollowGroup group;
        args.requireMin(1);
        ArrayList<Entity> entities = new ArrayList<Entity>();
        ArrayList<Class> classes = new ArrayList<Class>();
        if (args.hasExactlyOne()) {
            group = args.getEnum(FollowGroup.class);
            this.baritone.getFollowProcess().follow(group.filter);
        } else {
            args.requireMin(2);
            group = null;
            FollowList list = args.getEnum(FollowList.class);
            while (args.hasAny()) {
                Object gotten = args.getDatatypeFor(list.datatype);
                if (gotten instanceof Class) {
                    classes.add((Class)gotten);
                    continue;
                }
                entities.add((Entity)gotten);
            }
            this.baritone.getFollowProcess().follow(classes.isEmpty() ? entities::contains : e -> classes.stream().anyMatch(c -> c.isInstance(e)));
        }
        if (group != null) {
            this.logDirect(String.format("Following all %s", group.name().toLowerCase(Locale.US)));
        } else {
            this.logDirect("Following these types of entities:");
            if (classes.isEmpty()) {
                entities.stream().map(Entity::toString).forEach(this::logDirect);
            } else {
                classes.stream().map(EntityList::getKey).map(Objects::requireNonNull).map(ResourceLocation::toString).forEach(this::logDirect);
            }
        }
    }

    @Override
    public Stream<String> tabComplete(String label, IArgConsumer args) throws CommandException {
        IDatatypeFor followType;
        if (args.hasExactlyOne()) {
            return new TabCompleteHelper().append(FollowGroup.class).append(FollowList.class).filterPrefix(args.getString()).stream();
        }
        try {
            followType = args.getEnum(FollowList.class).datatype;
        }
        catch (NullPointerException e) {
            return Stream.empty();
        }
        while (args.has(2)) {
            if (args.peekDatatypeOrNull(followType) == null) {
                return Stream.empty();
            }
            args.get();
        }
        return args.tabCompleteDatatype(followType);
    }

    @Override
    public String getShortDesc() {
        return "Follow entity things";
    }

    @Override
    public List<String> getLongDesc() {
        return Arrays.asList("The follow command tells Baritone to follow certain kinds of entities.", "", "Usage:", "> follow entities - Follows all entities.", "> follow entity <entity1> <entity2> <...> - Follow certain entities (for example 'skeleton', 'horse' etc.)", "> follow players - Follow players", "> follow player <username1> <username2> <...> - Follow certain players");
    }

    @KeepName
    private static enum FollowList {
        ENTITY(EntityClassById.INSTANCE),
        PLAYER(NearbyPlayer.INSTANCE);

        final IDatatypeFor datatype;

        private FollowList(IDatatypeFor datatype) {
            this.datatype = datatype;
        }
    }

    @KeepName
    private static enum FollowGroup {
        ENTITIES(EntityLiving.class::isInstance),
        PLAYERS(EntityPlayer.class::isInstance);

        final Predicate<Entity> filter;

        private FollowGroup(Predicate<Entity> filter) {
            this.filter = filter;
        }
    }
}

