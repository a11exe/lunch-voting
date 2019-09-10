package ru.alabra.voting.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 10.09.2019
 */
@Component
@PropertySource({
        "classpath:conf/application.properties"
})
public class ConfigUtil {

    @Value("${end.voting.time}")
    private LocalTime endVotingTime;

    public void setEndVotingTime(LocalTime endVotingTime) {
        this.endVotingTime = endVotingTime;
    }

    public LocalTime getEndVotingTime() {
        return endVotingTime;
    }
}
