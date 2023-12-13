package me.earth.phobos.features.modules.render;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfo;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class NoRender
        extends Module {
    private static NoRender INSTANCE = new NoRender();

    static {
        NoRender.INSTANCE = new NoRender();
    }

    public Setting<Boolean> fire = this.register(new Setting<Boolean>("Fire", Boolean.valueOf(false), "Removes the portal overlay."));
    public Setting<Boolean> portal = this.register(new Setting<Boolean>("Portal", Boolean.valueOf(false), "Removes the portal overlay."));
    public Setting<Boolean> pumpkin = this.register(new Setting<Boolean>("Pumpkin", Boolean.valueOf(false), "Removes the pumpkin overlay."));
    public Setting<Boolean> totemPops = this.register(new Setting<Boolean>("TotemPop", Boolean.valueOf(false), "Removes the Totem overlay."));
    public Setting<Boolean> items = this.register(new Setting<Boolean>("Items", Boolean.valueOf(false), "Removes items on the ground."));
    public Setting<Boolean> nausea = this.register(new Setting<Boolean>("Nausea", Boolean.valueOf(false), "Removes Portal Nausea."));
    public Setting<Boolean> hurtcam = this.register(new Setting<Boolean>("HurtCam", Boolean.valueOf(false), "Removes shaking after taking damage."));
    public Setting<Fog> fog = this.register(new Setting<Fog>("Fog", Fog.NONE, "Removes Fog."));
    public Setting<Boolean> noWeather = this.register(new Setting<Boolean>("Weather", Boolean.valueOf(false), "AntiWeather"));
    public Setting<Boss> boss = this.register(new Setting<Boss>("BossBars", Boss.NONE, "Modifies the bossbars."));
    public Setting<Float> scale = this.register(new Setting<Object>("Scale", Float.valueOf(0.0f), Float.valueOf(0.5f), Float.valueOf(1.0f), v -> this.boss.getValue() == Boss.MINIMIZE || this.boss.getValue() != Boss.STACK, "Scale of the bars."));
    public Setting<Boolean> bats = this.register(new Setting<Boolean>("Bats", Boolean.valueOf(false), "Removes bats."));
    public Setting<NoArmor> noArmor = this.register(new Setting<NoArmor>("NoArmor", NoArmor.NONE, "Doesnt Render Armor on players."));
    public Setting<Boolean> glint = this.register(new Setting<Object>("Glint", Boolean.valueOf(false), v -> this.noArmor.getValue() != NoArmor.NONE));
    public Setting<Skylight> skylight = this.register(new Setting<Skylight>("Skylight", Skylight.NONE));
    public Setting<Boolean> barriers = this.register(new Setting<Boolean>("Barriers", Boolean.valueOf(false), "Barriers"));
    public Setting<Boolean> blocks = this.register(new Setting<Boolean>("Blocks", Boolean.valueOf(false), "Blocks"));
    public Setting<Boolean> advancements = this.register(new Setting<Boolean>("Advancements", false));
    public Setting<Boolean> pigmen = this.register(new Setting<Boolean>("Pigmen", false));
    public Setting<Boolean> timeChange = this.register(new Setting<Boolean>("TimeChange", false));
    public Setting<Integer> time = this.register(new Setting<Object>("Time", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(23000), v -> this.timeChange.getValue()));

    public NoRender() {
        super("NoRender", "Allows you to stop rendering stuff", Module.Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static NoRender getInstance() {
        if (NoRender.INSTANCE == null) {
            NoRender.INSTANCE = new NoRender();
        }
        return NoRender.INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.items.getValue()) {
            NoRender.mc.world.loadedEntityList.stream().filter(EntityItem.class::isInstance).map(EntityItem.class::cast).forEach(Entity::setDead);
        }
        if (this.noWeather.getValue() && NoRender.mc.world.isRaining()) {
            NoRender.mc.world.setRainStrength(0.0f);
        }
        if (this.timeChange.getValue()) {
            NoRender.mc.world.setWorldTime((long) this.time.getValue());
        }
    }

    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate & this.timeChange.getValue()) {
            event.setCanceled(true);
        }
    }

    public void doVoidFogParticles(final int posX, final int posY, final int posZ) {
        final int i = 32;
        final Random random = new Random();
        final ItemStack itemstack = NoRender.mc.player.getHeldItemMainhand();
        final boolean flag = !this.barriers.getValue() || (NoRender.mc.playerController.getCurrentGameType() == GameType.CREATIVE && !itemstack.isEmpty() && itemstack.getItem() == Item.getItemFromBlock(Blocks.BARRIER));
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int j = 0; j < 667; ++j) {
            this.showBarrierParticles(posX, posY, posZ, 16, random, flag, blockpos$mutableblockpos);
            this.showBarrierParticles(posX, posY, posZ, 32, random, flag, blockpos$mutableblockpos);
        }
    }

    public void showBarrierParticles(final int x, final int y, final int z, final int offset, final Random random, final boolean holdingBarrier, final BlockPos.MutableBlockPos pos) {
        final int i = x + NoRender.mc.world.rand.nextInt(offset) - NoRender.mc.world.rand.nextInt(offset);
        final int j = y + NoRender.mc.world.rand.nextInt(offset) - NoRender.mc.world.rand.nextInt(offset);
        final int k = z + NoRender.mc.world.rand.nextInt(offset) - NoRender.mc.world.rand.nextInt(offset);
        pos.setPos(i, j, k);
        final IBlockState iblockstate = NoRender.mc.world.getBlockState(pos);
        iblockstate.getBlock().randomDisplayTick(iblockstate, NoRender.mc.world, pos, random);
        if (!holdingBarrier && iblockstate.getBlock() == Blocks.BARRIER) {
            NoRender.mc.world.spawnParticle(EnumParticleTypes.BARRIER, i + 0.5f, j + 0.5f, k + 0.5f, 0.0, 0.0, 0.0);
        }
    }

    @SubscribeEvent
    public void onRenderPre(final RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && this.boss.getValue() != Boss.NONE) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderPost(final RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.BOSSINFO && this.boss.getValue() != Boss.NONE) {
            if (this.boss.getValue() == Boss.MINIMIZE) {
                final Map<UUID, BossInfoClient> map = NoRender.mc.ingameGUI.getBossOverlay().mapBossInfos;
                if (map == null) {
                    return;
                }
                final ScaledResolution scaledresolution = new ScaledResolution(NoRender.mc);
                final int i = scaledresolution.getScaledWidth();
                int j = 12;
                for (final Map.Entry<UUID, BossInfoClient> entry : map.entrySet()) {
                    final BossInfoClient info = entry.getValue();
                    final String text = info.getName().getFormattedText();
                    final int k = (int) (i / this.scale.getValue() / 2.0f - 91.0f);
                    GL11.glScaled((double) this.scale.getValue(), (double) this.scale.getValue(), 1.0);
                    if (!event.isCanceled()) {
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                        NoRender.mc.getTextureManager().bindTexture(GuiBossOverlay.GUI_BARS_TEXTURES);
                        NoRender.mc.ingameGUI.getBossOverlay().render(k, j, info);
                        NoRender.mc.fontRenderer.drawStringWithShadow(text, i / this.scale.getValue() / 2.0f - NoRender.mc.fontRenderer.getStringWidth(text) / 2, (float) (j - 9), 16777215);
                    }
                    GL11.glScaled(1.0 / this.scale.getValue(), 1.0 / this.scale.getValue(), 1.0);
                    j += 10 + NoRender.mc.fontRenderer.FONT_HEIGHT;
                }
            } else if (this.boss.getValue() == Boss.STACK) {
                final Map<UUID, BossInfoClient> map = NoRender.mc.ingameGUI.getBossOverlay().mapBossInfos;
                final HashMap<String, Pair<BossInfoClient, Integer>> to = new HashMap<String, Pair<BossInfoClient, Integer>>();
                for (final Map.Entry<UUID, BossInfoClient> entry2 : map.entrySet()) {
                    final String s = entry2.getValue().getName().getFormattedText();
                    if (to.containsKey(s)) {
                        Pair<BossInfoClient, Integer> p = to.get(s);
                        p = new Pair<BossInfoClient, Integer>(p.getKey(), p.getValue() + 1);
                        to.put(s, p);
                    } else {
                        final Pair<BossInfoClient, Integer> p = new Pair<BossInfoClient, Integer>(entry2.getValue(), 1);
                        to.put(s, p);
                    }
                }
                final ScaledResolution scaledresolution2 = new ScaledResolution(NoRender.mc);
                final int l = scaledresolution2.getScaledWidth();
                int m = 12;
                for (final Map.Entry<String, Pair<BossInfoClient, Integer>> entry3 : to.entrySet()) {
                    String text = entry3.getKey();
                    final BossInfoClient info2 = entry3.getValue().getKey();
                    final int a = entry3.getValue().getValue();
                    text = text + " x" + a;
                    final int k2 = (int) (l / this.scale.getValue() / 2.0f - 91.0f);
                    GL11.glScaled((double) this.scale.getValue(), (double) this.scale.getValue(), 1.0);
                    if (!event.isCanceled()) {
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                        NoRender.mc.getTextureManager().bindTexture(GuiBossOverlay.GUI_BARS_TEXTURES);
                        NoRender.mc.ingameGUI.getBossOverlay().render(k2, m, info2);
                        NoRender.mc.fontRenderer.drawStringWithShadow(text, l / this.scale.getValue() / 2.0f - NoRender.mc.fontRenderer.getStringWidth(text) / 2, (float) (m - 9), 16777215);
                    }
                    GL11.glScaled(1.0 / this.scale.getValue(), 1.0 / this.scale.getValue(), 1.0);
                    m += 10 + NoRender.mc.fontRenderer.FONT_HEIGHT;
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderLiving(final RenderLivingEvent.Pre<?> event) {
        if (this.bats.getValue() && event.getEntity() instanceof EntityBat) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlaySound(final PlaySoundAtEntityEvent event) {
        if ((this.bats.getValue() && event.getSound().equals(SoundEvents.ENTITY_BAT_AMBIENT)) || event.getSound().equals(SoundEvents.ENTITY_BAT_DEATH) || event.getSound().equals(SoundEvents.ENTITY_BAT_HURT) || event.getSound().equals(SoundEvents.ENTITY_BAT_LOOP) || event.getSound().equals(SoundEvents.ENTITY_BAT_TAKEOFF)) {
            event.setVolume(0.0f);
            event.setPitch(0.0f);
            event.setCanceled(true);
        }
    }

    public enum Skylight {
        NONE,
        WORLD,
        ENTITY,
        ALL
    }

    public enum Fog {
        NONE,
        AIR,
        NOFOG
    }

    public enum Boss {
        NONE,
        REMOVE,
        STACK,
        MINIMIZE
    }

    public enum NoArmor {
        NONE,
        ALL,
        HELMET
    }

    public static class Pair<T, S> {
        private T key;
        private S value;

        public Pair(final T key, final S value) {
            this.key = key;
            this.value = value;
        }

        public T getKey() {
            return this.key;
        }

        public void setKey(final T key) {
            this.key = key;
        }

        public S getValue() {
            return this.value;
        }

        public void setValue(final S value) {
            this.value = value;
        }
    }
}
