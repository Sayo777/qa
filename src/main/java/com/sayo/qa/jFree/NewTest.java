package com.sayo.qa.jFree;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class NewTest {

    private Configuration configuration = null;

    public NewTest() {
        configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
    }

    /**
     *   将文本信息转为word输出
     * @throws Exception
     */
    public File createDoc() {
        Map<String,Object> dataMap = new HashMap<>();
        getData(dataMap); //创建数据
//        configuration.setClassForTemplateLoading(this.getClass(), ""); //模板文件所在路径
        Template t = null;

        String fileName =System.currentTimeMillis()+"-请假.doc";
        File file = new File(fileName);
        try {
//            t = configuration.getTemplate("Test.ftl"); //获取模板文件
            Configuration cfg = new Configuration();
            cfg.setDirectoryForTemplateLoading(new File("D:/work/wordTemplate"));
            t = cfg.getTemplate("Test.ftl");
            t.setEncoding("utf-8");

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),"utf-8"));
            t.process(dataMap, out);  //将填充数据填入模板文件并输出到目标文件
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * 导出word 并提供下载
     * @param response
     */
    public void download(HttpServletResponse response,String pathname) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            File file = new File(pathname);
            String fileName = System.currentTimeMillis()+".doc";

            response.setContentType("application/msword;charset=utf-8");
            response.addHeader("Content-Disposition", "attachment; filename=\""
                    + new String(fileName.getBytes(),"iso-8859-1") + "\"");

            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(response.getOutputStream());

            byte[] buff = new byte[10240];
            int bytesRead;
            while(-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }

            bis.close();
            bos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //获得数据
    private void getData(Map<String,Object> dataMap) {
        dataMap.put("name", "xxx");
        dataMap.put("age", "12");
    }

    //获得图片的base64编码
//    private String getBase64(String imgUrl) {
//        ByteArrayOutputStream data = new ByteArrayOutputStream();
//        URL url = null;
//        InputStream in = null;
//        HttpURLConnection httpUrl = null;
//        byte[] by = new byte[1024];
//
//        try {
//            url = new URL(imgUrl);
//            httpUrl = (HttpURLConnection)url.openConnection();
//            httpUrl.connect();
//            in = httpUrl.getInputStream();
//            int len = -1;
//            while((len = in.read(by)) != -1) {
//                data.write(by, 0, len);
//            }
//
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data.toByteArray());
//    }
}
