package ru.alabra.voting.util.exception;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 04.09.2019
 */
public class VotingTimeExpiredException extends RuntimeException {
    public VotingTimeExpiredException(String message) {
        super(message);
    }
}
