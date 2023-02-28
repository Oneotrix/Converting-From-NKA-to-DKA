import java.util.Stack

/**
 * DKA class make DKA from NKA
 * @param startState is the initial state of the machine
 * @param finishState is the final state of the machine
 * @param graphMatrix is the adjacency matrix of a directed weighted graph //adjacency - смежность
 * @param inputAlphabet is the input character string
 * */
class DKA(startState : Int,
          finishState: Int,
          graphMatrix : Array<CharArray>,
          inputAlphabet: CharArray)
{

    private val start = startState
    private val finish = finishState
    private val matrix = graphMatrix
    private val alphabet = inputAlphabet

    private var dkaStart: Int? = null
    private var dkaFinish: Int? = null


    private fun eClosure(s : Int) : Set<Int> {
        /**
         * build e-closure on 's' state
         * @return e-closure on 's' state like a set
         */

        if(s >= matrix.size || s < 0) {
            println("'s' can't be higher than matrix.size or smaller than zero")
            return emptySet()
        }

        val set : MutableSet<Int> = mutableSetOf()
        val stack : Stack<Int> = Stack<Int>()

        stack.add(s)

        while (!stack.empty()) {
            val currentState = stack.pop()

            for (i in 0 until matrix[currentState].size) {
                if (matrix[currentState][i] == 'e' && i != s) {
                    stack.add(i)
                }
            }

            set.add(currentState)
        }


        return set
    }

    private fun charClosure(s: Int, char : Char) : Set<Int> {
        /**
         * @return the set of states reachable from s by the input symbol char
         */

        if(s >= matrix.size || s < 0) {
            println("'s' can't be higher than matrix.size or smaller than zero")
            return emptySet()
        }

        val set : MutableSet<Int> = mutableSetOf()

        for (i in 0 until matrix[s].size) {
            if (matrix[s][i] == char) {
                set.add(i)
            }
        }

        return set
    }

    private fun eClosure(t : Set<Int>): Set<Int> {
        /**
         * build e-closure for each 's' state, each 's' belongs 't'
         *
         * @return e-closure for each 's' state, each 's' belongs 't' like a set
         */

        val set : MutableSet<Int> = mutableSetOf()

        for (i in t) {
            set.add(i)
        }

        for(i in t) {
            set.addAll(eClosure(i))
        }

        return set
    }

    private fun move(t : Set<Int>, char: Char): Set<Int> {
        /**
         * @return set of states nka into which there are transitions from states s(i) belonging to 't'
         * according to the input symbol a
         */

        val set : MutableSet<Int> = mutableSetOf()
        for(i in t) {
            set.addAll(charClosure(i,char))
        }

        return set
    }

    private fun isCompare(firstSet: Set<Int>, secondSet: Set<Int>) : Boolean {
       /**
        * @return result of comparing firstSet and secondSet
        * */
       if (firstSet.size != secondSet.size) return false

        for(i in firstSet.indices) {
            if(firstSet.sorted()[i] != secondSet.sorted()[i]) {
                return false
            }
        }

        return true
    }

    private fun comparePullStateWithAllEpsPulls(statePull: MutableList<Set<Int>>,
                                                lastSetOfEachEpsPull: MutableList<Set<Int>>): Set<Int> {
        /**
         * Compares each link to each state and establishes a weight relationship
         * between the current state and other states (including the current state)
         *
         * @return new HEAD of state pull like a set
         * */
        var count = 0

        for (i in lastSetOfEachEpsPull) {
            if(i.isNotEmpty()) {
                for(j in statePull) {
                     if(isCompare(j, i)) {
                         count++
                     }
                }
            }

            if(count == 0 && i.isNotEmpty()) {
                return i
            }
        }

        return emptySet()
    }

    private fun buildResultGraphMatrix(statePull: MutableList<Set<Int>>,
                                       epsPull: MutableMap<Char,MutableList<Set<Int>>> ): Array<Array<MutableList<Char>>> {
        /**
         * @return adjacency matrix of a directed weighted DKA graph
         * */

        val array: Array<Array<MutableList<Char>>> = Array(statePull.size) { Array(statePull.size) { MutableList(0) {'-'} } }

        for(i in 0 until statePull.size) {
            for (j in epsPull) {
                for (k in 0 until statePull.size) {
                    if(isCompare(j.value[i],statePull[k])) {
                        array[i][k].add(j.key)
                    }
                }

            }
        }

        checkStartAndFinishState(statePull, start, finish )

        return array
    }

    private fun checkStartAndFinishState(statePull: MutableList<Set<Int>>, start: Int, finish: Int) {
        /**
         * set in
         * @param dkaStart start state of DKA
         * @param dkaFinish finish state of DKA
         */

        for (i in 0 until statePull.size) {
            for(j in statePull[i]) {
                when(j) {
                    start -> {
                        dkaStart = i
                        break
                    }
                    finish -> {
                        dkaFinish = i
                        break
                    }
                    else -> {

                    }
                }
            }
        }
    }

    fun fromNkaToDka() : Array<Array<MutableList<Char>>> {
        /**
         * converts NKA(graph matrix of NKA) to DKA(graph matrix of DKA)
         *
         * @return result graph matrix of DKA
         */
        val statePull : MutableList<Set<Int>> = mutableListOf()
        val epsPull: MutableMap<Char,MutableList<Set<Int>>> = mutableMapOf()
        val lastSetOfEachEpsPull: MutableList<Set<Int>> = mutableListOf()

        for(i in alphabet) {
            epsPull[i] = mutableListOf()
        }

        statePull.add(eClosure(start))

        while (true) {
            lastSetOfEachEpsPull.clear()
            for (i in alphabet) {
                epsPull[i]?.add(eClosure(move(statePull.last(), i)))
                lastSetOfEachEpsPull.add(epsPull[i]!!.last())
            }


            val newState = comparePullStateWithAllEpsPulls(statePull, lastSetOfEachEpsPull)

            if(newState.isEmpty()) {
                for(i in 0 until statePull.size) {
                    println("state (${i + 1}) : ${statePull[i]} ")
                }
                return buildResultGraphMatrix(statePull, epsPull)
            } else {
                statePull.add(newState)
            }
        }

    }

    fun startStateOfDKA() : Int? {
        /**
         * @return start state of DKA
         */
        return dkaStart
    }

    fun finishStateOfDKA() : Int? {
        /**
         * return finish state of DKA
         */
        return dkaFinish
    }
}