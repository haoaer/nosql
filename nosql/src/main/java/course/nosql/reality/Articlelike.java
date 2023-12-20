package course.nosql.reality;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("articlelike")
public class Articlelike {

    private String articleid;

    private String userid;

    private String value;

}
