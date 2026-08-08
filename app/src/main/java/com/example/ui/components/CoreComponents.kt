package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EarlyWarningLevel
import com.example.model.TrendDirection
import com.example.ui.theme.*

@Composable
fun HeaderBar(
    currentRole: String, // "Teacher" or "Admin"
    onRoleToggle: () -> Unit,
    onResetDemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Emerald900,
        contentColor = Color.White,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Gold500,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "پر",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Emerald900
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PARWAAZ-E-ILM",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "پروازِ علم • See the Signs. Support the Journey.",
                        fontSize = 11.sp,
                        color = Emerald100,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Role Selector Chip
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Emerald800,
                        onClick = onRoleToggle,
                        modifier = Modifier.testTag("role_toggle_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Switch Role",
                                tint = Gold500,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentRole,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = onResetDemo,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("reset_demo_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Demo Data",
                            tint = Emerald100,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RiskBadge(
    level: EarlyWarningLevel,
    modifier: Modifier = Modifier,
    showUrdu: Boolean = true
) {
    val (bgColor, textColor) = when (level) {
        EarlyWarningLevel.ON_TRACK -> Pair(RiskOnTrackBg, RiskOnTrack)
        EarlyWarningLevel.ATTENTION -> Pair(RiskAttentionBg, RiskAttention)
        EarlyWarningLevel.ELEVATED -> Pair(RiskElevatedBg, RiskElevated)
        EarlyWarningLevel.EARLY_WARNING -> Pair(RiskEarlyWarningBg, RiskEarlyWarning)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.3f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = level.displayName.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            if (showUrdu) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "• ${level.urduText}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Composable
fun TrendIndicator(
    trend: TrendDirection,
    modifier: Modifier = Modifier
) {
    val (symbol, color, label) = when (trend) {
        TrendDirection.IMPROVING -> Triple("↑", RiskOnTrack, "Improving")
        TrendDirection.STABLE -> Triple("→", Slate500, "Stable")
        TrendDirection.DECLINING -> Triple("↓", RiskAttention, "Declining")
        TrendDirection.SIGNIFICANT_DECLINE -> Triple("↓↓", RiskEarlyWarning, "Significant Decline")
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = symbol,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
}

@Composable
fun ParwaazInsightBanner(
    title: String = "PARWAAZ INSIGHT",
    insightText: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onClick?.invoke() },
        enabled = onClick != null,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Emerald900, Emerald700)
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = CircleShape,
                    color = Gold500.copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Sparkles",
                            tint = Gold500,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gold500,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = insightText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
