package org.slevin.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slevin.common.Emlak;

public class FileUtil {
	//public static String fileName = "C:\\Users\\ETR00529\\Desktop\\sahibinden\\cloudData\\prediction.cvs";
	public static String fileName = "/export/prediction.cvs";
	
public static void appendToFile(Emlak emlak) throws IOException{
	File file =new File(fileName);
	FileWriter fileWritter = new FileWriter(file,true);
    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
    bufferWritter.write(ConvertUtil.convertCVSFormat(emlak));
    bufferWritter.newLine();
    bufferWritter.flush();
    bufferWritter.close();

}


public static void resetFile() throws IOException{
	File file =new File(fileName);
	
	//if file doesnt exists, then create it
	if(file.exists()){
		file.delete();
		
	}
	if(!file.exists()){
		file.createNewFile();
	}
}
public static void main(String[] args) throws IOException {
	resetFile();
}
}
