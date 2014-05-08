package com.blinkcoder.controller;

import com.blinkcoder.common.myConstants;
import com.blinkcoder.model.User;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * User: Michael
 * Date: 13-10-10
 * Time: 下午9:27
 */
public class UserController extends MyController {

    private static final String CALLBACK_URI = "http://www.blinkcoder.com/action/user/loginAfter";
    /**
     * OAuth 2.0 scopes.
     */
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email");
    private AuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(), new JacksonFactory(),
            myConstants.GOOGLE_CLIENT_ID, myConstants.GOOGLE_CLIENT_SECRET_KEY, SCOPES).build();

    public void login() {
        String state = new BigInteger(130, new SecureRandom()).toString(32);
        getSession().setAttribute("state", state);

        GoogleAuthorizationCodeRequestUrl url = (GoogleAuthorizationCodeRequestUrl) flow.newAuthorizationUrl();

        url.setRedirectUri(CALLBACK_URI).setState(state).build();
        redirect(url.toString());
    }

    public void loginAfter() throws IOException {
        if (!getRequest().getParameter("state").equals(getSession().getAttribute("state"))) {
            getResponse().setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            getResponse().getWriter().print("Invalid state parameter.");
            return;
        }

        try {
            TokenResponse tokenResponse = flow.newTokenRequest(getPara("code")).setRedirectUri(CALLBACK_URI).execute();

            Credential credential = flow.createAndStoreCredential(tokenResponse, null);
            Oauth2 oauth = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential).setApplicationName("Blinkcoder").build();

            Userinfoplus userinfo = oauth.userinfo().get().execute();
            System.out.println(userinfo);

            User user = User.dao.findByOpenId(userinfo.getId());
            if (user == null) {
                // 创建一个新用户
                user = new User();
                user.set("username", userinfo.getName());
                user.set("email", userinfo.getEmail());
                user.set("openid", userinfo.getId());
                user.set("gender", userinfo.getGender());
                user.set("link", userinfo.getLink());
                user.set("locale", userinfo.getLocale());
                user.set("picture", userinfo.getPicture());
                user.set("verifiedemail", userinfo.getVerifiedEmail());

                user.set("role", User.ROLE_GENERAL);
                user.Save();
            } else {
                // 已经存在这个用户 更新部分资料
                user.set("username", userinfo.getName());
                user.set("email", userinfo.getEmail());
                user.set("gender", userinfo.getGender());
                user.set("link", userinfo.getLink());
                user.set("locale", userinfo.getLocale());
                user.set("picture", userinfo.getPicture());
                user.set("verifiedemail", userinfo.getVerifiedEmail());
                user.Update();
            }
            saveUserInCookie(user);
            redirect("/");
        } catch (TokenResponseException e) {
            e.printStackTrace();
            getResponse().setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            getResponse().getWriter().print("Failed to upgrade the authorization code.");
        } catch (IOException e) {
            e.printStackTrace();
            getResponse().setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            getResponse().getWriter().print("Failed to read token data from Google. " + e.getMessage());
        }
    }

    public void logout() {
        removeCookie("blinkcoder");
        getRequest().removeAttribute("g_user");
        redirect("/");
    }
}
