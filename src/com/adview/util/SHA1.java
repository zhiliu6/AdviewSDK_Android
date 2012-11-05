package com.adview.util;

import java.security.MessageDigest;

public class SHA1 {

	public static String SHA2(String input){
		 MessageDigest md=null;
		 String strDes=null;
		 try{
			 md=MessageDigest.getInstance("sha-1");
			 md.update(input.getBytes());
			 strDes=bytes2Hex(md.digest());
		 }catch(Exception e){
			 e.printStackTrace();
			 return null;
		 }
		 return strDes;
	}

	
	private static String bytes2Hex(byte[]bts) {
        String des="";
        String tmp=null;
        for (int i=0;i<bts.length;i++) {
                   tmp=(Integer.toHexString(bts[i] & 0xFF));
                   if (tmp.length()==1) {
                       des+="0";
                   }
                   des+=tmp;
               }
               return des;
       }
}

