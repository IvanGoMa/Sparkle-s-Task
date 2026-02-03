package cat.ivha.sparklestask
/*
class ItemAPI {
    companion object {
        private var mItemAPI: ItemService? = null

        @Synchronized
        fun API(): ItemService {
            if (mItemAPI == null) {

                val gsondateformat = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create()

                mItemAPI = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gsondateformat))
                    .baseUrl("https://oracleitic.mooo.com/")
                    .build()
                    .create(ItemService::class.java)
            }
            return mItemAPI!!
        }
    }
}
*/