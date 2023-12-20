package course.nosql.controller;

import course.nosql.reality.Category;
import course.nosql.reality.Result;
import course.nosql.service.CategotyService;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategotyService categotyService;

    @PostMapping
    public Result add(@RequestBody @Validated() Category category){
        categotyService.add(category);
        return Result.success();
    }
    @GetMapping("/all")
    public Result<List<Category>> listall(){
        List<Category>list=categotyService.listall();
        return Result.success(list);

    }

    @GetMapping
    public Result<List<Category>> list(){
        List<Category> list=categotyService.list();
        return Result.success(list);

    }
    @PutMapping
    public Result update(@RequestBody  @Validated(Category.Update.class) Category category){
        categotyService.update(category);
        return Result.success();
    }
    @DeleteMapping
    public Result delete(@RequestParam(required = true) String id){
        categotyService.delete(id);
        return Result.success();
    }
    @GetMapping("/detail")
    public Result<Category> detail(/*@Param("id")*/ String id){
         Category category= categotyService.findById(id);
         return Result.success(category);
    }

}
