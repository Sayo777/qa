package com.sayo.qa.CommonUtil;

import com.sayo.qa.entity.Inspector;
import org.springframework.stereotype.Component;

@Component
public class HostHolderInspector {
    private ThreadLocal<Inspector> inspectors = new ThreadLocal<>();
    public void setInspector(Inspector inspector){
        inspectors.set(inspector);
    }
    public Inspector getInspector(){
        return inspectors.get();
    }
    public void remove(){
        inspectors.remove();
    }
}


