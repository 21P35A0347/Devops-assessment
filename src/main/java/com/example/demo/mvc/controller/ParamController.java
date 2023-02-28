
package com.example.demo.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.mvc.param.ExampleParam;
import com.example.demo.mvc.param.ExampleParam2Parent;

@Controller
@RequestMapping("/example")
public class ParamController {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/example1")
	public void example1(@RequestParam String id, @RequestParam String code, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("code", code);
	}
	
	@GetMapping("/example2")
	public void example2(@RequestParam Map<String, Object> paramMap, Model model) {
		model.addAttribute("paramMap", paramMap);
	}
	
	@GetMapping("/example3")
	public void example3(ExampleParam exampleParam, Model model) {
		model.addAttribute("paramter", exampleParam);
	}
	
	@GetMapping("/example4/{id}/{code}")
	public String example4(@PathVariable String id, @PathVariable String code, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("code", code);
		
		return "/example/example4";
	}
	
	@GetMapping("/example5")
	public void example5(@RequestParam String[] ids, Model model) {
		model.addAttribute("ids", ids);
	}
	@GetMapping("/example6/form")
	public void form() {
	}
	
	@PostMapping("/example6/save")
	@ResponseBody
	public Map<String, Object> example6(@RequestBody Map<String, Object> requestBody) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", requestBody);
		logger.info("requestBody : {}", requestBody);
		return result;
	}
	
	@GetMapping("/example6/form2")
	public void form1() {
	}
	
	@PostMapping("/example6/save2")
	@ResponseBody
	public Map<String, Object> example7(@RequestBody ExampleParam2Parent exampleParam2Parent) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", exampleParam2Parent);
		logger.info("requestBody : {}", exampleParam2Parent);
		return result;
	}
}
