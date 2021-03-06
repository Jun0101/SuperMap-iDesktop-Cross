package com.supermap.desktop.ui.controls;

import com.supermap.desktop.ui.CheckTableModel;
import com.supermap.desktop.ui.controls.ChooseTable.MultipleCheckboxTableModel;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by xie on 2016/12/8.
 */
public class CheckHeaderCellRenderer implements TableCellRenderer {
	Object tableModel;
	JTableHeader tableHeader;
	final JCheckBox checkBox;

	/*
	*parm table要渲染的table，titletable标题，isSelectedCheckBox是否选中checkbox
	 */
	public CheckHeaderCellRenderer(JTable table, String title, boolean isSelectedCheckBox) {
		this.tableModel = table.getModel();
		this.tableHeader = table.getTableHeader();
		this.checkBox = new JCheckBox(tableModel instanceof CheckTableModel ? ((CheckTableModel) tableModel).getColumnName(0) : ((MultipleCheckboxTableModel) tableModel).getColumnName(0));
		this.checkBox.setSelected(isSelectedCheckBox);
		this.checkBox.setText(title);
		if (isSelectedCheckBox) {
			if (tableModel instanceof CheckTableModel) {
				((CheckTableModel) tableModel).selectAllOrNull(true);
			} else {
				((MultipleCheckboxTableModel) tableModel).selectAllOrNull(true);
			}
			tableHeader.repaint();
		}
		this.tableHeader.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() > 0) {
					//获得选中列
					int selectColumn = tableHeader.columnAtPoint(e.getPoint());
					if (selectColumn == 0) {
						boolean value = !checkBox.isSelected();
						checkBox.setSelected(value);
						if (tableModel instanceof CheckTableModel) {
							((CheckTableModel) tableModel).selectAllOrNull(value);
						} else {
							((MultipleCheckboxTableModel) tableModel).selectAllOrNull(value);
						}
						tableHeader.repaint();
					}
				}
			}
		});
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
	                                               boolean isSelected, boolean hasFocus, int row, int column) {
		String valueStr = (String) value;
		JLabel label = new JLabel(valueStr);
		label.setHorizontalAlignment(SwingConstants.CENTER); // 表头标签居左边
		this.checkBox.setHorizontalAlignment(SwingConstants.CENTER);// 表头checkBox居中
		//checkBox.setBorderPainted(true);
		JComponent component = (column == 0) ? checkBox : label;

		component.setForeground(tableHeader.getForeground());
		component.setBackground(tableHeader.getBackground());
		component.setFont(tableHeader.getFont());
		component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		return component;
	}
}
