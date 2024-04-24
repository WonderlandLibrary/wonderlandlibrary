package me.crimp.claudius.mod.modules.render;

import com.mojang.authlib.GameProfile;
import me.crimp.claudius.event.events.TotemPopEvent;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import me.crimp.claudius.utils.PopChamsUtil;
import me.crimp.claudius.utils.Timer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PopChams extends Module {
    public static PopChams INSTANCE = new PopChams();

    public  final Setting<Integer> fillred = register(new Setting<>("FillRed", 167, 0, 255));
    public  final Setting<Integer> fillgreen = register(new Setting<>("FillGreen", 215, 0, 255));
    public  final Setting<Integer> fillblue = register(new Setting<>("FillBlue", 233, 0, 255));
    public  final Setting<Integer> fillalpha = register(new Setting<>("FillAlpha", 50, 0, 255));
    public  final Setting<Integer> lineRed = register(new Setting<>("LineRed", 250, 0, 255));
    public  final Setting<Integer> lineGreen = register(new Setting<>("LineGreen", 185, 0, 255));
    public  final Setting<Integer> lineBlue = register(new Setting<>("LineBlue", 212, 0, 255));
    public  final Setting<Integer> lineAlpha = register(new Setting<>("LineAlpha", 50, 0, 255));
    public  final Setting<Integer> fadestart = register(new Setting<>("FadeStart", 1238, 0, 3000));
    public final Setting<Float> fadeTime = register(new Setting<>("FadeTime", 0.4f, 0.0f, 2.0f));
    public  final Setting<Boolean> smallArms = register(new Setting<>("SmallArms", false));
    public  final Setting<Boolean> self = register(new Setting<>("Self", false));
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    long startTime;
    double alphaFill;
    double alphaLine;
    Timer followTimer = new Timer();

    public PopChams() {
        super("PopChams", "When You Or Others Pop It Draws A Ghost", Category.Render,false,false);
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPop(TotemPopEvent event) {
        followTimer.reset();
        if (self.getValue() || event.getPlayer().getEntityId() != PopChams.mc.player.getEntityId()) {
                GameProfile profile = new GameProfile(PopChams.mc.player.getUniqueID(), "");
                this.player = new EntityOtherPlayerMP(PopChams.mc.world, profile);
                this.player.copyLocationAndAnglesFrom(event.getPlayer());
                this.playerModel = new ModelPlayer(0.0f, smallArms.getValue());
                this.startTime = System.currentTimeMillis();
                this.playerModel.bipedHead.showModel = false;
                this.playerModel.bipedBody.showModel = false;
                this.playerModel.bipedLeftArmwear.showModel = false;
                this.playerModel.bipedLeftLegwear.showModel = false;
                this.playerModel.bipedRightArmwear.showModel = false;
                this.playerModel.bipedRightLegwear.showModel = false;
                this.alphaFill = fillalpha.getValue();
                this.alphaLine = lineAlpha.getValue();
                PopChamsUtil popChamsUtil = new PopChamsUtil(this.player, this.playerModel, this.startTime, this.alphaFill, this.alphaLine);
            }
        }
    }
