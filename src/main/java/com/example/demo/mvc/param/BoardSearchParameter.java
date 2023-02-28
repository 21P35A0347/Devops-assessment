package com.example.demo.mvc.param;

import java.util.List;

import com.example.demo.mvc.domain.BoardType;

import lombok.Data;

@Data
public class BoardSearchParameter {

	private String keyword;
	
	private List<BoardType> boardTypes;
	
	public BoardSearchParameter() {
		
	}
	
}