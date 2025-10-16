package broker;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@Order(1)
public class LanguageFilter implements Filter {

    public static ThreadLocal<String> language = new ThreadLocal<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        language.set(((HttpServletRequest) request).getHeader("Accept-Language"));
        chain.doFilter(request, response);
    }
}
