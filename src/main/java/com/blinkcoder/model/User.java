package com.blinkcoder.model;

import com.blinkcoder.kit.ModelKit;

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

    public static final int ROLE_ADMIN = 100;
    public static final int ROLE_GENERAL = 1;

    public User Get(int id) {
        return mk.getModel(id);
    }

    @Override
    protected void removeCache() {

    }

    public User findByOpenId(String openId) {
        User user = dao.findFirstByCache(MODEL_CACHE, "openid#" + openId, "select id from user where openid = ?", openId);
        return user == null ? null : Get(user.getInt("id"));
    }
}
