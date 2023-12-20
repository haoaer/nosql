package course.nosql.service;

import course.nosql.reality.Article;
import course.nosql.reality.Like;
import course.nosql.reality.PageBean;

public interface ArticleService {


    //分页查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, String categoryId, String state);

    void add(Article article);
    // 更新文章
    void update(Article article);
    //获取文章详细信息
    Article detail(String id);
    //删除文章
    void delete(String id);

    // 获取文章列表
    PageBean<Article> listall(Integer pageNum, Integer pageSize,String categoryId);

    // 文章浏览数
    Long scan(String id);

    // 文章点赞数
    Like getlike(String articleId);

    // 取消/点赞
    void changelike(String articleid);


}
