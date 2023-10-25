/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.datatypes;

import baritone.api.command.datatypes.IDatatypeContext;
import baritone.api.command.datatypes.IDatatypeFor;
import baritone.api.command.exception.CommandException;
import baritone.api.command.helpers.TabCompleteHelper;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.entity.player.EntityPlayer;

public enum NearbyPlayer implements IDatatypeFor<EntityPlayer>
{
    INSTANCE;


    @Override
    public EntityPlayer get(IDatatypeContext ctx) throws CommandException {
        String username = ctx.getConsumer().getString();
        return NearbyPlayer.getPlayers(ctx).stream().filter(s -> s.getName().equalsIgnoreCase(username)).findFirst().orElse(null);
    }

    @Override
    public Stream<String> tabComplete(IDatatypeContext ctx) throws CommandException {
        return new TabCompleteHelper().append(NearbyPlayer.getPlayers(ctx).stream().map(EntityPlayer::getName)).filterPrefix(ctx.getConsumer().getString()).sortAlphabetically().stream();
    }

    private static List<EntityPlayer> getPlayers(IDatatypeContext ctx) {
        return ctx.getBaritone().getPlayerContext().world().playerEntities;
    }
}

