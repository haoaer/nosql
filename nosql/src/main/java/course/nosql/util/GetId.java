package course.nosql.util;

import java.text.SimpleDateFormat;

public class GetId {

    //更具时间生成一个ID
    public static String GetUUId(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
        String id = sdf.format(System.currentTimeMillis());
        return id;
    }

}
