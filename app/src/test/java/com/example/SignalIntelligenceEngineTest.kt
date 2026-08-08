package com.example

import com.example.engine.SignalIntelligenceEngine
import com.example.model.EarlyWarningLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SignalIntelligenceEngineTest {

    @Test
    fun `test Amina Khan multi-signal deterioration risk calculation`() {
        val attendance = listOf(92.0, 89.0, 85.0, 78.0, 68.0)
        val academic = listOf(76.0, 73.0, 70.0, 64.0, 59.0)
        val fees = listOf("Paid", "Paid", "Paid", "Delayed", "Delayed")

        val assessment = SignalIntelligenceEngine.calculateRiskAssessment("STU-001", attendance, academic, fees)

        assertEquals(EarlyWarningLevel.EARLY_WARNING, assessment.level)
        assertTrue("Score should be 70 or higher for early warning", assessment.score >= 70)
        assertTrue("Cross signal deterioration should be detected", assessment.crossSignalDeterioration)
        assertTrue("Evidence should contain attendance decrease", assessment.evidenceList.any { it.contains("Attendance decreased") })
    }

    @Test
    fun `test stable student risk calculation`() {
        val attendance = listOf(96.0, 95.0, 98.0, 96.0, 97.0)
        val academic = listOf(88.0, 90.0, 92.0, 89.0, 91.0)
        val fees = listOf("Paid", "Paid", "Paid", "Paid", "Paid")

        val assessment = SignalIntelligenceEngine.calculateRiskAssessment("STU-002", attendance, academic, fees)

        assertEquals(EarlyWarningLevel.ON_TRACK, assessment.level)
        assertTrue("Score should be low for stable student", assessment.score <= 29)
    }
}
