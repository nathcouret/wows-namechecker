package eu.ncouret.wows.namecheckers.controller

import eu.ncouret.wows.namecheckers.business.PlayersBusiness
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/players")
class PlayersController(val playersBusiness: PlayersBusiness) {

    @GetMapping
    suspend fun getPlayerByName(@RequestParam name: String, @RequestParam(required = false, defaultValue = "false") exact: Boolean) =
            playersBusiness.searchPlayer(name, exact)

    @GetMapping("/name/{name}")
    suspend fun getPlayerLocalFirst(@PathVariable name: String) =
            playersBusiness.searchName(name)

    @GetMapping("/history/{accountId}")
    suspend fun getPlayerHistory(@PathVariable accountId: Long) =
            playersBusiness.findPlayerHistory(accountId)
}