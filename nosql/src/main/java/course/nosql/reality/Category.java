package course.nosql.reality;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.InterfaceAddress;
import java.time.LocalDateTime;

@Data
@Document("category")
public class Category {
    @NotNull(groups = Update.class)
    @Id
    private String id;//主键ID
    @NotEmpty
    private String categoryName;//分类名称
    @NotEmpty
    private String categoryAlias;//分类别名
    private String createUser;//创建人ID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间


    //分组校验
    public interface Add extends Default {

    }
    public interface Update extends Default{

    }

}
