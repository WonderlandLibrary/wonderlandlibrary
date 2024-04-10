package info.sigmaclient.sigma.modules.player;

import com.mojang.blaze3d.matrix.MatrixStack;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class OldHitting extends Module {
    public static void rotate(float angel, float x,float y, float z,MatrixStack matrixStack){
        matrixStack.rotate(new Vector3f(x, y, z).rotationDegrees(angel));
    }
    public static void scale(float x,float y, float z,MatrixStack matrixStack){
        matrixStack.scale(x,y,z);
    }
    public static void translate(double x,double y, double z,MatrixStack matrixStack){
        matrixStack.translate(x,y,z);
    }
    public static void doBlockingAnimMainPre(MatrixStack matrixStack, ItemStack itemStack) {
        if(itemStack.isEmpty()) return;
//        if(mc.player.getHeldItemMainhand().isEmpty()) return;
        if(isBlocking() || always.isEnable()) {
            translate(offsetX.getValue().floatValue(), offsetY.getValue().floatValue(), offsetZ.getValue().floatValue(), matrixStack);
            scale(scaled.getValue().floatValue(), scaled.getValue().floatValue(), scaled.getValue().floatValue(), matrixStack);
        }
    }
    public static void doBlockingAnimOffPre(MatrixStack matrixStack, ItemStack itemStack) {
        if(itemStack.isEmpty()) return;
//        if(mc.player.getHeldItemOffhand().isEmpty()) return;
        translate(ooffsetX.getValue().floatValue(), ooffsetY.getValue().floatValue(), ooffsetZ.getValue().floatValue(), matrixStack);
        scale(scaled.getValue().floatValue(), scaled.getValue().floatValue(), scaled.getValue().floatValue(), matrixStack);
    }
    public static void doBlockingAnimMainPost(MatrixStack matrixStack, ItemStack itemStack) {
        if(itemStack.isEmpty()) return;
//        if(mc.player.getHeldItemMainhand().isEmpty()) return;
        if(isBlocking() || always.isEnable()) {
            rotate(rotateX.getValue().floatValue(), 1, 0, 0, matrixStack);
            rotate(rotateY.getValue().floatValue(), 0, 1, 0, matrixStack);
            rotate(rotateZ.getValue().floatValue(), 0, 0, 1, matrixStack);
        }
    }
    public static void doBlockingAnim(float swingProgress, MatrixStack matrixStack, float equippedProgress, ItemStack itemStack) {
        if(itemStack.isEmpty()) return;
        float f = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
        float f2 = MathHelper.sin(swingProgress * (float) Math.PI);
        switch (OldHitting.mode.getValue()) {
            case "Vanilla":
            case "Slide2":
                translate(0.47999998927116394, -0.550000011920929, -0.7199999690055847, matrixStack);
                translate(0.0, equippedProgress * -0.6f, 0.0, matrixStack);
                rotate(77.0f, 0.0f, 1.0f, 0.0f, matrixStack);
                rotate(-10.0f, 0.0f, 0.0f, 1.0f, matrixStack);
                rotate(f * -20.0f, 0.0f, 1.0f, 0.0f, matrixStack);
                rotate(f1 * -20.0f, 0.0f, 0.0f, 1.0f, matrixStack);
                rotate(f1 * -69.0f, 1.0f, 0.0f, 0.0f, matrixStack);
                rotate(-80.0f, 1.0f, 0.0f, 0.0f, matrixStack);
                scale(1.2f, 1.2f, 1.2f, matrixStack);
                break;
            case "Slide":
                translate(0.6480000019073486, -0.550000011920929, -0.7199999690055847, matrixStack);
                // damage anim
                translate(0.0, equippedProgress * -0.6f, 0.0, matrixStack);
                rotate(77.0f, 0.0f, 1.0f, 0.0f, matrixStack);
                rotate(-10.0f, 0.0f, 0.0f, 1.0f, matrixStack);
                rotate(-80.0f, 1.0f, 0.0f, 0.0f, matrixStack);
                rotate(-f1 * 20, 1.0f, 0.0f, 0.0f, matrixStack);
                scale(1.2f, 1.2f, 1.2f, matrixStack);
                break;
            //                translate(0.0, 0 * -0.6f, 0.0);
            case "scaled":
                translate(0.47999998927116394, -0.550000011920929, -0.7199999690055847, matrixStack);
                translate(0.0, equippedProgress * -0.6f, 0.0, matrixStack);
//                translate(0.0, n * -0.2f, 0.0);
                rotate(77.0f, 0.0f, 1.0f, 0.0f, matrixStack);
                rotate(-10.0f, 0.0f, 0.0f, 1.0f, matrixStack);
                rotate(-80.0f, 1.0f, 0.0f, 0.0f, matrixStack);
                final float sc = 1.2f - f1 * 0.3f;
                scale(sc, sc, sc, matrixStack);
                break;
            case "Tap":
                translate(0.0, -3.5, 0.0, matrixStack);
                translate(0.5600000023841858, -0.5199999809265137, -0.7200000286102295, matrixStack);
                translate(0.5600000023841858, -0.2199999988079071, -0.7199999690055847, matrixStack);
                translate(0.0, equippedProgress * -0.6f, 0.0, matrixStack);
                rotate(45.0f, 0.0f, 1.0f, 0.0f, matrixStack);
                rotate(0.0f, 0.0f, 0.0f, 1.0f, matrixStack);
                rotate(f1 * -9.0f, 1.0f, 0.0f, 0.0f, matrixStack);
                rotate(-9.0f, 0.0f, 0.0f, 1.0f, matrixStack);
                translate(0.0, 3.200000047683716, 0.0, matrixStack);
                rotate(-80.0f, 1.0f, 0.0f, 0.0f, matrixStack);
                scale(2.7f, 2.7f, 2.7f, matrixStack);
                break;
            case "Tap2":
                translate(0.6480000019073486, -0.550000011920929, -0.7199999690055847, matrixStack);
                translate(0.0, equippedProgress * -0.6f, 0.0, matrixStack);
                rotate(77.0f, 0.0f, 1.0f, 0.0f, matrixStack);
                rotate(-10.0f, 0.0f, 0.0f, 1.0f, matrixStack);
                rotate(-80.0f, 1.0f, 0.0f, 0.0f, matrixStack);
                rotate(-f1 * 10.0f, 1.0f, -2.0f, 3.0f, matrixStack);
                scale(1.2f, 1.2f, 1.2f, matrixStack);
                break;
            case "Leaked":
                translate(x, -y, -0.71999997F, matrixStack);
                rotate(50.0F, 0.0F, 1.0F, 0.0F, matrixStack);
                //放大
                size = .6f + 0.05f * f1;
               scale(size, size, size, matrixStack);
                translate(-0.1, 0.22, 0, matrixStack);
                translate(-0.5F, 0.2F, 0.0F, matrixStack);
                rotate(45f, 0.0F, 1.0F, 0.0F, matrixStack);
                rotate(-100.0F, 1.0F, 0.0F, 0.0F, matrixStack);
                rotate(60.0F, 0.0F, 1.0F, 0.0F, matrixStack);
                rotate(20f, 1.0F, 0f, 0f, matrixStack);
                rotate(-30f, 0.0F, 1F, 0f, matrixStack);

                rotate(9f, 1, 0, 0f, matrixStack);
                rotate(-3f, 0, 1f, 0f, matrixStack);
                // 摆动幅度
                float percent = 0.8f;
                // y :
                rotate(f1 * -30f * percent, 1, 0, 0f, matrixStack);
                // z :
                rotate(f1 * 30f * percent, 0, 0, 1f, matrixStack);
                // x :
                rotate(f1 * -35f * percent, 0, 1f, 0f, matrixStack);
                scale(1.2f, 1.2f, 1.2f, matrixStack);
                break;

            case "Ninja":
                translate(0.47999998927116394, -0.38999998569488525, -0.7199999690055847, matrixStack);
                translate(0.0, equippedProgress * -0.6f, 0.0, matrixStack);

                rotate(100.0f, 0.0f, 1.0f, 0.0f, matrixStack);
                rotate(-50.0f, 0.0f, 0.0f, 1.0f, matrixStack);
                final float 殢嶗퉧竬鼒ศ = f2;
                final float 殢嶗퉧竬鼒ศ2 = f2;
                rotate(殢嶗퉧竬鼒ศ * -10.0f, 0.0f, 1.0f, 0.0f, matrixStack);
                rotate(殢嶗퉧竬鼒ศ2 * -30.0f, 0.0f, 0.0f, 1.0f, matrixStack);
                rotate(殢嶗퉧竬鼒ศ2 * 109.0f, 1.0f, 0.0f, 0.0f, matrixStack);
                rotate(-90.0f, 1.0f, 0.0f, 0.0f, matrixStack);
                scale(1.2f, 1.2f, 1.2f, matrixStack);
                break;
            case "Tomy":
                translate(0.47999998927116394, -0.550000011920929, -0.7199999690055847, matrixStack);
                translate(0.0, equippedProgress * -0.6f, 0.0, matrixStack);
//                translate(0.0, n * -0.6f, 0.0, matrixStack);
                rotate(77.0f, 0.0f, 1.0f, 0.0f, matrixStack);
                rotate(-10.0f, 0.0f, 0.0f, 1.0f, matrixStack);
                rotate(f * -20.0f, 0.0f, 1.0f, 0.0f, matrixStack);
                rotate(f1 * -20.0f, 0.0f, 0.0f, 1.0f, matrixStack);
                rotate(f1 * -69.0f, 1.0f, 0.0f, 0.0f, matrixStack);
                rotate(-80.0f, 1.0f, 0.0f, 0.0f, matrixStack);
                final float n3 = 1.2f;
                scale(n3, n3, n3, matrixStack);
                break;
            case "Slow":
                if(f1 > 0 && attackAnim.getValue() == 0){
                    attackAnim.setValue(10);
                }
                float i = attackAnim.getValue() / 10.0f;
                if(i > 0.5f){
                    i = 1.0f - i;
                }
                translate(0.6480000019073486, -0.550000011920929, -0.7199999690055847, matrixStack);
                translate(0.0, equippedProgress * -0.6f, 0.0, matrixStack);
                // damage anim
                translate(-0.1, 0.2, 0.0, matrixStack);
                rotate(77.0f, 0.0f, 1.0f, 0.0f, matrixStack);
                rotate(-10.0f, 0.0f, 0.0f, 1.0f, matrixStack);
                rotate(-80.0f, 1.0f, 0.0f, 0.0f, matrixStack);
                rotate(-i * 100.0f, 1.0f, 0.0f, 0.0f, matrixStack);
                scale(0.7f, 0.7f, 0.7f, matrixStack);
                break;
            case "Custom":
                float swing = 0f;
                if(cmode.is("f1")){
                    swing = f;
                }
                if(cmode.is("f2")){
                    swing = f1;
                }
                if(cmode.is("f3")){
                    swing = f2;
                }
                if(cmode.is("Smooth")){
                    swing = swingProgress >= 0.5f ? (1 - swingProgress) : swingProgress;
                }
                translate(0.6480000019073486, -0.38999998569488525, -0.7199999690055847, matrixStack);
                rotate(crotateX.getValue().floatValue() * swing, 0.0f, 0.0f, 1.0f, matrixStack);
                rotate(crotateY.getValue().floatValue() * swing, 0.0f, 1.0f, 0.0f, matrixStack);
                rotate(crotateZ.getValue().floatValue() * swing, 1.0f, 0.0f, 0.0f, matrixStack);
                break;
        }
    }
    public static NumberValue offsetX = new NumberValue("OffsetX", 0, -1, 1, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue offsetY = new NumberValue("OffsetY", 0, -1, 1, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue offsetZ = new NumberValue("OffsetZ", 0, -1, 1, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue ooffsetX = new NumberValue("Off-OffsetX", 0, -1, 1, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue ooffsetY = new NumberValue("Off-OffsetY", 0, -1, 1, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue ooffsetZ = new NumberValue("Off-OffsetZ", 0, -1, 1, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue rotateX = new NumberValue("RotateX", 0, -360, 360, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue rotateY = new NumberValue("RotateY", 0, -360, 360, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue rotateZ = new NumberValue("RotateZ", 0, -360, 360, NumberValue.NUMBER_TYPE.FLOAT);
    public static NumberValue scaled = new NumberValue("Scaled", 1, 0, 2, NumberValue.NUMBER_TYPE.FLOAT);
    public static BooleanValue modify = new BooleanValue("Modify Swing Ticks", false);
    public static BooleanValue swing = new BooleanValue("Other Swing", false);
    public static NumberValue speed = new NumberValue("Swing Ticks", 0, 0, 40, NumberValue.NUMBER_TYPE.INT);
    public static BooleanValue always = new BooleanValue("Always Block", false);
    public static BooleanValue block = new BooleanValue("Block Animation", false);
    public static BooleanValue noSheild = new BooleanValue("No Sheild", true);
    public static NumberValue rotateS = new NumberValue("Rotate Slide", 60, -360, 360, NumberValue.NUMBER_TYPE.FLOAT);
    public static ModeValue mode = new ModeValue("Mode", "Vanilla", new String[]{"Vanilla", "Tap", "Tap2", "Custom", "Slide", "Slide2", "Leaked", "scaled", "Ninja", "Tomy", "Slow"});
    public static NumberValue crotateX = new NumberValue("CustomX", 0, -360, 360, NumberValue.NUMBER_TYPE.FLOAT){
        @Override
        public boolean isHidden() {
            return !mode.is("Custom");
        }
    };
    public static NumberValue crotateY = new NumberValue("CustomY", 0, -360, 360, NumberValue.NUMBER_TYPE.FLOAT){
        @Override
        public boolean isHidden() {
            return !mode.is("Custom");
        }
    };
    public static NumberValue crotateZ = new NumberValue("CustomZ", 0, -360, 360, NumberValue.NUMBER_TYPE.FLOAT){
        @Override
        public boolean isHidden() {
            return !mode.is("Custom");
        }
    };
    public static ModeValue cmode = new ModeValue("Custom2", "Smooth", new String[]{"f1", "f2", "f3", "Smooth"}){
        @Override
        public boolean isHidden() {
            return !mode.is("Custom");
        }
    };
    public static boolean blocking = false;
    public static PartialTicksAnim attackAnim = new PartialTicksAnim(0);
    public static boolean renderOldHitting() {
        return SigmaNG.SigmaNG.moduleManager.getModule(OldHitting.class).enabled && noSheild.getValue();
    }
    public static boolean renderFPOldHitting() {
        return SigmaNG.SigmaNG.moduleManager.getModule(OldHitting.class).enabled;
    }

    public static boolean isBlocking() {
        return (always.isEnable() || (blocking && mc.player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof SwordItem)) && SigmaNG.SigmaNG.moduleManager.getModule(OldHitting.class).enabled;
    }

    public OldHitting() {
        super("OldHitting", Category.Player, "1.12.2 block?");
     registerValue(offsetX);
     registerValue(offsetY);
     registerValue(offsetZ);
     registerValue(ooffsetX);
     registerValue(ooffsetY);
     registerValue(ooffsetZ);
     registerValue(rotateX);
     registerValue(rotateY);
     registerValue(rotateZ);
//     registerValue(scaled);
     registerValue(modify);
//     registerValue(swing);
     registerValue(speed);
     registerValue(always);
     registerValue(block);
     registerValue(noSheild);
//     registerValue(rotateS);
     registerValue(mode);
     registerValue(crotateX);
     registerValue(crotateY);
     registerValue(crotateZ);
     registerValue(cmode);
    }

    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.pre){
            float last = attackAnim.getValue();
            attackAnim.interpolate(0, mode.is("Tomy") ? -0.8 : -0.6);
            if(mode.is("Tomy")) {
                if (mc.player.swingProgress > 0 && attackAnim.getValue() <= 0.2) {
                    attackAnim.setValue(10);
                }
            }
        }
        suffix = mode.getValue();
        super.onUpdateEvent(event);
    }
}
