/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentScore;
import net.minecraft.util.ChatComponentSelector;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class ChatComponentProcessor {
    public static IChatComponent processComponent(ICommandSender commandSender, IChatComponent component, Entity entityIn) throws CommandException {
        IChatComponent ichatcomponent = null;
        if (component instanceof ChatComponentScore) {
            ChatComponentScore chatcomponentscore = (ChatComponentScore)component;
            String s2 = chatcomponentscore.getName();
            if (PlayerSelector.hasArguments(s2)) {
                List<Entity> list = PlayerSelector.matchEntities(commandSender, s2, Entity.class);
                if (list.size() != 1) {
                    throw new EntityNotFoundException();
                }
                s2 = list.get(0).getName();
            }
            ichatcomponent = entityIn != null && s2.equals("*") ? new ChatComponentScore(entityIn.getName(), chatcomponentscore.getObjective()) : new ChatComponentScore(s2, chatcomponentscore.getObjective());
            ((ChatComponentScore)ichatcomponent).setValue(chatcomponentscore.getUnformattedTextForChat());
        } else if (component instanceof ChatComponentSelector) {
            String s1 = ((ChatComponentSelector)component).getSelector();
            ichatcomponent = PlayerSelector.matchEntitiesToChatComponent(commandSender, s1);
            if (ichatcomponent == null) {
                ichatcomponent = new ChatComponentText("");
            }
        } else if (component instanceof ChatComponentText) {
            ichatcomponent = new ChatComponentText(((ChatComponentText)component).getChatComponentText_TextValue());
        } else {
            if (!(component instanceof ChatComponentTranslation)) {
                return component;
            }
            Object[] aobject = ((ChatComponentTranslation)component).getFormatArgs();
            for (int i2 = 0; i2 < aobject.length; ++i2) {
                Object object = aobject[i2];
                if (!(object instanceof IChatComponent)) continue;
                aobject[i2] = ChatComponentProcessor.processComponent(commandSender, (IChatComponent)object, entityIn);
            }
            ichatcomponent = new ChatComponentTranslation(((ChatComponentTranslation)component).getKey(), aobject);
        }
        ChatStyle chatstyle = component.getChatStyle();
        if (chatstyle != null) {
            ichatcomponent.setChatStyle(chatstyle.createShallowCopy());
        }
        for (IChatComponent ichatcomponent1 : component.getSiblings()) {
            ichatcomponent.appendSibling(ChatComponentProcessor.processComponent(commandSender, ichatcomponent1, entityIn));
        }
        return ichatcomponent;
    }
}

