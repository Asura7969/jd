package com.taotao.manage.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@Controller
@RequestMapping("content/category")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 查询所有节点
     *
     * @param parentid
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> querryListByParentId(@RequestParam(value = "id", defaultValue = "0") Long parentid) {

        try {
            ContentCategory record = new ContentCategory();
            record.setParentId(parentid);

            List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
            if (null == list || list.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok().body(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 增加节点
     *
     * @param contentCategory
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory) {
        try {
            //新增的节点
            contentCategory.setId(null);
            contentCategory.setIsParent(false);
            contentCategory.setSortOrder(1);
            contentCategory.setStatus(1);
            this.contentCategoryService.save(contentCategory);
            //修改新增节点的父节点  isParent书否为true
            ContentCategory conP = this.contentCategoryService.queryById(contentCategory.getParentId());
            if (!conP.getIsParent()) {
                conP.setIsParent(true);
                this.contentCategoryService.updateSelective(conP);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 重命名
     *
     * @param contentCategory
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> rename(ContentCategory contentCategory) {

        try {
            this.contentCategoryService.updateSelective(contentCategory);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    /**
     * 删除节点
     *
     * @param contentCategory
     * @return
     */

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(ContentCategory contentCategory) {
        try {
            //查找所有子节点
            List<Object> ids = new ArrayList<Object>();
            ids.add(contentCategory.getId());
            findAllIds(contentCategory.getId(), ids);
            //删除所有子节点
            this.contentCategoryService.deleteByIds(ContentCategory.class, "id", ids);

            //判断当前节点是否还有兄弟节点，没有则修改父节点的isParent属性
            ContentCategory contentCategory2 = new ContentCategory();
            contentCategory2.setParentId(contentCategory.getParentId());
            List<ContentCategory> list = this.contentCategoryService.queryListByWhere(contentCategory2);

            if (list == null || list.isEmpty()) {
                ContentCategory parent = new ContentCategory();
                parent.setId(contentCategory.getParentId());
                parent.setIsParent(false);
                this.contentCategoryService.updateSelective(parent);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {

            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    private void findAllIds(Long id, List<Object> ids) {
        //查询该节点下的所有子节点
        ContentCategory record = new ContentCategory();
        record.setParentId(id);
        List<ContentCategory> list = this.contentCategoryService.queryListByWhere(record);
        for (ContentCategory contentCategory : list) {
            ids.add(contentCategory.getId());
            //判断该节点是否为父节点,如果是，进行递归
            if (contentCategory.getIsParent()) {
                findAllIds(contentCategory.getId(), ids);
            }
        }
    }


}
