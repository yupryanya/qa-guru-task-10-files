package guru.qa;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.model.ApplicationJsonModel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationJsonTests {
    private static String jsonFile = "json/application.json";
    private static ClassLoader cl = ZipArchiveFilesTests.class.getClassLoader();
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void applicationJsonTest() throws IOException {
        try (InputStream stream = cl.getResourceAsStream(jsonFile);
             Reader reader = new InputStreamReader(stream)) {

            ApplicationJsonModel model = objectMapper.readValue(reader, ApplicationJsonModel.class);

            assertThat(model.getFirstName()).isEqualTo("John");
            assertThat(model.getLastName()).isEqualTo("Doe");
            assertThat(model.getPhoneNumber()).isEqualTo("123-456-78-90");
            assertThat(model.getDateOfBirth()).isEqualTo("1990-01-01");
            assertThat(model.getHobbies()).contains("reading");
            assertThat(model.getHobbies()).contains("art");
            assertThat(model.getHobbies()).contains("sport");
        }
    }
}
