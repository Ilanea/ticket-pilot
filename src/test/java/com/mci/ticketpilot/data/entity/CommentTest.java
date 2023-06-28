import static org.junit.jupiter.api.Assertions.*;
import com.mci.ticketpilot.data.entity.Comment;
import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.entity.Users;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CommentTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String commentText = "This is a comment";
        Users author = new Users();
        Ticket ticket = new Ticket();
        LocalDateTime timestamp = LocalDateTime.now();
        byte[] documentData = new byte[]{1, 2, 3};

        Comment comment = new Comment();

        // Act
        comment.setComment(commentText);
        comment.setAuthor(author);
        comment.setTicket(ticket);
        comment.setTimestamp(timestamp);
        comment.setDocumentData(documentData);

        // Assert
        assertEquals(commentText, comment.getComment());
        assertEquals(author, comment.getAuthor());
        assertEquals(ticket, comment.getTicket());
        assertEquals(timestamp, comment.getTimestamp());
        assertEquals(documentData, comment.getDocumentData());
    }

    @Test
    public void testEmptyConstructor() {
        // Arrange
        Comment comment = new Comment();

        // Assert
        assertNull(comment.getComment());
        assertNull(comment.getAuthor());
        assertNull(comment.getTicket());
        assertNull(comment.getTimestamp());
        assertNull(comment.getDocumentData());
    }
}

