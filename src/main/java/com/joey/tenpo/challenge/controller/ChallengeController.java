package com.joey.tenpo.challenge.controller;

import com.joey.tenpo.challenge.dto.HistoryDTO;
import com.joey.tenpo.challenge.service.ComputeService;
import com.joey.tenpo.challenge.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ChallengeController {

    private final HistoryService historyService;
    private final ComputeService computeService;

    @Operation(summary = "Compute two numbers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping(value = "/compute/{num1}/{num2}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BigDecimal> compute(@PathVariable int num1, @PathVariable int num2) throws Exception {
        return new ResponseEntity<>(computeService.compute(num1, num2), HttpStatus.OK);
    }

    @Operation(summary = "Get history data paged")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping(value = "/history/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<HistoryDTO>> filterHistory(@SortDefault(sort = "creationDate")
                                                          @PageableDefault(size = 5) Pageable pageable) {
        return new ResponseEntity<>(historyService.filterHistory(pageable), HttpStatus.OK);
    }

}
