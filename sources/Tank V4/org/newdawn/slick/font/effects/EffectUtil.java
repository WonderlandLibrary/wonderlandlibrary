package org.newdawn.slick.font.effects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JSpinner.DefaultEditor;

public class EffectUtil {
   private static BufferedImage scratchImage = new BufferedImage(256, 256, 2);

   public static BufferedImage getScratchImage() {
      Graphics2D var0 = (Graphics2D)scratchImage.getGraphics();
      var0.setComposite(AlphaComposite.Clear);
      var0.fillRect(0, 0, 256, 256);
      var0.setComposite(AlphaComposite.SrcOver);
      var0.setColor(Color.white);
      return scratchImage;
   }

   public static ConfigurableEffect.Value colorValue(String var0, Color var1) {
      return new EffectUtil.DefaultValue(var0, toString(var1)) {
         public void showDialog() {
            Color var1 = JColorChooser.showDialog((Component)null, "Choose a color", EffectUtil.fromString(this.value));
            if (var1 != null) {
               this.value = EffectUtil.toString(var1);
            }

         }

         public Object getObject() {
            return EffectUtil.fromString(this.value);
         }
      };
   }

   public static ConfigurableEffect.Value intValue(String var0, int var1, String var2) {
      return new EffectUtil.DefaultValue(var0, String.valueOf(var1), var1, var2) {
         private final int val$currentValue;
         private final String val$description;

         {
            this.val$currentValue = var3;
            this.val$description = var4;
         }

         public void showDialog() {
            JSpinner var1 = new JSpinner(new SpinnerNumberModel(this.val$currentValue, -32768, 32767, 1));
            if (this.showValueDialog(var1, this.val$description)) {
               this.value = String.valueOf(var1.getValue());
            }

         }

         public Object getObject() {
            return Integer.valueOf(this.value);
         }
      };
   }

   public static ConfigurableEffect.Value floatValue(String var0, float var1, float var2, float var3, String var4) {
      return new EffectUtil.DefaultValue(var0, String.valueOf(var1), var1, var2, var3, var4) {
         private final float val$currentValue;
         private final float val$min;
         private final float val$max;
         private final String val$description;

         {
            this.val$currentValue = var3;
            this.val$min = var4;
            this.val$max = var5;
            this.val$description = var6;
         }

         public void showDialog() {
            JSpinner var1 = new JSpinner(new SpinnerNumberModel((double)this.val$currentValue, (double)this.val$min, (double)this.val$max, 0.10000000149011612D));
            if (this.showValueDialog(var1, this.val$description)) {
               this.value = String.valueOf(((Double)var1.getValue()).floatValue());
            }

         }

         public Object getObject() {
            return Float.valueOf(this.value);
         }
      };
   }

   public static ConfigurableEffect.Value booleanValue(String var0, boolean var1, String var2) {
      return new EffectUtil.DefaultValue(var0, String.valueOf(var1), var1, var2) {
         private final boolean val$currentValue;
         private final String val$description;

         {
            this.val$currentValue = var3;
            this.val$description = var4;
         }

         public void showDialog() {
            JCheckBox var1 = new JCheckBox();
            var1.setSelected(this.val$currentValue);
            if (this.showValueDialog(var1, this.val$description)) {
               this.value = String.valueOf(var1.isSelected());
            }

         }

         public Object getObject() {
            return Boolean.valueOf(this.value);
         }
      };
   }

   public static ConfigurableEffect.Value optionValue(String var0, String var1, String[][] var2, String var3) {
      return new EffectUtil.DefaultValue(var0, var1.toString(), var2, var1, var3) {
         private final String[][] val$options;
         private final String val$currentValue;
         private final String val$description;

         {
            this.val$options = var3;
            this.val$currentValue = var4;
            this.val$description = var5;
         }

         public void showDialog() {
            int var1 = -1;
            DefaultComboBoxModel var2 = new DefaultComboBoxModel();

            for(int var3 = 0; var3 < this.val$options.length; ++var3) {
               var2.addElement(this.val$options[var3][0]);
               if (this.getValue(var3).equals(this.val$currentValue)) {
                  var1 = var3;
               }
            }

            JComboBox var4 = new JComboBox(var2);
            var4.setSelectedIndex(var1);
            if (this.showValueDialog(var4, this.val$description)) {
               this.value = this.getValue(var4.getSelectedIndex());
            }

         }

         private String getValue(int var1) {
            return this.val$options[var1].length == 1 ? this.val$options[var1][0] : this.val$options[var1][1];
         }

         public String toString() {
            for(int var1 = 0; var1 < this.val$options.length; ++var1) {
               if (this.getValue(var1).equals(this.value)) {
                  return this.val$options[var1][0].toString();
               }
            }

            return "";
         }

         public Object getObject() {
            return this.value;
         }
      };
   }

   public static String toString(Color var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("color cannot be null.");
      } else {
         String var1 = Integer.toHexString(var0.getRed());
         if (var1.length() == 1) {
            var1 = "0" + var1;
         }

         String var2 = Integer.toHexString(var0.getGreen());
         if (var2.length() == 1) {
            var2 = "0" + var2;
         }

         String var3 = Integer.toHexString(var0.getBlue());
         if (var3.length() == 1) {
            var3 = "0" + var3;
         }

         return var1 + var2 + var3;
      }
   }

   public static Color fromString(String var0) {
      return var0 != null && var0.length() == 6 ? new Color(Integer.parseInt(var0.substring(0, 2), 16), Integer.parseInt(var0.substring(2, 4), 16), Integer.parseInt(var0.substring(4, 6), 16)) : Color.white;
   }

   private static class ValueDialog extends JDialog {
      public boolean okPressed = false;

      public ValueDialog(JComponent var1, String var2, String var3) {
         this.setDefaultCloseOperation(2);
         this.setLayout(new GridBagLayout());
         this.setModal(true);
         if (var1 instanceof JSpinner) {
            ((DefaultEditor)((JSpinner)var1).getEditor()).getTextField().setColumns(4);
         }

         JPanel var4 = new JPanel();
         var4.setLayout(new GridBagLayout());
         this.getContentPane().add(var4, new GridBagConstraints(0, 0, 2, 1, 1.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
         var4.setBackground(Color.white);
         var4.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
         JTextArea var5 = new JTextArea(var3);
         var4.add(var5, new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
         var5.setWrapStyleWord(true);
         var5.setLineWrap(true);
         var5.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
         var5.setEditable(false);
         JPanel var8 = new JPanel();
         this.getContentPane().add(var8, new GridBagConstraints(0, 1, 1, 1, 1.0D, 1.0D, 10, 0, new Insets(5, 5, 0, 5), 0, 0));
         var8.add(new JLabel(var2 + ":"));
         var8.add(var1);
         JPanel var6 = new JPanel();
         this.getContentPane().add(var6, new GridBagConstraints(0, 2, 2, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 0), 0, 0));
         JButton var7 = new JButton("OK");
         var6.add(var7);
         var7.addActionListener(new ActionListener(this) {
            private final EffectUtil.ValueDialog this$0;

            {
               this.this$0 = var1;
            }

            public void actionPerformed(ActionEvent var1) {
               this.this$0.okPressed = true;
               this.this$0.setVisible(false);
            }
         });
         var7 = new JButton("Cancel");
         var6.add(var7);
         var7.addActionListener(new ActionListener(this) {
            private final EffectUtil.ValueDialog this$0;

            {
               this.this$0 = var1;
            }

            public void actionPerformed(ActionEvent var1) {
               this.this$0.setVisible(false);
            }
         });
         this.setSize(new Dimension(320, 175));
      }
   }

   private abstract static class DefaultValue implements ConfigurableEffect.Value {
      String value;
      String name;

      public DefaultValue(String var1, String var2) {
         this.value = var2;
         this.name = var1;
      }

      public void setString(String var1) {
         this.value = var1;
      }

      public String getString() {
         return this.value;
      }

      public String getName() {
         return this.name;
      }

      public String toString() {
         return this.value == null ? "" : this.value.toString();
      }

      public boolean showValueDialog(JComponent var1, String var2) {
         EffectUtil.ValueDialog var3 = new EffectUtil.ValueDialog(var1, this.name, var2);
         var3.setTitle(this.name);
         var3.setLocationRelativeTo((Component)null);
         EventQueue.invokeLater(new Runnable(this, var1) {
            private final JComponent val$component;
            private final EffectUtil.DefaultValue this$0;

            {
               this.this$0 = var1;
               this.val$component = var2;
            }

            public void run() {
               Object var1 = this.val$component;
               if (var1 instanceof JSpinner) {
                  var1 = ((DefaultEditor)((JSpinner)this.val$component).getEditor()).getTextField();
               }

               ((JComponent)var1).requestFocusInWindow();
            }
         });
         var3.setVisible(true);
         return var3.okPressed;
      }
   }
}
