package com.company;

import java.io.IOException;
import java.nio.ByteBuffer;


public class GostNew {
    private static int rounds=32;
    private static int blockSize=64;
    private int[][] s=new int[8][];
    private String[] textBin;
    private String[] subKeys;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getTextBin() {
        return textBin;
    }

    public void setTextBin(String[] textBin) {
        this.textBin = textBin;
    }

    public String[] getSubKeys() {
        return subKeys;
    }

    public void setSubKeys(String[] subKeys) {
        this.subKeys = subKeys;
    }

    void setS(){
        s[0]=new int[] {4, 10, 9, 2, 13, 8, 0, 14, 6, 11, 1, 12, 7, 15, 5, 3};
        s[1] =new int[] {14, 11, 4, 12, 6, 13, 15, 10, 2, 3, 8, 1, 0, 7, 5, 9};
        s[2] =new int[] {5, 8, 1, 13, 10, 3, 4, 2, 14, 15, 12, 7, 6, 0, 9, 11};
        s[3] =new int[] {7, 13, 10, 1, 0, 8, 9, 15, 14, 4, 6, 12, 11, 2, 5, 3};
        s[4] =new int[] {6, 12, 7, 1, 5, 15, 13, 8, 4, 10, 9, 14, 0, 3, 11, 2};
        s[5] =new int[] {4, 11, 10, 0, 7, 2, 1, 13, 3, 6, 8, 5, 9, 12, 15, 14};
        s[6] =new int[] {13, 11, 4, 1, 3, 15, 5, 9, 0, 10, 14, 7, 6, 8, 2, 12};
        s[7] =new int[] {1, 15, 13, 0, 5, 7, 10, 4, 9, 2, 3, 14, 6, 11, 8, 12};
    }


    public GostNew(){ setS();}

    public int getBlockSize() {
        return blockSize;
    }


    /*public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }*/

    public int getRounds() {
        return rounds;
    }

    /*public void setRounds(int rounds) {
        this.rounds = rounds;
    }*/

    public String[] getBlocks(String info){
        return StringWorker.getBinStrArrayFromString(info,8);
    }

    public String doEncrypt(String aBin, String[] subKeys, boolean reverse) {
        String[] a_byte=new String[2];
        a_byte[0]=aBin.substring(0,aBin.length()/2);
        a_byte[1]=aBin.substring(aBin.length()/2);

        String func_res;
        int round = reverse? rounds: 1;
        String t;
        for (int i = 0; i < rounds; i++) {

            if (round < 25) // если не последний раунд
            {
                func_res = func(a_byte[1], subKeys[(round-1)%subKeys.length]);

            }
            else {
                func_res = func(a_byte[1],subKeys[subKeys.length-1-(round)%subKeys.length]);
            }
            t = String.copyValueOf(a_byte[1].toCharArray());

            long tmpR=Long.valueOf(a_byte[0],2)^ Long.valueOf(func_res,2);
            a_byte[1]=String.format("%32s", Long.toBinaryString(tmpR)).replace(' ', '0');
            a_byte[0] = String.copyValueOf(t.toCharArray());

            round += reverse ? -1: 1;
        }

        return a_byte[1]+a_byte[0];
    }

    public String func(String b, String subKey) {
        Long Ri= Long.parseLong(b,2);
        Long subKeyL= Long.parseLong(subKey,2);
        Long RiSubKey=(Ri+subKeyL);
        RiSubKey%=4294967296L;
        //NEED TO ADD S;
        String res=String.format("%32s",Long.toBinaryString(RiSubKey)).replace(' ', '0');
        StringBuffer newRes=new StringBuffer();
        int index;
        for (int i=0;i<s.length;i++){
            index=s[i][Integer.parseInt(res.substring(i*4,(i+1)*4),2)];
            newRes.append(String.format("%4s",Integer.toBinaryString(index)).replace(' ','0'));
        }
        //RiSubKey<<=11;
        res=doShift(res.toString(),-11);
        return res;
    }

    public String doShift(String str, int shift){
        StringBuffer res=new StringBuffer(str);
        shift= shift>0 ? shift: str.length()+shift;
        for (int i=0;i<shift;i++){
            res.insert(0,res.charAt(res.length()-1));
            res.deleteCharAt(res.length()-1);
        }
        return res.toString();
    }



    public String[] getSubKeyFirst(String key) {
        subKeys=new String[8];
        for (int i=0;i<subKeys.length;i++){
            subKeys[i]=key.substring(i*32,(i+1)*32);
        }
        return subKeys;
    }

    public String doGost(String fileName,String keyFile, boolean flag){
        FileService fileService=new FileService();
        try {
            setText(fileService.getInfoFromFile(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String key= null;
        try {
            key = fileService.getInfoFromFile(keyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getSubKeyFirst(key);

        System.out.println("Исходный текст");
        System.out.println(getText());
        //System.out.println("KEY: "+key);

        String[] blocks=getBlocks(getText());
        String[] encr=new String[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            encr[i] = doEncrypt(blocks[i], subKeys, flag);
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

    public String doDaviesPrice(String fileName,String keyFile, String keyFile2, boolean flag){
        FileService fileService=new FileService();
        try {
            setText(fileService.getInfoFromFile(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String key= null;
        try {
            key = fileService.getInfoFromFile(keyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] subKeys1=getSubKeyFirst(key);

        try {
            key = fileService.getInfoFromFile(keyFile2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] subKeys2=getSubKeyFirst(key);

        System.out.println("Исходный текст");
        System.out.println(getText());
        //System.out.println("KEY: "+key);

        String[] blocks=getBlocks(getText());
        String[] encr=new String[blocks.length];
        encr[0] = doEncrypt(blocks[0], subKeys2, flag);
        for (int i = 1; i < blocks.length; i++) {
            encr[i] = doEncrypt(StringWorker.doXorBinaryStrings(blocks[i],doEncrypt(encr[i-1], subKeys1, flag)),subKeys2,flag);
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