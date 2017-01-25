package com.bank.account

import scala.io.StdIn.readLine
import scala.util.control.NonFatal

/** App that acts as a simple bank account
  *
  * User interacts using the command line and can
  * deposit, withdraw, check the balance, or exit.
  *
  */
object Interaction extends App {

    val account = new Account("/log.html")
    var active: Boolean = true

    while (active) {
        println("Please enter in a command (Deposit, Withdraw, Balance, Exit) :")
        val answer = readLine().toLowerCase()

        answer match {
            case "deposit" =>
                println("Please enter an amount to deposit:")
                try {
                    val amount = readLine().toDouble
                    if ("%.2f".format(amount).toDouble != amount || amount < 0) {
                        println("Invalid input. Either too many decimal places (limit 2) or a negative number.")
                    } else {
                        account.deposit(amount)
                    }
                } catch {
                    case NonFatal(_) => println("Something went wrong. Try again.")
                }
            case "withdraw" =>
                println("Please enter an amount to withdraw:")
                try {
                    val amount = readLine().toDouble
                    if ("%.2f".format(amount).toDouble != amount || amount < 0) {
                        println("Invalid input. Either too many decimal places (limit 2) or a negative number.")
                    } else {
                        account.withdraw(amount)
                    }
                } catch {
                    case NonFatal(_) => println("Something went wrong. Try again.")
                }
            case "balance" => account.balance()
            case "exit" => active = false
            case _ => println("Invalid input. Try again.")
        }
    }
}
