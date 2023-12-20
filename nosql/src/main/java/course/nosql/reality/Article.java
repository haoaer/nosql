package course.nosql.reality;


import com.fasterxml.jackson.annotation.JsonFormat;
import course.nosql.anno.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Data
@Document("article")
public class Article {
    @NotNull(groups = genxin.class)
    private String id;//主键ID
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String title;//文章标题
    @NotEmpty
    private String content;//文章内容
    @URL
    @NotEmpty
    private String coverImg;//封面图像
    @State
    @NotEmpty
    private String state;//发布状态 已发布|草稿
    @NotNull
    private String categoryId;//文章分类id
    private String createUser;//创建人ID
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间

    // !! 现在是为了内连接使用,实际数据库并没有这个字段(MYSQL版本)
    private String categoryName;

    //以下为了完成分组校验  不是实体-！
    public interface genxin extends Default {

    }
    public interface Add extends Default{

    }
}
