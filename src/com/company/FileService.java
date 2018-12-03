package com.company;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class FileService {
    static String encode="KOI8-R";

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public FileService(){ }

    public String getInfoFromFile(String path) throws IOException {
        List<String> text = Files.readAllLines(new File(path).toPath(), Charset.forName(encode));
        String res="";
        for (String b:text){
            res+=b+"\n";
        }
        //System.out.println();
        //String str = new String(data, encode);
        return res;
    }

    public int[] getIntInfoFromFile(String path) throws IOException {
        String info=getInfoFromFile(path);
        int[] res=new int[info.length()];
        for (int i=0;i< res.length;i++){
            res[i]=(int)info.charAt(i);
        }
        return res;
    }

    public static void saveInfoToFile(String filePath, String info) throws IOException {
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), encode));
        try {
            out.write(info);
        } finally {
            out.close();
        }
    }


}
