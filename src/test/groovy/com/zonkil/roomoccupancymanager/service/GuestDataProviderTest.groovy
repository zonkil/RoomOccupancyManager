package com.zonkil.roomoccupancymanager.service

import spock.lang.Specification
import spock.lang.Subject

@Subject(GuestDataProvider.class)
class GuestDataProviderTest extends Specification {

    def "should throw exception when initialized with null"() {
        given:
        def guestDataProvider = new GuestDataProvider()
        when:
        guestDataProvider.initialize(null)
        then:
        thrown(NullPointerException.class)
    }

    def "should initalize"() {
        given:
        def guestDataProvider = new GuestDataProvider()
        when:
        guestDataProvider.initialize(Collections.emptyList())
        then:
        notThrown(Exception.class)
    }

    def "should throw exception when initialized twice"() {
        given:
        def guestDataProvider = new GuestDataProvider()
        when:
        guestDataProvider.initialize(Collections.emptyList())
        guestDataProvider.initialize(Collections.emptyList())
        then:
        thrown(IllegalStateException.class)
    }

    def "should throw exception when used when not initialized"() {
        given:
        def guestDataProvider = new GuestDataProvider()
        when:
        guestDataProvider.getGuestData()
        then:
        thrown(IllegalStateException.class)
    }

    def "should provide data when initialized"() {
        given:
        def guestDataProvider = new GuestDataProvider()
        guestDataProvider.initialize(List.of(BigDecimal.ONE))
        when:
        def providedData = guestDataProvider.getGuestData()
        then:
        providedData.size() == 1
        providedData.get(0) == BigDecimal.ONE
    }
}
