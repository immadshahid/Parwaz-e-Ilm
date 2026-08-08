package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun RoleSelectionScreen(
    onSelectTeacher: () -> Unit,
    onSelectAdmin: () -> Unit,
    onLaunchAminaDemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Emerald900, Emerald800, WarmWhite)
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Gold500,
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "پرواز",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Emerald900
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "PARWAAZ-E-ILM",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Text(
                text = "پروازِ علم",
                fontSize = 18.sp,
                color = Gold100
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "See the Signs. Support the Journey.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Emerald100,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Portal Role",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onSelectTeacher,
                        colors = ButtonDefaults.buttonColors(containerColor = Emerald800),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("select_teacher_role_button")
                    ) {
                        Icon(imageVector = Icons.Default.School, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("TEACHER PORTAL (CLASS 8-A)")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onSelectAdmin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("select_admin_role_button")
                    ) {
                        Icon(imageVector = Icons.Default.SupervisorAccount, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ADMIN / COUNSELOR PORTAL")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(color = Slate200)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onLaunchAminaDemo,
                        colors = ButtonDefaults.buttonColors(containerColor = Gold600),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("launch_amina_demo_button")
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("LAUNCH AMINA KHAN DEMO CASE")
                    }
                }
            }
        }
    }
}
