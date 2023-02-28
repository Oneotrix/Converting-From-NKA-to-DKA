import java.lang.Exception

/**
 * Graph class is a directed graph
 *
 *
 * @param count the number of graph vertices
 * */

class Graph(count: Int) {
    /**
     *@array - matrix of this graph
     */
    private val  array = Array(count) {CharArray(count)}

    fun establishRelation(vertex : Int, inputRelations : CharArray ){
        /**
         * set relations between vertex and other vertex in graph (including vertex itself).
         *
         *
         * @param vertex - the current vertex of the graph
         * @param inputRelations - CharArray of size count, representing a relation between the
         * current vertex and the rest of the vertices in the graph (including the current one).
         **/

        try {
            array[vertex] = inputRelations
        } catch (e : Exception) {
            println(e)
            println("Please, check for correctness vertex or inputRelations")
        }
    }

    fun getGraph(): Array<CharArray> {
        /**
         * @return graph matrix
         * */
        return array
    }
}