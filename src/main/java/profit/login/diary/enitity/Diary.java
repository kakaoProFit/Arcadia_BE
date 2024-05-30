/*
package profit.login.diary.enitity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import profit.login.entity.User;
import profit.login.question_board.Entity.*;
import profit.login.question_board.dto.BoardDto;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;   // 제목

    @Enumerated(EnumType.STRING)
    private BoardCategory category; // 카테고리


    @ManyToOne(fetch = FetchType.LAZY)
    private User user;      // 작성자

    @OneToMany(mappedBy = "diary", orphanRemoval = true)
    private List<Like> diary_likes;       // 좋아요
    private Integer diary_likeCnt;        // 좋아요 수

    @OneToMany(mappedBy = "diary", orphanRemoval = true)
    private List<Like> diary_reports;   // 신고
    private Integer diary_reportsCnt;   // 신고 수

    @OneToMany(mappedBy = "diary", orphanRemoval = true)
    private List<Comment> diary_comments; // 댓글
    private Integer diary_commentCnt;     // 댓글 수

    @OneToOne(fetch = FetchType.LAZY)
    private UploadImage diary_uploadImage;

    public void update(BoardDto dto) {
        this.title = dto.getTitle();
    }

    public void likeChange(Integer likeCnt) {
        this.diary_likeCnt = likeCnt;
    }

    public void commentChange(Integer commentCnt) {
        this.diary_commentCnt = commentCnt;
    }

    public void setUploadImage(UploadImage uploadImage) {
        this.diary_uploadImage = uploadImage;
    }

}*/
