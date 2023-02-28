package com.example.demo.mvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mvc.param.UploadFileParameter;
import com.example.demo.mvc.repository.UploadFileRepository;

/**
 * 
 * @author arsurei
 *
 */
@Service
public class UploadFileService {
	
	@Autowired
	private UploadFileRepository uploadFileRepository;
	
	public void save(UploadFileParameter uploadFileParameter) {
		this.uploadFileRepository.save(uploadFileParameter);
	}
}
