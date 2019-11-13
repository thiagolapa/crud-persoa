package br.com.softplan.person.service.util;


import br.com.softplan.person.web.rest.errors.BadRequestAlertException;
import br.com.softplan.person.web.rest.errors.GoneAlertException;

/**
 * Utility class to throw errors
 */
public class ThrowUtils {

    public static void badRequest(String defaultMessage, String entityName, String errorKey) {
        throw new BadRequestAlertException(defaultMessage, entityName, errorKey);
    }

    public static void gone(String defaultMessage, String entityName, String errorKey) {
        throw new GoneAlertException(defaultMessage, entityName, errorKey);
    }
}
