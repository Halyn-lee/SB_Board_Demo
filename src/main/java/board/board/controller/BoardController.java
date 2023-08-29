package board.board.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import board.board.dto.BoardFileDto;
import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import board.board.dto.BoardDto;
import board.board.service.BoardService;

import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class BoardController {
    //private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private BoardService boardService;

    @RequestMapping("/board/openBoardList.do")
    public ModelAndView openBoardList() throws Exception{
        // log.debug("openBoardList");
        // @Slf4j 어노테이션을 사용하면 로거를 따로 생성할 필요 없음
        ModelAndView mv = new ModelAndView("/board/boardList");
        // int i = 10 / 0; 에러 처리 화면 확인용
        List<BoardDto> list = boardService.selectBoardList();
        mv.addObject("list", list);

        return mv;
    }

    @RequestMapping("/board/openBoardWrite.do")
    public String openBoardWrite() throws Exception{
        return "/board/boardWrite";
    }

    @RequestMapping("/board/insertBoard.do")
    public String insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{

        boardService.insertBoard(board, multipartHttpServletRequest);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping("/board/openBoardDetail.do")
    public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception{
        ModelAndView mv = new ModelAndView("/board/boardDetail");

        BoardDto board = boardService.selectBoardDetail(boardIdx);
        mv.addObject("board", board);

        return mv;
    }

    @RequestMapping("/board/updateBoard.do")
    public String updateBoard(BoardDto board) throws Exception{
        boardService.updateBoard(board);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping("/board/deleteBoard.do")
    public String deleteBoard(int boardIdx) throws Exception{
        boardService.deleteBoard(boardIdx);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping("/board/downloadBoardFile.do")
    public void downloadBoardFile(@RequestParam int idx, @RequestParam int boardIdx, HttpServletResponse response) throws Exception{
        // HttpServletRequest는 사용자로부터 들어오는 모든 요청 데이터, HttpServletResponse는 사용자에게 전달할 데이터
        BoardFileDto boardFile = boardService.selectBoardFileInformation(idx, boardIdx);
        // DB에서 선택된 파일 정보 조회
        if(ObjectUtils.isEmpty(boardFile) == false) {
            String fileName = boardFile.getOriginalFileName();

            byte[] files = FileUtils.readFileToByteArray(new File(boardFile.getStoredFilePath()));
            //조회한 파일 정보 중 저장 위치인 storedFilePath 값을 이용해 실제 저장된 파일을 읽어온 후 byte[] 형태로 변환

            response.setContentType("application/octet-stream");
            response.setContentLength(files.length);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
            response.setHeader("Content-Transfer-Encoding", "binary");
            // reseponse의 헤더에 콘텐츠 타입, 크기 및 형태 등을 설정

            response.getOutputStream().write(files);
            // 읽어온 파일 정보의 byte[] 데이터를 response에 작성
            response.getOutputStream().flush();
            response.getOutputStream().close();
            // reponse의 buffer를 정리하고 닫음
        }
    }
}