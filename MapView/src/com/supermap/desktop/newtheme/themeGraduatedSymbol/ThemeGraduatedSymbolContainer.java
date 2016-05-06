package com.supermap.desktop.newtheme.themeGraduatedSymbol;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.supermap.data.*;
import com.supermap.desktop.controls.utilties.SymbolDialogFactory;
import com.supermap.desktop.dialog.symbolDialogs.ISymbolApply;
import com.supermap.desktop.dialog.symbolDialogs.SymbolDialog;
import com.supermap.desktop.enums.UnitValue;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.newtheme.commonPanel.ThemeChangePanel;
import com.supermap.desktop.newtheme.commonUtils.ThemeGuideFactory;
import com.supermap.desktop.newtheme.commonUtils.ThemeUtil;
import com.supermap.desktop.ui.UICommonToolkit;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.LayersTree;
import com.supermap.desktop.utilties.MapUtilties;
import com.supermap.desktop.utilties.StringUtilties;
import com.supermap.mapping.*;
import com.supermap.ui.MapControl;

/**
 * 等级符号专题图
 * 
 * @author xie
 *
 */
public class ThemeGraduatedSymbolContainer extends ThemeChangePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPaneInfo;
	private JPanel panelProperty;
	private JPanel panelSymbolStyle;
	private JPanel panelOffset;

	private JLabel labelExpression;
	private JLabel labelGraduatedMode;
	private JLabel labelBaseValue;
	private JComboBox<String> comboBoxExpression;// 表达式
	private JComboBox<String> comboBoxGraduatedMode;// 分级方式
	private JTextField textFieldBaseValue;// 基准值

	private JLabel labelShowPositiveStyle;
	private JCheckBox checkBoxShowZero;
	private JCheckBox checkBoxShowNegativeStyle;
	private JCheckBox checkBoxIsFlowEnabled;
	private JButton buttonShowPositiveStyle;// 正值基准值风格
	private JButton buttonZeroStyle;// 零值风格
	private JButton buttonNegativeStyle;// 负值风格

	private JLabel labelOffsetUnity;
	private JLabel labelOffsetX;
	private JLabel labelOffsetY;
	private JCheckBox checkBoxShowLeaderLine;
	private JLabel labelOffsetXUnity;
	private JLabel labelOffsetYUnity;
	private JComboBox<String> comboBoxOffsetUnity;// 偏移量单位
	private JComboBox<String> comboBoxOffsetX;// 水平偏移量
	private JComboBox<String> comboBoxOffsetY;// 垂直偏移量
	private JButton buttonLeaderLineStyle;// 牵引线风格

	private Layer themeGraduatedSymbolLayer;
	private ThemeGraduatedSymbol themeGraduatedSymbol;
	private DatasetVector datasetVector;
	private String layerName;
	private Map map;
	private boolean isRefreshAtOnce;
	private ArrayList<String> comboBoxArrayForOffsetX;
	private ArrayList<String> comboBoxArrayForOffsetY;
	private ArrayList<String> comboBoxArray;
	private GraduatedMode graduatedMode;
	private LayersTree layersTree = UICommonToolkit.getLayersManager().getLayersTree();

	private final int POSITIVESTYLE = 0;
	private final int ZEROSTYLE = 1;
	private final int NEGATIVESTYLE = 2;
	private final int LEADERLINESTYLE = 3;
	public boolean isResetLayerProperty = false;
	private String expression;

	private ItemListener expressionListener = new ExpressionListener();
	private ItemListener graduatedModeListener = new GraduatedModelListener();
	private FocusAdapter textFieldPropertyListener = new TextFieldPropertyListener();
	private ItemListener checkboxListener = new CheckboxItemListener();
	private ItemListener comboxListener = new ComboxBoxListener();
	private ActionListener buttonListener = new ButtonActionListener();
	private PropertyChangeListener layerPropertyChangeListener = new LayerPropertyChangeListener();
	private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			isResetLayerProperty = false;
		}
	};

	public ThemeGraduatedSymbolContainer(DatasetVector datasetVector, ThemeGraduatedSymbol themeGraduatedSymbol, Layer themeGraduatedLayer) {
		this.datasetVector = datasetVector;
		this.themeGraduatedSymbol = new ThemeGraduatedSymbol(themeGraduatedSymbol);
		this.map = initCurrentTheme(datasetVector, themeGraduatedLayer);
		initComponents();
		initResources();
		registActionListener();
	}

	public ThemeGraduatedSymbolContainer(Layer layer) {
		this.themeGraduatedSymbolLayer = layer;
		this.layerName = layer.getName();
		this.themeGraduatedSymbol = new ThemeGraduatedSymbol((ThemeGraduatedSymbol) layer.getTheme());
		this.map = ThemeGuideFactory.getMapControl().getMap();
		this.datasetVector = (DatasetVector) layer.getDataset();
		initComponents();
		initResources();
		registActionListener();
	}

	private Map initCurrentTheme(DatasetVector datasetVector, Layer themeGraduatedLayer) {
		MapControl mapControl = ThemeGuideFactory.getMapControl();
		if (null != mapControl) {
			this.themeGraduatedSymbolLayer = mapControl.getMap().getLayers().add(datasetVector, themeGraduatedSymbol, true);
			// 复制关联表信息到新图层中
			this.themeGraduatedSymbolLayer.setDisplayFilter(themeGraduatedLayer.getDisplayFilter());
			this.layerName = this.themeGraduatedSymbolLayer.getName();
			UICommonToolkit.getLayersManager().getLayersTree().setSelectionRow(0);
		}
		return mapControl.getMap();
	}

	private void initComponents() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		this.setLayout(gridBagLayout);
		this.tabbedPaneInfo = new JTabbedPane();
		this.panelProperty = new JPanel();
		this.tabbedPaneInfo.add(MapViewProperties.getString("String_Theme_Property"), this.panelProperty);
		this.add(tabbedPaneInfo, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
		initPanelProperty();
	}

	private void initPanelProperty() {
		this.labelExpression = new JLabel();
		this.labelGraduatedMode = new JLabel();
		this.labelBaseValue = new JLabel();
		this.comboBoxExpression = new JComboBox<String>();
		this.comboBoxGraduatedMode = new JComboBox<String>();
		this.textFieldBaseValue = new JTextField();
		this.comboBoxExpression.setPreferredSize(new Dimension(100, 23));
		this.comboBoxGraduatedMode.setPreferredSize(new Dimension(100, 23));
		this.textFieldBaseValue.setPreferredSize(new Dimension(100, 23));
		initTextFieldBaseValue();
		initComboBoxGraduatedMode();
		initComboboxExpression();
		initPanelSymbolStyle();
		initPanelOffset();
		//@formatter:off
		JPanel panelContent = new JPanel();
		panelContent.setLayout(new GridBagLayout());
		panelContent.add(labelExpression,      new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(40, 1).setIpad(20, 0));
		panelContent.add(comboBoxExpression,   new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		panelContent.add(labelGraduatedMode,   new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		panelContent.add(comboBoxGraduatedMode,new GridBagConstraintsHelper(1, 1, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		panelContent.add(labelBaseValue,       new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1).setIpad(20, 0));
		panelContent.add(textFieldBaseValue,   new GridBagConstraintsHelper(1, 2, 2, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		panelContent.add(panelSymbolStyle,     new GridBagConstraintsHelper(0, 3, 3, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(100,1).setFill(GridBagConstraints.HORIZONTAL));
		panelContent.add(panelOffset,          new GridBagConstraintsHelper(0, 4, 3, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(100,1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelProperty.setLayout(new GridBagLayout());
		this.panelProperty.add(panelContent,  new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.NORTH).setFill(GridBagConstraints.HORIZONTAL).setInsets(5, 10, 5, 10));
		//@formatter:on
	}

	private void initTextFieldBaseValue() {
		double baseValue = themeGraduatedSymbol.getBaseValue();
		this.textFieldBaseValue.setText(String.valueOf(baseValue));
		if (Double.compare(baseValue, 0.0) == 0) {
			this.textFieldBaseValue.setForeground(Color.red);
		} else {
			this.textFieldBaseValue.setForeground(Color.black);
		}
	}

	private void initComboBoxGraduatedMode() {
		this.comboBoxGraduatedMode.setModel(new DefaultComboBoxModel<String>(new String[] { MapViewProperties.getString("String_GraduatedMode_Constant"),
				MapViewProperties.getString("String_GraduatedMode_Logarithm"), MapViewProperties.getString("String_GraduatedMode_SquareRoot") }));
		this.graduatedMode = themeGraduatedSymbol.getGraduatedMode();
		if (graduatedMode.equals(GraduatedMode.CONSTANT)) {
			this.comboBoxGraduatedMode.setSelectedIndex(0);
			return;
		}
		if (graduatedMode.equals(GraduatedMode.LOGARITHM)) {
			this.comboBoxGraduatedMode.setSelectedIndex(1);
			return;
		}
		if (graduatedMode.equals(GraduatedMode.SQUAREROOT)) {
			this.comboBoxGraduatedMode.setSelectedIndex(2);
			return;
		}
	}

	private void initComboboxExpression() {
		this.comboBoxArray = new ArrayList<String>();
		ThemeUtil.initComboBox(comboBoxExpression, themeGraduatedSymbol.getExpression(), datasetVector, themeGraduatedSymbolLayer.getDisplayFilter()
				.getJoinItems(), comboBoxArray, true, false);
	}

	private void initPanelSymbolStyle() {
		this.panelSymbolStyle = new JPanel();
		this.labelShowPositiveStyle = new JLabel();
		this.checkBoxShowZero = new JCheckBox();
		this.checkBoxShowNegativeStyle = new JCheckBox();
		this.checkBoxIsFlowEnabled = new JCheckBox();
		this.buttonShowPositiveStyle = new JButton("...");
		this.buttonZeroStyle = new JButton("...");
		this.buttonNegativeStyle = new JButton("...");
		this.panelSymbolStyle.setLayout(new GridBagLayout());
		initPanelComponentState();
		//@formatter:off
		this.panelSymbolStyle.add(this.labelShowPositiveStyle,   new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(40, 1));
		this.panelSymbolStyle.add(this.buttonShowPositiveStyle,  new GridBagConstraintsHelper(1, 0, 3, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(60, 1).setIpad(80, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelSymbolStyle.add(this.checkBoxShowZero,         new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1));
		this.panelSymbolStyle.add(this.buttonZeroStyle,          new GridBagConstraintsHelper(1, 1, 3, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(60, 1).setIpad(80, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelSymbolStyle.add(this.checkBoxShowNegativeStyle,new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1));
		this.panelSymbolStyle.add(this.buttonNegativeStyle,      new GridBagConstraintsHelper(1, 2, 3, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(60, 1).setIpad(80, 0).setFill(GridBagConstraints.HORIZONTAL));
		this.panelSymbolStyle.add(this.checkBoxIsFlowEnabled,    new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1));
		//@formatter:on
	}

	private void initPanelComponentState() {
		boolean isZero = themeGraduatedSymbol.isZeroDisplayed();
		this.checkBoxShowZero.setSelected(isZero);
		this.buttonZeroStyle.setEnabled(isZero);
		boolean isNegativeStyle = themeGraduatedSymbol.isNegativeDisplayed();
		this.checkBoxShowNegativeStyle.setSelected(isNegativeStyle);
		this.buttonNegativeStyle.setEnabled(isNegativeStyle);
		boolean isFlowEnabled = themeGraduatedSymbol.isFlowEnabled();
		this.checkBoxIsFlowEnabled.setSelected(isFlowEnabled);
	}

	private void initPanelOffset() {
		this.panelOffset = new JPanel();
		this.labelOffsetUnity = new JLabel();
		this.labelOffsetX = new JLabel();
		this.labelOffsetXUnity = new JLabel();
		this.labelOffsetY = new JLabel();
		this.labelOffsetYUnity = new JLabel();
		this.checkBoxShowLeaderLine = new JCheckBox();
		this.comboBoxOffsetUnity = new JComboBox<String>();
		this.comboBoxOffsetX = new JComboBox<String>();
		this.comboBoxOffsetY = new JComboBox<String>();
		this.buttonLeaderLineStyle = new JButton("...");
		this.panelOffset.setLayout(new GridBagLayout());
		initComboBoxOffsetUnity();
		initComboBoxOffsetX();
		initComboBoxOffsetY();
		boolean isLeaderLineDisplayed = themeGraduatedSymbol.isLeaderLineDisplayed();
		this.checkBoxShowLeaderLine.setSelected(isLeaderLineDisplayed);
		this.buttonLeaderLineStyle.setEnabled(isLeaderLineDisplayed);
		this.comboBoxOffsetUnity.setPreferredSize(new Dimension(120, 23));
		Dimension textDimension = new Dimension(80, 23);
		this.comboBoxOffsetX.setPreferredSize(textDimension);
		this.comboBoxOffsetY.setPreferredSize(textDimension);
		//@formatter:off
		this.panelOffset.setLayout(new GridBagLayout());
		this.panelOffset.add(this.labelOffsetUnity,      new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(5,10,5,10).setWeight(40, 1));
		this.panelOffset.add(this.comboBoxOffsetUnity,   new GridBagConstraintsHelper(1, 0, 2, 1).setAnchor(GridBagConstraints.CENTER).setInsets(5,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelOffset.add(this.labelOffsetX,          new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1));
		this.panelOffset.add(this.comboBoxOffsetX,       new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(50, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelOffset.add(this.labelOffsetXUnity,     new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(10, 1));
		this.panelOffset.add(this.labelOffsetY,          new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1));
		this.panelOffset.add(this.comboBoxOffsetY,       new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(50, 1).setFill(GridBagConstraints.HORIZONTAL));
		this.panelOffset.add(this.labelOffsetYUnity,     new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(10, 1));
		this.panelOffset.add(this.checkBoxShowLeaderLine,new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.WEST).setInsets(0,10,5,10).setWeight(40, 1));
		this.panelOffset.add(this.buttonLeaderLineStyle, new GridBagConstraintsHelper(1, 3, 2, 1).setAnchor(GridBagConstraints.CENTER).setInsets(0,10,5,10).setWeight(60, 1).setFill(GridBagConstraints.HORIZONTAL));
		//@formatter:on
	}

	/**
	 * 初始化偏移量单位
	 */
	private void initComboBoxOffsetUnity() {
		this.comboBoxOffsetUnity.setModel(new DefaultComboBoxModel<String>(new String[] {
				MapViewProperties.getString("String_MapBorderLineStyle_LabelDistanceUnit"), MapViewProperties.getString("String_ThemeLabelOffsetUnit_Map") }));
		if (this.themeGraduatedSymbol.isOffsetFixed()) {
			this.comboBoxOffsetUnity.setSelectedIndex(0);
		} else {
			this.comboBoxOffsetUnity.setSelectedIndex(1);
			this.labelOffsetXUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
			this.labelOffsetYUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
		}
	}

	/**
	 * 初始化水平偏移量
	 */
	private void initComboBoxOffsetX() {
		this.comboBoxArrayForOffsetX = new ArrayList<String>();
		ThemeUtil.initComboBox(comboBoxOffsetX, themeGraduatedSymbol.getOffsetX(), datasetVector, themeGraduatedSymbolLayer.getDisplayFilter().getJoinItems(),
				comboBoxArrayForOffsetX, true, true);
	}

	/**
	 * 初始化垂直偏移量
	 */
	private void initComboBoxOffsetY() {
		this.comboBoxArrayForOffsetY = new ArrayList<String>();
		ThemeUtil.initComboBox(comboBoxOffsetY, themeGraduatedSymbol.getOffsetY(), datasetVector, themeGraduatedSymbolLayer.getDisplayFilter().getJoinItems(),
				comboBoxArrayForOffsetY, true, true);
	}

	private void initResources() {
		this.labelExpression.setText(MapViewProperties.getString("String_Label_Expression"));
		this.labelGraduatedMode.setText(MapViewProperties.getString("String_ThemeGraduatedSymbolProperty_LabelStatisticMode"));
		this.labelBaseValue.setText(MapViewProperties.getString("String_ThemeGraduatedSymbolProperty_LabelBaseValue"));
		this.labelShowPositiveStyle.setText(MapViewProperties.getString("String_ThemeGraduatedSymbolProperty_LabelPositive"));
		this.checkBoxShowZero.setText(MapViewProperties.getString("String_ThemeGraduatedSymbolProperty_CheckBoxZero"));
		this.checkBoxShowNegativeStyle.setText(MapViewProperties.getString("String_ThemeGraduatedSymbolProperty_CheckBoxNegative"));
		this.checkBoxIsFlowEnabled.setText(MapViewProperties.getString("String_CheckBox_ShowFlow"));
		this.labelOffsetUnity.setText(MapViewProperties.getString("String_LabelOffsetUnit"));
		this.labelOffsetX.setText(MapViewProperties.getString("String_LabelOffsetX"));
		this.labelOffsetY.setText(MapViewProperties.getString("String_LabelOffsetY"));
		this.checkBoxShowLeaderLine.setText(MapViewProperties.getString("String_ShowLeaderLine"));
		this.buttonLeaderLineStyle.setText(MapViewProperties.getString("String_Button_LineStyle"));
		this.panelSymbolStyle.setBorder(new TitledBorder(MapViewProperties.getString("String_ThemeGraduatedSymbolProperty_GroupBoxSymbolStyle")));
		this.panelOffset.setBorder(new TitledBorder(MapViewProperties.getString("String_GroupBoxOffset")));
	}

	@Override
	public Theme getCurrentTheme() {
		return this.themeGraduatedSymbol;
	}

	@Override
	public Layer getCurrentLayer() {
		return this.themeGraduatedSymbolLayer;
	}

	@Override
	public void setCurrentLayer(Layer layer) {
		this.themeGraduatedSymbolLayer = layer;
	}

	@Override
	public void setRefreshAtOnce(boolean isRefreshAtOnce) {
		this.isRefreshAtOnce = isRefreshAtOnce;
	}

	@Override
	public void registActionListener() {
		this.comboBoxExpression.addItemListener(this.expressionListener);
		this.comboBoxGraduatedMode.addItemListener(this.graduatedModeListener);
		this.textFieldBaseValue.addFocusListener(this.textFieldPropertyListener);
		this.checkBoxShowZero.addItemListener(this.checkboxListener);
		this.checkBoxShowNegativeStyle.addItemListener(this.checkboxListener);
		this.checkBoxIsFlowEnabled.addItemListener(this.checkboxListener);
		this.checkBoxShowLeaderLine.addItemListener(this.checkboxListener);
		this.comboBoxOffsetUnity.addItemListener(this.comboxListener);
		this.comboBoxOffsetX.addItemListener(this.comboxListener);
		this.comboBoxOffsetY.addItemListener(this.comboxListener);
		this.buttonShowPositiveStyle.addActionListener(this.buttonListener);
		this.buttonZeroStyle.addActionListener(this.buttonListener);
		this.buttonNegativeStyle.addActionListener(this.buttonListener);
		this.buttonLeaderLineStyle.addActionListener(this.buttonListener);
		this.comboBoxExpression.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxOffsetX.getComponent(0).addMouseListener(this.mouseAdapter);
		this.comboBoxOffsetY.getComponent(0).addMouseListener(this.mouseAdapter);
		this.layersTree.addPropertyChangeListener("LayerPropertyChanged", this.layerPropertyChangeListener);
	}

	@Override
	public void unregistActionListener() {
		this.comboBoxExpression.removeItemListener(this.expressionListener);
		this.comboBoxGraduatedMode.removeItemListener(this.graduatedModeListener);
		this.textFieldBaseValue.removeFocusListener(this.textFieldPropertyListener);
		this.checkBoxShowZero.removeItemListener(this.checkboxListener);
		this.checkBoxShowNegativeStyle.removeItemListener(this.checkboxListener);
		this.checkBoxIsFlowEnabled.removeItemListener(this.checkboxListener);
		this.checkBoxShowLeaderLine.removeItemListener(this.checkboxListener);
		this.comboBoxOffsetUnity.removeItemListener(this.comboxListener);
		this.comboBoxOffsetX.removeItemListener(this.comboxListener);
		this.comboBoxOffsetY.removeItemListener(this.comboxListener);
		this.buttonShowPositiveStyle.removeActionListener(this.buttonListener);
		this.buttonZeroStyle.removeActionListener(this.buttonListener);
		this.buttonNegativeStyle.removeActionListener(this.buttonListener);
		this.buttonLeaderLineStyle.removeActionListener(this.buttonListener);
		this.comboBoxExpression.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxOffsetX.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.comboBoxOffsetY.getComponent(0).removeMouseListener(this.mouseAdapter);
		this.layersTree.removePropertyChangeListener("LayerPropertyChanged", this.layerPropertyChangeListener);
	}

	class ButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonShowPositiveStyle) {
				resetThemeGraduatedStyle(POSITIVESTYLE);
			}
			if (e.getSource() == buttonZeroStyle) {
				resetThemeGraduatedStyle(ZEROSTYLE);
			}
			if (e.getSource() == buttonNegativeStyle) {
				resetThemeGraduatedStyle(NEGATIVESTYLE);
			}
			if (e.getSource() == buttonLeaderLineStyle) {
				resetThemeGraduatedStyle(LEADERLINESTYLE);
			}
		}

	}

	private void resetThemeGraduatedStyle(final int type) {
		SymbolDialog symbolStyle = SymbolDialogFactory.getSymbolDialog(SymbolType.MARKER);
		if (LEADERLINESTYLE == type) {
			symbolStyle = SymbolDialogFactory.getSymbolDialog(SymbolType.LINE);
		}
		GeoStyle geoStyle = getThemeStyle(type);
		ISymbolApply symbolApply = new ISymbolApply() {
			@Override
			public void apply(GeoStyle geoStyle) {
				resetThemeGraduatedStyles(type, geoStyle);
				refreshAtOnce();
				return;
			}
		};
		DialogResult dialogResult;
		if (null != geoStyle) {
			dialogResult = symbolStyle.showDialog(geoStyle, symbolApply);
		} else {
			dialogResult = symbolStyle.showDialog(new GeoStyle(), symbolApply);
		}
		if (DialogResult.OK.equals(dialogResult)) {
			GeoStyle nowGeoStyle = symbolStyle.getCurrentGeoStyle();
			resetThemeGraduatedStyles(type, nowGeoStyle);
			refreshAtOnce();
			symbolStyle = null;
			return;
		}
	}

	private GeoStyle getThemeStyle(int type) {
		GeoStyle geoStyle = null;
		if (type == POSITIVESTYLE) {
			geoStyle = themeGraduatedSymbol.getPositiveStyle();
		}
		if (type == ZEROSTYLE) {
			geoStyle = themeGraduatedSymbol.getZeroStyle();
		}
		if (type == NEGATIVESTYLE) {
			geoStyle = themeGraduatedSymbol.getNegativeStyle();
		}
		if (type == LEADERLINESTYLE) {
			geoStyle = themeGraduatedSymbol.getLeaderLineStyle();
		}
		return geoStyle;
	}

	private void resetThemeGraduatedStyles(int type, GeoStyle geoStyle) {
		switch (type) {
		case POSITIVESTYLE:
			// 设置正基准值风格
			themeGraduatedSymbol.setPositiveStyle(geoStyle);
			break;
		case ZEROSTYLE:
			// 设置零值风格
			themeGraduatedSymbol.setZeroStyle(geoStyle);
			break;
		case NEGATIVESTYLE:
			// 设置负值风格
			themeGraduatedSymbol.setNegativeStyle(geoStyle);
			break;
		case LEADERLINESTYLE:
			// 设置牵引线风格
			themeGraduatedSymbol.setLeaderLineStyle(geoStyle);
			break;
		default:
			break;
		}
	}

	class ComboxBoxListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			Dataset[] tempDatasets = ThemeUtil.getDatasets(themeGraduatedSymbolLayer, datasetVector);
			if (isResetLayerProperty) {
				return;
			}
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == comboBoxOffsetUnity) {
					int unity = comboBoxOffsetUnity.getSelectedIndex();
					switch (unity) {
					case 0:
						themeGraduatedSymbol.setOffsetFixed(true);
						labelOffsetXUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
						labelOffsetYUnity.setText(MapViewProperties.getString("String_Combobox_MM"));
						refreshAtOnce();
						break;
					case 1:
						themeGraduatedSymbol.setOffsetFixed(false);
						labelOffsetXUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
						labelOffsetYUnity.setText(UnitValue.parseToString(map.getCoordUnit()));
						refreshAtOnce();
						break;
					default:
						break;
					}
				}
				if (e.getSource() == comboBoxOffsetX) {
					String offsetXExpression = themeGraduatedSymbol.getOffsetX();
					if (StringUtilties.isNullOrEmpty(offsetXExpression)) {
						offsetXExpression = "0";
					}
					boolean itemChangedForOffsetX = ThemeUtil.getSqlExpression(comboBoxOffsetX, tempDatasets, comboBoxArrayForOffsetX, offsetXExpression, true);
					if (itemChangedForOffsetX) {
						// 修改水平偏移量
						setOffsetX();
					}
				}
				if (e.getSource() == comboBoxOffsetY) {
					String offsetYExpression = themeGraduatedSymbol.getOffsetY();
					if (StringUtilties.isNullOrEmpty(offsetYExpression)) {
						offsetYExpression = "0";
					}
					boolean itemChangedForOffsetX = ThemeUtil.getSqlExpression(comboBoxOffsetY, tempDatasets, comboBoxArrayForOffsetY, offsetYExpression, true);
					if (itemChangedForOffsetX) {
						// 修改水平偏移量
						setOffsetY();
					}
				}
			}
		}

		private void setOffsetX() {
			String offsetX = comboBoxOffsetX.getSelectedItem().toString();
			themeGraduatedSymbol.setOffsetX(offsetX);
			refreshAtOnce();
			return;
		}

		private void setOffsetY() {
			String offsetY = comboBoxOffsetY.getSelectedItem().toString();
			themeGraduatedSymbol.setOffsetY(offsetY);
			refreshAtOnce();
			return;
		}
	}

	class CheckboxItemListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == checkBoxShowZero) {
				themeGraduatedSymbol.setZeroDisplayed(checkBoxShowZero.isSelected());
				buttonZeroStyle.setEnabled(checkBoxShowZero.isSelected());
				refreshAtOnce();
				return;
			}
			if (e.getSource() == checkBoxShowNegativeStyle) {
				themeGraduatedSymbol.setNegativeDisplayed(checkBoxShowNegativeStyle.isSelected());
				buttonNegativeStyle.setEnabled(checkBoxShowNegativeStyle.isSelected());
				refreshAtOnce();
				return;
			}
			if (e.getSource() == checkBoxIsFlowEnabled) {
				themeGraduatedSymbol.setFlowEnabled(checkBoxIsFlowEnabled.isSelected());
				refreshAtOnce();
				return;
			}
			if (e.getSource() == checkBoxShowLeaderLine) {
				themeGraduatedSymbol.setLeaderLineDisplayed(checkBoxShowLeaderLine.isSelected());
				buttonLeaderLineStyle.setEnabled(checkBoxShowLeaderLine.isSelected());
				refreshAtOnce();
				return;
			}
		}

	}

	class TextFieldPropertyListener extends FocusAdapter {

		@Override
		public void focusLost(FocusEvent e) {
			resetBaseValue();
		}

		private void resetBaseValue() {
			String strBaseValue = textFieldBaseValue.getText();
			if (!StringUtilties.isNullOrEmpty(strBaseValue) && StringUtilties.isNumber(strBaseValue)) {
				double newBaseValue = Double.parseDouble(strBaseValue);

				if (Double.compare(newBaseValue, 0.0) == 0) {
					textFieldBaseValue.setForeground(Color.red);
				} else {
					textFieldBaseValue.setForeground(Color.black);
				}
				if (Double.compare(newBaseValue, 0.0) < 0) {
					textFieldBaseValue.setText(String.valueOf(Math.abs(newBaseValue)));
				}
				themeGraduatedSymbol.setBaseValue(Math.abs(newBaseValue));
				refreshAtOnce();
				return;
			}
			if (!StringUtilties.isNullOrEmpty(strBaseValue) && !StringUtilties.isNumber(strBaseValue)) {
				textFieldBaseValue.setForeground(Color.red);
				return;
			}
		}
	}

	class GraduatedModelListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				int mode = comboBoxGraduatedMode.getSelectedIndex();
				switch (mode) {
				case 0:
					graduatedMode = GraduatedMode.CONSTANT;
					resetGraduatedMode();
					break;
				case 1:
					graduatedMode = GraduatedMode.LOGARITHM;
					resetGraduatedMode();
					break;
				case 2:
					graduatedMode = GraduatedMode.SQUAREROOT;
					resetGraduatedMode();
					break;
				default:
					break;
				}
			}
		}

		private void resetGraduatedMode() {
			String tempExpression = comboBoxExpression.getSelectedItem().toString();
			DatasetVector dataset = datasetVector;
			if (tempExpression.contains(".") && tempExpression.split("\\.").length == 2) {
				dataset = (DatasetVector) datasetVector.getDatasource().getDatasets().get(tempExpression.substring(0, tempExpression.indexOf(".")));
			}
			ThemeGraduatedSymbol tempTheme = ThemeGraduatedSymbol.makeDefault(dataset, tempExpression, graduatedMode);
			if (null != tempTheme) {
				copyThemeInfo(tempTheme, themeGraduatedSymbol);
				themeGraduatedSymbol.fromXML(tempTheme.toXML());
				textFieldBaseValue.setText(String.valueOf(themeGraduatedSymbol.getBaseValue()));
				refreshAtOnce();
				tempTheme.dispose();
			} else {
				UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
				resetThemeItem();
			}
		}

	}

	private void copyThemeInfo(ThemeGraduatedSymbol target, ThemeGraduatedSymbol resource) {
		target.setPositiveStyle(resource.getPositiveStyle());
		target.setZeroDisplayed(resource.isZeroDisplayed());
		target.setZeroStyle(resource.getZeroStyle());
		target.setNegativeDisplayed(resource.isNegativeDisplayed());
		target.setNegativeStyle(resource.getNegativeStyle());
		target.setFlowEnabled(resource.isFlowEnabled());
		target.setOffsetFixed(resource.isOffsetFixed());
		target.setOffsetX(resource.getOffsetX());
		target.setOffsetY(resource.getOffsetY());
		target.setLeaderLineDisplayed(resource.isLeaderLineDisplayed());
		target.setLeaderLineStyle(resource.getLeaderLineStyle());
	}

	class ExpressionListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			Dataset[] datasets = ThemeUtil.getDatasets(themeGraduatedSymbolLayer, datasetVector);
			if (isResetLayerProperty) {
				return;
			}
			if (e.getStateChange() == ItemEvent.SELECTED) {
				// sql表达式
				expression = themeGraduatedSymbol.getExpression();
				if (!comboBoxArray.contains(expression)) {
					expression = expression.substring(expression.indexOf(".") + 1, expression.length());
				}
				boolean itemHasChanged = ThemeUtil.getSqlExpression(comboBoxExpression, datasets, comboBoxArray, expression, true);
				// 修改表达式
				if (itemHasChanged) {
					// 如果sql表达式中修改了选项
					expression = comboBoxExpression.getSelectedItem().toString();
					DatasetVector dataset = datasetVector;
					if (expression.contains(".") && expression.split("\\.").length == 2) {
						dataset = (DatasetVector) datasetVector.getDatasource().getDatasets().get(expression.substring(0, expression.indexOf(".")));
					}
					ThemeGraduatedSymbol tempTheme = ThemeGraduatedSymbol.makeDefault(dataset, expression, graduatedMode);
					if (null != tempTheme) {
						copyThemeInfo(tempTheme, themeGraduatedSymbol);
						themeGraduatedSymbol.fromXML(tempTheme.toXML());
						textFieldBaseValue.setText(String.valueOf(themeGraduatedSymbol.getBaseValue()));
						refreshAtOnce();
						tempTheme.dispose();
					} else {
						UICommonToolkit.showMessageDialog(MapViewProperties.getString("String_Theme_UpdataFailed"));
						resetThemeItem();
					}
				}
			}
		}

	}

	private void resetThemeItem() {
		String tempExpression = themeGraduatedSymbol.getExpression();
		if (comboBoxArray.contains(tempExpression)) {
			comboBoxExpression.setSelectedItem(tempExpression);
		} else {
			comboBoxExpression.setSelectedItem(tempExpression.substring(tempExpression.indexOf(".") + 1, tempExpression.length()));
		}
	}

	class LayerPropertyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent e) {
			if (null != themeGraduatedSymbolLayer && !themeGraduatedSymbolLayer.isDisposed() && ((Layer) e.getNewValue()).equals(themeGraduatedSymbolLayer)) {
				isResetLayerProperty = true;
				initComboboxExpression();
				initComboBoxOffsetX();
				initComboBoxOffsetY();
			}
		}

	}

	private void refreshAtOnce() {
		firePropertyChange("ThemeChange", null, null);
		if (isRefreshAtOnce) {
			refreshMapAndLayer();
		}
	}

	@Override
	public void refreshMapAndLayer() {
		if (null != ThemeGuideFactory.getMapControl()) {
			this.map = ThemeGuideFactory.getMapControl().getMap();
		}
		this.themeGraduatedSymbolLayer = MapUtilties.findLayerByName(map, layerName);
		if (null != themeGraduatedSymbolLayer && null != themeGraduatedSymbolLayer.getTheme()
				&& themeGraduatedSymbolLayer.getTheme().getType() == ThemeType.GRADUATEDSYMBOL) {
			ThemeGraduatedSymbol nowThemeGraduatedSymbol = ((ThemeGraduatedSymbol) this.themeGraduatedSymbolLayer.getTheme());
			nowThemeGraduatedSymbol.fromXML(themeGraduatedSymbol.toXML());
			this.map.refresh();
		}
	}
}
