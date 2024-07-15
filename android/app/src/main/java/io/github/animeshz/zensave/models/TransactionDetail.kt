package io.github.animeshz.zensave.models

import android.icu.util.CurrencyAmount
import java.util.Date

class TransactionDetail(
    val amount: CurrencyAmount,
    val txnId: String,
    val utrNo: String,
    val referrer: String,
    val debit: Boolean,
    val timestamp: Date,
)