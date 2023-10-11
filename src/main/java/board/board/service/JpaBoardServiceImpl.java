package board.board.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;
import board.board.common.FileUtils;
import board.board.repository.JpaBoardRepository;

@Service
public class JpaBoardServiceImpl implements JpaBoardService {

    @Autowired
    JpaBoardRepository jpaBoardRepository;

    @Autowired
    FileUtils fileUtils;

    @Override
    public List<BoardEntity> selectBoardList() throws Exception {
        return jpaBoardRepository.findAllByOrderByBoardIdxDesc();
        // 게시글 번호로 정렬하여 전체 게시글 목록 조회. 아직 리포지터리 작성 X
    }

    @Override
    public void saveBoard(BoardEntity board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        board.setCreatorId("admin");
        List<BoardFileEntity> list = fileUtils.parseFileInfo(multipartHttpServletRequest);
        // 첨부파일의 정보를 저장하는 클래스가 BoardFileDto에서 Entity로 변경되었으므로 FileUtils 클래스의 parseFileInfo 메서드를 새로 생성
        if(CollectionUtils.isEmpty(list) == false) {
            board.setFileList(list);
            /*첨부파일 목록을 BoardFileEntity 클래스에 추가. 이전에는 첨부파일 정보를 저장하는 쿼리를 따로 실행,
            여기서는 게시글 저장 시 해당 게시글에 포함된 첨부파일 목록도 자동으로 저장
            BoardFileEntity 클래스에 첨부파일 목록이 @OneTomany로 연관관계가 있기 때문 */
        }
        jpaBoardRepository.save(board);
        // 리포지터리의 save 메서드는 저장할 내용이 새로 생성되었을 경우에는 insert, 기존 내용에서 변경시 update 수행
    }

    @Override
    public BoardEntity selectBoardDetail(int boardIdx) throws Exception {
        Optional<BoardEntity> optional = jpaBoardRepository.findById(boardIdx);
        if(optional.isPresent()) {
            BoardEntity board = optional.get();
            board.setHitCnt(board.getHitCnt() + 1);
            jpaBoardRepository.save(board);

            return board;
        }
        else {
            throw new NullPointerException();
        }
    }
    /* JPA의 CrudRepository에서 제공하는 기능. 주어진 id를 가진 엔티티를 조회
    Optional 클래스는 어떤 객체의 값으로 Null이 아니기 때문에 NullPointerException을 방지
    isPresent 메서드를 이용하여 객체의 값이 존재하는지 확인하고 상황에 맞는 코드를 강제 (객체 값 존재하면 true 반환하고 get메서드로 객체 값을 가져올 수 있음)
    여기서는 게시글 번호가 잘못되었을 경우 NullPointerException을 발생시켰지만 해당 상황에 맞게 적절한 예외처리를 해 주어야 함
    */

    @Override
    public void deleteBoard(int boardIdx) throws Exception {
        jpaBoardRepository.deleteById(boardIdx);
        // 주어진 id를 가진 엔티티를 삭제
    }

    @Override
    public BoardFileEntity selectBoardFileInformation(int idx, int boardIdx) throws Exception {
        BoardFileEntity boardFile = jpaBoardRepository.findBoardFile(idx, boardIdx);
        return boardFile;
    }
}