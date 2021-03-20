package hznu.linxin.cniaoshop.bean;

import java.util.List;

/**
 * @author: BacSon
 * data: 2021/3/11
 */

/**
 *  服务端的数据是有多页的
 *  使用Page来封装页
 *  @param <T>
 */
public class Page<T> {

    private  int currentPage;
    private  int pageSize;
    private  int totalPage;
    private  int totalCount;

    private List<T> list;


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {

        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
