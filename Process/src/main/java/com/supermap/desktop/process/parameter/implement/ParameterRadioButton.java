package com.supermap.desktop.process.parameter.implement;

import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.ParameterDataNode;
import com.supermap.desktop.process.parameter.ParameterPanels.ParameterRadioButtonPanel;
import com.supermap.desktop.process.parameter.interfaces.ISingleSelectionParameter;

import javax.swing.*;
import java.beans.PropertyChangeEvent;

/**
 * @author XiaJT
 */
public class ParameterRadioButton extends AbstractParameter implements ISingleSelectionParameter {
	private JPanel panel;
	private String describe;
	private ParameterDataNode[] items;
	private ParameterDataNode selectedItem;

	@Override
	public String getType() {
		return ParameterType.RADIO_BUTTON;
	}

	@Override
	public JPanel getPanel() {
		if (panel == null) {
			panel = new ParameterRadioButtonPanel(this);
		}
		return panel;
	}

	@Override
	public void setSelectedItem(Object value) {
		if (value instanceof ParameterDataNode) {
			ParameterDataNode oldValue = this.selectedItem;
			this.selectedItem = (ParameterDataNode) value;
			firePropertyChangeListener(new PropertyChangeEvent(this, AbstractParameter.PROPERTY_VALE, oldValue, value));
		}
	}

	@Override
	public int getItemCount() {
		if (items != null) {
			return items.length;
		}
		return 0;
	}

	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}

	@Override
	public ParameterDataNode[] getItems() {
		return items;
	}

	@Override
	public int getItemIndex(Object item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == item) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public ParameterDataNode getItemAt(int index) {
		return items[index];
	}

	public void setItems(ParameterDataNode[] parameterDataNodes) {
		this.items = parameterDataNodes;
	}

	public String getDescribe() {
		return describe;
	}

	public ParameterRadioButton setDescribe(String describe) {
		this.describe = describe;
		return this;
	}

	@Override
	public void dispose() {

	}
}