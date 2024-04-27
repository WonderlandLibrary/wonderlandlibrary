package me.aquavit;

import me.aquavit.liquidsense.LiquidSense;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Instruction {
    public static void main(String[] args) {
        JFrame frame = new JFrame("LiquidSense | Instruction.");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        InputStream inputStream = LiquidSense.class.getResourceAsStream("/instructions.html");
        StringBuilder msg = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader br=new BufferedReader(inputStreamReader);
            String line;
            while((line=br.readLine()) != null){
                msg.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JLabel label = new JLabel(msg.toString());
        frame.add(label, "Center");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
