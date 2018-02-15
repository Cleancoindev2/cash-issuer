package com.r3.cash.monzo

import com.r3.cash.monzo.models.Accounts
import com.r3.cash.monzo.models.Balance
import com.r3.cash.monzo.models.Transaction
import com.r3.cash.monzo.models.Transactions
import com.r3.cc.ApiClient

class ProductionMonzoApi(accessToken: String, url: String) : ApiClient(accessToken, url), MonzoApi {

    private fun transactionsInternal(queryString: String): Transactions {
        val payload = apiRequest(method = "GET", endpoint = "/transactions", query = queryString)
        return deserializeJson(payload, Transactions::class.java)
    }

    override fun accounts(): Accounts {
        val payload = apiRequest("GET", "/accounts")
        return deserializeJson(payload, Accounts::class.java)
    }

    override fun balance(accountId: String): Balance {
        val queryString = "?account_id=$accountId"
        val payload = apiRequest(method = "GET", endpoint = "/balance", query = queryString)
        return deserializeJson(payload, Balance::class.java)
    }

    override fun transaction(transactionId: String): Transaction {
        val queryString = "?account_id=$transactionId"
        val payload = apiRequest(method = "GET", endpoint = "/transactions", query = queryString)
        return deserializeJson(payload, Transaction::class.java)
    }

    override fun transactions(accountId: String, since: String, number: Int): Transactions {
        val limitQuery = if (number > 0) "&limit=$number" else ""
        val sinceQuery = if (since != "") "&since=$since" else ""
        val queryString = "?account_id=$accountId$limitQuery$sinceQuery"
        return transactionsInternal(queryString)
    }

}