package ru.itmo.moona.domain.users;

public class CurrentUser {
    private static CurrentUser instance;
    private User user;

    private CurrentUser() {
    }

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public void login(User u) {
        this.user = u;
    }

    public void logout() {
        this.user = null;
    }

    public User getUser() {
        return user;
    }
}
