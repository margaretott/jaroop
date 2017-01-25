package com.bank.account

import java.io.File
import scala.xml.{Elem, Node, NodeSeq, XML}
import scala.util.control.NonFatal

/** A bank account
  *
  * @param filePath path to an xml-formatted document containing account history
  *
  */
class Account(filePath: String) {

  val path: String = getClass.getResource(filePath).getPath
  val file: File = new File(path)
  val html: Elem = XML.loadFile(file)
  val tables: NodeSeq = html \\ "table"

  val donationTable: Node = tables.head
  var donationAmounts: List[Double] = (donationTable \\ "tbody" \\ "td").map(_ text).map(_ toDouble).toList

  var transactionTable: Node = tables.tail.head
  var transactionAmounts: List[Double] = (transactionTable \\ "tbody" \\ "td").map(_ text).map(_ toDouble).toList

  /** Deposits into the bank account
    *
    * @param amount the amount to deposit
    *
    * */
  def deposit(amount: Double): Unit = {
    transactionAmounts = addTransaction(transactionAmounts, amount)
  }

  /** Withdraws from the bank account
    *
    * @param amount the amount to withdraw
    *
    * */
  def withdraw(amount: Double): Unit = {
    transactionAmounts = addTransaction(transactionAmounts, -1.0*amount)
  }

  /** Calculates and prints the balance in the bank account */
  def balance(): Unit = {
    println("The current balance is: $" + "%.2f".format(transactionAmounts.sum + donationAmounts.sum))
  }

  /** Adds a transaction (deposit or withdrawal) to the account's
    * list of transactions and updates the xml file */
  def addTransaction(transactions: List[Double], value: Double): List[Double] = {

    val newTransactions: List[Double] = transactions :+ value

    val resultHtml = <html>
      <body>
        <div class="content">
          { donationTable }
          <table id="transactions" class="table-bordered">
            <thead>
              <th>Amount</th>
            </thead>
            <tbody>
              { for (amount <- newTransactions)
                yield <tr><td>{amount}</td></tr>
              }
            </tbody>
            </table>
        </div>
      </body>
    </html>

    try {
      XML.save(path, resultHtml)
    } catch {
        case NonFatal(_) => println("Could not save file.")
    }

    newTransactions

  }

}
