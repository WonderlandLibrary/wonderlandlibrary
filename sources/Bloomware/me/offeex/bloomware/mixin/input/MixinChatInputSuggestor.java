package me.offeex.bloomware.mixin.input;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import me.offeex.bloomware.api.manager.managers.CommandManager;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(ChatInputSuggestor.class)
public abstract class MixinChatInputSuggestor {
    @Shadow
    private ParseResults<ClientCommandSource> parse;
    @Shadow
    @Final
    TextFieldWidget textField;
    @Shadow
    boolean completingSuggestions;
    @Shadow
    private CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow
    private ChatInputSuggestor.SuggestionWindow window;

    @Shadow
    protected abstract void showCommandSuggestions();

    @Inject(method = "refresh", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/StringReader;canRead()Z", remap = false), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void refresh(CallbackInfo ci, String string, StringReader reader) {
        String prefix = CommandManager.INSTANCE.getPrefix();
        int pLength = prefix.length();
        int rCursor = reader.getCursor();

        if (!reader.canRead(pLength) || !reader.getString().startsWith(prefix, rCursor)) return;
        reader.setCursor(prefix.length() + rCursor);

        CommandDispatcher<ClientCommandSource> disp = CommandManager.INSTANCE.getDispatcher();
        if (this.parse == null) this.parse = disp.parse(reader, CommandManager.INSTANCE.getSource());

        int cursor = textField.getCursor();
        if (cursor >= 1 && (this.window == null || !this.completingSuggestions)) {
            this.pendingSuggestions = disp.getCompletionSuggestions(parse, cursor);
            this.pendingSuggestions.thenRun(() -> {
                if (this.pendingSuggestions.isDone()) this.showCommandSuggestions();
            });
        }

        ci.cancel();
    }
}
