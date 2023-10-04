package board.board.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="t_jpa_file")
// t_jpa_file 테이블과 매핑
@NoArgsConstructor
@Data
public class BoardFileEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int idx;

    @Column(nullable=false)
    private String originalFileName;

    @Column(nullable=false)
    private String storedFilePath;

    @Column(nullable=false)
    private long fileSize;

    @Column(nullable=false)
    private String creatorId;

    @Column(nullable=false)
    private LocalDateTime createdDatetime = LocalDateTime.now();

    private String updaterId;

    private LocalDateTime updatedDatetime;
}