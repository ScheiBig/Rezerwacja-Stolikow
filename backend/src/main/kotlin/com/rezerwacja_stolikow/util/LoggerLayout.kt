package com.rezerwacja_stolikow.util

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.LayoutBase
import kotlinx.datetime.LocalDateTime

class LoggerLayout: LayoutBase<ILoggingEvent>() {
    override fun doLayout(event: ILoggingEvent) = when (event.level.levelInt) {
        Level.ERROR_INT -> ANSI.Font.Bright.red
        Level.WARN_INT -> ANSI.Font.Bright.yellow
        Level.INFO_INT -> ANSI.Font.Bright.blue
        Level.DEBUG_INT -> ANSI.Font.Bright.magenta
        Level.TRACE_INT -> ANSI.Font.Bright.green
        else -> ""
    }.let { color ->
        StringBuilder()
            .append(color)
            .append(
                LocalDateTime
                    .fromEpochMilliseconds(event.timeStamp)
                    .toString()
                    .replace('T', ' ')
            )
            .append(" ${ANSI.Font.Bright.white}[")
            .append(event.threadName)
            .append("] ${ANSI.reset}")
            .append(color)
            .append(event.level)
            .append(" ${ANSI.Font.Bright.white}")
            .append(event.loggerName.take(20))
            .append(ANSI.Font.Bright.black)
            .append(" => ")
            .append(ANSI.reset)
            .append(event.formattedMessage)
            .append("\n\n")
            .toString()
    }
}
