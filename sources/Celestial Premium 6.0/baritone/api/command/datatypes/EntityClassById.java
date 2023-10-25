/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.datatypes;

import baritone.api.command.datatypes.IDatatypeContext;
import baritone.api.command.datatypes.IDatatypeFor;
import baritone.api.command.exception.CommandException;
import baritone.api.command.helpers.TabCompleteHelper;
import java.util.stream.Stream;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

public enum EntityClassById implements IDatatypeFor<Class<? extends Entity>>
{
    INSTANCE;


    @Override
    public Class<? extends Entity> get(IDatatypeContext ctx) throws CommandException {
        Class entity;
        ResourceLocation id = new ResourceLocation(ctx.getConsumer().getString());
        try {
            entity = EntityList.REGISTRY.getObject(id);
        }
        catch (NoSuchFieldError e) {
            try {
                entity = (Class)EntityList.class.getMethod("getClass", ResourceLocation.class).invoke(null, id);
            }
            catch (Exception ex) {
                throw new RuntimeException("EntityList.REGISTRY does not exist and failed to call the Forge-replacement method", ex);
            }
        }
        if (entity == null) {
            throw new IllegalArgumentException("no entity found by that id");
        }
        return entity;
    }

    @Override
    public Stream<String> tabComplete(IDatatypeContext ctx) throws CommandException {
        return new TabCompleteHelper().append(EntityList.getEntityNameList().stream().map(Object::toString)).filterPrefixNamespaced(ctx.getConsumer().getString()).sortAlphabetically().stream();
    }
}

