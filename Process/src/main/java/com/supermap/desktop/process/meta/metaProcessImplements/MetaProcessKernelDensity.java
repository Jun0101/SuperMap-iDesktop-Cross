package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.desktop.lbs.params.KernelDensityJobSetting;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.implement.ParameterTextField;
import com.supermap.desktop.process.parameter.interfaces.IParameters;

import javax.swing.*;

/**
 * Created by xie on 2017/2/10.
 */
public class MetaProcessKernelDensity extends MetaProcess {
	private IParameters parameters;

	ParameterTextField parameterFileInputPath;
	ParameterTextField parameterBounds;
	ParameterTextField parameterIndex;
	ParameterTextField parameterSeperator;
	ParameterTextField parameterResolution;
	ParameterTextField parameterRadius;


	public MetaProcessKernelDensity() {
		initMetaInfo();
	}

	private void initMetaInfo() {
		parameters = new DefaultParameters();
		//TODO 封装数据管理调用控件，此处先用ParameterTextField控件替换
		parameterFileInputPath = new ParameterTextField().setDescribe("");
		parameterFileInputPath.setSelectedItem("hdfs://localhost:9000/data/newyork_taxi_2013-01_147k.csv");
		//流程图中不支持在地图中绘制范围，范围表示与iServer的表示相同
		parameterBounds = new ParameterTextField().setDescribe(ProcessProperties.getString("String_AnalystBounds"));
		parameterBounds.setSelectedItem("-74.050,40.550,-73.750,40.950");
		parameterIndex = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Index"));
		parameterIndex.setSelectedItem("10");
		parameterSeperator = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Seperator"));
		parameterSeperator.setSelectedItem(",");
		parameterResolution = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Resolution"));
		parameterResolution.setSelectedItem("0.004");
		parameterRadius = new ParameterTextField().setDescribe(ProcessProperties.getString("String_Radius"));
		parameterRadius.setSelectedItem("0.004");
		parameters.setParameters(
				parameterFileInputPath,
				parameterBounds,
				parameterIndex,
				parameterSeperator,
				parameterResolution,
				parameterRadius
		);
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_KernelDensityAnalyst");
	}

	@Override
	public JComponent getComponent() {
		return this.parameters.getPanel();
	}

	@Override
	public void run() {
		//核密度分析功能实现
		KernelDensityJobSetting kenelDensityJobSetting = new KernelDensityJobSetting();
		kenelDensityJobSetting.analyst.query = parameterBounds.getSelectedItem().toString();
		kenelDensityJobSetting.analyst.geoidx = parameterIndex.getSelectedItem().toString();
		kenelDensityJobSetting.analyst.separator = parameterSeperator.getSelectedItem().toString();
		kenelDensityJobSetting.analyst.resolution = parameterResolution.getSelectedItem().toString();
		kenelDensityJobSetting.analyst.radius = parameterRadius.getSelectedItem().toString();
		kenelDensityJobSetting.input.filePath = parameterFileInputPath.getSelectedItem().toString();
//        IServerService service = new IServerServiceImpl();
//        CursorUtilities.setWaitCursor();
//        JobResultResponse response = service.query(kenelDensityJobSetting);
//        if (null != response) {
//            CursorUtilities.setDefaultCursor();
//            NewMessageBus.producer(response);
//        }
	}
}