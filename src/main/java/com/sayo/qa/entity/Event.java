package com.sayo.qa.entity;

import java.util.HashMap;
import java.util.Map;

public class Event {
    private String topic;
    private int userId; //是谁发的消息
    private String entityType;//消息类型
    private int entityuserId;//发给谁的消息
    private Map<String,Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public String getEntityType() {
        return entityType;
    }

    public Event setEntityType(String entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityuserId() {
        return entityuserId;
    }

    public Event setEntityuserId(int entityuserId) {
        this.entityuserId = entityuserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key,Object value) {
        this.data.put(key,value);
        return this;
    }
}
