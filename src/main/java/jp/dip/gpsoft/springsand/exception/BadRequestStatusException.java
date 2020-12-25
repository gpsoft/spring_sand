package jp.dip.gpsoft.springsand.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class BadRequestStatusException extends RuntimeException {
}
