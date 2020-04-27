package com.sayo.qa.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sayo.qa.entity.Event;
import com.sayo.qa.entity.Information;
import com.sayo.qa.service.InformationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventCon {
//    private static final Logger logger = LoggerFactory.getLogger(EventCon.class);
//
//    @Autowired
//    private InformationService informationService;
//
//    @KafkaListener(topics = {"inf","sam","fin","req"})
//    public void handle(ConsumerRecord record){
//        if (record == null || record.value() == null){
//            logger.error("消息的内容为空");
//            return;
//        }
//        Event event = JSONObject.parseObject(record.value().toString(),Event.class);
//        if (event == null){
//            logger.error("消息格式错误");
//            return;
//        }
//
//        //发送站内通知
//        Information information = new Information();
//        information.setFromid(1);//1就是系统通知
//        information.setToid(event.getEntityuserId());
//        information.setType(event.getTopic());
//        information.setCreatetime(new Date());
//
//
//        information.setContent(event.getEntityType());
//        informationService.addInformation(information);
//    }


}
