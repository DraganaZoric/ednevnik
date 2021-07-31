package com.iktpreobuka.ednevnik.services;

import java.io.File;
import org.springframework.stereotype.Service;


@Service
public class FileHandlerImpl implements FileHandler {

	public File downloadLog() {
		String path = "C:\\Users\\Korisnik\\Documents\\workspace-spring-tool-suite-4-4.10.0.RELEASE\\EDnevnik\\logs\\spring-boot-logging.log";
		File logFile = new File(path);
		return logFile;
	}

}
