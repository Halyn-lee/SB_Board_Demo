package board.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import board.board.entity.BoardEntity;
import board.board.entity.BoardFileEntity;

public interface JpaBoardRepository extends CrudRepository<BoardEntity, Integer>{
// 도메인 클래스인 BoardEntity와 id타입인 Integer를 파라미터로 받음
    List<BoardEntity> findAllByOrderByBoardIdxDesc();
    // 게시글 번호로 정렬하여 전체 게시글을 조회

    @Query("SELECT file FROM BoardFileEntity file WHERE board_idx = :boardIdx AND idx = :idx")
    // 첨부파일 정보 조회
    BoardFileEntity findBoardFile(@Param("boardIdx") int boardIdx, @Param("idx") int idx);
}