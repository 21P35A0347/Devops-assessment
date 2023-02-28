package com.example.demo.mvc.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.demo.framework.domain.PageRequestParameter;
import com.example.demo.mvc.domain.Board;
import com.example.demo.mvc.param.BoardParameter;
import com.example.demo.mvc.param.BoardSearchParameter;

/**
 * 게시판 Repository
 * @author arsurei
 *
 */
@Repository
public interface BoardRepository {

	List<Board> getList(PageRequestParameter<BoardSearchParameter> param);
	
	Board get(int boardSeq);
	
	void save(BoardParameter board);
	
	void saveList(Map<String, Object> boardMap);
	
	void update(BoardParameter board);
	
	void delete(int boardSeq);
	
}

