package com.example.linkstore.features.savelink.domain

import android.content.Context
import android.util.Log
import com.example.linkstore.common.utils.ResultData
import com.kedia.ogparser.OpenGraphCallback
import com.kedia.ogparser.OpenGraphParser
import com.kedia.ogparser.OpenGraphResult
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ViewModelScoped
class FetchLinkPreviewDataUseCase @Inject constructor(
    private val context: Context
) {

    operator fun invoke(link: String) = callbackFlow<ResultData<OpenGraphResult, String>> {
        val openGraphCallback = object : OpenGraphCallback {
            override fun onError(error: String) {
                Log.d("OpenGraphStuff", "error: $error")
                trySend(ResultData.Error(error))
                channel.close()
            }

            override fun onPostResponse(openGraphResult: OpenGraphResult) {
                Log.d("OpenGraphStuff", "result: $openGraphResult")
                trySend(ResultData.Success(openGraphResult))
                channel.close()
            }
        }

        val openGraphParser = OpenGraphParser(
            listener = openGraphCallback,
            showNullOnEmpty = true,
            context = context
        )

        openGraphParser.parse(link)
        awaitClose { channel.close() }
    }

}