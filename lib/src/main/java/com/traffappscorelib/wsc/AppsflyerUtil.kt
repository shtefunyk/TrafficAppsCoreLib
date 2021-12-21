package com.traffappscorelib.wsc

import android.text.TextUtils

class AppsflyerUtil {

    companion object {
        fun parse(namingSeparator: String, conversionData: Map<String, Any>, listener: IResultListener, url: String) {

            val naming = conversionData["c"]
            val namingParams: List<String> = naming.toString()
                .split(if(TextUtils.isEmpty(namingSeparator)) Constants.DEFAULT_NAMING_SEPARATOR else namingSeparator)

            var resultUrl = url

            if(namingParams.size > 1) {

                val hasStreamKey = isStreamKey(namingParams[1])
                if(hasStreamKey) resultUrl = if(url.endsWith("/")) resultUrl + namingParams[1] else resultUrl + "/" + namingParams[1]
                else resultUrl = addUrlParam(resultUrl, namingParams[1].replaceFirst(":", "="))

                if(namingParams.size > 2)
                    for (i in 2 until namingParams.size) {
                        resultUrl = addUrlParam(resultUrl, namingParams[i].replaceFirst(":", "="))
                    }

                listener.success(resultUrl)
            }
            else {
                listener.failed()
            }
        }

        private fun isStreamKey(param: String) = !param.contains(":")

        private fun addUrlParam(url: String, paramValue: String): String {
            return if(url.contains("?")) {
                "$url&$paramValue"
            }
            else "$url?$paramValue"
        }
    }
}