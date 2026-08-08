package com.example.engine

import com.example.model.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SignalIntelligenceEngine {

    // Configurable product assumptions for scoring weights (hackathon defaults)
    data class EngineConfig(
        val maxAttendancePoints: Int = 30,
        val maxAcademicPoints: Int = 30,
        val maxFeePoints: Int = 20,
        val maxCrossSignalPoints: Int = 20,
        val onTrackThreshold: Int = 29,
        val attentionThreshold: Int = 49,
        val elevatedThreshold: Int = 69
    )

    var config = EngineConfig()

    fun calculateRiskAssessment(
        studentId: String,
        attendanceHistory: List<Double>, // percentages e.g. [92.0, 89.0, 85.0, 78.0, 68.0]
        academicHistory: List<Double>,   // test score % e.g. [76.0, 73.0, 70.0, 64.0, 59.0]
        feeHistory: List<String>          // status e.g. ["Paid", "Paid", "Paid", "Delayed", "Delayed"]
    ): RiskAssessment {
        
        // 1. Attendance Analysis (0 - 30 points)
        val currentAttendance = attendanceHistory.lastOrNull() ?: 100.0
        val initialAttendance = attendanceHistory.firstOrNull() ?: 100.0
        val attendanceChange = currentAttendance - initialAttendance
        val attendanceTrend = determineTrend(attendanceHistory)
        
        var attendanceScore = 0
        if (currentAttendance < 70) attendanceScore += 15
        else if (currentAttendance < 80) attendanceScore += 10
        else if (currentAttendance < 85) attendanceScore += 5

        if (attendanceChange <= -20) attendanceScore += 15
        else if (attendanceChange <= -10) attendanceScore += 10
        else if (attendanceChange <= -5) attendanceScore += 5
        
        attendanceScore = attendanceScore.coerceAtMost(config.maxAttendancePoints)

        val attendanceSignalTrend = SignalTrend(
            currentValue = "${currentAttendance.toInt()}%",
            previousValue = "${initialAttendance.toInt()}%",
            changePercentage = attendanceChange,
            trend = attendanceTrend,
            description = when {
                attendanceChange <= -15 -> "Significant decline of ${Math.abs(attendanceChange.toInt())}% over recent weeks"
                attendanceChange < 0 -> "Gradual decline of ${Math.abs(attendanceChange.toInt())}% over recent weeks"
                attendanceChange > 0 -> "Improved by ${attendanceChange.toInt()}% over recent weeks"
                else -> "Attendance remains steady at ${currentAttendance.toInt()}%"
            },
            historyValues = attendanceHistory
        )

        // 2. Academic Performance Analysis (0 - 30 points)
        val currentAcademic = academicHistory.lastOrNull() ?: 100.0
        val initialAcademic = academicHistory.firstOrNull() ?: 100.0
        val academicChange = currentAcademic - initialAcademic
        val academicTrend = determineTrend(academicHistory)

        var academicScore = 0
        if (currentAcademic < 60) academicScore += 15
        else if (currentAcademic < 70) academicScore += 10
        else if (currentAcademic < 80) academicScore += 5

        if (academicChange <= -15) academicScore += 15
        else if (academicChange <= -10) academicScore += 10
        else if (academicChange <= -5) academicScore += 5

        academicScore = academicScore.coerceAtMost(config.maxAcademicPoints)

        val academicSignalTrend = SignalTrend(
            currentValue = "${currentAcademic.toInt()}%",
            previousValue = "${initialAcademic.toInt()}%",
            changePercentage = academicChange,
            trend = academicTrend,
            description = when {
                academicChange <= -15 -> "Assessment scores dropped by ${Math.abs(academicChange.toInt())}% points"
                academicChange < 0 -> "Scores declined by ${Math.abs(academicChange.toInt())}% points"
                academicChange > 0 -> "Performance increased by ${academicChange.toInt()}% points"
                else -> "Assessment average steady at ${currentAcademic.toInt()}%"
            },
            historyValues = academicHistory
        )

        // 3. Fee Payment Pattern Analysis (0 - 20 points)
        val recentDelays = feeHistory.count { it.equals("Delayed", ignoreCase = true) || it.equals("Pending", ignoreCase = true) }
        val feeStatusCurrent = feeHistory.lastOrNull() ?: "Paid"
        val feeTrend = if (recentDelays >= 2) TrendDirection.DECLINING else if (recentDelays == 1) TrendDirection.STABLE else TrendDirection.IMPROVING

        var feeScore = 0
        if (feeStatusCurrent.equals("Delayed", ignoreCase = true)) {
            feeScore += 12
        } else if (feeStatusCurrent.equals("Pending", ignoreCase = true)) {
            feeScore += 6
        }
        if (recentDelays >= 2) {
            feeScore += 8
        } else if (recentDelays == 1) {
            feeScore += 4
        }
        feeScore = feeScore.coerceAtMost(config.maxFeePoints)

        val feeSignalTrend = SignalTrend(
            currentValue = feeStatusCurrent,
            previousValue = feeHistory.firstOrNull() ?: "Paid",
            changePercentage = if (recentDelays > 0) -100.0 else 0.0,
            trend = feeTrend,
            description = when {
                recentDelays >= 2 -> "$recentDelays recent fee payment installments were delayed"
                recentDelays == 1 -> "1 recent fee payment installment was delayed"
                else -> "Fee payments are up to date and current"
            },
            historyValues = feeHistory.map { if (it.equals("Paid", ignoreCase = true)) 100.0 else 50.0 }
        )

        // 4. Cross-Signal Deterioration (0 - 20 points)
        val isAttendanceDeclining = attendanceTrend == TrendDirection.DECLINING || attendanceTrend == TrendDirection.SIGNIFICANT_DECLINE
        val isAcademicDeclining = academicTrend == TrendDirection.DECLINING || academicTrend == TrendDirection.SIGNIFICANT_DECLINE
        val isFeeDelayed = recentDelays > 0

        var crossSignalCount = 0
        if (isAttendanceDeclining) crossSignalCount++
        if (isAcademicDeclining) crossSignalCount++
        if (isFeeDelayed) crossSignalCount++

        val crossSignalDeterioration = crossSignalCount >= 2
        val crossSignalScore = when (crossSignalCount) {
            3 -> 20
            2 -> 14
            else -> 0
        }.coerceAtMost(config.maxCrossSignalPoints)

        // Calculate Total Risk Score (0 - 100)
        val totalScore = (attendanceScore + academicScore + feeScore + crossSignalScore).coerceIn(0, 100)

        val level = when {
            totalScore <= config.onTrackThreshold -> EarlyWarningLevel.ON_TRACK
            totalScore <= config.attentionThreshold -> EarlyWarningLevel.ATTENTION
            totalScore <= config.elevatedThreshold -> EarlyWarningLevel.ELEVATED
            else -> EarlyWarningLevel.EARLY_WARNING
        }

        // Build Evidence & Contributing Signals
        val contributingSignals = mutableListOf<ContributingSignal>()
        val evidenceList = mutableListOf<String>()

        if (attendanceScore > 0) {
            contributingSignals.add(
                ContributingSignal(
                    signalName = "Attendance",
                    scoreImpact = attendanceScore,
                    statusText = "Attendance decreased from ${initialAttendance.toInt()}% to ${currentAttendance.toInt()}%",
                    trend = attendanceTrend
                )
            )
            evidenceList.add("Attendance decreased from ${initialAttendance.toInt()}% to ${currentAttendance.toInt()}% over recent weeks.")
        }

        if (academicScore > 0) {
            contributingSignals.add(
                ContributingSignal(
                    signalName = "Academic Performance",
                    scoreImpact = academicScore,
                    statusText = "Average assessment performance decreased from ${initialAcademic.toInt()}% to ${currentAcademic.toInt()}%",
                    trend = academicTrend
                )
            )
            evidenceList.add("Average assessment performance decreased from ${initialAcademic.toInt()}% to ${currentAcademic.toInt()}%.")
        }

        if (feeScore > 0) {
            contributingSignals.add(
                ContributingSignal(
                    signalName = "Fee Payment Pattern",
                    scoreImpact = feeScore,
                    statusText = "$recentDelays recent fee payment installments were delayed",
                    trend = feeTrend
                )
            )
            evidenceList.add("$recentDelays recent fee payment installments were delayed.")
        }

        if (crossSignalDeterioration) {
            contributingSignals.add(
                ContributingSignal(
                    signalName = "Cross-Signal Pattern",
                    scoreImpact = crossSignalScore,
                    statusText = "Multiple indicators changed during the same period",
                    trend = TrendDirection.SIGNIFICANT_DECLINE
                )
            )
            evidenceList.add("Multiple signals (attendance, academics, fees) deteriorated simultaneously during the same period.")
        }

        val trendSummary = when {
            crossSignalDeterioration -> "Multiple signals suggest this student may benefit from timely educational or social support."
            level == EarlyWarningLevel.ON_TRACK -> "Student signals are stable and on track."
            else -> "Student shows emerging patterns in individual signals that warrant gentle attention."
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        return RiskAssessment(
            studentId = studentId,
            score = totalScore,
            level = level,
            attendanceTrend = attendanceSignalTrend,
            academicTrend = academicSignalTrend,
            feeTrend = feeSignalTrend,
            crossSignalDeterioration = crossSignalDeterioration,
            contributingSignals = contributingSignals,
            evidenceList = evidenceList,
            generatedAt = dateFormat.format(Date()),
            trendSummary = trendSummary
        )
    }

    private fun determineTrend(history: List<Double>): TrendDirection {
        if (history.size < 2) return TrendDirection.STABLE
        val first = history.first()
        val last = history.last()
        val diff = last - first

        return when {
            diff <= -15 -> TrendDirection.SIGNIFICANT_DECLINE
            diff < -3 -> TrendDirection.DECLINING
            diff >= 5 -> TrendDirection.IMPROVING
            else -> TrendDirection.STABLE
        }
    }
}
