package br.univates.api.filter;

import br.univates.api.dtos.apilogDTO;
import br.univates.api.model.api_log;
import br.univates.api.repositories.apilogRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Component
@Order(HIGHEST_PRECEDENCE)
public class RequestLoggingFilter implements Filter {

    // Use o logger padrão do Spring (SLF4J)
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private final apilogRepository apilogRepository;

    public RequestLoggingFilter(apilogRepository apilogRepository) {
        this.apilogRepository = apilogRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        // Para termos acesso ao Body, precisamos "embrulhar" (wrap) a request e a response
        // Isso permite que o body seja lido várias vezes (pelo nosso log e pelo Spring)
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) servletRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);

        long startTime = System.nanoTime();

        // Deixa a requisição seguir o fluxo normal
        filterChain.doFilter(requestWrapper, responseWrapper);

        // 2. MUDE AQUI: Calcule o tempo em nanossegundos e converta
        long durationNanos = System.nanoTime() - startTime;
        long timeTaken = durationNanos / 1_000_000;

        apilogDTO apilogDTO = new apilogDTO(requestWrapper.getMethod(),requestWrapper.getRequestURI(),responseWrapper.getStatus(), timeTaken);
        api_log api = new api_log();
        BeanUtils.copyProperties(apilogDTO, api);
        try{
            apilogRepository.save(api);
        }catch (Exception e){
            throw new IOException(e.getMessage());
        }
        // 4. IMPORTANTE: Copia o conteúdo da response (que estava em cache) de volta
        // para a response original, para que o cliente receba a resposta.
        responseWrapper.copyBodyToResponse();
    }

    // Método utilitário para converter o array de bytes do body em String
    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (Exception e) {
            logger.warn("Erro ao converter body para String", e);
            return "[conversão falhou]";
        }
    }
}