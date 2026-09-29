package com.privateuseless.brightnesswidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class BrightnessWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(
                context.packageName,
                R.layout.brightness_widget
            )

            appWidgetManager.updateAppWidget(
                appWidgetId,
                views
            )
        }
    }
}
