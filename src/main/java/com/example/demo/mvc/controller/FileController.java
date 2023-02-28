package com.example.demo.mvc.controller;

import java.io.File;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.configuration.GlobalConfig;
import com.example.demo.configuration.Exception.BaseException;
import com.example.demo.configuration.conf.GlobalProperties;
import com.example.demo.configuration.http.BaseResponse;
import com.example.demo.configuration.http.BaseResponseCode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/file")
@Api(tags = "파일 API")
public class FileController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private GlobalConfig config;
	
	@PostMapping("/save")
	@ApiOperation(value = "업로드", notes = "")
	public BaseResponse<Boolean> save(@RequestParam("uploadFile") MultipartFile multipartFile) {
		logger.debug("MultipartFile : {}", multipartFile);
		String uploadFilePath = config.getProperties(GlobalProperties.UPLOAD_PROPERTIES);
		
		if (multipartFile == null || multipartFile.isEmpty()) {
			throw new BaseException(BaseResponseCode.DATA_IS_NULL);
		}
		logger.debug("uploadFilePath : {}", uploadFilePath);
		if (config.getProperties(GlobalProperties.MODE) == "prod" ) {
			logger.debug("Process Mode : prod");
		}
		String prefix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1, multipartFile.getOriginalFilename().length());
		String filename = UUID.randomUUID().toString() + "." + prefix;
		
		File folder = new File(uploadFilePath);
		if (!folder.isDirectory()) {
			folder.mkdirs();
		}
		
		String pathname = uploadFilePath + filename;
		File dest = new File(pathname);
		
		
		try {
			multipartFile.transferTo(dest);
		} catch (Exception e) {
			logger.error("Uploade File Exception: {}", e);
		}
		
		
		logger.debug("uploadFilePath : {}", uploadFilePath);
		return new BaseResponse<Boolean>(true);
	}
}
