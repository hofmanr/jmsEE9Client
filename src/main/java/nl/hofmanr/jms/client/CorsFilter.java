package nl.hofmanr.jms.client;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    private static final List<String> URLS =
            Arrays.asList("http://localhost:8080",
                    "http://localhost:3000",
                    "http://127.0.0.1:8080",
                    "http://127.0.0.1:3000");
    /**
     * See: https://www.baeldung.com/cors-in-jax-rs and https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
     * @param containerRequestContext
     * @param containerResponseContext
     * @throws IOException
     */
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        String origin = containerRequestContext.getHeaders().getFirst("Origin");
        if (URLS.contains(origin)) {
            containerResponseContext.getHeaders().add(
                    "Access-Control-Allow-Origin", origin);  // all '*'
        }
        containerResponseContext.getHeaders().add(
                "Access-Control-Allow-Credentials", "true");
        containerResponseContext.getHeaders().add(
                "Access-Control-Allow-Headers",
                "origin, content-type, access-control-allow-headers, accept, authorization, x-requested-with");
        containerResponseContext.getHeaders().add(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        if ("OPTIONS".equalsIgnoreCase(containerRequestContext.getMethod())) {
            containerResponseContext.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
    }
}
