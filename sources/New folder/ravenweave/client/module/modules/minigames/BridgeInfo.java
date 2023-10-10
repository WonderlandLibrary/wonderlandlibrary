package ravenweave.client.module.modules.minigames;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.weavemc.loader.api.event.ChatReceivedEvent;
import net.weavemc.loader.api.event.RenderGameOverlayEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import ravenweave.client.event.impl.EntityJoinWorldEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.Setting;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.module.setting.impl.RGBSetting;
import ravenweave.client.module.setting.impl.TickSetting;
import ravenweave.client.utils.Utils;

public class BridgeInfo extends Module {
    public static DescriptionSetting a;
    public static TickSetting ep;
    public static RGBSetting rgb;
    private static int hudX = 5;
    private static int hudY = 70;
    private String en = "";
    private BlockPos g1p;
    private BlockPos g2p;
    private boolean q;
    private double d1;
    private double d2;
    private int blc;

    public BridgeInfo() {
        super("Bridge Info", ModuleCategory.minigames);
        this.registerSetting(a = new DescriptionSetting("Only for solos."));
        this.registerSetting(ep = new TickSetting("Edit position", false));
        this.registerSetting(rgb = new RGBSetting("Text Color", 0,200,200));
    }

    public void onDisable() {
        this.rv();
    }

    public void guiButtonToggled(TickSetting b) {
        if (b == ep) {
            ep.disable();
            mc.displayGuiScreen(new BridgeInfo.eh());
        }

    }

    @SubscribeEvent
    public void onTick(TickEvent ev) {
        if (!this.en.isEmpty() && this.inBridgeGame()) {
            EntityPlayer enemy = null;

            for (Entity e : mc.theWorld.loadedEntityList) {
                if (e instanceof EntityPlayer) {
                    if (e.getName().equals(this.en)) {
                        enemy = (EntityPlayer) e;
                    }
                } else if (e instanceof EntityArmorStand) {
                    String g2t = "Jump in to score!";
                    String g1t = "Defend!";
                    if (e.getName().contains(g1t)) {
                        this.g1p = e.getPosition();
                    } else if (e.getName().contains(g2t)) {
                        this.g2p = e.getPosition();
                    }
                }
            }

            if (this.g1p != null && this.g2p != null) {
                this.d1 = Utils.Java
                        .round(mc.thePlayer.getDistance(this.g2p.getX(), this.g2p.getY(), this.g2p.getZ()) - 1.4D, 1);
                if (this.d1 < 0.0D) {
                    this.d1 = 0.0D;
                }

                this.d2 = enemy == null ? 0.0D
                        : Utils.Java.round(enemy.getDistance(this.g1p.getX(), this.g1p.getY(), this.g1p.getZ()) - 1.4D,
                                1);
                if (this.d2 < 0.0D) {
                    this.d2 = 0.0D;
                }
            }

            int blc2 = 0;

            for (int i = 0; i < 9; ++i) {
                ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() instanceof ItemBlock
                        && ((ItemBlock) stack.getItem()).getBlock().equals(Blocks.stained_hardened_clay)) {
                    blc2 += stack.stackSize;
                }
            }

            this.blc = blc2;
        }
    }

    @Override
    public void guiButtonToggled(Setting setting) {
        if (setting == ep) {
            mc.displayGuiScreen(new BridgeInfo.eh());
            ep.disable();
        }
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Pre ignoredEv) {
        if (Utils.Player.isPlayerInGame() && this.inBridgeGame()) {
            if (mc.currentScreen != null || mc.gameSettings.showDebugInfo) {
                return;
            }

            String t1 = "Enemy: ";
            mc.fontRendererObj.drawString(t1 + this.en, (float) hudX, (float) hudY, rgb.getRGB(), true);
            String t2 = "Distance to goal: ";
            mc.fontRendererObj.drawString(t2 + this.d1, (float) hudX, (float) (hudY + 11), rgb.getRGB(), true);
            String t3 = "Enemy distance to goal: ";
            mc.fontRendererObj.drawString(t3 + this.d2, (float) hudX, (float) (hudY + 22), rgb.getRGB(), true);
            String t4 = "Blocks: ";
            mc.fontRendererObj.drawString(t4 + this.blc, (float) hudX, (float) (hudY + 33), rgb.getRGB(), true);
        }
    }

    @SubscribeEvent
    public void onChatReceived(ChatReceivedEvent c) {
        if (Utils.Player.isPlayerInGame()) {
            String s = Utils.Java.str(c.getMessage().getUnformattedText());
            if (s.startsWith(" ")) {
                String qt = "First player to score 5 goals wins";
                if (s.contains(qt)) {
                    this.q = true;
                } else if (this.q && s.contains("Opponent:")) {
                    String n = s.split(":")[1].trim();
                    if (n.contains("[")) {
                        n = n.split("] ")[1];
                    }

                    this.en = n;
                    this.q = false;
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent e) {
        if (!this.enabled) return;
        if (e.entity == mc.thePlayer) {
            this.rv();
        }
    }


    private boolean inBridgeGame() {
        if (Utils.Client.isHyp()) {
            for (String s : Utils.Client.getPlayersFromScoreboard()) {
                System.out.println(s);
                String s2 = s.toLowerCase();
                String bd = "bridge";
                if (s2.contains("mode")) System.out.println(s);
                if (s2.contains("mode") && s2.contains(bd)) {
                    return true;
                }
            }
        }

        return false;
    }

    private void rv() {
        this.en = "";
        this.q = false;
        this.g1p = null;
        this.g2p = null;
        this.d1 = 0.0D;
        this.d2 = 0.0D;
        this.blc = 0;
    }

    static class eh extends GuiScreen {
        final String displayString = "Enemy: Player123-Distance to goal: 17.2-Enemy distance to goal: 16.3-Blocks: 98";
        GuiButton resetPositionButton;
        boolean dragging;
        int miX;
        int miY;
        int maX;
        int maY;
        int aX = 5;
        int aY = 70;
        int laX;
        int laY;
        int lmX;
        int lmY;

        public void initGui() {
            super.initGui();
            this.buttonList.add(this.resetPositionButton = new GuiButton(1, this.width - 90, 5, 85, 20, "Reset position"));
            this.aX = hudX;
            this.aY = hudY;
        }

        public void drawScreen(int mX, int mY, float pt) {
            drawRect(0, 0, this.width, this.height, -1308622848);
            int miX = this.aX;
            int miY = this.aY;
            int maX = miX + 140;
            int maY = miY + 41;
            this.draw(this.mc.fontRendererObj, this.displayString);
            this.miX = miX;
            this.miY = miY;
            this.maX = maX;
            this.maY = maY;
            hudX = miX;
            hudY = miY;
            ScaledResolution res = new ScaledResolution(this.mc);
            int x = res.getScaledWidth() / 2 - 84;
            int y = res.getScaledHeight() / 2 - 20;
            Utils.HUD.drawColouredText("Edit the HUD position by dragging.", '-', x, y, 2L, 0L, true,
                    this.mc.fontRendererObj);

            this.handleInput();

            super.drawScreen(mX, mY, pt);
        }

        private void draw(FontRenderer fr, String t) {
            int x = this.miX;
            int y = this.miY;
            String[] var5 = t.split("-");

            for (String s : var5) {
                fr.drawString(s, (float) x, (float) y, rgb.getRGB(), true);
                y += fr.FONT_HEIGHT + 2;
            }

        }

        public void mouseClickMove(int mX, int mY, int b, long t) {
            super.mouseClickMove(mX, mY, b, t);
            if (b == 0) {
                if (this.dragging) {
                    this.aX = this.laX + (mX - this.lmX);
                    this.aY = this.laY + (mY - this.lmY);
                } else if (mX > this.miX && mX < this.maX && mY > this.miY && mY < this.maY) {
                    this.dragging = true;
                    this.lmX = mX;
                    this.lmY = mY;
                    this.laX = this.aX;
                    this.laY = this.aY;
                }

            }
        }

        public void mouseReleased(int mX, int mY, int s) {
            super.mouseReleased(mX, mY, s);
            if (s == 0) {
                this.dragging = false;
            }

        }

        public void actionPerformed(GuiButton b) {
            if (b == this.resetPositionButton) {
                this.aX = hudX = 5;
                this.aY = hudY = 70;
            }

        }

        public boolean doesGuiPauseGame() {
            return false;
        }
    }
}
