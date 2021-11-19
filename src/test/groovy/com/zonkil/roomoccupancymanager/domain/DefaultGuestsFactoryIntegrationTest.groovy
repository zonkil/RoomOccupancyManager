package com.zonkil.roomoccupancymanager.domain

import com.zonkil.roomoccupancymanager.service.EmptyPremiumRoomUpgradeService
import spock.lang.Specification
import spock.lang.Subject

class DefaultGuestsFactoryIntegrationTest extends Specification {

    @Subject
    DefaultGuestsFactory defaultGuestsFactory

    void setup() {
        def threshold = 100.0
        defaultGuestsFactory = new DefaultGuestsFactory(new EmptyPremiumRoomUpgradeService(threshold))
    }

    def "testGuestsCreation"() {
        when:
        def guests = defaultGuestsFactory.createGuests(allGuests)

        then:
        guests != null
        with(guests) {
            premiumGuests == expectedPremiumGuests.collect { g -> new Guest(g) }
            economyGuests == expectedEconomyGuests.collect { g -> new Guest(g) }
        }

        where:
        allGuests                | expectedPremiumGuests | expectedEconomyGuests
        []                       | []                    | []
        [1.0, 2.0]               | []                    | [2.0, 1.0]
        [111.0, 222.0]           | [222.0, 111.0]        | []
        [1.0, 111.0, 222.0, 2.0] | [222.0, 111.0]        | [2.0, 1.0]
        [99.99, 100.0, 100.1]    | [100.1, 100.0]        | [99.99]
    }
}
