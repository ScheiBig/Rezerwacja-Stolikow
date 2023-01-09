@file:Suppress("unused")

package com.rezerwacja_stolikow.util

object ANSI {
    object Font {
        /**
         * Black font
         */
        const val black = "\u001b[30m"
        
        /**
         * Red font
         */
        const val red = "\u001b[31m"
        
        /**
         * Green font
         */
        const val green = "\u001b[32m"
        
        /**
         * Yellow font
         */
        const val yellow = "\u001b[33m"
        
        /**
         * Blue font
         */
        const val blue = "\u001b[34m"
        
        /**
         * Magenta font
         */
        const val magenta = "\u001b[35m"
        
        /**
         * Cyan font
         */
        const val cyan = "\u001b[36m"
        
        /**
         * Almost-white (usually light-gray) font
         */
        const val white = "\u001b[37m"
        
        object Bright {
            /**
             * Dark-gray font
             *
             * _Best for additional information (or punctuations)_
             */
            const val black = "\u001b[90m"
            
            /**
             * Brighter-red font
             *
             * _Best for error messages (terminating exceptions)_
             */
            const val red = "\u001b[91m"
            
            /**
             * Brighter-green font
             *
             * _Best for confirmation messages / printing requested data_
             */
            const val green = "\u001b[92m"
            
            /**
             * Brighter-yellow font
             *
             * _Best for warning messages (handled exceptions)_
             */
            const val yellow = "\u001b[93m"
            
            /**
             * Brighter-blue font
             *
             * _Best for information messages (current state)_
             */
            const val blue = "\u001b[94m"
            
            /**
             * Brighter-magenta font
             *
             * _Best for debug messages_
             */
            const val magenta = "\u001b[95m"
            
            /**
             * Brighter-cyan font
             *
             * _Best for input prompts_
             */
            const val cyan = "\u001b[96m"
            
            /**
             * True-white font
             *
             * _Best for important information_
             */
            const val white = "\u001b[97m"
        }
    }
    
    object Background {
        
        /**
         * Black background
         */
        const val black = "\u001b[40m"
        
        /**
         * Red background
         */
        const val red = "\u001b[41m"
        
        /**
         * Green background
         */
        const val green = "\u001b[42m"
        
        /**
         * Yellow background
         */
        const val yellow = "\u001b[43m"
        
        /**
         * Blue background
         */
        const val blue = "\u001b[44m"
        
        /**
         * Magenta background
         */
        const val magenta = "\u001b[45m"
        
        /**
         * Cyan background
         */
        const val cyan = "\u001b[46m"
        
        /**
         * Almost-white (usually light-gray) background
         */
        const val white = "\u001b[47m"
        
        object Bright {
            
            /**
             * Dark-gray background
             */
            const val black = "\u001b[100m"
            
            /**
             * Brighter-red background
             */
            const val red = "\u001b[101m"
            
            /**
             * Brighter-green background
             */
            const val green = "\u001b[102m"
            
            /**
             * Brighter-yellow background
             */
            const val yellow = "\u001b[103m"
            
            /**
             * Brighter-blue background
             */
            const val blue = "\u001b[104m"
            
            /**
             * Brighter-magenta background
             */
            const val magenta = "\u001b[105m"
            
            /**
             * Brighter-cyan background
             */
            const val cyan = "\u001b[106m"
            
            /**
             * True-white background
             */
            const val white = "\u001b[107m"
            
        }
    }
    
    /**
     * Resets all formatting (from this point)
     */
    const val reset = "\u001b[0m"
}
