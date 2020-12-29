package com.nickmafra.demo.infra.interceptors;

import com.nickmafra.demo.Messages_;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Slf4j
@Component
public class AppErrorAttributes extends DefaultErrorAttributes {

    private static final String MSG1 = "message";
    private static final String MSG2 = "details";

    @Autowired
    private MessageSource messageSource;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest req, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(req, includeStackTrace);

        Integer status = (Integer) errorAttributes.get("status");
        if (status == 404) {
            setMessageAttributes(errorAttributes, Messages_.ERRO_URL_INVALIDA);
        } else if (status == 403) {
            setMessageAttributes(errorAttributes, Messages_.ERRO_ACESSO_NEGADO);
        } else if (status == 500) {
            setMessageAttributes(errorAttributes, Messages_.ERRO_GENERICO_INTERNO);
        }
        parseMessageAttributes(errorAttributes, req);

        return errorAttributes;
    }

    public static void setMessageAttributes(Map<String, Object> errorAttributes, Object... args) {
        String message = null;
        String details = null;
        for (Object arg : args) {
            if (arg instanceof Throwable) {
                arg = ((Throwable) arg).getMessage();
            }
            if (arg != null) {
                if (message == null)
                    message = arg.toString();
                else if (details == null)
                    details = arg.toString();
            }
        }
        errorAttributes.put(MSG1, message);
        errorAttributes.put(MSG2, details);
    }

    private void parseMessageAttributes(Map<String, Object> errorAttributes, WebRequest req) {
        parseAttribute(errorAttributes, req, MSG1);
        parseAttribute(errorAttributes, req, MSG2);
    }

    private void parseAttribute(Map<String, Object> errorAttributes, WebRequest req, String attributeName) {
        errorAttributes.put(attributeName, getParsedMessage(req, (String) errorAttributes.get(attributeName)));
    }

    private String getParsedMessage(WebRequest req, String msgCode) {
        if (msgCode == null)
            return null;
        if (msgCode.length() > 100 || msgCode.contains(" ") || !msgCode.contains("."))
            return msgCode; // não é código de mensagem

        try {
            return messageSource.getMessage(msgCode, null, req.getLocale());
        } catch (NoSuchMessageException e) {
            log.debug("Código de mensagem não encontrado: {}", msgCode);
            return msgCode;
        }
    }

}
