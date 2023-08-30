package board.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import board.board.dto.BoardDto;
import board.board.service.BoardService;

@RestController
// @Controller + @ResponseBody, @RestController는 결괏값을 JSON 형식으로 만들어 줌
public class RestBoardApiController {

    @Autowired
    private BoardService boardService;

    @RequestMapping(value="/api/board", method=RequestMethod.GET)
    public List<BoardDto> openBoardList() throws Exception{
        return boardService.selectBoardList();
    }
    /* 기존에 ModelAndView 클래스에 게시글 목록 조회 결과를 담아 뷰에 보냈던 것과 달리
    조회 결과를 바로 API의 응답 결과로 사용 */

    @RequestMapping(value="/api/board/write", method=RequestMethod.POST)
    public void insertBoard(@RequestBody BoardDto board) throws Exception{
        boardService.insertBoard(board, null);
    }
    /* REST API 기본 사항 알아보는 중이라 첨부파일은 일단 안 받도록 */

    @RequestMapping(value="/api/board/{boardIdx}", method=RequestMethod.GET)
    public BoardDto openBoardDetail(@PathVariable("boardIdx") int boardIdx) throws Exception{

        return boardService.selectBoardDetail(boardIdx);
    }

    @RequestMapping(value="/api/board/{boardIdx}", method=RequestMethod.PUT)
    public String updateBoard(@RequestBody BoardDto board) throws Exception{
        boardService.updateBoard(board);
        return "redirect:/board";
    }
    /* @RequestBody는 메서드의 파라미터가 반드시 HTTP 패킷의 바디에 담겨있어야 함을 나타냄
     POST나 PUT을 사용하는 메서드에는 @RequestBody를 사용해야 함
     GET은 @RequestParam */

    @RequestMapping(value="/api/board/{boardIdx}", method=RequestMethod.DELETE)
    public String deleteBoard(@PathVariable("boardIdx") int boardIdx) throws Exception{
        boardService.deleteBoard(boardIdx);
        return "redirect:/board";
    }
}