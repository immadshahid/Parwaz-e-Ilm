package com.example.engine

import com.example.BuildConfig
import com.example.model.RiskAssessment
import com.example.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

data class GeminiAIExplanation(
    val summary: String,
    val whyFlagged: String,
    val contributingSignals: List<String>,
    val recommendedAction: String,
    val interventionPriority: String,
    val parentMessageDraftEnglish: String,
    val parentMessageDraftUrdu: String
)

object GeminiReasoningEngine {

    suspend fun generateExplanation(
        student: Student,
        assessment: RiskAssessment
    ): GeminiAIExplanation = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "null") {
            return@withContext generateFallbackExplanation(student, assessment)
        }

        try {
            val url = URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            conn.doOutput = true

            val promptText = """
                You are the AI Reasoning Engine for 'Parwaaz-e-Ilm' (پروازِ علم), an Early Student Support System for Pakistani schools.
                Your task is to analyze structured student data and generate an empathetic, supportive, explainable assessment.

                RULES:
                - Do NOT use surveillance, policing, or judgment words.
                - Do NOT use 'dropout', 'high risk', or 'failing'.
                - Use terms like 'Early-Warning Support', 'Needs Attention', and 'May benefit from timely support'.
                - Output ONLY valid raw JSON with keys:
                  "summary", "whyFlagged", "contributingSignals" (array of strings), "recommendedAction", "interventionPriority", "parentMessageDraftEnglish", "parentMessageDraftUrdu".

                STUDENT DATA:
                Name: ${student.name}
                Class: ${student.className}-${student.section}
                Guardian: ${student.guardianName}
                Risk Score: ${assessment.score} / 100 (${assessment.level.displayName})
                Attendance: Current ${assessment.attendanceTrend.currentValue}, previous ${assessment.attendanceTrend.previousValue}, Change: ${assessment.attendanceTrend.changePercentage}%
                Academic: Current ${assessment.academicTrend.currentValue}, previous ${assessment.academicTrend.previousValue}, Change: ${assessment.academicTrend.changePercentage}%
                Fees: Status ${assessment.feeTrend.currentValue}, ${assessment.feeTrend.description}
                Cross Signal Deterioration: ${assessment.crossSignalDeterioration}
                Evidence: ${assessment.evidenceList.joinToString("; ")}

                JSON Response structure:
                {
                  "summary": "...",
                  "whyFlagged": "...",
                  "contributingSignals": ["...", "..."],
                  "recommendedAction": "...",
                  "interventionPriority": "High / Medium / Gentle",
                  "parentMessageDraftEnglish": "...",
                  "parentMessageDraftUrdu": "..."
                }
            """.trimIndent()

            val requestJson = JSONObject().apply {
                put("contents", org.json.JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", org.json.JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", promptText)
                            })
                        })
                    })
                })
            }

            val writer = OutputStreamWriter(conn.outputStream)
            writer.write(requestJson.toString())
            writer.flush()
            writer.close()

            if (conn.responseCode == 200) {
                val responseText = conn.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(responseText)
                val textCandidate = jsonResponse
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")

                // Extract JSON substring if wrapped in markdown blocks
                val jsonString = textCandidate.substringAfter("```json")
                    .substringAfter("```")
                    .substringBeforeLast("```")
                    .trim()

                val obj = JSONObject(jsonString)
                val signalsArray = obj.optJSONArray("contributingSignals")
                val signalsList = mutableListOf<String>()
                if (signalsArray != null) {
                    for (i in 0 until signalsArray.length()) {
                        signalsList.add(signalsArray.getString(i))
                    }
                }

                return@withContext GeminiAIExplanation(
                    summary = obj.optString("summary", assessment.trendSummary),
                    whyFlagged = obj.optString("whyFlagged", "Multiple independent signals deteriorated in the same period."),
                    contributingSignals = if (signalsList.isNotEmpty()) signalsList else assessment.evidenceList,
                    recommendedAction = obj.optString("recommendedAction", "Schedule a gentle teacher-student follow-up meeting within 3 days."),
                    interventionPriority = obj.optString("interventionPriority", "Medium"),
                    parentMessageDraftEnglish = obj.optString("parentMessageDraftEnglish", generateDefaultEnglishMessage(student)),
                    parentMessageDraftUrdu = obj.optString("parentMessageDraftUrdu", generateDefaultUrduMessage(student))
                )
            } else {
                return@withContext generateFallbackExplanation(student, assessment)
            }
        } catch (e: Exception) {
            return@withContext generateFallbackExplanation(student, assessment)
        }
    }

    fun generateFallbackExplanation(
        student: Student,
        assessment: RiskAssessment
    ): GeminiAIExplanation {
        val signals = assessment.evidenceList.ifEmpty {
            listOf("Routine monitoring indicates stable student performance across indicators.")
        }

        val why = when {
            assessment.crossSignalDeterioration ->
                "${student.name}'s attendance and assessment performance have both declined over the last five weeks, alongside recent fee payment delays."
            assessment.score >= 50 ->
                "${student.name} shows emerging decline in key academic or attendance indicators requiring timely support."
            else ->
                "${student.name} is currently performing well with minor areas for gentle observation."
        }

        val summary = if (assessment.crossSignalDeterioration) {
            "${student.name}'s attendance and academic performance have declined together for five consecutive weeks. Multiple signals suggest this student may benefit from timely support."
        } else {
            "Parwaaz signals for ${student.name} indicate stable overall progress with minor observational indicators."
        }

        val action = when (assessment.level) {
            com.example.model.EarlyWarningLevel.EARLY_WARNING -> "Schedule an informal teacher follow-up within 3 days to offer learning support."
            com.example.model.EarlyWarningLevel.ELEVATED -> "Initiate gentle classroom check-in and review recent study schedule."
            com.example.model.EarlyWarningLevel.ATTENTION -> "Monitor attendance and upcoming class quizzes over the next 2 weeks."
            else -> "Continue standard encouraging classroom engagement."
        }

        return GeminiAIExplanation(
            summary = summary,
            whyFlagged = why,
            contributingSignals = signals,
            recommendedAction = action,
            interventionPriority = when(assessment.level) {
                com.example.model.EarlyWarningLevel.EARLY_WARNING -> "High Priority Support"
                com.example.model.EarlyWarningLevel.ELEVATED -> "Elevated Attention"
                com.example.model.EarlyWarningLevel.ATTENTION -> "Gentle Monitoring"
                else -> "On Track"
            },
            parentMessageDraftEnglish = generateDefaultEnglishMessage(student),
            parentMessageDraftUrdu = generateDefaultUrduMessage(student)
        )
    }

    private fun generateDefaultEnglishMessage(student: Student): String {
        return "Respected ${student.guardianName},\n\n" +
                "Greetings from the school! We are reaching out to share how much we appreciate ${student.name}'s effort in Class ${student.className}. " +
                "To help ${student.name} maintain strong confidence and momentum, we would love to have a brief conversation with you about learning goals and daily attendance.\n\n" +
                "Please let us know a convenient time for a short call or meeting this week.\n\n" +
                "Warm regards,\nClass Teacher, Class ${student.className}\nParwaaz-e-Ilm Support System"
    }

    private fun generateDefaultUrduMessage(student: Student): String {
        return "محترم/محترمہ ${student.guardianName}،\n\n" +
                "السلام علیکم!\n" +
                "ہم اسکول کی طرف سے آپ سے رابطہ کر رہے ہیں۔ ہم جماعت ${student.className} میں ${student.name} کی تعلیمی ترقی اور بہتری کے خواہاں ہیں۔ " +
                "طالب علم کی پڑھائی اور باقاعدگی میں مزید بہتری کے لیے، ہم آپ کے ساتھ ایک مختصر گفتگو کرنا چاہتے ہیں۔\n\n" +
                "براہِ کرم اس ہفتے اپنے مناسب وقت کے بارے میں آگاہ فرمائیں۔\n\n" +
                "والسلام،\nکلاس ٹیچر، جماعت ${student.className}"
    }
}
