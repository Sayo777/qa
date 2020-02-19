package com.sayo.qa.jFree;

import java.util.ArrayList;
import java.util.HashMap;
import cn.afterturn.easypoi.entity.ImageEntity;

/**
 * @author 何昌杰
 */
public class TestDemo {


    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>();

        //模拟饼状图数据
//        HashMap<String, Integer> datas = new HashMap<>(3);
//        datas.put("一号",10);
//        datas.put("二号",20);
//        datas.put("三号",40);
//        ImageEntity imageEntity = JfreeUtil.pieChart("测试",datas, 500, 300);
//        map.put("picture", imageEntity);

        //模拟其它普通数据
        map.put("qaName", "千千质检机构");
        map.put("qaTime", "2019-10-10");
        map.put("enterpriseName", "森马服饰");
        map.put("commercialNumber", 92993);
        map.put("address", "浙江省杭州市江岸区");
        map.put("legalperson", "王大为");
        map.put("contact", "王大为");
        map.put("phone", "1388892920");
        map.put("productName", "羽绒服");
        map.put("productDes", "productDes");
        map.put("appearance", "appearance");
        map.put("size", "size");
        map.put("accessory", "accessory");
        map.put("craft", "craft");
        map.put("textile", "textile");
        map.put("pressing", "pressing");
        map.put("packaging", "packaging");
        map.put("material", "是");
        map.put("standard", "是");
        map.put("system", "是");
        map.put("hasReport", "是");
        map.put("unlawful", "否");
//        map.put("boo", true);

        //模拟表格数据
//        ArrayList<HashMap<String, String>> list = new ArrayList<>();
//        HashMap<String, String> temp = new HashMap<>();
//        temp.put("enterpriseName","森马服饰");
//        temp.put("name","第一个人");
//        temp.put("age","23");
//        list.add(temp);
//        temp = new HashMap<>(3);
//        temp.put("sn","2");
//        temp.put("name","第二个人");
//        temp.put("age","24");
//        list.add(temp);
//        map.put("personlist",list);
        //word模板相对路径、word生成路径、word生成的文件名称、数据源
//        WordUtil.exportWord("D:/work/wordTemplate/resultTemplate.docx", "D:/work/wordTemplate", "demo1.docx", map);
    }
}
