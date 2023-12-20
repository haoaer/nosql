package course.nosql.reality;



import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("user")
public class User {
    @NotNull
    private String id;//主键ID

    private String username;//用户名
    private String password;//密码
    @NotEmpty
    @Pattern(regexp = "^\\S{2,16}$")
    private String nickname;//昵称
    @NotEmpty
    @Email
    private String email;//邮箱
    private String userPic;//用户头像地址
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间
}
