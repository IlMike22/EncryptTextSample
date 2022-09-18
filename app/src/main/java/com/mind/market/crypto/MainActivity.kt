package com.mind.market.crypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mind.market.crypto.ui.theme.CryptoTheme
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cryptoManager = CryptoManager()
        setContent {
            CryptoTheme {
                var messageToEncrypt by remember {
                    mutableStateOf("")
                }
                var messageToDecrypt by remember {
                    mutableStateOf("")
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    ) {
                        TextField(
                            value = messageToEncrypt,
                            onValueChange = {
                                messageToEncrypt = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("encrypt string") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(onClick = {
                                val bytes = messageToEncrypt.encodeToByteArray()
                                val file = File(filesDir, "secret.txt")
                                if (!file.exists()) {
                                    file.createNewFile()
                                }
                                val fos = FileOutputStream(file)
                                messageToDecrypt = cryptoManager.encrypt(
                                    bytes = bytes,
                                    outputStream = fos
                                ).decodeToString()
                            }) {
                                Text(text = "Encrypt")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(onClick = {
                                val file = File(filesDir, "secret.txt")
                                if (file.exists()) {
                                    messageToEncrypt = cryptoManager.decrypt(
                                        inputStream = FileInputStream(file)
                                    ).decodeToString()
                                }
                            }) {
                                Text(text = "Decrypt")
                            }
                        }
                        Text(text = messageToDecrypt)
                    }
                }
            }
        }
    }
}