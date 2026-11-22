package com.example.mathrush.ui.components

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MathView(
    latex: String,
    modifier: Modifier = Modifier,
    displayMode: Boolean = true
) {
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = false
                settings.allowFileAccess = true
                settings.allowContentAccess = false
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: android.webkit.WebResourceRequest?): Boolean {
                        return true
                    }
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        val escapedLatex = latex.replace("\\", "\\\\").replace("\"", "\\\"")
                        view?.evaluateJavascript("javascript:renderMath(\"$escapedLatex\", \"$displayMode\");", null)
                    }
                }
                loadUrl("file:///android_asset/katex/katex_viewer.html")
                webViewInstance = this
            }
        },
        update = { webView ->
            val escapedLatex = latex.replace("\\", "\\\\").replace("\"", "\\\"")
            webView.evaluateJavascript("javascript:renderMath(\"$escapedLatex\", \"$displayMode\");", null)
        }
    )
}
