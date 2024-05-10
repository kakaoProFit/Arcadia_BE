package profit.login.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;


/**
 * @Id : 키 값이 되며, 후술할 @RedisHash - value 에 prefix에 덧붙여져 위 예제의 경우 refresh_token:{id} 형태로 형성된다.
 * @RedisHash : value 속성을 통해 설정한 값을 키 값 prefix로 사용한다. timeToLive 속성을 사용할 수도 있다.
 * @Indexed : 해당 값으로 검색을 할 시에 추가한다.
 * @TimeToLive : 만료시간을 설정한다.(초 단위)
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@RedisHash(value = "token", timeToLive = 60*60*24*7)//7일
public class TokenRedis {
    @Id
    private Integer authId;

    @Indexed
    private String accesstoken;

    private String refreshtoken;


}
