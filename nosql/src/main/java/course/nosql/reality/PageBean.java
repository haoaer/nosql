package course.nosql.reality;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean<T> {
    private Long total; //总数
    private List<T>items;  // 文章列表
}
