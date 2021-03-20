package hznu.linxin.cniaoshop.bean;

import java.io.Serializable;

/**
 * @author: BacSon
 * data: 2021/3/13
 */

/**
 *  购物车数据
 */
public class ShoppingCart extends Wares implements Serializable {

    private int count;  // 数量
    private boolean isChecked=true;  // 是否选中

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

}