package com.openapigateway.netty.sync.client;

import com.alibaba.fastjson.JSON;
import com.topinfo.ci.netty.bean.CmwSensoralert;
import com.topinfo.ci.netty.bean.Equip;
import com.topinfo.ci.netty.bean.JsonResult;
import com.topinfo.ci.netty.bean.RealDataInfo;
import com.topinfo.ci.netty.mapper.SensorAlertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topinfo.ci.netty.service.NettyClientService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private NettyClientService clientService;
    @Autowired
    private SensorAlertMapper sensorAlertMapper;

    @RequestMapping("/sendSyncMsg")
    public String sendSyncMsg(String dataId, String text) {
        
        String serviceId = "mmmm";
        
        String result = clientService.sendSyncMsg(text, dataId, serviceId);
        
        return "result:"+result ;
    }
     
}