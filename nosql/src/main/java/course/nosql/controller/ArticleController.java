package course.nosql.controller;

import course.nosql.reality.*;
import course.nosql.service.ArticleService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
   @Autowired
   private ArticleService articleService;
   @PostMapping
   public Result add(@RequestBody @Validated Article article){
      articleService.add(article);
      return Result.success();
   }

   @GetMapping
   public Result<PageBean<Article>> list(
           Integer pageNum,
           Integer pageSize,
           @RequestParam(required = false) String categoryId,
           @RequestParam(required = false) String state
   ){
      PageBean<Article> pageBean=articleService.list(pageNum,pageSize,categoryId,state);
      return Result.success(pageBean);

   }
   @GetMapping("/all")
   public Result<PageBean<Article>> listall(
           Integer pageNum,
           Integer pageSize,
           @RequestParam(required = false) String categoryId

           ){
      PageBean<Article> pageBean=articleService.listall(pageNum,pageSize,categoryId);
      return Result.success(pageBean);

   }
   @PutMapping
   public Result update(@RequestBody @Validated(Article.genxin.class) Article article){
         articleService.update(article);
         return Result.success();
   }

   @GetMapping("/detail")
   public Result<Article> detail(@RequestParam(required = true) String id){
      Article article= articleService.detail(id);
      return Result.success(article);
   }

   @DeleteMapping
   public Result delete(@RequestParam(required = true) String id){
      articleService.delete(id);
      return Result.success();
   }

   @GetMapping("/scan")
   public Result<Long> getScan(@RequestParam(required = true) String id){
      Long count= articleService.scan(id);

      return Result.success(count);
   }

   @GetMapping("/like")
   public Result<Like> getLike(
           @RequestParam(required = true) String id){
     Like like=articleService.getlike(id);

      return Result.success(like);
   }
   @GetMapping("/changelike")
   public Result changeLike(
           @RequestParam(required = true) String id){
      articleService.changelike(id);

      return Result.success("操作成功");
   }

}
