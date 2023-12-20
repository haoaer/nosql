package course.nosql.reality;

import lombok.Data;

//点赞
@Data
public class Like {
    private int like; //点赞总数
    private boolean tag;  //是否点赞
}
