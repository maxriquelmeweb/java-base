package com.riquelme.springbootcrudhibernaterestful.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger("logger.specific");

    public static void trace(String msg) {
        logger.trace(msg);
    }

    public static void trace(String msg, Throwable throwable) {
        logger.trace(msg, throwable);
    }

    public static void debug(String msg) {
        logger.debug(msg);
    }

    public static void debug(String msg, Throwable throwable) {
        logger.debug(msg, throwable);
    }

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void info(String msg, Throwable throwable) {
        logger.info(msg, throwable);
    }

    public static void warn(String msg) {
        logger.warn(msg);
    }

    public static void warn(String msg, Throwable throwable) {
        logger.warn(msg, throwable);
    }

    public static void error(String msg) {
        logger.error(msg);
    }

    public static void error(String msg, Throwable throwable) {
        logger.error(msg, throwable);
    }

    public static void fatal(String msg) {
        logger.error("[FATAL] " + msg);
    }

    public static void fatal(String msg, Throwable throwable) {
        logger.error("[FATAL] " + msg, throwable);
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
