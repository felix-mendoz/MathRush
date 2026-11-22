package com.example.mathrush.ui.components

import android.annotation.SuppressLint
import android.graphics.Color
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.json.JSONObject

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MathView(
    latex: String,
    modifier: Modifier = Modifier,
    displayMode: Boolean = true
) {
    var isPageLoaded by remember { mutableStateOf(false) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.allowFileAccess = true
                settings.allowContentAccess = true
                settings.allowFileAccessFromFileURLs = true
                settings.allowUniversalAccessFromFileURLs = true

                setBackgroundColor(Color.TRANSPARENT)
                isVerticalScrollBarEnabled = false
                isHorizontalScrollBarEnabled = false
                overScrollMode = WebView.OVER_SCROLL_NEVER

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        isPageLoaded = true
                        view?.let {
                            renderLatex(it, latex, displayMode)
                        }
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: android.webkit.WebResourceRequest?
                    ): Boolean = true
                }

                loadUrl("file:///android_asset/katex/katex_viewer.html")
                webViewRef = this
            }
        },
        update = { webView ->
            if (isPageLoaded) {
                renderLatex(webView, latex, displayMode)
            }
        }
    )
}

private fun renderLatex(
    webView: WebView,
    latex: String,
    displayMode: Boolean
) {
    val escapedLatex = JSONObject.quote(latex)
    webView.evaluateJavascript("renderMath($escapedLatex, \"$displayMode\");", null)
}
