package kevin.utils.proxy;

import javax.swing.*;

public class ProxySelectorSUI extends JFrame {
    private JButton confirmButton;
    private JList<String> proxyList;
    private JPanel pane;

    public ProxySelectorSUI(GuiProxySelect gui) {
        setContentPane(pane);
        setTitle("Proxy Selector");
        setSize(300, 600);
        confirmButton.addActionListener(e -> {
            String value = proxyList.getSelectedValue();
            if (value == null) return;
            FreeProxyManager.INSTANCE.getProxies().stream().filter(it -> value.endsWith(it.getSecond())).findFirst().ifPresent(p -> {
                ProxyManager.INSTANCE.setProxyType(p.getFirst().type());
                String string = p.getFirst().address().toString();
                gui.textField.setText(string.substring(string.lastIndexOf("/") + 1));
            });
            setVisible(false);
        });

        updateList();
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setVisible(true);
        try {
            setAlwaysOnTop(true);
        } catch (Throwable ignored) {}
    }

    public void updateList() {
        proxyList.setListData(
                FreeProxyManager.INSTANCE.getProxies().stream()
                        .map(it -> FreeProxyManager.INSTANCE.getPings().getOrDefault(it.getSecond(), "no ping") + "| " + it.getSecond())
                        .filter(it -> !it.endsWith("time out"))
                        .sorted()
                        .toArray(String[]::new)
        );
    }
}
