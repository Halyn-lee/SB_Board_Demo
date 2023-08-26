package board.board.service;

import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import board.board.dto.BoardDto;
import board.board.mapper.BoardMapper;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService{

    @Autowired
    private BoardMapper boardMapper;

    @Override
    public List<BoardDto> selectBoardList() throws Exception {
        return boardMapper.selectBoardList();
    }

    @Override
    public void insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        //boardMapper.insertBoard(board); 업로드 파일 정보 확인 목적으로 게시글 저장 x
        if(ObjectUtils.isEmpty(multipartHttpServletRequest) == false){
            Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
            String name;
            while(iterator.hasNext()){
                name = iterator.next();
                log.debug("file tag name : " + name);
                List<MultipartFile> list = multipartHttpServletRequest.getFiles(name);
                for(MultipartFile multipartFile : list){
                    log.debug("start file information");
                    log.debug("file name : " + multipartFile.getOriginalFilename());
                    log.debug("file size : " + multipartFile.getSize());
                    log.debug("file content type : " + multipartFile.getContentType());
                    log.debug("END FILE INFORMATION.\n");
                }
            }
        }
    }

    @Override
    public BoardDto selectBoardDetail(int boardIdx) throws Exception{
        BoardDto board = boardMapper.selectBoardDetail(boardIdx);
        boardMapper.updateHitCount(boardIdx);
        // int i = 10 / 0; 트랜잭션 설정 적용 확인 용도였음 (조회수 카운트 예외)
        return board;
    }

    @Override
    public void updateBoard(BoardDto board) throws Exception {
        boardMapper.updateBoard(board);
    }

    @Override
    public void deleteBoard(int boardIdx) throws Exception {
        boardMapper.deleteBoard(boardIdx);
    }
}
