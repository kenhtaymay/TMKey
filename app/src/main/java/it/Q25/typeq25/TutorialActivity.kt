package it.srik.TypeQ25

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

class TutorialActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TutorialScreen(
                    onComplete = {
                        // Mark tutorial as completed
                        SettingsManager.setTutorialCompleted(this, true)
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TutorialScreen(onComplete: () -> Unit) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val totalPages = 5

    Scaffold(
        bottomBar = {
            Surface(
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onComplete,
                        enabled = pagerState.currentPage < totalPages - 1
                    ) {
                        Text(if (pagerState.currentPage < totalPages - 1) "Bỏ qua" else "")
                    }

                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        activeColor = MaterialTheme.colorScheme.primary
                    )

                    Button(
                        onClick = {
                            if (pagerState.currentPage < totalPages - 1) {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else {
                                onComplete()
                            }
                        }
                    ) {
                        Text(if (pagerState.currentPage < totalPages - 1) "Tiếp" else "Bắt đầu")
                    }
                }
            }
        }
    ) { paddingValues ->
        HorizontalPager(
            count = totalPages,
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> WelcomePage()
                1 -> SetupPage()
                2 -> TelexPage()
                3 -> ModifierKeysPage()
                4 -> ShortcutsPage()
            }
        }
    }
}

@Composable
fun WelcomePage() {
    TutorialPageLayout(
        icon = Icons.Default.Keyboard,
        title = "Chào mừng đến với TMKey",
        subtitle = "Gõ tiếng Việt Telex ngay trên bàn phím cứng của Q25 và P26"
    ) {
        TutorialItem(
            icon = Icons.Default.Translate,
            title = "Telex bật sẵn",
            description = "Cài xong là gõ được tiếng Việt có dấu, không cần bàn phím ảo"
        )
        TutorialItem(
            icon = Icons.Default.KeyboardAlt,
            title = "Làm riêng cho phím cứng",
            description = "Shift, Alt, Ctrl, SYM và các phím tắt được tối ưu cho máy bàn phím QWERTY"
        )
        TutorialItem(
            icon = Icons.Default.Lock,
            title = "Riêng tư",
            description = "Không quảng cáo, không thu thập dữ liệu. Bản trên Google Play không có quyền Internet nên những gì bạn gõ không rời khỏi máy"
        )
    }
}

@Composable
fun SetupPage() {
    TutorialPageLayout(
        icon = Icons.Default.Settings,
        title = "Bật bàn phím",
        subtitle = "Chỉ cần làm một lần"
    ) {
        TutorialItem(
            icon = Icons.Default.ToggleOn,
            title = "1. Bật TMKey",
            description = "Ở màn hình chính, chọn \"Enable TMKey\" rồi gạt công tắc TMKey - Telex"
        )
        TutorialItem(
            icon = Icons.Default.Keyboard,
            title = "2. Chọn làm bàn phím",
            description = "Chọn \"Choose input method\" rồi chọn TMKey - Telex"
        )
        TutorialItem(
            icon = Icons.Default.Edit,
            title = "3. Gõ thử",
            description = "Gõ vào ô thử trên màn hình chính, ví dụ: tieesng vieejt → tiếng việt"
        )
    }
}

@Composable
fun TelexPage() {
    TutorialPageLayout(
        icon = Icons.Default.Translate,
        title = "Gõ Telex",
        subtitle = "Gõ chữ trước, thêm dấu sau"
    ) {
        TutorialItem(
            icon = Icons.Default.TextFields,
            title = "Chữ có mũ, có móc",
            description = "aa → â    ee → ê    oo → ô\naw → ă    ow → ơ    uw → ư\ndd → đ    w → ư"
        )
        TutorialItem(
            icon = Icons.Default.FormatQuote,
            title = "Dấu thanh",
            description = "s → sắc    f → huyền    r → hỏi\nx → ngã    j → nặng    z → xoá dấu\nVí dụ: hoawjc → hoặc, dduwowngf → đường"
        )
        TutorialItem(
            icon = Icons.Default.Undo,
            title = "Gõ lặp để huỷ",
            description = "Gõ phím dấu thêm lần nữa để bỏ dấu và giữ chữ cái, ví dụ: ass → as"
        )
        TutorialItem(
            icon = Icons.Default.SwapHoriz,
            title = "Bật/tắt Telex: Ctrl + Space",
            description = "Một thông báo nhỏ cho biết đang gõ tiếng Việt hay không dấu"
        )
        TutorialItem(
            icon = Icons.Default.Info,
            title = "Tự gõ không dấu ở ô đặc biệt",
            description = "Ô mật khẩu, email và ô số luôn gõ không dấu. Thanh địa chỉ trình duyệt vẫn gõ được tiếng Việt để tìm kiếm"
        )
    }
}

@Composable
fun ModifierKeysPage() {
    TutorialPageLayout(
        icon = Icons.Default.KeyboardAlt,
        title = "Phím bổ trợ",
        subtitle = "Nhấn một lần cho phím kế tiếp, nhấn đúp để khoá"
    ) {
        TutorialItem(
            icon = Icons.Default.ArrowUpward,
            title = "Shift",
            description = "Nhấn một lần: viết hoa chữ kế tiếp\nNhấn đúp: bật/tắt CAPS LOCK"
        )
        TutorialItem(
            icon = Icons.Default.Dialpad,
            title = "Alt",
            description = "Giữ Alt và nhấn phím để ra số hoặc ký tự in trên phím\nÔ chỉ nhập số: gõ thẳng ra số, không cần Alt\nNhấn giữ một phím cũng ra ký tự Alt của phím đó"
        )
        TutorialItem(
            icon = Icons.Default.ControlCamera,
            title = "Ctrl (Shift phải trên Q25)",
            description = "Ctrl + C / X / V / A / Z: chép, cắt, dán, chọn hết, hoàn tác\nCtrl + Backspace: xoá cả từ\nCtrl + E / S / D / F: di chuyển con trỏ"
        )
        TutorialItem(
            icon = Icons.Default.EmojiEmotions,
            title = "SYM",
            description = "Mở bảng emoji và ký hiệu, nhấn phím chữ tương ứng để chèn\nNhấn giữ một biểu tượng để thêm vào Yêu thích"
        )
    }
}

@Composable
fun ShortcutsPage() {
    TutorialPageLayout(
        icon = Icons.Default.Bolt,
        title = "Phím tắt nhanh",
        subtitle = "Dùng máy không cần chạm màn hình"
    ) {
        TutorialItem(
            icon = Icons.Default.VolumeUp,
            title = "Âm lượng",
            description = "SYM + L: tăng    SYM + K: giảm    SYM + $: tắt tiếng"
        )
        TutorialItem(
            icon = Icons.Default.Lightbulb,
            title = "Đèn nền bàn phím",
            description = "SYM + 0: bật/tắt đèn nền"
        )
        TutorialItem(
            icon = Icons.Default.Navigation,
            title = "Chế độ điều hướng",
            description = "Nhấn đúp Ctrl khi không ở ô nhập để dùng E / S / D / F như phím mũi tên. Nhấn Ctrl hoặc Back để thoát"
        )
        TutorialItem(
            icon = Icons.Default.Apps,
            title = "Mở nhanh ứng dụng",
            description = "Ở màn hình chính, nhấn một phím chữ để mở ứng dụng đã gán cho phím đó (Settings → Advanced)"
        )
        TutorialItem(
            icon = Icons.Default.Refresh,
            title = "Xem lại hướng dẫn",
            description = "Settings → Help & About → Tutorial"
        )
    }
}

@Composable
fun TutorialPageLayout(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconTint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(72.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        content()
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun TutorialItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(24.dp)
        )
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
