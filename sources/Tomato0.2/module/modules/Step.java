/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.commands.CommandManager;
import cow.milkgod.cheese.events.EventChatSend;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.events.StepConfirmEvent;
import cow.milkgod.cheese.events.StepEvent;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Logger;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovementInput;

public class Step
extends Module {
    private boolean reverse = false;
    private int groundTicks;
    private int recentStepTicks;
    private double height = 1.0;
    private int stepStage;
    public static boolean stepping;

    public Step() {
        super("Step", 0, Category.MOVEMENT, 689348, true, "Step up full blocks", new String[]{"stp"});
    }

    @EventTarget
    private void onStep(StepEvent event) {
        if (this.height > 1.0) {
            Wrapper.getMinecraft().thePlayer.stepHeight = (float)this.height;
            event.stepHeight = this.height;
        } else {
            Wrapper.getMinecraft().thePlayer.stepHeight = 0.6f;
            if (Wrapper.getMinecraft().thePlayer.movementInput != null && !Wrapper.getMinecraft().thePlayer.movementInput.jump && Wrapper.getMinecraft().thePlayer.isCollidedVertically) {
                event.stepHeight = 1.0;
                event.bypass = true;
            }
        }
    }

    @EventTarget
    private void onStepConfirmed(StepConfirmEvent event) {
        this.stepStage = 0;
        Wrapper.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getMinecraft().thePlayer.posX, Wrapper.getMinecraft().thePlayer.posY + 0.42, Wrapper.getMinecraft().thePlayer.posZ, Wrapper.getMinecraft().thePlayer.onGround));
        Wrapper.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Wrapper.getMinecraft().thePlayer.posX, Wrapper.getMinecraft().thePlayer.posY + 0.75, Wrapper.getMinecraft().thePlayer.posZ, Wrapper.getMinecraft().thePlayer.onGround));
    }

    @Override
    public void onDisable() {
        this.stepStage = 0;
        Wrapper.getMinecraft().thePlayer.stepHeight = 0.6f;
        super.onDisable();
    }

    @Override
    protected void addCommand() {
        Cheese.getInstance();
        CommandManager commandManager = Cheese.commandManager;
        CommandManager.addCommand(new Command("Step", "Unknown option! ", new String[]{"st"}, "<reverse || height ||| values>", "Changes the different step mode"){

            @EventTarget
            public void onEvent(EventTick tick) {
                block11 : {
                    try {
                        String mode = EventChatSend.getMessage().split(" ")[1];
                        if (mode.equalsIgnoreCase("reverse") || mode.equalsIgnoreCase("reverseStep")) {
                            Step.access$1(Step.this, !Step.this.reverse);
                            if (Step.this.reverse) {
                                Logger.logChat("\u00a7eReverse Step \u00a77has been \u00a7aenabled");
                            } else {
                                Logger.logChat("\u00a7eReverse Step \u00a77has been \u00a7cdisabled");
                            }
                            break block11;
                        }
                        if (mode.equalsIgnoreCase("height") || mode.equalsIgnoreCase("h")) {
                            String message2 = EventChatSend.getMessage().split(" ")[2];
                            try {
                                if (message2.equalsIgnoreCase("actual")) {
                                    Logger.logChat("Actual height: \u00a7e" + String.valueOf(Step.this.height));
                                    break block11;
                                }
                                Step.access$3(Step.this, Double.parseDouble(message2));
                                Logger.logChat("Step height set to: \u00a7e" + Step.this.height);
                            }
                            catch (Exception e) {
                                Logger.logChat("Invalid Value! height <actual | Height>");
                            }
                            break block11;
                        }
                        if (mode.equalsIgnoreCase("values")) {
                            Logger.logChat("Reverse: \u00a7e" + String.valueOf(Step.this.reverse));
                            Logger.logChat("Height: \u00a7e" + String.valueOf(Step.this.height));
                        } else {
                            Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                        }
                    }
                    catch (Exception e2) {
                        Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
                    }
                }
                this.Toggle();
            }
        });
    }

    static /* synthetic */ void access$1(Step step, boolean bl2) {
        step.reverse = bl2;
    }

    static /* synthetic */ void access$3(Step step, double d2) {
        step.height = d2;
    }

}

