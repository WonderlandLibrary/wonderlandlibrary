/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.ChatLine
 *  net.minecraft.client.gui.GuiNewChat
 */
package net.dev.important.injection.forge.mixins.accessors;

import java.util.List;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={GuiNewChat.class})
public interface GuiNewChatAccessor {
    @Accessor
    public List<ChatLine> getChatLines();

    @Accessor
    public List<ChatLine> getDrawnChatLines();
}

