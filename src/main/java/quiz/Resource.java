package quiz;

import java.net.URI;
import java.net.URISyntaxException;

public class Resource {

    public static URI getUri(String resourceName) throws URISyntaxException {
        return Resource.class.getResource("/" + resourceName).toURI();
    }

    public static String getPath(String resourceName) throws URISyntaxException {
        return getUri(resourceName).getPath();
    }
}
