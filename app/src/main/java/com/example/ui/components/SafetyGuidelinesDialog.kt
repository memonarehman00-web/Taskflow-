package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.PrimaryDark
import com.example.ui.theme.PrimaryGreen

@Composable
fun SafetyGuidelinesDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(20.dp)),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "سنڌي رشتو تحفظ ۽ اخلاقي ضابطا",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryDark,
                            fontSize = 16.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                SafetyRuleItem(
                    title = "1. قانوني ۽ شرعي نڪاح",
                    text = "هي پليٽ فارم رڳو 18 سال يا ان کان مٿي عمر وارن سنجيده مردن ۽ عورتن جي شرعي شاديءَ لاءِ آهي. غير سنجيده يا ڪاروباري گفتگو جي سخت ممانعت آهي."
                )

                SafetyRuleItem(
                    title = "2. والدين جي سرپرستي ۽ باضابطه رابطو",
                    text = "سڀ رابطا والدين ۽ گهرڀاتين جي وساطت ۽ باهمي رضامندي بعد ئي ڏنا ويندا آهن، جيئن خانداني وقار هميشه محفوظ رهي."
                )

                SafetyRuleItem(
                    title = "3. تصويرن جي مڪمل رازداري (Privacy Blur)",
                    text = "خاص ڪري سنڌي نياڻين ۽ معزز گهرڀاتين جي تصويرن کي بلر رکڻ جي اجازت آهي، جيڪا فقط تصديق ٿيل ۽ باوقار ناتي لاءِ پڌري ڪئي ويندي."
                )

                SafetyRuleItem(
                    title = "4. غير مناسب روئي تي زيرو ٽالرنس",
                    text = "ڪنهن به غير اخلاقي، جعلي يا بليڪ ميلنگ واري عمل تي لاڳاپيل واهپيدار کي بنا دير هميشه لاءِ بلاڪ ۽ رپورٽ ڪيو ويندو."
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "سمجھي ويس (قبول آهي)",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun SafetyRuleItem(title: String, text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FAFC))
            .padding(10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryDark,
                fontSize = 13.sp
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF475569),
                lineHeight = 18.sp,
                fontSize = 11.5.sp
            )
        )
    }
}
