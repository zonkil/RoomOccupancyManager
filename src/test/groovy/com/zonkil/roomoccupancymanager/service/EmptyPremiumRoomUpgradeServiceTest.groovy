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
        def threshold = 100.0
        emptyPremiumRoomUpgradeService = new EmptyPremiumRoomUpgradeService(threshold)
    }

    def "testIfUpgradeHappened"() {
        given:
        def guests = new Guests(premiumGuests.collect { new Guest(it) }, economyGuests.collect { new Guest(it) })
        when:
        def guestAfterPromotion = emptyPremiumRoomUpgradeService.upgradeGuestsIfNeeded(rooms, guests)

        then:
        guestAfterPromotion != null
        with(guestAfterPromotion) {
            premiumGuestsNumber == expectedPremiumGuestsCount
            economyGuestsNumber == expectedEconomyGuestsCount
        }

        where:
        rooms                   | premiumGuests | economyGuests   | expectedPremiumGuestsCount | expectedEconomyGuestsCount
        AvailableRooms.of(0, 2) | [150.0]       | [1.0, 2.0, 3.0] | 1                          | 3
        AvailableRooms.of(2, 4) | [150.0]       | [1.0, 2.0, 3.0] | 1                          | 3
        AvailableRooms.of(2, 2) | [150.0]       | [1.0, 2.0, 3.0] | 2                          | 2
    }

    def "testIfGuestIsPremium"() {
        when:
        def isPremium = emptyPremiumRoomUpgradeService.isPremiumGuest(new Guest(guestMoney))

        then:
        isPremium == expectedPremiumGuest

        where:
        guestMoney | expectedPremiumGuest
        0.0        | false
        99.99      | false
        100        | true
        100.01     | true
    }
}
