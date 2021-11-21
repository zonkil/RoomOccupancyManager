package com.zonkil.roomoccupancymanager.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
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
        def result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$.premiumOccupancy").value(3))
                .andExpect(jsonPath("\$.premiumProfit").value(738.0))
                .andExpect(jsonPath("\$.economyOccupancy").value(3))
                .andExpect(jsonPath("\$.economyProfit").value(167.99))
                .andReturn()

        then:
        result != null

        when:
        numPrem = 3
        numEcon = 3
        guests = "1.0,2.0,3.0"
        def url2 = "/occupancy?numberOfPremiumRooms=${numPrem}&numberOfEconomyRooms=${numEcon}&guests=${guests}".toString()

        def result2 = mockMvc.perform(get(url2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$.premiumOccupancy").value(0))
                .andExpect(jsonPath("\$.premiumProfit").value(0))
                .andExpect(jsonPath("\$.economyOccupancy").value(3))
                .andExpect(jsonPath("\$.economyProfit").value(6.0))
                .andReturn()

        then:
        result2 != null
    }

    def "should execute calculation twice with and without guest parameter"() {
        given:
        def numPrem = 3
        def numEcon = 3
        def url = "/occupancy?numberOfPremiumRooms=${numPrem}&numberOfEconomyRooms=${numEcon}".toString()
        when:
        def result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$.premiumOccupancy").value(3))
                .andExpect(jsonPath("\$.premiumProfit").value(738.0))
                .andExpect(jsonPath("\$.economyOccupancy").value(3))
                .andExpect(jsonPath("\$.economyProfit").value(167.99))
                .andReturn()

        then:
        result != null

        when:
        numPrem = 3
        numEcon = 3
        def guests = "1.0,2.0,3.0"
        def url2 = "/occupancy?numberOfPremiumRooms=${numPrem}&numberOfEconomyRooms=${numEcon}&guests=${guests}".toString()

        def result2 = mockMvc.perform(get(url2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$.premiumOccupancy").value(0))
                .andExpect(jsonPath("\$.premiumProfit").value(0))
                .andExpect(jsonPath("\$.economyOccupancy").value(3))
                .andExpect(jsonPath("\$.economyProfit").value(6.0))
                .andReturn()

        then:
        result2 != null
    }
}
