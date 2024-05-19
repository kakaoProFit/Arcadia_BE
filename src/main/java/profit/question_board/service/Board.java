package profit.question_board.service;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import profit.login.dto.LoginUserDto;
import profit.login.entity.User;
import profit.question_board.Entity.BaseEntity;
import profit.login.entity.Like;
import profit.login.entity.Comment;
import profit.question_board.Entity.UploadImage;
import profit.question_board.dto.BoardDto;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String title;   // 제목
    private String body;    // 본문

    @Enumerated(EnumType.STRING)
    private BoardCategory category; // 카테고리

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;      // 작성자

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Like> likes;       // 좋아요
    private Integer likeCnt;        // 좋아요 수

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments; // 댓글
    private Integer commentCnt;     // 댓글 수

    @OneToOne(fetch = FetchType.LAZY)
    private UploadImage uploadImage;

    public void update(BoardDto dto) {
        this.title = dto.getTitle();
        this.body = dto.getBody();
    }

    public void likeChange(Integer likeCnt) {
        this.likeCnt = likeCnt;
    }

    public void commentChange(Integer commentCnt) {
        this.commentCnt = commentCnt;
    }

    public void setUploadImage(UploadImage uploadImage) {
        this.uploadImage = uploadImage;
    }

}