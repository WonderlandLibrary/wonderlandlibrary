/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.text;

import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.util.text.TextComponentScore;
import net.minecraft.util.text.TextComponentSelector;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class TextComponentUtils {
    public static ITextComponent processComponent(ICommandSender commandSender, ITextComponent component, Entity entityIn) throws CommandException {
        ITextComponent itextcomponent;
        if (component instanceof TextComponentScore) {
            TextComponentScore textcomponentscore = (TextComponentScore)component;
            String s = textcomponentscore.getName();
            if (EntitySelector.hasArguments(s)) {
                List<Entity> list = EntitySelector.matchEntities(commandSender, s, Entity.class);
                if (list.size() != 1) {
                    throw new EntityNotFoundException("commands.generic.selector.notFound", s);
                }
                Entity entity = list.get(0);
                s = entity instanceof EntityPlayer ? entity.getName() : entity.getCachedUniqueIdString();
            }
            String s2 = entityIn != null && s.equals("*") ? entityIn.getName() : s;
            itextcomponent = new TextComponentScore(s2, textcomponentscore.getObjective());
            ((TextComponentScore)itextcomponent).setValue(textcomponentscore.getUnformattedComponentText());
            ((TextComponentScore)itextcomponent).resolve(commandSender);
        } else if (component instanceof TextComponentSelector) {
            String s1 = ((TextComponentSelector)component).getSelector();
            itextcomponent = EntitySelector.matchEntitiesToTextComponent(commandSender, s1);
            if (itextcomponent == null) {
                itextcomponent = new TextComponentString("");
            }
        } else if (component instanceof TextComponentString) {
            itextcomponent = new TextComponentString(((TextComponentString)component).getText());
        } else if (component instanceof TextComponentKeybind) {
            itextcomponent = new TextComponentKeybind(((TextComponentKeybind)component).func_193633_h());
        } else {
            if (!(component instanceof TextComponentTranslation)) {
                return component;
            }
            Object[] aobject = ((TextComponentTranslation)component).getFormatArgs();
            for (int i = 0; i < aobject.length; ++i) {
                Object object = aobject[i];
                if (!(object instanceof ITextComponent)) continue;
                aobject[i] = TextComponentUtils.processComponent(commandSender, (ITextComponent)object, entityIn);
            }
            itextcomponent = new TextComponentTranslation(((TextComponentTranslation)component).getKey(), aobject);
        }
        Style style = component.getStyle();
        if (style != null) {
            itextcomponent.setStyle(style.createShallowCopy());
        }
        for (ITextComponent itextcomponent1 : component.getSiblings()) {
            itextcomponent.appendSibling(TextComponentUtils.processComponent(commandSender, itextcomponent1, entityIn));
        }
        return itextcomponent;
    }
}

