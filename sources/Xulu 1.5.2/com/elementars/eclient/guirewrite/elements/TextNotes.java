// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.guirewrite.elements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class TextNotes extends JFrame
{
    JPanel jp;
    JLabel jl;
    JTextField jt;
    JButton jb;
    
    public TextNotes() {
        this.jp = new JPanel();
        this.jl = new JLabel();
        this.jt = new JTextField(30);
        this.jb = new JButton("Set Text");
        this.setTitle("TextNotes");
        this.setVisible(true);
        this.setSize(400, 200);
        this.setDefaultCloseOperation(1);
        this.jp.add(this.jt);
        this.jt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String input = TextNotes.this.jt.getText();
                TextNotes.this.jl.setText(input);
            }
        });
        this.jp.add(this.jb);
        this.jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final String input = TextNotes.this.jt.getText();
                StickyNotes.processText(input);
            }
        });
        this.jp.add(this.jl);
        this.add(this.jp);
    }
    
    public static void initTextBox() {
        final TextNotes t = new TextNotes();
    }
}
