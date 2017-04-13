package com.supermap.desktop.process.meta.metaProcessImplements;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.conversion.*;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.dataconversion.IParameterCreator;
import com.supermap.desktop.process.dataconversion.ImportParameterCreator;
import com.supermap.desktop.process.dataconversion.ImportSettingSetter;
import com.supermap.desktop.process.dataconversion.ReflectInfo;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.parameter.implement.DefaultParameters;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.datas.types.DatasetTypes;
import com.supermap.desktop.ui.UICommonToolkit;

import javax.swing.*;
import java.text.MessageFormat;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author XiaJT
 */
public class MetaProcessImport extends MetaProcess {

	private final static String OUTPUT_DATA = "ImportResult";
	protected ImportSetting importSetting;
	private CopyOnWriteArrayList<ReflectInfo> defaultImportParameters;
	private CopyOnWriteArrayList<ReflectInfo> paramParameters;
	private String importType = "";
	private IParameterCreator parameterCreator;
	private ImportSteppedListener importStepListener = new ImportSteppedListener() {
		@Override
		public void stepped(ImportSteppedEvent e) {
			fireRunning(new RunningEvent(MetaProcessImport.this, e.getSubPercent(), ""));
		}
	};

	public MetaProcessImport(ImportSetting importSetting, String importType) {
		this.importSetting = importSetting;
		this.importType = importType;
		initParameters();
	}

	public MetaProcessImport() {
		parameters = new DefaultParameters();
		this.outputs.addData(OUTPUT_DATA, DatasetTypes.DATASET);
	}

	public void initParameters() {
		parameters = new DefaultParameters();
		this.outputs.addData(OUTPUT_DATA, DatasetTypes.DATASET);
		parameterCreator = new ImportParameterCreator();
		setDefaultImportParameters(parameterCreator.createDefault(importSetting));
		setParamParameters(parameterCreator.create(importSetting));
		updateParameters();
	}

	public void updateParameters() {
		if (null != parameterCreator.getParameterCombineParamSet()) {
			parameters.setParameters(parameterCreator.getParameterFileSet(""), parameterCreator.getParameterCombineResultSet(), parameterCreator.getParameterCombineParamSet());
		} else {
			parameters.setParameters(parameterCreator.getParameterFileSet(""), parameterCreator.getParameterCombineResultSet());
		}
	}

	public void setImportSetting(ImportSetting importSetting) {
		this.importSetting = importSetting;
	}

	public void setDefaultImportParameters(CopyOnWriteArrayList<ReflectInfo> defaultImportParameters) {
		this.defaultImportParameters = defaultImportParameters;
	}

	public void setParamParameters(CopyOnWriteArrayList<ReflectInfo> paramParameters) {
		this.paramParameters = paramParameters;
	}

	@Override
	public IParameterPanel getComponent() {
		return parameters.getPanel();
	}

	@Override
	public String getTitle() {
		return MessageFormat.format(ProcessProperties.getString("String_ImportFileType"), importType);
	}

	@Override
	public void run() {
		fireRunning(new RunningEvent(this, 0, "start"));
		DataImport dataImport = ImportSettingSetter.setParameter(importSetting, defaultImportParameters, paramParameters);
		dataImport.addImportSteppedListener(this.importStepListener);
		ImportResult result = dataImport.run();
		ImportSetting[] succeedSettings = result.getSucceedSettings();
		if (succeedSettings.length > 0) {
			final Datasource datasource = succeedSettings[0].getTargetDatasource();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (null != datasource) {
						UICommonToolkit.refreshSelectedDatasourceNode(datasource.getAlias());
					}
				}
			});
			Dataset dataset = datasource.getDatasets().get(succeedSettings[0].getTargetDatasetName());
			this.outputs.getData(OUTPUT_DATA).setValue(dataset);
			fireRunning(new RunningEvent(this, 100, "finished"));
			setFinished(true);
		} else {
			fireRunning(new RunningEvent(this, 100, ProcessProperties.getString("String_ImportFailed")));
			setFinished(true);
		}
		dataImport.removeImportSteppedListener(this.importStepListener);
	}

	@Override
	public String getKey() {
		return MetaKeys.IMPORT;
	}

	@Override
	public Icon getIcon() {
		return getIconByPath("/processresources/Process/import.png");
	}
}
