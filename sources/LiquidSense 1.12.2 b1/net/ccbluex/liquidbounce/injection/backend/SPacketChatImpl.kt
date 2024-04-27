package net.ccbluex.liquidbounce.injection.backend

import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketChat
import net.minecraft.network.play.server.SPacketChat
import net.minecraft.util.text.ChatType
import net.minecraft.util.text.ITextComponent

class SPacketChatImpl<T : SPacketChat>(wrapped: T) : PacketImpl<T>(wrapped), ISPacketChat {
    override val chatComponent: ITextComponent
        get() = wrapped.chatComponent
    override val type: ChatType
        get() = wrapped.type
    override val getChat:ITextComponent
        get() = wrapped.getChatComponent()


}

inline fun ISPacketChat.unwrap(): SPacketChat = (this as SPacketChatImpl<*>).wrapped
inline fun SPacketChat.wrap(): ISPacketChat = SPacketChatImpl(this)