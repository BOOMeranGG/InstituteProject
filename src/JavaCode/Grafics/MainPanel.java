package JavaCode.Grafics;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;

class MainPanel extends JPanel {

    private MapPanel mapPanel;
    private JTextField textFieldWidth;
    private JTextField textFieldHeight;
    private JTextField textFieldHouses;
    private JSlider sliderWidth;
    private JSlider sliderHeight;
    private JSlider sliderHouses;
    private JButton buttonA;
    private JButton buttonB;
    private JButton buttonMap;
    private JButton buttonMan;
    private JButton buttonWay;

    MainPanel() {
        setSize(609, 678);
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);
        mapPanel = new MapPanel();
        add(mapPanel).setBounds(0, 0, 500, 500);
        initializeSettingsBounds();
        initializeButtons();
        addListeners();
        addFilters();
    }

    private void initializeSettingsBounds() {
        add(new JLabel("width")).setBounds(10, 510, 50, 23);
        add(new JLabel("height")).setBounds(10, 569, 50, 23);
        add(new JLabel("houses")).setBounds(10, 622, 50, 23);

        textFieldWidth = new JTextField("15");
        add(textFieldWidth).setBounds(10, 533, 50, 23);
        textFieldHeight = new JTextField("15");
        add(textFieldHeight).setBounds(10, 592, 50, 23);
        textFieldHouses = new JTextField("30");
        add(textFieldHouses).setBounds(10, 645, 50, 23);

        sliderWidth = createSlider(2, 50, 15, 1, 12);
        add(sliderWidth).setBounds(70, 510, 420, 46);
        sliderHeight = createSlider(2, 50, 15, 1, 12);
        add(sliderHeight).setBounds(70, 569, 420, 46);
        sliderHouses = createSlider(2, 50, 30, 1, 156);
        add(sliderHouses).setBounds(70, 622, 420, 46);
    }

    private void initializeButtons() {
        buttonA = new JButton(new ImageIcon("res/iconBtnA.png"));
        add(buttonA).setBounds(510, 10, 40, 40);
        buttonB = new JButton(new ImageIcon("res/iconBtnB.png"));
        add(buttonB).setBounds(560, 10, 40, 40);
        buttonMap = new JButton(new ImageIcon("res/iconBtnMap.png"));
        add(buttonMap).setBounds(510, 60, 90, 90);
        buttonMan = new JButton(new ImageIcon("res/iconBtnMan.png"));
        add(buttonMan).setBounds(510, 160, 90, 90);
        buttonWay = new JButton(new ImageIcon("res/iconBtnWay.png"));
        add(buttonWay).setBounds(510, 260, 90, 90);
    }

    private JSlider createSlider(int min, int max, int value, int minor, int major) {
        JSlider slider = new JSlider(min, max, value);
        slider.setMinorTickSpacing(minor);
        slider.setMajorTickSpacing(major);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return slider;
    }

    private void addListeners() {
        coValues(textFieldWidth, sliderWidth);
        coValues(textFieldHeight, sliderHeight);
        coValues(textFieldHouses, sliderHouses);
        buttonA.addActionListener(e -> mapPanel.randBeginHouse());
        buttonB.addActionListener(e -> mapPanel.randEndHouse());
        buttonMap.addActionListener(e -> {
            mapPanel.generateMap(sliderWidth.getValue(), sliderHeight.getValue(),
                    sliderHouses.getValue());
            mapPanel.regenerate();
        });
        buttonMan.addActionListener(e -> mapPanel.displayMan());
        buttonWay.addActionListener(e -> mapPanel.displayWay());
    }

    /**
     * Метод coValues делает значения соответствующих текстовых полей и слайдеров
     * зависящими друг от друга при изменении.
     * А также добавляет зависимость количества домов от остальных значений.
     **/

    private void coValues(JTextField textField, JSlider slider) {
        textField.addActionListener(e -> {
            try {
                slider.setValue(Integer.parseInt(textField.getText()));
                textField.setText(String.valueOf(slider.getValue()));
            }
            catch (Exception exc) {
                textField.setText(String.valueOf(slider.getMaximum()));
                slider.setValue(slider.getMaximum());
            }
        });
        slider.addChangeListener(e -> {
            textField.setText(String.valueOf(slider.getValue()));
            int s = sliderHeight.getValue() * sliderWidth.getValue();
            sliderHouses.setMaximum(s / 2);
            sliderHouses.setMinorTickSpacing(s / 49);
            sliderHouses.setMajorTickSpacing(s / 7);
        });
    }

    private void addFilters() {
        PlainDocument doc;

        doc = (PlainDocument) textFieldWidth.getDocument();
        doc.setDocumentFilter(new DigitFilter());
        doc = (PlainDocument) textFieldHeight.getDocument();
        doc.setDocumentFilter(new DigitFilter());
        doc = (PlainDocument) textFieldHouses.getDocument();
        doc.setDocumentFilter(new DigitFilter());
    }
}
