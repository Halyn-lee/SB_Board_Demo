package board.board.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoggerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 해당 요청의 URI가 "/css/style.css"인 경우 로그를 출력하지 않음
        if (!request.getRequestURI().equals("/css/style.css")) {
            log.debug("======================================          START         ======================================");
            log.debug(" Request URI \t:  " + request.getRequestURI());
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 해당 요청의 URI가 "/css/style.css"인 경우 로그를 출력하지 않음
        if (!request.getRequestURI().equals("/css/style.css")) {
        } else
        log.debug("======================================           END          ======================================\n");
    }
}
