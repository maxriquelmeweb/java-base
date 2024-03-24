package com.riquelme.springbootcrudhibernaterestful.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger("logger.log");

    public static void debug(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    public static void debug(String msg, Map<String, String> details) {
        if (logger.isDebugEnabled()) {
            logger.debug("{}: {}", msg, mapToString(details));
        }
    }

    public static void info(String msg) {
        if (logger.isInfoEnabled()) {
            logger.info(msg);
        }
    }

    public static void warn(String msg) {
        if (logger.isWarnEnabled()) {
            logger.warn(msg);
        }
    }

    public static void error(String msg) {
        if (logger.isErrorEnabled()) {
            logger.error(msg);
        }
    }

    public static void error(String msg, Throwable t) {
        if (logger.isErrorEnabled()) {
            logger.error(msg, t);
        }
    }

    private static String mapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        map.forEach((key, value) -> sb.append(key).append("=").append(value).append(", "));
        if (sb.length() > 0) {
            sb.delete(sb.length() - 2, sb.length()); // Remove the trailing comma and space
        }
        return sb.toString();
    }

    /*
     * Explicación de los niveles de registro y cuándo usarlos:
     *
     * trace: El nivel de registro más detallado, utilizado para mensajes de
     * depuración muy detallados.
     * Debe utilizarse para registrar información específica y detallada sobre el
     * flujo de ejecución de tu aplicación.
     * Útil para rastrear problemas difíciles de diagnosticar.
     *
     * debug: Utilizado para mensajes de depuración general.
     * Proporciona información útil durante el desarrollo y depuración de la
     * aplicación, pero no se debe dejar en producción para evitar la sobrecarga de
     * registros.
     *
     * info: Utilizado para mensajes informativos sobre el funcionamiento normal de
     * la aplicación.
     * Útil para informar sobre eventos significativos o hitos en la ejecución del
     * programa.
     *
     * warn: Utilizado para mensajes de advertencia sobre situaciones que podrían
     * indicar un problema potencial, pero que no interrumpen el funcionamiento
     * normal de la aplicación.
     *
     * error: Utilizado para mensajes de error que indican un problema que ha
     * ocurrido.
     * Los errores pueden variar desde problemas menores que no afectan
     * significativamente la operación de la aplicación hasta errores graves que
     * requieren atención inmediata.
     *
     * fatal: Reservado para mensajes que indican un error grave que interrumpe
     * gravemente el funcionamiento de la aplicación y podría llevar a un cierre
     * inesperado.
     *
     * Se utiliza para situaciones críticas que requieren intervención inmediata.
     * Elige el nivel de registro apropiado según la importancia y el tipo de
     * mensaje que deseas registrar. Asegúrate de ajustar la configuración de
     * registro según el entorno (por ejemplo, desactivar los niveles de depuración
     * en producción para evitar la sobrecarga de registros).
     */

}
