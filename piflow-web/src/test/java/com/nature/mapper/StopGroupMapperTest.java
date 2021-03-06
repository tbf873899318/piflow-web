package com.nature.mapper;

import java.util.List;

import com.nature.mapper.flow.StopGroupMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.flow.model.StopGroup;

public class StopGroupMapperTest extends ApplicationTests {

	@Autowired
	private StopGroupMapper stopGroupMapper;

	Logger logger = LoggerUtil.getLogger();

	@Test
	public void testGetStopGroupList() {
		List<StopGroup> stopGroupList = stopGroupMapper.getStopGroupList();
		if (null == stopGroupList) {
			logger.info("查询结果为空");
		}
		logger.info(stopGroupList.size() + "");
	}

}
