package com.supermap.desktop.implement;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

/**
 * Created by xie on 2016/11/7.
 */
public class DefaultComboBoxUI extends BasicComboBoxUI {
    public static ComponentUI createUI(JComponent c) {
        return new DefaultComboBoxUI();
    }

    protected JButton createArrowButton() {
        JButton button = new InnerArrowButton(BasicArrowButton.SOUTH,
                Color.white,
                Color.white,
                Color.black,
                UIManager.getColor("ComboBox.buttonHighlight"));
        button.setBorder(new LineBorder(Color.white));
        button.setName("ComboBox.arrowButton");
        button.setMargin(new Insets(0, 3, 0, 1));
        return button;
    }
}
