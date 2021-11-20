package com.zonkil.roomoccupancymanager.config

import com.zonkil.roomoccupancymanager.persistance.repositories.GuestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
@AutoConfigureTestDatabase
class DbInitializationTest extends Specification {

    @Autowired
    GuestRepository guestRepository

    def "testIfDBIsInitialized"() {
        //full spring context should be running so PostConstruct method in DbInitialization class will be executed
        when:
        def count = guestRepository.count()

        then:
        count == 10
    }
}
