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
    private JButton buttonPlus;
    private JButton buttonMinus;
    private JLabel labelSpeed;
    private JLabel labelWidth;
    private JLabel labelHeight;
    private JLabel labelHouses;


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
        setBounds(0, 0);
    }

    private void initializeSettingsBounds() {
        labelWidth = new JLabel("width");
        add(labelWidth);
        labelHeight = new JLabel("height");
        add(labelHeight);
        labelHouses = new JLabel("houses");
        add(labelHouses);

        textFieldWidth = new JTextField("15");
        add(textFieldWidth);
        textFieldHeight = new JTextField("15");
        add(textFieldHeight);
        textFieldHouses = new JTextField("30");
        add(textFieldHouses);

        sliderWidth = createSlider(7, 50, 15, 1, 12);
        add(sliderWidth);
        sliderHeight = createSlider(7, 50, 15, 1, 12);
        add(sliderHeight);
        sliderHouses = createSlider(2, 50, 30, 1, 156);
        add(sliderHouses);

        labelSpeed = new JLabel("1");
        add(labelSpeed);
    }

    private void initializeButtons() {
        buttonA = new JButton(new ImageIcon("res/iconBtnA.png"));
        add(buttonA).setBounds(510, 10, 40, 40);
        buttonB = new JButton(new ImageIcon("res/iconBtnB.png"));
        add(buttonB).setBounds(560, 10, 40, 40);
        buttonMap = new JButton(new ImageIcon("res/iconBtnMap.png"));
        add(buttonMap).setBounds(510, 60, 90, 90);
        buttonWay = new JButton(new ImageIcon("res/iconBtnWay.png"));
        add(buttonWay).setBounds(510, 160, 90, 90);
        buttonMan = new JButton(new ImageIcon("res/iconBtnMan.png"));
        add(buttonMan).setBounds(510, 260, 90, 90);
        buttonMinus = new JButton("-");
        add(buttonMinus).setBounds(510, 360, 41, 40);
        buttonPlus = new JButton("+");
        add(buttonPlus).setBounds(559, 360, 41, 40);
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
            mapPanel.reGenerate();
        });
        buttonMan.addActionListener(e -> mapPanel.displayMan());
        buttonWay.addActionListener(e -> mapPanel.displayWay());
        buttonPlus.addActionListener(e -> {
            int local = Integer.parseInt(labelSpeed.getText());
            if (local < 9) {
                mapPanel.setSpeed(local + 1);
                labelSpeed.setText(String.valueOf(local + 1));
            }
        });
        buttonMinus.addActionListener(e -> {
            int local = Integer.parseInt(labelSpeed.getText());
            if (local > 1) {
                mapPanel.setSpeed(local - 1);
                labelSpeed.setText(String.valueOf(local - 1));
            }
        });
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
            sliderHouses.setMaximum(s / 4);
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

    public void reSize(int width, int height) {
        setBounds(width - 609, height - 678);
        mapPanel.setSize(width - 109, height - 178);
    }

    private void setBounds(int width, int height) {
        labelWidth.setBounds(10, 510 + height, 50, 23);
        labelHeight.setBounds(10, 569 + height, 50, 23);
        labelHouses.setBounds(10, 622 + height, 50, 23);
        textFieldWidth.setBounds(10, 533 + height, 50, 23);
        textFieldHeight.setBounds(10, 592 + height, 50, 23);
        textFieldHouses.setBounds(10, 645 + height, 50, 23);
        sliderWidth.setBounds(70, 510 + height, 420 + width, 46);
        sliderHeight.setBounds(70, 569 + height, 420 + width, 46);
        sliderHouses.setBounds(70, 622 + height, 420 + width, 46);

        labelSpeed.setBounds(552 + width, 360, 42, 40);
        buttonA.setBounds(510 + width, 10, 40, 40);
        buttonB.setBounds(560 + width, 10, 40, 40);
        buttonMap.setBounds(510 + width, 60, 90, 90);
        buttonWay.setBounds(510 + width, 160, 90, 90);
        buttonMan.setBounds(510 + width, 260, 90, 90);
        buttonMinus.setBounds(510 + width, 360, 41, 40);
        buttonPlus.setBounds(559 + width, 360, 41, 40);
    }
}
