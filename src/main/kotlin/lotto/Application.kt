package lotto

import camp.nextstep.edu.missionutils.Console
import camp.nextstep.edu.missionutils.Randoms

const val ERROR = "[ERROR]"
const val NOT_MULTIPLE_OF_1000_ERROR = " 구입금액은 1000의 배수여야 합니다. 다시 입력해 주세요."

fun main() {
    println("구입금액을 입력해 주세요.")

    val numberOfTickets = getNumberOfTickets( validateInputMoney( getInputMoney() ) )
    println("\n${numberOfTickets}개를 구매했습니다.")


    val tickets = generateAllTickets(numberOfTickets)

    printTickets(tickets)

    var sevenNumbers = getSevenNumbers(getWinningNumbers(), getBonusNumber())

    sevenNumbers = validateSevenNumbers(sevenNumbers)

    print(sevenNumbers)

}


fun getInputMoney(): Int {

    return Console.readLine().toInt()
}

fun checkMoneyException(inputMoney: Int) {
    require(inputMoney % 1000 == 0) { throw IllegalArgumentException(ERROR+NOT_MULTIPLE_OF_1000_ERROR) }

}

fun validateInputMoney(inputMoney: Int): Int {
    var isValidInput = false
    var localInputMoney = inputMoney    // 매개변수가 val이므로

    while (!isValidInput) {
        try {
            checkMoneyException(localInputMoney)
            isValidInput = true

        } catch (e: IllegalArgumentException) {
            println("${e.message}")
            localInputMoney = getInputMoney()

        }
    }

    return localInputMoney
}

fun getNumberOfTickets( inputMoney: Int ): Int{

    return inputMoney / 1000
}

fun generateLottoNumbers(): List<Int> {

    return Randoms.pickUniqueNumbersInRange(1, 45, 6).sorted<Int>()
}

fun generateAllTickets(numberOfTickets: Int): List<Lotto> {
    val tickets = mutableListOf<Lotto>()
    repeat (numberOfTickets) {
        val numbers = generateLottoNumbers()
        val aTicket = Lotto(numbers)
        tickets.add(aTicket)

    }

    return tickets
}

fun printTickets(tickets: List<Lotto>) {
    for (aTicket in tickets) {
        println(aTicket)

    }
}

fun getWinningNumbers(): MutableList<Int> {
    println("\n당첨 번호를 입력해 주세요.")

    return Console.readLine().split(',').map { it.toInt() }.sorted().toMutableList()
}

fun getBonusNumber(): Int {
    println("\n보너스 번호를 입력해 주세요.")

    return Console.readLine().toInt()
}

fun getSevenNumbers(winningNumbers: MutableList<Int>, bonusNumber: Int): MutableList<Int> {
    winningNumbers.add(bonusNumber)
    val sevenNumbers = winningNumbers

    return sevenNumbers
}

fun checkNumbersException(sevenNumbers: MutableList<Int>) {
    var subsetOfSeven = mutableListOf<Int>()

    for (item in sevenNumbers) {
        // 사용자의 입력이 1 이상 45 이하가 아닌 경우
        if (item < 1 || item > 45) {
            throw IllegalArgumentException("$ERROR 1부터 45까지의 정수만 입력하실 수 있습니다.")
        }
        // 사용자의 입력에서 같은 숫자가 중복될 경우
        if (subsetOfSeven.contains(item)) {
            throw IllegalArgumentException("$ERROR 7자리 숫자는 모두 달라야 합니다.")
        }
        subsetOfSeven.add(item)
    }

}

fun validateSevenNumbers(sevenNumbers: MutableList<Int>): MutableList<Int> {
    var isValidInput = false

    while (!isValidInput) {
        try {
            checkNumbersException(sevenNumbers)
            isValidInput = true

        } catch (e: IllegalArgumentException) {
            println("${e.message}")

            sevenNumbers.clear() // Kotlin에서 함수의 매개변수는 기본적으로 val로 선언되니까 재할당 불가. 그래서 비우고
            sevenNumbers.addAll(getSevenNumbers(getWinningNumbers(), getBonusNumber())) // 새로운 요소들을 추가

        }
    }

    return sevenNumbers
}