package com.company;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringWorker {


    public static String[] getBinStrArrayFromString(String string, int blockSize){
       List<String> res=new ArrayList();
       while (string.length()%blockSize!=0) {
           string+="0";
       }

       String block="";

       for(int i=0;i<string.length()-1;i+=blockSize){
           block="";
           for (int j=0;j<blockSize;j++) {
               block+=getBinStringFromChar(string.charAt(i+j));
           }
           res.add(block);
       }
       //System.out.println(res);
       return Arrays.copyOf(res.toArray(), res.toArray().length, String[].class);
    }

    private static String getBinStringFromChar(char c){
        String str=Character.toString(c);
        byte[] res={1,1};
        String str2="";
        try {
            res=str.getBytes("KOI8-R");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for(byte b:res){
            str2+=String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
        }
        return str2;
    }


    //???????????????????????????????
    public static String BinStrToStr(String binStr){
        byte[] byteL=new byte[(int)binStr.length()/8];
        for (int i=0;i<byteL.length;i++) {
            //System.out.println("ss"+binStr.substring(i*8,(i+1)*8));
            byteL[i]=(byte) Integer.parseInt(binStr.substring(i*8,(i+1)*8), 2);
        }

        String decodedData = new String(byteL, Charset.forName("KOI8-R"));
        StringBuffer res=new StringBuffer();

        return decodedData;
    }

    public static String doXorBinaryStrings(String str1, String str2){
        String res="";
        for (int i=0;i<str1.length();i++){
            res+=str1.charAt(i)^str2.charAt(i);
        }
        return  res;
    }
}