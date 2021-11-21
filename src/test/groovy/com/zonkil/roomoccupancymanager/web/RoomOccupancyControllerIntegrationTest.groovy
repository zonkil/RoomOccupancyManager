package com.zonkil.roomoccupancymanager.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class RoomOccupancyControllerIntegrationTest extends Specification {

    @Autowired
    MockMvc mockMvc

    def "should execute calculation twice (test if DataProvider works correctly)"() {
        given:
        def numPrem = 3
        def numEcon = 3
        def guests = "23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0"
        def url = "/occupancy?numberOfPremiumRooms=${numPrem}&numberOfEconomyRooms=${numEcon}&guests=${guests}".toString()
        when:
        def result = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn()

        then:
        result.getResponse().getContentAsString() == "{\"premiumOccupancy\":3,\"premiumProfit\":738.0,\"economyOccupancy\":3,\"economyProfit\":167.99}"

        when:
        numPrem = 3
        numEcon = 3
        guests = "1.0,2.0,3.0"
        def url2 = "/occupancy?numberOfPremiumRooms=${numPrem}&numberOfEconomyRooms=${numEcon}&guests=${guests}".toString()

        def result2 = mockMvc.perform(get(url2)).andExpect(status().isOk()).andReturn()

        then:
        result2.getResponse().getContentAsString() == "{\"premiumOccupancy\":0,\"premiumProfit\":0,\"economyOccupancy\":3,\"economyProfit\":6.0}"
    }

    def "should execute calculation twice with and without guest parameter"() {
        given:
        def numPrem = 3
        def numEcon = 3
        def url = "/occupancy?numberOfPremiumRooms=${numPrem}&numberOfEconomyRooms=${numEcon}".toString()
        when:
        def result = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn()

        then:
        result.getResponse().getContentAsString() == "{\"premiumOccupancy\":3,\"premiumProfit\":738.00,\"economyOccupancy\":3,\"economyProfit\":167.99}"

        when:
        numPrem = 3
        numEcon = 3
        def guests = "1.0,2.0,3.0"
        def url2 = "/occupancy?numberOfPremiumRooms=${numPrem}&numberOfEconomyRooms=${numEcon}&guests=${guests}".toString()

        def result2 = mockMvc.perform(get(url2)).andExpect(status().isOk()).andReturn()

        then:
        result2.getResponse().getContentAsString() == "{\"premiumOccupancy\":0,\"premiumProfit\":0,\"economyOccupancy\":3,\"economyProfit\":6.0}"
    }
}
