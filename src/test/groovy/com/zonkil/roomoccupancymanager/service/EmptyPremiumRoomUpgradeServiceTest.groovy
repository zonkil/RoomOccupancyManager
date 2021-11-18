package com.zonkil.roomoccupancymanager.service

import com.zonkil.roomoccupancymanager.domain.AvailableRooms
import com.zonkil.roomoccupancymanager.domain.Guest
import com.zonkil.roomoccupancymanager.domain.Guests
import spock.lang.Specification
import spock.lang.Subject

class EmptyPremiumRoomUpgradeServiceTest extends Specification {

    @Subject
    EmptyPremiumRoomUpgradeService emptyPremiumRoomUpgradeService

    void setup() {
        emptyPremiumRoomUpgradeService = new EmptyPremiumRoomUpgradeService()
    }

    def "testIfUpgradeHappened"() {
        when:
        def guestAfterPromotion = emptyPremiumRoomUpgradeService.upgradeGuestsIfNeeded(rooms, guests)

        then:
        guestAfterPromotion != null
        with(guestAfterPromotion) {
            premiumGuestsNumber == expectedPremiumGuestsCount
            economyGuestsNumber == expectedEconomyGuestsCount
        }

        where:
        rooms                   | guests                                                                     | expectedPremiumGuestsCount | expectedEconomyGuestsCount
        AvailableRooms.of(0, 2) | Guests.ofAllGuests([150.0, 1.0, 2.0, 3.0].collect { it -> new Guest(it) }) | 1                          | 3
        AvailableRooms.of(2, 4) | Guests.ofAllGuests([150.0, 1.0, 2.0, 3.0].collect { it -> new Guest(it) }) | 1                          | 3
        AvailableRooms.of(2, 2) | Guests.ofAllGuests([150.0, 1.0, 2.0, 3.0].collect { it -> new Guest(it) }) | 2                          | 2
    }
}
