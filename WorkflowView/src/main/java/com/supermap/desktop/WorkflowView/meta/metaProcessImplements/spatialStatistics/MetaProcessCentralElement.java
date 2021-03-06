package com.supermap.desktop.WorkflowView.meta.metaProcessImplements.spatialStatistics;

import com.supermap.analyst.spatialstatistics.SpatialMeasure;
import com.supermap.data.DatasetVector;
import com.supermap.desktop.Application;
import com.supermap.desktop.WorkflowView.ProcessOutputResultProperties;
import com.supermap.desktop.WorkflowView.meta.MetaKeys;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.utilities.DatasetUtilities;

/**
 * @author XiaJT
 */
public class MetaProcessCentralElement extends MetaProcessSpatialMeasure {

	public MetaProcessCentralElement() {
		super();
	}

	protected void initHook() {
		OUTPUT_DATASET = "CentralElementResult";
		resultName = "result_centralElement";
		outputName = ProcessOutputResultProperties.getString("String_Result_CentralElement");
	}

	@Override
	protected boolean doWork(DatasetVector datasetVector) {
		boolean isSuccessful = false;

		try {
			SpatialMeasure.addSteppedListener(steppedListener);
			// 调用中心要素方法，并获取结果矢量数据集
			DatasetVector result = SpatialMeasure.measureCentralElement(
					datasetVector,
					parameterSaveDataset.getResultDatasource(),
					DatasetUtilities.getAvailableDatasetName(parameterSaveDataset.getResultDatasource(),parameterSaveDataset.getDatasetName(),null)
					,
					measureParameter.getMeasureParameter());
			this.getParameters().getOutputs().getData(OUTPUT_DATASET).setValue(result);
			isSuccessful = result != null;
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e.getMessage());
			e.printStackTrace();
		} finally {
			SpatialMeasure.removeSteppedListener(steppedListener);
		}
		return isSuccessful;
	}

	@Override
	public String getTitle() {
		return ProcessProperties.getString("String_CentralElement");
	}

	@Override
	public String getKey() {
		return MetaKeys.CENTRAL_ELEMENT;
	}
}
