package com.supermap.desktop.ui.controls.comboBox;

import com.supermap.data.*;
import com.supermap.desktop.controls.utilities.DatasetUIUtilities;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SQLExpressionDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * Created by hanyz on 2017/2/15.
 */
public class SmFieldInfoComboBox extends JComboBox {
    private ComboBoxListener comboBoxListener = new ComboBoxListener();
    private DatasetVector dataset;
    private JoinItems joinItems;
    private ArrayList<DatasetFieldInfo> datasetFieldInfos;
    private String[] additionalItems;
    private Object selectedValue;

    //用于构建SQL表达式面板的字段类型，默认null表示所有字段
    protected ArrayList<FieldType> getFieldIntoTypes() {
        return null;
    }

    public Object getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(Object selectedValue) {
        this.selectedValue = selectedValue;
    }

    /**
     * 用数据集提供可选字段
     *
     * @param dataset
     */
    public SmFieldInfoComboBox(DatasetVector dataset) {
        super();
        this.dataset = dataset;
        init();
    }

    /**
     * 用数据集和关联表提供可选字段
     *
     * @param dataset
     * @param joinItems
     */
    public SmFieldInfoComboBox(DatasetVector dataset, JoinItems joinItems) {
        super();
        this.dataset = dataset;
        this.joinItems = joinItems;
        init();
    }


    /**
     * 自定义字符数组作为字段选择项
     *
     * @param fieldInfos
     */
    public SmFieldInfoComboBox(String[] fieldInfos) {
        super();
        this.additionalItems = fieldInfos;
        init();
    }

    /**
     * 添加除了字段意外的选项，如“0”
     */
    public void setAdditionalItems(String[] additionalItems) {
        if (this.additionalItems != null && this.additionalItems.length > 0) {
            for (String additionalItem : this.additionalItems) {
                if (getItemIndex(additionalItem) > -1) {
                    this.removeItemAt(getItemIndex(additionalItem));
                }
            }
        }
        this.additionalItems = additionalItems;
        if (this.additionalItems != null && this.additionalItems.length > 0) {
            for (String additionalItem : this.additionalItems) {
                if (getItemIndex(additionalItem) == -1) {
                    this.addItem(additionalItem);
                }
            }
        }
    }

    @Override
    public void setSelectedItem(Object anObject) {
        int itemIndex = this.getItemIndex(anObject);
        if (itemIndex > -1) {
            super.setSelectedItem(this.getItemAt(itemIndex));
            this.setSelectedValue(anObject);
            return;
        }
        super.setSelectedItem(anObject.toString());
        this.setSelectedValue(anObject);
    }

    @Override
    public void setSelectedIndex(int anIndex) {
        try {
            super.setSelectedIndex(anIndex);
        } catch (Exception e) {
//            e.printStackTrace();
            return;
        }
        this.setSelectedValue(this.getItemIndex(anIndex));
    }

    public void init() {
        initComponent();
        registerListener();
        setSelectedValue(getSelectedItem());
    }

    private void initComponent() {
        this.setPreferredSize(new Dimension(200, 120));
        createComboBoxField();
    }

    /**
     * 数据集字段信息类：绑定数据集和字段信息，关联表能明确字段归属
     */
    private JComboBox<Object> createComboBoxField() {
        this.setEditable(true);
        //添加数据集数值字段
        initFieldInfo();
        for (DatasetFieldInfo datasetFieldInfo : datasetFieldInfos) {
            this.addItem(datasetFieldInfo);
        }
        //添加额外的字符串，如“0”
        if (additionalItems != null && additionalItems.length > 0) {
            for (String additionalItem : additionalItems) {
                if (getItemIndex(additionalItem) == -1) {
                    this.addItem(additionalItem);
                }
            }
        }
        this.setAdditionalItems(additionalItems);
        //添加“表达式...”
        String SQLExpressionText = CoreProperties.getString("String_ThemeGraphItemExpressionPicker_ButtonExpression");
        if (getItemIndex(SQLExpressionText) == -1) {
            this.addItem(SQLExpressionText);
        }
        this.setRenderer(new DatasetFieldInfoRender());
        return this;
    }

    private void initFieldInfo() {
        if (this.dataset == null)
            return;
        //保留定义好的datasetFieldInfos列表
        if (datasetFieldInfos != null && datasetFieldInfos.size() > 0)
            return;
        //从数据集中获取数值字段
        datasetFieldInfos = new ArrayList<>();
        FieldInfos fieldInfos = dataset.getFieldInfos();
        for (int i = 0; i < fieldInfos.getCount(); i++) {
            FieldInfo fieldInfo = fieldInfos.get(i);
            if (isLegalField(fieldInfo)) {
                datasetFieldInfos.add(new DatasetFieldInfo(dataset, fieldInfo, true));
            }
        }
        //获取关联表FieldInfos
        if (joinItems != null) {
            for (int i = 0; i < joinItems.getCount(); i++) {
                DatasetVector joinDataset = (DatasetVector) dataset.getDatasource().getDatasets().get(joinItems.get(i).getForeignTable());
                fieldInfos = joinDataset.getFieldInfos();
                for (int j = 0; j < fieldInfos.getCount(); j++) {
                    FieldInfo fieldInfo = fieldInfos.get(j);
                    if (isLegalField(fieldInfo)) {
                        datasetFieldInfos.add(new DatasetFieldInfo(joinDataset, fieldInfo));
                    }
                }
            }
        }
    }

    protected void registerListener() {
        this.addItemListener(this.comboBoxListener);
    }

    protected void unRegisterListener() {
        this.removeItemListener(this.comboBoxListener);
    }

    class ComboBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object item = e.getItem();
                //选中的是FieldInfo，直接设置为选中项
                if (item instanceof DatasetFieldInfo) {
                    setSelectedItem(item);
                    setSelectedValue(item);
                    return;
                }
                //选中或输入字符串
                if (item instanceof String) {
                    //表达式...
                    if (item.equals(CoreProperties.getString("String_ThemeGraphItemExpressionPicker_ButtonExpression"))) {
                        SQLExpressionDialog sqlDialog = new SQLExpressionDialog();
                        Dataset[] datasets;
                        if (joinItems != null && joinItems.getCount() > 0) {
                            datasets = new Dataset[joinItems.getCount() + 1];
                            datasets[0] = dataset;
                            for (int i = 0; i < joinItems.getCount(); i++) {
                                String foreignTable = joinItems.get(i).getForeignTable();
                                datasets[i + 1] = DatasetUIUtilities.getDatasetFromDatasource(foreignTable, dataset.getDatasource());
                            }
                        } else {
                            datasets = new Dataset[1];
                            datasets[0] = dataset;
                        }
                        DialogResult dialogResult;
                        if (getFieldIntoTypes() == null) {
                            dialogResult = sqlDialog.showDialog(getSelectedValue().toString(), datasets);
                        } else {
                            dialogResult = sqlDialog.showDialog(datasets, getFieldIntoTypes(), getSelectedValue().toString());
                        }
                        if (dialogResult == DialogResult.OK) {
                            item = sqlDialog.getQueryParameter().getAttributeFilter();
                            item = ((String) item).trim();
                        } else {
                            Object selectedValue = getSelectedValue();
                            setSelectedItem(getSelectedValue());
                            setSelectedValue(selectedValue);
                            return;
                        }
                    }
                    //属于comboBox的字符串直接选择
                    int itemIndex = getItemIndex(item);
                    if (itemIndex > -1) {
                        setSelectedItem(getItemAt(itemIndex));
                        setSelectedValue(getItemAt(itemIndex));
                    } else {
                        setSelectedItem(item);
                        setSelectedValue(item);
                    }
                }
                //item为其他类型对象：主动调用setSelectItem设置的
                else {
                    setSelectedItem(item);
                    setSelectedValue(item);
                }

            }
        }
    }

    public class DatasetFieldInfoRender implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel jLabel = new JLabel();
            jLabel.setOpaque(true);
            if (isSelected) {
                jLabel.setBackground(list.getSelectionBackground());
                jLabel.setForeground(list.getSelectionForeground());
            } else {
                jLabel.setBackground(list.getBackground());
                jLabel.setForeground(list.getForeground());
            }

            if (value != null) {
                //DatasetFieldInfo选项，关联表字段：表名.Caption；本表：Caption
                if (value instanceof DatasetFieldInfo) {
                    String valueText;
                    DatasetFieldInfo datasetFieldInfo = (DatasetFieldInfo) value;
                    String datasetName = datasetFieldInfo.getDataset() == null ? "" : datasetFieldInfo.getDataset().getName();
                    String FieldCaption = datasetFieldInfo.getFieldInfo() == null ? "" : datasetFieldInfo.getFieldInfo().getCaption();
                    if (datasetFieldInfo.getDataset() != null && datasetFieldInfo.getDataset().equals(dataset)) {
                        valueText = datasetFieldInfo.getFieldInfo().getCaption();
                    } else {
                        valueText = datasetName + "." + FieldCaption;
                    }
                    jLabel.setText(valueText);
                }
                //字符串选项直接作为标签
                if (value instanceof String) {
                    jLabel.setText((String) value);
                }
            }
            return jLabel;
        }
    }

    /**
     * 返回合法字段，先过滤二进制字段吧
     */
    protected boolean isLegalField(FieldInfo fieldInfo) {
        return !fieldInfo.getType().equals(FieldType.LONGBINARY);
    }

    /**
     * 获取给定对象是否已经存在于列表中。输入字符串如果与选项字符串相同，直接选中选项。
     */
    private int getItemIndex(Object item) {
        for (int i = 0; i < this.getItemCount(); i++) {
            if (this.getItemAt(i) != null && this.getItemAt(i).equals(item)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回FieldInfo或String
     */
    public Object getSelectedFieldInfo() {
        Object selectedItem = this.getSelectedItem();
        if (selectedItem instanceof DatasetFieldInfo) {
            DatasetFieldInfo datasetFieldInfo = (DatasetFieldInfo) selectedItem;
            return datasetFieldInfo.getFieldInfo();
        }
        return selectedItem;
    }

    public static void main(String[] args) {
        DatasourceConnectionInfo connectionInfo = new DatasourceConnectionInfo();
        connectionInfo.setServer("C:\\Users\\hanyz\\Desktop\\2.udb");
        connectionInfo.setEngineType(EngineType.UDB);
        Datasource datasoure = new Datasource(EngineType.UDB);
        datasoure.open(connectionInfo);
        System.out.println(datasoure.isConnected());
        final DatasetVector dataset = (DatasetVector) datasoure.getDatasets().get("BaseMap_R");

        JFrame jFrame = new JFrame();
        jFrame.setSize(new Dimension(300, 120));
        jFrame.setLocationRelativeTo(null);
        JButton jButton = new JButton("Action");
        final SmFieldInfoComboBox smNumericFieldComboBox = new SmFieldInfoComboBox(dataset);
        smNumericFieldComboBox.setAdditionalItems(new String[]{"0", "1"});
        smNumericFieldComboBox.setAdditionalItems(new String[]{"1"});
        smNumericFieldComboBox.setMaximumSize(new Dimension(80, jButton.getPreferredSize().height));
        Container contentPane = jFrame.getContentPane();
        GroupLayout groupLayout = new GroupLayout(contentPane);
        jFrame.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();
        hGroup.addComponent(smNumericFieldComboBox).addComponent(jButton);
        groupLayout.setHorizontalGroup(hGroup);
        GroupLayout.ParallelGroup vGroup = groupLayout.createParallelGroup();
        vGroup.addComponent(smNumericFieldComboBox).addComponent(jButton);
        groupLayout.setVerticalGroup(vGroup);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FieldInfo fieldInfo = dataset.getFieldInfos().get(3);
                // todo comboBox选中fieldInfo
                smNumericFieldComboBox.setSelectedItem(fieldInfo);
                // todo 获取当前选中fieldInfo
                System.out.println(smNumericFieldComboBox.getSelectedIndex());
                System.out.println("selectItem Class:" + smNumericFieldComboBox.getSelectedItem().getClass());
                System.out.println("selectItem:" + smNumericFieldComboBox.getSelectedItem());
                System.out.println("selectedField Class:" + smNumericFieldComboBox.getSelectedFieldInfo().getClass());
                System.out.println("selectedField String:" + smNumericFieldComboBox.getSelectedFieldInfo());

            }
        });
        jFrame.setVisible(true);


    }


}
