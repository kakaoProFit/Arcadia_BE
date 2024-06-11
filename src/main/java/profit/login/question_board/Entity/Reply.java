package profit.login.question_board.Entity;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.bson.types.ObjectId;
import profit.login.entity.User;
import profit.login.question_board.Entity.BaseEntity;
import profit.login.question_board.Entity.Board;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private String documentId;

    private String body;

    private boolean selected;       // 채택여부

    @OneToMany(mappedBy = "reply",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Like> likes;       // 좋아요
    private Integer likeCnt;        // 좋아요 수

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;      // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Board board;    // 답글이 달린 게시판

    private String nickname;


    public void update(String newBody) {
        this.body = newBody;
    }

    public void likeChange(Integer likeCnt) {
        this.likeCnt = likeCnt;
    }

    public void select() {
        this.selected = true;
    }

    public void deselect() {
        this.selected = false;
    }
}
