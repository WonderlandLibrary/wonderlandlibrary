package me.theresa.fontRenderer.font.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import me.theresa.fontRenderer.font.effect.ConfigurableEffect;

public class EffectUtil {
   private static final BufferedImage scratchImage = new BufferedImage(256, 256, 2);

   public static BufferedImage getScratchImage() {
      Graphics2D g = (Graphics2D)scratchImage.getGraphics();
      g.setComposite(AlphaComposite.Clear);
      g.fillRect(0, 0, 256, 256);
      g.setComposite(AlphaComposite.SrcOver);
      g.setColor(Color.white);
      return scratchImage;
   }

   public static ConfigurableEffect.Value colorValue(String name, Color currentValue) {
      return new DefaultValue(name, toString(currentValue)) {
         public void showDialog() {
            Color newColor = JColorChooser.showDialog((Component)null, "Choose a color", EffectUtil.fromString(this.value));
            if (newColor != null) {
               this.value = EffectUtil.toString(newColor);
            }

         }

         public Object getObject() {
            return EffectUtil.fromString(this.value);
         }
      };
   }

   public static ConfigurableEffect.Value intValue(String name, final int currentValue, final String description) {
      return new DefaultValue(name, String.valueOf(currentValue)) {
         public void showDialog() {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(currentValue, -32768, 32767, 1));
            if (this.showValueDialog(spinner, description)) {
               this.value = String.valueOf(spinner.getValue());
            }

         }

         public Object getObject() {
            return Integer.valueOf(this.value);
         }
      };
   }

   public static ConfigurableEffect.Value floatValue(String name, final float currentValue, final float min, final float max, final String description) {
      return new DefaultValue(name, String.valueOf(currentValue)) {
         public void showDialog() {
            JSpinner spinner = new JSpinner(new SpinnerNumberModel((double)currentValue, (double)min, (double)max, 0.10000000149011612));
            if (this.showValueDialog(spinner, description)) {
               this.value = String.valueOf(((Double)spinner.getValue()).floatValue());
            }

         }

         public Object getObject() {
            return Float.valueOf(this.value);
         }
      };
   }

   public static ConfigurableEffect.Value booleanValue(String name, final boolean currentValue, final String description) {
      return new DefaultValue(name, String.valueOf(currentValue)) {
         public void showDialog() {
            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(currentValue);
            if (this.showValueDialog(checkBox, description)) {
               this.value = String.valueOf(checkBox.isSelected());
            }

         }

         public Object getObject() {
            return Boolean.valueOf(this.value);
         }
      };
   }

   public static ConfigurableEffect.Value optionValue(String name, final String currentValue, final String[][] options, final String description) {
      return new DefaultValue(name, currentValue) {
         public void showDialog() {
            int selectedIndex = -1;
            DefaultComboBoxModel model = new DefaultComboBoxModel();

            for(int i = 0; i < options.length; ++i) {
               model.addElement(options[i][0]);
               if (this.getValue(i).equals(currentValue)) {
                  selectedIndex = i;
               }
            }

            JComboBox comboBox = new JComboBox(model);
            comboBox.setSelectedIndex(selectedIndex);
            if (this.showValueDialog(comboBox, description)) {
               this.value = this.getValue(comboBox.getSelectedIndex());
            }

         }

         private String getValue(int i) {
            return options[i].length == 1 ? options[i][0] : options[i][1];
         }

         public String toString() {
            for(int i = 0; i < options.length; ++i) {
               if (this.getValue(i).equals(this.value)) {
                  return options[i][0];
               }
            }

            return "";
         }

         public Object getObject() {
            return this.value;
         }
      };
   }

   public static String toString(Color color) {
      if (color == null) {
         throw new IllegalArgumentException("color cannot be null.");
      } else {
         String r = Integer.toHexString(color.getRed());
         if (r.length() == 1) {
            r = "0" + r;
         }

         String g = Integer.toHexString(color.getGreen());
         if (g.length() == 1) {
            g = "0" + g;
         }

         String b = Integer.toHexString(color.getBlue());
         if (b.length() == 1) {
            b = "0" + b;
         }

         return r + g + b;
      }
   }

   public static Color fromString(String rgb) {
      return rgb != null && rgb.length() == 6 ? new Color(Integer.parseInt(rgb.substring(0, 2), 16), Integer.parseInt(rgb.substring(2, 4), 16), Integer.parseInt(rgb.substring(4, 6), 16)) : Color.white;
   }

   private static class ValueDialog extends JDialog {
      public boolean okPressed = false;

      public ValueDialog(JComponent component, String name, String description) {
         this.setDefaultCloseOperation(2);
         this.setLayout(new GridBagLayout());
         this.setModal(true);
         if (component instanceof JSpinner) {
            ((JSpinner.DefaultEditor)((JSpinner)component).getEditor()).getTextField().setColumns(4);
         }

         JPanel descriptionPanel = new JPanel();
         descriptionPanel.setLayout(new GridBagLayout());
         this.getContentPane().add(descriptionPanel, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
         descriptionPanel.setBackground(Color.white);
         descriptionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
         JTextArea descriptionText = new JTextArea(description);
         descriptionPanel.add(descriptionText, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
         descriptionText.setWrapStyleWord(true);
         descriptionText.setLineWrap(true);
         descriptionText.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
         descriptionText.setEditable(false);
         JPanel panel = new JPanel();
         this.getContentPane().add(panel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 10, 0, new Insets(5, 5, 0, 5), 0, 0));
         panel.add(new JLabel(name + ":"));
         panel.add(component);
         JPanel buttonPanel = new JPanel();
         this.getContentPane().add(buttonPanel, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, 13, 0, new Insets(0, 0, 0, 0), 0, 0));
         JButton cancelButton = new JButton("OK");
         buttonPanel.add(cancelButton);
         cancelButton.addActionListener((evt) -> {
            this.okPressed = true;
            this.setVisible(false);
         });
         cancelButton = new JButton("Cancel");
         buttonPanel.add(cancelButton);
         cancelButton.addActionListener((evt) -> {
            this.setVisible(false);
         });
         this.setSize(new Dimension(320, 175));
      }
   }

   private abstract static class DefaultValue implements ConfigurableEffect.Value {
      String value;
      String name;

      public DefaultValue(String name, String value) {
         this.value = value;
         this.name = name;
      }

      public void setString(String value) {
         this.value = value;
      }

      public String getString() {
         return this.value;
      }

      public String getName() {
         return this.name;
      }

      public String toString() {
         return this.value == null ? "" : this.value;
      }

      public boolean showValueDialog(JComponent component, String description) {
         ValueDialog dialog = new ValueDialog(component, this.name, description);
         dialog.setTitle(this.name);
         dialog.setLocationRelativeTo((Component)null);
         EventQueue.invokeLater(() -> {
            JComponent focusComponent = component;
            if (component instanceof JSpinner) {
               focusComponent = ((JSpinner.DefaultEditor)((JSpinner)component).getEditor()).getTextField();
            }

            ((JComponent)focusComponent).requestFocusInWindow();
         });
         dialog.setVisible(true);
         return dialog.okPressed;
      }
   }
}
