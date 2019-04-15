package rationals

import java.math.BigInteger

infix fun <T : Number> T.divBy(d: T): Rational = Rational(BigInteger.valueOf(this.toLong()), BigInteger.valueOf(d.toLong()))

fun String.toRational() : Rational {
    val split = split('/')
    return Rational(split.get(0).toBigInteger(), if (split.size == 1) BigInteger.ONE else split.get(1).toBigInteger())
}

class RationalRange(override val start: Rational, override val endInclusive: Rational) : ClosedRange<Rational> {

    override fun contains(value: Rational): Boolean = start <= value && value <= endInclusive

    override fun isEmpty(): Boolean = endInclusive > endInclusive
}

data class Rational(val n :BigInteger, val d: BigInteger) : Comparable<Rational> {

    operator fun plus(inc: Rational) : Rational = Rational(n * inc.d + inc.n * d, d * inc.d)
    operator fun minus(inc: Rational) : Rational = Rational(n * inc.d - inc.n * d, d * inc.d)
    operator fun times(inc: Rational) : Rational = Rational(n * inc.n, d * inc.d)
    operator fun div(inc: Rational) : Rational = Rational(n * inc.d, d * inc.n)
    operator fun unaryMinus() : Rational = Rational(-n , d)
    operator fun unaryPlus() : Rational = Rational(n , d)
    operator fun rangeTo(r : Rational) : RationalRange = RationalRange(this, r)

    override fun equals(other: Any?): Boolean {
        if (!(other is Rational)) return false
        val normilize = this.normilize()
        val normilizeOther = other.normilize()
        return normilize.n.equals(normilizeOther.n) && normilize.d.equals(normilizeOther.d)
    }

    override fun toString(): String {
        val normilize = normilize()
        if (normilize.n.equals(BigInteger.ZERO)) return "0"
            else if (normilize.d.equals(BigInteger.ONE)) return normilize.n.toString()
            else return normilize.n.toString() + '/' + normilize.d.toString()
    }

    operator override fun compareTo(other : Rational) : Int = if (n * other.d < other.n * d) -1 else if (n * other.d > other.n * d) 1 else 0

    private fun normilize() : Rational {
        var divider = nod()
        if (d.compareTo(BigInteger.ZERO) < 0) divider *= BigInteger.valueOf(-1)
        return Rational(n.div(divider), d.div(divider))
    }

    private fun nod() : BigInteger {
        var n1 = n.abs()
        var d1 = d.abs()
        while (n1 != BigInteger.ZERO && d1 != BigInteger.ZERO) {
            if (n1 > d1) {
                n1 = n1.mod(d1)
            } else {
                d1 = d1.mod(n1)
            }
        }
        return n1 + d1
    }
}

fun main() {
    1..3
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}