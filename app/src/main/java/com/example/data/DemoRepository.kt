package com.example.data

import com.example.engine.SignalIntelligenceEngine
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class StudentState(
    val student: Student,
    val attendanceHistory: List<Double>,
    val academicHistory: List<Double>,
    val feeHistory: List<String>,
    val riskAssessment: RiskAssessment,
    val isInterventionActive: Boolean = false,
    val intervention: Intervention? = null,
    val outcome: InterventionOutcome? = null
)

object DemoRepository {

    private val _studentsState = MutableStateFlow<List<StudentState>>(emptyList())
    val studentsState: StateFlow<List<StudentState>> = _studentsState.asStateFlow()

    private val _interventions = MutableStateFlow<List<Intervention>>(emptyList())
    val interventions: StateFlow<List<Intervention>> = _interventions.asStateFlow()

    private val _outcomes = MutableStateFlow<List<InterventionOutcome>>(emptyList())
    val outcomes: StateFlow<List<InterventionOutcome>> = _outcomes.asStateFlow()

    init {
        resetDemoData()
    }

    fun resetDemoData() {
        val list = mutableListOf<StudentState>()

        // 1. PRIMARY DEMO STUDENT: Amina Khan (Multi-signal deterioration -> EARLY WARNING)
        val amina = Student(
            id = "STU-001",
            name = "Amina Khan",
            className = "8-A",
            section = "A",
            age = 13,
            guardianName = "Tariq Khan",
            guardianContact = "+92 300 1234567"
        )
        val aminaAttendance = listOf(92.0, 89.0, 85.0, 78.0, 68.0)
        val aminaAcademic = listOf(76.0, 73.0, 70.0, 64.0, 59.0)
        val aminaFees = listOf("Paid", "Paid", "Paid", "Delayed", "Delayed")
        val aminaRisk = SignalIntelligenceEngine.calculateRiskAssessment("STU-001", aminaAttendance, aminaAcademic, aminaFees)

        list.add(
            StudentState(
                student = amina,
                attendanceHistory = aminaAttendance,
                academicHistory = aminaAcademic,
                feeHistory = aminaFees,
                riskAssessment = aminaRisk
            )
        )

        // 2. Sara Ahmed (ON TRACK - Stable)
        val sara = Student("STU-002", "Sara Ahmed", "8-A", "A", 13, "Ahmed Hassan", "+92 301 2345678")
        val saraAtt = listOf(96.0, 95.0, 98.0, 96.0, 97.0)
        val saraAca = listOf(88.0, 90.0, 92.0, 89.0, 91.0)
        val saraFee = listOf("Paid", "Paid", "Paid", "Paid", "Paid")
        list.add(StudentState(sara, saraAtt, saraAca, saraFee, SignalIntelligenceEngine.calculateRiskAssessment("STU-002", saraAtt, saraAca, saraFee)))

        // 3. Hina Ali (ATTENTION - Attendance Decline)
        val hina = Student("STU-003", "Hina Ali", "8-A", "A", 14, "Ali Raza", "+92 302 3456789")
        val hinaAtt = listOf(90.0, 88.0, 84.0, 80.0, 76.0)
        val hinaAca = listOf(82.0, 80.0, 81.0, 79.0, 80.0)
        val hinaFee = listOf("Paid", "Paid", "Paid", "Paid", "Paid")
        list.add(StudentState(hina, hinaAtt, hinaAca, hinaFee, SignalIntelligenceEngine.calculateRiskAssessment("STU-003", hinaAtt, hinaAca, hinaFee)))

        // 4. Bilawal Shah (ELEVATED - Academic & Fee Decline)
        val bilawal = Student("STU-004", "Bilawal Shah", "8-A", "A", 13, "Shahbaz Shah", "+92 303 4567890")
        val bilAtt = listOf(88.0, 87.0, 86.0, 85.0, 85.0)
        val bilAca = listOf(75.0, 70.0, 65.0, 60.0, 54.0)
        val bilFee = listOf("Paid", "Paid", "Delayed", "Delayed", "Delayed")
        list.add(StudentState(bilawal, bilAtt, bilAca, bilFee, SignalIntelligenceEngine.calculateRiskAssessment("STU-004", bilAtt, bilAca, bilFee)))

        // 5. Usman Malik (ATTENTION - Fee Delay Pattern)
        val usman = Student("STU-005", "Usman Malik", "8-A", "A", 13, "Kamran Malik", "+92 304 5678901")
        val usmAtt = listOf(92.0, 93.0, 91.0, 90.0, 91.0)
        val usmAca = listOf(78.0, 79.0, 77.0, 76.0, 78.0)
        val usmFee = listOf("Paid", "Paid", "Delayed", "Pending", "Delayed")
        list.add(StudentState(usman, usmAtt, usmAca, usmFee, SignalIntelligenceEngine.calculateRiskAssessment("STU-005", usmAtt, usmAca, usmFee)))

        // 6. Zainab Iqbal (IMPROVING - Recovered Student)
        val zainab = Student("STU-006", "Zainab Iqbal", "8-A", "A", 13, "Iqbal Zafar", "+92 305 6789012")
        val zaiAtt = listOf(72.0, 75.0, 80.0, 85.0, 89.0)
        val zaiAca = listOf(62.0, 68.0, 72.0, 78.0, 82.0)
        val zaiFee = listOf("Delayed", "Paid", "Paid", "Paid", "Paid")
        list.add(StudentState(zainab, zaiAtt, zaiAca, zaiFee, SignalIntelligenceEngine.calculateRiskAssessment("STU-006", zaiAtt, zaiAca, zaiFee)))

        // 7-32: Generate remaining realistic Pakistani students for Class 8-A
        val studentNames = listOf(
            "Hamza Abbasi", "Ayesha Bibi", "Mustafa Chaudhry", "Fatima Jamil",
            "Zohaib Hassan", "Mariam Nawaz", "Daniyal Siddiqui", "Sadia Parveen",
            "Saad Rehman", "Laiba Noor", "Taha Hashmi", "Eshal Imran",
            "Omer Saeed", "Mahnoor Qureshi", "Fahad Mustafa", "Alishba Khan",
            "Bilal Farooq", "Zunaira Rashid", "Kashif Mahmood", "Anum Gul",
            "Rayyan Ahmed", "Rumaisa Akram", "Ahsan Iqbal", "Nida Yasir",
            "Haris Rauf", "Zahra Batool"
        )

        for ((index, name) in studentNames.withIndex()) {
            val id = "STU-${String.format(Locale.US, "%03d", index + 7)}"
            val guardian = "${name.split(" ").last()} Guardian"
            val contact = "+92 321 ${1000000 + index}"

            // Generate realistic varied baselines
            val baseAtt = 85.0 + (index % 12)
            val baseAca = 70.0 + (index % 25)
            val attHist = listOf(baseAtt - 2, baseAtt - 1, baseAtt, baseAtt + 1, baseAtt)
            val acaHist = listOf(baseAca - 1, baseAca, baseAca + 2, baseAca + 1, baseAca + 3)
            val feeHist = if (index % 9 == 0) listOf("Paid", "Paid", "Delayed", "Paid", "Paid") else listOf("Paid", "Paid", "Paid", "Paid", "Paid")

            val stu = Student(id, name, "8-A", "A", 13, guardian, contact)
            val risk = SignalIntelligenceEngine.calculateRiskAssessment(id, attHist, acaHist, feeHist)
            list.add(StudentState(stu, attHist, acaHist, feeHist, risk))
        }

        _studentsState.value = list
        _interventions.value = emptyList()
        _outcomes.value = emptyList()
    }

    fun getStudentState(studentId: String): StudentState? {
        return _studentsState.value.find { it.student.id == studentId }
    }

    fun recordAttendance(studentId: String, status: String) {
        val currentList = _studentsState.value.toMutableList()
        val index = currentList.indexOfFirst { it.student.id == studentId }
        if (index != -1) {
            val oldState = currentList[index]
            val newPercentage = if (status.equals("Present", ignoreCase = true)) 100.0 else 0.0
            val updatedAttHistory = oldState.attendanceHistory.drop(1) + newPercentage
            val newRisk = SignalIntelligenceEngine.calculateRiskAssessment(
                studentId, updatedAttHistory, oldState.academicHistory, oldState.feeHistory
            )
            currentList[index] = oldState.copy(
                attendanceHistory = updatedAttHistory,
                riskAssessment = newRisk
            )
            _studentsState.value = currentList
        }
    }

    fun enterAssessmentScore(studentId: String, scorePercentage: Double) {
        val currentList = _studentsState.value.toMutableList()
        val index = currentList.indexOfFirst { it.student.id == studentId }
        if (index != -1) {
            val oldState = currentList[index]
            val updatedAcademicHistory = oldState.academicHistory.drop(1) + scorePercentage
            val newRisk = SignalIntelligenceEngine.calculateRiskAssessment(
                studentId, oldState.attendanceHistory, updatedAcademicHistory, oldState.feeHistory
            )
            currentList[index] = oldState.copy(
                academicHistory = updatedAcademicHistory,
                riskAssessment = newRisk
            )
            _studentsState.value = currentList
        }
    }

    fun updateFeeStatus(studentId: String, status: String) {
        val currentList = _studentsState.value.toMutableList()
        val index = currentList.indexOfFirst { it.student.id == studentId }
        if (index != -1) {
            val oldState = currentList[index]
            val updatedFeeHistory = oldState.feeHistory.drop(1) + status
            val newRisk = SignalIntelligenceEngine.calculateRiskAssessment(
                studentId, oldState.attendanceHistory, oldState.academicHistory, updatedFeeHistory
            )
            currentList[index] = oldState.copy(
                feeHistory = updatedFeeHistory,
                riskAssessment = newRisk
            )
            _studentsState.value = currentList
        }
    }

    fun createIntervention(
        studentId: String,
        type: InterventionType,
        targetDate: String,
        assignedTo: String,
        notes: String,
        englishDraft: String,
        urduDraft: String
    ): Intervention {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val intervention = Intervention(
            id = "INT-${System.currentTimeMillis().toString().takeLast(6)}",
            studentId = studentId,
            type = type,
            status = InterventionStatus.IN_PROGRESS,
            createdAt = dateFormat.format(Date()),
            targetDate = targetDate,
            assignedTo = assignedTo,
            notes = notes,
            parentMessageDraftEnglish = englishDraft,
            parentMessageDraftUrdu = urduDraft
        )

        _interventions.value = _interventions.value + intervention

        // Update student state to indicate active intervention
        val currentList = _studentsState.value.toMutableList()
        val index = currentList.indexOfFirst { it.student.id == studentId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(
                isInterventionActive = true,
                intervention = intervention
            )
            _studentsState.value = currentList
        }

        return intervention
    }

    fun simulateInterventionOutcome(studentId: String): InterventionOutcome? {
        val studentState = getStudentState(studentId) ?: return null
        val activeIntervention = studentState.intervention ?: return null

        // Primary simulation for Amina Khan or any intervention target
        val newAttendance = listOf(68.0, 72.0, 77.0, 81.0, 84.0)
        val newAcademic = listOf(59.0, 63.0, 66.0, 69.0, 71.0)
        val newFees = listOf("Paid", "Paid", "Paid", "Paid", "Paid")

        val newRisk = SignalIntelligenceEngine.calculateRiskAssessment(studentId, newAttendance, newAcademic, newFees)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outcome = InterventionOutcome(
            interventionId = activeIntervention.id,
            studentId = studentId,
            dateCompleted = dateFormat.format(Date()),
            attendanceBefore = studentState.attendanceHistory.lastOrNull() ?: 68.0,
            attendanceAfter = newAttendance.last(),
            performanceBefore = studentState.academicHistory.lastOrNull() ?: 59.0,
            performanceAfter = newAcademic.last(),
            feeStatusBefore = studentState.feeHistory.lastOrNull() ?: "Delayed",
            feeStatusAfter = "Paid",
            riskBefore = studentState.riskAssessment.score,
            riskAfter = newRisk.score,
            narrative = "Student indicators improved after intervention. Attendance improved for three consecutive weeks and assessment performance recovered."
        )

        _outcomes.value = _outcomes.value + outcome

        // Update student state to recovered / improved state
        val updatedIntervention = activeIntervention.copy(status = InterventionStatus.RESOLVED)
        val currentList = _studentsState.value.toMutableList()
        val index = currentList.indexOfFirst { it.student.id == studentId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(
                attendanceHistory = newAttendance,
                academicHistory = newAcademic,
                feeHistory = newFees,
                riskAssessment = newRisk,
                isInterventionActive = false,
                intervention = updatedIntervention,
                outcome = outcome
            )
            _studentsState.value = currentList
        }

        return outcome
    }
}
