package com.zonkil.roomoccupancymanager.service

import com.zonkil.roomoccupancymanager.domain.AvailableRooms
import spock.lang.Specification
import spock.lang.Subject

class DefaultRoomOccupancyServiceIntegrationTest extends Specification {

    @Subject
    DefaultRoomOccupancyService defaultRoomOccupancyService
    GuestDataProvider guestDataProvider
    DataProviderGuestService dataProviderGuestService

    void setup() {
        guestDataProvider = new GuestDataProvider()
        dataProviderGuestService = new DataProviderGuestService(guestDataProvider)
        defaultRoomOccupancyService = new DefaultRoomOccupancyService(dataProviderGuestService)
    }

    def "shouldCalculateRoomOccupancy"() {
        given:
        guestDataProvider.initialize(allGuests)
        def availableRooms = AvailableRooms.of(numPremium, numEconomy)
        when:
        def calculation = defaultRoomOccupancyService.calculateRoomOccupancy(availableRooms)

        then:
        calculation != null
        with(calculation) {
            premiumOccupancy == premOcc
            premiumTotalMoney == premMoney
            economyOccupancy == ecoOcc
            economyTotalMoney == ecoMonney
        }


        where:
        numPremium | numEconomy | allGuests                                                           | premOcc | premMoney | ecoOcc | ecoMonney
        3          | 3          | [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0] | 3       | 738.0     | 3      | 167.99
        7          | 5          | [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0] | 6       | 1054.0    | 4      | 189.99
        2          | 7          | [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0] | 2       | 583.0     | 4      | 189.99
        //I believe there is a bug in test data set, .99 should be in total premium money not in economy
        7          | 1          | [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0] | 7       | 1153.99   | 1      | 45.0
        0          | 0          | [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0] | 0       | 0         | 0      | 0
        1          | 1          | []                                                                  | 0       | 0         | 0      | 0
        1          | 0          | [150.0, 120.0, 5.0, 1.0]                                            | 1       | 150.0     | 0      | 0
        2          | 0          | [150.0, 5.0, 1.0]                                                   | 2       | 155.0     | 0      | 0
        3          | 0          | [150.0, 5.0, 1.0]                                                   | 3       | 156.0     | 0      | 0
        1          | 1          | [150.0, 120.0, 5.0, 1.0]                                            | 1       | 150.0     | 1      | 5.0
        0          | 1          | [150.0, 120.0, 5.0, 1.0]                                            | 0       | 0         | 1      | 5.0
        0          | 2          | [150.0, 120.0, 5.0, 1.0]                                            | 0       | 0         | 2      | 6.0
    }
}
