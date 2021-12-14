package com.traffappscorelib.wsc

class AppsflyerUtil {

    companion object {
        fun parse(conversionData: Map<String, Any>, listener: IResultListener, url: String) {

            val fieldSub1 = "{sub_1}"
            val fieldSub2 = "{sub_2}"
            val fieldSub3 = "{sub_3}"
            val fieldSub4 = "{sub_4}"
            val fieldSub5 = "{sub_5}"

            val naming = conversionData["c"]
            val namingParams: List<String> = naming.toString().split("_")

            if(namingParams.size > 3) {
                val sub1 = if(namingParams.size > 3) namingParams[3] else ""
                val sub2 = if(namingParams.size > 4) namingParams[4] else ""
                val sub3 = if(namingParams.size > 5) namingParams[5] else ""
                val sub4 = if(namingParams.size > 6) namingParams[6] else ""
                val sub5 = if(namingParams.size > 7) namingParams[7] else ""

                val result = url
                    .replace(fieldSub1, sub1)
                    .replace(fieldSub2, sub2)
                    .replace(fieldSub3, sub3)
                    .replace(fieldSub4, sub4)
                    .replace(fieldSub5, sub5)

                listener.success(result)
            }
            else {
                listener.failed()
            }
        }
    }
}