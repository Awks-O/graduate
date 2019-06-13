package cn.core.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExportUtil {

    /**
     * CSV文件列分隔符
     */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    /**
     * CSV文件列分隔符
     */
    private static final String CSV_RN = "\r\n";

    /**
     * @param dataList 集合数据
     * @param colNames 表头部数据
     * @param mapKey   查找的对应数据
     * @param os       返回结果
     */
    public static boolean doExport(List<Map<String, Object>> dataList, String colNames, String mapKey, OutputStream os) {
        try {
            StringBuilder buf = new StringBuilder();

            String[] colNamesArr;
            String[] mapKeyArr;

            colNamesArr = colNames.split(",");
            mapKeyArr = mapKey.split(",");

            // 完成数据csv文件的封装
            // 输出列头
            for (String aColNamesArr : colNamesArr) {
                buf.append(aColNamesArr).append(CSV_COLUMN_SEPARATOR);
            }
            buf.append(CSV_RN);

            if (null != dataList) { // 写入数据
                for (Map<String, Object> aDataList : dataList) {
                    for (String aMapKeyArr : mapKeyArr) {
                        buf.append(aDataList.get(aMapKeyArr)).append(CSV_COLUMN_SEPARATOR);
                    }
                    buf.append(CSV_RN);
                }
            }
            // 写出响应
            os.write(buf.toString().getBytes("GBK"));
            os.flush();
            return true;
        } catch (Exception e) {
            log.error("doExport错误...", e);
        }
        return false;
    }

    /**
     * setHeader
     */
    public static void setHeader(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        // 设置文件后缀
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String fn = fileName + sdf.format(new Date()) + ".csv";
        // 读取字符编码
        String utf = "UTF-8";

        // 设置响应
        response.setContentType("application/oct-stream;charset=utf-8");
        response.setCharacterEncoding(utf);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, utf));
    }

}
