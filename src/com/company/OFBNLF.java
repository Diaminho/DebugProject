package com.company;

import java.io.IOException;
import java.util.Arrays;

public class OFBNLF {
    static GostNew gostNew=new GostNew();

    public static String doOFBNLF(String fileName, String keyFile, boolean flag){
        FileService fileService=new FileService();
        try {
            gostNew.setText(fileService.getInfoFromFile(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String key= null;
        try {
            key = fileService.getInfoFromFile(keyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] subKeys1=gostNew.getSubKeyFirst(key);

        System.out.println("Исходный текст");
        System.out.println(gostNew.getText());
        //System.out.println("KEY: "+key);

        String[] blocks=gostNew.getBlocks(gostNew.getText());
        String[] encr=new String[blocks.length];
        String[] ki=new String[2];
        String step0= String.copyValueOf(key.toCharArray(),0,32);
        ki[1] ="1";
        for (int i = 0; i < blocks.length; i++) {
            step0= gostNew.doEncrypt(step0,subKeys1,flag);
            ki[1]= step0.substring(step0.length()/2);
            ki[0]= step0.substring(0,step0.length()/2);
            encr[i] = gostNew.doEncrypt(blocks[i],ki,flag);
        }

        System.out.println("Зашифрованный текст");
        String encrStr="";
        for (String s:encr){
            encrStr+=s;
            //System.out.println(s);
        }
        String res=StringWorker.BinStrToStr(encrStr);
        System.out.println(res);

        return res;

    }
}
