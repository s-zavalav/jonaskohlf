package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author shkstart
 * @create 2022-04-12 9:07
 *
 * 响应对象
 * 该类的每一个实例用于表示HTTP协议要求的响应
 * 每个响应由三部分组成:
 * 状态行，响应头，响应正文
 */
public class HttpServletResponse {
    private Socket socket;
    //状态行相关信息
    private int statusCode = 200;//状态代码
    private String statusReason = "OK";//状态描述

    //响应头相关信息

    //响应正文相关信息
    private File contentFile;
    public HttpServletResponse(Socket socket){
        this.socket = socket;
    }

    /**
     * 将当前响应对象内容按照标准的HTTP响应格式发送客户端
     * @throws IOException
     */
    public void response() throws IOException {
        //3.1发送状态行
        sendStatusLine();
        //3.2发送响应头
        sendHeaders();
        //3.3发送响应正文(index.html页面的数据)发送给浏览器
        sendContent();
    }

    /**
     * 发送状态行
     */
    private void sendStatusLine() throws IOException {
        println("HTTP/1.1"+" "+statusCode+" "+statusReason);
    }

    /**
     * 发送响应头
     */
    private void sendHeaders() throws IOException {
        println("Content-Type: text/html");
        println("Content-Length:"+contentFile.length());
        println("");
    }

    /**
     * 发送响应正文
     */
    private void sendContent() throws IOException {
        //负责给浏览器发送
        OutputStream out = socket.getOutputStream();
        //获取文件的字节
        FileInputStream fis = new FileInputStream(contentFile);
        int len;
        byte[] data = new byte[1024*10];
        while ((len = fis.read(data))!= -1){
            out.write(data,0,len);
        }
    }
    /**
     * 将一行字符串按照iso8859-1编码发送到客户端,最后会自动添加回车和换行符
     * @param line
     */
    private void println(String line) throws IOException {
        //获取输出流
        OutputStream out = socket.getOutputStream();
        //创建byte数组
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);//单独发送了回车符
        out.write(10);//单独发送了换行符
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
    }
}
