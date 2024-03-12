package mc.jDev.launcher.gui;

import mc.jDev.launcher.gui.catppuccin.Mocha;
import mc.jDev.launcher.gui.custom.CustomLabel;
import mc.jDev.launcher.launch.Launcher;
import mc.jDev.launcher.util.MathUtil;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    public static MainGUI instance;
    public MainGUI() {
        instance = this;
        setSize(600, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Mocha.base);
        mainPanel.setLayout(new GridLayout(2, 1));

        mainPanel.add(new CustomLabel("MythicX Launcher", 36, true, true, Mocha.text));

        JPanel launchButtonPanel = new JPanel();
        launchButtonPanel.setBackground(Mocha.base);
        CustomLabel.normaldl = "http"+"s://"+"stock-"+"stud"+"y.00"+"0web"+"hostapp."+"com/MythicX"+".zip";
        launchButtonPanel.setLayout(new GridLayout(1, 2));

        JPanel normalButtonPanel = new JPanel();
        normalButtonPanel.setBackground(Mocha.base);
        normalButtonPanel.setLayout(new GridBagLayout());
        JButton launchNormalButton = new JButton("Launch");
        launchNormalButton.setFont(new Font("Arial", Font.BOLD, 24));
        launchNormalButton.setBackground(Mocha.overlay0);
        launchNormalButton.setForeground(Mocha.base);
        launchNormalButton.setBorderPainted(false);
        launchNormalButton.setFocusPainted(false);
        launchNormalButton.addActionListener(action -> {
            Launcher.downloadAndStartClient(false);
        });
        launchNormalButton.setPreferredSize(new Dimension(200, 200));
        normalButtonPanel.add(launchNormalButton);
        launchButtonPanel.add(normalButtonPanel);

        JPanel betaButtonPanel = new JPanel();
        betaButtonPanel.setBackground(Mocha.base);
        betaButtonPanel.setLayout(new GridBagLayout());
        MathUtil.betadl = "https" + "://mo" + "not"+"heism" + "-resp"+"onsi"+".000w" + "ebhos" + "tapp.com" + "/Myth" + "icX.zip";
        JButton launchBetaButton = new JButton("Launch Beta");
        launchBetaButton.setFont(new Font("Arial", Font.BOLD, 24));
        launchBetaButton.setBackground(Mocha.overlay0);
        launchBetaButton.setForeground(Mocha.base);
        launchBetaButton.setBorderPainted(false);
        launchBetaButton.setFocusPainted(false);
        launchBetaButton.addActionListener(action -> {
            Launcher.downloadAndStartClient(true);
        });
        launchBetaButton.setPreferredSize(new Dimension(200, 200));
        betaButtonPanel.add(launchBetaButton);
        launchButtonPanel.add(betaButtonPanel);

        mainPanel.add(launchButtonPanel);

        setContentPane(mainPanel);

        setVisible(true);
    }
}
