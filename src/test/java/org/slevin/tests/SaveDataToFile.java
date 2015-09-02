package org.slevin.tests;

import java.io.File;
import java.io.IOException;

public class SaveDataToFile {

	public static void main(String[] args) throws IOException {
		
		
		File file =new File("c:\\sahijavaio-appendfile.txt");
		
		//if file doesnt exists, then create it
		if(!file.exists()){
			file.createNewFile();
		}
	}
}
