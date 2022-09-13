package org.example.tools;

import java.io.*;
import java.net.*;

public class HttpUtil {
    public static String doGet(String url){
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
            URLConnection conn = realUrl.openConnection(proxy);
            // 设置请求头
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:66.0) Gecko/20100101 Firefox/66.0");
            conn.connect();
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;

    }
    public static String doPost(String url,String param,String contenttype) throws IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        InputStream inputStream = null;
        try {
            URL urlStr = new URL(url);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlStr.openConnection(proxy);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Accept", "application/xml;charset=UTF-8");
            httpURLConnection.setRequestProperty("Content-type",contenttype);
            httpURLConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(param);
            wr.flush();
            wr.close();
            in = new BufferedReader(new InputStreamReader(
                    httpURLConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
