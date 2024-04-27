package me.report.Hwid;

import net.ccbluex.liquidbounce.LiquidBounce;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class HwidCheckKyinoSense {
    public static String clientName = LiquidBounce.CLIENT_NN;

    public static void LoadingTitle() {
        Display.setTitle(LiquidBounce.CLIENT_NN + " Loading...");
    }
    public static void Title() {
        Display.setTitle(LiquidBounce.CLIENT_NN + " | " + version + " | Username: " + HwidCheckKyinoSense.username);
    }

    public static String version = "b2";
    public static String username;
    public static boolean Finish;

    public static void sendWindowsMessageLogin() throws AWTException, IOException {
        //SystemUtils.displayTray("请输入账号密码", "Login", TrayIcon.MessageType.WARNING);
        String AT = JOptionPane.showInputDialog("Напишите ваш код");
        final int R = 0;
        HwidCheckKyinoSense.username = AT;

        try {
            if (username == null) {
                JOptionPane.showMessageDialog(null, "Ваша строка пуста!", "DineTire | HWID", 0);
                System.exit(0);
            } else {
                if (WebUtils.get("ваша ссылка на гидхаб или гитте").contains(HWIDUtlis.getHWID())) {
                    JOptionPane.showMessageDialog(null, "DineTire B2 запущен - Подписка активирована", "DineTire | HWID", 1);
                    //SystemUtils.displayTray("登陆成功", "正在启动", TrayIcon.MessageType.WARNING);
                    HwidCheckKyinoSense.Finish = true;
                } else {
                    JOptionPane.showMessageDialog(null, "На вашем аккаунте нету подписки на чит", "DineTire | HWID", 0);
                    JOptionPane.showInputDialog(null, "Передайте этот код продавцу чита", HWIDUtlis.getHWID());
                    //SystemUtils.displayTray("您还未绑定HWID,请绑定后重试！", "您还未绑定HWID,请绑定后重试！", TrayIcon.MessageType.WARNING);
                    System.exit(0);
                }
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            JOptionPane.showMessageDialog(null, "Подключи уже интернет! :D");
            //SystemUtils.displayTray("连接服务器失败,无效的登录会话", "连接服务器失败,无效的登录会话", TrayIcon.MessageType.WARNING);
            System.exit(0);
        }
    }
}
