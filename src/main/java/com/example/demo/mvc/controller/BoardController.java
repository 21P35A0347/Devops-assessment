package com.example.demo.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.configuration.Exception.BaseException;
import com.example.demo.configuration.http.BaseResponse;
import com.example.demo.configuration.http.BaseResponseCode;
import com.example.demo.framework.domain.MySqlPageRequest;
import com.example.demo.framework.domain.PageRequestParameter;
import com.example.demo.framework.web.annotation.RequestConfig;
import com.example.demo.mvc.domain.Board;
import com.example.demo.mvc.param.BoardParameter;
import com.example.demo.mvc.param.BoardSearchParameter;
import com.example.demo.mvc.service.BoardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/board")
@Api(tags = "게시판 API 테스트")
public class BoardController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private BoardService boardService;
	
	/**
	 * 목록 조회
	 * @return
	 */
	@GetMapping
	@ApiOperation(value = "게시물 목록 조회", notes = "게시물 리스트 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "keyword", value = "검색어", example = ""),
		@ApiImplicitParam(name = "boardTypes", value = "검색타입(NOTICE,FAQ,INQUIRY)을 적음, 복수의 경우 컴마 구분", example = ""),

	})
	public BaseResponse<List<Board>> getList(BoardSearchParameter param, MySqlPageRequest pageRequest) {
		logger.info("pageRequest : {}", pageRequest);
		PageRequestParameter<BoardSearchParameter> pageRequestParameter = new PageRequestParameter<BoardSearchParameter>(pageRequest, param);
		
		return new BaseResponse<List<Board>>(boardService.getList(pageRequestParameter));
	}
	
	/**
	 * 상세 조회
	 * @param boardSeq 게시물ID
	 * @return 
	 */
	@GetMapping("/{boardSeq}")
	@ApiOperation(value = "게시물 상세 조회", notes = "게시물 번호에 해당하는 게시물 정보 조회")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "boardSeq", value = "게시물 번호", example = "1", required = true)
	})
	public BaseResponse<Board> get(@PathVariable int boardSeq) {
		Board board = boardService.get(boardSeq);
		if (board == null) {
			throw new BaseException(BaseResponseCode.DATA_IS_NULL, new String[] {"게시판"});
			
		}
		return new BaseResponse<Board>(board);
	}
	
	/**
	 * 등록/수정 처리
	 * @param boardParam
	 */
	@PutMapping
	@RequestConfig(loginCheck = true)
	@ApiOperation(value = "게시물 작성 / 수정", notes = "게시물을 작성 / 수정")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "boardSeq", value = "게시물 ID", example = "1"),
		@ApiImplicitParam(name = "title", value = "게시물 제목", example = "게시물 제목", required = true),
		@ApiImplicitParam(name = "contents", value = "게시물 내용", example = "게시물 내용", required = true)
	})
	public BaseResponse<Integer> save(BoardParameter boardParam) {
		if (StringUtils.isEmpty(boardParam.getTitle())) {
			throw new BaseException(BaseResponseCode.VALIDATE_REQUIRED, new String[] { "title", "제목" });
		}
		if (StringUtils.isEmpty(boardParam.getContents())) {
			throw new BaseException(BaseResponseCode.VALIDATE_REQUIRED, new String[] { "contents", "내용" });
		}
		boardService.save(boardParam);
		return new BaseResponse<Integer>(boardParam.getBoardSeq());
	}
	
	/**
	 * 대량등록(대량 커넥션 오픈)
	 * @param boardParam
	 */
	@PutMapping("/saveList1")
	@ApiOperation(value = "대량 등록 처리1", notes = "대량 등록 처리, 등록당 컨낵션 발생")
	public BaseResponse<Boolean> saveList1() {
		
		List<BoardParameter> list = new ArrayList<BoardParameter>();
		int count = 0;
		while(true) {
			count++;
			String title = RandomStringUtils.randomAlphabetic(10);
			String contents = RandomStringUtils.randomAlphabetic(20);
			list.add(new BoardParameter(title, contents));
			if (count >= 10000) {
				break;
			}
		}
		long start = System.currentTimeMillis();
		boardService.saveList1(list);
		long end = System.currentTimeMillis();
		logger.info("실행시간 : {}", (end - start) / 1000);
		
		return new BaseResponse<Boolean>(true);
	}
	
	/**
	 * 대량등록(커넥션 오픈 1회)
	 * @param boardParam
	 */
	@PutMapping("/saveList2")
	@ApiOperation(value = "대량 등록 처리1", notes = "대량 등록 처리, 등록당 컨낵션 발생")
	public BaseResponse<Boolean> saveList2() {
		
		List<BoardParameter> list = new ArrayList<BoardParameter>();
		int count = 0;
		while(true) {
			count++;
			String title = RandomStringUtils.randomAlphabetic(10);
			String contents = RandomStringUtils.randomAlphabetic(20);
			list.add(new BoardParameter(title, contents));
			if (count >= 10000) {
				break;
			}
		}
		long start = System.currentTimeMillis();
		boardService.saveList2(list);
		long end = System.currentTimeMillis();
		logger.info("실행시간 : {}", (end - start) / 1000);
		
		return new BaseResponse<Boolean>(true);
	}

	/**
	 * 삭제 처리
	 * @param boardSeq
	 */
	@DeleteMapping("/{boardSeq}")
	@RequestConfig(loginCheck = true)
	@ApiOperation(value = "게시물 삭제", notes = "게시물 번호에 해당하는 게시물 삭제")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "boardSeq", value = "게시물 번호", example = "1")
	})
	public BaseResponse<Boolean> delete(@PathVariable int boardSeq) {
		Board board = boardService.get(boardSeq);
		if (board == null) {
			return new BaseResponse<Boolean>(false);
		}
		boardService.delete(boardSeq);
		return new BaseResponse<Boolean>(true);
	}
}
