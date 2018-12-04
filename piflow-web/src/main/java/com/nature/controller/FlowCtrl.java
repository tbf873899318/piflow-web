package com.nature.controller;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.mxGraph.service.IMxCellService;
import com.nature.component.mxGraph.service.IMxGraphModelService;
import com.nature.component.mxGraph.service.IMxGraphService;
import com.nature.component.process.model.Process;
import com.nature.component.process.service.IProcessService;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Property;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.service.*;
import com.nature.component.workFlow.vo.FlowVo;
import com.nature.third.inf.IGetFlowInfo;
import com.nature.third.inf.IGetFlowLog;
import com.nature.third.inf.IStartFlow;
import com.nature.third.inf.IStopFlow;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flow/*")
public class FlowCtrl {

    /**
     * 引入日志，注意都是"org.slf4j"包下
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private IFlowService flowServiceImpl;

    @Autowired
    private IStartFlow startFlowImpl;

    @Autowired
    private IStopFlow stopFlowImpl;

    @Autowired
    private IGetFlowInfo getFlowInfoImpl;

    @Autowired
    private IGetFlowLog getFlowLogImpl;

    @Autowired
    private IPropertyService propertyServiceImpl;

    @Autowired
    private IStopsService stopsServiceImpl;

    @Autowired
    private IPathsService pathsServiceImpl;

    @Autowired
    private IMxGraphModelService mxGraphModelServiceImpl;

    @Autowired
    private IFlowInfoDbService flowInfoDbServiceImpl;

    @Autowired
    private IMxCellService mxCellServiceImpl;

    @Autowired
    private IMxGraphService mxGraphServiceImpl;

    @Autowired
    private IProcessService processServiceImpl;

    /**
     * 启动flow
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/runFlow")
    @ResponseBody
    public String runFlow(HttpServletRequest request, Model model) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("code", "0");
        String errMsg = "";
        String flowId = request.getParameter("flowId");
        if (StringUtils.isNotBlank(flowId)) {
            // 根据flowId查询flow
            Flow flowById = flowServiceImpl.getFlowById(flowId);
            // addFlow不为空且ReqRtnStatus的值为true,则保存成功
            if (null != flowById) {
                Process process = processServiceImpl.startFlowAndUpdateProcess(flowById);
                if (null != process) {
                    rtnMap.put("code", "1");
                    rtnMap.put("processId", process.getId());
                    rtnMap.put("errMsg", "保存到process成功，调用接口成功，更新成功");
                } else {
                    errMsg = "调用接口失败";
                    rtnMap.put("errMsg", errMsg);
                    logger.warn(errMsg);
                }
            } else {
                errMsg = "未查询到flowId为" + flowId + "的flow";
                rtnMap.put("errMsg", errMsg);
                logger.warn(errMsg);
            }
        } else {
            errMsg = "flowId为空";
            rtnMap.put("errMsg", errMsg);
            logger.warn(errMsg);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @RequestMapping("/queryFlowData")
    @ResponseBody
    public String queryFlowData(String load) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", "0");
        FlowVo flowVo = flowServiceImpl.getFlowVoById(load);
        rtnMap.put("flow", flowVo);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * 保存添加flow
     *
     * @param flowVo
     * @return
     */
    @RequestMapping("/saveFlowInfo")
    @ResponseBody
    public String saveFlowInfo(FlowVo flowVo) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", "0");
        Flow flow = new Flow();

        BeanUtils.copyProperties(flowVo, flow);
        String id = Utils.getUUID32();
        flow.setId(id);
        flow.setCrtDttm(new Date());
        flow.setCrtUser("Add");
        flow.setLastUpdateDttm(new Date());
        flow.setLastUpdateUser("Add");
        flow.setEnableFlag(true);
        flow.setVersion(0L);
        flow.setUuid(id);

        MxGraphModel mxGraphModel = new MxGraphModel();
        mxGraphModel.setFlow(flow);
        mxGraphModel.setId(Utils.getUUID32());
        mxGraphModel.setCrtDttm(new Date());
        mxGraphModel.setCrtUser("Add");
        mxGraphModel.setLastUpdateDttm(new Date());
        mxGraphModel.setLastUpdateUser("Add");
        mxGraphModel.setEnableFlag(true);
        mxGraphModel.setVersion(0L);
        flow.setMxGraphModel(mxGraphModel);
        int addFlow = flowServiceImpl.addFlow(flow);
        if (addFlow > 0) {
            rtnMap.put("code", "1");
            rtnMap.put("flowId", id);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * 修改Flow
     *
     * @param flow
     * @return
     */
    @RequestMapping("/updateFlowInfo")
    @ResponseBody
    public int updateFlowInfo(Flow flow) {
        String id = flow.getId();
        flow.setId(id);
        flow.setName(flow.getName());
        flow.setDescription(flow.getDescription());
        flow.setLastUpdateDttm(new Date());
        flow.setLastUpdateUser("ddw");
        flow.setVersion(0L + 1);
        int result = flowServiceImpl.updateFlow(flow);
        return result;
    }

    /**
     * 根据flowId删除flow关联信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteFlow")
    @ResponseBody
    @Transactional
    public int deleteFlow(String id) {
        int deleteFLowInfo = 0;
        Flow flowById = flowServiceImpl.getFlowById(id);
        if (null != flowById) {
            if (null != flowById.getStopsList())
                //先循环删除stop属性
                for (Stops stopId : flowById.getStopsList()) {
                    if (null != stopId.getProperties())
                        for (Property property : stopId.getProperties()) {
                            propertyServiceImpl.deleteStopsPropertyByStopId(property.getId());
                        }
                }
            //删除stop
            stopsServiceImpl.deleteStopsByFlowId(flowById.getId());
            //删除paths
            pathsServiceImpl.deletePathsByFlowId(flowById.getId());
            //删除FLowInfo
            if (flowById.getAppId() != null) {
                flowInfoDbServiceImpl.deleteFlowInfoById(flowById.getAppId().getId());
            }
            if (null != flowById.getMxGraphModel()) {
                List<MxCell> root = flowById.getMxGraphModel().getRoot();
                if (null != root && !root.isEmpty()) {
                    for (MxCell mxcell : root) {
                        if (mxcell.getMxGeometry() != null) {
                            logger.info(mxcell.getMxGeometry().getId());
                            mxGraphServiceImpl.deleteMxGraphById(mxcell.getMxGeometry().getId());
                        }
                        mxCellServiceImpl.deleteMxCellById(mxcell.getId());

                    }
                }
                mxGraphModelServiceImpl.deleteMxGraphModelById(flowById.getId());
            }
            //删除FLow
            deleteFLowInfo = flowServiceImpl.deleteFLowInfo(id);
        }
        return deleteFLowInfo;
    }
}