import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志工具类 - 记录日志
 */
public class LoggerUtil {
    /**
     * 记录日志
     * @param msg           需要记录的信息
     * @param filePath      日志文件的路径
     */
    public static void log(String msg,String filePath){
        try {
            // 指定一个日志文件
            PrintStream printStream = new PrintStream(new FileOutputStream(filePath,true));

            // 改变输出方向
            System.setOut(printStream);

            // 日期调用方法时的当前时间
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
            String strTime = sdf.format(now);

            // 输出日志信息到日志文件
            System.out.println(strTime + " ： " + msg);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
