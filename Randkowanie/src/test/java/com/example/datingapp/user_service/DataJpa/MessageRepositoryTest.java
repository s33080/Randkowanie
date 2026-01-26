package com.example.datingapp.user_service.DataJpa;

import com.example.datingapp.model.ChatMessage;
import com.example.datingapp.model.User;
import com.example.datingapp.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@TestPropertySource(properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldReturnChronologicalChatHistoryBetweenTwoUsers() {
        // Arrange: Tworzymy dwóch użytkowników
        User userA = new User(); userA.setName("User A"); userA.setEmail("a@t.pl"); userA.setPassword("p");
        User userB = new User(); userB.setName("User B"); userB.setEmail("b@t.pl"); userB.setPassword("p");
        entityManager.persist(userA);
        entityManager.persist(userB);

        // Arrange: Tworzymy wiadomości w różnym czasie i w obie strony
        ChatMessage msg1 = new ChatMessage();
        msg1.setSender(userA); msg1.setRecipient(userB); msg1.setContent("Cześć B!");
        msg1.setSentAt(LocalDateTime.now().minusMinutes(10));
        entityManager.persist(msg1);

        ChatMessage msg2 = new ChatMessage();
        msg2.setSender(userB); msg2.setRecipient(userA); msg2.setContent("Hej A, co słychać?");
        msg2.setSentAt(LocalDateTime.now().minusMinutes(5));
        entityManager.persist(msg2);

        ChatMessage msg3 = new ChatMessage(); // Wiadomość od kogoś innego (nie powinna się pojawić)
        msg3.setSender(userA); msg3.setRecipient(new User()); // inny odbiorca
        msg3.setContent("Tego nie chcemy w teście");

        entityManager.flush();

        // Act: Pobieramy historię między A i B
        List<ChatMessage> history = messageRepository.findChatHistory(userA.getId(), userB.getId());

        // Assert: Sprawdzamy czy są 2 wiadomości i czy są w dobrej kolejności
        assertThat(history, hasSize(2));
        assertThat(history.get(0).getContent(), is(equalTo("Cześć B!")));
        assertThat(history.get(1).getContent(), is(equalTo("Hej A, co słychać?")));

        // Sprawdzenie chronologii: czy data pierwszej wiadomości jest przed datą drugiej
        assertThat(history.get(0).getSentAt().isBefore(history.get(1).getSentAt()), is(true));
    }
}