package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        GostNew gostNew = new GostNew();

        //FileService fileService=new FileService();

        //ENCR
        System.out.println("Gost");
        System.out.println("Encrypting");
        String encr=gostNew.doGost("text.txt","key.txt",false);
        FileService.saveInfoToFile("res.txt", encr);
        //DECR
        System.out.println("\nDecrypting");
        String decr=gostNew.doGost("res.txt","key.txt",true);
        FileService.saveInfoToFile("decr.txt", decr);


        ///DO Davies-Price
        System.out.println("\nDavies-Price");
        System.out.println("Encrypting");
        encr=gostNew.doDaviesPrice("text.txt","key.txt","key2.txt",false);
        FileService.saveInfoToFile("resD.txt", encr);

        System.out.println("\nDecrypting");
        decr=gostNew.doDaviesPrice("resD.txt","key.txt","key2.txt",true);
        FileService.saveInfoToFile("decrD.txt", decr);

        // OBNLF
        System.out.println("\nOBNLF");
        System.out.println("Encrypting");
        encr=OFBNLF.doOFBNLF("text.txt","key.txt",false);
        FileService.saveInfoToFile("resD.txt", encr);

        System.out.println("\nDecrypting");
        decr=OFBNLF.doOFBNLF("resD.txt","key.txt",true);
        FileService.saveInfoToFile("decrD.txt", decr);

    }
}
