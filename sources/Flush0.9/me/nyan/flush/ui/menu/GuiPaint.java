package me.nyan.flush.ui.menu;

import me.nyan.flush.Flush;
import me.nyan.flush.ui.elements.Button;
import me.nyan.flush.ui.elements.CheckBox;
import me.nyan.flush.ui.elements.ColorPicker;
import me.nyan.flush.ui.elements.Slider;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MouseUtils;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.ExportingHelper;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.vecmath.Vector2f;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GuiPaint extends GuiScreen {
    public static final String FPA_VERSION = "V1.0";

    private GuiScreen previousScreen;

    private final ArrayList<Position> positions = new ArrayList<>();
    private int clicked;
    private Vector2f lineStartVec;
    private Rectangle2D canvas;

    private ColorPicker colorPicker;
    private Slider thicknessSlider;
    private final ArrayList<Button> buttons = new ArrayList<>();
    private CheckBox lineCheck;

    private String status;
    private File loadedFpa;

    public GuiPaint() {
        status = "§7Waiting...";
    }

    @Override
    public void initGui() {
        super.initGui();

        clicked = -1;

        float y = 50;
        colorPicker = new ColorPicker(18, y, 90, 10, 90,
                colorPicker == null ? 0xFF000000 : colorPicker.getColor(), true);
        y += colorPicker.getHeight() + 6;

        thicknessSlider = new Slider("Line Thickness", 18, y, 90, 20,
                1, 20, 8, 1, -1);
        y += 20 + 2;

        lineCheck = new CheckBox("Line", 18, y, 90, 20,
                lineCheck != null && lineCheck.getValue(), 0xFFBB86FC);

        buttons.clear();

        buttons.add(new Button("Done", width/2f - 100, height - 30, 200, 20));

        buttons.add(new Button("Clear", 10, height - 30, 80, 18));

        buttons.add(new Button("Export as", width - 90, height - 120, 80, 18));
        buttons.add(new Button("Load", width - 90, height - 90, 80, 18));
        buttons.add(new Button("Save", width - 90, height - 60, 80, 18));
        buttons.add(new Button("Save as", width - 90, height - 30, 80, 18));

        for (Button button : buttons) {
            button.setColor(0xFF000000);
        }

        canvas = new Rectangle2D.Float(120, 50, width - 240, height - 100);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();

        if (!lineCheck.getValue()) {
            if (clicked == 0) {
                addPostition(mouseX, mouseY);
            } else if (clicked == 1) {
                addPostition(mouseX, mouseY, -1);
            }
        }

        drawRect(0, 0, width, height, 0xFF121212);
        drawRect(canvas.getX(), canvas.getY(), canvas.getMaxX(), canvas.getMaxY(), -1);

        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.glScissor(canvas.getX(), canvas.getY(), canvas.getMaxX(), canvas.getMaxY());
        GlStateManager.disableTexture2D();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.translate(canvas.getX(), canvas.getY(), 0);
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();

        ArrayList<ArrayList<Position>> pos = new ArrayList<>();
        ArrayList<Position> current = new ArrayList<>();
        for (Position position : positions) {
            if (position == null) {
                pos.add(new ArrayList<>(current));
                current.clear();
                continue;
            }

            current.add(position);
        }
        if (!current.isEmpty()) {
            pos.add(new ArrayList<>(current));
            current.clear();
        }

        for (ArrayList<Position> positions : pos) {
            if (positions.isEmpty()) {
                continue;
            }

            Position first = positions.get(0);
            GL11.glLineWidth(first.getThickness());
            RenderUtils.glColor(first.getColor());

            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (Position p : positions) {
                GL11.glVertex2d(p.getX() * canvas.getWidth(), p.getY() * canvas.getHeight());
            }
            GL11.glEnd();
        }

        if (lineStartVec != null) {
            GL11.glLineWidth(thicknessSlider.getValueInt());
            RenderUtils.glColor(ColorUtils.alpha(colorPicker.getColor(), 100));
            GL11.glBegin(GL11.GL_LINE_STRIP);
            GL11.glVertex2d(lineStartVec.x - canvas.getX(), lineStartVec.y - canvas.getY());
            GL11.glVertex2d(mouseX - canvas.getX(), mouseY - canvas.getY());
            GL11.glEnd();
        }

        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.color(1,1,1);
        GlStateManager.enableTexture2D();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();

        GlyphPageFontRenderer font = Flush.getFont("GoogleSansDisplay", 18);

        font.drawString(
                "Flush Paint - Editing \"" + (loadedFpa == null ? "untitled.fpa" : loadedFpa.getName()) + "\"",
                4,
                4,
                -1
        );
        font.drawString(status, width - font.getStringWidth(status) - 4, 4, -1);

        colorPicker.draw(mouseX, mouseY);
        //alphaSlider.drawScreen(mouseX, mouseY, partialTicks);
        thicknessSlider.drawScreen(mouseX, mouseY);

        lineCheck.drawScreen(mouseX, mouseY, partialTicks);

        buttons.forEach(button -> button.drawButton(mouseX, mouseY));

        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.popMatrix();
    }

    /*
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        IntBuffer buffer = BufferUtils.createIntBuffer(1);
        glPixelStorei(GL_PACK_ALIGNMENT, 1);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glReadPixels(mouseX, height - mouseY, 1, 1, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
        System.out.println("Mouse.getX(): " + Mouse.getX() + ", mouseX: " + mouseX);
        System.out.println("Mouse.getY(): " + Mouse.getY() + ", mouseY: " + mouseY);

        int[] colors = new int[buffer.capacity()];
        buffer.get(colors);
        color = colors[0];
    }
     */

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        clicked = mouseButton;

        buttons.forEach(button -> {
            if (button.mouseClicked(mouseX, mouseY, mouseButton)) {
                switch (button.getName().toLowerCase()) {
                    case "done":
                        mc.displayGuiScreen(previousScreen);
                        break;

                    case "save as":
                        status = save(null);
                        break;

                    case "save":
                        status = save(loadedFpa);
                        break;

                    case "load":
                        status = load();
                        break;

                    case "clear":
                        positions.clear();
                        break;

                    case "export as":
                        JFileChooser chooser = new JFileChooser(Flush.getClientPath());
                        chooser.setDialogTitle(Flush.NAME + " - Select save location");
                        chooser.setAcceptAllFileFilterUsed(false);
                        chooser.setSelectedFile(new File(chooser.getCurrentDirectory(),
                                ExportingHelper.getFileNameForDirectory(chooser.getCurrentDirectory())));
                        for (ExportingHelper.Format format : ExportingHelper.Format.values()) {
                            FileFilter filter = new FileFilter() {
                                @Override
                                public boolean accept(File f) {
                                    return f.getName().toLowerCase().endsWith("." + format.name);
                                }

                                @Override
                                public String getDescription() {
                                    return format.name.toUpperCase();
                                }
                            };
                            chooser.addChoosableFileFilter(filter);
                        }

                        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                            status = EnumChatFormatting.RED + "Cancelled by user";
                            break;
                        }

                        String format = chooser.getFileFilter().getDescription().toLowerCase();
                        File selected = chooser.getSelectedFile();
                        File file = selected.getName().toLowerCase().endsWith("." + format) ? selected :
                                new File(selected.getParentFile(), selected.getName() + "." + format);

                        status = ExportingHelper.saveScreenshot(
                                file.getParentFile(),
                                file.getName(),
                                Arrays.stream(ExportingHelper.Format.values())
                                        .filter(f -> f.name.equalsIgnoreCase(format))
                                        .findFirst()
                                        .orElse(null),
                                240,
                                100,
                                mc.displayWidth - 480,
                                mc.displayHeight - 200
                        );
                        break;
                }
            }
        });

        colorPicker.mouseClicked(mouseX, mouseY, mouseButton);
        //alphaSlider.mouseClicked(mouseX, mouseY, mouseButton);
        thicknessSlider.mouseClicked(mouseX, mouseY, mouseButton);

        if (lineCheck.getValue() &&
                MouseUtils.hovered(mouseX, mouseY, canvas.getX(), canvas.getY(), canvas.getMaxX(), canvas.getMaxY())) {
            lineStartVec = new Vector2f(mouseX, mouseY);
        }

        if (lineCheck.mouseClicked(mouseX, mouseY, mouseButton)) {
            if (!lineCheck.getValue()) {
                lineStartVec = null;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        clicked = -1;
        positions.add(null);
        colorPicker.mouseReleased(state);
        //alphaSlider.mouseReleased();
        thicknessSlider.mouseReleased();

        if (lineStartVec != null) {
            int color = colorPicker.getColor();

            addPostition((int) lineStartVec.x, (int) lineStartVec.y, color);
            addPostition(
                    Math.max(Math.min(mouseX, (int) canvas.getMaxX()), (int) canvas.getX()),
                    Math.max(Math.min(mouseY, (int) canvas.getMaxY()), (int) canvas.getY()),
                    color
            );
            lineStartVec = null;
        }

        super.mouseReleased(mouseX, mouseY, state);
    }

    private void addPostition(int x, int y) {
        addPostition(x, y, colorPicker.getColor());
    }

    private void addPostition(int x, int y, int color) {
        if (x > canvas.getX() && x < canvas.getMaxX() && y > canvas.getY() && y < canvas.getMaxY()) {
            positions.add(new Position((x - (int) canvas.getX()) / canvas.getWidth(),
                    (y - (int) canvas.getY()) / canvas.getHeight(),
                    color, thicknessSlider.getValueInt()));
        }
    }

    public String save(File file) {
        if (file == null) {
            JFileChooser chooser = new JFileChooser(Flush.getClientPath());
            chooser.setDialogTitle(Flush.NAME + " - Select save location");
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setSelectedFile(new File(chooser.getCurrentDirectory(), "New Project.fpa"));
            FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".fpa");
                }

                @Override
                public String getDescription() {
                    return "FPA - Flush Paint Image";
                }
            };
            chooser.setFileFilter(filter);

            if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                return EnumChatFormatting.RED + "Cancelled by user";
            }

            File selected = chooser.getSelectedFile();
            file = selected.getName().toLowerCase().endsWith(".fpa") ? selected : new File(selected.getParentFile(), selected.getName() + ".fpa");
        }

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return EnumChatFormatting.RED + "Failed to save FPA!";
        }

        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println(FPA_VERSION);

            for (Position position : positions) {
                if (position == null) {
                    printWriter.println("#");
                    continue;
                }
                printWriter.println(position.getX() + ":" + position.getY() + ":" + position.getColor() + ":" + position.getThickness());
            }

            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return EnumChatFormatting.RED + "Failed to save FPA!";
        }

        loadedFpa = file;
        return EnumChatFormatting.GREEN + "Saved FPA";
    }

    public String load() {
        JFileChooser chooser = new JFileChooser(Flush.getClientPath());
        chooser.setDialogTitle(Flush.NAME + " - Select FPA");
        chooser.setAcceptAllFileFilterUsed(false);
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".fpa");
            }

            @Override
            public String getDescription() {
                return "FPA - Flush Paint Image";
            }
        };
        chooser.setFileFilter(filter);

        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
            return EnumChatFormatting.RED + "Cancelled by user";
        }

        File selected = chooser.getSelectedFile();

        if (!selected.exists()) {
            return EnumChatFormatting.BLUE + "File doesn't exist";
        }
        ArrayList<String> lines = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(selected));
            String line = reader.readLine();
            if (!line.equals(FPA_VERSION)) {
                return EnumChatFormatting.DARK_RED + "Unsupported format";
            }

            line = reader.readLine();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return EnumChatFormatting.RED + "Failed to read FPA";
        }

        positions.clear();

        for (String string : lines) {
            String[] args = string.split(":");

            try {
                if (string.equals("#")) {
                    positions.add(null);
                    continue;
                }

                positions.add(new Position(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])));
            } catch (Exception e) {
                e.printStackTrace();
                return EnumChatFormatting.RED + "FPA \"" + selected.getName() + "\" is invalid!";
            }
        }

        loadedFpa = selected;
        return EnumChatFormatting.GREEN + "Loaded FPA";
    }

    public void setPreviousScreen(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    private static class Position {
        private final double x;
        private final double y;
        private final int color;
        private final int thickness;

        private Position(double x, double y, int color, int thickness) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.thickness = thickness;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public int getColor() {
            return this.color;
        }

        public int getThickness() {
            return this.thickness;
        }
    }
}
