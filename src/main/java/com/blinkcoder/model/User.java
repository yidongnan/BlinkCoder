package com.blinkcoder.model;

import com.blinkcoder.kit.ModelKit;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * User: Michael Chen
 * Email: yidongnan@gmail.com
 * Date: 13-10-10
 * Time: 上午8:19
 */
public class User extends MyModel<User> {
    public static final User dao = new User();
    private static final String MODEL_CACHE = "user";
    private static final ModelKit mk = new ModelKit(dao, MODEL_CACHE);

    public static void main(String[] args) {
        System.out.println(DigestUtils.shaHex("111"));
    }

    public User Get(int id) {
        return mk.getModel(id);
    }

    @Override
    public boolean Save() {
        String password = DigestUtils.shaHex(this.getStr("password"));
        this.set("password", password);
        return super.Save();
    }

    @Override
    protected void removeCache() {

    }

    public User login(String username, String password) {
        User user = dao.findFirst("select id from user where username=? and password=?",
                username, DigestUtils.shaHex(password));
        return user == null ? null : Get(user.getInt("id"));
    }
}
