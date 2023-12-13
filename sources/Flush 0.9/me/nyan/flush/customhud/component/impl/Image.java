package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.setting.impl.BooleanSetting;
import me.nyan.flush.customhud.setting.impl.TextSetting;
import me.nyan.flush.notifications.Notification;
import me.nyan.flush.ui.elements.ImageRenderer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Image extends Component {
    private BooleanSetting set;

    private TextSetting path;
    private String loadedPath = "";

    private ImageRenderer imageRenderer;

    @Override
    public void onAdded() {
        settings.add(set = new BooleanSetting("Set image", false));
        settings.add(path = new TextSetting("Image path", ""));
    }

    @Override
    public void draw(float x, float y) {
        if (set.getValue()) {
            set.setValue(false);
            chooseImage();
        }

        File file = new File(path.getValue());

        if (path.getValue().isEmpty()) {
            loadedPath = "";
            return;
        } else if (!path.getValue().equals(loadedPath) && file.exists() && !file.isDirectory()) {
            loadedPath = "";
            loadImage();
            return;
        }

        if (!file.exists() || file.isDirectory() || imageRenderer == null) {
            loadedPath = "";
            if (imageRenderer != null) {
                imageRenderer.dispose();
                imageRenderer = null;
            }
            return;
        }

        imageRenderer.draw(x, y, imageRenderer.getWidth(), imageRenderer.getHeight());
    }

    @Override
    public int width() {
        if (imageRenderer != null) {
            return imageRenderer.getWidth();
        }
        return 100;
    }

    @Override
    public int height() {
        if (imageRenderer != null) {
            return imageRenderer.getHeight();
        }
        return 100;
    }

    @Override
    public ResizeType getResizeType() {
        return ResizeType.CUSTOM;
    }

    private void chooseImage() {
        JDialog dialog = new JDialog();
        dialog.setAlwaysOnTop(true);
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(Flush.NAME + " - Select image");

        if (chooser.showOpenDialog(dialog) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        path.setValue(chooser.getSelectedFile().getAbsolutePath());

        loadImage();
        if (!loadedPath.isEmpty()) {
            flush.getNotificationManager().show(Notification.Type.INFO, "CustomHUD", "Set image to " + path.getValue(), 3000);
        }
    }

    private void loadImage() {
        if (imageRenderer != null) {
            imageRenderer.dispose();
            imageRenderer = null;
        }

        File file = new File(path.getValue());

        try {
            InputStream stream = Files.newInputStream(file.toPath());
            imageRenderer = new ImageRenderer();

            int status = imageRenderer.load(stream);
            if (status == ImageRenderer.STATUS_FAILURE) {
                throw new IOException();
            }
            if (status == ImageRenderer.STATUS_INVALID_FORMAT) {
                flush.getNotificationManager().show(Notification.Type.ERROR, "CustomHUD", "Failed to load image: format unsupported", 3000);
                path.setValue("");
                customHud.save();
                imageRenderer = null;
            }
        } catch (IOException e) {
            flush.getNotificationManager().show(Notification.Type.ERROR, "CustomHUD", "Failed to load image", 3000);
            path.setValue("");
            customHud.save();
            imageRenderer = null;
        }
        loadedPath = path.getValue();
    }

    @Override
    public void dispose() {
        if (imageRenderer != null) {
            imageRenderer.dispose();
            imageRenderer = null;
        }
    }
}
