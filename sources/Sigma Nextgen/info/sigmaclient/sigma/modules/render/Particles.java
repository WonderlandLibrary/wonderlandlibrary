package info.sigmaclient.sigma.modules.render;

import com.google.common.collect.Lists;
import info.sigmaclient.sigma.config.values.ModeValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.player.AttackEvent;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.event.render.RenderShaderEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.combat.Killaura;
import info.sigmaclient.sigma.modules.gui.ColorChanger;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.block.Blocks;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Random;

import static info.sigmaclient.sigma.modules.gui.TabGUI.smoothTrans;
import static info.sigmaclient.sigma.utils.render.RenderUtils.createFrameBuffer;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Particles extends Module {
    public static NumberValue scale = new NumberValue("Scale", 3, 1, 6, NumberValue.NUMBER_TYPE.INT);
    public static NumberValue speed = new NumberValue("Speed", 2, 1, 10, NumberValue.NUMBER_TYPE.LOW_FLOAT);
    public static NumberValue amount = new NumberValue("Amount", 2, 1, 6, NumberValue.NUMBER_TYPE.INT);
    public static ModeValue a = new ModeValue("Icon", "Heart", new String[]{"Heart", "Star", "Dollar", "Snow"});
    public Particles() {
        super("Particles", Category.Render, "Attack particles");
     registerValue(scale);
     registerValue(speed);
     registerValue(amount);
     registerValue(a);
    }
    ArrayList<String> all = new ArrayList<>(Lists.newArrayList("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "VVI", "VV", "VVI", "VVII", "VVIII", "VVVI", "X"));
    static class Partical {
        float alpha;
        String rect;
        double x, y, z;
        double lx, ly, lz;
        double motionX, motionY, motionZ;
        double speed;
        int color;
        public static float random(float min, float max) {
            return (float) (Math.random() * (max - min) + min);
        }
        public Partical(){
            float sp = Particles.speed.getValue().floatValue() * 4.5f;
            motionX = random(-(float) sp / 100f, (float) sp / 100f);
            motionY = random(-(float) sp / 100f / 3f, (float) sp / 100f / 3f);
            motionZ = random(-(float) sp / 100f, (float) sp / 100f);
        }
        public void update(){
            alpha *= 0.99f;
            alpha -= 0.01;
            lx = x;
            ly = y;
            lz = z;

            double sp = Math.sqrt(motionX * motionX + motionZ * motionZ) * 1;
            x += motionX;
            y += motionY;
            z += motionZ;

            if (posBlock(x, y - scale.getValue().floatValue() / 10f, z)) {
                motionY = -motionY * 0.7f;
                if(motionY < 0.043){
                    motionY = 0;
                }
            } else {
                if (posBlock(x, y, z) || posBlock(x, y, z) || posBlock(x, y, z) || posBlock(x - sp, y, z - sp)
                        || posBlock(x + sp, y, z + sp) || posBlock(x + sp, y, z - sp) || posBlock(x - sp, y, z + sp)
                        || posBlock(x + sp, y, z) || posBlock(x - sp, y, z) || posBlock(x, y, z + sp) || posBlock(x, y, z - sp)
                        || posBlock(x - sp, y, z - sp) || posBlock(x + sp, y, z + sp) || posBlock(x + sp, y, z - sp)
                        || posBlock(x - sp, y, z + sp) || posBlock(x + sp, y, z) || posBlock(x - sp, y, z) || posBlock(x, y, z + sp)
                        || posBlock(x, y, z - sp) || posBlock(x - sp, y, z - sp) || posBlock(x + sp, y, z + sp) || posBlock(x + sp, y, z - sp)
                        || posBlock(x - sp, y, z + sp) || posBlock(x + sp, y, z) || posBlock(x - sp, y, z) || posBlock(x, y, z + sp)
                        || posBlock(x, y, z - sp)) {
                    motionX = -motionX;
                    motionZ = -motionZ;
                }
            }

            // fall
            motionY -= 0.01f;

            motionX /= 1.02;
            motionZ /= 1.02;
            motionY /= 1.02;
        }
        private boolean posBlock(double x, double y, double z) {
            return (mc.world.getBlockState(new BlockPos((int) x, (int) y, (int) z)).getBlock() != Blocks.AIR && mc.world.getBlockState(new BlockPos((int) x, (int) y, (int) z)).getBlock() != Blocks.WATER && mc.world.getBlockState(new BlockPos((int) x, (int) y, (int) z)).getBlock() != Blocks.LAVA);
        }
    }
    Framebuffer framebuffer;
    ArrayList<Partical> particals = new ArrayList<>();
    @Override
    public void onWindowUpdateEvent(WindowUpdateEvent event) {
        for(Object obf : particals.toArray()){
            Partical partical = (Partical) obf;
            partical.update();

            if(partical.alpha <= 0){
                particals.remove(partical);
            }
        }
        for(Entity playerEntity : mc.world.getLoadedEntityList()){
            if(playerEntity.equals(mc.player) || !(playerEntity instanceof LivingEntity)) continue;
            if(((LivingEntity) playerEntity).hurtTime == 9 && playerEntity == Killaura.attackTarget){
                for(int i = 0; i < amount.getValue().intValue() * 5; i ++){
                    Partical partical = new Partical();
                    partical.x = playerEntity.getPosX() + new Random().nextDouble() - 0.5;
                    partical.y = playerEntity.getPosY() + 1 + (new Random().nextDouble() - 0.5) * 2;
                    partical.z = playerEntity.getPosZ() + new Random().nextDouble() - 0.5;
                    partical.lx = partical.x;
                    partical.ly = partical.y;
                    partical.lz = partical.z;
                    partical.speed = Math.min(Math.max(new Random().nextDouble(), 0.95), 1);
                    partical.alpha = 1;
                    partical.color = ColorChanger.getColor(i * 12 + playerEntity.ticksExisted % 360, 10).getRGB();
                    partical.rect = all.get(new Random().nextInt(all.size()));
                    particals.add(partical);
                }
            }
        }
    }

    @Override
    public void onAttackEvent(AttackEvent event) {
        if(event.post){

        }
        super.onAttackEvent(event);
    }

    @Override
    public void onRenderShaderEvent(RenderShaderEvent event) {
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        GlStateManager.pushMatrix();

        GlStateManager.disableLighting();
//        GlStateManager.depthMask(false);
//        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        for(Partical partical : particals){
            float f = RenderUtils.getRenderPos().playerViewY;
            float f1 = RenderUtils.getRenderPos().playerViewX;

            double camX = RenderUtils.getRenderPos().renderPosX;
            double camY = RenderUtils.getRenderPos().renderPosY;
            double camZ = RenderUtils.getRenderPos().renderPosZ;
            double n = partical.x;
            n = smoothTrans(n, partical.lx);
            final double ex = n - camX;
            double n2 = partical.y;
            n2 = smoothTrans(n2, partical.ly);
            final double ey = n2 - camY;
            double n3 = partical.z;
            n3 = smoothTrans(n3, partical.lz);
            final double ez = n3 - camZ;

            GlStateManager.pushMatrix();

            GlStateManager.translate(ex, ey, ez);
            GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
            double scale = 0.07f;
            GlStateManager.scale(-scale, -scale, 1f);

            GlStateManager.resetColor();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            switch (a.getValue()){
                case "Heart"-> RenderUtils.drawTextureLocationZoom(0,0, Particles.scale.getValue().floatValue(), Particles.scale.getValue().floatValue(),"heart", ColorUtils.reAlpha(partical.color, partical.alpha * 0.9f));
                case "Snow"-> RenderUtils.drawTextureLocationZoom(0,0, Particles.scale.getValue().floatValue(), Particles.scale.getValue().floatValue(),"snowflake", ColorUtils.reAlpha(partical.color, partical.alpha * 0.9f));
                case "Star"-> RenderUtils.drawTextureLocationZoom(0,0, Particles.scale.getValue().floatValue(), Particles.scale.getValue().floatValue(),"star", ColorUtils.reAlpha(partical.color, partical.alpha * 0.9f));
                case "Dollar"-> RenderUtils.drawTextureLocationZoom(0,0, Particles.scale.getValue().floatValue(), Particles.scale.getValue().floatValue(),"dollar", ColorUtils.reAlpha(partical.color, partical.alpha * 0.9f));
            }
            GlStateManager.popMatrix();
        }

//        GlStateManager.depthMask(true);
//        GlStateManager.enableDepth();
//        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
        GlStateManager.popMatrix();

        super.onRender3DEvent(event);
    }
}
