package com.zonkil.roomoccupancymanager.persistance.repositories

import com.zonkil.roomoccupancymanager.persistance.entieties.GuestEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification
import spock.lang.Subject

@DataJpaTest
class GuestRepositoryTest extends Specification {

    @Subject
    @Autowired
    GuestRepository guestRepository

    void setup() {
        def guests = [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0].collect { it -> new GuestEntity(it) }
        guestRepository.saveAll(guests)
    }

    def "countPremiumGuests"() {
        when:
        def count = guestRepository.countAllByWillingnessToPayIsGreaterThanEqual(BigDecimal.valueOf(100.0));
        then:
        count == 6
    }

    def "countEconomyGuests"() {
        when:
        def count = guestRepository.countAllByWillingnessToPayIsLessThan(BigDecimal.valueOf(100.0));
        then:
        count == 4
    }

    def "calculatePremiumProfit"() {
        when:
        def profit = guestRepository.countPremiumProfit(limitPrem)
        then:
        profit == expectedPremiumProfit

        where:
        limitPrem | expectedPremiumProfit
        1         | 374.0
        2         | 583.0
        3         | 738.0
        4         | 853.0
        5         | 954.0
        6         | 1054
        7         | 1054
    }

    def "calculateEconomyProfit"() {
        when:
        def profit = guestRepository.countProfitEconomy(limitEco)
        then:
        profit == expectedEconomyProfit

        where:
        limitEco | expectedEconomyProfit
        1        | 99.99
        2        | 144.99
        3        | 167.99
        4        | 189.99
        5        | 189.99
    }
}
