package board.board.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice // 예외처리 클래스임을 명시
public class ExceptionHandler {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ModelAndView defaultExceptionHandler(HttpServletRequest request, Exception exception){
        ModelAndView mv = new ModelAndView("/error/error_default"); // 예외 발생시 보여줄 화면
        mv.addObject("exception", exception);

        log.error("exception", exception);

        return mv;
    }
}
