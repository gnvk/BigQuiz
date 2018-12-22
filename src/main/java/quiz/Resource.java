package quiz;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Resource {

    public static URI getUri(String resourceName) throws URISyntaxException {
        URL resource = Resource.class.getResource("/" + resourceName);
        return resource.toURI();
    }

    public static String getPath(String resourceName) throws URISyntaxException {
        return getUri(resourceName).getPath();
    }
}
