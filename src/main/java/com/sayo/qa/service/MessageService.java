package com.sayo.qa.service;

import com.sayo.qa.dao.MessageMapper;
import com.sayo.qa.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    public int addMessage(Message message){
        return messageMapper.insertSelective(message);
    }

    public Message findMessageByToandStatus(int toId,int status){
        return messageMapper.findMessageByToandStatus(toId,status);
    }

    public int findCountByToId(int toId){
        return messageMapper.findCountByToId(toId);
    }

    public int updateMessage(Message message){
        return messageMapper.updateByPrimaryKeySelective(message);
    }
}
