package com.supermap.desktop.lbs.Interface;

import com.supermap.desktop.lbs.params.BuildCacheJobSetting;
import com.supermap.desktop.lbs.params.JobResultResponse;
import com.supermap.desktop.lbs.params.KernelDensityJobSetting;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created by xie on 2017/1/6.
 */
public interface IServerService {

    /**
     * 登录iserver服务
     *
     * @param userName
     * @param passWord
     * @return
     */
    CloseableHttpClient login(String userName, String passWord);

    /**
     * 生成子（核密度分析）任务
     *
     * @param kernelDensityJobSetting 核密度分析参数
     * @return
     */
    
    JobResultResponse query(KernelDensityJobSetting kernelDensityJobSetting);

    /**
     * 生成子（热度图）任务
     *
     * @param buildCacheJobSetting
     * @return
     */
    JobResultResponse query(BuildCacheJobSetting buildCacheJobSetting);

    /**
     * 查询JSON结果
     *
     * @param url
     * @return
     */
    String query(String url);

}
