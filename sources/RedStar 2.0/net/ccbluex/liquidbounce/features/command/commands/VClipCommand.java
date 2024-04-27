package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\n\b\u000020B¢J02\f\b00H¢\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/VClipCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "Pride"})
public final class VClipCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        if (args.length > 1) {
            try {
                IEntity iEntity;
                String string = args[1];
                boolean bl = false;
                double y = Double.parseDouble(string);
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    return;
                }
                IEntityPlayerSP thePlayer = iEntityPlayerSP;
                if (thePlayer.isRiding()) {
                    iEntity = thePlayer.getRidingEntity();
                    if (iEntity == null) {
                        Intrinsics.throwNpe();
                    }
                } else {
                    iEntity = thePlayer;
                }
                IEntity entity = iEntity;
                entity.setPosition(entity.getPosX(), entity.getPosY() + y, entity.getPosZ());
                this.chat("You were teleported.");
            }
            catch (NumberFormatException ex) {
                this.chatSyntaxError();
            }
            return;
        }
        this.chatSyntax("vclip <value>");
    }

    public VClipCommand() {
        super("vclip", new String[0]);
    }
}
