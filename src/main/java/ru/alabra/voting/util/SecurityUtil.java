package ru.alabra.voting.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.alabra.voting.AuthorizedUser;

import static java.util.Objects.requireNonNull;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 09.09.2019
 */
@Component
public class SecurityUtil {


    public AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    public AuthorizedUser get() {
        AuthorizedUser user = safeGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }

    public int getAuthUserId() {
        return get().getId();
    }



}
