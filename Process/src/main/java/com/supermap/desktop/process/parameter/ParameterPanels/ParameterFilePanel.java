package com.supermap.desktop.process.parameter.ParameterPanels;

import com.supermap.desktop.process.parameter.implement.AbstractParameter;
import com.supermap.desktop.process.parameter.implement.ParameterFile;
import com.supermap.desktop.ui.controls.FileChooserControl;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Objects;

/**
 * @author XiaJT
 */
public class ParameterFilePanel extends JPanel {
	private ParameterFile parameterFile;
	private FileChooserControl fileChooserControl = new FileChooserControl();
	private boolean isSelectingFile = false;
	private JLabel label = new JLabel();

	public ParameterFilePanel(ParameterFile parameterFile) {
		this.parameterFile = parameterFile;
		// todo fileChooseControl不好用，没时间重构，后面再优化
		if (parameterFile.getSelectedItem() != null) {
			fileChooserControl.setText(((File) parameterFile.getSelectedItem()).getAbsolutePath());
		}
		initListener();
		initLayout();
	}

	private void initListener() {
		fileChooserControl.getButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser = new JFileChooser();
				if (jFileChooser.showOpenDialog(ParameterFilePanel.this) == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jFileChooser.getSelectedFile();
					isSelectingFile = true;
					fileChooserControl.setText(selectedFile.getAbsolutePath());
					ParameterFilePanel.this.parameterFile.setSelectedItem(selectedFile);
					isSelectingFile = false;
				}
			}
		});
		parameterFile.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectingFile && Objects.equals(evt.getPropertyName(), AbstractParameter.PROPERTY_VALE)) {
					fileChooserControl.setText(((File) evt.getNewValue()).getAbsolutePath());
				}
			}
		});
	}

	private void initLayout() {
		fileChooserControl.setPreferredSize(new Dimension(20, 23));
		this.setLayout(new GridBagLayout());
		this.add(label, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.NONE));
		this.add(fileChooserControl, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(0, 5, 0, 0).setFill(GridBagConstraints.HORIZONTAL));
	}
}
