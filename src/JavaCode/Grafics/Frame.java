package JavaCode.Grafics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Frame extends JFrame {

    private int width;
    private int height;

    public Frame() {
        super("Совет депутатов");
        setIconImage(new ImageIcon("res/icon.png").getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        MainPanel mainPanel = new MainPanel();
        add(mainPanel);
        setVisible(true);
        setSize(new Dimension(609 + getInsets().left + getInsets().right,
                678 + getInsets().top + getInsets().bottom));
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        width = getContentPane().getWidth();
        height = getContentPane().getHeight();
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainPanel.reSize(getContentPane().getWidth(), getContentPane().getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }
}
