package com.webserver.controller;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import qrcode.QRCodeUtil;

import java.io.FileOutputStream;

/**
 * @author shkstart
 * @create 2022-04-15 19:53
 */
@Controller
public class ToolsController {
//    public static void main(String[] args) {
//
//        String message = "http://www.baidu.com";
//
//        try {
//            //参数1:二维码上包含的文本信息 参数2:图片生成的位置
////            QRCodeUtil.encode(message,"./qr.jpg");
//            //参数1:二维码上包含的文本信息 参数2:图片生成后会通过该流写出
////            QRCodeUtil.encode(message,new FileOutputStream("./qr.jpg"));
//            //参数1:二维码上包含的文本信息 参数2:二维码中间logo图片 参数3:图片生成的位置
//            //参数4:是否需要压缩logo图片到中间大小
////            QRCodeUtil.encode(message,"123.jpg","./qr.jpg",true);
//            QRCodeUtil.encode(message,"123.jpg",new FileOutputStream("./qr.jpg"),true);
//
//            System.out.println("二维码生成完毕!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    @RequestMapping("/myweb/createQR")
    public void createQr(HttpServletRequest request, HttpServletResponse response) {
        String content = request.getParameter("content");
        try {
            QRCodeUtil.encode(content, "1234.jpg", response.getOutputStream(), true);
            response.setContentType("image/jpeg");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
