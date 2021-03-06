package com.nature.third.impl;

import com.nature.base.util.*;
import com.nature.base.vo.UserVo;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.flow.model.StopGroup;
import com.nature.mapper.flow.StopGroupMapper;
import com.nature.third.inf.IGetAllGroups;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class GetAllGroupsImpl implements IGetAllGroups {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private StopGroupMapper stopGroupMapper;

    @Override
    public void getAllGroup() {
        String sendGetData = HttpUtils.doGet(SysParamsCache.STOP_GROUPS_URL(), null, 5 * 1000);
        logger.info("返回信息：" + sendGetData);
    }

    @Override
    public void getGroupAndSave() {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        //先清空Group表信息再插入
        stopGroupMapper.deleteGroupCorrelation();
        int deleteGroup = stopGroupMapper.deleteGroup();
        logger.debug("成功删除Group" + deleteGroup + "条数据！！！");
        HttpClientStop stop = new HttpClientStop();
        String stopGroupInfo = stop.getGroupAndStopInfo("", SysParamsCache.STOP_GROUPS_URL());
        String jsonResult = "";
        if (StringUtils.isNotBlank(stopGroupInfo)) {
            jsonResult = JSONObject.fromObject(stopGroupInfo).getString("groups");
        }
        String[] group = jsonResult.split(",");
        int a = 0;
        for (String string : group) {
            if (string.length() > 0) {
                StopGroup stopGroup = new StopGroup();
                stopGroup.setId(SqlUtils.getUUID32());
                stopGroup.setCrtDttm(new Date());
                stopGroup.setCrtUser(username);
                stopGroup.setLastUpdateUser(username);
                stopGroup.setEnableFlag(true);
                stopGroup.setLastUpdateDttm(new Date());
                stopGroup.setGroupName(string);
                int insertStopGroup = stopGroupMapper.insertStopGroup(stopGroup);
                a += insertStopGroup;
            }
        }
        logger.debug("成功插入Group" + a + "条数据！！！");
    }

}
