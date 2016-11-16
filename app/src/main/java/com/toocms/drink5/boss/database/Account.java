package com.toocms.drink5.boss.database;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 搜索历史记录表
 *
 * @author Zero
 * @date 2016/1/30 17:23
 */
@Table(name = "Account")
public class Account {
    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "phone")
    private String phone;

    @Column(name = "pass")
    private String pass;

    @Column(name = "head")
    private String head;

    @Column(name = "name")
    private String name;

    @Column(name = "site_id")
    private String site_id;

//    public Account(String phone, String pass, String head, String name, String site_id) {
//        this.phone = phone;
//        this.pass = pass;
//        this.head = head;
//        this.name = name;
//        this.site_id = site_id;
//        LogUtil.e(phone + "," + pass + "," + head + "," + name + "," + site_id);
//    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", pass='" + pass + '\'' +
                ", head='" + head + '\'' +
                '}';
    }
}
