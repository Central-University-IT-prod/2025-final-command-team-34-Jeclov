package com.jeklov.superfinancer.ui.screens.finance

import android.widget.RadioButton
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jeklov.superfinancer.MainActivity
import com.jeklov.superfinancer.data.db.MainDB
import com.jeklov.superfinancer.data.models.Aim
import com.jeklov.superfinancer.data.models.Transaction
import com.jeklov.superfinancer.data.view.model.AimViewModel
import com.jeklov.superfinancer.data.view.model.AimViewModelProviderFactory
import com.jeklov.superfinancer.data.view.model.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Date

@Suppress("PreviewAnnotationInFunctionWithParameters")
@Preview
@Composable
fun FinanceUI(paddingValues: PaddingValues, database: MainDB, context: MainActivity) {
    /*val transactions: List<Transaction> = listOf(Transaction(1, 11, "Квартира", 93312, true),
        Transaction(2, 12, "Car", 174322090, false)
    )*/

    val viewModelAim: AimViewModel = viewModel(factory = AimViewModelProviderFactory(database))
    val allAims by viewModelAim.allAims.collectAsState(initial = emptyList())

    val viewModelTransaction = TransactionViewModel(transactionDB = database)
    val allTransactions by viewModelTransaction.allTransactions.collectAsState(initial = emptyList())

    Box(
        Modifier
            .fillMaxWidth()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
        //.background(brush = Brush.verticalGradient(listOf(Color.Red, BlueLT)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AimForm(viewModelAim)

            // Отображение списка целей
            LazyColumn {
                items(allAims) { aim ->
                    AimItem(aim, viewModelAim)
                }
            }

            if (allAims.isNotEmpty()) {
                TransactionForm(viewModelTransaction, context, allAims)
            } else {
                Text("Сначала создайте цель.")
            }

            // Отображение списка операций
            LazyColumn {
                items(allTransactions) { transaction ->
                    TransactionItem(transaction, viewModelTransaction)
                }
            }
        }
    }
}

@Composable
fun TransactionForm(
    viewModelTransaction: TransactionViewModel,
    context: MainActivity,
    aims: List<Aim>
) {
    var selectedAimId = remember { mutableStateOf<Int?>(null) }
    var amount = remember { mutableStateOf("") }
    var withdrawal = remember { mutableStateOf(true) } // true - пополнение, false - снятие
    var isOpen = remember { mutableStateOf(true) }

    Column {
        // Выбор цели
        DropdownMenu(
            expanded = isOpen.value,
            onDismissRequest = { isOpen.value = false }
        ) {
            aims.forEach { aim ->
                DropdownMenuItem(onClick = { selectedAimId.value = aim.id }) {
                    Text(aim.name)
                }
            }
        }

        TextField(
            value = amount.value,
            onValueChange = { amount.value = it },
            label = { Text("Сумма") })

        Row {
            Text("Пополнение")
            Switch(
                checked = withdrawal.value,
                onCheckedChange = { withdrawal.value = false }
            )
        }


        Button(onClick = {
            if (selectedAimId != null && amount.value.isNotBlank()) {
                viewModelTransaction.insertTransaction(
                    Transaction(
                        aimId = selectedAimId.value,
                        aimName = aims.find { it.id == selectedAimId.value }?.name ?: "",
                        amount = amount.value.toInt(),
                        withdrawalOrReplenishment = withdrawal.value
                    )
                )

            }
        }) {
            Text("Сохранить")
        }
    }
}


@Composable
fun TransactionItem(transaction: Transaction, viewModel: TransactionViewModel) {
    Column {
        Text("Операция: ${if (transaction.withdrawalOrReplenishment) "Пополнение" else "Снятие"}")
        Text("Сумма: ${transaction.amount}")
        Text("Цель: ${transaction.aimName}")

        // Кнопка для удаления операции
        Button(onClick = { viewModel.deleteTransaction(transaction) }) {
            Text("Удалить операцию")
        }
    }
}

@Composable
fun AimItem(aim: Aim, viewModel: AimViewModel) {
    Column {
        Text("Цель: ${aim.name}")
        Text("Требуемые средства: ${aim.requiredFunds}")
        Text("Накопленные средства: ${aim.accumulatedFunds}")
        Text("Прогресс: ${viewModel.calculateProgress(aim)}%")

        // Кнопка для удаления цели
        Button(onClick = { viewModel.deleteAim(aim) }) {
            Text("Удалить цель")
        }
    }
}

@Composable
fun AimForm(viewModelAim: AimViewModel) {
    var name = remember { mutableStateOf("") }
    var requiredFunds = remember { mutableStateOf("") }
    var date = remember { mutableStateOf(Date()) }

    Column {
        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Название цели") })
        TextField(
            value = requiredFunds.value,
            onValueChange = { requiredFunds.value = it },
            label = { Text("Требуемые средства") })
        // Опционально: выбор даты

        Button(onClick = {
            if (name.value.isNotBlank() && requiredFunds.value.isNotBlank()) {
                viewModelAim.addAim(
                    (Aim(
                        name = name.value,
                        requiredFunds = requiredFunds.value.toInt(),
                        accumulatedFunds = 0,
                        date = date.value
                    ))
                )
            }
        }) {
            Text("Сохранить")
        }
    }
}

@Composable
fun MyCard(transaction: Transaction, viewModel: TransactionViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = transaction.id.toString(), modifier = Modifier.weight(1f))
        Text(
            text = transaction.aimId?.toString() ?: "N/A",
            modifier = Modifier.weight(1f)
        )
        Text(text = transaction.aimName, modifier = Modifier.weight(3f))
        Text(text = transaction.amount.toString(), modifier = Modifier.weight(1f))
        Text(
            text = if (transaction.withdrawalOrReplenishment) "Пополнение" else "Вывод",
            modifier = Modifier.weight(2f)
        )
        IconButton(
            onClick = {
                transaction.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.deleteTransaction(transaction)
                    }
                    //Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete"
            )
        }
    }
    HorizontalDivider()
}