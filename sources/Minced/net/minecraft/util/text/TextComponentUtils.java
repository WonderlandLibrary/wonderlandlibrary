// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.text;

import net.minecraft.command.CommandException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.command.ICommandSender;

public class TextComponentUtils
{
    public static ITextComponent processComponent(final ICommandSender commandSender, final ITextComponent component, final Entity entityIn) throws CommandException {
        ITextComponent itextcomponent;
        if (component instanceof TextComponentScore) {
            final TextComponentScore textcomponentscore = (TextComponentScore)component;
            String s = textcomponentscore.getName();
            if (EntitySelector.isSelector(s)) {
                final List<Entity> list = EntitySelector.matchEntities(commandSender, s, (Class<? extends Entity>)Entity.class);
                if (list.size() != 1) {
                    throw new EntityNotFoundException("commands.generic.selector.notFound", new Object[] { s });
                }
                final Entity entity = list.get(0);
                if (entity instanceof EntityPlayer) {
                    s = entity.getName();
                }
                else {
                    s = entity.getCachedUniqueIdString();
                }
            }
            final String s2 = (entityIn != null && s.equals("*")) ? entityIn.getName() : s;
            itextcomponent = new TextComponentScore(s2, textcomponentscore.getObjective());
            ((TextComponentScore)itextcomponent).setValue(textcomponentscore.getUnformattedComponentText());
            ((TextComponentScore)itextcomponent).resolve(commandSender);
        }
        else if (component instanceof TextComponentSelector) {
            final String s3 = ((TextComponentSelector)component).getSelector();
            itextcomponent = EntitySelector.matchEntitiesToTextComponent(commandSender, s3);
            if (itextcomponent == null) {
                itextcomponent = new TextComponentString("");
            }
        }
        else if (component instanceof TextComponentString) {
            itextcomponent = new TextComponentString(((TextComponentString)component).getText());
        }
        else if (component instanceof TextComponentKeybind) {
            itextcomponent = new TextComponentKeybind(((TextComponentKeybind)component).getKeybind());
        }
        else {
            if (!(component instanceof TextComponentTranslation)) {
                return component;
            }
            final Object[] aobject = ((TextComponentTranslation)component).getFormatArgs();
            for (int i = 0; i < aobject.length; ++i) {
                final Object object = aobject[i];
                if (object instanceof ITextComponent) {
                    aobject[i] = processComponent(commandSender, (ITextComponent)object, entityIn);
                }
            }
            itextcomponent = new TextComponentTranslation(((TextComponentTranslation)component).getKey(), aobject);
        }
        final Style style = component.getStyle();
        if (style != null) {
            itextcomponent.setStyle(style.createShallowCopy());
        }
        for (final ITextComponent itextcomponent2 : component.getSiblings()) {
            itextcomponent.appendSibling(processComponent(commandSender, itextcomponent2, entityIn));
        }
        return itextcomponent;
    }
}
