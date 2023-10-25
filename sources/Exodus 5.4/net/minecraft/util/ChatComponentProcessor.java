/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

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
    public static IChatComponent processComponent(ICommandSender iCommandSender, IChatComponent iChatComponent, Entity entity) throws CommandException {
        Object object;
        Object object2;
        IChatComponent iChatComponent2 = null;
        if (iChatComponent instanceof ChatComponentScore) {
            object2 = (ChatComponentScore)iChatComponent;
            String string = ((ChatComponentScore)object2).getName();
            if (PlayerSelector.hasArguments(string)) {
                object = PlayerSelector.matchEntities(iCommandSender, string, Entity.class);
                if (object.size() != 1) {
                    throw new EntityNotFoundException();
                }
                string = object.get(0).getName();
            }
            iChatComponent2 = entity != null && string.equals("*") ? new ChatComponentScore(entity.getName(), ((ChatComponentScore)object2).getObjective()) : new ChatComponentScore(string, ((ChatComponentScore)object2).getObjective());
            ((ChatComponentScore)iChatComponent2).setValue(((ChatComponentScore)object2).getUnformattedTextForChat());
        } else if (iChatComponent instanceof ChatComponentSelector) {
            object2 = ((ChatComponentSelector)iChatComponent).getSelector();
            iChatComponent2 = PlayerSelector.matchEntitiesToChatComponent(iCommandSender, (String)object2);
            if (iChatComponent2 == null) {
                iChatComponent2 = new ChatComponentText("");
            }
        } else if (iChatComponent instanceof ChatComponentText) {
            iChatComponent2 = new ChatComponentText(((ChatComponentText)iChatComponent).getChatComponentText_TextValue());
        } else {
            if (!(iChatComponent instanceof ChatComponentTranslation)) {
                return iChatComponent;
            }
            object2 = ((ChatComponentTranslation)iChatComponent).getFormatArgs();
            int n = 0;
            while (n < ((Object[])object2).length) {
                object = object2[n];
                if (object instanceof IChatComponent) {
                    object2[n] = ChatComponentProcessor.processComponent(iCommandSender, (IChatComponent)object, entity);
                }
                ++n;
            }
            iChatComponent2 = new ChatComponentTranslation(((ChatComponentTranslation)iChatComponent).getKey(), (Object[])object2);
        }
        object2 = iChatComponent.getChatStyle();
        if (object2 != null) {
            iChatComponent2.setChatStyle(((ChatStyle)object2).createShallowCopy());
        }
        for (IChatComponent iChatComponent3 : iChatComponent.getSiblings()) {
            iChatComponent2.appendSibling(ChatComponentProcessor.processComponent(iCommandSender, iChatComponent3, entity));
        }
        return iChatComponent2;
    }
}

