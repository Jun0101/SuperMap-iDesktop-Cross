package com.supermap.desktop.workspacemanagerwindow;

import com.supermap.data.Datasource;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataview.DataViewResources;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static com.supermap.desktop.workspacemanagerwindow.WorkspaceManagerWindowResources.*;

/**
 * @author YuanR
 */
public class TableCellRendererDatasource extends DefaultTableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value,
	                                               boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		//根据数据源类型，添加相应图标
		if (column == COLUMN_NAME) {
			this.setIcon(DataViewResources.getIcon(DATAVIEW_ICON_ROOTPATH + table.getModel().getValueAt(row, COLUMN_TYPE) + ".png"));
			this.setText(((Datasource) value).getAlias());
		}
		if (column == COLUMN_NUMBER) {
			//靠左对齐
			this.setHorizontalAlignment(LEFT);
		}
		return this;
	}
}
