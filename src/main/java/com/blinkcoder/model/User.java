package com.blinkcoder.model;

import com.blinkcoder.kit.ModelKit;

import java.security.NoSuchAlgorithmException;

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

    public User Get(int id) {
        return mk.getModel(id);
    }

    @Override
    public boolean Save() {
        String password = getMD5(this.getStr("password").getBytes());
        this.set("password", password);
        return super.Save();
    }

    @Override
    protected void removeCache() {

    }

    @Override
    protected void removeModelCache() {

    }

    public User login(String username, String password) {
        User user = dao.findFirst("select id from user where username=? and password=?", username, getMD5(password.getBytes()));
        return user == null ? null : Get(user.getInt("id"));
    }

    private String getMD5(byte[] src) {
        StringBuilder sb = new StringBuilder();
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(src);
            for (byte b : md.digest())
                sb.append(Integer.toString(b >>> 4 & 0xF, 16)).append(Integer.toString(b & 0xF, 16));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
