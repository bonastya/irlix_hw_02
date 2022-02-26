package com.example.irlix_hw_02


data class City(val title: String)

data class Product(val name: String, val price: Double)

data class Order(val id: Int, val products: List<Product>, val isDelivered: Boolean)

data class Customer(val name: String, val city: City, val orders: List<Order>)

data class Shop(val name: String, val customers: List<Customer>)



fun main() {
    val city1 = City("Ulsk")
    val city2 = City("Piter")
    val city3 = City("Msc")

    val product1 = Product("Bread", 10.0)
    val product2 = Product("Butter", 77.0)
    val product3 = Product("Beer", 90.0)
    val product4 = Product("Apple", 19.0)
    val product5 = Product("Orange", 2.0)
    val product6 = Product("Milk", 17.0)
    val product7 = Product("Honey", 34.0)
    val product8 = Product("Sugar", 30.0)
    val product9 = Product("Egg", 1.0)
    val product10 = Product("Brain", 100500.0)

    val customer1 = Customer("Bob", city1, listOf(
        Order(1, listOf(product1,product2), true),
        Order(2, listOf(product3,product4), false))
    )
    val customer2 = Customer("Mike", city2, listOf(
        Order(4, listOf(product6,product7), true),
        Order(5, listOf(product8,product1), true),
        Order(6, listOf(product9,product2), true),
        Order(3, listOf(product3,product5), true)
    ))
    val customer3 = Customer("Liza", city3, listOf(
        Order(7, listOf(product9,product10), false),
        Order(8, listOf(product1,product4), false),
        Order(9, listOf(product3,product6), false))
    )

    val shop = Shop("Test", listOf(customer1,customer2,customer3))

    println(shop.getSetOfCustomers() == setOf(customer1,customer2,customer3))
    println(shop.getCitiesCustomersAreFrom() == setOf(city1,city2,city3))
    println(shop.getCustomersFrom(city1) == listOf(customer1))
    println(shop.hasCustomerFrom(city2))
    println(shop.countCustomersFrom(city1) == 1)

    println(shop.findAnyCustomerFrom(city1) == customer1)
    println(shop.findAnyCustomerFrom(City("Urupinsk")) == null)

    println(customer3.getOrderedProducts() == setOf(product1,product3,product4,product6,product9,product10))

    println(shop.getCustomersSortedByNumberOfOrders() == listOf(customer1,customer3,customer2))

    println(shop.groupCustomersByCity() == mapOf(city1.title to setOf(customer1), city2.title to setOf(customer2), city3.title to setOf(customer3)))

    println(shop.getCustomersWithMoreUndeliveredOrdersThanDelivered() == setOf(customer3))

    println(customer1.getMostExpensiveDeliveredProduct() == product2)

    println(shop.getNumberOfTimesProductWasOrdered(product1) == 3)
}
//Пример как может выглядеть реализация (в сложном кейсе)
fun Shop.example(): Int? = customers.map { it.orders }.flatten().find { it.id == 3 }?.id

//TODO

//Преобразовать список клиентов в сет
fun Shop.getSetOfCustomers(): Set<Customer> {
    println("qqq"+HashSet(this.customers).toString())
    return HashSet(this.customers)
}

// Вернуть сет городов в которых проживают клиенты
fun Shop.getCitiesCustomersAreFrom(): Set<City> {
    return HashSet(this.customers.map{ c->c.city })
}

// Вернуть список клиентов из представленного города
fun Shop.getCustomersFrom(city: City): List<Customer> {
    return this.customers.filter { it.city==city }
}

// Вернуть true если хоть один клиент из выбранного города
fun Shop.hasCustomerFrom(city: City): Boolean {
    return this.customers.any{it.city==city}
}

// Вернуть количество клментов из выбранного города
fun Shop.countCustomersFrom(city: City): Int {
    return this.customers.count { c->c.city==city }
}

// Вернуть клиента из выбранного города или null, если нет таких
fun Shop.findAnyCustomerFrom(city: City): Customer? {
    return this.customers.firstOrNull{c->c.city==city}
}

// Вернуть сет всех продуктов заказанных клиентом
fun Customer.getOrderedProducts(): Set<Product> {
    return HashSet(this.orders.flatMap { order->order.products })
}

// Отсортировать клиентов по количеству заказов от меньшего к большему
fun Shop.getCustomersSortedByNumberOfOrders(): List<Customer> {
    return this.customers.sortedBy { customer -> customer.orders.size }
}

// Вернуть словарь в котором названия городов являются ключами, а значениями - сет клиентов, проживающих в этом городе
fun Shop.groupCustomersByCity(): Map<String, Set<Customer>> {
    val map = mutableMapOf<String, Set<Customer>>()
    HashSet(this.customers.map{ c->c.city }).forEach {
        map.put(
            it.title,
            HashSet(this.customers.filter{ c->c.city==it })
        )
    }
    return map
}

// Вернуть сет клиентов, у которых не доставленных заказов больше чем заказанных
fun Shop.getCustomersWithMoreUndeliveredOrdersThanDelivered(): Set<Customer> {
    return HashSet(this.customers.filter{c-> c.orders.count { !it.isDelivered }>c.orders.count{it.isDelivered}})
}

// Вернуть наиболее дорогой продукт из всех доставленных
fun Customer.getMostExpensiveDeliveredProduct(): Product? {
    return this.orders.filter{ order->order.isDelivered }.flatMap { orders->orders.products }.sortedBy { p->p.price }.last()
}

// Вернуть число - сколько раз был заказан выбранный продукт
fun Shop.getNumberOfTimesProductWasOrdered(product: Product): Int {
    return this.customers.flatMap { customer->customer.orders }.flatMap { order->order.products }.count { prod->prod==product }
}