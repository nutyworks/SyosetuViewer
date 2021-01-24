package me.nutyworks.syosetuviewerv2.network

import me.nutyworks.syosetuviewerv2.data.TranslationWrapper
import me.nutyworks.syosetuviewerv2.data.YomouSearchResult
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.lang.reflect.Type
import kotlin.reflect.full.primaryConstructor

object Yomou {

    private val mRetrofit = Retrofit.Builder()
        .addConverterFactory(PageAdapter.FACTORY)
        .baseUrl("https://yomou.syosetu.com/")
        .build()

    private val mService = mRetrofit.create(YomouService::class.java)

    object Genre {
        /**
         * 異世界
         */
        const val DIFFERENT_WORLD = 101

        /**
         * 現実世界
         */
        const val REAL_WORLD = 102

        /**
         * ハイファンタジー
         */
        const val HIGH_FANTASY = 201

        /**
         * ローファンタジー
         */
        const val LOW_FANTASY = 202

        /**
         * 純文学
         */
        const val PURE_LITERATURE = 301

        /**
         * ヒューマンドラマ
         */
        const val HUMAN_DRAMA = 302

        /**
         * 歴史
         */
        const val HISTORY = 303

        /**
         * 推理
         */
        const val DETECTIVE = 304

        /**
         * ホラー
         */
        const val HORROR = 305

        /**
         * アクション
         */
        const val ACTION = 306

        /**
         * コメディー
         */
        const val COMEDY = 307

        /**
         * VRゲーム
         */
        const val VR_GAME = 401

        /**
         * 宇宙
         */
        const val UNIVERSE = 402

        /**
         * 空想科学
         */
        const val FANTASY_SCIENCE = 403

        /**
         * パニック
         */
        const val PANIC = 404

        /**
         * 童話
         */
        const val FAIRY_TALE = 9901

        /**
         * 詩
         */
        const val POETRY = 9902

        /**
         * エッセイ
         */
        const val ESSAY = 9903

        /**
         * リプレイ
         */
        const val REPLAY = 9904

        /**
         * その他
         */
        const val OTHER = 9999

        /**
         * ノンジャンル
         */
        const val NON_GENRE = 9801
    }

    object Type {
        const val SHORT = "t"
        const val IN_SERIAL = "r"
        const val COMPLETED = "e"

        fun typeToString(type: String) =
            when (type) {
                "t" -> "단편"
                "r" -> "연재 중"
                "e" -> "완결"
                else -> "?"
            }
    }

    object Order {
        /**
         * 新着更新順
         */
        const val RECENT_RENEW = "new"

        /**
         * 週間ユニークアクセスが多い順
         */
        const val WEEKLY_ACCESS = "weekly"

        /**
         * ブックマーク登録の多い順
         */
        const val BOOKMARK_COUNT = "favnovelcnt"

        /**
         * レビューの多い順
         */
        const val REVIEW_COUNT = "reviewcnt"

        /**
         * 総合ポイントの高い順
         */
        const val TOTAL_POINT = "hyoka"

        /**
         * 日間ポイントの高い順
         */
        const val DAILY_POINT = "dailypoint"

        /**
         * 週間ポイントの高い順
         */
        const val WEEKLY_POINT = "weeklypoint"

        /**
         * 月間ポイントの高い順
         */
        const val MONTHLY_POINT = "monthlypoint"

        /**
         * 四半期ポイントの高い順
         */
        const val QUARTER_POINT = "quarterpoint"

        /**
         * 年間ポイントの高い順
         */
        const val YEARLY_POINT = "yearlypoint"

        /**
         * 評価者数の多い順
         */
        const val EVALUATOR_COUNT = "hyokacnt"

        /**
         * 文字数の多い順
         */
        const val CHARACTER_COUNT = "lengthdesc"

        /**
         * 新着投稿順
         */
        const val RECENT_CREATE = "ncodedesc"

        /**
         * 更新が古い順
         */
        const val OLD_RENEW = "old"
    }

    fun search(
        wordInclude: String = "",
        wordExclude: String = "",
        genres: List<Int> = listOf(),
        types: List<String> = listOf(),
        minTime: Int? = null,
        maxTime: Int? = null,
        minLen: Int? = null,
        maxLen: Int? = null,
        minGlobalPoint: Int? = null,
        maxGlobalPoint: Int? = null,
        minLastUp: String = "",
        maxLastUp: String = "",
        minFirstUp: String = "",
        maxFirstUp: String = "",
        order: String = "new",
        others: Map<String, String> = mapOf(),
        page: Int = 1,
    ): List<YomouSearchResult> {
        return mService.search(
            wordInclude,
            wordExclude,
            genres.joinToString("-"),
            types.joinToString(""),
            minTime?.toString() ?: "",
            maxTime?.toString() ?: "",
            minLen?.toString() ?: "",
            maxLen?.toString() ?: "",
            minGlobalPoint?.toString() ?: "",
            maxGlobalPoint?.toString() ?: "",
            minLastUp,
            maxLastUp,
            minFirstUp,
            maxFirstUp,
            order,
            others,
            page,
        ).execute().body()?.document?.let { doc ->
            val aTagInnerRegex =
                """<a[\s\S]*?>(.*?)</a>""".toRegex()
            val writerAndNcodeRegex =
                """作者：(?:<a[\s\S]*?>(.*?)</a>|.*?)／[\s\S]*?／Nコード：([Nn]\d{4}[A-Za-z]{1,2})""".toRegex()

            doc.select(".searchkekka_box").map { searchResult ->
                arrayOf(
                    TranslationWrapper(searchResult.select(".novel_h > a").text()), // title
                    *writerAndNcodeRegex.find(searchResult.html())?.groupValues?.slice(1..2)
                        ?.toTypedArray() // writer, ncode
                        ?: throw IllegalStateException("Couldn't get writer or ncode"),
                    *searchResult.select("table > tbody > tr > td:first-child").text()
                        .let { publishInfo ->
                            val regex =
                                """(短編|連載中|完結済)(?:[\s\S]*?\(全(\d+)部分\))?""".toRegex()

                            regex.find(publishInfo)?.groupValues?.let {
                                arrayOf(
                                    when (it[1]) {
                                        "短編" -> Type.SHORT
                                        "連載中" -> Type.IN_SERIAL
                                        "完結済" -> Type.COMPLETED
                                        else -> -1
                                    }, // status
                                    if (it[2].isEmpty()) 1 else it[2].toInt() // episodes
                                )
                            } ?: throw IllegalStateException("Couldn't get publish info")
                        },
                    *searchResult.select("table > tbody > tr > td:nth-child(2)").let { detail ->
                        arrayOf(
                            TranslationWrapper(detail.select("div.ex").text()), // description
                            *detail.html().split("<br>").let { advancedDetail ->
                                arrayOf(
                                    TranslationWrapper(
                                        aTagInnerRegex.find(advancedDetail[0])?.groupValues?.get(1)
                                            ?: throw IllegalStateException("Couldn't get genre")
                                    ), // genre
                                    aTagInnerRegex.findAll(advancedDetail[1])
                                        .map { TranslationWrapper(it.groupValues[1]) }
                                        .toList(), // keywords
                                )
                            }
                        )
                    }
                ).let {
                    YomouSearchResult::class.primaryConstructor?.call(*it)
                        ?: throw NoSuchFieldException("Couldn't find YomouSearchResult primary constructor")
                }
            }
        } ?: throw IllegalStateException("Couldn't get search result document")
    }
}

interface YomouService {
    @GET("search.php")
    fun search(
        @Query("word") wordInclude: String,
        @Query("notword") wordExclude: String,
        @Query("genre") genre: String,
        @Query("type") type: String,
        @Query("mintime") minTime: String,
        @Query("maxtime") maxTime: String,
        @Query("minlen") minLen: String,
        @Query("maxlen") maxLen: String,
        @Query("min_globalpoint") minGlobalPoint: String,
        @Query("max_globalpoint") maxGlobalPoint: String,
        @Query("minlastup") minLastUp: String,
        @Query("maxlastup") maxLastUp: String,
        @Query("minfirstup") minFirstUp: String,
        @Query("maxfirstup") maxFirstUp: String,
        @Query("order") order: String,
        @QueryMap others: Map<String, String>,
        @Query("p") page: Int,
    ): Call<Page>
}

data class Page(val document: Document)

internal class PageAdapter : Converter<ResponseBody, Page> {
    override fun convert(responseBody: ResponseBody): Page {
        val document: Document = Jsoup.parse(responseBody.string())
        return Page(document)
    }

    companion object {
        val FACTORY: Converter.Factory = object : Converter.Factory() {
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<Annotation>,
                retrofit: Retrofit
            ): Converter<ResponseBody, *> {
                return PageAdapter()
            }
        }
    }
}
