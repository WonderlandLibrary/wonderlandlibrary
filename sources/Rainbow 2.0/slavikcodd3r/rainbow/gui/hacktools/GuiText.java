package slavikcodd3r.rainbow.gui.hacktools;

import java.awt.Component;
import java.awt.LayoutManager;
import javax.swing.GroupLayout;
import java.awt.TextArea;
import javax.swing.JFrame;

public class GuiText extends JFrame
{
    public TextArea textArea1;
    
    public GuiText() {
        this.initComponents();
    }
    
    private void initComponents() {
        this.textArea1 = new TextArea();
        this.setTitle("Result");
        final GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.textArea1, -1, 619, 32767).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.textArea1, -1, 330, 32767).addContainerGap()));
        this.pack();
    }
}
