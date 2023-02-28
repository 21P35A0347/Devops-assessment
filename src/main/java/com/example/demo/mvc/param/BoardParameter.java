package com.example.demo.mvc.param;

import com.example.demo.mvc.domain.BoardType;

import lombok.Data;

@Data
public class BoardParameter {
	
	private int boardSeq;
	private BoardType boardType;
	private String title;
	private String contents;
	
	public BoardParameter() {
		
	}
	
	public BoardParameter(String title, String contens) {
		this.title = title;
		this.contents = contens;
	}
}
