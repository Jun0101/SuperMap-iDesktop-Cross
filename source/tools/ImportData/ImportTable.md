---
title: 导入电子表格数据
---

### 使用说明

　　在数据采集时，常常会将地理数据的空间信息保存到 Excel或csv文件中，可提高现有数据的利用率。

　　Excel文件说明：

  - 导入的 Microsoft Excel 文件格式只支持 Office 2007 及更新版本，即 *.xlsx 格式的文件。 
  - Excel 文件的第一条记录可以是字段名。 
  - 导入的结果数据集的名称，一般情况下是 “Excel 文件名”+“_”+“sheet名称”。 
  - 导入 Microsoft Excel 时，默认导入所有有数据的sheet，忽略无数据的sheet。 
  - 当存在合并单元格时，仅合并单元格区域的第一个单元格有数据。

　　CSV文件说明：

  - 开头不能留空，且以行为单位，每条记录是一行。 
  - 文本中只能包含分隔符及字段值等信息。 
  - 默认以英文逗号作为分隔符，SuperMap iDesktop 9D 也支持自定义文本分隔符。 
  - 第一条记录可以是字段名。


### 操作说明

　　导入Excel的参数设置比较简单，此处以导入CSV为例介绍导入的转换参数，通用参数可参考[通用参数](GeneraParameters.html)页面。

　　1. 分隔符：用来设置 CSV 文件中的分隔符，默认使用半角逗号（,）。另外可选的分隔符还有点（.）、制表符（Tab）、空格。系统也支持用户自定义一个文本可识别的字符（包括汉字）。 

　　2. 首行为字段信息：设置需要导入的原 CSV 文件的首行是否是字段名称。勾选该参数，则导入后的字段名称为首行的字段值，否则为属性信息。如果 CSV 文件首行指定了字段信息，则应用程序会自动读取。

　　3. 数据预览：可预览CSV文件导入为属性表数据的效果。

　　4. 导入为空间数据 
　
   - WKT串字段：通过指定WKT串字段方式获取数据的空间信息。 WKT(Well-known text)是一种文本标记语言，用于表示矢量几何对象、空间参照系统及空间参照系统之间的转换。
   - 坐标字段：通过设置经度、纬度、高程字段来指定CSV数据对应的空间信息。 


### 相关主题

![](img/smalltitle.png) [AutoCAD数据](ImportAutoCAD.html)

![](img/smalltitle.png) [ArcGis数据](ImportArcGIS.html)

![](img/smalltitle.png) [MapGIS数据](ImportMapGIS.html)

![](img/smalltitle.png) [MapInfo数据](ImportMapInfo.html)

![](img/smalltitle.png) [影像栅格数据](ImportIMG.html)

![](img/smalltitle.png) [三维模型数据](ImportModel.html)

![](img/smalltitle.png) [Lidar数据](ImportLidar.html)

![](img/smalltitle.png) [Google数据](ImportKML.html)

![](img/smalltitle.png) [矢量文件](ImportVectorFiles.html)

![](img/smalltitle.png) [导入文件夹](ImportFolder.html)


