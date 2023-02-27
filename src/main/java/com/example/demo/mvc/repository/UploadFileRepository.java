package com.example.demo.mvc.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.mvc.param.UploadFileParameter;

@Repository
public interface UploadFileRepository {
	/**
	 * 저장
	 * @param uploadFileParameter
	 */
	void save(UploadFileParameter uploadFileParameter);
}
