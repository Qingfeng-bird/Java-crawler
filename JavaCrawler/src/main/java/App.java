import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import utils.PrintLogThread;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 爬取指定网址上的图片
 *
 * Tips：
 * 若出现 403 错误则可能是由于“写入”访问被禁止而造成的，
 * 当试图将文件上载到目录或在目录中修改文件，但该目录不允许"写"访问时就会出现此种错误
 *
 * @author 秋玄
 * @version 1.0
 * @since 1.0
 */
public class App {
    public static void main(String[] args) {
        // 网站地址
        String site = "https://pvp.qq.com/";
        // 图片保存路径
        String filePath = "F://test";
        // 自定义图片名称
        String fileName = "img";
        downloadImg(site,filePath,fileName);
    }

    /**
     * 获取指定网站上所有图片
     * @param website       指定网站的完整域名 包括请求协议，例如：www.xxx.com
     * @param filePath      图片存放路径 例如：F://test
     * @param fileName      图片名称 例如：xxx
     */
    private static void downloadImg(String website,String filePath,String fileName) {
        List<String> urlList = new ArrayList<>();
        try {
            // 获取网站图片的 src
            // 连接到指定网站
            Connection connection = Jsoup.connect(website);
            // 获取网站页面上所有的 DOM 元素
            Document document = connection.get();
            // 获取所有的 img 元素
            Elements imgs = document.getElementsByTag("img");
            // 遍历 imgs
            for (int i = 0; i < imgs.size(); i++) {
                // 获取 img 元素的 src 属性
                String src = imgs.get(i).attr("src");

                // url地址以 “//” 开始，需要拼接请求协议
                if (src.startsWith("//")){
                    src = "http:" + src;
                }

                // 路径为 空 或 “about:blank” 则不添加到 List 中
                if (src.length() != 0 && !"about:blank".equals(src)) {
                    urlList.add(src);
                }

                // 下载图片
                getImg(urlList,filePath,fileName);

                // 记录日志到 log.txt 文件
                PrintLogThread thread = new PrintLogThread("下载完成，第" + (i + 1) + "张图片",filePath + "//log.txt");
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 下载指定 URL 的图片
     * @param imgURL        图片地址的 list 集合
     * @param filePath      图片存放路径
     * @param fileName      图片文件名称
     */
    private static void getImg(List<String> imgURL,String filePath,String fileName){
        InputStream in = null;
        FileOutputStream fos = null;

        // 遍历图片地址 list 集合
        for (int i = 0; i < imgURL.size(); i++) {
            try {
                URL url = new URL(imgURL.get(i));
                in = url.openStream();

                // 拼接文件存放路径及文件名
                String path = appendPath(filePath,fileName,i);

                // 将图片写入本地
                fos = new FileOutputStream(path);
                byte[] bytes = new byte[1024];
                int count = in.read(bytes);
                while(count != -1){
                    fos.write(bytes,0,count);
                    fos.flush();
                    count = in.read(bytes);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally {
                // 释放资源
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * 拼接文件存放路径及文件名
     * @param filePath      文件路径
     * @param fileName      文件名
     * @param i             文件编号
     * @return              文件完整路径
     * 格式：文件路径 + 文件名称 + _ + 文件编号 + 文件后缀（.jpg）
     */
    private static String appendPath(String filePath,String fileName,Integer i) {
        return filePath + "//" + fileName + "_" + (i + 1) + ".jpg";
    }
}
