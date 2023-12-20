package course.nosql.controller;

import course.nosql.reality.Result;
import course.nosql.util.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        String orginname= file.getOriginalFilename();
        //保证文件名字唯一
        String filename= UUID.randomUUID().toString()+orginname.substring(orginname.lastIndexOf("."));
        String url= AliOssUtil.upLoad(filename,file.getInputStream());
        return Result.success(url);
    }
}
