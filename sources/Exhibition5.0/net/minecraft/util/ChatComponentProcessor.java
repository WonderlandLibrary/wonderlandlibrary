// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import net.minecraft.command.CommandException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.Entity;
import net.minecraft.command.ICommandSender;

public class ChatComponentProcessor
{
    private static final String __OBFID = "CL_00002310";
    
    public static IChatComponent func_179985_a(final ICommandSender p_179985_0_, final IChatComponent p_179985_1_, final Entity p_179985_2_) throws CommandException {
        Object var3 = null;
        if (p_179985_1_ instanceof ChatComponentScore) {
            final ChatComponentScore var4 = (ChatComponentScore)p_179985_1_;
            String var5 = var4.func_179995_g();
            if (PlayerSelector.hasArguments(var5)) {
                final List var6 = PlayerSelector.func_179656_b(p_179985_0_, var5, Entity.class);
                if (var6.size() != 1) {
                    throw new EntityNotFoundException();
                }
                var5 = var6.get(0).getName();
            }
            var3 = ((p_179985_2_ != null && var5.equals("*")) ? new ChatComponentScore(p_179985_2_.getName(), var4.func_179994_h()) : new ChatComponentScore(var5, var4.func_179994_h()));
            ((ChatComponentScore)var3).func_179997_b(var4.getUnformattedTextForChat());
        }
        else if (p_179985_1_ instanceof ChatComponentSelector) {
            final String var7 = ((ChatComponentSelector)p_179985_1_).func_179992_g();
            var3 = PlayerSelector.func_150869_b(p_179985_0_, var7);
            if (var3 == null) {
                var3 = new ChatComponentText("");
            }
        }
        else if (p_179985_1_ instanceof ChatComponentText) {
            var3 = new ChatComponentText(((ChatComponentText)p_179985_1_).getChatComponentText_TextValue());
        }
        else {
            if (!(p_179985_1_ instanceof ChatComponentTranslation)) {
                return p_179985_1_;
            }
            final Object[] var8 = ((ChatComponentTranslation)p_179985_1_).getFormatArgs();
            for (int var9 = 0; var9 < var8.length; ++var9) {
                final Object var10 = var8[var9];
                if (var10 instanceof IChatComponent) {
                    var8[var9] = func_179985_a(p_179985_0_, (IChatComponent)var10, p_179985_2_);
                }
            }
            var3 = new ChatComponentTranslation(((ChatComponentTranslation)p_179985_1_).getKey(), var8);
        }
        final ChatStyle var11 = p_179985_1_.getChatStyle();
        if (var11 != null) {
            ((IChatComponent)var3).setChatStyle(var11.createShallowCopy());
        }
        for (final IChatComponent var13 : p_179985_1_.getSiblings()) {
            ((IChatComponent)var3).appendSibling(func_179985_a(p_179985_0_, var13, p_179985_2_));
        }
        return (IChatComponent)var3;
    }
}
