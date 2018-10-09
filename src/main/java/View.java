import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class View {
    public String developer_view() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("developer_view.html").getFile());
        byte[] encoded = Files.toByteArray(file);
        String html = new String(encoded);
        return html;
    }

    public String interview_view(int id) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("interview_view.html").getFile());
        byte[] encoded = Files.toByteArray(file);
        String html = new String(encoded);
        return html;
    }
}
