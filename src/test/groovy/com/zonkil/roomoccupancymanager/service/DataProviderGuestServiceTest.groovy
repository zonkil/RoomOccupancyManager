package com.zonkil.roomoccupancymanager.service

import com.zonkil.roomoccupancymanager.service.v2.GuestType
import spock.lang.Specification
import spock.lang.Subject

@Subject(DataProviderGuestService.class)
class DataProviderGuestServiceTest extends Specification {

    GuestDataProvider guestDataProvider = Mock()

    def "testCountingGuests"() {
        given:
        guestDataProvider.getGuestData() >> allGuests
        def dataProviderGuestService = new DataProviderGuestService(guestDataProvider)
        when:
        def premCount = dataProviderGuestService.countGuests(GuestType.PREMIUM)
        def econCount = dataProviderGuestService.countGuests(GuestType.ECONOMY)
        then:
        premCount == expectedPremiumGuestCount
        econCount == expectedEconomyGuestCount

        where:

        allGuests                                                           | expectedPremiumGuestCount | expectedEconomyGuestCount
        [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0] | 6                         | 4
        []                                                                  | 0                         | 0
        [150.0, 120.0, 5.0, 1.0]                                            | 2                         | 2
        [150.0, 5.0, 1.0]                                                   | 1                         | 2
    }

    def "testCalculateProfit"() {
        given:
        guestDataProvider.getGuestData() >> allGuests
        def dataProviderGuestService = new DataProviderGuestService(guestDataProvider)
        when:
        def premProfit = dataProviderGuestService.calculateProfit(GuestType.PREMIUM, limitPrem)
        def econProfit = dataProviderGuestService.calculateProfit(GuestType.ECONOMY, limitEco)
        then:
        premProfit == expectedPremiumProfit
        econProfit == expectedEconomyProfit

        where:

        allGuests                                                           | limitPrem | limitEco | expectedPremiumProfit | expectedEconomyProfit
        [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0] | 1         | 1        | 374.0                 | 99.99
        [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0] | 2         | 2        | 583.0                 | 144.99
        [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0] | 3         | 3        | 738.0                 | 167.99
        [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0] | 4         | 4        | 853.0                 | 189.99
        [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0] | 5         | 5        | 954.0                 | 189.99
        [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0] | 6         | 6        | 1054                  | 189.99
        [23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0] | 7         | 7        | 1054                  | 189.99
        []                                                                  | 0         | 0        | 0                     | 0
    }
}
