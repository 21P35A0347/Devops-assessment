package com.example.demo.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.mvc.param.BoardSearchParameter;

@Controller
@RequestMapping("/taglib")
public class TaglibController {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping("search")
	public void search(BoardSearchParameter param, Model model) {
		model.ad 
		model.addAttribute(param);
	}
}
