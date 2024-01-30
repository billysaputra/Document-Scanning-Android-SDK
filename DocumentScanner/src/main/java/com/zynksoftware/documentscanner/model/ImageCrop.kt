package com.zynksoftware.documentscanner.model

import android.graphics.Point
import android.graphics.PointF
import java.io.Serializable

data class ImageCrop(
    val imageUri: String,
    val xMin: Int,
    val yMin: Int,
    val xMax: Int,
    val yMax: Int
) : Serializable {
    fun getPointF(): Map<Int, PointF> {
        val resultPointEdge = mutableMapOf<Int, PointF>()
        resultPointEdge[0] = PointF(xMin.toFloat(), yMin.toFloat()) // TOP LEFT
        resultPointEdge[1] = PointF(xMin.toFloat(), yMax.toFloat()) // BOTTOM LEFT
        resultPointEdge[2] = PointF(xMax.toFloat(), yMin.toFloat()) // TOP RIGHT
        resultPointEdge[3] = PointF(xMax.toFloat(), yMax.toFloat()) // BOTTOM RIGHT
        return resultPointEdge
    }
}