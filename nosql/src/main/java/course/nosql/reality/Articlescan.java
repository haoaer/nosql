package course.nosql.reality;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("articlescan")
public class Articlescan {


    private String id;//主键ID

    private String count;


}
