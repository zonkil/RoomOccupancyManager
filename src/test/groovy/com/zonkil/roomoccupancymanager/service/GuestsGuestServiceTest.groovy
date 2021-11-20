package com.zonkil.roomoccupancymanager.service

import com.zonkil.roomoccupancymanager.domain.Guest
import com.zonkil.roomoccupancymanager.domain.Guests
import spock.lang.Specification
import spock.lang.Subject

class GuestsGuestServiceTest extends Specification {

    @Subject
    GuestsGuestService guestsGuestService


    def "testPremiumProfitCalculation"() {
        given:
        def dividedGuests = guests.collect { it -> new Guest(it) }
        guestsGuestService = new GuestsGuestService(new Guests(dividedGuests, []))

        when:
        def result = guestsGuestService.calculateProfit(GuestType.PREMIUM, limit)

        then:
        result == expectedResult

        where:
        guests                              | limit | expectedResult
        [200.0, 150.0, 140.0, 110.0, 100.0] | 5     | 700
        [200.0, 150.0, 140.0, 110.0, 100.0] | 4     | 600
        [200.0, 150.0, 140.0, 110.0, 100.0] | 3     | 490
        [200.0, 150.0, 140.0, 110.0, 100.0] | 2     | 350
        [200.0, 150.0, 140.0, 110.0, 100.0] | 1     | 200
        [200.0, 150.0, 140.0, 110.0, 100.0] | 0     | 0
        [200.0, 150.0, 140.0, 110.0, 100.0] | -1    | 0
    }

    def "testEconomyProfitCalculation"() {
        given:
        def dividedGuests = guests.collect { it -> new Guest(it) }
        guestsGuestService = new GuestsGuestService(new Guests([], dividedGuests))

        when:
        def result = guestsGuestService.calculateProfit(GuestType.ECONOMY, limit)

        then:
        result == expectedResult

        where:
        guests                              | limit | expectedResult
        [200.0, 150.0, 140.0, 110.0, 100.0] | 5     | 700
        [200.0, 150.0, 140.0, 110.0, 100.0] | 4     | 600
        [200.0, 150.0, 140.0, 110.0, 100.0] | 3     | 490
        [200.0, 150.0, 140.0, 110.0, 100.0] | 2     | 350
        [200.0, 150.0, 140.0, 110.0, 100.0] | 1     | 200
        [200.0, 150.0, 140.0, 110.0, 100.0] | 0     | 0
        [200.0, 150.0, 140.0, 110.0, 100.0] | -1    | 0
    }
}
