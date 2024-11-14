package com.example.newsheadlines

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.newsheadlines.ui.theme.NewsHeadlinesTheme

class LoginActivity : ComponentActivity() {
    private lateinit var databaseHelper: UserDatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseHelper = UserDatabaseHelper(this)
        setContent {
            NewsHeadlinesTheme {
                LoginScreen(this, databaseHelper)
            }
        }
    }
}

@Composable
fun LoginScreen(context: Context, databaseHelper: UserDatabaseHelper) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.news),
            contentDescription = "News Icon"
        )

        Spacer(modifier = Modifier.height(10.dp))

        LoginHeader()

        Spacer(modifier = Modifier.height(10.dp))

        UsernameField(username) { username = it }

        Spacer(modifier = Modifier.height(20.dp))

        PasswordField(password) { password = it }

        Spacer(modifier = Modifier.height(12.dp))

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        LoginButton {
            if (username.isNotEmpty() && password.isNotEmpty()) {
                val user = databaseHelper.getUserByUsername(username)
                if (user != null && user.password == password) {
                    error = "Successfully logged in"
                    startMainPage(context)
                } else {
                    error = "Invalid username or password"
                }
            } else {
                error = "Please fill all fields"
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        ActionsRow(context)
    }
}

@Composable
fun LoginHeader() {
    Row {
        Divider(color = Color.LightGray, thickness = 2.dp, modifier = Modifier.width(155.dp).padding(top = 20.dp, end = 20.dp))
        Text(text = "Login", color = Color(0xFF6495ED), fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Divider(color = Color.LightGray, thickness = 2.dp, modifier = Modifier.width(155.dp).padding(top = 20.dp, start = 20.dp))
    }
}

@Composable
fun UsernameField(username: String, onUsernameChange: (String) -> Unit) {
    TextField(
        value = username,
        onValueChange = onUsernameChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person Icon",
                tint = Color(0xFF6495ED)
            )
        },
        placeholder = { Text(text = "Username", color = Color.Black) },
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
    )
}

@Composable
fun PasswordField(password: String, onPasswordChange: (String) -> Unit) {
    TextField(
        value = password,
        onValueChange = onPasswordChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock Icon",
                tint = Color(0xFF6495ED)
            )
        },
        placeholder = { Text(text = "Password", color = Color.Black) },
        visualTransformation = PasswordVisualTransformation(),
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
    )
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF77a2ef)),
        modifier = Modifier.width(200.dp).padding(top = 16.dp)
    ) {
        Text(text = "Log In", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ActionsRow(context: Context) {
    Row(modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = {
            context.startActivity(Intent(context, RegistrationActivity::class.java))
        }) {
            Text(text = "Sign up", color = Color.Black)
        }
        Spacer(modifier = Modifier.width(100.dp))
        TextButton(onClick = { /* Implement Forgot Password */ }) {
            Text(text = "Forgot password?", color = Color.Black)
        }
    }
}

private fun startMainPage(context: Context) {
    val intent = Intent(context, MainPage::class.java)
    ContextCompat.startActivity(context, intent, null)
}
