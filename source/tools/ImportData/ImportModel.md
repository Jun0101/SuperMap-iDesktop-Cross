---
title: 导入三维模型文件
---

### 使用说明

　　SuperMap iDesktop Cross 支持导入多种格式的三维模型文件，支持的模型文件格式有 \*.dxf、\*.x、\*.3ds、\*.osgb 等。

 模型数据格式           | 文件说明             |  导入数据类型          
 :-------------- | :--------------- | :---------------
 \*.3ds | 3DS 三维模型数据。 | 模型数据集  
 \*.dxf | DXF 三维模型文件。 | CAD、简单数据集 
 \*.x | DirectX 的三维模型文件。 | 模型数据集  
 \*.osgb | 倾斜摄影 OSGB 模型文件，可导入为模型数据集。 | 模型数据集 

### 操作说明

　　1.导入界面中的结果设置和源文件信息可参考[通用参数](GeneraParameters.html)页面。

　　2.转换参数的具体说明如下：

 - **旋转方式**：设置模型对象的旋转方式，只有当导入的数据为\*.obj格式时，该下拉按钮才可用。提供了不旋转、右手系绕X轴旋转90度(x, -z, y)和右手系绕X轴旋转-90度(x, z, -y)。  
 - **分解为多个模型对象**：若勾选了该复选框，则导入时会根据原始数据将模型分解为多个对象，且会增加一个ModelName字段，记录模型对象的名称。 
 - **投影设置**：用于设置导入数据集的投影，选中“投影设置”单选框，单击其右侧的“设置...”按钮，在弹出的“投影设置”对话框中设置导入后数据集的投影，具体操作请参见[设置设置](docs/DataProcess/Projection/SetPrjCoordSys.html)。 
 - **导入投影文件**：选择“导入投影文件”单选框，单击右侧的“选择”按钮，在弹出的的“选择”窗口中，选择投影信息文件并导入即可。支持导入 shape 投影信息文件（*.shp;*.prj）、MapInfo 交换格式（*.mif）、MapInfo TAB 文件（*.tab）、影像格式投影信息文件（*.tif;*.img;*.sit）、投影信息文件（*.xml）。 
 - **模型定位点**：模型导入时的位置，用一个三维点对象表示。默认定位点为（0,0,0）。 

　　**注意**：模型数据导入后，模型数据集的坐标系默认与所在数据源坐标系一致。若导入后数据集坐标系为地理坐标系，则模型定位点应设置为经纬度类型；若导入后数据集坐标系为平面或投影坐标系，则模型定位点应设置为 X、Y、Z 坐标。

### 注意

　　在设置模型定位点时，需按照数据导入后所在数据源的坐标系来设置相应的点坐标，若导入的模型数据定位点坐标与数据集坐标系不符，加载到场景中会导致数据显示效果错误。例如：将模型数据集导入到平面坐标系的数据源中，模型定位点的坐标误设为经纬度，加载模型数据到平面场景时，模型数据将不显示。 

### 相关主题

![](img/smalltitle.png) [AutoCAD数据](ImportAutoCAD.html)

![](img/smalltitle.png) [ArcGis数据](ImportArcGIS.html)

![](img/smalltitle.png) [MapGIS数据](ImportMapGIS.html)

![](img/smalltitle.png) [MapInfo数据](ImportMapInfo.html)

![](img/smalltitle.png) [电子表格](ImportTable.html)

![](img/smalltitle.png) [影像栅格数据](ImportIMG.html)

![](img/smalltitle.png) [Lidar数据](ImportLidar.html)

![](img/smalltitle.png) [Google数据](ImportKML.html)

![](img/smalltitle.png) [矢量文件](ImportVectorFiles.html)

![](img/smalltitle.png) [导入文件夹](ImportFolder.html)



