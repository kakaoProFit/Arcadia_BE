package profit.arcadia.mapper;


import org.apache.ibatis.annotations.Mapper;
import profit.arcadia.vo.UserVo;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserVo> getUserList();
}