package quiz;

import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

public class TestTsvReader {

    @Test
    public void testReadTsv() throws URISyntaxException {
        InputStream inputStream = getClass().getResourceAsStream("/questions.tsv");
        List<List<String>> result = TsvReader.readTsv(inputStream);
        Assert.assertEquals("Tas", result.get(1).get(5));
    }

}