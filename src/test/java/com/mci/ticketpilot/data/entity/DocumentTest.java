import static org.junit.jupiter.api.Assertions.*;
import com.mci.ticketpilot.data.entity.Document;
import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.entity.Users;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
public class DocumentTest {

    @Test
    public void testGettersAndSetters() {
        // Arrange
        String fileName = "document.txt";
        String fileType = "txt";
        LocalDate documentCreationDate = LocalDate.now();
        Users author = new Users();
        byte[] documentData = new byte[]{1, 2, 3};
        Ticket ticket = new Ticket();

        Document document = new Document();

        // Act
        document.setFileName(fileName);
        document.setFileType(fileType);
        document.setDocumentCreationDate(documentCreationDate);
        document.setAuthor(author);
        document.setDocumentData(documentData);
        document.setTicket(ticket);

        // Assert
        assertEquals(fileName, document.getFileName());
        assertEquals(fileType, document.getFileType());
        assertEquals(documentCreationDate, document.getDocumentCreationDate());
        assertEquals(author, document.getAuthor());
        assertEquals(documentData, document.getDocumentData());
        assertEquals(ticket, document.getTicket());
    }

    @Test
    public void testSetFileTypeFromFileName() {
        // Arrange
        String fileName = "document.txt";
        Document document = new Document();
        document.setFileName(fileName);

        // Act
        document.setFileTypeFromFileName();

        // Assert
        assertEquals("Optional[txt]", document.getFileType());
    }

    // Reflection because of private Access von extractFileExtension
    @Test
    public void testExtractFileExtension() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        String fileName = "document.txt";
        Document document = new Document();
        Method method = Document.class.getDeclaredMethod("extractFileExtension", String.class);
        method.setAccessible(true);

        // Act
        Optional<String> fileExtension = (Optional<String>) method.invoke(document, fileName);

        // Assert
        assertEquals("txt", fileExtension.orElse(null));
    }

    @Test
    public void testExtractFileExtension_NoExtension() throws Exception {
        // Arrange
        String fileName = "document";
        Document document = new Document();

        // Get the reference to the private method using Reflection
        Method extractFileExtensionMethod = Document.class.getDeclaredMethod("extractFileExtension", String.class);
        extractFileExtensionMethod.setAccessible(true); // Set the method as accessible

        // Act
        Optional<String> fileExtension = (Optional<String>) extractFileExtensionMethod.invoke(document, fileName);

        // Assert
        assertFalse(fileExtension.isPresent());
    }

}
