package com.myshows.studentapp.rest.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Cookies")
public class Cookies extends Model {

    public static final String SITE_USER_LOGIN = "SiteUser[login]";         // Login
    public static final String SITE_USER_PASSWORD = "SiteUser[password]";   // Password hash
    public static final String PHPSESSID = "PHPSESSID";                     // SESSION ID

    public Cookies() {
        super();
    }

    @SuppressWarnings("unused")
    @Column(name = "Make_me_single", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private boolean unique = true;

    @Column(name = "SITE_USER_LOGIN")
    public String login;

    @Column(name = "SITE_USER_PASSWORD")
    public String passwordHash;

    @Column(name = "PHPSESSID")
    public String phpSessionId;

    public static Cookies getCookies() {
        try {
            return new Select()
                    .all()
                    .from(Cookies.class)
                    .where("Make_me_single = ?", true)
                    .executeSingle();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder cookies = new StringBuilder();
        if (phpSessionId != null && login != null && passwordHash != null) {
            cookies
                    .append("PHPSESSID=").append(phpSessionId).append("; ")
                    .append("SiteUser[login]=").append(login).append("; ")
                    .append("SiteUser[password]=").append(passwordHash).append(";");
        }
        return cookies.toString();
    }

}
