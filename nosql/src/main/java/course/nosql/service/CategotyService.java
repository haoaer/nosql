package course.nosql.service;

import course.nosql.reality.Category;

import java.util.List;

public interface CategotyService {

    //添加种类
    void add(Category category);

    // 获取种类列表
    List<Category> list();

    // 获取分类信息
    Category findById(String id);
    //更新分类信息
    void update(Category category);
    //删除分类
    void delete(String id);
    // 获取所有分类
    List<Category> listall();
}
