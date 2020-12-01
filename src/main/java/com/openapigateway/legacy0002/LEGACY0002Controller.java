package com.openapigateway.legacy0002;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LEGACY0002Controller {

    @Autowired
    private LEGACY0002Service legacy0002Service;

    @RequestMapping(value = "/LEGACY0002/", method = GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getBalances() {
        log.debug("getCredits {}", "start");

        String credits = legacy0002Service.getBalances();

        return new ResponseEntity<>(credits, HttpStatus.OK);
    }

    @RequestMapping(value = "/LEGACY0002/{userId}", method = GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getBalanceByPk(@PathVariable String userId) {
        log.debug("getCreditByPk {} {}", "start", userId);

        String credits = legacy0002Service.getBalanceByPk(userId);

        return new ResponseEntity<>(credits, HttpStatus.OK);
    }

}
