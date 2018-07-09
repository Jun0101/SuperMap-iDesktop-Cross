---
title: 表面体积
---

　　栅格表面距离，可计算所选多边形区域内的栅格数据集拟合的三维曲面与一个基准平面之间的空间上的体积。


　　应用程序提供了2种计算表面体积的方式：一种是通过绘制对象和基准高程，计算绘制对象区域内的表面体积；一种是直接选择面数据集中的一个或者多个对象，分析其表面体积（之和）。

### ![](../img/read.gif) 绘制多边形

1. 在地图窗口中打开要计算表面距离的栅格数据集。注意：当前工作空间中，如果不存在打开的栅格数据时，该功能不能使用。
2. 单击“空间分析”→“栅格分析”→“表面分析”下拉按钮，在弹出的下拉菜单中选择“表面量算”中的“地表体积”。
3. 输出窗口中提示：“请用鼠标在地图上画一个多边形，然后单击右键进行分析”，同时鼠标状态变为绘制状态。
4. 在地图上绘制一个面对象，单击鼠标右键结束绘制。
5. 在弹出对话框中输入基准高程，其中基准高程为量算表面体积的起始高度。
6. 单击“计算”按钮，计算临时绘制的多边形覆盖的体积。
7. 输出窗口即会显示分析区域的体积，默认的体积单位为立方米。


### ![](../img/read.gif) 选中多边形对象

1. 如果地图窗口中同时存在栅格数据和面数据，选中一个或者多个多边形对象（按住 Shift 键可以选择多个多边形对象）。 

2. 单击“空间分析”→“栅格分析”→“表面分析”下拉按钮，在弹出的下拉菜单中选择“表面量算”中的“选面体积”。 
3. 在弹出的“表面体积参数设置”对话框中，设置基准高程。
4. 单击“计算”按钮，计算选中多边形的体积，输出窗口显示分析的表面体积大小，默认的体积单位为立方米。

　　或执行以下操作：

1. 单击“空间分析”→“栅格分析”→“表面分析”下拉按钮，在弹出的下拉菜单中选择“表面量算”中的“选面体积”，地图中会弹出提示"选择一个或多个面对象来进行表面量算，单击右键结束选择"。 
2. 根据提示选择一个或者多个面对象，按住 Shift 键可以选择多个面对象，单击右键结束选择。 
3. 在弹出的“表面体积参数设置”对话框中，设置基准高程。
4. 单击“计算”按钮，计算选中多边形的体积，输出窗口显示分析的表面体积大小，默认的体积单位为立方米。
　　注：当选中多个多边形对象时，量算结果为多个多边形对象的表面体积之和。默认距离的单位为立方米。按住 Esc 键可以清除地图窗口中多边形对象的选中状态。