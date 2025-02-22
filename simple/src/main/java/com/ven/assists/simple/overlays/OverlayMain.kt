package com.ven.assists.simple.overlays

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Path
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.ven.assists.Assists
import com.ven.assists.Assists.click
import com.ven.assists.Assists.longClick
import com.ven.assists.Assists.nodeGestureClick
import com.ven.assists.Assists.scrollBackward
import com.ven.assists.Assists.scrollForward
import com.ven.assists.Assists.selectionText
import com.ven.assists.Assists.setNodeText
import com.ven.assists.AssistsServiceListener
import com.ven.assists.AssistsWindowManager
import com.ven.assists.AssistsWindowWrapper
import com.ven.assists.simple.MultiTouchDrawingActivity
import com.ven.assists.simple.TestActivity
import com.ven.assists.simple.databinding.MainControlBinding
import com.ven.assists.simple.databinding.MainOverlayBinding
import com.ven.assists.utils.CoroutineWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin

object OverlayMain : AssistsServiceListener {

    @SuppressLint("StaticFieldLeak")
    var viewBinding: MainOverlayBinding? = null
        private set
        get() {
            if (field == null) {
                field = MainOverlayBinding.inflate(LayoutInflater.from(Assists.service)).apply {
                    //点击
                    btnClick.setOnClickListener {
                        CoroutineWrapper.launch {
                            ActivityUtils.getTopActivity()?.startActivity(Intent(Assists.service, TestActivity::class.java))
                            delay(1000)
                            Assists.findById("com.ven.assists.demo:id/btn_test").firstOrNull()?.click()
                        }
                    }
                    //手势点击
                    btnGestureClick.setOnClickListener {
                        CoroutineWrapper.launch {
                            ActivityUtils.getTopActivity()?.startActivity(Intent(Assists.service, TestActivity::class.java))
                            delay(1000)
                            Assists.findById("com.ven.assists.demo:id/btn_test").firstOrNull()?.nodeGestureClick()
                        }
                    }
                    //长按
                    btnLongClick.setOnClickListener {
                        CoroutineWrapper.launch {
                            ActivityUtils.getTopActivity()?.startActivity(Intent(Assists.service, TestActivity::class.java))
                            delay(1000)
                            Assists.findById("com.ven.assists.demo:id/btn_test").firstOrNull()?.longClick()
                        }
                    }
                    //手势长按
                    btnGestureLongClick.setOnClickListener {
                        CoroutineWrapper.launch {
                            ActivityUtils.getTopActivity()?.startActivity(Intent(Assists.service, TestActivity::class.java))
                            delay(1000)
                            Assists.findById("com.ven.assists.demo:id/btn_test").firstOrNull()?.nodeGestureClick(duration = 1000)
                        }
                    }
                    //单指手势（画圆）
                    btnGestureSingleDraw.setOnClickListener {
                        CoroutineWrapper.launch(isMain = true) {
                            ActivityUtils.getTopActivity()?.startActivity(Intent(Assists.service, MultiTouchDrawingActivity::class.java))
                            delay(1000)

                            performCircularGestureSingle()
                        }
                    }
                    //双指手势（画圆）
                    btnGestureDoubleDraw.setOnClickListener {
                        CoroutineWrapper.launch(isMain = true) {
                            ActivityUtils.getTopActivity()?.startActivity(Intent(Assists.service, MultiTouchDrawingActivity::class.java))
                            delay(1000)

                            performCircularGestureDouble()
                        }
                    }
                    //单指手势（不规则）
                    btnGestureThreeDraw.setOnClickListener {
                        CoroutineWrapper.launch(isMain = true) {
                            ActivityUtils.getTopActivity()?.startActivity(Intent(Assists.service, MultiTouchDrawingActivity::class.java))
                            delay(1000)

                            performSnakeGesture()
                        }
                    }
                    //选择文本
                    btnSelectText.setOnClickListener {
                        CoroutineWrapper.launch(isMain = true) {
                            ActivityUtils.getTopActivity()?.startActivity(Intent(Assists.service, TestActivity::class.java))
                            delay(1000)

                            TestActivity.scrollUp?.invoke()
                            delay(500)

                            Assists.findById("com.ven.assists.demo:id/et_input").firstOrNull()?.let {
                                it.selectionText(it.text.length - 3, it.text.length)
                            }
                        }

                    }
                    //修改文本
                    btnChangeText.setOnClickListener {
                        CoroutineWrapper.launch(isMain = true) {
                            ActivityUtils.getTopActivity()?.startActivity(Intent(Assists.service, TestActivity::class.java))
                            delay(1000)

                            TestActivity.scrollUp?.invoke()
                            delay(500)
                            Assists.findById("com.ven.assists.demo:id/et_input").firstOrNull()?.let {
                                it.setNodeText("测试修改文本: ${TimeUtils.getNowString()}")
                            }
                        }
                    }
                    //向前滚动
                    btnListScroll.setOnClickListener {
                        CoroutineWrapper.launch(isMain = true) {
                            ActivityUtils.getTopActivity()?.startActivity(Intent(Assists.service, TestActivity::class.java))
                            delay(1000)

                            TestActivity.scrollUp?.invoke()
                            delay(500)
                            var next = true
                            while (next) {
                                Assists.findById("com.ven.assists.demo:id/scrollView").firstOrNull()?.let {
                                    next = it.scrollForward()
                                    delay(1000)
                                }
                            }
                            ToastUtils.showShort("已滚动到底部")
                        }

                    }
                    //向后滚动
                    btnListScrollBack.setOnClickListener {
                        CoroutineWrapper.launch(isMain = true) {
                            ActivityUtils.getTopActivity()?.startActivity(Intent(Assists.service, TestActivity::class.java))
                            delay(1000)

                            TestActivity.scrollDown?.invoke()
                            delay(500)
                            var next = true
                            while (next) {
                                Assists.findById("com.ven.assists.demo:id/scrollView").firstOrNull()?.let {
                                    next = it.scrollBackward()
                                    delay(1000)
                                }
                            }
                            ToastUtils.showShort("已滚动到顶部")
                        }

                    }

                    //返回
                    btnBack.setOnClickListener {
                        Assists.back()
                    }
                    //桌面
                    btnHome.setOnClickListener { Assists.home() }
                    //通知
                    btnTask.setOnClickListener { Assists.tasks() }
                    //最新任务
                    btnNotification.setOnClickListener { Assists.notifications() }

                    btnPowerDialog.setOnClickListener {
                        Assists.service?.performGlobalAction(AccessibilityService.GLOBAL_ACTION_POWER_DIALOG)

                    }
                    btnToggleSplitScreen.setOnClickListener {
                        Assists.service?.performGlobalAction(AccessibilityService.GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN)

                    }
                    btnLockScreen.setOnClickListener {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            Assists.service?.performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN)
                        } else {
                            ToastUtils.showShort("仅支持Android9及以上版本")
                        }
                    }
                    btnTakeScreenshot.setOnClickListener {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            Assists.service?.performGlobalAction(AccessibilityService.GLOBAL_ACTION_TAKE_SCREENSHOT)
                        } else {
                            ToastUtils.showShort("仅支持Android9及以上版本")
                        }
                    }
                    btn1.setOnClickListener {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            Assists.service?.performGlobalAction(AccessibilityService.GLOBAL_ACTION_KEYCODE_HEADSETHOOK)
                        } else {
                            ToastUtils.showShort("仅支持Android12及以上版本")
                        }
                    }
                    btn2.setOnClickListener {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            Assists.service?.performGlobalAction(AccessibilityService.GLOBAL_ACTION_ACCESSIBILITY_ALL_APPS)
                        } else {
                            ToastUtils.showShort("仅支持Android12及以上版本")
                        }
                    }
                }
            }
            return field
        }

    var onClose: ((parent: View) -> Unit)? = null

    var showed = false
        private set
        get() {
            assistWindowWrapper?.let {
                return AssistsWindowManager.isVisible(it.getView())
            } ?: return false
        }

    var assistWindowWrapper: AssistsWindowWrapper? = null
        private set
        get() {
            viewBinding?.let {
                if (field == null) {
                    field = AssistsWindowWrapper(it.root, wmLayoutParams = AssistsWindowManager.createLayoutParams().apply {
                        width = (ScreenUtils.getScreenWidth() * 0.8).toInt()
                        height = (ScreenUtils.getScreenHeight() * 0.5).toInt()
                    }, onClose = this.onClose).apply {
                        minWidth = (ScreenUtils.getScreenWidth() * 0.6).toInt()
                        minHeight = (ScreenUtils.getScreenHeight() * 0.4).toInt()
                        initialCenter = true
                    }
                }
            }
            return field
        }

    fun show() {
        if (!Assists.serviceListeners.contains(this)) {
            Assists.serviceListeners.add(this)
        }
        AssistsWindowManager.add(assistWindowWrapper)
    }

    fun hide() {
        AssistsWindowManager.removeView(assistWindowWrapper?.getView())
    }

    override fun onUnbind() {
        viewBinding = null
        assistWindowWrapper = null
    }

    suspend fun performCircularGestureSingle() {
        val screenWidth = ScreenUtils.getScreenWidth()
        val screenHeight = ScreenUtils.getScreenHeight()
        val centerX = screenWidth / 2f
        val centerY = screenHeight / 2f
        val radius1 = 200f // 第一个圆的半径

        // 创建第一个圆的路径
        val path1 = Path()
        path1.addCircle(centerX, centerY, radius1, Path.Direction.CW)

        // 创建两个手势描述
        val stroke1 = GestureDescription.StrokeDescription(path1, 0, 2000)

        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(stroke1)

        // 分发手势
        Assists.dispatchGesture(gestureBuilder.build(), null, null)
    }

    suspend fun performCircularGestureDouble() {
        val screenWidth = ScreenUtils.getScreenWidth()
        val screenHeight = ScreenUtils.getScreenHeight()
        val centerX = screenWidth / 2f
        val centerY = screenHeight / 2f
        val radius1 = 200f // 第一个圆的半径
        val radius2 = 300f // 第二个圆的半径

        // 创建第一个圆的路径
        val path1 = Path()
        path1.addCircle(centerX, centerY, radius1, Path.Direction.CW)

        // 创建第二个圆的路径
        val path2 = Path()
        path2.addCircle(centerX, centerY, radius2, Path.Direction.CW)

        // 创建两个手势描述
        val stroke1 = GestureDescription.StrokeDescription(path1, 0, 2000)
        val stroke2 = GestureDescription.StrokeDescription(path2, 0, 2000)

        // 创建手势构建器并添加两个手势
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(stroke1)
        gestureBuilder.addStroke(stroke2)

        // 分发手势
        Assists.dispatchGesture(gestureBuilder.build(), null, null)
    }

    suspend fun performSnakeGesture() {
        val screenWidth = ScreenUtils.getScreenWidth()
        val screenHeight = ScreenUtils.getScreenHeight()
        val segmentHeight = 200f // 每段路径的垂直高度
        val maxHorizontalOffset = 300f // 水平方向的最大偏移量

        val path = Path()
        var currentY = BarUtils.getStatusBarHeight() + BarUtils.getActionBarHeight().toFloat() + 100f
        var currentX = screenWidth / 2f // 从屏幕中间开始

        // 移动到起点
        path.moveTo(currentX, currentY)

        // 生成蛇形路径
        while (currentY < screenHeight) {
            // 随机生成水平偏移量
            val offsetX = (Math.random() * maxHorizontalOffset * 2 - maxHorizontalOffset).toFloat()
            currentX += offsetX
            currentY += segmentHeight

            // 确保 X 坐标在屏幕范围内
            currentX = currentX.coerceIn(0f, screenWidth.toFloat())

            // 添加路径点
            path.lineTo(currentX, currentY)
        }

        // 创建手势描述
        val stroke = GestureDescription.StrokeDescription(path, 0, 5000) // 5秒完成手势
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(stroke)

        // 分发手势
        Assists.dispatchGesture(gestureBuilder.build(), null, null)
    }
}