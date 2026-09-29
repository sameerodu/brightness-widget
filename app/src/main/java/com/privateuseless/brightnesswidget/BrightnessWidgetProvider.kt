package com.privateuseless.brightnesswidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.RemoteViews

class BrightnessWidgetProvider : AppWidgetProvider() {

    companion object {
        private const val ACTION_SET_BRIGHTNESS =
            "com.privateuseless.brightnesswidget.SET_BRIGHTNESS"

        private const val EXTRA_BRIGHTNESS =
            "brightness_level"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        super.onReceive(context, intent)

        if (intent.action == ACTION_SET_BRIGHTNESS) {
            val brightness = intent.getIntExtra(
                EXTRA_BRIGHTNESS,
                -1
            )

            if (brightness >= 0) {
                setScreenBrightness(
                    context,
                    brightness
                )
            }
        }
    }

    private fun setScreenBrightness(
        context: Context,
        brightnessPercent: Int
    ) {
        if (!Settings.System.canWrite(context)) {
            return
        }

        val brightnessValue = when {
            brightnessPercent <= 0 -> 1
            brightnessPercent >= 100 -> 255
            else -> {
                (brightnessPercent * 255) / 100
            }
        }

        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS,
            brightnessValue
        )
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(
            context.packageName,
            R.layout.brightness_widget
        )

        setBrightnessClick(
            context,
            views,
            R.id.brightness_0,
            0,
            appWidgetId
        )

        setBrightnessClick(
            context,
            views,
            R.id.brightness_35,
            35,
            appWidgetId
        )

        setBrightnessClick(
            context,
            views,
            R.id.brightness_50,
            50,
            appWidgetId
        )

        setBrightnessClick(
            context,
            views,
            R.id.brightness_65,
            65,
            appWidgetId
        )

        setBrightnessClick(
            context,
            views,
            R.id.brightness_100,
            100,
            appWidgetId
        )

        appWidgetManager.updateAppWidget(
            appWidgetId,
            views
        )
    }

    private fun setBrightnessClick(
        context: Context,
        views: RemoteViews,
        viewId: Int,
        brightness: Int,
        appWidgetId: Int
    ) {
        val intent = Intent(
            context,
            BrightnessWidgetProvider::class.java
        ).apply {
            action = ACTION_SET_BRIGHTNESS
            putExtra(EXTRA_BRIGHTNESS, brightness)
            putExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId
            )
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            brightness,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        views.setOnClickPendingIntent(
            viewId,
            pendingIntent
        )
    }
}
