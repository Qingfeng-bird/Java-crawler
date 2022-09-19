package utils;

public class PrintLogThread extends Thread{
    private String msg;
    private String filePath;

    private PrintLogThread(){}

    public PrintLogThread(String msg,String filePath){
        this.msg = msg;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        LoggerUtil logger = new LoggerUtil();
        logger.log(msg,filePath);
    }
}
