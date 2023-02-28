fun main() {

    val nka = Graph(4)

    nka.establishRelation(0, charArrayOf('b', 'a', '-', 'e'))
    nka.establishRelation(1, charArrayOf('-', '-', 'e', 'c'))
    nka.establishRelation(2, charArrayOf('-', 'b', '-', 'a'))
    nka.establishRelation(3, charArrayOf('-', '-', '-', 'c'))




    val dka = DKA(0,3,nka.getGraph(), charArrayOf('a', 'b', 'c'))
    val resultMatrix = dka.fromNkaToDka() //result matrix of DKA

    for (i in resultMatrix.indices) {

        var str = ""

        for (j in resultMatrix[i]) {
            str += "$j "
        }

        println("${i + 1} : $str")
    } //format output

    println("Start state of DKA is ${dka.startStateOfDKA()?.plus(1)}")
    println("Finish state of DKA is ${dka.finishStateOfDKA()?.plus(1)}")
}