package taxipark

import kotlin.math.roundToInt
import kotlin.random.Random

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        allDrivers.subtract(trips.map { it.driver }.toSet())

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    if (minTrips == 0) return allPassengers
    val list = trips.map { it.passengers }.flatten()
    return list.filter { p -> list.count { it.equals(p) } >= minTrips }.toSet()
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        trips.filter { it.driver.equals(driver) }.map { it.passengers }
                .flatten().groupingBy { it }.eachCount().filter { it.value > 1 }.keys

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val passengers = trips.map { it.passengers }.flatten().toSet()
    //пара пассажир + скидка
    val pairs = trips.map { Pair(it.passengers, it.discount) }
            .map { p -> p.first.map { Pair(it, p.second) }.toList() }
            .flatten()
    return passengers.filter { isSmartPassenger (pairs, it) }.toSet()

}

fun isSmartPassenger(pairs: List<Pair<Passenger, Double?>>, passenger: Passenger) : Boolean {
    val filterPassenger = filterPassenger(pairs, passenger)
    return filterPassenger.isEmpty()
            || filterPassenger.count { it.second != null } / filterPassenger.count().toDouble() > 0.5
}

fun filterPassenger(pairs: List<Pair<Passenger, Double?>>, passenger: Passenger) : List<Pair<Passenger, Double?>> {
    return pairs.filter { it.first.equals(passenger) }
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    if (trips.isEmpty()) return null;
    val first = trips.map { it.duration / 10 }.groupingBy { it }.eachCount().maxBy { it.value }!!.toPair().first
    return IntRange(first * 10, first * 10 + 9)
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    Random.nextInt()
    if (trips.isEmpty()) return false

    val drivers20 = (allDrivers.size * 0.2 - 0.5).roundToInt()
    val total = trips.map { it.cost }.reduce { acc, d -> acc + d }
    val bestDrivers = trips.map { Pair(it.driver, it.cost) }.groupBy { it.first }.map { Pair(it.key, it.value.map { it.second }.sum()) }
            .sortedByDescending { it.second }.take(drivers20)
    return bestDrivers.map { it.second }.sum() / total >= 0.8

}