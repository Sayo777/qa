package com.sayo.qa.jFree;

import cn.afterturn.easypoi.word.WordExportUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.util.Assert;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author 何昌杰
 */
public class WordUtil {
    /**
     * 导出word
     * <p>第一步生成替换后的word文件，只支持docx</p>
     * <p>第二步下载生成的文件</p>
     * <p>第三步删除生成的临时文件</p>
     * 模版变量中变量格式：{{foo}}
     *
     * @param templatePath word模板地址
     * @param temDir       生成临时文件存放地址
     * @param fileName     文件名
     * @param params       替换的参数
     */
    public static void exportWord(String templatePath, String temDir, String fileName,
                                  Map<String, Object> params) {
        Assert.notNull(templatePath, "模板路径不能为空");
        Assert.notNull(temDir, "临时文件路径不能为空");
        Assert.notNull(fileName, "导出文件名不能为空");
        Assert.isTrue(fileName.endsWith(".docx"), "word导出请使用docx格式");
        if (!temDir.endsWith("/")) {
            temDir = temDir + File.separator;
        }
        File dir = new File(temDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String tmpPath = temDir + fileName;
        try {
            XWPFDocument doc = WordExportUtil.exportWord07(templatePath, params);

            FileOutputStream fos = new FileOutputStream(tmpPath);
            doc.write(fos);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void downloadWord(HttpServletResponse response,String tmpPath) throws IOException {
        InputStream fin = null;
        ServletOutputStream out = null;
        try {
            fin = new FileInputStream(tmpPath);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            // 设置浏览器以下载的方式处理该文件名
            String fileName = "文件"+ System.currentTimeMillis() + ".doc";
            response.setHeader("Content-Disposition", "attachment;filename="
                    .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

            out = response.getOutputStream();
            byte[] buffer = new byte[512];  // 缓冲区
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while((bytesToRead = fin.read(buffer)) != -1) {
                out.write(buffer, 0, bytesToRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(fin != null) fin.close();
            if(out != null) out.close();
        }
    }
}
