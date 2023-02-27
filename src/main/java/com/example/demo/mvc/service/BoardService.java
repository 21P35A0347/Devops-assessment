package com.example.demo.mvc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.framework.domain.PageRequestParameter;
import com.example.demo.mvc.domain.Board;
import com.example.demo.mvc.param.BoardParameter;
import com.example.demo.mvc.param.BoardSearchParameter;
import com.example.demo.mvc.repository.BoardRepository;


/**
 * 게시판 서비스
 * @author arsurei
 *
 */
@Service
public class BoardService {
	
	@Autowired
	private BoardRepository repository;
	
	/**
	 * 목록 조회
	 * @return
	 */
	public List<Board> getList(PageRequestParameter<BoardSearchParameter> param){
		return repository.getList(param);
	}
	
	/**
	 * 상세 조회
	 * @param boardSeq 게시물ID
	 * @return 
	 */
	public Board get(int boardSeq) {
		return repository.get(boardSeq);
	}
	
	/**
	 * 등록처리
	 * @param board
	 */
	public void save(BoardParameter param) {
		Board board = repository.get(param.getBoardSeq());
		if (board == null) {
			repository.save(param);			
		} else {
			repository.update(param);
		}
	}
	
	/**
	 * 단순반복
	 * @param list
	 */
	public void saveList1(List<BoardParameter> list) {
		for (BoardParameter boardParameter : list) {
			repository.save(boardParameter);
		}
	}
	
	/**
	 * 일괄 삽입
	 * @param boardMap
	 */
	public void saveList2(List<BoardParameter> list) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("boardList", list);
		repository.saveList(paramMap);
	}

	/**
	 * 삭제 처리
	 * @param boardSeq
	 */
	public void delete(int boardSeq) {
		repository.delete(boardSeq);
	}
}
