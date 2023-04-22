package by.banking.currency.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class LoggingFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Value("${http.logging.file.path.name}")
    private String filePath;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper requestWrapper = requestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = responseWrapper(response);

        chain.doFilter(requestWrapper, responseWrapper);

        logRequest(requestWrapper);
        logResponse(responseWrapper);
    }

    private void logRequest(ContentCachingRequestWrapper request) throws IOException {
        if (request.getRequestURI().startsWith("/h2-console")) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("\n" + request.getRemoteAddr() + ":" + request.getRemotePort() + "\n");
        builder.append(request.getRequestURI());
        builder.append("\n");
        builder.append(headersToString(Collections.list(request.getHeaderNames()), request::getHeader));
        builder.append(new String(request.getContentAsByteArray()));
        log.info("Request: {}", builder);
        FileUtils.writeStringToFile(new File(filePath), "Request: {" + builder + System.lineSeparator() + "}", UTF_8, true);
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        if (response.getStatus() != 304 && (response.getContentType() == null ||
                response.getContentType().equals("application/json") ||
                response.getContentType().equals("application/xml") ||
                response.getContentType().equals("application/problem+json"))) {
            StringBuilder builder = new StringBuilder();
            builder.append("\n");
            builder.append(response.getStatus());
            builder.append("\n");
            builder.append(headersToString(response.getHeaderNames(), response::getHeader));
            builder.append(new String(response.getContentAsByteArray()));
            log.info("Response: {}", builder);
            FileUtils.writeStringToFile(new File(filePath),
                    System.lineSeparator() + "Response: {" + builder + "\n}" + "\n\n"
                    , UTF_8, true);
            response.copyBodyToResponse();
        } else {
            response.copyBodyToResponse();
        }
    }

    private String headersToString(Collection<String> headerNames, Function<String, String> headerValueResolver) {
        StringBuilder builder = new StringBuilder();
        for (String headerName : headerNames) {
            String header = headerValueResolver.apply(headerName);
            builder.append("%s=%s".formatted(headerName, header)).append("\n");
        }
        return builder.toString();
    }

    private ContentCachingRequestWrapper requestWrapper(ServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper requestWrapper) {
            return requestWrapper;
        }
        return new ContentCachingRequestWrapper((HttpServletRequest) request);
    }

    private ContentCachingResponseWrapper responseWrapper(ServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper responseWrapper) {
            return responseWrapper;
        }
        return new ContentCachingResponseWrapper((HttpServletResponse) response);
    }
}