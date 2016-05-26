package com.eva.httpdemo.api.entity;

import java.util.List;

/**
 *
 * @author test
 * @date 15/11/19
 */
public abstract class BaseListBean<E> {

    private int page;
    private int perpage;
    private int total;
    private List<E> list;

    public BaseListBean() {}

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
//        if (list != null) {
//            // 确保列表集合是不可变的
//            this.list = Collections.unmodifiableList(list);
//        }
        this.list = list;
    }

    public int getItemCount() {
        return this.list != null ? this.list.size() : 0;
    }

    public E getItemAt(int position) {
        return this.list != null ? this.list.get(position) : null;
    }

    public abstract Class getType() ;

}
