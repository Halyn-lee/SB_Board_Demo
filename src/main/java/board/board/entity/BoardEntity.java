package board.board.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
// 해당 클래스가 JPA 엔티티임을 나타냄
@Table(name="t_jpa_board")
// t_jpa_board 테이블과 매핑
@NoArgsConstructor
@Data
public class BoardEntity {
    @Id // 엔티티의 기본키임을 나타냄
    @GeneratedValue(strategy=GenerationType.AUTO)
    // 기본키 생성 전략. MySQL은 자동증가 (오라클은 자동증가가 지원되지 않으므로 시퀀스를 생성)
    private int boardIdx;

    @Column(nullable=false)
    // 컬럼에 Not NULL 속성 지정
    private String title;

    @Column(nullable=false)
    private String contents;

    @Column(nullable=false)
    private int hitCnt = 0;

    @Column(nullable=false)
    private String creatorId;

    @Column(nullable=false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    /* 날짜 형식 지정. @JsonFormat은 Jackson 라이브러리에서 제공하는 어노테이션으로 JSON 응답값의 형식을 지정할 때 사용
    스프링 부트 스타터 패키지에 Jackson 라이브러리가 포함되어 있음 */
    private LocalDateTime createdDatetime = LocalDateTime.now();
    // 작성시간의 초깃값 설정

    private String updaterId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updatedDatetime;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    // 1:N 관계를 표현하는 JPA 어노테이션. (e.g. 하나의 게시글은 첨부파일이 없거나 1개 이상의 첨부파일을 가질 수 있음)
    @JoinColumn(name="board_idx")
    // 릴레이션 관계가 있는 테이블의 컬럼을 지정
    private Collection<BoardFileEntity> fileList;
    // fileList 필드를 데이터베이스의 board_idx 컬럼과 매핑하도록 지정
}
