package hznu.linxin.cniaoshop.bean;

/**
 * @author: BacSon
 * data: 2021/3/11
 */

import java.io.Serializable;

/**
 *  热门商品的Bean文件
 *  url地址： http://112.124.22.238:8081/course_api/wares/hot 可以查看一下数据的封装格式
 */
public class Wares implements Serializable {


    private Long id;
    private String name;
    private String imgUrl;
    private String description;
    private Float price;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}

