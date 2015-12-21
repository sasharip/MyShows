package com.myshows.studentapp.rest.model;

public class Cookies {

    public static final String SITE_USER_LOGIN = "SiteUser[login]";         // Login
    public static final String SITE_USER_PASSWORD = "SiteUser[password]";   // Password hash
    public static final String PHPSESSID = "PHPSESSID";                     // SESSION ID

    public String login;
    public String passwordHash;
    public String phpSessionId;

    @Override
    public String toString() {
        StringBuilder cookies = new StringBuilder();
        if (phpSessionId != null && login != null && passwordHash != null) {
            cookies
                    .append(PHPSESSID).append("=").append(phpSessionId).append("; ")
                    .append(SITE_USER_LOGIN).append("=").append(login).append("; ")
                    .append(SITE_USER_PASSWORD).append("=").append(passwordHash).append(";");
        }
        return cookies.toString();
    }

}
