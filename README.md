# 🕊️ Parwaaz-e-Ilm

### AI-Powered Early Student Support & Dropout Risk Detection System

> **See the Signs. Support the Journey.**

Parwaaz-e-Ilm is an AI-powered student early-warning system designed to help schools identify students who may be at risk of disengaging from education **before the warning signs become irreversible**.

Instead of looking at attendance, academic performance, or fee status independently, Parwaaz-e-Ilm analyzes **patterns across multiple signals over time**, explains why a student has been flagged, and recommends timely interventions.

The goal is simple:

> **Don't wait for a student to disappear. Notice when they need support.**

---

## 🎯 The Problem

Student dropout rarely happens because of a single event.

It is often preceded by a gradual combination of warning signs:

- 📉 Attendance starts declining
- 📚 Academic performance begins falling
- 💰 Fee payments become delayed
- 📆 Absences become more frequent
- ⚠️ Previous interventions may not produce improvement

Teachers often notice individual problems, but connecting these signals across weeks or months can be difficult when they are managing large classes.

By the time a student completely stops attending, the opportunity for early intervention may already have passed.

### Parwaaz-e-Ilm addresses this gap.

It transforms scattered student data into:

**Signals → Patterns → Risk → Explanation → Intervention → Improvement**

---

# 🚀 Key Features

## 👩‍🏫 Teacher Dashboard

Teachers can quickly:

- View their classes
- View students requiring attention
- Record attendance
- Enter quiz/test scores
- Update fee status
- View student trends
- Review AI-generated insights
- Record interventions
- Monitor improvement

The interface is designed for **fast classroom data entry** rather than complicated forms.

---

## 📊 Multi-Signal Risk Detection

Parwaaz-e-Ilm evaluates three primary signals:

### Attendance

Tracks:

- Attendance percentage
- Recent absences
- Attendance trend
- Changes over time

### Academic Performance

Tracks:

- Quiz scores
- Test scores
- Average performance
- Performance trends
- Sudden declines

### Fee Status

Tracks:

- Paid
- Pending
- Delayed
- Repeated payment delays

The system combines these signals rather than treating them independently.

For example:

> One absence does not automatically mean a student is at risk.

But:

> Declining attendance + falling scores + repeated fee delays

can indicate a much stronger early-warning pattern.

---

# 🧠 Parwaaz Intelligence Engine

The intelligence layer is designed around two complementary components.

### Layer 1 — Signal Intelligence

A deterministic, explainable risk engine evaluates:

- Attendance trend
- Academic trend
- Fee payment pattern
- Rate of change
- Multi-signal deterioration
- Historical patterns

It produces structured evidence and a risk level.

### Layer 2 — AI Reasoning

A generative AI layer can use the structured evidence to:

- Explain why a student was flagged
- Summarize the important signals
- Identify meaningful patterns
- Recommend an appropriate intervention
- Generate supportive parent communication drafts

This separation is intentional.

The system does **not** ask an LLM to blindly determine whether a student should be considered at risk.

Instead:

```text
Student Data
     ↓
Signal Analysis
     ↓
Structured Evidence
     ↓
AI Explanation & Recommendation
```

This makes the system easier to understand, test, and improve.

---

# 🟢 Risk Levels

| Level | Meaning | Suggested Action |
|---|---|---|
| 🟢 Stable | Student appears to be progressing normally | Continue monitoring |
| 🟡 Watch | Early warning signs detected | Monitor closely |
| 🟠 At Risk | Multiple concerning patterns detected | Begin intervention |
| 🔴 Critical | Strong sustained warning pattern | Immediate follow-up |

### Important

A risk level is an **early-warning indicator**, not a statement that a student will definitely drop out.

The final decision always belongs to the teacher, counselor, or administrator.

---

# 💡 Explainable AI

Parwaaz-e-Ilm does not simply display:

> `Risk Score: 82%`

Instead, it explains the evidence.

### Example

**Student:** Amina Khan  
**Class:** 8-A  
**Risk:** 🔴 Critical

> Amina's attendance has declined from 91% to 73% over the last four weeks. Her average test score has also fallen by 18%, while her fee payment has been delayed twice.

### Contributing Factors

- 📉 Attendance declining
- 📉 Academic performance declining
- 💰 Repeated fee delays

### Recommended Action

> Schedule a counselor follow-up and check whether the student is experiencing academic, financial, family, or attendance-related difficulties.

---

# 🤝 Intervention System

When a student's risk increases, Parwaaz-e-Ilm can recommend an intervention.

Possible interventions include:

### Teacher Follow-up

A teacher can privately check in with the student.

### Counselor Meeting

The student can be referred to a counselor or administrator.

### Parent Communication

The system can generate a respectful communication draft.

### Follow-up

The intervention can be recorded and scheduled for review.

---

# 📈 Intervention Impact

Parwaaz-e-Ilm doesn't stop at predicting risk.

It also tracks what happens **after intervention**.

### Before Intervention

```text
Attendance: 73%
Average Score: 54%
Risk: 🔴 Critical
```

### After Intervention

```text
Attendance: 84%
Average Score: 67%
Risk: 🟡 Watch
```

The system can therefore demonstrate:

> **Intervention → Change → Improvement**

This creates a continuous student-support loop.

---

# 📱 Application Flow

```text
Teacher / Admin
       │
       ▼
   Mobile App
       │
       ▼
Student Data Entry
       │
       ├── Attendance
       ├── Academic Scores
       └── Fee Status
       │
       ▼
Trend Analysis
       │
       ▼
Risk Engine
       │
       ▼
Structured Evidence
       │
       ▼
AI Reasoning
       │
       ├── Explanation
       ├── Risk Insight
       └── Intervention Recommendation
       │
       ▼
Teacher / Counselor Alert
       │
       ▼
Intervention
       │
       ▼
Follow-up Data
       │
       ▼
Measure Improvement
```

---

# 🏗️ Architecture

```text
┌──────────────────────────────┐
│          USERS               │
│                              │
│ Teacher | Admin | Counselor  │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│       FLUTTER MOBILE APP     │
│                              │
│ Dashboard                    │
│ Students                     │
│ Attendance                   │
│ Assessments                  │
│ Fees                         │
│ Alerts                       │
│ Analytics                    │
└──────────────┬───────────────┘
               │ HTTPS
               ▼
┌──────────────────────────────┐
│       FASTAPI BACKEND        │
│                              │
│ Authentication               │
│ Student Management           │
│ Data APIs                    │
│ Risk APIs                    │
│ Intervention APIs            │
│ Dashboard APIs               │
└───────┬───────────┬──────────┘
        │           │
        ▼           ▼
┌─────────────┐  ┌──────────────────────┐
│  DATABASE   │  │ PARWAAZ INTELLIGENCE │
│             │  │       ENGINE         │
│ Students    │  │                      │
│ Attendance  │  │ Signal Analysis      │
│ Scores      │  │ Trend Detection      │
│ Fees        │  │ Risk Classification  │
│ Interventions│ │ AI Reasoning         │
└─────────────┘  └──────────┬───────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ AI INSIGHTS     │
                    │                 │
                    │ Explanation     │
                    │ Recommendation  │
                    │ Communication   │
                    └─────────────────┘
```

---

# 🛠️ Technology Stack

## Frontend

- **Flutter**
- Dart
- Mobile-first UI
- Responsive dashboards
- Interactive charts

## Backend

- **Python**
- **FastAPI**
- REST APIs
- Authentication & authorization
- Student data management

## Database

Development:

- SQLite

Production-ready architecture:

- PostgreSQL

## AI

The intelligence layer can use:

- Deterministic trend/risk analysis
- **Google Gemini API** for explanation and reasoning

The AI layer is designed to complement structured risk analysis rather than replace it.

## Notifications

Potential integrations:

- Firebase Cloud Messaging
- Email
- SMS / WhatsApp

---

# 👥 User Roles

## Teacher

Teachers can:

- Manage assigned classes
- Record attendance
- Enter assessments
- Update fee status
- View risk indicators
- View AI explanations
- Record interventions

## Admin / Counselor

Administrators can:

- View school-wide statistics
- Monitor risk distribution
- View student profiles
- Review alerts
- Track interventions
- Analyze trends
- Monitor intervention outcomes

---

# 📊 Dashboard

The main dashboard provides a quick overview of student wellbeing.

Example:

```text
┌─────────────────────────────────────┐
│ Good Morning, Mahnoor 👋            │
│ Here's how your students are doing. │
├─────────────────────────────────────┤
│ 32 Students                         │
│                                     │
│ 🟢 24 Stable                        │
│ 🟡 5 Watch                          │
│ 🟠 2 At Risk                        │
│ 🔴 1 Critical                       │
├─────────────────────────────────────┤
│ Students Requiring Attention        │
│                                     │
│ 🔴 Amina Khan                       │
│    Attendance: 73% ↓                │
│    Scores: 18% ↓                    │
│    Fees: Delayed                    │
│                                     │
│ 🟠 Sana Ahmed                       │
│    Attendance: 81% ↓                │
│    Scores: 12% ↓                    │
└─────────────────────────────────────┘
```

---

# 🧪 Demo Scenario

The primary demonstration follows a fictional Grade 8 student named **Amina Khan**.

### Week 1

```text
Attendance: 91%
Average Score: 78%
Fees: Regular
Risk: 🟢 Stable
```

### Week 2

Attendance begins declining.

```text
Attendance: 86%
Risk: 🟡 Watch
```

### Week 3

Academic performance begins falling.

```text
Attendance: 79%
Average Score: 66%
Risk: 🟠 At Risk
```

### Week 4

Fee payment is delayed.

```text
Attendance: 73%
Average Score: 54%
Fees: Delayed
Risk: 🔴 Critical
```

Parwaaz-e-Ilm identifies the combined pattern and recommends an intervention.

### After Intervention

```text
Attendance: 84%
Average Score: 67%
Risk: 🟡 Watch
```

The system detects positive movement and continues monitoring the student.

---

# 🎬 Hackathon Demo Story

The product demonstration follows:

```text
Meet Amina
     ↓
Normal Student
     ↓
Attendance Declines
     ↓
Academic Performance Declines
     ↓
Fee Delay Appears
     ↓
AI Detects Combined Pattern
     ↓
Student Flagged
     ↓
AI Explains Why
     ↓
Intervention Recommended
     ↓
Teacher/Counselor Acts
     ↓
Student Improves
     ↓
Risk Decreases
```

### Closing Message

> **“Parwaaz-e-Ilm doesn't wait for a student to disappear. It helps schools notice when they need support.”**

---

# 🔐 Privacy & Responsible AI

Student data is sensitive.

Parwaaz-e-Ilm follows a support-first philosophy.

The system should:

- Avoid publicly ranking students
- Avoid stigmatizing students
- Avoid permanent labels
- Treat risk as an early-warning signal
- Provide explanations for AI-generated insights
- Keep final decisions with educators
- Use data only for appropriate educational support

The application should never claim:

> “This student will drop out.”

Instead:

> “Multiple early-warning signals have been detected. Support may be appropriate.”

---

# 🌱 Future Roadmap

Potential future capabilities include:

- Parent portal
- Counselor portal
- Multilingual support
- Urdu/Hindko communication templates
- Advanced predictive models trained on historical school data
- SMS/WhatsApp integration
- Automated attendance integration
- School ERP integration
- Offline-first data entry
- Multi-school management
- District-level analytics
- Explainable ML dashboards
- Long-term intervention outcome analysis

---

# 📁 Project Structure

A possible project structure:

```text
parwaaz-e-ilm/
│
├── frontend/
│   └── Flutter application
│
├── backend/
│   ├── api/
│   ├── models/
│   ├── services/
│   ├── risk_engine/
│   ├── ai/
│   └── main.py
│
├── data/
│   └── mock/
│
├── docs/
│   ├── architecture/
│   └── screenshots/
│
├── README.md
└── LICENSE
```

---

# 📦 Deliverables

- [x] Mobile-first application
- [x] Teacher dashboard
- [x] Student management
- [x] Attendance tracking
- [x] Academic performance tracking
- [x] Fee status tracking
- [x] Multi-signal risk analysis
- [x] Explainable AI insights
- [x] Intervention workflow
- [x] Intervention impact tracking
- [x] Admin analytics
- [x] Architecture documentation
- [x] Mock/demo data

---

# 🏆 Why Parwaaz-e-Ilm?

Most education systems tell schools:

> **“What happened?”**

Parwaaz-e-Ilm tries to answer:

> **“What is changing, why might it matter, and what can we do now?”**

It transforms student data into an opportunity for early support.

### The core philosophy:

> **See the Signs.  
> Understand the Student.  
> Take Action.  
> Keep the Journey Going.**

---

## 👨‍💻 Project

**Parwaaz-e-Ilm**

AI-Powered Early Student Support System

Built for **AI Seekho Builders Day 2026 / Independence Day Hackathon**

### #AISeekhoBuildersDay2026 #GDG #NIC #IndependenceDayHackathon

---

## ❤️ Built With a Simple Belief

Every student deserves more than a final attendance record.

Sometimes, all they need is for someone to notice the pattern early enough.
