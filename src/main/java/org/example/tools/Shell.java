package org.example.tools;

import java.io.*;
import java.util.*;

public class Shell {
    public static List<String> readShell(){
        File file = new File("./shell");
        List<String> filename = new ArrayList<>();
        for(int i=0;i<file.listFiles().length;i++){
            if(file.listFiles()[i].isFile()){
                System.out.println(file.listFiles()[i].getName());
                filename.add(file.listFiles()[i].getName());
            }
        }
        return filename;
    }
    //读取shell目录下的webshell文件，并转base64
    public static String GetShellBase64Str(String path){
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] buffer = new byte[1024];
        int len = 0;
        String result = "" ;
        StringBuilder builder = new StringBuilder();
        try (FileInputStream inputStream = new FileInputStream(new File(path))) {
            while ((len = inputStream.read(buffer)) != -1) {
                byte[] src = Arrays.copyOfRange(buffer, 0, len);
                builder.append(encoder.encodeToString(src));
            }
            result = builder.toString();
        } catch (FileNotFoundException e) {
            result = e.toString();
        } catch (IOException e) {
            result = e.toString();
        }
        return result;
    }
    public static String GenerateUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
