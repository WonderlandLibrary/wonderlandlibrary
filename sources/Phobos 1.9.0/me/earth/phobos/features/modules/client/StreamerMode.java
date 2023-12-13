package me.earth.phobos.features.modules.client;

import me.earth.phobos.Phobos;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.PotionEffect;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class StreamerMode
        extends Module {
    public Setting<Integer> width = this.register(new Setting<Integer>("Width", 600, 100, 3160));
    public Setting<Integer> height = this.register(new Setting<Integer>("Height", 900, 100, 2140));
    private SecondScreenFrame window = null;

    public StreamerMode() {
        super("StreamerMode", "Displays client info in a second window.", Module.Category.CLIENT, false, false, false);
    }

    @Override
    public void onEnable() {
        EventQueue.invokeLater(() -> {
            if (this.window == null) {
                this.window = new SecondScreenFrame();
            }
            this.window.setVisible(true);
        });
    }

    @Override
    public void onDisable() {
        if (this.window != null) {
            this.window.setVisible(false);
        }
        this.window = null;
    }

    @Override
    public void onLogout() {
        if (this.window != null) {
            ArrayList<String> drawInfo = new ArrayList<String>();
            drawInfo.add("Phobos v1.9.0");
            drawInfo.add("");
            drawInfo.add("No Connection.");
            this.window.setToDraw(drawInfo);
        }
    }

    @Override
    public void onUnload() {
        this.disable();
    }

    @Override
    public void onLoad() {
        this.disable();
    }

    @Override
    public void onUpdate() {
        if (this.window != null) {
            ArrayList<String> drawInfo = new ArrayList<String>();
            drawInfo.add("Phobos v1.9.0");
            drawInfo.add("");
            drawInfo.add("Fps: " + Minecraft.debugFPS);
            drawInfo.add("TPS: " + Phobos.serverManager.getTPS());
            drawInfo.add("Ping: " + Phobos.serverManager.getPing() + "ms");
            drawInfo.add("Speed: " + Phobos.speedManager.getSpeedKpH() + "km/h");
            drawInfo.add("Time: " + new SimpleDateFormat("h:mm a").format(new Date()));
            boolean inHell = StreamerMode.mc.world.getBiome(StreamerMode.mc.player.getPosition()).getBiomeName().equals("Hell");
            int posX = (int) StreamerMode.mc.player.posX;
            int posY = (int) StreamerMode.mc.player.posY;
            int posZ = (int) StreamerMode.mc.player.posZ;
            float nether = !inHell ? 0.125f : 8.0f;
            int hposX = (int) (StreamerMode.mc.player.posX * (double) nether);
            int hposZ = (int) (StreamerMode.mc.player.posZ * (double) nether);
            String coordinates = "XYZ " + posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]";
            String text = Phobos.rotationManager.getDirection4D(false);
            drawInfo.add("");
            drawInfo.add(text);
            drawInfo.add(coordinates);
            drawInfo.add("");
            for (Module module : Phobos.moduleManager.sortedModules) {
                String moduleName = TextUtil.stripColor(module.getFullArrayString());
                drawInfo.add(moduleName);
            }
            drawInfo.add("");
            for (PotionEffect effect : Phobos.potionManager.getOwnPotions()) {
                String potionText = TextUtil.stripColor(Phobos.potionManager.getColoredPotionString(effect));
                drawInfo.add(potionText);
            }
            drawInfo.add("");
            Map<String, Integer> map = EntityUtil.getTextRadarPlayers();
            if (!map.isEmpty()) {
                for (Map.Entry<String, Integer> player : map.entrySet()) {
                    String playerText = TextUtil.stripColor(player.getKey());
                    drawInfo.add(playerText);
                }
            }
            this.window.setToDraw(drawInfo);
        }
    }

    public class SecondScreen
            extends JPanel {
        private final int B_WIDTH;
        private final int B_HEIGHT;
        private Font font;
        private ArrayList<String> toDraw;

        public SecondScreen() {
            this.B_WIDTH = StreamerMode.this.width.getValue();
            this.B_HEIGHT = StreamerMode.this.height.getValue();
            this.font = new Font("Verdana", 0, 20);
            this.toDraw = new ArrayList();
            this.initBoard();
        }

        public void setToDraw(ArrayList<String> list) {
            this.toDraw = list;
            this.repaint();
        }

        @Override
        public void setFont(Font font) {
            this.font = font;
        }

        public void setWindowSize(int width, int height) {
            this.setPreferredSize(new Dimension(width, height));
        }

        private void initBoard() {
            this.setBackground(Color.black);
            this.setFocusable(true);
            this.setPreferredSize(new Dimension(this.B_WIDTH, this.B_HEIGHT));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.drawScreen(g);
        }

        private void drawScreen(Graphics g) {
            Font small = this.font;
            FontMetrics metr = this.getFontMetrics(small);
            g.setColor(Color.white);
            g.setFont(small);
            int y = 40;
            for (String msg : this.toDraw) {
                g.drawString(msg, (this.getWidth() - metr.stringWidth(msg)) / 2, y);
                y += 20;
            }
            Toolkit.getDefaultToolkit().sync();
        }
    }

    public class SecondScreenFrame
            extends JFrame {
        private SecondScreen panel;

        public SecondScreenFrame() {
            this.initUI();
        }

        private void initUI() {
            this.panel = new SecondScreen();
            this.add(this.panel);
            this.setResizable(true);
            this.pack();
            this.setTitle("Phobos - Info");
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(2);
        }

        public void setToDraw(ArrayList<String> list) {
            this.panel.setToDraw(list);
        }
    }
}

