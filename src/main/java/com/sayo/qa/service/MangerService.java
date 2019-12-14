package com.sayo.qa.service;

import com.sayo.qa.dao.ManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MangerService {
    @Autowired
    ManagerMapper managerMapper;


}
